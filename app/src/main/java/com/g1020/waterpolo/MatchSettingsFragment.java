package com.g1020.waterpolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Application.ApplicationRuntime;
import Domain.Domaincontroller;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.

 * create an instance of this fragment.
 */
public class MatchSettingsFragment extends Fragment {



    public MatchSettingsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    private TextView txtDuration;
    private TextView txtTeamName;
    private TextView txtVisitorName;

    private ApplicationRuntime ar;  //this adds temporary code to this class
    private Domaincontroller dc;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_match_settings, container, false);

        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();

        txtDuration = (TextView)view.findViewById(R.id.txtDuration);
        txtTeamName = (TextView)view.findViewById(R.id.txtTeamName);
        txtVisitorName = (TextView)view.findViewById(R.id.txtVisitorName);


        txtTeamName.setText(dc.getSelectedMatch().getHome().getTeamName());
        txtVisitorName.setText(dc.getSelectedMatch().getVisitor().getTeamName());
txtDuration.setText("8:00");


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    public void endSelection(View view) {
        //get selected match value from layout -> create Match object from the MatchRest Object and store it in the applicationruntime domaincontroller

        Intent intent = new Intent(getContext(), MatchControl.class);

        //Intent intent = new Intent(this, AdministrationSetup.class);
        startActivity(intent);
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


}
