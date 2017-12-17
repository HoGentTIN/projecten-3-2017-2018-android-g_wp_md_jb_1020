package persistency;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Impling on 12-Nov-17.
 */

public class TeamRest {

    //Variables
    @SerializedName("id")
    private int team_id;
    @SerializedName("name")
    private String teamName;

    @SerializedName("division_id")
    private int division_id;
    @SerializedName("division")
    private DivisionRest division;

    @SerializedName("players")
    private List<PlayerRest> players = new ArrayList<>();

    @SerializedName("logo")
    private String logo;


    public TeamRest(String teamName, int division_id, DivisionRest division, List<PlayerRest> players) {
        this.teamName = teamName;
        this.division_id = division_id;
        this.division = division;
        this.players = players;
    }


    //private Location location;      // now hear me out on this one, when we need to replace a member they can be replaced by anyone of the same organization that has the same division so In my oppinion this should be added to class diagram
    //private Boolean isHomeTeam;


    public int getTeam_id() {
        return team_id;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getDivision_id() {
        return division_id;
    }

    public void setDivision_id(int division_id) {
        this.division_id = division_id;
    }

    public DivisionRest getDivision() {
        return division;
    }

    public void setDivision(DivisionRest division) {
        this.division = division;
    }

    public List<PlayerRest> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerRest> players) {
        this.players = players;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
