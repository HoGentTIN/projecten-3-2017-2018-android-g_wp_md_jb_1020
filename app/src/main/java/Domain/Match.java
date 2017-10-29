package Domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by timos on 5-10-2017.
 */

public class Match {

    //Variables
    private int match_id;
    private int home_id;
    private int visitor_id;

    private String matchLog;            //keep this unless there is another way to better keep a written down form of the match progress

    private List<Team> teams = new ArrayList<>();               //always size 2 (1 is hometeam, 2 is awayteam)
    private List<Official> officials;       //first official is always main official
    private List<Quarter> quarters;         //list of maximum 4 length initialize in start admin when setting up quarterlenght

    private Date date;
    private Location location;
    private Valor valor;


    private Difficulty difficulty;
    private int currentQuarter; // Question? do we prepare 4 quarters in the list or do we add to the list each time we start a new quarter

    //Constructor


    //Functions
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addTeams(List<Team> teams){
        teams.addAll(teams);
    }

    public void addTeam(Team team){
        teams.add(team);
    }

    public Team getHomeTeam(){
        return teams.get(0);
    }

    //Function calculate total match score from rounds

}
