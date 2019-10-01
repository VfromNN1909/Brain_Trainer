package com.vladimirvlasoff.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private TextView textViewResult;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings){
            Intent settingsIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        textViewResult = findViewById(R.id.textViewResult);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("result")){
            int result = intent.getIntExtra("result",0);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int max = preferences.getInt("max",0);
            String score = String.format("Ваш результат: %s\nМаксимальный результат : %s",result,max);
            textViewResult.setText(score);
        }
    }

    public void onClickStartNewGame(View view) {
        Intent start = new Intent(this,MainActivity.class);
        startActivity(start);
    }
}
