package Domain;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by timos on 5-10-2017.
 */


public class Domaincontroller {

    private Match match;
    private Official o;

    private List<String[]> logList = new ArrayList<>();                                             //List of all events, event = String[] => [0] = roundNumber, [1] = roundTime, [2] = eventCode ,[3] = eventDescription
    private int eventCounter = 0;                                                                   //eventCode used for filtering logs, show all goals, faults, [G|C|P|U|V|V4][H,A][1,2,3,4] {G,C,P,U,V,V4 = goal|change of player|Penalty|faults.. , H,A = Home|Away, 1,2,3,4 = Round}


    public Domaincontroller(){

    }

    public Match getMatch() {
        return match;
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

    public void startMatch(){
        match = new Match();
    }

    public void createPlayers(){
        Player[] homePlayers = {new Player(1,"Beirens", "Sam" ),new Player(2,"Beirens","Stijn"),new Player(3,"Boedt","Olivier"),
                new Player(7,"Callebout", "Tom"),new Player(10,"Crombez","Brecht"),new Player(5,"David","Indy"),
                new Player(8,"Devlies","Tim"),new Player(4,"Haelemeersch","Benoit"),new Player(6,"Hendryckx","Kris"),
                new Player(9,"Mechele","Steve"), new Player(11,"Peel","Dailly"),new Player(12,"Piens","Tim"),
                new Player(13,"Vandermeulen","Matisse")};
        for(Player p: homePlayers){
            p.setTeam(match.getTeam(0));
        }
        Log.i("game","Hometeam " + match.getTeam(0).getTeamName() + ", players created");

        Player[] awayPlayers = {new Player(1,"Backaert","Guy"),new Player(2,"Cassiman","Thomas"),new Player(3,"De Smedt","Peter"),
                new Player(7,"Gheyssens","Ruben"),new Player(10,"Goossens","Jonas"),new Player(5,"Heyvaert","Norbert"),
                new Player(8,"Langelet","Rik"),new Player(4,"Pandolfi","Mateo"),new Player(6,"Uyttersprot","Dieter"),new Player(9,"Van der Heyden","Stijn"),
                new Player(11,"Verhoeve","Lander"),new Player(12,"Verhoeven","Maxim"),new Player(13,"Bakker","Boris")};
        for(Player p: awayPlayers){
            p.setTeam(match.getTeam(1));
        }
        Log.i("game","AwayTeam " + match.getTeam(1).getTeamName() + ", players created");

        match.getTeam(0).addPlayers(homePlayers);
        match.getTeam(1).addPlayers(awayPlayers);

        setPlayersStatus();
    }

    private void setPlayersStatus(){
        // match.getTeam().getPlayers().subList(0,6).forEach(p -> p.setStatus(Status.PLAYING));
        for (int i = 0; i < match.getTeam(0).getPlayers().size(); i++){
            if(i < 8) {
                match.getTeam(0).getPlayers().get(i).setStatus(Status.ACTIVE);
                match.getTeam(1).getPlayers().get(i).setStatus(Status.ACTIVE);
            }
            else{
                match.getTeam(0).getPlayers().get(i).setStatus(Status.BENCHED);
                match.getTeam(1).getPlayers().get(i).setStatus(Status.BENCHED);
            }
        }

    }

    public void createTeams(String homeTeamName, CompetitionClass homeCc, String awayTeamName, CompetitionClass awayCc){
        Team hometeam = new Team(homeTeamName, homeCc);
        Team awayteam = new Team(awayTeamName, awayCc);
        match.addTeams(hometeam,awayteam);
    }


    //eventcode - description => Overview of eventlogcodes and corresponding description
    //G01 - Player [Int playernumber] [String playername] from the hometeam has scored a goal [int homescore] - [ int awayscore]    //Goalevent
    //

}
