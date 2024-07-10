package rhap.library.lms.Dto;

public class UserDto {
    private String fullName;
    private String email;
    private String password;
    private Long fees;
    private String role;
    private String bookTitle;

    public UserDto() {
    }

    public UserDto(String bookTitle, Long fees, String email, String password, String fullName, String role) {
        this.bookTitle = bookTitle;
        this.fees = fees;
        this.email = email;
        this.role = role;
        this.password = password;
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Long getFees() {
        return fees;
    }

    public void setFees(Long fees) {
        this.fees = fees;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
