package hu.ait.android.sportsradar.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnc on 12/11/2017.
 */

public class SportResult implements Parcelable{

    @SerializedName("generated_at")
    @Expose
    public String generatedAt;
    @SerializedName("schema")
    @Expose
    public String schema;
    @SerializedName("sport")
    @Expose
    public Sport sport;
    @SerializedName("sport_events")
    @Expose
    public List<SportEvent> sportEvents = null;

    protected SportResult(Parcel in) {
        generatedAt = in.readString();
        schema = in.readString();
        if(sportEvents == null) {
            sportEvents = new ArrayList<SportEvent>();
        }
        in.readTypedList(sportEvents, SportEvent.CREATOR);
    }

    public static final Creator<SportResult> CREATOR = new Creator<SportResult>() {
        @Override
        public SportResult createFromParcel(Parcel in) {
            return new SportResult(in);
        }

        @Override
        public SportResult[] newArray(int size) {
            return new SportResult[size];
        }
    };

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public List<SportEvent> getSportEvents() {
        return sportEvents;
    }

    public void setSportEvents(List<SportEvent> sportEvents) {
        this.sportEvents = sportEvents;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(generatedAt);
        parcel.writeString(schema);
        parcel.writeTypedList(sportEvents);
    }
}
