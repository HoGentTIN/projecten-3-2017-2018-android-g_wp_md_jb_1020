package Domain;

import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;

/**
 * Created by timos on 24-10-2017.
 */

public class MatchTimer {

    //VARIABLES
    private boolean isChronoOn = false;
    private long baseTime;
    private long stopTime;
    private long elapsedTime = 0;
    private String maxTime = "00:30";     //For testing purpose given default value
    private Chronometer matchTimer;

    //CONSTRUCTORS
    public MatchTimer(Chronometer chronometer){
        matchTimer = chronometer;
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
    public void setMaxTime(String maxTime){
        this.maxTime = maxTime;
    }

    //CLASS FUNTIONS
    //Function initialize matchtimer - reset matchtimer
    public void initTimer(){
        //at start of round reset the timer to start from 0
        baseTime = SystemClock.elapsedRealtime();   //Time when chrono is first started
        setChronoTimes(baseTime, elapsedTime);
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
        baseTime+=elapsedTime;                  //restart time of chrono has to be the sum of all the elapsed times - add active time
        matchTimer.setBase(baseTime);    //Set the starttime of the cronometer
        matchTimer.setFormat("%s"); // set the format for a chronometer
        isChronoOn = true;
    }

    //Function stopTimer
    public void stopChrono(){
        matchTimer.stop();
        stopTime = SystemClock.elapsedRealtime();
        elapsedTime = baseTime-stopTime;            //get the time the chrono was active
        isChronoOn = false;
        setChronoTimes(baseTime, elapsedTime);
    }

    //Function checkifroundended
    public boolean checkRoundTime(String currentTime){
        if(maxTime.equalsIgnoreCase(currentTime)){
            //temporary at end of round stop timer
            matchTimer.stop();
            isChronoOn = false;
            return true;
        }
        return false;
    }









}
