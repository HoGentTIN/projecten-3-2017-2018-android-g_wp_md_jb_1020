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
    private long baseTime;                  //Basetime of the matchtimer, hold starttime of when chronometer is started
    private long elapsedTime = 0;           //The time between when the chronometer started running and when it was stopped, at the start of every round this is = 0
    private long stopTime;                  //Time at which the chronometer was stopped
    private long shotlockBaseTime = 0;      //Shotlock is countown of 30 seconds for every person
    private String maxTime = "00:00";       //For testing purpose given default value
    private Chronometer matchTimer;         //matchtimer holds time exiperd during each match round
    private Chronometer shotlockTimer;      //timer to follow ball posision of team (every player may only hold ball for 30 sec) 00:30
    private Chronometer timeoutTimer;       //Each team can call 1 time out per round, even if not all time used they cant do again time out = 01:00
    private int roundTime = 8;

    //CONSTRUCTORS
    @RequiresApi(api = Build.VERSION_CODES.N)
    public MatchTimer(Chronometer matchChrono){ //temporary constructor - when shotlock has been implemented use the other constructor
        matchTimer = matchChrono;
        matchTimer.setFormat("08:00");          //replace string with max roundtime
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
        baseTime = (SystemClock.elapsedRealtime()+ ((long)(roundTime*60000)));   //Time when chrono is first started, for countdown add maximum roundtime as long (formula is time in minutes * 60 000)
        matchTimer.setCountDown(true);
        setChronoTimes(baseTime, elapsedTime);
    }
    //Function initShotlock
    public void initShotlock(){


    }
    //Function resetTimer
    public void resetTimer(){
        this.elapsedTime = 0;
    }

    //Function startTimer
    public void startChrono(){
        baseTime = getBaseTime();
        elapsedTime = getElapsedTime();
        matchTimer.start();
        if(elapsedTime<=0){//Only on initial startup  - reset this to 0 every time the rounds change
            initTimer();
        }
        baseTime+=elapsedTime ;                  //restart time of chrono has to be the sum of all the elapsed times - add active time
        matchTimer.setBase(baseTime);    //Set the starttime of the cronometer
        matchTimer.setFormat("%s"); // set the format for a chronometer
        isChronoOn = true;
    }

    //Function stopTimer
    public void stopChrono(){
        matchTimer.stop();
        stopTime = SystemClock.elapsedRealtime() + ((long)(roundTime*60000));
        elapsedTime = baseTime-stopTime;            //get the time the chrono was active
        isChronoOn = false;
        setChronoTimes(baseTime, elapsedTime);
    }

    //Function checkifroundended
    public boolean checkRoundTime(String currentTime){
        if(maxTime.equalsIgnoreCase(currentTime)){
            //temporary at end of round stop timer
            Log.i("timer","" + SystemClock.elapsedRealtime());
            matchTimer.stop();
            isChronoOn = false;
            return true;
        }
        return false;
    }

    //Function Check shotlockTime
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
        if("01:00".equalsIgnoreCase(currentTime)){
            //
            Log.i("Info","Timeout has expired. Resuming matchtimer...");
            return true;
        }
        return false;
    }






}
