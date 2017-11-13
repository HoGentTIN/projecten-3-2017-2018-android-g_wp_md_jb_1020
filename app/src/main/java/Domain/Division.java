package Domain;

import java.sql.Time;

/**
 * Created by Impling on 18-Oct-17.
 */

public class Division {

    private int division_id;
    private String divisionName;
    private DivisionType divisionType;

    public Division(String divisionName, DivisionType divisionType){
        this.divisionName = divisionName;
        this.divisionType = divisionType;
    }

}
