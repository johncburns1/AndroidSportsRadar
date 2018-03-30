package hu.ait.android.sportsradar.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by johnc on 12/11/2017.
 */

public class Outcome implements Parcelable{

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("odds")
    @Expose
    public String odds;
    @SerializedName("total")
    @Expose
    public String total;
    @SerializedName("spread")
    @Expose
    public String spread;

    protected Outcome(Parcel in) {
        type = in.readString();
        odds = in.readString();
        total = in.readString();
        spread = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(odds);
        dest.writeString(total);
        dest.writeString(spread);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Outcome> CREATOR = new Creator<Outcome>() {
        @Override
        public Outcome createFromParcel(Parcel in) {
            return new Outcome(in);
        }

        @Override
        public Outcome[] newArray(int size) {
            return new Outcome[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSpread() {
        return spread;
    }

    public void setSpread(String spread) {
        this.spread = spread;
    }
}
