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

@Service
public class BookService {

    private final String storageDir = "static/img";
    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

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
        return ResponseEntity.status(201).body(book);
    }

    // READ
    public List<Book> getBooks(){
        return bookRepository.findAll();
    }


    // UPDATE
    public ResponseEntity<Object> editBook(BookDto bookDto) {
        Book book = bookRepository.findByTitle(bookDto.getTitle());

        //check if the book is valid
        if(book == null){
            return ResponseEntity.status(404).body("{\"message\":\"Book Not Found\"}");
        }

        //check if user want to change author
        if(bookDto.getNew_author() != null){
            // check if the author is valid
            Author author = authorService.getAuthor(bookDto.getNew_author());
            if(author == null){
                return ResponseEntity.status(404).body("{\"message\":\"Author Not Found\"}");
            }
            book.setAuthor(author);
        }

        // check if user entered the new title
        if(bookDto.getNew_title() == null){
            return ResponseEntity.status(404).body("{\"message\":\"Please Enter The New Title\"}");
        }

        updateField(bookDto.getNew_title(), book::setTitle);
        bookRepository.save(book);
        return ResponseEntity.status(201).body(book);
    }

    // DELETE
    public ResponseEntity<Object> deleteBook(BookDto bookDto){

        // Check if User Enter Title
        if(bookDto.getTitle() == null){
            return ResponseEntity.status(404).body("{\"message\":\"Please Enter The Title\"}");
        }


        // Find Book
        Book book = bookRepository.findByTitle(bookDto.getTitle());
        if(book == null){
            return ResponseEntity.status(404).body("{\"message\":\"Book Not Found\"}");
        }
        bookRepository.delete(book);
        return ResponseEntity.ok("{\"message\":\"Book Deleted\"}");
    }



    private String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty() || !isValidImageExtension(file.getOriginalFilename())) {
            throw new IllegalArgumentException("Invalid file extension. Only jpg, jpeg, or gif are allowed.");
        }

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

    public ResponseEntity<Object> borrowBook(long id, User user){
        Book book = bookRepository.findById(id);
        if(book == null){
            return ResponseEntity.status(404).body("{\"Message\":\"Book Not Found\"}");
        }
        if(!book.isAvailable()){
            return ResponseEntity.status(404).body("{\"Message\":\"Book is not available\"}");
        }

        // Calculate the return date (7 days from now)
        LocalDate returnDate = LocalDate.now().plusDays(7);

        user.setSlot(user.getSlot() - 1);
        book.setAvailable(false);
        book.setUserEmail(user.getEmail());
        book.setReturnDate(returnDate);

        bookRepository.save(book);
        return ResponseEntity.ok(book);
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

        return ResponseEntity.ok(response);

    }
}


