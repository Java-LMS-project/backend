package rhap.library.lms.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rhap.library.lms.Dto.AdminDto;
import rhap.library.lms.Model.Admin;
import rhap.library.lms.Service.AdminService;
import rhap.library.lms.Service.AuthService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private AuthService authService;

    @PostMapping("")
    public ResponseEntity<?> add(@RequestHeader(value = "Auth", required = false) String AuthHeader, @RequestBody AdminDto adminDto) {
        Admin admin = authService.isAdmin(AuthHeader);
        if(admin == null){
            return ResponseEntity.status(403).body("{\"Message\":\"Unauthorized\"}");
        }
        return adminService.addAdmin(adminDto);
    }

}
