package Domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class that holds the information for a waterpolo match
 * It holds both teams, goals scored and faults committed.
 */
public class Match {

    //Variables
    private int match_id;
    private Date date;
    private Location location;
    private boolean cancelled;

    private Team homeTeam;
    private Team awayTeam;
    private List<Goal> goals;
    private PenaltyBook penaltyBook;

    private int currentRound;


    //Constructor
    public Match(){
        goals = new ArrayList<Goal>();
        penaltyBook = new PenaltyBook();
        cancelled = false;
        currentRound = 1;
    }

    //Functions
    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int cr) {
        this.currentRound = cr;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team home) {
        this.homeTeam = home;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public PenaltyBook getPenaltyBook() {
        return penaltyBook;
    }

    /**
     * Method that calculates the number of goals a certain team has scored.
     */
    public int getScoreForTeam(int teamId) {

        int numberOfGoals = 0;

        for(Goal g: goals){
            if(g.getTeamid() == teamId){
                numberOfGoals++;
            }
        }
        return numberOfGoals;
    }

    /**
     * Method that adds a goal to this match.
     */
    public void addGoal(Goal g){
        goals.add(g);
    }

}
