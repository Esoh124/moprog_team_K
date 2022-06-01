package sw2022.mp.mobileprogramming;

/**
 *  사용자 계정 정보 모델 클래스
 */
public class PromiseList {
    private String mPromiseName;
    private String mDate;  // firebase Uid (고유 아이디)
    private String mTime;
    private String mFriend;
    private double mlatitude;
    private double mlongitude;

    public PromiseList() { }
    public PromiseList(String mName, String mDate, String mTime, String mFriend) {
        this.mPromiseName = mName;
        this.mDate = mDate;
        this.mTime = mTime;
        this.mFriend = mFriend;
    }

    public String getmPromiseName() {
        return mPromiseName;
    }

    public void setmPromiseName(String mPromiseName) {
        this.mPromiseName = mPromiseName;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmFriend() {
        return mFriend;
    }

    public void setmFriend(String mFriend) {
        this.mFriend = mFriend;
    }

    public double getMlatitude() {
        return mlatitude;
    }

    public void setMlatitude(double mlatitude) {
        this.mlatitude = mlatitude;
    }

    public double getMlongitude() {
        return mlongitude;
    }

    public void setMlongitude(double mlongitude) {
        this.mlongitude = mlongitude;
    }
}
