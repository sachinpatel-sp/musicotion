package com.excavanger.musicotion.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.excavanger.musicotion.R;
import com.excavanger.musicotion.utils.Common;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("my_preferences",MODE_PRIVATE);
                if(!isNetworkAvailable()){
                    Toast.makeText(MainActivity.this,"Please Connect To Internet",Toast.LENGTH_LONG).show();
                }else {
                    if(!preferences.getBoolean("onboarding_complete", false)){
                        Intent lang = new Intent(MainActivity.this,
                                LanguageActivity.class);
                        startActivity(lang);
                    }else {
                        Common.setLanguage(preferences.getString("lang","hindi"));
                        Common.setQuality(preferences.getString("quality","128"));
                        Intent home = new Intent(MainActivity.this,
                                HomeActivity.class);
                        startActivity(home);
                    }
                }
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}