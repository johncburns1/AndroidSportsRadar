package hu.ait.android.sportsradar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import hu.ait.android.sportsradar.Matches;
import hu.ait.android.sportsradar.R;
import hu.ait.android.sportsradar.data.ListSport;
import hu.ait.android.sportsradar.data.Match;
import hu.ait.android.sportsradar.data.Sport;
import hu.ait.android.sportsradar.data.SportEvent;
import hu.ait.android.sportsradar.data.SportResult;
import hu.ait.android.sportsradar.network.SportAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by johnc on 12/11/2017.
 */

public class SportAdapter extends RecyclerView.Adapter<SportAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public KenBurnsView ivSport;

        public ViewHolder(View itemView) {
            super(itemView);

            ivSport = (KenBurnsView) itemView.findViewById(R.id.ivSport);
        }
    }

    public static final String BBALL = "Basketball";
    public static final String DOTA = "Dota";
    public static final String OW = "Overwatch";
    public static final String NFL = "Football";
    private static final String API_KEY = "ax9tt84a8peu2embkw5zcyu8";
    public static final String KEY_MATCHES = "KEY_MATCHES";

    private List<ListSport> sportList;
    private SportAPI sportAPI;
    private Context context;
    private int lastPosition = -1;

    public SportAdapter(List<ListSport> sportList, Context context) {
        this.sportList = sportList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.sport_list_activity, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ListSport sport = sportList.get(position);
        final String sportName = sport.getName();

        switch (sportName) {
            case NFL:
                holder.ivSport.setImageResource(R.drawable.nfl);
                sport.setImage(R.drawable.team);
                break;
            case BBALL:
                holder.ivSport.setImageResource(R.drawable.nba);
                sport.setImage(R.drawable.bball);
                break;
            case OW:
                holder.ivSport.setImageResource(R.drawable.ow_logo);
                sport.setImage(R.drawable.ow);
                break;
            case DOTA:
                holder.ivSport.setImageResource(R.drawable.dota_logo);
                sport.setImage(R.drawable.dota);
                break;
        }

        holder.ivSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] date = getDate(); //{"15", "12", "2017"};//getDate();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.sportradar.us")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                sportAPI = retrofit.create(SportAPI.class);

                Call<SportResult> call =
                        sportAPI.getSportData(sport.getSportID(), date[2], date[1], date[0], API_KEY);
                call.enqueue(new Callback<SportResult>() {

                    @Override
                    public void onResponse(Call<SportResult> call, Response<SportResult> response) {

                        if (response.isSuccessful()) {
                            String message = (String) (response.message() + ", " + sportName + " " +context.getString(R.string.matches_found));
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                            ArrayList<Match> matchList = new ArrayList<Match>();
                            Sport sport_header = response.body().getSport();
                            SportEvent event;

                            for (int i = 0; i < response.body().getSportEvents().size(); i++) {
                                event = response.body().getSportEvents().get(i);
                                Match newMatch = new Match();
                                newMatch.setIcon(sportList.get(holder.getAdapterPosition()).getImage());
                                newMatch.setSportName(sport_header.getName());
                                newMatch.setSportID(sport.getSportID());

                                String time = event.getScheduled();
                                if (time != null) {
                                    String[] timeSplit = time.split("T");
                                    timeSplit = timeSplit[1].split("\\u002B");
                                    newMatch.setStartTime(timeSplit[0]);
                                }

                                newMatch.setCompetitors(event.getCompetitors());
                                newMatch.setLeague(event.getTournament().getName());
                                newMatch.setPlayType(event.getTournament().getCategory().getName());

                                if (event.getVenue() != null) {
                                    newMatch.setStadium(event.getVenue().getName());
                                    newMatch.setCityOfPlay(event.getVenue().getCityName());
                                    newMatch.setCountryOfPlay(event.getVenue().getCountryCode());
                                }
                                if (event.getMarkets() != null) {
                                    newMatch.setMarkets(event.getMarkets());
                                }

                                matchList.add(newMatch);
                            }

                            Intent matchIntent = new Intent(context, Matches.class);
                            matchIntent.putParcelableArrayListExtra(KEY_MATCHES, matchList);
                            context.startActivity(matchIntent);

                        } else {
                            String message = (String) (sportName +" "+ context.getString(R.string.matches_spaces) +" "+ response.message() +" "+ context.getString(R.string.today));
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                            try {
                                System.out.println(response.errorBody().string());

                            } catch (IOException e) {
                                e.getStackTrace();

                            } finally {
                                System.out.println(call.request().url());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SportResult> call, Throwable t) {
                        Toast.makeText(context, R.string.request_failed, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return sportList.size();
    }

    public void swapSports(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(sportList, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(sportList, i, i - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
    }

    public String[] getDate() {
        DateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String[] formatted_date = dateFormater.format(date).split("/");
        return formatted_date;
    }
}
