package Domain;

/**
 * Created by timos on 5-10-2017.
 */

class Team {

    //Variables
    private int team_id;
    private String teamname;


    private Location location;      // now hear me out on this one, when we need to replace a member they can be replaced by anyone of the same organization that has the same division so In my oppinion this should be added to class diagram
    private Division division;
    private CompetitionClass competitionClass;

    private Player[] players;

    public Team(String teamname, CompetitionClass competitionClass) {
        this.teamname = teamname;
        this.competitionClass = competitionClass;
    }
}
