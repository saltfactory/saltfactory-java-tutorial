package net.saltfactory.tutorial;

import java.io.Serializable;

/**
 * Created by saltfactory<saltfactory@gmail.com> on 11/21/15.
 */
public class Comment implements Serializable {
    private long id;
    private long articleId;
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
