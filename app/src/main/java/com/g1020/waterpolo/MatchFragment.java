package com.g1020.waterpolo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Application.ApplicationRuntime;
import Domain.Domaincontroller;
import Domain.Match;


public class MatchFragment extends Fragment {
    Domaincontroller dc;
    private static MatchFragment mf;
    List<Match> hostedmatches;
    Match selectedMatch;
    private OnMatchSelectedListener mListener;

    public MatchFragment() {
        ApplicationRuntime ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
        // Required empty public constructor
    }
public interface OnMatchSelectedListener{
        public void onMatchSelected(int matchNumber);
}

    public static MatchFragment newInstance(int matchNumber) {
        mf = new MatchFragment();
        Bundle args = new Bundle();
args.putInt("matchnumber",matchNumber);
        mf.setArguments(args);
        return mf;
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_match, container, false);
        TextView txtTeams = (TextView) view.findViewById(R.id.txtTeams);
        TextView txtDate = (TextView) view.findViewById(R.id.txtDatum);
       txtTeams.setText( dc.getMatch().getTeam(0).getTeamName()+" - "+dc.getMatch().getTeam(1).getTeamName());
       txtDate.setText(dc.getMatch().getDate().getDay() +"/"+dc.getMatch().getDate().getMonth());


       return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (MatchFragment.OnMatchSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPlayerSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
