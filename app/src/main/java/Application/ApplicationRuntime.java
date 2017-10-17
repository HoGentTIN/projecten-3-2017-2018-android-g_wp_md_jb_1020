package Application;

import android.app.Application;

import Domain.Domaincontroller;

/**
 * Created by timos on 5-10-2017.
 */

public class ApplicationRuntime extends Application {

    //Variables
    private static ApplicationRuntime singleInstance = null;    //Singleton - This class is universal in the app and can be used in each different activity
    public Domaincontroller dc;                                 //called in ApplicationRuntime functions to manipulate/retrieve/set data or call/use domain functionality
    
    //Class initializer - setter runtime instance
    @Override
    public void onCreate() {
        super.onCreate();
        singleInstance = this;
        dc = new Domaincontroller();
    }

    //Getter of runtime instance
    public static ApplicationRuntime getInstance(){
        return singleInstance;
    }

    // TEMPORARY CODE FOR CHRONO SHOWCASE
    private long elapsedTime;
    private long baseTime;

    public void setChronoTimes(long b, long e){
        elapsedTime = e;
        baseTime = b;
    }

    public long getElapsedTime(){
        return elapsedTime;
    }

    public long getBaseTime() {
        return baseTime;
    }

    // TEMPORARY CODE FOR CHRONO SHOWCASE END
}
