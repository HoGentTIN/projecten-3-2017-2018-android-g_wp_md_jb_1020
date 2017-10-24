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
    private Official o;

    private List<String[]> logList = new ArrayList<>();
    private int eventCounter = 0;

    private String matchCode = "National012";                                                       //Temporary init value replace by match code after administration is done

    public Domaincontroller(){

    }


    //Function: appendLog - add an event to the processLog
    //eventCode seems pretty useless => replace by matchCode = same for entire match
    public void appendLog(String eventDescription){
        //Get Time of event
        String timeOfEvent = "" + Calendar.getInstance().getTime().getHours() + ":" + Calendar.getInstance().getTime().getMinutes() + ":" + Calendar.getInstance().getTime().getSeconds();
        String[] logInfo = new String[]{timeOfEvent, matchCode, eventDescription};
        logList.add(eventCounter, logInfo);

        Log.i("logInfo", eventCounter + " [" + logList.get(eventCounter)[0].toString() + "] " + logList.get(eventCounter)[1].toString() +  ": " + logList.get(eventCounter)[2].toString()); //Testinglog
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




    //eventcode - description => Overview of eventlogcodes and corresponding description
    //G01 - Player [Int playernumber] [String playername] from the hometeam has scored a goal [int homescore] - [ int awayscore]    //Goalevent
    //

}
