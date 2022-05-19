package sw2022.gachon.mobileprogramming;

/**
 *  사용자 계정 정보 모델 클래스
 */
public class UserAccount {
    private String idToken;  // firebase Uid (고유 아이디)
    private String name;
    private String emailId;
    private String password;

    private String friendName;
    private String friendEmailId;
    private String friendIdToken;

    public UserAccount() { }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendEmailId() {
        return friendEmailId;
    }

    public void setFriendEmailId(String friendEmailId) {
        this.friendEmailId = friendEmailId;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setFriendIdToken(String friendIdToken) {
        this.friendIdToken = friendIdToken;
    }

    public String getFriendIdToken() { return friendIdToken; }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
