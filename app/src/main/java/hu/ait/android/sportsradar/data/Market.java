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

public class Market implements Parcelable{

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("group_name")
    @Expose
    public String groupName;
    @SerializedName("books")
    @Expose
    public List<Book> books = null;

    protected Market(Parcel in) {
        name = in.readString();
        groupName = in.readString();
        if(books == null) {
            books = new ArrayList<Book>();
        }
        in.readTypedList(books, Book.CREATOR);
    }

    public static final Creator<Market> CREATOR = new Creator<Market>() {
        @Override
        public Market createFromParcel(Parcel in) {
            return new Market(in);
        }

        @Override
        public Market[] newArray(int size) {
            return new Market[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(groupName);
        parcel.writeTypedList(books);
    }
}
