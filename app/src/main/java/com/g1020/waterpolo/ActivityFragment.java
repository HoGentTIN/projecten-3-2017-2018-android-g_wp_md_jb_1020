package com.g1020.waterpolo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Application.ApplicationRuntime;
import Domain.Domaincontroller;
import views.FontManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment {

    Domaincontroller dc;
    ListView roundActivities;
    String[] activityArray;
    private static ActivityFragment activityFragment;
    RelativeLayout btnUndo,btnRevert;
    TextView iconUndo, iconRevert;

    public ActivityFragment() {
        // Required empty public constructor
        ApplicationRuntime ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
    }

    public static ActivityFragment newInstance(int round){
        activityFragment = new ActivityFragment();

        Bundle args = new Bundle();
        args.putInt("roundNumber", round);
        activityFragment.setArguments(args);

        return activityFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        activityArray = dc.getLogForRound(getArguments().getInt("roundNumber")).toArray(new String[0]);

        btnUndo = (RelativeLayout) view.findViewById(R.id.btnUndoAction);
        btnRevert = (RelativeLayout) view.findViewById(R.id.btnRevertToAction);
        iconRevert = (TextView) view.findViewById(R.id.iconRevertToAction);
        iconUndo = (TextView) view.findViewById(R.id.iconUndoAction);
        setIconFont(iconRevert);
        setIconFont(iconUndo);

        roundActivities = (ListView) view.findViewById(R.id.lsvActivities);
        roundActivities.setAdapter(createListAdaptor(activityArray));
        return view;
    }

    public ArrayAdapter createListAdaptor(String[] activities){

        //reverse list to put last activity on top
        List<String> tempList = Arrays.asList(activities);
        Collections.reverse(tempList);
        return new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tempList);
    }

    public void updateActivities(int round){
        activityArray = dc.getLogForRound(round).toArray(new String[0]);
        roundActivities.setAdapter(createListAdaptor(activityArray));
    }

    private void setIconFont(TextView icon) {
            icon.setTypeface(FontManager.getTypeface(getContext(), FontManager.FONTAWESOME));
            icon.setTextColor(Color.BLACK);
        }
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

