package com.g1020.waterpolo;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import views.FontManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityButtonsFragment extends Fragment {


    private TextView iconGoal, iconUMV4;
    private Button btnInjury, btnNumberChange, btnU20, btnUMV;
    private RelativeLayout btnGoal, btnUMV4;
    private RelativeLayout btnList[];
    private TextView iconList[];

    public ActivityButtonsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_buttons, container, false);

        btnGoal = (RelativeLayout) view.findViewById(R.id.btnGoal);
        iconGoal = (TextView) view.findViewById(R.id.iconGoal);
        btnUMV4 = (RelativeLayout) view.findViewById(R.id.btnUMV4);
        iconUMV4 = (TextView) view.findViewById(R.id.iconUMV4);

        btnInjury = (Button) view.findViewById(R.id.btnInjury);
        btnNumberChange = (Button) view.findViewById(R.id.btnNumberChange);
        btnU20 = (Button) view.findViewById(R.id.btnU20);
        btnUMV = (Button) view.findViewById(R.id.btnUMV);

        btnList = new RelativeLayout[]{btnGoal/*, btnInjury, btnNumberChange, btnU20, btnUMV, btnUMV4*/};
        iconList = new TextView[] {iconGoal,iconUMV4};

        setIconFont();

        return view;
    }

    public void disableButtons(){
        for(RelativeLayout rl: btnList){
            rl.setEnabled(false);
        }
    }

    public void enableButtons(){
        for(RelativeLayout rl: btnList){
            rl.setEnabled(true);
        }
    }

    private void setIconFont() {
        for(TextView icon: iconList) {
            icon.setAlpha(0.4f);
            icon.setTypeface(FontManager.getTypeface(getContext(), FontManager.FONTAWESOME));
        }
    }

}
