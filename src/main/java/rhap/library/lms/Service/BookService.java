package rhap.library.lms.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rhap.library.lms.Dto.BookDto;
import rhap.library.lms.Model.Author;
import rhap.library.lms.Model.Book;
import rhap.library.lms.Model.User;
import rhap.library.lms.Repository.BookRepository;
import org.apache.commons.codec.digest.DigestUtils;
import rhap.library.lms.Repository.UserRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import com.google.zxing.WriterException;


@Service
public class BookService {

    private final String storageDir = "static/img";
    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private rhap.library.lms.service.QRCodeService qrCodeService;

    public void updateField(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isEmpty()) {
            setter.accept(newValue);
        }
    }

    // CREATE
    public ResponseEntity<Object> addBook(BookDto bookDto){
        //getting author info
        Author author = authorService.getAuthor(bookDto.getAuthor());
        if(author == null){
            return ResponseEntity.status(404).body("{\"message\":\"Author Not Found\"}");
        }

        MultipartFile imageFile  = bookDto.getCover_img();
        String imagePath = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                if (imageFile.isEmpty() || !isValidImageExtension(imageFile.getOriginalFilename())) {
                    return ResponseEntity.status(403).body("{\"message\":\"Invalid file extension. Only jpg, jpeg, svg or gif are allowed.\"}");
                }
                imagePath = uploadImage(imageFile);
            } catch (IOException e) {
                return ResponseEntity.status(500).body("{\"message\":\"Failed to upload image\"}");
            }
        }

        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(author);
        book.setPath(imagePath);
        book.setDescription(bookDto.getDescription());
        book.setAvailable(true);
        bookRepository.save(book);
        return ResponseEntity.status(201).body("{\"message\":\"Book Added\"}");
    }

    // READ
    public List<Book> getAllBooks(){
        List<Book> books = bookRepository.findAll();
        bubbleSort(books);
        return books;
    }

    private void bubbleSort(List<Book> books) {
        int n = books.size();
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                // Compare adjacent books
                if (books.get(j).getTitle().toLowerCase().compareTo(books.get(j + 1).getTitle()) > 0) {
                    // Swap if they are in the wrong order
                    Book temp = books.get(j);
                    books.set(j, books.get(j + 1));
                    books.set(j + 1, temp);
                    swapped = true;
                }
            }
            // If no two elements were swapped, break
            if (!swapped) break;
        }
    }

    // Book Search Using Linear Search Algorithm
    public List<Book> getBooks(String query){
        List<Book> books = bookRepository.findAll();
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    book.getAuthor().getName().toLowerCase().contains(query.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }


    // UPDATE
    public ResponseEntity<Object> editBook(long id,BookDto bookDto) {
        Book book = bookRepository.findById(id);

        //check if the book is valid
        if(book == null){
            return ResponseEntity.status(404).body("{\"message\":\"Book Not Found\"}");
        }

        if(bookDto.getAuthor() != null){
            // check if the author is valid
            Author author = authorService.getAuthor(bookDto.getAuthor());
            if(author == null){
                return ResponseEntity.status(404).body("{\"message\":\"Author Not Found\"}");
            }
            book.setAuthor(author);
        }

        

        // Updating Book Info
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());

        bookRepository.save(book);
        return ResponseEntity.status(201).body("{\"message\":\"Book Updated\"}");
    }

    // DELETE
    public ResponseEntity<Object> deleteBook(long id){

        // Find Book
        Book book = bookRepository.findById(id);
        if(book == null){
            return ResponseEntity.status(404).body("{\"message\":\"Book Not Found\"}");
        }
        bookRepository.delete(book);
        return ResponseEntity.ok("{\"message\":\"Book Deleted\"}");
    }



    private String uploadImage(MultipartFile file) throws IOException {


        // Generate MD5 hash for the filename
        String md5Hex = DigestUtils.md5Hex(file.getInputStream());
        String extension = getFileExtension(file.getOriginalFilename());

        // Create a timestamp
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String newFilename = timestamp + "_" + md5Hex + "." + extension;

        // Save file to storage
        Path storagePath = Paths.get(storageDir);
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }
        Path filePath = storagePath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath);

        return "/static/img/" + newFilename;
    }

    private boolean isValidImageExtension(String filename) {
        String extension = getFileExtension(filename);
        return extension.equalsIgnoreCase("jpg") ||
                extension.equalsIgnoreCase("jpeg") ||
                extension.equalsIgnoreCase("gif") ||
                extension.equalsIgnoreCase("svg") ||
                extension.equalsIgnoreCase("png");
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }


    public ResponseEntity<Object> getBookById(long id) {
        Book book = bookRepository.findById(id);
        if(book == null){
            return ResponseEntity.status(404).body("{\"Message\":\"Book Not Found\"}");
        }
        return ResponseEntity.ok(book);
    }

    public ResponseEntity<Object> borrowBook(long id, User user) {
        Book book = bookRepository.findById(id);
        if (book == null) {
            return ResponseEntity.status(404).body("{\"Message\":\"Book Not Found\"}");
        }
        if (!book.isAvailable()) {
            return ResponseEntity.status(404).body("{\"Message\":\"Book is not available\"}");
        }

        // Calculate the return date (7 days from now)
        LocalDate returnDate = LocalDate.now().plusDays(7);

        user.setSlot(user.getSlot() - 1);
        book.setAvailable(false);
        book.setUserEmail(user.getEmail());
        book.setReturnDate(returnDate);

        bookRepository.save(book);

        // Generate QR Code
        String qrCodeText = String.format("BookID: %s\nTitle: %s\nUser Email: %s\nReturn Date: %s",
                book.getId(),book.getTitle(), book.getUserEmail(), returnDate);

        try {
            byte[] qrCodeBytes = qrCodeService.generateQRCode(qrCodeText);
            return ResponseEntity.ok().header("Content-Type", "image/png").body(qrCodeBytes);
        } catch (WriterException | IOException e) {
            return ResponseEntity.status(500).body("{\"Message\":\"Failed to generate QR code\"}");
        }
    }

    public ResponseEntity<Object> returnBook(long id, User user){
        Book book = bookRepository.findById(id);
        if(book == null){
            return ResponseEntity.status(404).body("{\"Message\":\"Book Not Found\"}");
        }
        if(book.isAvailable()){
            return ResponseEntity.status(201).body("{\"Message\":\"Book has already been returned\"}");
        }
        if(!Objects.equals(book.getUserEmail(), user.getEmail())){
            return ResponseEntity.status(403).body("{\"Message\":\"Unauthorized\"}");
        }

        book.setAvailable(true);
        book.setUserEmail(null);
        book.setReturnDate(null);
        user.setSlot(user.getSlot()+1);
        bookRepository.save(book);

        Map<String, Object> response = new HashMap<>();

        // Calculating if he has crossed the deadline
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), book.getReturnDate());
        if(daysBetween < 0){
            user.setFees( user.getFees() + (daysBetween * -1 * 50));
            userRepository.save(user);
        }
        response.put("book",book);
        response.put("fees",user.getFees());

        return ResponseEntity.ok("{\"Message\":\"Book returned successfully\"}");

    }
}


