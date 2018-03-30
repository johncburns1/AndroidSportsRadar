package hu.ait.android.sportsradar.network;

import hu.ait.android.sportsradar.data.SportResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by johnc on 12/11/2017.
 */

public interface SportAPI {

    @GET("oddscomparison-ust1/en/eu/sports/sr:sport:{num}/{year}-{month}-{day}/schedule.json")
    Call<SportResult> getSportData(@Path("num") String num, @Path("year") String year, @Path("month") String month, @Path("day") String day, @Query("api_key") String api_key);

}
