package com.example.salesapp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.salesapp.Database.DbHandler;
import com.example.salesapp.Fragments.CartFragment;
import com.example.salesapp.Fragments.ChargeFragment;
import com.example.salesapp.Fragments.HomeFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private View actionBar;
    private ImageButton backArrow;
    private FragmentManager fragmentManager;
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DbHandler(MainActivity.this);
        initActionBar();
        initDrawer();
        onNavItemHome();
    }
    private void initDrawer(){
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        DrawerArrowDrawable drawerArrowDrawable = new DrawerArrowDrawable(this);
        drawerArrowDrawable.setColor(ContextCompat.getColor(this, R.color.white));
        actionBarDrawerToggle.setDrawerArrowDrawable(drawerArrowDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setupNavigation();
    }
    private void initActionBar(){
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.salesBlue)));
        actionBar = getSupportActionBar().getCustomView();
        backArrow = actionBar.findViewById(R.id.backArrow);
        fragmentManager = getSupportFragmentManager();
        actionBar = getSupportActionBar().getCustomView();
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupBackFunction();
            }
        });

    }
    private void setupBackFunction(){
        Fragment fragment = setArrow();
        fragmentManager.beginTransaction()
                .remove(fragment)
                .commit();
        fragmentManager.popBackStack();
        //finish();
    }
    public Fragment setArrow(){
        int backStackCount = fragmentManager.getBackStackEntryCount();
        FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(backStackCount-1);
        String fragmentTag = backStackEntry.getName();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        if(backStackCount>0){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            backArrow.setVisibility(View.VISIBLE);

        }else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            backArrow.setVisibility(View.GONE);
        }
        return fragment;
    }
    private void setupNavigation() {
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle item clicks here
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    onNavItemHome();
                }
//                else if (id == R.id.nav_account) {
//                    onNavItemAccount();
//                } else if (id == R.id.nav_settings) {
//                    onNavItemSettings();
//                } else if (id == R.id.nav_logout) {
//                    onNavItemLogout();
//                }
                drawerLayout.closeDrawers(); // Close the navigation drawer
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void onNavItemHome(){
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .remove(new HomeFragment())
            .commit();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .remove(new ChargeFragment())
                .commit();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .remove(new CartFragment())
                .commit();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, HomeFragment.class,null,HomeFragment.TAG)
                .setReorderingAllowed(true)
                .addToBackStack("HomeFragment")
                .commit();
    }
}