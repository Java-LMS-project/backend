package rhap.library.lms.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rhap.library.lms.Dto.UserDto;
import rhap.library.lms.Model.User;
import rhap.library.lms.Repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Object> registerUser(UserDto userDto) {
        if(userDto.getEmail() == null || userDto.getPassword() == null || userDto.getFullName() == null || userDto.getRole() == null) {
            return ResponseEntity.status(403).body("{\"message\":\"Email or Password or Full Name or Role is empty\"}");
        }
        if(userRepository.findByEmail(userDto.getEmail()) != null){
            return ResponseEntity.status(403).body("{\"message\":\"Email already Existed\"}");
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setFullName(userDto.getFullName());
        user.setPhone(null);
        user.setKeyValue();
        user.setFees(0L);
        user.setRole(userDto.getRole());
        switch (userDto.getRole()){
            case "bachelor":
                user.setSlot(3);
            case "master":
                user.setSlot(3);
            case "phd":
                user.setSlot(4);
            case "teacher":
                user.setSlot(5);
            default:
                user.setSlot(2);
        }
        userRepository.save(user);
        return ResponseEntity.ok("{\"message\":\"User Created Successfully\"}");
    }


}
