package Domain;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import persistency.MatchRest;
import persistency.PlayerRest;

/**
 * Created by timos on 5-10-2017.
 */


public class Domaincontroller {

    private Match match;
    private MatchRest matchR;
    //hier ga ik een selectedmatch maken in principe is dit exact hetzelfde als 'match' dat hierboven staat maar ik ga et voor de functionaliteit even apart declareren
    private MatchRest selectedMatch;

    //hier ga ik een aantal matches aanmaken om een lijst van matches van een official te simuleren (gade uiteindelijk moeten verwijderen)
    //de waarden toekennen staat in startmatch() dus daar ook wegdoen dan
    private Match testMatch1 = new Match();
    private Match testMatch2 = new Match();


    private List<Match> ownedMatches;
    private List<MatchRest> ownedMatchesR;
    private Official o;
    private String startTime;

    private Player selectedPlayer;

    private List<String[]> logList = new ArrayList<>();                                             //List of all events, event = String[] => [0] = roundNumber, [1] = roundTime, [2] = eventCode ,[3] = eventDescription
    private int eventCounter = 0;                                                                   //eventCode used for filtering logs, show all goals, faults, [G|C|P|U|V|V4][H,A][1,2,3,4] {G,C,P,U,V,V4 = goal|change of player|Penalty|faults.. , H,A = Home|Away, 1,2,3,4 = Round}

    //pretty sure this isn't the correct way to switch players, but it works
    private Boolean switchPlayer = false;
    private Player playerToSwitch;

    private AppCompatActivity currentActivity;

    public Domaincontroller(){

    }

    public Player getSelectedPlayer() {
        return selectedPlayer;
    }

    public void setMatch(int matchNumber){
        //hier moet er nog vanuit de lijst van ownedmathes de juiste match worden gehaald om die vervolgens in match te steken wat de
        //eigenlijke geselecteerde match is.
        int aantal = ownedMatchesR.size();
        for (int i =0;i<aantal;i++){
            if(ownedMatchesR.get(i).getMatch_id()==matchNumber){
                selectedMatch = ownedMatchesR.get(i);
            }
        }
    }

    public void setSelectedPlayer(Boolean homeTeam, int playerId) {
        int teamNr;
        if (homeTeam){
            selectedPlayer = match.getHomeTeam().getPlayerById(playerId);
        } else {
            selectedPlayer = match.getAwayTeam().getPlayerById(playerId);
        }

        Log.i("game", selectedPlayer.getFullName() + " selected in DC");

        //check if there's a player to switch
        checkPlayerSwitch();
    }

    public void resetSelectedPlayer() {
        selectedPlayer = null;
    }

    public Match getMatch() {
        return match;
    }
    public MatchRest getSelectedMatch(){return  selectedMatch;}

    public MatchRest getMatchR() {
        return matchR;
    }

    public void setOwnedMatchesR(List<MatchRest> matcherR){
        this.ownedMatchesR = matcherR;
    }

    public List<MatchRest> getOwnedMatchesR(){return ownedMatchesR;}

    public List<Match> getOwnedMatches(){return ownedMatches;}

    public void convertBackendToClass(){

        //first create team objects widouth player list
        List<Team> teams = convertTeamRestToTeam();
        //create player lists with team objects and PlayerRest
        List<Player> homePlayers = convertPlayerRestToPlayer(teams.get(0),true);
        List<Player> awayPlayers = convertPlayerRestToPlayer(teams.get(1), false);
        //add playerlist to team objects
        teams.get(0).setPlayers(homePlayers);
        teams.get(1).setPlayers(awayPlayers);
        //set match variable from dc using team and playerlists
        Match m = new Match();
        m.setHomeTeam(teams.get(0));
        m.setAwayTeam(teams.get(1));
        m.setLocation(selectedMatch.getLocation());
        //still need code to set date of match after adding string to date converter in Rest class
        //after backend adds official to match setofficial

        this.match = m;
    }

    public List<Player> convertPlayerRestToPlayer(Team t, boolean home){
        List<Player> players = new ArrayList<>();
        if(home){
            List<PlayerRest> playersR = selectedMatch.getHome().getPlayers();
            for (PlayerRest pr : playersR){
                int firstSpace = pr.getName().indexOf(" ");                     // find divide between first and lastname
                String firstName = pr.getName().substring(0, firstSpace);       // get everything upto the first space character
                String lastName = pr.getName().substring(firstSpace).trim();

                Player p = new Player(pr.getPlayerNumber(), firstName, lastName);
                p.setTeam(t);
                //still need code to get status enum for player
                //add converter in restobject to turn birthdate into date instead of string and calculate age
                //add code for adding player image? only needed in rest yes or no.
                players.add(p);
            }
        }else{
            List<PlayerRest> playersR = selectedMatch.getVisitor().getPlayers();
            for (PlayerRest pr : playersR){
                int firstSpace = pr.getName().indexOf(" ");                     // find divide between first and lastname
                String firstName = pr.getName().substring(0, firstSpace);       // get everything upto the first space character
                String lastName = pr.getName().substring(firstSpace).trim();

                Player p = new Player(pr.getPlayerNumber(), firstName, lastName);
                p.setTeam(t);
                //still need code to get status enum for player
                //add converter in restobject to turn birthdate into date instead of string and calculate age
                //add code for adding player image? only needed in rest yes or no.
                players.add(p);
            }

        }

        return players;
    }

