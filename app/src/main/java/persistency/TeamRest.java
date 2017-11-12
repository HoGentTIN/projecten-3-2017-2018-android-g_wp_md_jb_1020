package persistency;

import java.util.ArrayList;
import java.util.List;

import Domain.CompetitionClass;
import Domain.Division;
import Domain.Location;
import Domain.Official;
import Domain.Player;

/**
 * Created by Impling on 12-Nov-17.
 */

public class TeamRest {

    //Variables
    private int team_id;
    private String teamName;


    private Location location;      // now hear me out on this one, when we need to replace a member they can be replaced by anyone of the same organization that has the same division so In my oppinion this should be added to class diagram
    private Division division;
    private CompetitionClass competitionClass;
    private Boolean isHomeTeam;

    private Official coach;
    //private List<Integer> players = new ArrayList<>();

}
