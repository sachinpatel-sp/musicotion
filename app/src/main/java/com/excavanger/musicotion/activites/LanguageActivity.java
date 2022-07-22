package com.excavanger.musicotion.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.excavanger.musicotion.R;
import com.excavanger.musicotion.utils.Common;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanguageActivity extends AppCompatActivity {
    private Button doneBtn;
    private ChipGroup languageGroup;
    private List<String> language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        doneBtn = findViewById(R.id.done_btn);
        languageGroup = findViewById(R.id.chips_group);
        language = Arrays.asList("g","bhojpuri","english","hindi","punjabi","tamil","telugu","marathi","gujarati","bengali","kannada","malayalam","haryanvi","rajasthani","odia","assamese");

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("my_preferences",MODE_PRIVATE);
                List<Integer> list = languageGroup.getCheckedChipIds();
                if(list.size() == 0){
                    Toast.makeText(getApplicationContext(),"Please Select at least 1 Language!",Toast.LENGTH_SHORT).show();
                }else {
                    String st = "";
                    for (int i = 0; i < list.size() - 1; i++) {
                        st += language.get(list.get(i));
                        st += ",";
                    }
                    st += language.get(list.get(list.size() - 1));
                    preferences.edit().putBoolean("onboarding_complete",true).apply();
                    preferences.edit().putString("lang",st).apply();
                    preferences.edit().putString("quality","128").apply();
                    Common.setLanguage(st);
                    Common.quality = "128";
                    Intent home = new Intent(LanguageActivity.this,HomeActivity.class);
                    startActivity(home);
                    finish();
                }
            }
        });
    }
}