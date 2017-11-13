package views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.g1020.waterpolo.R;

import java.util.List;

import Application.ApplicationRuntime;
import Domain.Domaincontroller;
import Domain.Match;
import persistency.MatchRest;

/**
 * Created by timos on 13-11-2017.
 */

public class CustomMatchRestListAdapter extends ArrayAdapter<MatchRest> {

    private ApplicationRuntime ar = ApplicationRuntime.getInstance();  //this adds temporary code to this class
    private Domaincontroller dc = ar.getDc();

    private TextView txtTeams;
    private TextView txtDate;


    public CustomMatchRestListAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<MatchRest> matches) {
        super(context, textViewResourceId, matches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());


            MatchRest p = getItem(position);
            if (p!=dc.getMatchR()) {
                v = vi.inflate(R.layout.list_item_match, null);

                txtTeams = (TextView) v.findViewById(R.id.txtTeams);
                txtDate = (TextView) v.findViewById(R.id.txtDatum);

                txtTeams.setText(p.getHome().getTeamName()+" - "+p.getVisitor().getTeamName());
                txtDate.setText(p.getDate());
            }

        }
        return v;
    }
}
