package com.g1020.waterpolo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Domain.Player;
import application.ApplicationRuntime;
import Domain.Domaincontroller;
import views.FontManager;

public class AdministrationEnd extends AppCompatActivity implements PlayersFragmentSingleList.OnPlayerSelectedListener {

    ButtonsFragment btnFragment;
    TeamsHeaderFragment teamsHeaderFragment;
    PlayersFragmentSingleList homeTeam;
    PlayersFragmentSingleList awayTeam;

    RelativeLayout btnSign;
    ListView lvActivitiesQ1,lvActivitiesQ2, lvActivitiesQ3, lvActivitiesQ4;
    List q1,q2,q3,q4;

    TextInputEditText txtInputLayName, txtInputLayCode;
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
        dc.setCurrentActivity(this);

        txtInputLayName = (TextInputEditText) findViewById(R.id.txtInputLayName);
        txtInputLayCode = (TextInputEditText) findViewById(R.id.txtInputLayCode);

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

        txtIconSign = (TextView) findViewById(R.id.iconSignMatch);
        setIconFont(txtIconSign);

        //setPasswordVisibilityToggleEnabled(boolean)

        //homeTeam.updateBackgroundPlayers();
        //awayTeam.updateBackgroundPlayers();

    }

    public void signMatch(View view) {

        String pss = txtInputLayCode.getText().toString();
        String email = txtInputLayName.getText().toString();

        dc.asyncPostSignMatch(email,pss);

        // if the match is signed
    //    if() {
    //        finishAdmin();
    //    }
    }

    public void finishAdmin(){
        Intent intent = new Intent(this, CompetitionSelection.class);
        startActivity(intent);
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

            //Logging
            addToLog(sp, "G","Goal by " + dc.getSelectedPlayer().getFullName());
            clearSelectedPlayer();


            teamsHeaderFragment.updateHeader();


        }else {
            toast("Select a player first.");
        }
    }

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

            addToLog(sp, "UMV4","Fault UMV4 for " + sp.getFullName() + ".");
            updateBackgroundPlayer(sp);

            clearSelectedPlayer();

        }else {
            toast("Select a player first.");
        }

    }

    public void addToLog(Player sp, String event, String description){
        //Determine home or away team
        String t;
        if(sp.getTeam().equals(dc.getHomeTeam())){
            t = "H";
        } else {
            t = "A";
        }

        dc.appendLog(description,event + t + dc.getMatch().getCurrentRound(), "END",dc.getMatch().getCurrentRound());

        updateLog();
    }

    // reloads the lof for the fourth quarter
    private void updateLog() {
        q4 = dc.getLogForRound(4);
        lvActivitiesQ4.setAdapter(new ArrayAdapter<String>(AdministrationEnd.this,
                android.R.layout.simple_list_item_1, q4));
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

    Toast toast;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.arg1 == 1) {
                toast = Toast.makeText(getApplicationContext(), "Authentication failed when signing form, please fill in your credentials correctly", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        }
    };

    //Setup toast notification
    public void toast(final String message){
           Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
           toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
           toast.show();
    }
    public void toast(){
        Message msg = handler.obtainMessage();
        msg.arg1 = 1;
        handler.sendMessage(msg);

    }

    private void setIconFont(TextView icon) {
            icon.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME));
    }



}
