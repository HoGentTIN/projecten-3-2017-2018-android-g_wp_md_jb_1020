package com.g1020.waterpolo;

import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Domain.PenaltyType;
import Domain.Player;
import application.ApplicationRuntime;
import Domain.Domaincontroller;
import views.FontManager;

public class AdministrationEnd extends AppCompatActivity implements PlayersFragmentSingleList.OnPlayerSelectedListener {

    ButtonsFragment btnFragment;
    TeamsHeaderFragment teamsHeaderFragment;
    PlayersFragmentSingleList homeTeam;
    PlayersFragmentSingleList awayTeam;

    ListView lvActivitiesQ1,lvActivitiesQ2, lvActivitiesQ3, lvActivitiesQ4;
    List q1,q2,q3,q4;

    TextInputLayout txtInputLayName, txtInputLayCode;
    TextView txtIconSign;

    ApplicationRuntime ar;  //this adds temporary code to this class
    Domaincontroller dc;
    MatchControl mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration_end);

        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
        dc.getMatch().setCurrentRound(4);

        txtInputLayName = (TextInputLayout) findViewById(R.id.txtInputLayName);
        txtInputLayCode = (TextInputLayout) findViewById(R.id.txtInputLayCode);

        btnFragment = new ButtonsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.adminbuttonsContainer, btnFragment).commit();

        teamsHeaderFragment = new TeamsHeaderFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.teamsheadercontainer, teamsHeaderFragment).commit();

        homeTeam = PlayersFragmentSingleList.newInstance(0);
        getSupportFragmentManager().beginTransaction().add(R.id.homeContainer, homeTeam).commit();

        awayTeam = PlayersFragmentSingleList.newInstance(1);
        getSupportFragmentManager().beginTransaction().add(R.id.awayContainer, awayTeam).commit();

        homeTeam.setOtherTeam(awayTeam);
        awayTeam.setOtherTeam(homeTeam);

        TabHost host = (TabHost)findViewById(R.id.tab_host);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Quarter 1")
                .setContent(R.id.activitiesContainerFirstRound)
                .setIndicator("Quarter 1");

        host.addTab(spec);

        spec = host.newTabSpec("Quarter 2");
        spec.setContent(R.id.activitiesContainerSecondRound);
        spec.setIndicator("Quarter 2");
        host.addTab(spec);

        spec = host.newTabSpec("Quarter 3");
        spec.setContent(R.id.activitiesContainerThirdRound);
        spec.setIndicator("Quarter 3");
        host.addTab(spec);

        spec = host.newTabSpec("Quarter 4");
        spec.setContent(R.id.activitiesContainerFourthRound);
        spec.setIndicator("Quarter 4");
        host.addTab(spec);

        lvActivitiesQ1 = (ListView) findViewById(R.id.activitiesContainerFirstRound) ;
        q1 = dc.getLogForRound(1);
        lvActivitiesQ1.setAdapter(new ArrayAdapter<String>(AdministrationEnd.this,
                android.R.layout.simple_list_item_1, q1));

        lvActivitiesQ2 = (ListView) findViewById(R.id.activitiesContainerSecondRound) ;
        q2 = dc.getLogForRound(2);
        lvActivitiesQ2.setAdapter(new ArrayAdapter<String>(AdministrationEnd.this,
                android.R.layout.simple_list_item_1, q2));

        lvActivitiesQ3 = (ListView) findViewById(R.id.activitiesContainerThirdRound) ;
        q3 = dc.getLogForRound(3);
        lvActivitiesQ3.setAdapter(new ArrayAdapter<String>(AdministrationEnd.this,
                android.R.layout.simple_list_item_1, q3));

        lvActivitiesQ4 = (ListView) findViewById(R.id.activitiesContainerFourthRound) ;
        q4 = dc.getLogForRound(4);
        lvActivitiesQ4.setAdapter(new ArrayAdapter<String>(AdministrationEnd.this,
                android.R.layout.simple_list_item_1, q4));

