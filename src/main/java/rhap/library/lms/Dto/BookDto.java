package rhap.library.lms.Dto;

import org.springframework.web.multipart.MultipartFile;

public class BookDto {

    private String title;
    private String author;
    private String description;
    private MultipartFile cover_img;
    private String new_title;
    private String new_author;
    private String is_available;

    public MultipartFile getCover_img() {
        return cover_img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCover_img(MultipartFile cover_img) {
        this.cover_img = cover_img;
    }

    public String getIs_available() {
        return is_available;
    }

    public void setIs_available(String is_available) {
        this.is_available = is_available;
    }

    public String getNew_author() {
        return new_author;
    }

    public void setNew_author(String new_author) {
        this.new_author = new_author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public String getNew_title() {
        return new_title;
    }

    public void setNew_title(String new_title) {
        this.new_title = new_title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
