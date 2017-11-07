package persistency;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;


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

    @SerializedName("home_score")
    private int scoreHome;
    @SerializedName("visitor_score")
    private int scoreAway;

    @SerializedName("date")
    private Date date;

    @SerializedName("location_id")
    private int location_id;
    @SerializedName("valor_id")
    private int valor_id;
    @SerializedName("difficulty_id")
    private int difficulty_id;
    //@SerializedName("divisioin_id")
    //private int division_id;


    public MatchRest(int match_id, int home_id, int visitor_id, int scoreHome, int scoreAway, Date date, int location_id, int valor_id, int difficulty_id) {
        this.match_id = match_id;
        this.home_id = home_id;
        this.visitor_id = visitor_id;
        this.scoreHome = scoreHome;
        this.scoreAway = scoreAway;
        this.date = date;
        this.location_id = location_id;
        this.valor_id = valor_id;
        this.difficulty_id = difficulty_id;
    }

    public int getMatch_id() {
        return match_id;
    }

    public int getHome_id() {
        return home_id;
    }

    public int getVisitor_id() {
        return visitor_id;
    }

    public int getScoreHome() {
        return scoreHome;
    }

    public int getScoreAway() {
        return scoreAway;
    }

    public Date getDate() {
        return date;
    }

    public int getLocation_id() {
        return location_id;
    }

    public int getValor_id() {
        return valor_id;
    }

    public int getDifficulty_id() {
        return difficulty_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public void setHome_id(int home_id) {
        this.home_id = home_id;
    }

    public void setVisitor_id(int visitor_id) {
        this.visitor_id = visitor_id;
    }

    public void setScoreHome(int scoreHome) {
        this.scoreHome = scoreHome;
    }

    public void setScoreAway(int scoreAway) {
        this.scoreAway = scoreAway;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public void setValor_id(int valor_id) {
        this.valor_id = valor_id;
    }

    public void setDifficulty_id(int difficulty_id) {
        this.difficulty_id = difficulty_id;
    }
}
