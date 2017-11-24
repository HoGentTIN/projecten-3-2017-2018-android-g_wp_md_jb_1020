package views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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
    private TextView txtDateDetail;
    private TextView txtTime;
    private TextView txtDivision;
    private TextView txtLocation;


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

            //here i look if the the match is selected, if so it will show a more detailed listitem


            v = vi.inflate(R.layout.list_item_match, null);

            txtTeams = (TextView) v.findViewById(R.id.txtTeamsDetail);
            txtDate = (TextView) v.findViewById(R.id.txtDatum);
            txtDateDetail = (TextView) v.findViewById(R.id.txtDatumDetail);
            txtDivision = (TextView)v.findViewById(R.id.txtDivision);
            txtLocation=(TextView)v.findViewById(R.id.txtLocation);
            txtTime = (TextView)v.findViewById(R.id.txtTime);



            txtTeams.setText(p.getHome().getTeamName()+" - "+p.getVisitor().getTeamName());

            txtDate.setText(p.getRealFullDate().toString());
            txtDateDetail.setText(p.getRealDate().toString());
            txtDivision.setText("Division:  "+p.getHome().getDivision().getDivision_name());
            txtLocation.setText("Locatie:   " +p.getLocation().getFullAddress());
            txtTime.setText("TIME:   "/*+ p.getRealTime().toString()*/);
            // txtDivision.setText("Division "+ p.getDivision().getDivisionName());
        }
        return v;
    }
    public void setSelectedMatch(int position,View convertView){
        View v = convertView;
        LinearLayout llunfold = (LinearLayout) v.findViewById(R.id.llUnfold);
        TextView txtDate = (TextView) v.findViewById(R.id.txtDatum);
        TextView txtTeams = (TextView)v.findViewById(R.id.txtTeamsDetail);
        txtTeams.setGravity(Gravity.CENTER_HORIZONTAL);
        txtDate.setVisibility(View.GONE);
        llunfold.setVisibility(View.VISIBLE);



    }
    public void unselectMatch(int position,View convertView){
        View v = convertView;
        LinearLayout llunfold = (LinearLayout) v.findViewById(R.id.llUnfold);
        TextView txtDate = (TextView) v.findViewById(R.id.txtDatum);
        TextView txtTeams = (TextView)v.findViewById(R.id.txtTeamsDetail);
        txtTeams.setGravity(Gravity.LEFT);
        txtTeams.setGravity(Gravity.CENTER_VERTICAL);
        txtDate.setVisibility(View.VISIBLE);
        llunfold.setVisibility(View.GONE);



    }
}
