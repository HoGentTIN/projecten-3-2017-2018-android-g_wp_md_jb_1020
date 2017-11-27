package rest;

/**
 * Created by timos on 7-11-2017.
 */
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import Utils.GsonUTCDateAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient extends AsyncTask {

    public static final String BASE_URL = "http://voom.be:12005/";
    private static Retrofit retrofit = null;
    private static ApiInterface resource;



    public static Retrofit getClient() {
       // DateFormat dateFormat;
        //dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);      //This is the format I need
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;

    }

    public static class ArrayListStarters{
        @Expose
        private ArrayList<Starter> starters;
        public ArrayListStarters() {
            //this.starters=starters;
        }

        public void addStarters(ArrayList<Starter> arrStarters){
            this.starters = arrStarters;
        }
    }

    public static class Starter {
        private int player_id;
        private int starter;

        public Starter(int player_id, int starter){
            this.player_id = player_id;
            this.starter = starter;
        }
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
    public  class ObjectPlayerStarters{
        @SerializedName("player_id")
        int player_id;

        @SerializedName("starter") int starter;

    }
}
