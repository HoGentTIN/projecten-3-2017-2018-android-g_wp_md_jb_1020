package com.g1020.waterpolo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import application.ApplicationRuntime;
import Domain.Domaincontroller;
import views.FontManager;

/**
 * A simple {@link Fragment} subclass.
 * This fragment is used to display the action buttons displayed in {@link MatchControl} & {@link AdministrationEnd}
 */
public class ButtonsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buttons, container, false);

        RelativeLayout btnGoal = (RelativeLayout) view.findViewById(R.id.btnGoal);
        TextView iconGoal = (TextView) view.findViewById(R.id.iconGoal);
        RelativeLayout btnUMV4 = (RelativeLayout) view.findViewById(R.id.btnUMV4);
        TextView iconUMV4 = (TextView) view.findViewById(R.id.iconUMV4);
        RelativeLayout btnInjury = (RelativeLayout) view.findViewById(R.id.btnInjury);
        TextView iconInjury = (TextView) view.findViewById(R.id.iconInjury);
        RelativeLayout btnNumberChange = (RelativeLayout) view.findViewById(R.id.btnNumberChange);
        TextView iconNumberChange = (TextView) view.findViewById(R.id.iconNumberChange);
        RelativeLayout btnU20 = (RelativeLayout) view.findViewById(R.id.btnU20);
        RelativeLayout btnUMV = (RelativeLayout) view.findViewById(R.id.btnUMV);
        TextView iconU20 = (TextView) view.findViewById(R.id.iconU20);
        TextView iconUMV = (TextView) view.findViewById(R.id.iconUMV);

        RelativeLayout[] btnList = new RelativeLayout[]{btnGoal, btnInjury, btnNumberChange, btnU20, btnUMV, btnUMV4};
        TextView[] iconList = new TextView[]{iconGoal, iconInjury, iconNumberChange, iconU20, iconUMV, iconUMV4};

        setIconFont(iconList);

        return view;
    }
    /**
     * Method to set the Font for the icons on the buttons
     */
    private void setIconFont(TextView[] icons) {
        for(TextView icon: icons) {
            icon.setAlpha(0.4f);
            icon.setTypeface(FontManager.getTypeface(getContext(), FontManager.FONTAWESOME));
        }
    }
}
