package com.example.salesapp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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
import com.example.salesapp.Fragments.ProductsFragment;
import com.example.salesapp.Fragments.UsersFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private View actionBar;
    private ImageButton backArrow,discountBtn;
    private LinearLayout viewCart;
    private FragmentManager fragmentManager;
    private DbHandler dbHandler;
    private HomeFragment homeFragment;

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
        viewCart = actionBar.findViewById(R.id.viewCartBtn);
        discountBtn = actionBar.findViewById(R.id.discountBtn);
        fragmentManager = getSupportFragmentManager();
        actionBar = getSupportActionBar().getCustomView();
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setArrow(true);
                backFragments();
            }
        });

    }
    public void backFragments(){
        int backStackCount = fragmentManager.getBackStackEntryCount();
        FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(backStackCount-1);
        String fragmentTag = backStackEntry.getName();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        fragmentManager.beginTransaction()
                .remove(fragment)
                .commit();
        fragmentManager.popBackStack();
        //finish();
        backStackCount = fragmentManager.getBackStackEntryCount();
        if(backStackCount<3){
            setArrow(true);
        }
        if(backStackCount < 2){
            finish();
        }
    }
    public void refreshFragments(String fragmentTag){
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        fragmentManager.beginTransaction()
                .remove(fragment)
                .commit();
        //fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment.getClass(), null,fragmentTag)
                .commit();
    }
    public void setArrow(boolean isHome){

        if(!isHome){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            backArrow.setVisibility(View.VISIBLE);
            viewCart.setVisibility(View.GONE);
            discountBtn.setVisibility(View.GONE);

        }else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            backArrow.setVisibility(View.GONE);
            viewCart.setVisibility(View.VISIBLE);
            discountBtn.setVisibility(View.VISIBLE);
        }
//        if(!homeFragment.getIsCarting()){
//            backArrow.setVisibility(View.VISIBLE);
//            viewCart.setVisibility(View.GONE);
//        }else{
//            backArrow.setVisibility(View.GONE);
//            viewCart.setVisibility(View.VISIBLE);
//        }
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
                if (id == R.id.nav_products) {
                    onNavItemProducts();
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
    public void onNavItemHome(){
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
    public void onNavItemProducts(){
        setArrow(false);
//        fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .remove(new ProductsFragment())
//                .commit();
//        fragmentManager.popBackStack();
//
//        fragmentManager.beginTransaction()
//                .remove(new HomeFragment())
//                .commit();
//        fragmentManager.popBackStack();
//
//        fragmentManager.beginTransaction()
//                .remove(new ChargeFragment())
//                .commit();
//        fragmentManager.popBackStack();
//
//        fragmentManager.beginTransaction()
//                .remove(new CartFragment())
//                .commit();
//        fragmentManager.popBackStack();

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, ProductsFragment.class,null,ProductsFragment.TAG)
                .setReorderingAllowed(true)
                .addToBackStack("ProductsFragment")
                .commit();
    }

}

