package sw2022.mp.mobileprogramming;

public class FriendData {

    private String friendName;
    private String friendEmailId;
    private String friendIdToken;
    private String imageUri;

    public FriendData(){ }

    public FriendData(String imageUri, String friendName, String friendEmailId) {
        this.imageUri = imageUri;
        this.friendName = friendName;
        this.friendEmailId = friendEmailId;
    }

    public String getImageUri() {
        return imageUri;
    }

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

    public void setFriendIdToken(String friendIdToken) {
        this.friendIdToken = friendIdToken;
    }

    public String getFriendIdToken() { return friendIdToken; }
}