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

import com.g1020.waterpolo.MatchControl;

import application.ApplicationRuntime;

/**
 * Created by timos on 24-10-2017.
 */

//This class holds the functionallity of the timers used during the match
public class MatchTimer {

    //VARIABLES
    ApplicationRuntime ar;
    Domaincontroller dc;

    private boolean isChronoOn = false;
    private boolean isShotlockOn = false;
    private boolean istimoutUsed = false;
    private boolean isPauzeOn = false;

    private long timeRemaining;
    private long shotlockTimeRemaining;     //Stores the shotlock time when pausing the match
    private long shotlockTotalTime = 0;
    private long breakRemaining;

    private CountDownTimer cdtTimer;
    private CountDownTimer cdtShotlock;             //shotlock to follow timelimit on ball possesion
    private CountDownTimer cdtTimout;          //Each team can call 1 time out per round, even if not all time used they cant do again time out = 01:00
    private CountDownTimer cdtBreak;            //timer for the break
    private long roundTime = 8;                  //roundtime in minutes set default for testing
    private long breakTime = 3;



    //CONSTRUCTORS
    public MatchTimer(TextView txtTimer, long roundtime, long breakTime){
        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
        setMaxTime(roundtime);
        if(dc.getMatch().getCurrentRound()==2){
            setBreaktime(1);
        }else{
            setBreaktime(breakTime);
        }

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
    public Long getMaxTime(){
        return (roundTime*1000*60);
    }
    public void setBreaktime(long breakTime){
        this.breakTime = breakTime;
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

    public long getShotlockTotalTime() {
        return shotlockTotalTime;
    }
    public void setShotlockTotalTime(long shotlockTotalTime) {
        this.shotlockTotalTime = shotlockTotalTime;
    }

    //CLASS FUNCTIONS
    //Function initialize matchtimer - reset matchtimer
    public void initTimer(final TextView txtTimer, long timeRemaining){
        if(timeRemaining==(roundTime*1000*60)){
            if(cdtTimer!=null)
                cdtTimer.cancel();
            txtTimer.setText(roundTime + ":00");
        }

        cdtTimer = new CountDownTimer(timeRemaining, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutesRemaining = (long) Math.ceil(millisUntilFinished/(1000.0*60.0))-1;
                long secondRemaining = (long) Math.ceil((millisUntilFinished - (minutesRemaining*(1000*60)))/1000.0);

                if(secondRemaining==60 && minutesRemaining+1 == roundTime)
                    txtTimer.setText(roundTime + ":00");
                else if(secondRemaining==60){
                    txtTimer.setText(String.format("%d:00",minutesRemaining));
                }else {
                    if(secondRemaining<10){
                        txtTimer.setText(String.format("%d:0%d", minutesRemaining, secondRemaining));
                    }else {
                        txtTimer.setText(String.format("%d:%d", minutesRemaining, secondRemaining));
                    }
                }
                setTimeRemaining(millisUntilFinished);
                shotlockTotalTime = shotlockTotalTime + millisUntilFinished;

                //Update matchTimerView before stopping otherwise it will be 1 second behind
                if(getShotlockTimeRemaining()==0){
                    stopChrono();
                }
            }

            @Override
            public void onFinish() {
                Log.i("Info","Matchtimer has expired.");
                if(isChronoOn()){
                    stopChrono();
                }
                //starts function in matchcontrol to start the break
                txtTimer.setText(String.format("0:01"));
                MatchControl mc = (MatchControl) dc.getCurrentActivity();
                mc.prepareBreak();
            }
        };
    }
    //Function initShotlock - Setup shotlock, also callable when match paused to reset shotlock when neither team has ball possesion
    public void initShotlock(final TextView txtShotlock, Long shotlockTimeRemaining){
        //when the matchtimers pauzes, also pause shotlock, after special reset shotlock to neutral
        //textview clickable, reset shotlock to no team, only possible during player edit events

        //No team has bal possesion neutral shotlock state
        if(shotlockTimeRemaining==30000){                                                           //Set layout if neither teams have ball possesion
//            txtShotlock.setBackgroundColor(Color.MAGENTA);
            if(cdtShotlock!=null)
                cdtShotlock.cancel();                                                               //Stop previous countdowntimers, to prevent needless background process
        }

        cdtShotlock = new CountDownTimer(shotlockTimeRemaining,100) {               //Initialize countdowntimer on correct starting time

            @Override
            public void onTick(long millisUntilFinished) {
                txtShotlock.setText(String.format("%.0f",(Math.ceil(millisUntilFinished/1000.0)))); //Do to tick registration and rounding down the milisecond value it might skip the first value, hence the usage of ceil
                setShotlockTimeRemaining(millisUntilFinished);                                      //Hold on to remaining time to resume after pause
            }

            @Override
            public void onFinish() {
                txtShotlock.setText("0");
                Log.i("Info","Shotlock has expired.");
                setShotlockTimeRemaining(0);

                //testcode for calling current activity screen
                MatchControl mc = (MatchControl) dc.getCurrentActivity();
                mc.testFunction();
            }
        };
    }
    //Function initTimeout
    public void initTimeout(final Button btnTimeout){

        btnTimeout.setClickable(false);                     //Can no longer be activated in this quarter

        //Countdown only once per round no pausing timout possible
        cdtTimout = new CountDownTimer(60000,500) {

            @Override
            public void onTick(long millisUntilFinished) {
                btnTimeout.setText(String.format("%.0f",(Math.ceil(millisUntilFinished/1000.0)))); //Do to tick registration and rounding down the milisecond value it might skip the first value, hence the usage of ceil
            }

            @Override
            public void onFinish() {
                btnTimeout.setText("T");
                Log.i("Info","Timeout has expired.");
                MatchControl m = (MatchControl) dc.getCurrentActivity();
                m.enableActions();
            }

        };
        stopChrono();               //stop other timers
        istimoutUsed = true;          //Remember timout was used

    }
    //Function intiBreak
    public void initBreak(final TextView txtTimer){
        //before starting timer initvalue
        long fullBreakTime = breakTime*1000*60;
        txtTimer.setText("Pauze - " + breakTime + ":00");

        cdtBreak = new CountDownTimer(fullBreakTime, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                //round down for correct textview
                long minutesRemaining = (long) Math.ceil(millisUntilFinished/(1000.0*60.0))-1;
                long secondRemaining = (long) Math.ceil((millisUntilFinished - (minutesRemaining*(1000*60)))/1000.0);

                if(secondRemaining==60 && minutesRemaining+1 == roundTime)
                    txtTimer.setText("Pauze - " + roundTime + ":00");
                else if(secondRemaining==60){
                    txtTimer.setText(String.format("Pauze - " + "%d:00",minutesRemaining));
                }else {
                    if(secondRemaining<10){
                        txtTimer.setText(String.format("Pauze - " + "%d:0%d", minutesRemaining, secondRemaining));
                    }else {
                        txtTimer.setText(String.format("Pauze - " + "%d:%d", minutesRemaining, secondRemaining));
                    }
                }
            }

            @Override
            public void onFinish() {
                //setup variables for next round
                dc.nextRound();
                MatchControl mc = (MatchControl) dc.getCurrentActivity();
                mc.setupNewRound();
            }
        };

    }

    //Function resetTimer
    public void resetTimer(final TextView txtTimer){
        timeRemaining = (roundTime*1000*60);
    }
    //Function resetShotlock - set remaining shotlocktime to 30000
    public void resetShotlock(final TextView txtShotlock){
        shotlockTimeRemaining = 30000;
    }
    public void clearBreak(){
        this.cdtBreak = null;
    }
    public void clearTimer(){this.cdtTimer = null;}

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
    //start breaktimer
    public void startBreak(){
        this.cdtBreak.start();
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

