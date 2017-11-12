package com.g1020.waterpolo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import Application.ApplicationRuntime;
import Domain.Domaincontroller;
import Domain.Match;
import views.CustomMatchListAdapter;


public class MatchFragment extends Fragment {

    Domaincontroller dc;
    private static MatchFragment mf;
    List<Match> hostedmatches;
    CustomMatchListAdapter matchAdapter;
    Match selectedMatch;

    private ListView lvMatches;
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
       View view = inflater.inflate(R.layout.fragment_matches, container, false);
lvMatches = (ListView) view.findViewById(R.id.lsvMatches);
       hostedmatches = dc.getOwnedMatches();

       matchAdapter = new CustomMatchListAdapter(getContext(),android.R.id.text1,hostedmatches);
lvMatches.setAdapter(matchAdapter);


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

}
