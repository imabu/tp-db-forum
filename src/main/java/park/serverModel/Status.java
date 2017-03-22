package park.serverModel;


public class Status {
    private int forum;
    private int thread;
    private int post;
    private int user;

    public Status(int forum, int thread, int post, int user) {
        this.forum = forum;
        this.post = post;
        this.user = user;
        this.thread = thread;
    }

    public int getForum() {
        return forum;
    }

    public int getPost() {
        return post;
    }

    public int getThread() {
        return thread;
    }

    public int getUser() {
        return user;
    }
}
