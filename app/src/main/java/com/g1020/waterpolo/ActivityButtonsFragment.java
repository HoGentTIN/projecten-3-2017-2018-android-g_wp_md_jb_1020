package com.g1020.waterpolo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityButtonsFragment extends Fragment {

    Button btnGoal, btnInjury, btnNumberChange, btnU20, btnUMV, btnUMV4;
    private Button btnList[];

    public ActivityButtonsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_buttons, container, false);

        btnGoal = (Button) view.findViewById(R.id.btnGoal);
        btnInjury = (Button) view.findViewById(R.id.btnInjury);
        btnNumberChange = (Button) view.findViewById(R.id.btnNumberChange);
        btnU20 = (Button) view.findViewById(R.id.btnU20);
        btnUMV = (Button) view.findViewById(R.id.btnUMV);
        btnUMV4 = (Button) view.findViewById(R.id.btnUMV4);

        btnList = new Button[]{btnGoal, btnInjury, btnNumberChange, btnU20, btnUMV, btnUMV4};

        return view;
    }

    public void disableButtons(){
        for(Button b: btnList){
            b.setEnabled(false);
        }
    }

    public void enableButtons(){
        for(Button b: btnList){
            b.setEnabled(true);
        }
    }

}
