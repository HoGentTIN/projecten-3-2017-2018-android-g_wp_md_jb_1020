package rest;

/**
 * Created by timos on 7-11-2017.
 */

import persistency.MatchRestResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("api/matches")
    Call<MatchRestResponse> getMatches();
}
