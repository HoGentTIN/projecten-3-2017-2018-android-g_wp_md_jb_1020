package com.g1020.waterpolo;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;

import Application.ApplicationRuntime;
import Domain.MatchTimer;

public class MatchControl extends AppCompatActivity {

    //VARIABLES
    //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY START
    public static final String EXTRA_MESSAGE = "com.g1020.Waterpolo.MESSAGE";

    ApplicationRuntime ar;  //this adds temporary code to this class
    MatchTimer matchTimer;
    //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY END


    //LIFECYCLE FUNCTIONS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_control);
        //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY START
        ar = ApplicationRuntime.getInstance();
        matchTimer = ar.chronoSetup((Chronometer) findViewById(R.id.matchTimer));
        //Chrono listner to set maximum time
        matchTimer.getMatchTimer().setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

            if(matchTimer.checkRoundTime(chronometer.getText().toString())){
                Log.i("Info","Timer reached round end");
                //checkRoundTime already does the timerprocess
                //Still need code here for actions related to activity
            }
            }
        });
        //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY END
    }


    //PROCESS FUNCTIONS
    //TEMP function to move to PlayerControl activity
    public void editPlayer(View view) {
        Intent intent = new Intent(this, PlayerControl.class);

        //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY START
        matchTimer.stopChrono();
        intent.putExtra(EXTRA_MESSAGE, matchTimer.getTime());
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

    //Function togglechrono - start/stop the chronometer
    public void toggleChrono(View view){
        if(matchTimer.isChronoOn()){
            matchTimer.stopChrono();
        }else{
            matchTimer.startChrono();
        }
    }




}
