package jwh.com.eumme.Presenter;

public class FlagSingleton {
    private boolean flag= false;
    private long time = 0;
    private volatile static  FlagSingleton ourInstance = new FlagSingleton();

    public static synchronized FlagSingleton getInstance() {
        if(ourInstance == null){
            ourInstance= new FlagSingleton();
        }
        return ourInstance;
    }

    private FlagSingleton() {
        flag = false;
        time = 0;
    }

    public void setTime(long createdTime){
        time=createdTime;
    }
    public boolean getFlag(){
        return flag;
    }

    public String getTime() {
        long duration = time / 1000;
        return Integer.toString((int) duration);
    }

    public void changeFlag(int mode){
        if(mode==1) flag = true;
        else if(mode==2) flag = false;
    }
}
