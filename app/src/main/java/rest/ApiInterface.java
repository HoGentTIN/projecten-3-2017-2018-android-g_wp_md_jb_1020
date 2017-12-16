package rest;

/**
 * Created by timos on 7-11-2017.
 */

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import persistency.DivisionRest;
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
    @GET("api/matches/active")
    Call<List<MatchRest>> getMatches();

    //Get all matches belonging to logged in official get list matchrest where official id = ... or get Logged in OfficialRest and retrieve his matches
    //1)get matches of official -> in app select map use selected id to

    //2)Get match with id retrieved id and fill dc data


    //Get all matches being played today?

    //get team
    @GET("api/teams/{id}")
    Call<TeamRest> getTeam(@Path("id") int id);



    //Update goal
    @FormUrlEncoded
    @POST("api/goals/")
    Call<Void> addGoal(
            @Field("match_id") int match_id,
            @Field("player_id") int player_id,
            @Field("quarter") int roundNumber
    );
    //

    @FormUrlEncoded
    @POST("api/penaltybooks/")
    Call<Void> addPenalty(
            @Field("match_id") int match_id,
            @Field("player_id") int player_id,
            @Field("penalty_type_id") int penalty_id
    );


    @PUT("api/matches/{id}/starters")
    Call<ResponseBody> putListOfStarters(
            @Path("id") int id,
            @Body ApiClient.ArrayListStarters starters
        );

    @FormUrlEncoded
    @PUT("api/players/{id}/changenumber")
    Call<Void> updateNumber(
            @Path("id") int id,
            @Field("player_number") int player_number

    );


    @PUT("api/matches/{id}/cancel")
    Call<Void> cancelMatch(
            @Path("id") int id

    );

    @PUT("api/matches/{id}/start")
    Call<Void> startMatch(
            @Path("id") int id
    );

    @PUT("api/matches/{id}/end")
    Call<Void> endMatch(
            @Path("id") int id
    );

    @FormUrlEncoded
    @POST("api/matches/{id}/sign")
    Call<Void> signMatch(
            @Path("id") int id,
            @Field("email") String email,
            @Field("password") String password
    );
    @GET("api/divisions")
    Call<List<DivisionRest>> getDivisions();




}

