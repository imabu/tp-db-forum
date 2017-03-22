package park.serverModel;


import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class PostPage {
    private List<PostInfo> posts;
    private String marker;

    public PostPage(List<PostInfo> posts, String marker) {
        this.posts = posts;
        this.marker = marker;
    }

    public List<PostInfo> getPosts() {
        return posts;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public void setPosts(List<PostInfo> posts) {
        this.posts = posts;
    }
}
