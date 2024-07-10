package rhap.library.lms.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rhap.library.lms.Dto.LoginDto;
import rhap.library.lms.Service.AuthService;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/auth/admin")
    public ResponseEntity<Object> adminLogin(@RequestBody LoginDto loginDto) {
        return authService.adminLogin(loginDto);
    }


}
