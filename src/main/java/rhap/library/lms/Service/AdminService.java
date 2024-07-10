package rhap.library.lms.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rhap.library.lms.Dto.AdminDto;
import rhap.library.lms.Model.Admin;
import rhap.library.lms.Repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    // Adding Admin Account
    public ResponseEntity<?> addAdmin(AdminDto adminDto) {
        // Check if user input is valid
        if(adminDto.getEmail() == null || adminDto.getPassword() == null) {
            return ResponseEntity.badRequest().body("{\"Message\":\"Email or Password can't be Empty.\"}");
        }

        // check duplicate email
        if(adminRepository.findByEmail(adminDto.getEmail())!=null){
            return ResponseEntity.badRequest().body("{\"Message\":\"Email Already Exists.\"}");
        }

        // add admin info into database
        Admin admin = new Admin(adminDto.getEmail(), adminDto.getPassword());
        adminRepository.save(admin);

        return ResponseEntity.ok().body("{\"Message\":\"Successfully Added Admin.\"}");
    }
}
