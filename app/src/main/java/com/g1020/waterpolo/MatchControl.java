package com.g1020.waterpolo;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;

import Application.ApplicationRuntime;

public class MatchControl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_control);
        //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY START
        ar = ApplicationRuntime.getInstance();
        isChronoOn = false;
        matchTimer = (Chronometer) findViewById(R.id.matchTimer);
        //Chrono listner to set maximum time
        matchTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                if (chronometer.getText().toString().equalsIgnoreCase("01:00")) { //for demonstration purpose the maxtime has been set to 1 minute
                    //Stop chronometer and go to the round end screen
                    matchTimer.stop();
                    isChronoOn = false;
                    //incomplete code chrono can be restarted
                }
            }
        });
        //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY END
    }

    //TEMP function to move to PlayerControl activity
    public void editPlayer(View view) {
        Intent intent = new Intent(this, PlayerControl.class);

        //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY START
        stopChrono();
        intent.putExtra(EXTRA_MESSAGE, matchTimer.getText().toString());
        //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY END

        startActivity(intent);
    }
    //TEMP function to move to PlayerControl activity
    public void setupRound(View view) {
        Intent intent = new Intent(this, roundSetup.class);
        startActivity(intent);
    }
    //TEMP function to move to PlayerControl activity
    public void endMatch(View view) {
        Intent intent = new Intent(this, AdministrationEnd.class);
        startActivity(intent);
    }



    //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY START
    public static final String EXTRA_MESSAGE = "com.g1020.Waterpolo.MESSAGE";
    public boolean isChronoOn;
    long baseTime;
    long stopTime;
    long elapsedTime;
    ApplicationRuntime ar;  //this adds temporary code to this class
    Chronometer matchTimer;

    public void startChrono(){
        baseTime = ar.getBaseTime();
        elapsedTime = ar.getElapsedTime();
        matchTimer.start();
        if(elapsedTime<=0){//Only on initial startup  - reset this to 0 every time the rounds change
            baseTime = SystemClock.elapsedRealtime();   //Time when chrono is first started
            ar.setChronoTimes(baseTime, elapsedTime);
        }
        baseTime+=elapsedTime;                  //restart time of chrono has to be the sum of all the elapsed times - add active time
        matchTimer.setBase(baseTime);    //Set the starttime of the cronometer
        matchTimer.setFormat("%s"); // set the format for a chronometer
        isChronoOn = true;
    }

    public void stopChrono(){
        matchTimer.stop();
        stopTime = SystemClock.elapsedRealtime();
        elapsedTime = baseTime-stopTime;            //get the time the chrono was active
        isChronoOn = false;
        ar.setChronoTimes(baseTime, elapsedTime);
    }

    public void toggleChrono(View view){
        if(isChronoOn){
            stopChrono();
        }else{
            startChrono();
        }
    }

    //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY END
}
