package com.mubita.foodorderapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.mubita.foodorderapp.adapters.PagerAdapter;
import com.mubita.foodorderapp.fragments.DrinksFragment;
import com.mubita.foodorderapp.fragments.FoodFragment;
import com.mubita.foodorderapp.models.Notification;
import com.mubita.foodorderapp.utilities.DataSync;
import com.mubita.foodorderapp.viewmodel.NotificationViewModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    // **
    private int notifCount;
    final Timer timer = new Timer();
    DataSync dataSync = new DataSync();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // **
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        // **
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        // Adding Fragments
        adapter.AddFragment(new FoodFragment(), "Food");
        adapter.AddFragment(new DrinksFragment(), "Drinks");
        // Adapter Setup0
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        appDatabaseSync(MainActivity.this);

        getUnreadNotificationCount();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_cart);
        menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this, AppDataStore.getInstance().getProductList().size(), R.drawable.ic_shopping_cart_white_24dp));

        MenuItem menuItem1 = menu.findItem(R.id.nav_notifications);
        menuItem1.setIcon(Converter.convertLayoutToImage(MainActivity.this, notifCount, R.drawable.ic_notifications_white_24dp));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            startActivity(new Intent(MainActivity.this,CartActivity.class));
            return true;
        } else if (id == R.id.nav_notifications) {
            startActivity(new Intent(MainActivity.this,NotificationsActivity.class));
            return true;
        }
        else if (id == R.id.nav_myorders) {
            startActivity(new Intent(MainActivity.this, OrdersActivity.class));
            return true;
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            return true;
        }
        else if (id == R.id.action_logout) {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_food_menu){
            // **
        } else if (id == R.id.nav_cart) {
            startActivity(new Intent(MainActivity.this,CartActivity.class));
        } else if (id == R.id.nav_share) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareSub = "Sharing is caring";
            String shareBody = "https://play.google.com/store/apps/details?id=com.foodorderapp";
            myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
            myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
            startActivity(Intent.createChooser(myIntent, "Share using:"));
        } else if (id == R.id.nav_feedback) {
            startActivity(new Intent(MainActivity.this,FeedbackActivity.class));
        }

        return true;
    }

    public void appDatabaseSync(final Context context){
        //invalidateOptionsMenu();
        if(AppDataStore.getInstance().isRunning() == false){
            // Set the schedule function and rate
            timer.scheduleAtFixedRate(new TimerTask() {
                                          @Override
                                          public void run() {
                                              AppDataStore.getInstance().setRunning(true);
                                              // Called each time when 5000 milliseconds (1 second) (the period parameter)
                                              System.out.println("Syncing Started!");

                                              dataSync.getNotifications(context);
                                              dataSync.getOrders(context);
                                              // **
                                              invalidateOptionsMenu();
                                          }
                                      },
                    // Set how long before to start calling the TimerTask (in milliseconds)
                    0,
                    // Set the amount of time between each execution (in milliseconds)
                    3000);
        }

    }

    public void getUnreadNotificationCount(){

        NotificationViewModel notificationViewModel;

        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        notificationViewModel.getUnreadNotifications().observe(this, new Observer<List<Notification>>() {
            @Override
            public void onChanged(@Nullable List<Notification> notifications1) {
                notifCount = notifications1.size();
            }
        });

    }

}
