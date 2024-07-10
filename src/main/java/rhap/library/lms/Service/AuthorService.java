package rhap.library.lms.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rhap.library.lms.Dto.AuthorDto;
import rhap.library.lms.Model.Author;
import rhap.library.lms.Repository.AuthorRepository;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    // All author name
    public Author getAuthor(String name){
        return authorRepository.findByName(name);
    }

    // Adding an author
    public void addAuthor(AuthorDto authorDto){
        Author author = new Author(authorDto.getName());
        authorRepository.save(author);
        return;
    }
}
