package views;

import android.view.View;
import android.widget.TextView;

import com.g1020.waterpolo.R;

/**
 * Created by pieter on 27/11/2017.
 */

public class PlayerHolder {

    PlayerNumberTextView playerNumberTxtV;
    TextView txtPlayername;
    TextView txtFaultField;

        public PlayerHolder(View v) {
            playerNumberTxtV = (PlayerNumberTextView) v.findViewById(R.id.txtplayernumber);
            txtPlayername = (TextView) v.findViewById(R.id.txtplayername);
        }

}
