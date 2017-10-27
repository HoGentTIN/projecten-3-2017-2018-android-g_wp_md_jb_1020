package Domain;

import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.TextView;

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
    private long elapsedTime = 0;           //The time between when the chronometer started running and when it was stopped, at the start of every round this is = 0
    private long stopTime;                  //Time at which the chronometer was stopped
    private long shotlockTimeRemaining;     //Stores the shotlock time when pausing the match

    private String maxTime = "08:00";       //For testing purpose given default value
    private Chronometer matchTimer;         //matchtimer holds time exiperd during each match round
    CountDownTimer cdtShotlock;             //shotlock to follow timelimit on ball possesion
    private Chronometer timeoutTimer;       //Each team can call 1 time out per round, even if not all time used they cant do again time out = 01:00
    private int roundTime = 8;              //roundtime in minutes


    //CONSTRUCTORS
    @RequiresApi(api = Build.VERSION_CODES.N)
    public MatchTimer(Chronometer matchChrono){ //temporary constructor - when shotlock has been implemented use the other constructor
        setShotlockTimeRemaining((long) 30000);
        matchTimer = matchChrono;
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
    //Function getShotlockTimer
    public CountDownTimer getCdtShotlock(){return cdtShotlock;}
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

    public void setShotlockTimeRemaining(Long shotlockTimeRemaining){
        this.shotlockTimeRemaining = shotlockTimeRemaining;
    }
    public long getShotlockTimeRemaining(){
        return this.shotlockTimeRemaining;
    }

    //CLASS FUNTIONS
    //Function initialize matchtimer - reset matchtimer
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initTimer(){
        //at start of round reset the timer to start from 0
        matchTimer.setFormat("START - " + maxTime);          //replace string with max roundtime - needs to be relocated so that maxtime can be set correctly
        baseTime = (SystemClock.elapsedRealtime() + ((long)(roundTime*60000)));   //Time when chrono is first started, for countdown add maximum roundtime as long (formula is time in minutes * 60 000)
        matchTimer.setCountDown(true);
        setChronoTimes(baseTime, elapsedTime);
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
    public void initTimeout(){
        timeoutBaseTime = (SystemClock.elapsedRealtime()+ ((long)(1*60000)));  //set timer to countdown for 1 minute
        timeoutTimer.setCountDown(true);
    }

    //Function resetTimer
    public void resetTimer(){
        this.elapsedTime = 0;
    }
    //Function resetShotlock - set remaining shotlocktime to 30000
    public void resetShotlock(final TextView txtShotlock){
        shotlockTimeRemaining = 30000;
        txtShotlock.setBackgroundColor(Color.MAGENTA);
    }

    //Function startTimer
    public void startChrono(){
        matchTimer.setFormat("%s"); // set the format for a chronometer
        baseTime = getBaseTime();
        elapsedTime = getElapsedTime();
        matchTimer.start();

        if(elapsedTime<=0){//Only on initial startup  - reset this to 0 every time the rounds change - currently only needed in constructor but keep until rounds implementation
            initTimer();
            matchTimer.setFormat("%s"); //Call again to show the clock counting down
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

        //stop running shotlocktimer
        cdtShotlock.cancel();

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

