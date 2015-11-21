package net.saltfactory.tutorial;

import java.io.Serializable;
import java.util.List;

/**
 * Created by saltfactory<saltfactory@gmail.com> on 11/21/15.
 */

public class Article implements Serializable {
    private long id;
    private String title;
    private String content;
    private List<Comment> comments;


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
