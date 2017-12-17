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
    /**
     * Constructor for creating controller to control timers used during match.
     *
     * @param txtTimer The textfield where the matchtimer will be placed.
     * @param roundtime Value to set round duration.
     *  @param breakTime value to set normal break duration.
     */
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


    //CLASS FUNCTIONS

    /**
     * Method for initializing timer.
     *
     * @param txtTimer The textfield where the matchtimer will be placed.
     * @param timeRemaining value to set remaining time in milliseconds.
     */
    public void initTimer(final TextView txtTimer, long timeRemaining){
        if(timeRemaining==(roundTime*1000*60)){
            if(cdtTimer!=null)
                cdtTimer.cancel();
            txtTimer.setText(roundTime + ":00");
        }

        cdtTimer = new CountDownTimer(timeRemaining, 150) {
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

    /**
     * Method for for initializing shotlock.
     *
     * @param txtShotlock The textfield where the shotlock will be placed.
     * @param shotlockTimeRemaining Value to set shotlock duration.
     */
    public void initShotlock(final TextView txtShotlock, Long shotlockTimeRemaining){
        //when the matchtimers pauzes, also pause shotlock, after special reset shotlock to neutral
        //textview clickable, reset shotlock to no team, only possible during player edit events

        //No team has bal possesion neutral shotlock state

            if(cdtShotlock!=null)
                cdtShotlock.cancel();                                                               //Stop previous countdowntimers, to prevent needless background process

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
            }
        };
    }


    /**
     * Method for for initializing Timout.
     *
     * @param btnTimeout button in view used to display this timout.
     */
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
    /**
     * Method for for initializing breaktimer.
     *
     * @param txtTimer The textfield where the break timer will be placed.
     */
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

    /**
     * Method for for initializing clearing the timer.
     *
     */
    public void clearTimer(){this.cdtTimer = null;}

    /**
     * Method for for starting matchtimer.
     *
     * this will restart the timer by recreating it with the time it had left
     * shotlock is connected to this method as well to sync the two together
     */
    public void startChrono(){
        cdtTimer.start();
        cdtShotlock.start();
        isChronoOn = true;
    }
    /**
     * Method for for stopping matchtimer.
     *
     * this will stop and destroy the timer so it can be recreated starting from the time it had remaining
     * shotlock is connected to this method as well to sync the two together
     */
    public void stopChrono(){
        cdtTimer.cancel();
        isChronoOn = false;
        cdtShotlock.cancel();   //stop running shotlocktimer
    }

    /**
     * Method for starting break.
     *
     * This method is called when matchtimer reaches 0 and will automatically start the break
     */
    public void startBreak(){
        this.cdtBreak.start();
    }


    /**
     * Method for checking chronostate.
     *
     * This is used as a safety check to determine the current state of the timers so no faulty methods are called.
     */
    public boolean isChronoOn() {
        return isChronoOn;
    }
    /**
     * Method for checking timout.
     *
     * Used to prevent timout being called more than once per round
     */
    public boolean isTimoutUsed() {
        return istimoutUsed;
    }
    /**
     * Method for reseting timoutimers
     *
     * This method is used to allow the resettimers to be used again once a new round starts
     */
    public void resetIsTimoutUsed() {
        istimoutUsed = false;
    }
}

