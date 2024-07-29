package rhap.library.lms.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rhap.library.lms.Dto.AuthorDto;
import rhap.library.lms.Model.Author;
import rhap.library.lms.Repository.AuthorRepository;

import java.util.Objects;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    // All author name
    public Author getAuthor(String name){
        return authorRepository.findByName(name);
    }

    // Adding an author
    public ResponseEntity<Object> addAuthor(AuthorDto authorDto){
        if(Objects.equals(authorDto.getName(), "") || authorDto.getName() == null) {
            return ResponseEntity.status(403).body("Author name cannot be empty");
        }
        if(authorRepository.findByName(authorDto.getName()) != null){
            return ResponseEntity.status(403).body("Author already exists");
        }
        Author author = new Author(authorDto.getName());
        authorRepository.save(author);
        return ResponseEntity.ok("{\"Message\":\"Author added\"}");
    }

    public ResponseEntity<Object> editAuthor(long id, AuthorDto authorDto) {
        Author author = authorRepository.findById(id);
        if( author == null) {
            return ResponseEntity.status(403).body("{\"Message\":\"Author not found\"}");
        }
        author.setName(authorDto.getName());
        authorRepository.save(author);
        return ResponseEntity.ok("{\"Message\":\"Author Updated\"}");
    }
}
