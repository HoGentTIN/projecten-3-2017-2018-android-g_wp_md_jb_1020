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
    private Team home;
    @SerializedName("visitor")
    private Team visitor;


    @SerializedName("location")
    private Location location;
    @SerializedName("valor")
    private Valor valor;
    @SerializedName("difficulty")
    private Difficulty difficulty;

    public MatchRest(int match_id, int home_id, int visitor_id, int scoreHome, int scoreAway, String date, int location_id, int valor_id, int difficulty_id, int division_id, Team home, Team visitor, Location location, Valor valor, Difficulty difficulty) {
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

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public int getHome_id() {
        return home_id;
    }

    public void setHome_id(int home_id) {
        this.home_id = home_id;
    }

    public int getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(int visitor_id) {
        this.visitor_id = visitor_id;
    }

    public int getScoreHome() {
        return scoreHome;
    }

    public void setScoreHome(int scoreHome) {
        this.scoreHome = scoreHome;
    }

    public int getScoreAway() {
        return scoreAway;
    }

    public void setScoreAway(int scoreAway) {
        this.scoreAway = scoreAway;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public int getValor_id() {
        return valor_id;
    }

    public void setValor_id(int valor_id) {
        this.valor_id = valor_id;
    }

    public int getDifficulty_id() {
        return difficulty_id;
    }

    public void setDifficulty_id(int difficulty_id) {
        this.difficulty_id = difficulty_id;
    }

    public int getDivision_id() {
        return division_id;
    }

    public void setDivision_id(int division_id) {
        this.division_id = division_id;
    }

    public Team getHome() {
        return home;
    }

    public void setHome(Team home) {
        this.home = home;
    }

    public Team getVisitor() {
        return visitor;
    }

    public void setVisitor(Team visitor) {
        this.visitor = visitor;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Valor getValor() {
        return valor;
    }

    public void setValor(Valor valor) {
        this.valor = valor;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}


