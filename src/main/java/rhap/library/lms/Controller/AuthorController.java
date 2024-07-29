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
import java.util.Optional;

@RestController
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthService authService;

    // READ
    @GetMapping("")
    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    // READ
    @GetMapping("/{id}")
    public ResponseEntity<Object> getAuthor(@PathVariable Long id) {
        Optional<Author> author = authorRepository.findById(id);
        if(author.isPresent()) {
            return ResponseEntity.ok(author);
        }
        return ResponseEntity.status(404).body("{\"Message\":\"Author Not Found\"}");
    }

    // CREATE
    @PostMapping("")
    public ResponseEntity<Object> addAuthor(@RequestHeader(value = "Auth", required = false) String AuthHeader,@RequestBody AuthorDto authorDto) {
        Admin admin = authService.isAdmin(AuthHeader);
        if(admin == null){
            return ResponseEntity.status(403).body("{\"Message\":\"Unauthorized\"}");
        }
        return authorService.addAuthor(authorDto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAuthor(@RequestHeader(value = "Auth", required = false) String AuthHeader,@PathVariable long id, @RequestBody AuthorDto authorDto) {
        Admin admin = authService.isAdmin(AuthHeader);
        if(admin == null){
            return ResponseEntity.status(403).body("{\"Message\":\"Unauthorized\"}");
        }
        return authorService.editAuthor(id,authorDto);
    }
}
