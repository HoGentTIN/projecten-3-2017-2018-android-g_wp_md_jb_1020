package com.g1020.waterpolo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * This fragment is used to display the Shotclock used in {@link MatchControl}
 */
public class ShotClockFragment extends Fragment {

    private TextView txtShotClock;

    public TextView getTxtShotClock() {
        return txtShotClock;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shotclock, container, false);

        txtShotClock = (TextView) view.findViewById(R.id.txtShotlock);

        return view;
    }

}
