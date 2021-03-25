package com.joelnemi.foro;

import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.joelnemi.foro.fragments.HomeFragment;
import com.joelnemi.foro.fragments.SearchFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_saved:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.flMainLayout, HomeFragment.getInstance()).addToBackStack(null)
                                .commit();
                        return true;
                    case R.id.nav_history:
                        //openFragment(NotificationFragment.newInstance("", ""));
                        return true;
                    case R.id.nav_comunidad:
                        //openFragment(NotificationFragment.newInstance("", ""));
                        return true;
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        bottomNavigation.setOnNavigationItemSelectedListener(this);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flMainLayout, HomeFragment.getInstance()).addToBackStack(null)
                        .commit();

                return true;
            case R.id.navigation_search:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flMainLayout, SearchFragment.getInstance()).addToBackStack(null)
                        .commit();
                return true;
            case R.id.navigation_upload:
                //openFragment(NotificationFragment.newInstance("", ""));
                return true;
            case R.id.navigation_messages:
                //openFragment(NotificationFragment.newInstance("", ""));
                return true;
            case R.id.navigation_profile:
                //openFragment(NotificationFragment.newInstance("", ""));
                return true;
        }
        return false;
    }



    /*@Override
    public void onNavigationItemSelected(@NonNull  MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flMainLayout, HomeFragment.getInstance()).addToBackStack(null)
                        .commit();

                break;
            case R.id.navigation_search:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flMainLayout, SearchFragment.getInstance()).addToBackStack(null)
                        .commit();
                break;
            case R.id.navigation_upload:
                //openFragment(NotificationFragment.newInstance("", ""));
                break;
        }
    }*/
}
