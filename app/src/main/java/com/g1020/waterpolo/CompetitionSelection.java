package com.g1020.waterpolo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CompetitionSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_selection);
    }

    //TEMP function to move to administration activity
    public void endSelection(View view) {
        Intent intent = new Intent(this, MatchControl.class);
        //Intent intent = new Intent(this, AdministrationSetup.class);
        startActivity(intent);
    }

}
