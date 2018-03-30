package hu.ait.android.sportsradar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hu.ait.android.sportsradar.data.Competitor;
import hu.ait.android.sportsradar.data.Market;
import hu.ait.android.sportsradar.data.Match;
import hu.ait.android.sportsradar.data.Outcome;
import hu.ait.android.sportsradar.data.Sport;
import hu.ait.android.sportsradar.data.SportEvent;
import hu.ait.android.sportsradar.data.SportResult;
import hu.ait.android.sportsradar.network.SportAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static hu.ait.android.sportsradar.adapter.SportAdapter.KEY_MATCHES;

public class Matches extends AppCompatActivity {

    private ArrayList<Match> matchList;
    private String sportName;
    private String sportID;
    private Toolbar toolbar;
    private SportAPI sportAPI;
    private Context context = this;

    public static final String HOME = "home";
    public static final String THREE_WAY = "3way";
    public static final String TWO_WAY = "2way";
    public static final String TOTAL = "total";
    public static final String SPREAD = "spread";
    public static final String HOME_SPREAD = "home spread";
    public static final String AWAY_SPREAD = "away spread";
    public static final String LABEL_THREE = "Odds: Home, Away, Draw";
    public static final String LABEL_TWO = "Odds: Home, Away";
    public static final String LABEL_TOTAL = "Over, Under, Total";
    public static final String LABEL_SPREAD = "Odds: Home, Away/Spread: Home, Away";
    public static final String COLOR_BLUE = "#5bc0de";
    public static final String COLOR_GREEN = "#5cb85c";
    public static final String COLOR_NEGATIVE = "#d9534f";
    public static final String COLOR_POSITIVE = "#f0ad4e";
    private static final String API_KEY = "ax9tt84a8peu2embkw5zcyu8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        toolbar = (Toolbar) findViewById(R.id.matchesToolbar);

        Intent intent = getIntent();
        matchList = intent.getParcelableArrayListExtra(KEY_MATCHES);