    public List<Team> convertTeamRestToTeam(){
        List<Team> teams = new ArrayList<>();

        Team homeTeam = new Team(selectedMatch.getHome().getTeam_id(), selectedMatch.getHome().getTeamName(), selectedMatch.getHome().getDivision());
        homeTeam.setCoach(selectedMatch.getHome().getCoach());
        Team awayTeam = new Team(selectedMatch.getVisitor().getTeam_id(), selectedMatch.getVisitor().getTeamName(), selectedMatch.getVisitor().getDivision());
        awayTeam.setCoach(selectedMatch.getVisitor().getCoach());
        teams.add(homeTeam);
        teams.add(awayTeam);

        //still need to add location to teams
        //still need to add teamlogo image to team

        return teams;
    }

    //Actions
    public void addGoal(){
        if(selectedPlayer != null) {
            if(selectedPlayer.getStatus() == Status.ACTIVE) {
                match.addGoal(new Goal(match.getMatch_id(), selectedPlayer.getTeam().getTeam_id(),selectedPlayer));
            }
        }
    }

    public void addFaultU20() {
        match.getPenaltyBook().addPenalty(new Penalty(selectedPlayer,PenaltyType.U20));
    }

    // indicate that players want to change number & store first player object
    public void switchPlayerCaps() {
        playerToSwitch = selectedPlayer;
        switchPlayer = true;
    }

    // calls method in team to switch the player numbers when both players are from the same team and passes both player id's
    private void checkPlayerSwitch(){
        if(switchPlayer){
            if(playerToSwitch.getTeam().equals(selectedPlayer.getTeam())) {
                playerToSwitch.getTeam().switchPlayerCaps(playerToSwitch.getPlayer_id(), selectedPlayer.getPlayer_id());
                Log.i("game", playerToSwitch.getFullName() + " switched numbers with " + selectedPlayer.getFullName());

                switchPlayer = false;
            }
        }
    }

    public void addFaultUMV() {
        match.getPenaltyBook().addPenalty(new Penalty(selectedPlayer,PenaltyType.UMV));
    }

    public void addFaultUMV4() {
        match.getPenaltyBook().addPenalty(new Penalty(selectedPlayer,PenaltyType.UMV4));
    }


    //Function: appendLog - add an event to the processLog
    public void appendLog(String eventDescription, String eventCode){
        //Get Time of event
        String timeOfEvent = "" + Calendar.getInstance().getTime().getHours() + ":" + Calendar.getInstance().getTime().getMinutes() + ":" + Calendar.getInstance().getTime().getSeconds();
        String[] logInfo = new String[]{timeOfEvent, eventCode, eventDescription};
        logList.add(eventCounter, logInfo);

        Log.i("logInfo", eventCounter + " [" + logList.get(eventCounter)[0].toString() + "] " + logList.get(eventCounter)[1].toString() +  ": " + logList.get(eventCounter)[2].toString()); //Testinglog
        eventCounter++;
    }

    //Function: appendLog - add an event to the processLog - but using the current round time instead of the system clock to register event
    //eventCode seems pretty useless => replace by matchCode = same for entire match
    public void appendLog(String eventDescription, String eventCode, String roundTime, int round){
        //Get Time of event
        String[] logInfo = new String[]{"" + round, roundTime, eventCode, eventDescription};
        logList.add(eventCounter, logInfo);

        Log.i("logInfo", eventCounter + " [" + logList.get(eventCounter)[0].toString() + " " + logList.get(eventCounter)[1].toString() + "] " + logList.get(eventCounter)[2].toString() +  ": " + logList.get(eventCounter)[3].toString()); //Testinglog
        eventCounter++;
    }

    //Function; getLogByIndex - ask after specific event
    public String[] getLogByIndex(int i){
        return logList.get(i);
    }

    //Function: request matchLog    - return a string containing everything that happend in a match
    public String requestLog(){
        int iterator = 0;
        StringBuilder log = new StringBuilder();
        for(String[] s : logList){
            String line = iterator + " [" + s[0].toString() + "] " + s[1].toString() +  ": " + s[2].toString();
            log.append(line + "\n");
            //Log.i("logInfo", line);           //Commented to prevent android monitor overload of info, uncomment for testing
            iterator++;
        }
        return log.toString();
    }

