package com.g1020.waterpolo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AdministrationEnd extends AppCompatActivity {

    ButtonsFragment btnFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration_end);

        btnFragment = new ButtonsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.adminbuttonsContainer, btnFragment).commit();

    }


}
