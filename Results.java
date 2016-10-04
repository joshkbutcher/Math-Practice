package com.jkb.mathpractice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Results extends AppCompatActivity {

    RelativeLayout resultRL;
    RelativeLayout gradeRL;
    TextView recentGradesTV;
    TextView scoreTextView;
    TextView correctAnsTV;
    TextView wrongAnsTV;
    TextView gradeTextView;
    TextView encourageTV;
    TextView allGrades;
    Button nextLevel;
    int score, wrong, numberOfQuestions, level;
    String pemdas = "", totalString, recentGradeMessage = "", formattedDate, levelString, scoreString, totalQuestString, allGradesMessage = "", timer = "";
    Double totalScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //resultRelativeLayout
        scoreTextView = (TextView)findViewById(R.id.scoreTextView);
        correctAnsTV = (TextView)findViewById(R.id.correctAnsTV);
        wrongAnsTV = (TextView)findViewById(R.id.wrongAnsTV);
        gradeTextView = (TextView)findViewById(R.id.gradeTextView);
        encourageTV = (TextView)findViewById(R.id.encourageTV);
        recentGradesTV = (TextView)findViewById(R.id.recentGrades);
        allGrades = (TextView)findViewById(R.id.allGradesView);
        nextLevel = (Button)findViewById(R.id.playHarder);
        resultRL = (RelativeLayout)findViewById(R.id.resultView);
        gradeRL = (RelativeLayout)findViewById(R.id.gradeView);

        Intent i = getIntent();
        score = i.getIntExtra("correctAns", -1);
        numberOfQuestions = i.getIntExtra("totalQuestions", -1);
        level = i.getIntExtra("levelNumber", -1);
        totalScore = i.getDoubleExtra("totalScore", -1);
        if (totalScore >= 80) {
            level--;
        }
        levelString = String.valueOf(level);

        pemdas = i.getStringExtra("orderOfOperations");
        totalString = String.format("%.0f", totalScore) + "%";
        scoreString = String.valueOf(score);
        totalQuestString = String.valueOf(numberOfQuestions);
        timer = i.getStringExtra("time");

        wrong = numberOfQuestions - score;



        Calendar date = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
        formattedDate = df.format(date.getTime());

        /** Calendar date = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd");
        formattedDate = df.format(date.getTime());**/

        correctAnsTV.setText("Correct Answers: " + score);
        wrongAnsTV.setText("Wrong Answers: " + wrong);

        if (totalScore >= 90) {
            gradeTextView.setText("Grade: A / " + totalString);
            encourageTV.setText("Awesome Job!");
            nextLevel.setEnabled(true);
        } else if (totalScore >= 80) {
            gradeTextView.setText("Grade: B / " + totalString);
            encourageTV.setText("Great Job!");
            nextLevel.setEnabled(true);
        } else if (totalScore >= 70) {
            gradeTextView.setText("Grade: C / " + totalString);
            encourageTV.setText("You can do better.");
            nextLevel.setEnabled(false);
        } else if (totalScore >= 60) {
            gradeTextView.setText("Grade: D / " + totalString);
            encourageTV.setText("Did you even try?");
            nextLevel.setEnabled(false);
        } else {
            gradeTextView.setText("Grade: F / " + totalString);
            encourageTV.setText("No more mathing for you.");
            nextLevel.setEnabled(false);
        }


        try {

            SQLiteDatabase miniTopGrades = this.openOrCreateDatabase("Mini Top Grades", MODE_PRIVATE, null);
            miniTopGrades.execSQL("CREATE TABLE IF NOT EXISTS miniGrades (pemdas VARCHAR, grade VARCHAR, level VARCHAR, timer VARCHAR)");
            miniTopGrades.execSQL("INSERT INTO miniGrades (pemdas, grade, level, timer) VALUES ('"+pemdas+"', '"+totalString+"', '"+levelString+"', '"+timer+"')");
            Cursor c = miniTopGrades.rawQuery("SELECT * FROM miniGrades", null);

            int pemdasIndex = c.getColumnIndex("pemdas");
            int gradeIndex = c.getColumnIndex("grade");
            int levelIndex = c.getColumnIndex("level");
            int timerIndex = c.getColumnIndex("timer");

            c.moveToLast();
            for (int p = 0; p < 5; p++) {
                recentGradeMessage += c.getString(pemdasIndex) + " - " + "Lvl: " + c.getString(levelIndex) + " - " + c.getString(gradeIndex) + " - " + c.getString(timerIndex) +  "\r\n";
                recentGradesTV.setText(recentGradeMessage);
                c.moveToPrevious();
            }

            SQLiteDatabase allTopGrades = this.openOrCreateDatabase("All Top Grades", MODE_PRIVATE, null);
            allTopGrades.execSQL("CREATE TABLE IF NOT EXISTS allGrade (pemdas VARCHAR, grade VARCHAR, level VARCHAR, score VARCHAR, totalQuestions VARCHAR, date VARCHAR)");
            allTopGrades.execSQL("INSERT INTO allGrade (pemdas, grade, level, score, totalQuestions, date) VALUES ('"+pemdas+"', '"+totalString+"', '"+levelString+"', '"+scoreString+"', '"+totalQuestString+"', '"+formattedDate+"' )");
            Cursor d = allTopGrades.rawQuery("SELECT * FROM allGrade", null);

            int pemdasIndex2 = d.getColumnIndex("pemdas");
            int gradeIndex2 = d.getColumnIndex("grade");
            int levelIndex2 = d.getColumnIndex("level");
            int scoreIndex2 = d.getColumnIndex("score");
            int totQuestIndex2 = d.getColumnIndex("totalQuestions");
            int dateIndex2 = d.getColumnIndex("date");

            d.moveToLast();
            for (int p = 0; p < allTopGrades.getPageSize(); p++) {
                allGradesMessage +=p+1 + ". " + d.getString(pemdasIndex2) + " - " + "Lvl: " + d.getString(levelIndex2) + " - " + d.getString(gradeIndex2) + " - " + d.getString(scoreIndex2) + "/" + d.getString(totQuestIndex2) + " - " + d.getString(dateIndex2) + "\r\n";
                allGrades.setText(allGradesMessage);
                d.moveToPrevious();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void goHome(View v) {
        Intent goHome = new Intent(Results.this, MainActivity.class);
        startActivity(goHome);
        finish();
    }

    public void playHarder(View view) {
        if (pemdas.equals("Addition")) {
            Intent backToPast = new Intent(Results.this, Addition.class);
            backToPast.putExtra("doubleTime", level+1);
            startActivity(backToPast);
            finish();
        } else if (pemdas.equals("Subtraction")) {
            Intent backToPast = new Intent(Results.this, Subtraction.class);
            backToPast.putExtra("doubleTime", level+1);
            startActivity(backToPast);
            finish();
        } else if (pemdas.equals("Multiplication")) {
            Intent backToPast = new Intent(Results.this, Multiplication.class);
            backToPast.putExtra("doubleTime", level+1);
            startActivity(backToPast);
            finish();
        } else if (pemdas.equals("Division")) {
            Intent backToPast = new Intent(Results.this, Division.class);
            backToPast.putExtra("doubleTime", level+1);
            startActivity(backToPast);
            finish();
        }
    }

    public void playAgain(View view) {
        if (totalScore >= 80) {
            if (pemdas.equals("Addition")) {
                Intent playAgain2 = new Intent(getApplicationContext(), Addition.class);
                playAgain2.putExtra("doubleTime", level);
                startActivity(playAgain2);
                finish();
            } else if (pemdas.equals("Subtraction")) {
                Intent playAgain3 = new Intent(getApplicationContext(), Subtraction.class);
                playAgain3.putExtra("doubleTime", level);
                startActivity(playAgain3);
                finish();
            } else if (pemdas.equals("Multiplication")) {
                Intent playAgain4 = new Intent(getApplicationContext(), Multiplication.class);
                playAgain4.putExtra("doubleTime", level);
                startActivity(playAgain4);
                finish();
            } else if (pemdas.equals("Division")){
                Intent playAgain5 = new Intent(getApplicationContext(), Division.class);
                playAgain5.putExtra("doubleTime", level);
                startActivity(playAgain5);
                finish();
            }
        } else {
            if (pemdas.equals("Addition")) {
                Intent playAgain2 = new Intent(getApplicationContext(), Addition.class);
                playAgain2.putExtra("doubleTime", level);
                startActivity(playAgain2);
                finish();
            } else if (pemdas.equals("Subtraction")) {
                Intent playAgain3 = new Intent(getApplicationContext(), Subtraction.class);
                playAgain3.putExtra("doubleTime", level);
                startActivity(playAgain3);
                finish();
            } else if (pemdas.equals("Multiplication")) {
                Intent playAgain4 = new Intent(getApplicationContext(), Multiplication.class);
                playAgain4.putExtra("doubleTime", level);
                startActivity(playAgain4);
                finish();
            } else if (pemdas.equals("Division")){
                Intent playAgain5 = new Intent(getApplicationContext(), Division.class);
                playAgain5.putExtra("doubleTime", level);
                startActivity(playAgain5);
                finish();
            }
        }

    }

    public void getGrades(View view) {
        resultRL.setVisibility(RelativeLayout.INVISIBLE);
        gradeRL.setVisibility(RelativeLayout.VISIBLE);
    }

    public void closeGrades(View view) {
        gradeRL.setVisibility(RelativeLayout.INVISIBLE);
        resultRL.setVisibility(RelativeLayout.VISIBLE);
    }
}