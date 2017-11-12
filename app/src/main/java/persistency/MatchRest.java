package persistency;

import com.google.gson.annotations.SerializedName;

import Domain.Difficulty;
import Domain.Location;
import Domain.Team;
import Domain.Valor;


/**
 * Created by timos on 7-11-2017.
 */

public class MatchRest {

    @SerializedName("id")
    private int match_id;
    @SerializedName("home_id")
    private int home_id;
    @SerializedName("visitor_id")
    private int visitor_id;
    @SerializedName("score_home")
    private int scoreHome;
    @SerializedName("score_visitor")
    private int scoreAway;
    @SerializedName("date")
    private String date;
    @SerializedName("location_id")
    private int location_id;
    @SerializedName("valor_id")
    private int valor_id;
    @SerializedName("difficulty_id")
    private int difficulty_id;
    @SerializedName("division_id")
    private int division_id;


    @SerializedName("home")
    private TeamRest home;
    @SerializedName("visitor")
    private TeamRest visitor;


    @SerializedName("location")
    private Location location;
    @SerializedName("valor")
    private Valor valor;
    @SerializedName("difficulty")
    private Difficulty difficulty;

    public MatchRest(int match_id, int home_id, int visitor_id, int scoreHome, int scoreAway, String date, int location_id, int valor_id, int difficulty_id, int division_id, TeamRest home, TeamRest visitor, Location location, Valor valor, Difficulty difficulty) {
        this.match_id = match_id;
        this.home_id = home_id;
        this.visitor_id = visitor_id;
        this.scoreHome = scoreHome;
        this.scoreAway = scoreAway;
        this.date = date;
        this.location_id = location_id;
        this.valor_id = valor_id;
        this.difficulty_id = difficulty_id;
        this.division_id = division_id;
        this.home = home;
        this.visitor = visitor;
        this.location = location;
        this.valor = valor;
        this.difficulty = difficulty;
    }
}


