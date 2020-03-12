package com.vvdev.colorpicker.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vvdev.colorpicker.R;
import com.vvdev.colorpicker.services.CirclePickerService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.vvdev.colorpicker.activity.CirclePickerActivityStart.isCirclePickerActivityRunning;
import static com.vvdev.colorpicker.activity.CirclePickerActivityStart.wmCirclePickerView;

public class MainActivity extends AppCompatActivity {

    public static boolean isCPRunning = false; // is circle picker running

    private ImageView startCirclePickerI;
    private CircleImageView startCirclePickerB;


    public static int appNavigationBarHeight =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Thread.currentThread().getStackTrace();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        startCirclePickerB = findViewById(R.id.backgroundPipette);
        startCirclePickerI = findViewById(R.id.pipette);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_import, R.id.navigation_palette).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        startCirclePickerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCPRunning){ // check if circle picker is not running. If not, we start it
                    startCirclePickerService();
                }
            }
        });

        startCirclePickerI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCPRunning){ // check if circle picker is not running. If not, we start it
                    startCirclePickerService();
                }else if(isCPRunning&&wmCirclePickerView==null&&!isCirclePickerActivityRunning){
                    isCPRunning=false;
                    startCirclePickerService();
                    Log.e("MainActivity","Bug detected, isCPRunning = true and isCirclePickerActivityRunning = false but no circle view attached.\nisCPRunning have been set to false and startCirPickerService have been started");
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView navView = findViewById(R.id.nav_view); // used in CirclePickerView
        appNavigationBarHeight=navView.getHeight(); // used in CirclePickerView
    }

    private void startCirclePickerService(){
        Intent CirclePickerServiceIntent = new Intent(this, CirclePickerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(CirclePickerServiceIntent);
        }else{
            startService(CirclePickerServiceIntent);
        }
    }
}
