package hu.ait.android.sportsradar.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by johnc on 12/11/2017.
 */

class TournamentRound {

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("number")
    @Expose
    public Integer number;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}

