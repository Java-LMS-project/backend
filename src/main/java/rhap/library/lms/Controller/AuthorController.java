package rhap.library.lms.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rhap.library.lms.Dto.AuthorDto;
import rhap.library.lms.Model.Admin;
import rhap.library.lms.Model.Author;
import rhap.library.lms.Repository.AuthorRepository;
import rhap.library.lms.Service.AuthService;
import rhap.library.lms.Service.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthService authService;

    @GetMapping("")
    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addAuthor(@RequestHeader(value = "Auth", required = false) String AuthHeader,@RequestBody AuthorDto authorDto) {
        Admin admin = authService.isAdmin(AuthHeader);
        if(admin == null){
            return ResponseEntity.status(403).body("{\"Message\":\"Unauthorized\"}");
        }
        authorService.addAuthor(authorDto);
        return ResponseEntity.ok("{\"Message\":\"Author added\"}");
    }
}
