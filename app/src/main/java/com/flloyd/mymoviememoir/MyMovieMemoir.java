package com.flloyd.mymoviememoir;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.flloyd.mymoviememoir.fragment.HomeFragment;
import com.flloyd.mymoviememoir.fragment.MapFragment;
import com.flloyd.mymoviememoir.fragment.MovieMemoirFragment;
import com.flloyd.mymoviememoir.fragment.MovieSearchFragment;
import com.flloyd.mymoviememoir.fragment.ReportFragment;
import com.flloyd.mymoviememoir.fragment.WatchListFragment;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class MyMovieMemoir  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_movie_memoir);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nv);
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new HomeFragment());
    }

    private void replaceFragment(Fragment nextFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                replaceFragment(new HomeFragment());
                break;
            case R.id.displayMessage:
                replaceFragment(new MovieSearchFragment());
                break;
            case R.id.watchlist:
                replaceFragment(new WatchListFragment());
                break;
            case  R.id.MovieMemoir:
                replaceFragment( new MovieMemoirFragment());
                break;
            case  R.id.Report:
                replaceFragment( new ReportFragment());
                break;
            case  R.id.mapsPage:
                replaceFragment(new MapFragment());
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
