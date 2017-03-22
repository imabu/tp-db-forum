package park.serverModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class PostInfo {
    private int id;
    private int  parent;
    private String author;
    private String message;
    private boolean isEdited;
    private String forum;
    private int thread;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Timestamp created;

    public PostInfo(){}
    @JsonCreator
    public PostInfo(@JsonProperty("id") int id,
                    @JsonProperty("parent") int parent,
                    @JsonProperty("author") String author,
                    @JsonProperty("message") String message,
                    @JsonProperty("isEdited") boolean isEdited,
                    @JsonProperty("forum") String forum,
                    @JsonProperty("thread") int thread,
                    @JsonProperty("created") Timestamp created){
        this.id=id;
        this.parent=parent;
        this.author=author;
        this.message=message;
        this.isEdited =isEdited;
        this.forum=forum;
        this.thread=thread;
        this.created=created;
    }
    public Integer getId(){return this.id;}

    public int getParent() {
        return parent;
    }

    public String getAuthor() {
        return author;
    }

    public int getThread() {
        return thread;
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

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsEdited(boolean edited) {
        isEdited = edited;
    }

    public void setForum(String forum) {
        this.forum = forum;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public boolean getIsEdited() {
        return isEdited;
    }
}
