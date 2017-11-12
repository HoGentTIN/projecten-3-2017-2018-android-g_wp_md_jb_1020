package Domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Created by timos on 5-10-2017.
 */

public class Match {

    //Variables
    private int match_id;
    private int home_id;
    private int visitor_id;

    private String matchLog;            //keep this unless there is another way to better keep a written down form of the match progress

    private List<Team> teams;               //always size 2 (1 is hometeam, 2 is awayteam)
    private List<Goal> scoreHome;
    private List<Goal> scoreAway;
    private Official official;       //first official is always main official
    private List<Quarter> quarters;         //list of maximum 4 length initialize in start admin when setting up quarterlenght

    private Date date;
    private Location location;
    private Valor valor;


    private Difficulty difficulty;
    private int currentQuarter; // Question? do we prepare 4 quarters in the list or do we add to the list each time we start a new quarter


    //Constructor
    public Match(){
        teams = new ArrayList<>(2);
        quarters = new ArrayList<Quarter>(4);
        Quarter q1 = new Quarter();
        q1.setQuarterPeriod(1);
        quarters.add(q1);

        scoreAway = new ArrayList<Goal>();
        scoreHome = new ArrayList<Goal>();
    }

    //Functions
    public Location getLocation() {
        return location;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Quarter> getQuarters() {
        return quarters;
    }

    public void addTeams(Team homeTeam, Team awayTeam){
        teams.add(0, homeTeam);
        teams.add(1, awayTeam);
    }

    public Team getTeam(int i){
        return teams.get(i);
    }

    public int getScoreHome() {
        return scoreHome.size();
    }

    public int getScoreAway() {
        return scoreAway.size();
    }

    public void addGoal(Goal g){
        if(teams.get(0).equals(g.getPlayer().getTeam())){
            scoreHome.add(g);
        } else {
            scoreAway.add(g);
        }
    }
    public void setDate(Date date){
        this.date = date;
    }
    public Date getDate(){
        return date;
    }

    //Function calculate total match score from rounds

}
