package views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.g1020.waterpolo.R;

import java.util.List;

import Domain.Domaincontroller;
import application.ApplicationRuntime;
import persistency.DivisionRest;
import persistency.MatchRest;

/**
 * Created by laure on 10/12/2017.
 */

public class CustomDivisionRestListAdapter  extends ArrayAdapter<DivisionRest> {
    private ApplicationRuntime ar = ApplicationRuntime.getInstance();  //this adds temporary code to this class
    private Domaincontroller dc = ar.getDc();




    public CustomDivisionRestListAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<DivisionRest> divisions) {
        super(context, textViewResourceId, divisions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View v = convertView;
        MyHolder holder;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());





            //here i look if the the match is selected, if so it will show a more detailed listitem


            v = vi.inflate(R.layout.division_list_item, null);
            holder = new MyHolder(v);
            v.setTag(holder);










        }
        else {
            holder = (MyHolder) v.getTag();
        }
        holder.txtdivisionName.setText(getItem(position).getDivision_name());
        return v;
    }
    public class MyHolder {
        TextView txtdivisionName;
        public MyHolder(View v) {
            txtdivisionName = (TextView) v.findViewById(R.id.txtDivisionName);
        }


    }}


