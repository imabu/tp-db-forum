package park.serverModel;


public class PostFull {
    private PostInfo post;
    private UserInfo author;
    private ThreadInfo thread;
    private ForumInfo forum;

    public PostFull() {
    }

    public PostFull(PostInfo post, UserInfo author, ThreadInfo thread, ForumInfo forum) {
        this.post = post;
        this.author = author;
        this.forum = forum;
        this.thread = thread;
    }

    public ForumInfo getForum() {
        return forum;
    }

    public PostInfo getPost() {
        return post;
    }

    public ThreadInfo getThread() {
        return thread;
    }

    public UserInfo getAuthor() {
        return author;
    }

    public void setAuthor(UserInfo author) {
        this.author = author;
    }

    public void setForum(ForumInfo forum) {
        this.forum = forum;
    }

    public void setPost(PostInfo post) {
        this.post = post;
    }

    public void setThread(ThreadInfo thread) {
        this.thread = thread;
    }
}
