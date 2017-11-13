package Domain;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by timos on 5-10-2017.
 */


public class Domaincontroller {

    private Match match;
    //hier ga ik een selectedmatch maken in principe is dit exact hetzelfde als 'match' dat hierboven staat maar ik ga et voor de functionaliteit even apart declareren
    private Match selectedMatch;

    //hier ga ik een aantal matches aanmaken om een lijst van matches van een official te simuleren (gade uiteindelijk moeten verwijderen)
    //de waarden toekennen staat in startmatch() dus daar ook wegdoen dan
    private Match testMatch1 = new Match();
    private Match testMatch2 = new Match();


    private List<Match> ownedMatches;
    private Official o;
    private String startTime;

    private Player selectedPlayer;

    private List<String[]> logList = new ArrayList<>();                                             //List of all events, event = String[] => [0] = roundNumber, [1] = roundTime, [2] = eventCode ,[3] = eventDescription
    private int eventCounter = 0;                                                                   //eventCode used for filtering logs, show all goals, faults, [G|C|P|U|V|V4][H,A][1,2,3,4] {G,C,P,U,V,V4 = goal|change of player|Penalty|faults.. , H,A = Home|Away, 1,2,3,4 = Round}

    //pretty sure this isn't the correct way to switch players, but it works
    private Boolean switchPlayer = false;
    private Player playerToSwitch;

    public int round = 1;

    public Domaincontroller(){

    }

    public Player getSelectedPlayer() {
        return selectedPlayer;
    }

    public void setMatch(int matchNumber){
        //hier moet er nog vanuit de lijst van ownedmathes de juiste match worden gehaald om die vervolgens in match te steken wat de
        //eigenlijke geselecteerde match is.
    }
    public void setSelectedPlayer(Boolean homeTeam, int playerId) {
        int teamNr;
        if (homeTeam){
            selectedPlayer = match.getHome().getPlayerById(playerId);
        } else {
            selectedPlayer = match.getVisitor().getPlayerById(playerId);
        }

        Log.i("game", selectedPlayer.getFullName() + " selected in DC");

        //check if there's a player to switch
        checkPlayerSwitch();
    }

    public void addGoal(){
        if(selectedPlayer != null) {
            if(selectedPlayer.getStatus() !=Status.GAMEOVER) {
                match.addGoal(new Goal(selectedPlayer),selectedPlayer.getTeam().isHomeTeam());
            }
        }
    }

    public void addFaultU20() {
        //set fault to player
        selectedPlayer.setFaults(selectedPlayer.getFaults() + 1);
        Log.i("game", selectedPlayer.getFullName() + " has " + selectedPlayer.getFaults() + " faults");
        //Reset selectedplayer to zero
        selectedPlayer = null;
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

    public Match getMatch() {
        return match;
    }

    public List<Match> getOwnedMatches(){return ownedMatches;}

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


    public void startMatch(){
        match = new Match();

        //hier ga ik al een vaste datum meegeven om te testen dit moet nog aangepast worden met de juiste functionaliteit (groetjes laurentje)
        match.setDate(new Date(2017,11,11));
        //ik ga hier verder die testmatchen uitwerken dus dit ook ni vergeten weg te doen dan;
        ownedMatches = new ArrayList<Match>();
        testMatch1.setDate(new Date(2017,12,12));
        testMatch1.setHome(new Team("Gent",CompetitionClass.DAMES));
        testMatch1.setVisitor(new Team("Oostakker",CompetitionClass.DAMES));
        testMatch2.setDate(new Date(2017,12,11));
        testMatch2.setHome(new Team("Kortrijk",CompetitionClass.DAMES));
        testMatch2.setVisitor(new Team("Lochristi",CompetitionClass.DAMES));
        ownedMatches.add(testMatch1);
        ownedMatches.add(testMatch2);
    }

    public void createPlayers(){
        Player[] homePlayers = {new Player(1,"Beirens", "Sam" ),new Player(2,"Beirens","Stijn"),new Player(3,"Boedt","Olivier"),
                new Player(7,"Callebout", "Tom"),new Player(10,"Crombez","Brecht"),new Player(5,"David","Indy"),
                new Player(8,"Devlies","Tim"),new Player(4,"Haelemeersch","Benoit"),new Player(6,"Hendryckx","Kris"),
                new Player(9,"Mechele","Steve"), new Player(11,"Peel","Dailly"),new Player(12,"Piens","Tim"),
                new Player(13,"Vandermeulen","Matisse")};
        for(Player p: homePlayers){
            p.setTeam(match.getHome());
        }
        Log.i("game","Hometeam " + match.getHome().getTeamName() + ", players created");

        Player[] awayPlayers = {new Player(1,"Backaert","Guy"),new Player(2,"Cassiman","Thomas"),new Player(3,"De Smedt","Peter"),
                new Player(7,"Gheyssens","Ruben"),new Player(10,"Goossens","Jonas"),new Player(5,"Heyvaert","Norbert"),
                new Player(8,"Langelet","Rik"),new Player(4,"Pandolfi","Mateo"),new Player(6,"Uyttersprot","Dieter"),new Player(9,"Van der Heyden","Stijn"),
                new Player(11,"Verhoeve","Lander"),new Player(12,"Verhoeven","Maxim"),new Player(13,"Bakker","Boris")};
        for(Player p: awayPlayers){
            p.setTeam(match.getVisitor());
        }
        Log.i("game","AwayTeam " + match.getVisitor().getTeamName() + ", players created");

        match.getHome().addPlayers(homePlayers);
        match.getVisitor().addPlayers(awayPlayers);

        setPlayersStatus();
    }

    private void setPlayersStatus(){
        // match.getTeam().getPlayers().subList(0,6).forEach(p -> p.setStatus(Status.PLAYING));
        for (int i = 0; i < match.getHome().getPlayers().size(); i++){
            if(i < 7) {
                match.getHome().getPlayers().get(i).setStatus(Status.ACTIVE);
                match.getVisitor().getPlayers().get(i).setStatus(Status.ACTIVE);
            }
            else{
                match.getHome().getPlayers().get(i).setStatus(Status.BENCHED);
                match.getVisitor().getPlayers().get(i).setStatus(Status.BENCHED);
            }
        }

    }

    public void createTeams(String homeTeamName, CompetitionClass homeCc, String awayTeamName, CompetitionClass awayCc){
        Team hometeam = new Team(homeTeamName, homeCc);
        hometeam.setHomeTeam(true);
        Team awayteam = new Team(awayTeamName, awayCc);
        awayteam.setHomeTeam(false);
        match.setHome(hometeam);
        match.setVisitor(awayteam);
    }

    public void resetSelectedPlayer() {
        selectedPlayer = null;
    }


    //eventcode - description => Overview of eventlogcodes and corresponding description
    //G01 - Player [Int playernumber] [String playername] from the hometeam has scored a goal [int homescore] - [ int awayscore]    //Goalevent
    //

}