//        homeTeam.updateBackgroundPlayers();
//        awayTeam.updateBackgroundPlayers();
        txtIconSign = (TextView) findViewById(R.id.iconSignMatch);
        setIconFont(txtIconSign);

        //setPasswordVisibilityToggleEnabled(boolean)

    }


    public void SignMatch(View view) {
    }

    @Override
    public void onArticleSelected(Boolean homeTeam, int playerId) {
        dc.setSelectedPlayer(homeTeam, playerId);
    }

    //Function: GoalMade - press goal button to change view so you can select who scored
    public void goalMade(View view){

        Player sp = dc.getSelectedPlayer();
        if(sp!=null){
            //add the goal to the current selected player in domaincontroller
            dc.addGoal();

            //Post goal to live
            dc.asyncPostGoal(sp);

            //Logging
            addToLog(sp, "G","Goal by " + dc.getSelectedPlayer().getFullName());
            clearSelectedPlayer();


            teamsHeaderFragment.updateHeader();


        }else {
            toast("Select a player first.");
        }
    }

    //TEMP function to move to PlayerControl activity
    public void changePlayers(View view) {

        Player sp = dc.getSelectedPlayer();
        if(sp!=null){

            dc.switchPlayerCaps();
            clearSelectedPlayer();

        }else {
            toast("Select a player first.");
        }
        //PlayersFragment awayTeam = new PlayersFragment();
        //getSupportFragmentManager().beginTransaction().detach(faultAwayTeam).commit();
        //getSupportFragmentManager().beginTransaction().add(R.id.awayContainer, awayTeam).commit();

    }


    public void injurySustained(View view){

        //guessing this is here to test end administration
        /*
        Player sp = dc.getSelectedPlayer();
        if(sp!=null){
            //dc.asyncPostInjury();

            addToLog(sp, "I",dc.getSelectedPlayer().getFullName() + " got injured");
            activities.updateActivities(dc.getMatch().getCurrentRound());
            clearSelectedPlayer();
        }else {
            toast("Select a player first.");
        }
        */

    }


    public void faultU20(View view){

        //FIRST CHECK IF PLAYER ALREADY HAS 20 sec FAULT if not ignore press of button or give message player allready punished

        //If players selected preform action
        if(dc.getSelectedPlayer()!=null){
            Player sp = dc.getSelectedPlayer();
                //add the fault to the current selected player in domaincontroller
                dc.addFaultU20();
                //post fault
                dc.asyncPostFault(sp, PenaltyType.U20);

                //update the background color
                updateBackgroundPlayer(sp);

                //Logging
                addToLog(sp, "U", "Fault U20 for " + sp.getFullName() + ".");

                clearSelectedPlayer();      //clear selected player from layout

        }else {
            toast("Select a player first.");
        }

    }

    public void faultUMV(View view) {



        Player sp = dc.getSelectedPlayer();
        if(sp!=null){

            dc.addFaultUMV();
            //post fault
            dc.asyncPostFault(sp, PenaltyType.UMV);

            addToLog(sp, "UMV","Fault UMV for " + sp.getFullName() + ".");

            updateBackgroundPlayer(sp);

            clearSelectedPlayer();

        }else {
            toast("Select a player first.");

        }
    }

    public void faultUMV4(View view) {

        Player sp = dc.getSelectedPlayer();
        if(sp!=null){

            dc.addFaultUMV4();
            //post fault
            dc.asyncPostFault(sp, PenaltyType.UMV4);

            addToLog(sp, "UMV4","Fault UMV for " + sp.getFullName() + ".");
            updateBackgroundPlayer(sp);

            clearSelectedPlayer();

        }else {
            toast("Select a player first.");
        }

    }

    public void addToLog(Player sp, String event, String description){
        //Determine home or away team
        String t;
        if(sp.getTeam().equals(dc.getHomeTeam())){t = "H";}else {t = "A";}

        dc.appendLog(description,event + t + dc.getMatch().getCurrentRound(), "END",dc.getMatch().getCurrentRound());
    }

    private void updateBackgroundPlayer(Player sp){
        if(sp.getTeam().equals(dc.getHomeTeam())){
            homeTeam.updateBackgroundPlayer();
        } else {
            awayTeam.updateBackgroundPlayer();
        }
    }
    //Function to clear selectedPlayer after performing buttonAction
    public void clearSelectedPlayer(){
        homeTeam.resetFontPlayers();
        awayTeam.resetFontPlayers();
        dc.resetSelectedPlayer();
    }

    //Setup toast notification
    public void toast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    private void setIconFont(TextView icon) {
            icon.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME));
    }
}
