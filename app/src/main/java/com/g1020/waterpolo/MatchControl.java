package com.g1020.waterpolo;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;

import Application.ApplicationRuntime;
import Domain.Domaincontroller;
import Domain.MatchTimer;

public class MatchControl extends AppCompatActivity {

    //VARIABLES
    //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY START
    public static final String EXTRA_MESSAGE = "com.g1020.Waterpolo.MESSAGE";
    //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY END

    ApplicationRuntime ar;  //this adds temporary code to this class
    Domaincontroller dc;
    MatchTimer matchTimer;

    FaultFragment faultAwayTeam;



    //LIFECYCLE FUNCTIONS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_control);

        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
        matchTimer = ar.chronoSetup((Chronometer) findViewById(R.id.matchTimer));
        //Chrono listner to set maximum time
        matchTimer.getMatchTimer().setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if(matchTimer.checkRoundTime(chronometer.getText().toString())){
                    Log.i("Info","Timer reached round end");
                    dc.appendLog("End of round" + 1 + " - Current matchscore: " + 2 + " - " + 5);                                              //Replace 1 by dc.functiontoretrieveroundnumber, and retrievecurrentscores home-away
                    //checkRoundTime already does the timerprocess
                    //Still need code here for actions related to activity
                }
            }
        });

        TeamsHeaderFragment teamsHeader = new TeamsHeaderFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.teamsheadercontainer, teamsHeader).commit();

        FaultFragment faultHomeTeam = new FaultFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.faultHomeContainer, faultHomeTeam).commit();

        faultAwayTeam = new FaultFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.faultAwayContainer, faultAwayTeam).commit();


        //Testcode for adding logging functionallity
        testLog();

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
        PlayersFragment awayTeam = new PlayersFragment();
        getSupportFragmentManager().beginTransaction().detach(faultAwayTeam).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.faultAwayContainer, awayTeam).commit();
    }
    //TEMP function to move to PlayerControl activity
    public void endMatch(View view) {
        Intent intent = new Intent(this, AdministrationEnd.class);
        startActivity(intent);
    }

    //Function togglechrono - start/stop the chronometer - Clickable function
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void toggleChrono(View view){
        if(matchTimer.isChronoOn()){
            matchTimer.stopChrono();
        }else{
            matchTimer.startChrono();
        }

    }


    //Shotlock gets paused when matchtimer is paused, shotlock has 2 buttons 1 button for home (shotlock resets countdown stays hometeam color) the other for away, (reset shotlock ,and set teamcolor)
    public void homeShotlock(View view){}

    public void awayShotlock(View view){}

    //Time out for each round each team can call time out once, cannot be paused, when clicked cannot be used again in same round for that team
    //2 buttons 1 for each team
    public void homeTimeout(View view){}

    public void awayTimeout(View view){}

    //Function: GoalMade - press goal button to change view so you can select who scored
    public void goalMade(View view){

    }





    //start testcode log
    public void testLog(){
        dc.appendLog("player 1H " +  "Steve "  + "from antwerp hawks has scored a goal [0 - 1]");
        dc.appendLog("player 8A " +  "Marcus "  + "from Gent bobcat has scored a goal [1 - 1]");

       // dc.getSegmentedLog();
    }
    //end testcode log
}
