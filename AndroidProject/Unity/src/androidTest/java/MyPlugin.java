import android.util.Log;

public class MyPlugin {
    private static final MyPlugin ourInstance = new MyPlugin();

    private static final String LOGTAG = "ImageCampus";
    private long startTime;

    public static MyPlugin getInstance() {return ourInstance;}

    private MyPlugin() {
        Log.i(LOGTAG, "Created MyPlugin");
        startTime = System.currentTimeMillis();
    }

    public double GetElapsedTime(){
        return (System.currentTimeMillis() - startTime)/1000;
    }
}
