package com.g1020.waterpolo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AdministrationSetup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration_setup);
    }

    //TEMP function to move to MatchControl
    public void startMatch(View view) {
        Intent intent = new Intent(this, MatchControl.class);
        startActivity(intent);
    }
}
