package views;

import android.view.View;
import android.widget.TextView;

import com.g1020.waterpolo.R;

/**
 * Holder object for players in the listview.
 * Has a field for the playernumber, playername and one to indicate the player's faults.
 */
class PlayerHolder {

    PlayerNumberTextView playerNumberTxtV;
    TextView txtPlayername;
    TextView txtFaultField;

    PlayerHolder(View v) {
            playerNumberTxtV = (PlayerNumberTextView) v.findViewById(R.id.txtplayernumber);
            txtPlayername = (TextView) v.findViewById(R.id.txtplayername);
            txtFaultField = (TextView) v.findViewById(R.id.txtFaultField);
        }

}