    public List<String> getLogForRound(int round){

        List<String> roundLog = new ArrayList<>();

        for (String[] log : this.logList) {
            if(Integer.parseInt(log[0])== round){
                roundLog.add("[" + log[1] + "] - " + log[3]);
            }
        }
        return roundLog;
    }

    public void undoLog(){
        logList.remove(logList.size()-1);
    }

    //temporary static value until we set it in code
    public long getRoundTime(){
        return 8;
    }

    public Team getHomeTeam(){
       return match.getHomeTeam();
    }

    public Team getAwayTeam(){
        return match.getAwayTeam();
    }

    public AppCompatActivity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(AppCompatActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public void startMatch(){

        //these steps should now already be done in endselection of competitionselection

        //Original test code
        /*
        match = new Match();
        matchR = new MatchRest();
        matchR.setDate(new Date(2017,11,11).toString());


        //hier ga ik al een vaste datum meegeven om te testen dit moet nog aangepast worden met de juiste functionaliteit (groetjes laurentje)
        match.setDate(new Date(2017,11,11));
        //ik ga hier verder die testmatchen uitwerken dus dit ook ni vergeten weg te doen dan;
        ownedMatches = new ArrayList<Match>();
        testMatch1.setDate(new Date(2017,12,12));
        Division Dames = new Division("Dames",7,2);
        testMatch1.setHomeTeam(new Team(0,"Gent",Dames));
        testMatch1.setAwayTeam(new Team(1,"Oostakker",Dames));
        testMatch2.setDate(new Date(2017,12,11));
        testMatch2.setHomeTeam(new Team(2,"Kortrijk",Dames));
        testMatch2.setAwayTeam(new Team(3,"Lochristi",Dames));
        ownedMatches.add(testMatch1);
        ownedMatches.add(testMatch2);
        */
    }

    public void createPlayers(){
        Player[] homePlayers = {new Player(1,"Beirens", "Sam" ),new Player(2,"Beirens","Stijn"),new Player(3,"Boedt","Olivier"),
                new Player(7,"Callebout", "Tom"),new Player(10,"Crombez","Brecht"),new Player(5,"David","Indy"),
                new Player(8,"Devlies","Tim"),new Player(4,"Haelemeersch","Benoit"),new Player(6,"Hendryckx","Kris"),
                new Player(9,"Mechele","Steve"), new Player(11,"Peel","Dailly"),new Player(12,"Piens","Tim"),
                new Player(13,"Vandermeulen","Matisse")};
        for(Player p: homePlayers){
            p.setTeam(match.getHomeTeam());
        }
        Log.i("game","Hometeam " + match.getHomeTeam().getTeamName() + ", players created");

        Player[] awayPlayers = {new Player(1,"Backaert","Guy"),new Player(2,"Cassiman","Thomas"),new Player(3,"De Smedt","Peter"),
                new Player(7,"Gheyssens","Ruben"),new Player(10,"Goossens","Jonas"),new Player(5,"Heyvaert","Norbert"),
                new Player(8,"Langelet","Rik"),new Player(4,"Pandolfi","Mateo"),new Player(6,"Uyttersprot","Dieter"),new Player(9,"Van der Heyden","Stijn"),
                new Player(11,"Verhoeve","Lander"),new Player(12,"Verhoeven","Maxim"),new Player(13,"Bakker","Boris")};
        for(Player p: awayPlayers){
            p.setTeam(match.getAwayTeam());
        }
        Log.i("game","AwayTeam " + match.getAwayTeam().getTeamName() + ", players created");

        match.getHomeTeam().addPlayers(homePlayers);
        match.getAwayTeam().addPlayers(awayPlayers);

        setPlayersStatus();
    }

    private void setPlayersStatus(){
        // match.getTeam().getPlayers().subList(0,6).forEach(p -> p.setStatus(Status.PLAYING));
        for (int i = 0; i < match.getHomeTeam().getPlayers().size(); i++){

                match.getHomeTeam().getPlayers().get(i).setStatus(Status.ACTIVE);
                match.getAwayTeam().getPlayers().get(i).setStatus(Status.ACTIVE);
        }

    }

    public void createTeams(String homeTeamName, Division dH, String awayTeamName, Division dA){
        Team hometeam = new Team(4,homeTeamName, dH);
        Team awayteam = new Team(5,awayTeamName, dA);
        match.setHomeTeam(hometeam);
        match.setAwayTeam(awayteam);
    }




    //eventcode - description => Overview of eventlogcodes and corresponding description
    //G01 - Player [Int playernumber] [String playername] from the hometeam has scored a goal [int homescore] - [ int awayscore]    //Goalevent
    //

}
