package persistency;

import com.google.gson.annotations.SerializedName;

/**
 * Created by laure on 21/11/2017.
 */

public class DivisionRest {
    @SerializedName("id")
    private int divisionid;
    @SerializedName("name")
    private String division_name;
    @SerializedName("period_length")
    private String period_length;
    @SerializedName("ranking")
    private int goalId;

    public int getDivisionid() {
        return divisionid;
    }

    public void setDivisionid(int divisionid) {
        this.divisionid = divisionid;
    }

    public String getDivision_name() {
        return division_name;
    }

    public void setDivision_name(String division_name) {
        this.division_name = division_name;
    }

    public String getPeriod_length() {
        return period_length;
    }

    public void setPeriod_length(String period_length) {
        this.period_length = period_length;
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }
}
