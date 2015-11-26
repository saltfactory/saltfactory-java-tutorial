package net.saltfactory.tutorial;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by saltfactory<saltfactory@gmail.com> on 11/21/15.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Article implements Serializable {
    private long id;
    private String title;
    private String content;
    private List<Comment> comments;

    @JsonIgnore
    private MultipartFile file;

    @JsonProperty("file")
    private String fileName;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.file == null ? null : this.file.getOriginalFilename();
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
