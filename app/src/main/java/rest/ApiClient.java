package rest;

/**
 * Created by timos on 7-11-2017.
 */
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.g1020.waterpolo.AdministrationEnd;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import Domain.Domaincontroller;
import Utils.GsonUTCDateAdapter;
import application.ApplicationRuntime;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient extends AsyncTask {

    public static final String BASE_URL = "http://voom.be:12005/";
    private static Retrofit retrofit = null;
    private static ApiInterface resource;

    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {

                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    ApplicationRuntime ar = ApplicationRuntime.getInstance();
                    Domaincontroller dc = ar.getDc();

                    Request request = chain.request();
                    okhttp3.Response response = chain.proceed(request);

                    Log.i("servertest","" + request);
                    Log.i("servertest","" + response);

                    switch(response.code()) {
                        case 400:
                            Toast.makeText(dc.getCurrentActivity().getApplicationContext(), response.code() +
                                    "Authentication failed when signing form, please fill in your credentials correctly", Toast.LENGTH_SHORT).show();

                            return response;
                        case 401:
                            Toast.makeText(dc.getCurrentActivity().getApplicationContext(), response.code() +
                                    "Authentication failed when signing form, please fill in your credentials correctly", Toast.LENGTH_SHORT).show();
                            return response;
                        case 402:
                            Toast.makeText(dc.getCurrentActivity().getApplicationContext(), response.code() +
                                    "Authentication failed when signing form, please fill in your credentials correctly", Toast.LENGTH_SHORT).show();
                            return response;
                        case 403:
                            AdministrationEnd adminEnd = (AdministrationEnd) dc.getCurrentActivity();
                            adminEnd.toast();
                            return response;
                        case 422:
                            adminEnd = (AdministrationEnd) dc.getCurrentActivity();
                            adminEnd.toast();
                            return response;
                        case 500:
                            Toast.makeText(dc.getCurrentActivity().getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                            Log.i("servertest", "" + response);
                            return response;
                        case 503:
                            Toast.makeText(dc.getCurrentActivity().getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                            Log.i("servertest", "" + response);
                            return response;
                        case 504:
                            Toast.makeText(dc.getCurrentActivity().getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                            Log.i("servertest", "" + response);
                            return response;

                        default:
                                Log.i("servertest", "shit happens " + response.code());
                    }
                        return response;
                }

            })
            .build();


    public static Retrofit getClient() {
       // DateFormat dateFormat;
        //dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);      //This is the format I need
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).client(okHttpClient)
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
