package com.g1020.waterpolo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import Application.ApplicationRuntime;
import Domain.Domaincontroller;
import Domain.Match;
import persistency.MatchRest;
import views.CustomMatchRestListAdapter;


public class MatchFragment extends Fragment {

    Domaincontroller dc;
    private static MatchFragment mf;
    List<MatchRest> hostedmatches;
    CustomMatchRestListAdapter matchAdapter;
    List<MatchRest> hostedmatchesR;
    CustomMatchRestListAdapter matchRestAdapter;

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



        hostedmatchesR = dc.getOwnedMatchesR();
        matchRestAdapter = new CustomMatchRestListAdapter(getContext(),android.R.id.text1,hostedmatchesR);

        lvMatches.setAdapter(matchRestAdapter);
        matchClickAction(lvMatches);


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
    private void matchClickAction(final ListView listview) {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // retrieve the selected player
                int itemAmount = listview.getChildCount();
                MatchRest selectedMatch = (MatchRest) listview.getItemAtPosition(position);

                mListener.onMatchSelected(selectedMatch.getMatch_id());

                //Change look of selected item and the others
                CustomMatchRestListAdapter ca = (CustomMatchRestListAdapter) lvMatches.getAdapter();
                for (int i =0;i<itemAmount;i++){
                    ca.unselectMatch(i,listview.getChildAt(i));
                }
                ca.setSelectedMatch(position,  listview.getChildAt(position));



            }
        });
    }
}
