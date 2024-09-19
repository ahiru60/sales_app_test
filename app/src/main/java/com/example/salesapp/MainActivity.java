package com.example.salesapp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.salesapp.Fragments.ChargeFragment;
import com.example.salesapp.Fragments.DatePickerFragment;
import com.example.salesapp.Fragments.HomeFragment;
import com.example.salesapp.Fragments.ProductsFragment;
import com.example.salesapp.Fragments.ReceiptFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private View actionBar;
    private ImageButton backArrow,discountBtn,addUserBtn,dateRange;
    private TextView title;
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
        title = actionBar.findViewById(R.id.action_bar_title);
        backArrow = actionBar.findViewById(R.id.backArrow);
        addUserBtn = actionBar.findViewById(R.id.action_bar_add_user);
        dateRange = actionBar.findViewById(R.id.action_bar_select_date);
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
        dateRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment().show(getSupportFragmentManager(), "datePicker");
            }
        });
    }
    public int presstime = 0;
    public void backFragments(){

        int backStackCount = fragmentManager.getBackStackEntryCount();
        FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(backStackCount-1);
        String fragmentTag = backStackEntry.getName();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);

        if(fragment instanceof ChargeFragment){
            presstime = 0;
            onNavItemHome();
        }
        else{
            if(backStackCount < 2){
                presstime++;
                if(presstime <2){
                    Toast.makeText(MainActivity.this,"Press back again to exit",Toast.LENGTH_SHORT).show();
                }else{
                    finish();
                }
            }
            else{
                presstime = 0;
                fragmentManager.beginTransaction()
                        .remove(fragment)
                        .commit();
                fragmentManager.popBackStack();
            }
        }
        //finish();
        backStackCount = fragmentManager.getBackStackEntryCount();
        if(backStackCount<3){
            homeSetActions(true);
            showHideActions("HomeFragment");
        }
//        if(backStackCount < 2){
//            presstime++;
//            if(presstime <2){
//                Toast.makeText(MainActivity.this,"Press back again to exit",Toast.LENGTH_SHORT).show();
//            }else{
//                finish();
//            }
//        }
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
    public void homeSetActions(boolean isHome){

        if(!isHome){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            backArrow.setVisibility(View.VISIBLE);
            viewCart.setVisibility(View.GONE);
            discountBtn.setVisibility(View.GONE);
            addUserBtn.setVisibility(View.GONE);

        }else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            backArrow.setVisibility(View.GONE);
            viewCart.setVisibility(View.VISIBLE);
            discountBtn.setVisibility(View.VISIBLE);
            addUserBtn.setVisibility(View.VISIBLE);
        }
    }
    private void showHideActions(String navPoint){
        switch (navPoint){
            case "HomeFragment":
                title.setText("");
                dateRange.setVisibility(View.GONE);
                homeSetActions(true);
                break;
            case "ProductsFragment":
                title.setText("Add Product");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                backArrow.setVisibility(View.GONE);
                viewCart.setVisibility(View.GONE);
                discountBtn.setVisibility(View.GONE);
                addUserBtn.setVisibility(View.GONE);
                dateRange.setVisibility(View.GONE);
                break;
            case "ReceiptFragment":
                title.setText("Past Receipts");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                backArrow.setVisibility(View.GONE);
                viewCart.setVisibility(View.GONE);
                discountBtn.setVisibility(View.GONE);
                addUserBtn.setVisibility(View.GONE);
                dateRange.setVisibility(View.VISIBLE);
                break;
        }
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
                if (id == R.id.nav_receipt) {
                    onNavItemReceipts();
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
//        fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//            .remove(new HomeFragment())
//            .commit();
//        fragmentManager.popBackStack();
//        fragmentManager.beginTransaction()
//                .remove(new ChargeFragment())
//                .commit();
//        fragmentManager.popBackStack();
//        fragmentManager.beginTransaction()
//                .remove(new CartFragment())
//                .commit();
//        fragmentManager.popBackStack();
        showHideActions(HomeFragment.TAG);
        homeSetActions(true);
        presstime = 0;
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, HomeFragment.class,null,HomeFragment.TAG)
                .setReorderingAllowed(true)
                .addToBackStack("HomeFragment")
                .commit();
    }
    public void onNavItemProducts(){
        showHideActions(ProductsFragment.TAG);
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, ProductsFragment.class,null,ProductsFragment.TAG)
                .setReorderingAllowed(true)
                .addToBackStack("ProductsFragment")
                .commit();
    }
    private void onNavItemReceipts() {
        showHideActions(ReceiptFragment.TAG);
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, ReceiptFragment.class,null,ReceiptFragment.TAG)
                .setReorderingAllowed(true)
                .addToBackStack("ReceiptFragment")
                .commit();
    }

}

