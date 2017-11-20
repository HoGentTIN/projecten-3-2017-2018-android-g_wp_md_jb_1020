package rest;

/**
 * Created by timos on 7-11-2017.
 */

import java.util.List;

import okhttp3.RequestBody;
import persistency.GoalRest;
import persistency.MatchRest;
import persistency.PlayerRest;
import persistency.TeamRest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    //Get all matches
    @GET("api/matches")
    Call<List<MatchRest>> getMatches();

    //Get all matches belonging to logged in official get list matchrest where official id = ... or get Logged in OfficialRest and retrieve his matches
    //1)get matches of official -> in app select map use selected id to

    //2)Get match with id retrieved id and fill dc data


    //Get all matches being played today?

    //get team
    @GET("api/teams/{id}")
    Call<TeamRest> getTeam(@Path("id") int id);



    //TEST: update goal
    @POST("api/players")
    Call<PlayerRest> addPlayer(@Body PlayerRest rest);

}
