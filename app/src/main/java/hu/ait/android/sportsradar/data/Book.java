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

public class Book implements Parcelable{

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("outcomes")
    @Expose
    public List<Outcome> outcomes = null;

    protected Book(Parcel in) {
        id = in.readString();
        if(outcomes == null) {
            outcomes = new ArrayList<Outcome>();
        }
        in.readTypedList(outcomes, Outcome.CREATOR);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Outcome> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(List<Outcome> outcomes) {
        this.outcomes = outcomes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeTypedList(outcomes);
    }
}
