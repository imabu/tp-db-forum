package park.serverModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class ThreadInfo {
    private int id;
    private String title;
    private String author;
    private String forum;
    private String message;
    private int votes;
    private String slug;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Timestamp created;
    //private String created;
    public ThreadInfo() {
    }

    @JsonCreator
    public ThreadInfo(@JsonProperty("id") int id,
                      @JsonProperty("title") String title,
                      @JsonProperty("author") String author,
                      @JsonProperty("forum") String forum,
                      @JsonProperty("message") String message,
                      @JsonProperty("slug") String slug,
                      @JsonProperty("votes") int votes,
                      @JsonProperty("created") Timestamp created) {
        this.id = id;
        this.author = author;
        this.created = created;
        this.forum = forum;
        this.message = message;
        this.slug = slug;
        this.title=title;
        this.votes=votes;
    }

    public String getTitle() {
        return this.title;
    }

    public int getId() {
        return id;
    }

    public int getVotes() {
        return votes;
    }

    public String getAuthor() {
        return author;
    }

    public Timestamp getCreated() {
        return created;
    }

    public String getForum() {
        return forum;
    }

    public String getMessage() {
        return message;
    }

    public String getSlug() {
        return slug;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public void setForum(String forum) {
        this.forum = forum;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
