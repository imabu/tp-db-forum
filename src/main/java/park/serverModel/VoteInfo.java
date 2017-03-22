package park.serverModel;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VoteInfo {
    private String nickname;
    private int voice;
    public VoteInfo(){}
    @JsonCreator
    public VoteInfo(@JsonProperty("nickname")String nickname,
                    @JsonProperty("voice") int voice){
        this.nickname=nickname;
        this.voice=voice;
    }

    public int getVoice() {
        return voice;
    }

    public String getNickname() {
        return nickname;
    }
}
