package rhap.library.lms.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rhap.library.lms.Dto.LoginDto;
import rhap.library.lms.Model.Admin;
import rhap.library.lms.Model.User;
import rhap.library.lms.Repository.AdminRepository;
import rhap.library.lms.Repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    // Check user authentication
    public User isAuthenticated(String authKey) {
        if(authKey == null){
            return null;
        } else return userRepository.findBykeyValue(authKey);
    }


    // Check admin authentication
    public Admin isAdmin(String authKey) {
        if(authKey == null){
            return null;
        } else return adminRepository.findByKeyValue(authKey);
    }

    public ResponseEntity<Object> adminLogin(LoginDto loginDto) {
        Admin admin = adminRepository.findByEmail(loginDto.getEmail());
        if(admin == null){
            return ResponseEntity.status(403).body("{\"Message\":\"Invalid username or password\"}");
        }

        boolean isAdmin = admin.getPassword().equals(loginDto.getPassword());
        if(isAdmin){
            return ResponseEntity.ok("{\"key\": \"" + admin.getKeyValue() +"\" }");
        }
        return ResponseEntity.status(403).body("{\"Message\":\"Invalid username or password\"}");

    }



    public ResponseEntity<Object> login(LoginDto loginDto){
        User user = userRepository.findByEmail(loginDto.getEmail());
        if(user == null){
            return ResponseEntity.status(403).body("{\"Message\":\"Invalid username or password\"}");
        }


        boolean auth = user.getPassword().equals(loginDto.getPassword());
        if(auth) {
            return ResponseEntity.ok("{\"key\": \"" + user.getKeyValue() +"\" }");
        }
        return ResponseEntity.status(403).body("{\"Message\":\"Invalid username or password\"}");

    }
}
