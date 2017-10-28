package com.g1020.waterpolo;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import Domain.Domaincontroller;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment {

    ListView roundActivities;
    String[] activityArray;

    public ActivityFragment() {
        // Required empty public constructor
    }

    public ActivityFragment(Domaincontroller dc, int round) {
        // INPUT PIETER NEEDED - add arguments outside constructor
        activityArray = dc.getLogForRound(round).toArray(new String[0]);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        roundActivities = (ListView) view.findViewById(R.id.lsvActivities);
        roundActivities.setAdapter(createListAdaptor(activityArray));
        return view;
    }

    public ArrayAdapter createListAdaptor(String[] activities){
        return new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, activities);
    }

    public void updateActivities(Domaincontroller dc, int round){
        activityArray = dc.getLogForRound(round).toArray(new String[0]);
    }



/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/

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
    /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */

}
