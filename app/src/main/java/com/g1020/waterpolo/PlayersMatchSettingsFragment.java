package com.g1020.waterpolo;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import application.ApplicationRuntime;
import Domain.Domaincontroller;
import Domain.Player;
import persistency.MatchRest;
import persistency.PlayerRest;
import persistency.TeamRest;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import views.CustomPlayerListAdapter;
import views.CustomPlayerRestListAdapter;

import static android.content.ContentValues.TAG;


public class PlayersMatchSettingsFragment extends Fragment {

    private ApplicationRuntime ar;  //this adds temporary code to this class
    private Domaincontroller dc;
    private CustomPlayerRestListAdapter playerAdapter1;
    private CustomPlayerRestListAdapter playerAdapter2;
    private ListView lvPlayers;
    private ListView lvPlayers2;
    private TextView TeamTitle;
    private boolean hometeam;
    ApiInterface apiService;
    public onPlayerClickedInteractionListener pListener;

    // TODO: Rename and change types of parameters

    public interface onPlayerClickedInteractionListener{
       public void setSelectedPlayer(PlayerRest selectedPlayer);
    }

    public void setHometeam(int id){
        if (id ==1)
            hometeam = true;
        else
            hometeam=false;
    }

    public PlayersMatchSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_players, container, false);
        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
        TeamRest team;

        if(hometeam)
            team = dc.getSelectedMatch().getHome();
        else
            team = dc.getSelectedMatch().getVisitor();


        //ga nu nog de dummydata uit match gebruiken omdat de adapter voor players nog op player ipv playerRest staat
        List<PlayerRest> players = new ArrayList<>();


        for (PlayerRest player : team.getPlayers()){
            if(player.getStarter()) {
                players.add(player);
            }
        }
        //playerTitle = (TextView) view.findViewById(R.id.playerTitle);
        // playerTitle.setText(String.format("<-players as starter players that are not starters ->"));
        playerAdapter1 = new CustomPlayerRestListAdapter(getContext(),android.R.id.text1, players);
        List<PlayerRest> players2 = new ArrayList<>();
        for (PlayerRest player : team.getPlayers()){
            if(!player.getStarter()) {
                players2.add(player);
            }
        }
        playerAdapter2= new CustomPlayerRestListAdapter(getContext(),android.R.id.text1,players2);


        lvPlayers = (ListView) view.findViewById(R.id.lsvplayers);

        lvPlayers.setAdapter(playerAdapter1);
        playerClickAction(lvPlayers);
        teamClickAction(lvPlayers);
        lvPlayers2 = (ListView) view.findViewById(R.id.lsvplayers2);

        lvPlayers2.setAdapter(playerAdapter2);
        playerClickAction(lvPlayers2);
        teamClickAction(lvPlayers2);
        CustomPlayerRestListAdapter ca = (CustomPlayerRestListAdapter) lvPlayers.getAdapter();

        for(int i =0 ; i<lvPlayers.getChildCount();i++){
            ca.setSelectedPlayer(i, lvPlayers.getChildAt(i),R.drawable.player_tile);

        }
        ca = (CustomPlayerRestListAdapter) lvPlayers2.getAdapter();

        for(int i =0 ; i<lvPlayers2.getChildCount();i++){
            ca.setSelectedPlayer(i, lvPlayers2.getChildAt(i),R.drawable.player_tile);

        }
        return view;

    }

    private void playerClickAction(final ListView listview) {
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                PlayerRest currentplayer = (PlayerRest)listview.getItemAtPosition(pos);

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.players_detail);
                dialog.show();


                ((TextView)dialog.findViewById(R.id.txtPlayerDetName)).setText("Name:  " + currentplayer.getName());
                ImageView imgPlayer = (ImageView)dialog.findViewById(R.id.imgPlayer);
                new DownloadImageTask(imgPlayer).execute(currentplayer.getPhoto());
                ((TextView)dialog.findViewById(R.id.txtPlayerDetBirthdate)).setText("BirthDate:  " + currentplayer.getBirthDate().substring(0,10));
                ((TextView)dialog.findViewById(R.id.txtPlayerDetNumber)).setText("PlayerNumber:  " + currentplayer.getPlayerNumber());


                //get the team from the database
                apiService = dc.getApiService();
                Call<TeamRest> call = apiService.getTeam(currentplayer.getTeamId());
                call.enqueue(new Callback<TeamRest>() {
                    @Override
                    public void onResponse(Call<TeamRest> call, Response<TeamRest> response) {
                        TeamRest team = response.body();
                        ((TextView)dialog.findViewById(R.id.txtPlayerDetTeam)).setText("Team:  " + team.getTeamName());}
                                 @Override
                                 public void onFailure(Call<TeamRest> call, Throwable t) {
                                     Log.e(TAG,t.toString());
                                 }});





                // set a listener for the ok-button to close the popup
                final View okButton = dialog.findViewById(R.id.okButton); // From layout
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });



                return true;
            }
        });
    }

    private void teamClickAction(final ListView listview) {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // retrieve the selected player

                pListener.setSelectedPlayer((PlayerRest) listview.getItemAtPosition(position));
                CustomPlayerRestListAdapter ca = (CustomPlayerRestListAdapter) lvPlayers.getAdapter();

                for(int i =0 ; i<lvPlayers.getChildCount();i++){
                    ca.setSelectedPlayer(i, lvPlayers.getChildAt(i),R.drawable.player_tile);

                }
                ca = (CustomPlayerRestListAdapter) lvPlayers2.getAdapter();

                for(int i =0 ; i<lvPlayers2.getChildCount();i++){
                    ca.setSelectedPlayer(i, lvPlayers2.getChildAt(i),R.drawable.player_tile);

                }
                ca = (CustomPlayerRestListAdapter) listview.getAdapter();
                ca.setSelectedPlayer(position, listview.getChildAt(position),R.drawable.player_tile_selected);


            }
        });
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            pListener = (onPlayerClickedInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onPlayerClickedInteractionListener");
        }
        try {
            pListener = (onPlayerClickedInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onPlayerClickedInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        pListener = null;


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

}
