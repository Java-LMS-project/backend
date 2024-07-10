package rhap.library.lms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rhap.library.lms.Model.Book;
import rhap.library.lms.Model.User;
import rhap.library.lms.Repository.BookRepository;
import rhap.library.lms.Repository.UserRepository;
import rhap.library.lms.Service.AuthService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @GetMapping("")
    public ResponseEntity<?> index(@RequestHeader(value = "Auth", required = false) String AuthHeader){
        User user = authService.isAuthenticated(AuthHeader);
        if(user != null){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(403).body("{\"Message\":\"Unauthorized\"}");
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBorrowedList(@RequestHeader(value = "Auth", required = false) String AuthHeader) {
        User user = authService.isAuthenticated(AuthHeader);
        if(user != null){
            Long id = user.getId();
            Optional<User> userOptional = userRepository.findById(id);
            if(userOptional.isPresent()) {
                return ResponseEntity.ok(bookRepository.findByUserEmail(userOptional.get().getEmail()));
            }
        }
        return ResponseEntity.status(403).body(null);
    }
}
