package park.serverModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ForumInfo {

    private String title;
    private String user;
    private String slug;
    private int posts;
    private int threads;

    public ForumInfo() {
    }

    @JsonCreator
    public ForumInfo(@JsonProperty("title") String title,
                     @JsonProperty("user") String user,
                     @JsonProperty("slug") String slug,
                     @JsonProperty("posts") int posts,
                     @JsonProperty("threads") int threads) {
        this.posts = posts;
        this.user = user;
        this.slug = slug;
        this.threads = threads;
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public int getPosts() {
        return posts;
    }

    public String getSlug() {
        return slug;
    }

    public int getThreads() {
        return threads;
    }

    public String getUser() {
        return user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    @Override
    public String toString() {
        return "[" + title + "," + user + "," + slug + "," + posts + "," + threads + "]";
    }
}
