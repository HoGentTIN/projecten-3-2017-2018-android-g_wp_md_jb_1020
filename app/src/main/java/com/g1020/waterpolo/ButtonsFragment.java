package com.g1020.waterpolo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import Application.ApplicationRuntime;
import Domain.Domaincontroller;
import views.FontManager;

/**
 * Created by pieter on 26/11/2017.
 */

public class ButtonsFragment extends Fragment {

    private TextView iconGoal, iconUMV4, iconInjury, iconNumberChange, iconU20, iconUMV;
    private RelativeLayout btnGoal, btnUMV4, btnInjury, btnNumberChange, btnU20, btnUMV;
    private RelativeLayout btnList[];
    private TextView iconList[];
    private ApplicationRuntime ar;
    private Domaincontroller dc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buttons, container, false);

        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();

        btnGoal = (RelativeLayout) view.findViewById(R.id.btnGoal);
        iconGoal = (TextView) view.findViewById(R.id.iconGoal);
        btnUMV4 = (RelativeLayout) view.findViewById(R.id.btnUMV4);
        iconUMV4 = (TextView) view.findViewById(R.id.iconUMV4);
        btnInjury = (RelativeLayout) view.findViewById(R.id.btnInjury);
        iconInjury = (TextView) view.findViewById(R.id.iconInjury);
        btnNumberChange = (RelativeLayout) view.findViewById(R.id.btnNumberChange);
        iconNumberChange = (TextView) view.findViewById(R.id.iconNumberChange);
        btnU20 = (RelativeLayout) view.findViewById(R.id.btnU20);
        btnUMV = (RelativeLayout) view.findViewById(R.id.btnUMV);
        iconU20 = (TextView) view.findViewById(R.id.iconU20);
        iconUMV = (TextView) view.findViewById(R.id.iconUMV);

        btnList = new RelativeLayout[]{btnGoal, btnInjury, btnNumberChange, btnU20, btnUMV, btnUMV4};
        iconList = new TextView[] {iconGoal, iconInjury, iconNumberChange, iconU20, iconUMV,iconUMV4};

        setIconFont(iconList);

        return view;
    }

    private void setIconFont(TextView[] icons) {
        for(TextView icon: icons) {
            icon.setAlpha(0.4f);
            icon.setTypeface(FontManager.getTypeface(getContext(), FontManager.FONTAWESOME));
        }
    }
}
