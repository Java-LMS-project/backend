package rhap.library.lms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rhap.library.lms.Dto.UserDto;
import rhap.library.lms.Repository.BookRepository;
import rhap.library.lms.Repository.UserRepository;
import rhap.library.lms.Service.UserService;



@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;



    @PostMapping("")
    public ResponseEntity<Object> register(@RequestBody UserDto userDto){
        return userService.registerUser(userDto);
    }
}
