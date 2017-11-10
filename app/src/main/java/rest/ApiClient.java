package rest;

/**
 * Created by timos on 7-11-2017.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import Utils.GsonUTCDateAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    public static final String BASE_URL = "http://voom.be:12005/";
    private static Retrofit retrofit = null;



    public static Retrofit getClient() {
        private final DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);      //This is the format I need
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Gson gson = new GsonBuilder()
                .setDateFormat(String.valueOf(dateFormat))
                .create();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
