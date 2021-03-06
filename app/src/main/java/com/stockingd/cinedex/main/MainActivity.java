package com.stockingd.cinedex.main;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.stockingd.cinedex.ActivityRxJavaModule;
import com.stockingd.cinedex.R;
import com.stockingd.cinedex.app.BaseActivity;
import com.stockingd.cinedex.movie_list.MovieListFragment;
import com.stockingd.cinedex.movie_list.MovieListFragmentArgs;
import com.stockingd.cinedex.utils.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainContract.View {

    public static final String INSTANCE_STATE_TITLE = "INSTANCE_STATE_TITLE";
    @Inject MainPresenter presenter;
    @Inject ActivityUtils activityUtils;

    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.drawer) View drawer;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindString(R.string.app_name) String appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().mainComponent(new MainModule(this, this),
                                  new ActivityRxJavaModule()).inject(this);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                                                                 drawerLayout,
                                                                 toolbar,
                                                                 R.string.navigation_drawer_open,
                                                                 R.string.navigation_drawer_closed);
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();
        presenter.onCreate(savedInstanceState != null);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            ActionBar actionbar = getSupportActionBar();
            if (actionbar != null) {
                String title = savedInstanceState.getString(INSTANCE_STATE_TITLE, appName);
                actionbar.setTitle(title);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void showMostPopular() {
        swapIn(MovieListFragmentArgs.Mode.MostPopular);
        activityUtils.setActionBarTitle("Most Popular");
    }

    @Override
    public void showHighestRated() {
        swapIn(MovieListFragmentArgs.Mode.HighestRated);
        activityUtils.setActionBarTitle("Highest Rated");
    }

    @Override
    public void showFavorites() {
        swapIn(MovieListFragmentArgs.Mode.Favorites);
        activityUtils.setActionBarTitle("Favorites");
    }

    private void swapIn(MovieListFragmentArgs.Mode mode) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putParcelable(MovieListFragment.EXTRA_ARGUMENTS, MovieListFragmentArgs.create(mode));
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_container, fragment)
                .commit();
    }

    @Override
    public void closeDrawer() {
        drawerLayout.closeDrawer(drawer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            outState.putString(INSTANCE_STATE_TITLE, actionBar.getTitle().toString());
        }
    }
}
