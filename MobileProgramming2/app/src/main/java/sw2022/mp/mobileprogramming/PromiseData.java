package sw2022.mp.mobileprogramming;

public class PromiseData {
    private int imageNum;
    private String promiseDate;


    public PromiseData(){ }

    public PromiseData(int imageNum,String promiseDate) {
        this.imageNum = imageNum;
        this.promiseDate = promiseDate;
    }

    public int getImageNum() {
        return imageNum;
    }

    public String getPromiseDate() {
        return promiseDate;
    }

    public void setPromiseDate(String promiseDate) {
        this.promiseDate = promiseDate;
    }

}
