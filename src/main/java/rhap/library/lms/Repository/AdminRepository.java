package rhap.library.lms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rhap.library.lms.Model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    public Admin findByEmail(String email);
    public Admin findByKeyValue(String keyValue);
}
