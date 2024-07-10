package rhap.library.lms.Dto;


public class AdminDto {
    private String email;
    private String password;

    public AdminDto(){};

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
