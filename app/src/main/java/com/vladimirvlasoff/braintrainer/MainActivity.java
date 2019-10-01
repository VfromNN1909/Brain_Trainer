package com.vladimirvlasoff.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTask;
    private TextView textViewOpinion1;
    private TextView textViewOpinion2;
    private TextView textViewOpinion3;
    private TextView textViewOpinion4;
    private TextView textViewScore;
    private TextView textViewTimer;

    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private boolean isPositive;
    private int max = 30;
    private int min = 5;
    private int countOfQuestions = 0;
    private int countOfRightAnswers = 0;

    private boolean gameOver = false;

    ArrayList<TextView> options = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTask = findViewById(R.id.textViewTask);
        textViewOpinion1 = findViewById(R.id.textViewOpinion1);
        textViewOpinion2 = findViewById(R.id.textViewOpinion2);
        textViewOpinion3 = findViewById(R.id.textViewOpinion3);
        textViewOpinion4 = findViewById(R.id.textViewOpinion4);
        textViewScore = findViewById(R.id.textViewScore);
        textViewTimer = findViewById(R.id.textViewTimer);

        options.add(textViewOpinion1);
        options.add(textViewOpinion2);
        options.add(textViewOpinion3);
        options.add(textViewOpinion4);
        playNext();

        CountDownTimer timer = new CountDownTimer(6000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setText(getTime(millisUntilFinished));
                if (millisUntilFinished <= 10000){
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }

            }

            @Override
            public void onFinish() {
                gameOver = true;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int max = preferences.getInt("max",0);
                if (countOfRightAnswers >= max){
                    preferences.edit().putInt("max",countOfRightAnswers).apply();
                }
                String zero = "00:00";
                textViewTimer.setText(zero);
                Intent intent = new Intent(MainActivity.this,ScoreActivity.class);
                intent.putExtra("result",countOfRightAnswers);
                startActivity(intent);

            }
        };
        timer.start();
    }

    private void playNext(){
        generateQuestion();
        for (int i = 0;i < options.size();i++){
            if (i == rightAnswerPosition){
                options.get(i).setText(Integer.toString(rightAnswer));
            }else{
                options.get(i).setText(Integer.toString(generateWrongAnswer()));
            }
        }
        String score = String.format("%s / %s", countOfRightAnswers,countOfQuestions);
        textViewScore.setText(score);
    }

    private void generateQuestion(){
        int a = (int) (Math.random() * (max - min + 1) + min);
        int b = (int) (Math.random() * (max - min + 1) + min);
        int mark =(int)(Math.random() * 2);
        isPositive = mark == 1;
        if (isPositive){
            rightAnswer = a + b;
            question = String.format("%s + %s",a,b);
        } else{
            rightAnswer = a - b;
            question = String.format("%s - %s",a,b);
        }
        textViewTask.setText(question);
        rightAnswerPosition = (int)(Math.random() * 4);
    }
    private int generateWrongAnswer(){
        int result;
        do {
            result = (int)(Math.random() * max * 2 + 1) - (max - min);
        }while(result == rightAnswer);
        return result;
    }

    private String getTime(long millis){
        int seconds = (int)(millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
    }

    public void onClickAnswer(View view) {
        if (!gameOver) {
            TextView textView = (TextView) view;
            String answer = textView.getText().toString();
            int choosenAnswer = Integer.parseInt(answer);
            if (choosenAnswer == rightAnswer) {
                countOfRightAnswers++;
                Toast.makeText(this, "Верно!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Неверно!", Toast.LENGTH_SHORT).show();
            }
            countOfQuestions++;
            playNext();
        }
    }
}