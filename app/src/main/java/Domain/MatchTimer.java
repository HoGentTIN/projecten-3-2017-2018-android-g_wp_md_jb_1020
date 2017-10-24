package Domain;

import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;

/**
 * Created by timos on 24-10-2017.
 */

//This class holds the functionallity of the timers used during the match
public class MatchTimer {

    //VARIABLES
    private boolean isChronoOn = false;
    private boolean shotlockRunning = false;
    private boolean timeoutRunning = false;

    private long baseTime;                  //Basetime of the matchtimer, hold starttime of when chronometer is started
    private long timeoutBaseTime;
    private long shotlockBaseTime = 0;      //Shotlock is countown of 30 seconds for every person
    private long elapsedTime = 0;           //The time between when the chronometer started running and when it was stopped, at the start of every round this is = 0
    private long stopTime;                  //Time at which the chronometer was stopped

    private String maxTime = "08:00";       //For testing purpose given default value
    private Chronometer matchTimer;         //matchtimer holds time exiperd during each match round
    private Chronometer shotlockTimer;      //timer to follow ball posision of team (every player may only hold ball for 30 sec) 00:30
    private Chronometer timeoutTimer;       //Each team can call 1 time out per round, even if not all time used they cant do again time out = 01:00
    private int roundTime = 8;              //roundtime in minutes

    //CONSTRUCTORS
    @RequiresApi(api = Build.VERSION_CODES.N)
    public MatchTimer(Chronometer matchChrono){ //temporary constructor - when shotlock has been implemented use the other constructor
        matchTimer = matchChrono;
        matchTimer.setFormat(maxTime);          //replace string with max roundtime - needs to be relocated so that maxtime can be set correctly
        initTimer();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public MatchTimer(Chronometer matchChrono, Chronometer shotlockChrono){
        matchTimer = matchChrono;
        shotlockTimer = shotlockChrono;
        initTimer();
    }

    //GETTERS AND SETTERS
    //Function getElapsed
    public long getElapsedTime(){
        return elapsedTime;
    }
    //Function getBase
    public long getBaseTime() {
        return baseTime;
    }
    //Function getChrono
    public Chronometer getMatchTimer(){
        return matchTimer;
    }
    //Function get Time
    public String getTime(){
        return matchTimer.getText().toString();
    }
    //Function get Chronostate
    public boolean isChronoOn() {
        return isChronoOn;
    }

    //Function setChronoTimes
    public void setChronoTimes(long b, long e){
        elapsedTime = e;
        baseTime = b;
    }
    //Function setMaxTime for round
    public void setMaxTime(String maxTime, int roundTime){
        this.maxTime = maxTime;
        this.roundTime = roundTime;
    }

    //CLASS FUNTIONS
    //Function initialize matchtimer - reset matchtimer
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initTimer(){
        //at start of round reset the timer to start from 0
        baseTime = (SystemClock.elapsedRealtime() + ((long)(roundTime*60000)));   //Time when chrono is first started, for countdown add maximum roundtime as long (formula is time in minutes * 60 000)
        matchTimer.setCountDown(true);
        setChronoTimes(baseTime, elapsedTime);
    }
    //Function initShotlock
    public void initShotlock(){
        //LOOK AT CLASS COUNTS_DOWNTIMER FOR SHOTLOCK, chrono textformat does not allow seconds only
        //shotlockBaseTime = (SystemClock.elapsedRealtime()+ ((long)(30*1000)));   //Time when we add 30 000 milliseconds to allow for countdown
        //matchTimer.setCountDown(true);
    }
    //Function initTimeout

    public void initTimeout(){
        timeoutBaseTime = (SystemClock.elapsedRealtime()+ ((long)(1*60000)));  //set timer to countdown for 1 minute
        timeoutTimer.setCountDown(true);
    }

    //Function resetTimer
    public void resetTimer(){
        this.elapsedTime = 0;
    }

    //Function startTimer
    public void startChrono(){
        matchTimer.setFormat("%s"); // set the format for a chronometer
        baseTime = getBaseTime();
        elapsedTime = getElapsedTime();
        matchTimer.start();

        if(elapsedTime<=0){//Only on initial startup  - reset this to 0 every time the rounds change
            initTimer();
        }

        baseTime+=elapsedTime ;                  //restart time of chrono has to be the sum of all the elapsed times - add active time
        matchTimer.setBase(baseTime);    //Set the starttime of the cronometer

        isChronoOn = true;
    }

    //Function stopTimer
    public void stopChrono(){
        matchTimer.stop();
        stopTime = SystemClock.elapsedRealtime() + ((long) (roundTime*60000));
        elapsedTime = baseTime-stopTime;      //get the time the chrono was active
        isChronoOn = false;
        setChronoTimes(baseTime, elapsedTime);
    }

    //Function StartTimeout   //only one neede timout process is same for both teams
    public void startTimeout(Chronometer timeoutTimer){
        //Matchtimer needs to pause when starting timeout
        if(isChronoOn()){
            stopChrono();
        }

        this.timeoutTimer = timeoutTimer;   //timeouttimer is assigned to the timeoutbutton that was pressed (away or home)
        timeoutTimer.setFormat("01:00");
        initTimeout();
        timeoutRunning = true;
        //timeout alwas starts from start

    }

    //Function stopTimeout //in case time out needs to be stopped earlier ?should automaticly stop when restarting chrono
    public void stopTimeout(){
        //make button of timeout unavailable in activity when this is called
        timeoutRunning = false;
        timeoutTimer.setFormat("X");    //cannot be used this round

    }

    //Function checkifroundended
    public boolean checkRoundTime(String currentTime){
        if("00:00".equalsIgnoreCase(currentTime)){
            //temporary at end of round stop timer
            Log.i("timer","Round has ended");
            matchTimer.stop();
            isChronoOn = false;
            return true;
        }
        return false;
    }

    //Function Check shotlockTime
    //REVISABLE AFTER TURNIGN SHOTLOCK INTO COUNTDOWN INSTEAD OF CHRONO
    public boolean checkShotlockTime(String currentTime){
        if("00:30".equalsIgnoreCase(currentTime)){
            //
            Log.i("Info","Shotlock has expired.");
            return true;
        }
        return false;
    }

    //Function Check timeoutTime
    public boolean checkTimeoutTime(String currentTime){
        if("00:00".equalsIgnoreCase(currentTime)){
            //
            Log.i("Info","Timeout has expired. Resuming matchtimer...");
            return true;
        }
        return false;
    }






}

