package Application;

import android.app.Application;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.Locale;

import Domain.Domaincontroller;
import Domain.MatchTimer;

/**
 * Created by timos on 5-10-2017.
 */
// this is the singleton class
public class ApplicationRuntime extends Application {

    //Variables
    private static ApplicationRuntime singleInstance = null;    //Singleton - This class is universal in the app and can be used in each different activity
    public Domaincontroller dc;                                 //called in ApplicationRuntime functions to manipulate/retrieve/set data or call/use domain functionality
    //Domaincontroller is used to acces variables gained from database, runtime info like the timerclock data is stored in applicationruntime itself
    public MatchTimer mt;                                       //Keep chronometer functions during runtime

    
    //Class initializer - setter runtime instance
    @Override
    public void onCreate() {
        super.onCreate();
        singleInstance = this;
        dc = new Domaincontroller();
    }

    public MatchTimer getMt() {
        return mt;
    }

    //Getter of runtime instance
    public static ApplicationRuntime getInstance(){
        return singleInstance;
    }

    //FUNCTIONS
    //Function chronoSetup - create chronometer
    public MatchTimer chronoSetup(TextView txtTimer, long roundtime, long breaktime){
        return mt = new MatchTimer(txtTimer, roundtime, breaktime);
    }

    //Function getDomeincontroller
    public Domaincontroller getDc(){
        return this.dc;
    }

    public void setLocale(String l){
        //for testing - default value for l
        l = "FR";

        Resources res = getApplicationContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        //conf.setLocale(new Locale(language_code.toLowerCase())); // API 17+ only.
        conf.locale = new Locale(l); //if targeting lower versions
        res.updateConfiguration(conf, dm);
    }
}
