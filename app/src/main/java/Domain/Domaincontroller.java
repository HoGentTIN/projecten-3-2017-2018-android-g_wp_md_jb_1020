package Domain;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.g1020.waterpolo.AdministrationEnd;
import com.g1020.waterpolo.MatchControl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import persistency.DivisionRest;
import persistency.MatchRest;
import persistency.PlayerRest;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Domaincontroller {

    //Backend connector
    private final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    private Match match;
    private List<DivisionRest> divisions;
    private String selectedDivisionName =null;
    private String selectedDivisionNameTemp = null;
    private List<MatchRest> ownedMatchesR;
    private MatchRest selectedMatch;
    private String startTime;

    private Player selectedPlayer;
    private Player playerToSwitch = null;

    private List<String[]> logList = new ArrayList<>();                                             //List of all events, event = String[] => [0] = roundNumber, [1] = roundTime, [2] = eventCode ,[3] = eventDescription
    private int eventCounter = 0;                                                                   //eventCode used for filtering logs, show all goals, faults, [G|C|P|U|V|V4][H,A][1,2,3,4] {G,C,P,U,V,V4 = goal|change of player|Penalty|faults.. , H,A = Home|Away, 1,2,3,4 = Round}

    private AppCompatActivity currentActivity;

    public Domaincontroller(){

    }

    public List<DivisionRest> getDivisions() {
        return divisions;
    }

    public void setDivisions(List<DivisionRest> divisions) {
        this.divisions = divisions;
    }

    public String getSelectedDivisionName() {
        return selectedDivisionName;
    }

    public void setSelectedDivisionName() {
        this.selectedDivisionName = this.getSelectedDivisionNameTemp();
    }
    public String getSelectedDivisionNameTemp() {
        return selectedDivisionNameTemp;
    }

    public void setSelectedDivisionNameTemp(String selectedDivisionNameTemp) {
        this.selectedDivisionNameTemp = selectedDivisionNameTemp;
    }

    public long getRoundTime(){
        return match.getHomeTeam().getDivision().getRoundLength();
    }

    public long getBreakTime() {return match.getHomeTeam().getDivision().getPauseLength();}

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

    public ApiInterface getApiService() {
        return apiService;
    }

    public Player getPlayerToSwitch(){return playerToSwitch;}

    public void setPlayerToSwitch(Player sp){this.playerToSwitch=sp;}

    public void setMatch(int matchNumber){
        int aantal = ownedMatchesR.size();
        for (int i =0;i<aantal;i++){
            if(ownedMatchesR.get(i).getMatch_id()==matchNumber){
                selectedMatch = ownedMatchesR.get(i);
            }
        }
    }

    //set player via team and player id
    public void setSelectedPlayer(Boolean homeTeam, int playerId) {
        if (homeTeam){
            selectedPlayer = match.getHomeTeam().getPlayerById(playerId);
        } else {
            selectedPlayer = match.getAwayTeam().getPlayerById(playerId);
        }

        Log.i("game", selectedPlayer.getFullName() + " selected in DC");

        //check if there's a player to switch
        checkPlayerSwitch();

    }

    public Player getSelectedPlayer() {
        return selectedPlayer;
    }

    public void resetSelectedPlayer() {
        selectedPlayer = null;
    }

    public Match getMatch() {
        return match;
    }
    public MatchRest getSelectedMatch(){return  selectedMatch;}

    public void setOwnedMatchesR(List<MatchRest> matcherR){
        this.ownedMatchesR = matcherR;
    }

    public List<MatchRest> getOwnedMatchesR(){return ownedMatchesR;}


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
        m.setMatch_id(selectedMatch.getMatch_id());
        //still need code to set date of match after adding string to date converter in Rest class
        //after backend adds official to match setofficial

        this.match = m;
    }

    public List<Player> convertPlayerRestToPlayer(Team t, boolean home){
        List<Player> players = new ArrayList<>();
        if(home){
            List<PlayerRest> playersR = selectedMatch.getHome().getPlayers();
            for (PlayerRest pr : playersR){
                if(pr.getStarter()) {
                    int firstSpace = pr.getName().indexOf(" ");                     // find divide between first and lastname
                    String firstName = pr.getName().substring(0, firstSpace);       // get everything upto the first space character
                    String lastName = pr.getName().substring(firstSpace).trim();

                    Player p = new Player(pr.getPlayerNumber(), firstName, lastName);
                    p.setTeam(t);
                    p.setPlayer_id(pr.getPlayerId());
                    //still need code to get status enum for player
                    p.setStatus(pr.getStatus());
                    //add converter in restobject to turn birthdate into date instead of string and calculate age
                    //add code for adding player image? only needed in rest yes or no.
                    players.add(p);
                }
            }
        }else{
            List<PlayerRest> playersR = selectedMatch.getVisitor().getPlayers();
            for (PlayerRest pr : playersR){
                if(pr.getStarter()) {
                int firstSpace = pr.getName().indexOf(" ");                     // find divide between first and lastname
                String firstName = pr.getName().substring(0, firstSpace);       // get everything upto the first space character
                String lastName = pr.getName().substring(firstSpace).trim();

                Player p = new Player(pr.getPlayerNumber(), firstName, lastName);
                p.setPlayer_id(pr.getPlayerId());
                p.setTeam(t);
                //still need code to get status enum for player
                p.setStatus(pr.getStatus());
                //add converter in restobject to turn birthdate into date instead of string and calculate age
                //add code for adding player image? only needed in rest yes or no.
                players.add(p);}
            }

        }

        return players;
    }

    public List<Team> convertTeamRestToTeam(){
        List<Team> teams = new ArrayList<>();

        Team homeTeam = new Team(selectedMatch.getHome().getTeam_id(), selectedMatch.getHome().getTeamName(), convertDivisionRestToDivision(selectedMatch.getHome().getDivision()));
        homeTeam.setTeam_id(selectedMatch.getHome().getTeam_id());
        Team awayTeam = new Team(selectedMatch.getVisitor().getTeam_id(), selectedMatch.getVisitor().getTeamName(), convertDivisionRestToDivision(selectedMatch.getVisitor().getDivision()));
        awayTeam.setTeam_id(selectedMatch.getVisitor().getTeam_id());
        teams.add(homeTeam);
        teams.add(awayTeam);

        //still need to add location to teams
        //still need to add teamlogo image to team
        homeTeam.setLogo(selectedMatch.getHome().getLogo());
        awayTeam.setLogo(selectedMatch.getVisitor().getLogo());

        return teams;
    }
    public Division convertDivisionRestToDivision(DivisionRest divisionR){
        return new Division(divisionR.getDivision_name(),divisionR.getPeriod_length(), divisionR.getBreak_length());
    }

    //Actions
    public void addGoal(){
        if(selectedPlayer != null) {
            if(selectedPlayer.getStatus() == Status.ACTIVE) {
                match.addGoal(new Goal(selectedPlayer.getTeam().getTeam_id()));

                //Post goal to live
                asyncPostGoal(selectedPlayer);
            }
        }
    }

    public void addFaultU20() {
        match.getPenaltyBook().addPenalty(new Penalty(selectedPlayer,PenaltyType.U20));

        //post fault
        asyncPostFault(selectedPlayer, 1);
    }

    public void addFaultUMV() {
        match.getPenaltyBook().addPenalty(new Penalty(selectedPlayer,PenaltyType.UMV));

        selectedPlayer.setStatus(Status.GAMEOVER);

        //post fault
        asyncPostFault(selectedPlayer, 2);
    }

    public void addFaultUMV4() {
        match.getPenaltyBook().addPenalty(new Penalty(selectedPlayer,PenaltyType.UMV4));

        selectedPlayer.setStatus(Status.GAMEOVER);

        //post fault
        asyncPostFault(selectedPlayer, 3);
    }

    public void addInjury() {
        selectedPlayer.setStatus(Status.GAMEOVER);

        //asyncPostInjury();

    }

    // calls method in team to switch the player numbers when both players are from the same team and passes both player id's
    private void checkPlayerSwitch(){
        if(playerToSwitch!=null){
            if(playerToSwitch.getTeam().equals(selectedPlayer.getTeam())) {
                playerToSwitch.getTeam().switchPlayerCaps(playerToSwitch.getPlayer_id(), selectedPlayer.getPlayer_id());
                Log.i("game", playerToSwitch.getFullName() + " switched numbers with " + selectedPlayer.getFullName());

                setPlayerToSwitch(null);
            }

            MatchControl mc = (MatchControl) getCurrentActivity();

            mc.ReloadFragments(); //see matchcontrol line 554
        }

    }

    //function to increase current round
    public void nextRound(){
        this.match.setCurrentRound(match.getCurrentRound()+1);
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
        eventCounter--;
        logList.remove(logList.size()-1);
    }

    //Function to get latest log in case of revert command, user gives ammount of time to go back in minutes and seconds long:long (for matchtimer) concert it into string and
    //execute this functions how many events to go back to
    //after this one reverse loop throuhg loglist chech what event and revert it, stop when reaching loglist
    public int getLogIndex(String time){
        int index = -1; //imposible value in case no log found
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        Date timeInDate = new Date();
        Date loggedTime = new Date();
        try {
            timeInDate = dateFormat.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for(int i=0; i<logList.size(); i++){
            String[] s = logList.get(i);
            //for current round only
            if(s[0] == (""+ getMatch().getCurrentRound())){
                try {
                    loggedTime = dateFormat.parse(s[1]);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //get last log before or at revertime - countdowntimer so reverse
                if(loggedTime.before(timeInDate) || loggedTime.equals(timeInDate)){
                    index = i;  //last
                    return index;
                }
            }
        }
        return index;
    }


    private void displayToast(String message){
        Toast toast = Toast.makeText(getCurrentActivity(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    //METHODS that connect the app with the backend
    /**
     * Method that creates a seperate task on a thread to post that the match has ended and calls the {@link ApiInterface#endMatch(int)} to post it to the backend
     *
     */
    public void startMatch(){

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Call<Void> call = apiService.startMatch(getSelectedMatch().getMatch_id());
                    try {
                        call.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Error e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(task, "Service thread testpost").start();
    }

    /**
     * Method that creates a seperate task on a thread to post that the match has ended and calls the {@link ApiInterface#endMatch(int)} to post it to the backend
     *
     */
    public void endMatch(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Call<Void> call = apiService.endMatch(getSelectedMatch().getMatch_id());
                    try {
                        call.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Error e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(task, "Service thread testpost").start();
    }

    /**
     * Method that creates a seperate task on a thread to post that the match has been cancelled and calls the {@link ApiInterface#cancelMatch(int)} to post it to the backend
     *
     */
    public void cancelMatch(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Call<Void> call = apiService.cancelMatch(getSelectedMatch().getMatch_id());
                    try {
                        call.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Error e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(task, "Service thread testpost").start();
    }

    /**
     * Method that creates a task on a separate thread to post a scored goal to the backend.
     *
     * @param player needs to be passed since async could allow the activity to reset the selected player to null before it can be called
     */
    private void asyncPostGoal(Player player){
        final Player p = player;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    postGoal(p);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(task, "Service thread testpost").start();
    }

    /**
     * Method that calls the {@link ApiInterface#addGoal(int, int, int)} to post the goal to the backend
     *
     * @param p needs to be passed since async could allow the activity to reset the selected player to null before it can be called
     * @throws InterruptedException for when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    private void postGoal(Player p) throws InterruptedException {
        Call<Void> call = apiService.addGoal(match.getMatch_id(),p.getPlayer_id(),match.getCurrentRound());
        try {
            call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that creates a task on a separate thread to post a scored goal to the backend.
     *
     * @param player needs to be passed since async could allow the activity to reset the selected player to null before it can be called
     * @param penaltyTypeId id for the type of penalty
     */
    private void asyncPostFault(Player player, final int penaltyTypeId){
        final Player p = player;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    postfault(p, penaltyTypeId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(task, "Service thread testpost").start();
    }

    /**
     * Method that calls the {@link ApiInterface#addPenalty(int, int, int)} to post the faults to the backend.
     *
     * @param p needs to be passed since async could allow the activity to reset the selected player to null before it can be called
     * @param penaltyTypeId id for the type of penalty
     * @throws InterruptedException for when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    private void postfault(Player p, int penaltyTypeId) throws InterruptedException{
        Call<Void> call = apiService.addPenalty(match.getMatch_id(),p.getPlayer_id(),penaltyTypeId);
        try {
            call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that creates a task on a separate thread to post to the backend that the match has been signed.
     *
     * @param email email of the assigned match referee
     * @param password password of the assigned match referee
     */
    public void asyncPostSignMatch(final String email, final String password){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    postSignMatch(email, password);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(task, "Service thread testpost").start();

    }

    /**
     * Method that calls the {@link ApiInterface#signMatch(int, String, String)} to post that the match has been signed to the backend.
     * This method displays a toast in the when the match has been succesfully signed or when an authentication 400, 401, 402, 403, 422 error has occurred.
     *
     * @param email email of the assigned match referee
     * @param password password of the assigned match referee
     * @throws InterruptedException for when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    private void postSignMatch(String email, String password) throws InterruptedException{
        final AdministrationEnd[] adminEnd = {(AdministrationEnd) getCurrentActivity()};

        Call<Void> call = apiService.signMatch(match.getMatch_id(),email,password);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    displayToast("You have succesfully signed the match");
                    try {
                        adminEnd[0].finishAdmin();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    switch(response.code()) {
                        case 400:
                            displayToast("Authentication failed when signing form, please fill in your credentials correctly");
                            break;
                        case 401:
                            displayToast("Authentication failed when signing form, please fill in your credentials correctly");
                            break;
                        case 402:
                            displayToast("Authentication failed when signing form, please fill in your credentials correctly");
                            break;
                        case 403:
                            displayToast("Authentication failed when signing form, please fill in your credentials correctly");
                            break;
                        case 422:
                            displayToast("Authentication failed when signing form, please fill in your credentials correctly");
                            break;
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                displayToast("Connection with server failed");
            }
        });

    }

    /**
     * Method that calls the {@link ApiInterface#updateNumber(int, int)} to post the new capnumber of the player.
     *
     * @param playerid id of the player to update the number for
     * @param number the capnumber of the player
     * @throws InterruptedException for when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    private void updateNumber(int playerid, int number) throws InterruptedException {
        Call<Void> call = apiService.updateNumber(playerid,number);
        try {
            call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that creates a task on a separate thread to post to the backend that an active player has been switched pre match and needs to switch capnumber.
     *
     * @param playerid id of the player to update the number for
     * @param number the capnumber of the player
     */
    public void asyncUpdatePlayerNumber(int playerid, int number){
        final int p = playerid;
        final int n = number;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    updateNumber(p,n);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(task, "Service thread testpost").start();
    }

    /**
     * Method that calls the {@link ApiInterface#putListOfStarters(int, ApiClient.ArrayListStarters)} to update the active players to the backend.
     *
     * @param starters the list of players that wil play in the match
     * @throws InterruptedException for when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    private void updatestarters(ApiClient.ArrayListStarters starters) throws InterruptedException {
        Call<ResponseBody> call = apiService.putListOfStarters(getSelectedMatch().getMatch_id(),starters);
        try {
            call.execute();
        } catch (Exception e) {
            Log.e("log_tag","den eersten eeft nie gewerkt");
        }
    }

    /**
     * Method that creates a task on a separate thread to post the active players to the backend.
     * These are all the players that will perform actions during the match
     *
     * @param starters the list of players that wil play in the match
     */
    public void asyncUpdateStarters(ApiClient.ArrayListStarters starters){
        final ApiClient.ArrayListStarters s =  starters;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    updatestarters(s);
                } catch (InterruptedException e) {
                    Log.e("log_tag","theeft nie gewerkt 2");
                }
            }
        };
        new Thread(task, "Service thread testpost").start();
    }
}
