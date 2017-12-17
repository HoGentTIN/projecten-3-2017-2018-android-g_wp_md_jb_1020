package Domain;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class to represent the teams
 * A team has a division and players
 */
public class Team {

    //Variables
    private int team_id;
    private String teamName;
    private String logo;
    private Division division;
    private List<Player> players = new ArrayList<>();

    public Team(int teamId, String teamname, Division division) {
        this.team_id = teamId;
        this.teamName = teamname;
        this.division = division;
    }

    public int getTeam_id() {
        return team_id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addPlayers(Player[] p){
        players.addAll(Arrays.asList(p));
    }

    public void sortPlayersByNumber(){
        Collections.sort(players);
    }

    public List<Player> getPlayersByStatus(Status s){
        List<Player> list = new ArrayList<>();
        for(Player p: players){
            if(p.getStatus().equals(s)){
                list.add(p);
            }
        }
        return list;
    }


    public List<Player> getPlayers(){
        sortPlayersByNumber();
        return players;
    }

    /**
     * Searches the playerlist for a given id
     *
     * @param id id of the player to search
     * @return the playerobject if found, otherwise null
     */
    public Player getPlayerById(int id){
        Player pl = null;
        for(Player p: players){
            if(p.getPlayer_id() == id){
                pl = p;
            }
        }
        return pl;
    }

    /**
     * Switch the capnumbers of the chosen players
     *
     * @param playerId1 id of the first player to switch cap
     * @param playerId2 id of the second player to switch cap
     */
    public void switchPlayerCaps(int playerId1, int playerId2){
        Player p1 = getPlayerById(playerId1);
        Player p2 = getPlayerById(playerId2);

        int firstPlayerNumber = p1.getPlayerNumber();
        int secondPlayerNumber = p2.getPlayerNumber();

        p1.setPlayerNumber(secondPlayerNumber);
        p2.setPlayerNumber(firstPlayerNumber);

    }

}
