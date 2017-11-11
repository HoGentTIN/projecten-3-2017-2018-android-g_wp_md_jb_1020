package Domain;

import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

/**
 * Created by timos on 24-10-2017.
 */

//This class holds the functionallity of the timers used during the match
public class MatchTimer {

    //VARIABLES
    private boolean isChronoOn = false;
    private boolean isShotlockOn = false;
    private boolean istimoutUsed = false;

    private long timeRemaining;
    private long shotlockTimeRemaining;     //Stores the shotlock time when pausing the match

    private CountDownTimer cdtTimer;
    private CountDownTimer cdtShotlock;             //shotlock to follow timelimit on ball possesion
    private CountDownTimer cdtTimout;          //Each team can call 1 time out per round, even if not all time used they cant do again time out = 01:00
    private long roundTime = 8;                     //roundtime in minutes


    //CONSTRUCTORS
    public MatchTimer(TextView txtTimer, long roundtime){
        setMaxTime(roundtime);
        setShotlockTimeRemaining((long) 30000);
        initTimer(txtTimer, (this.roundTime*1000*60));
    }

    //GETTERS AND SETTERS
    //Function getmatchTimer
    public CountDownTimer getCdtTimer(){return  cdtTimer;}
    //Function getShotlockTimer
    public CountDownTimer getCdtShotlock(){return cdtShotlock;}
    //Function getTimeoutTimer
    public CountDownTimer getCdtTimout(){return cdtTimout;}


    //Function setMaxTime for round
    public void setMaxTime(long roundTime){
        this.roundTime = roundTime;
        setTimeRemaining( (roundTime*1000*60));
    }

    public void setTimeRemaining(long timeRemaining){
        this.timeRemaining = timeRemaining;
    }
    public long getTimeRemaining(){
        return this.timeRemaining;
    }

    public void setShotlockTimeRemaining(long shotlockTimeRemaining){
        this.shotlockTimeRemaining = shotlockTimeRemaining;
    }
    public long getShotlockTimeRemaining(){
        return this.shotlockTimeRemaining;
    }

    //CLASS FUNCTIONS
    //Function initialize matchtimer - reset matchtimer
    public void initTimer(final TextView txtTimer, long timeRemaining){
        if(timeRemaining==(roundTime*1000*60)){
            if(cdtTimer!=null)
                cdtTimer.cancel();
            txtTimer.setText(roundTime + ":00");
        }

        cdtTimer = new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutesRemaining = (long) Math.ceil(millisUntilFinished/(1000.0*60.0))-1;
                long secondRemaining = (long) Math.ceil((millisUntilFinished - (minutesRemaining*(1000*60)))/1000.0);

                if(secondRemaining==60 && minutesRemaining+1 == roundTime)
                    txtTimer.setText(roundTime + ":00");
                else if(secondRemaining==60){
                    txtTimer.setText(String.format("%d:00",minutesRemaining));
                }else {
                    txtTimer.setText(String.format("%d:%d",minutesRemaining , secondRemaining));
                }
                setTimeRemaining(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                txtTimer.setText("0");
                Log.i("Info","Matchtimer has expired.");
            }
        };
    }
    //Function initShotlock - Setup shotlock, also callable when match paused to reset shotlock when neither team has ball possesion
    public void initShotlock(final TextView txtShotlock, Long shotlockTimeRemaining){
        //when the matchtimers pauzes, also pause shotlock, after special reset shotlock to neutral
        //textview clickable, reset shotlock to no team, only possible during player edit events

        //No team has bal possesion neutral shotlock state
        if(shotlockTimeRemaining==30000){                                                           //Set layout if neither teams have ball possesion
            txtShotlock.setBackgroundColor(Color.MAGENTA);
            if(cdtShotlock!=null)
                cdtShotlock.cancel();                                                               //Stop previous countdowntimers, to prevent needless background process
        }

        cdtShotlock = new CountDownTimer(shotlockTimeRemaining,1000) {               //Initialize countdowntimer on correct starting time

            @Override
            public void onTick(long millisUntilFinished) {
                txtShotlock.setText(String.format("%.0f",(Math.ceil(millisUntilFinished/1000.0)))); //Do to tick registration and rounding down the milisecond value it might skip the first value, hence the usage of ceil
                setShotlockTimeRemaining(millisUntilFinished);                                      //Hold on to remaining time to resume after pause
            }

            @Override
            public void onFinish() {
                txtShotlock.setText("0");
                Log.i("Info","Shotlock has expired.");
            }
        };
    }
    //Function initTimeout
    public void initTimeout(final Button btnTimeout){

        btnTimeout.setClickable(false);                     //Can no longer be activated in this quarter

        //Countdown only once per round no pausing timout possible
        cdtTimout = new CountDownTimer(60000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                btnTimeout.setText(String.format("%.0f",(Math.ceil(millisUntilFinished/1000.0)))); //Do to tick registration and rounding down the milisecond value it might skip the first value, hence the usage of ceil
            }

            @Override
            public void onFinish() {
                btnTimeout.setText("T");
                Log.i("Info","Timeout has expired.");
            }

        };
        stopChrono();               //stop other timers
        istimoutUsed = true;          //Remember timout was used

    }

    //Function resetTimer
    public void resetTimer(final TextView txtTimer){
        timeRemaining = (roundTime*1000*60);
    }
    //Function resetShotlock - set remaining shotlocktime to 30000
    public void resetShotlock(final TextView txtShotlock){
        shotlockTimeRemaining = 30000;
    }

    //Function startTimer
    public void startChrono(){
        cdtTimer.start();
        isChronoOn = true;
    }
    //Function stopTimer
    public void stopChrono(){
        cdtTimer.cancel();
        isChronoOn = false;
        cdtShotlock.cancel();   //stop running shotlocktimer
    }


    //Function stopTimeout //in case time out needs to be stopped earlier ?should automaticly stop when restarting chrono
    public void stopTimeout(){
        //make button of timeout unavailable in activity when this is called
        //???Can timeout be prematurly stopped
    }

    //Function get Chronostate
    public boolean isChronoOn() {
        return isChronoOn;
    }
    //Function get shotlock used
    public boolean isTimoutUsed() {
        return istimoutUsed;
    }
    //Function set shotlock used
    public void resetIsTimoutUsed() {
        istimoutUsed = false;
    }
}

