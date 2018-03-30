package hu.ait.android.sportsradar;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import hu.ait.android.sportsradar.adapter.SportAdapter;
import hu.ait.android.sportsradar.data.ListSport;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REFRESH_DELAY = 2000;
    private SportAdapter sportAdapter;

    private List<ListSport> sportList;
    private ListSport fb;
    private ListSport bball;
    private ListSport dota;
    private ListSport ow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sportList = new ArrayList<ListSport>();

        fb = new ListSport(sportAdapter.NFL, getString(R.string.football));
        sportList.add(fb);

        bball = new ListSport(sportAdapter.BBALL, getString(R.string.nba));
        sportList.add(bball);

        dota = new ListSport(sportAdapter.DOTA, getString(R.string.dota));
        sportList.add(dota);

        ow = new ListSport(sportAdapter.OW, getString(R.string.overwatch));
        sportList.add(ow);

        sportAdapter = new SportAdapter(sportList, this);
        RecyclerView recyclerViewSports = (RecyclerView) findViewById(R.id.recyclerViewSports);
        recyclerViewSports.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSports.setAdapter(sportAdapter);

        final PullToRefreshView mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.about) {
            Toast.makeText(getApplicationContext(), R.string.about_message, Toast.LENGTH_LONG).show();
        }

        else if(id == R.id.help) {
            Toast.makeText(getApplicationContext(), R.string.help_message, Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
