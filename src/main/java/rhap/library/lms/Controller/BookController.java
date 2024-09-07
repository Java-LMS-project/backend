package rhap.library.lms.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rhap.library.lms.Dto.BookDto;
import rhap.library.lms.Model.Admin;
import rhap.library.lms.Model.Book;
import rhap.library.lms.Model.User;
import rhap.library.lms.Service.AuthService;
import rhap.library.lms.Service.BookService;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthService authService;



    private Book book;

    @GetMapping("")
    public List<Book> index(@RequestParam(value = "q", required = false) String query) {
        if (query == null || query.isEmpty()) {
            return bookService.getAllBooks();
        }
        return bookService.getBooks(query);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBook(@PathVariable long id) {
        return bookService.getBookById(id);
    }

    @PostMapping("")
    public ResponseEntity<Object> addBook(@RequestHeader(value = "Auth", required = false) String AuthHeader,
                                          @RequestParam("title") String title,
                                          @RequestParam("author") String author,
                                          @RequestParam(value = "description", required = false) String description,
                                          @RequestParam(value = "image", required = false) MultipartFile image) {

        // Check if you are admin
        Admin admin = authService.isAdmin(AuthHeader);
        if(admin == null){
            return ResponseEntity.status(403).body("{\"Message\":\"Unauthorized\"}");
        }

        BookDto bookDto = new BookDto();
        bookDto.setTitle(title);
        bookDto.setAuthor(author);
        bookDto.setDescription(description);
        bookDto.setCover_img(image);
        return bookService.addBook(bookDto);
    };


    @PutMapping("/{id}")
    public ResponseEntity<Object> editBook(@RequestHeader(value = "Auth", required = false) String AuthHeader,
                                           @PathVariable long id,
                                           @RequestParam("title") String title,
                                           @RequestParam("author") String author,
                                           @RequestParam("description") String description) {
        Admin admin = authService.isAdmin(AuthHeader);
        if(admin == null){
            return ResponseEntity.status(403).body("{\"Message\":\"Unauthorized\"}");
        }
        BookDto bookDto = new BookDto();
        bookDto.setTitle(title);
        bookDto.setAuthor(author);
        bookDto.setDescription(description);
        return bookService.editBook(id, bookDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@RequestHeader(value = "Auth", required = false) String AuthHeader,@PathVariable long id) {
        Admin admin = authService.isAdmin(AuthHeader);
        if(admin == null){
            return ResponseEntity.status(403).body("{\"Message\":\"Unauthorized\"}");
        }
        return bookService.deleteBook(id);
    }



    @PostMapping("/borrow/{id}")
    public ResponseEntity<Object> borrowBook(
            @RequestHeader(value = "Auth", required = false) String authHeader,
            @PathVariable long id) {

        User user = authService.isAuthenticated(authHeader);
        if (user != null) {
            if (user.getSlot() == 0) {
                return ResponseEntity.status(403).body("{\"Message\":\"Your Slot is Fulled\"}");
            }
            return bookService.borrowBook(id, user);
        }
        return ResponseEntity.status(403).body("{\"Message\":\"Unauthorized\"}");
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<Object> returnBook(@RequestHeader(value = "Auth", required = false) String AuthHeader,@PathVariable long id) {

        User user = authService.isAuthenticated(AuthHeader);
        if(user != null){
            return bookService.returnBook(id, user);
        }
        return ResponseEntity.status(403).body("{\"Message\":\"Unauthorized\"}");
    }


}
