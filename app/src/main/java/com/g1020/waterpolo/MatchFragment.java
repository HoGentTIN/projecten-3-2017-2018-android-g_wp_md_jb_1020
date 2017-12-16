package com.g1020.waterpolo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import application.ApplicationRuntime;
import Domain.Domaincontroller;
import Domain.Match;
import persistency.DivisionRest;
import persistency.MatchRest;
import views.CustomDivisionRestListAdapter;
import views.CustomMatchRestListAdapter;


public class MatchFragment extends Fragment {

    Domaincontroller dc;
    private static MatchFragment mf;
    List<MatchRest> hostedmatches;
    CustomMatchRestListAdapter matchAdapter;
    List<MatchRest> hostedmatchesR;
    CustomMatchRestListAdapter matchRestAdapter;
    CustomDivisionRestListAdapter divisionRestAdapter;
    private Dialog dialog;

    Match selectedMatch;

    private ListView lvMatches;
    private ListView lvDivisions;
    private Button btnFilter;
    private OnMatchSelectedListener mListener;

    public MatchFragment() {
        ApplicationRuntime ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
        // Required empty public constructor
    }
    public interface OnMatchSelectedListener{
        public void onMatchSelected(int position);
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
        btnFilter = (Button) view.findViewById(R.id.btnFilter);
        filterClickAction(btnFilter);


        List<MatchRest> filterList = new ArrayList<MatchRest>();

        filterList =  dc.getOwnedMatchesR();
        hostedmatchesR = new ArrayList<MatchRest>();
        for(MatchRest match : filterList){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date strDate = sdf.parse(match.getRealFullDate());
                if (! new Date().after(strDate)){
                    if(dc.getSelectedDivisionName()==null)
                        hostedmatchesR.add(match);
                    else{
                        if(match.getDivision().getDivision_name()==dc.getSelectedDivisionName()){
                            hostedmatchesR.add(match);
                        }
                    }
                }
            } catch (ParseException e) {
                Log.e("log_tag","de datumconversie is niet gelukt");
            }

        }


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

                MatchRest selectedMatch = (MatchRest) listview.getItemAtPosition(position);

                dc.setMatch(selectedMatch.getMatch_id());

                mListener.onMatchSelected(position);
                //Change look of selected item and the others






            }
        });
    }
    public void changeMatch(int position){
        int itemAmount = lvMatches.getChildCount();
        CustomMatchRestListAdapter ca = (CustomMatchRestListAdapter) lvMatches.getAdapter();
        ca.setSelectedMatch(position,  lvMatches.getChildAt(position));

        for (int i =0;i<itemAmount;i++){
            if(i!=position)
                ca.unselectMatch(i,lvMatches.getChildAt(i));
        }
    }
    private void divisionClickAction(final ListView listview) {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // retrieve the selected player

                DivisionRest selectedDivision = (DivisionRest) listview.getItemAtPosition(position);

                dc.setSelectedDivisionNameTemp(selectedDivision.getDivision_name());
                List<MatchRest> filterList = new ArrayList<MatchRest>();
                hostedmatchesR = new ArrayList<MatchRest>();
                filterList =  dc.getOwnedMatchesR();
                dc.setSelectedDivisionName();
                for(MatchRest match : filterList){
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date strDate = sdf.parse(match.getRealFullDate());
                        if (! new Date().after(strDate)){
                            if(dc.getSelectedDivisionName()==null||dc.getSelectedDivisionName().equals("All"))
                                hostedmatchesR.add(match);
                            else{
                                if(match.getDivision().getDivision_name().equals(dc.getSelectedDivisionName())){
                                    hostedmatchesR.add(match);
                                }
                            }
                        }
                    } catch (ParseException e) {
                        Log.e("log_tag","de datumconversie is niet gelukt");
                    }

                }
                matchRestAdapter = new CustomMatchRestListAdapter(getContext(),android.R.id.text1,hostedmatchesR);

                lvMatches.setAdapter(matchRestAdapter);
                dialog.dismiss();








                //Change look of selected item and the others






            }
        });
    }
    private void filterClickAction(Button btnfilter){
        btnfilter.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.division_filter);
                dialog.show();

                lvDivisions = (ListView) dialog.findViewById(R.id.lsvDivisions);
                List<DivisionRest> divisions = dc.getDivisions();
                divisionRestAdapter = new CustomDivisionRestListAdapter(getContext(),android.R.id.text1, divisions);
                lvDivisions.setAdapter(divisionRestAdapter);
                divisionClickAction(lvDivisions);



            }


        });
    }
}