        sportName = matchList.get(0).getSportName();
        sportID = matchList.get(0).getSportID();
        toolbar.setTitle("Match Info and Lines (" + sportName + ")");

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMatches);
        RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(expMgr.createWrappedAdapter(new MyAdapter(matchList)));

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        expMgr.attachRecyclerView(recyclerView);

        final PullToRefreshView mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshData(sportID, sportName);
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, MainActivity.REFRESH_DELAY);
            }
        });
    }

    static abstract class MyHeaderBase {
        public final long id;
        public final Match match;

        public MyHeaderBase(long id, Match match) {
            this.id = id;
            this.match = match;
        }
    }

    static abstract class MyChildBase {
        public final long id;
        public final String home;
        public final String away;
        public final Market market;

        public MyChildBase(long id, String home, String away, Market market) {
            this.id = id;
            this.home = home;
            this.away = away;
            this.market = market;
        }
    }

    static class MyGroupItem extends MyHeaderBase {
        public final List<MyChildItem> children;

        public MyGroupItem(long id, Match match) {
            super(id, match);
            children = new ArrayList<>();
        }
    }

    static class MyChildItem extends MyChildBase {
        public MyChildItem(long id, String home, String away, Market market) {
            super(id, home, away, market);
        }
    }

    static abstract class MyBaseGroupViewHolder extends AbstractExpandableItemViewHolder {
        TextView tvMatchHeader;
        TextView tvLeague;
        TextView tvStadium;
        ImageView ivMatchIcon;

        public MyBaseGroupViewHolder(View itemView) {
            super(itemView);
            tvMatchHeader = itemView.findViewById(R.id.tvMatchHeader);
            tvLeague = itemView.findViewById(R.id.tvLeague);
            tvStadium = itemView.findViewById(R.id.tvStadium);
            ivMatchIcon = itemView.findViewById(R.id.ivRowIcon);
        }
    }

    static abstract class MyBaseChildViewHolder extends AbstractExpandableItemViewHolder {
        GraphView gvChild;

        public MyBaseChildViewHolder(View itemView) {
            super(itemView);
            gvChild = (GraphView) itemView.findViewById(R.id.graph);
        }
    }

    static class MyGroupViewHolder extends MyBaseGroupViewHolder {
        public MyGroupViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class MyChildViewHolder extends MyBaseChildViewHolder {
        public MyChildViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class MyAdapter extends AbstractExpandableItemAdapter<MyGroupViewHolder, MyChildViewHolder> {
        List<MyGroupItem> mItems;
        List<Match> matchList;

        public MyAdapter(List<Match> matchList) {
            setHasStableIds(true);

            this.matchList = matchList;
            mItems = new ArrayList<>();
            for (int i = 0; i < matchList.size(); i++) {
                Match match = matchList.get(i);
                MyGroupItem group = new MyGroupItem(i, match);
                List<Market> markets = match.getMarkets();
                if (markets != null) {

                    String home = match.getCompetitors().get(0).getName();
                    String away = match.getCompetitors().get(1).getName();

                    for (int j = 0; j < markets.size(); j++) {
                        group.children.add(new MyChildItem(j, home, away, markets.get(j)));
                    }
                    mItems.add(group);
                }
            }
        }

        @Override
        public int getGroupCount() {
            return mItems.size();
        }

        @Override
        public int getChildCount(int groupPosition) {
            return mItems.get(groupPosition).children.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return mItems.get(groupPosition).id;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return mItems.get(groupPosition).children.get(childPosition).id;
        }

        @Override
        public MyGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group_item, parent, false);
            return new MyGroupViewHolder(v);
        }

        @Override
        public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_child_item, parent, false);
            return new MyChildViewHolder(v);
        }

        @Override
        public void onBindGroupViewHolder(MyGroupViewHolder holder, int groupPosition, int viewType) {
            MyGroupItem group = mItems.get(groupPosition);

            String startTime = "";
            if (group.match.getStartTime() != null) {
                startTime = group.match.getStartTime();
            }

            Competitor homeTeam = group.match.getCompetitors().get(0);
            Competitor awayTeam = group.match.getCompetitors().get(1);
            String homeCC = "";
            String awayCC = "";

            if (homeTeam.getCountryCode() != null) {
                homeCC = "(" + homeTeam.getCountryCode() + ")";
            }
            if (awayTeam.getCountryCode() != null) {
                awayCC = "(" + awayTeam.getCountryCode() + ")";
            }

            holder.tvMatchHeader.setText(
                    homeTeam.getAbbreviation() + homeCC +
                            " vs " + awayTeam.getAbbreviation() + awayCC);

            holder.tvLeague.setText(group.match.getLeague() + ", " + group.match.getPlayType());

            if (group.match.getStadium() != null &&
                    group.match.getCityOfPlay() != null && group.match.getCountryOfPlay() != null) {
                holder.tvStadium.setText(
                        group.match.getStadium() + ", " + group.match.getCityOfPlay() + "(" + group.match.getCountryOfPlay() + ") " + startTime);
            } else {
                holder.tvStadium.setText(startTime);
            }
            holder.ivMatchIcon.setImageResource(group.match.getIcon());
        }

        @Override
        public void onBindChildViewHolder(MyChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
            MyChildItem child = mItems.get(groupPosition).children.get(childPosition);
            Market market = child.market;
            holder.gvChild.getSeries().clear();

            String market_type = market.getName();
            List<Outcome> outcomes = market.getBooks().get(0).getOutcomes();

            BarGraphSeries<DataPoint> series = null;

            if (market_type.equals(THREE_WAY)) {
                double home_odds = Double.parseDouble(outcomes.get(0).getOdds());
                double away_odds = Double.parseDouble(outcomes.get(1).getOdds());
                double tie_odds = Double.parseDouble(outcomes.get(2).getOdds());

                DataPoint[] dataPoints = {new DataPoint(1, home_odds), new DataPoint(2, away_odds), new DataPoint(3, tie_odds)};
                series = new BarGraphSeries<DataPoint>(dataPoints);

                holder.gvChild.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);
                holder.gvChild.getGridLabelRenderer().setHorizontalAxisTitleTextSize(50);
                holder.gvChild.getGridLabelRenderer().setHorizontalAxisTitle(LABEL_THREE);

            } else if (market_type.equals(TWO_WAY)) {
                double home_odds = Double.parseDouble(outcomes.get(0).getOdds());
                double away_odds = Double.parseDouble(outcomes.get(1).getOdds());

                DataPoint[] dataPoints = {new DataPoint(1, home_odds), new DataPoint(2, away_odds)};
                series = new BarGraphSeries<DataPoint>(dataPoints);

                holder.gvChild.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);
                holder.gvChild.getGridLabelRenderer().setHorizontalAxisTitleTextSize(50);
                holder.gvChild.getGridLabelRenderer().setHorizontalAxisTitle(LABEL_TWO);

            } else if (market_type.equals(TOTAL) || market_type.equals(SPREAD)) {
                double home_odds = Double.parseDouble(outcomes.get(0).getOdds());
                double away_odds = Double.parseDouble(outcomes.get(1).getOdds());

                if (market_type.equals(TOTAL)) {
                    double total = Double.parseDouble(outcomes.get(1).getTotal());

                    DataPoint[] dataPoints = {new DataPoint(1, home_odds), new DataPoint(2, away_odds), new DataPoint(3, total)};
                    series = new BarGraphSeries<DataPoint>(dataPoints);

                    holder.gvChild.getGridLabelRenderer().setHorizontalAxisTitle(LABEL_TOTAL);

                } else {
                    double top_spread = Double.parseDouble(outcomes.get(0).getSpread());
                    double bottom_spread = Double.parseDouble(outcomes.get(1).getSpread());

                    DataPoint[] dataPoints = {new DataPoint(1, home_odds), new DataPoint(2, away_odds), new DataPoint(3, top_spread), new DataPoint(4, bottom_spread)};
                    series = new BarGraphSeries<DataPoint>(dataPoints);

                    holder.gvChild.getGridLabelRenderer().setHorizontalAxisTitle(LABEL_SPREAD);
                }

                holder.gvChild.getGridLabelRenderer().setHorizontalAxisTitleTextSize(50);
                holder.gvChild.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);
            }

            series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                @Override
                public int get(DataPoint data) {
                    if (data.getX() == 1) {
                        return Color.parseColor(COLOR_POSITIVE);
                    } else if (data.getX() == 2) {
                        return Color.parseColor(COLOR_BLUE);
                    } else if (data.getX() == 3) {
                        if (data.getY() < 0) {
                            return Color.parseColor(COLOR_NEGATIVE);
                        } else {
                            return Color.parseColor(COLOR_GREEN);
                        }
                    } else {
                        if (data.getY() < 0) {
                            return Color.parseColor(COLOR_NEGATIVE);
                        } else {
                            return Color.parseColor(COLOR_GREEN);
                        }
                    }
                }
            });

            series.setSpacing(50);
            series.setAnimated(true);
            series.setDrawValuesOnTop(true);
            series.setValuesOnTopColor(Color.WHITE);
            series.setValuesOnTopSize(80);
            holder.gvChild.addSeries(series);

            holder.gvChild.getViewport().setXAxisBoundsManual(true);
            holder.gvChild.getViewport().setMinX(0);
            holder.gvChild.getViewport().setMaxX(5);

            holder.gvChild.getViewport().setYAxisBoundsManual(true);
            holder.gvChild.getViewport().setMinY(-6);
            holder.gvChild.getViewport().setMaxY(6);

            holder.gvChild.setTitle(child.home + " vs " + child.away + " (" + market_type + ")");
            holder.gvChild.setTitleColor(Color.WHITE);
            holder.gvChild.setTitleTextSize(50);
            holder.gvChild.getGridLabelRenderer().setHorizontalLabelsVisible(false);
            holder.gvChild.getGridLabelRenderer().setHighlightZeroLines(true);
            holder.gvChild.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
            holder.gvChild.getGridLabelRenderer().setLabelsSpace(10);
        }

        @Override
        public boolean onCheckCanExpandOrCollapseGroup(MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
            return true;
        }
    }

    private void refreshData(final String sportID, final String sportName) {
        String[] date = getDate(); //{"15", "12", "2017"};//getDate();
        matchList.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.sportradar.us")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sportAPI = retrofit.create(SportAPI.class);

        Call<SportResult> call =
                sportAPI.getSportData(sportID, date[2], date[1], date[0], API_KEY);
        call.enqueue(new Callback<SportResult>() {

            @Override
            public void onResponse(Call<SportResult> call, Response<SportResult> response) {

                if (response.isSuccessful()) {
                    String message = (String) (response.message() + ", " + sportName + " " +context.getString(R.string.matches_found));
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    ArrayList<Match> events = new ArrayList<Match>();
                    Sport sport_header = response.body().getSport();
                    SportEvent event;

                    for (int i = 0; i < response.body().getSportEvents().size(); i++) {
                        event = response.body().getSportEvents().get(i);
                        Match newMatch = new Match();
                        newMatch.setSportName(sport_header.getName());
                        newMatch.setSportID(sportID);

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

                } else {
                    String message = (String) (sportName +" "+ context.getString(R.string.matches_spaces) +" "+ response.message() +" "+ context.getString(R.string.today));
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
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

    public String[] getDate() {
        DateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String[] formatted_date = dateFormater.format(date).split("/");
        return formatted_date;
    }
}
