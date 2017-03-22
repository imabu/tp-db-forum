package park.serverModel;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfo {
    private String nickname;
    private String email;
    private String fullname;
    private String about;
    public UserInfo(){}
    @JsonCreator
    public UserInfo(@JsonProperty("nickname") String nickname,
                    @JsonProperty("email") String email,
                    @JsonProperty("fullname") String fullname,
                    @JsonProperty("about") String about){
        this.about=about;
        this.email=email;
        this.fullname=fullname;
        this.nickname=nickname;
    }

    public String getNickname(){return this.nickname;}

    public String getAbout() {
        return about;
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
