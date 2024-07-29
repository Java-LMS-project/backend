package rhap.library.lms.Dto;

import org.springframework.web.multipart.MultipartFile;

public class BookDto {

    private String title;
    private String author;
    private String description;
    private MultipartFile cover_img;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }


    public void setAuthor(String author) {
        this.author = author;
    }
}
