package com.jkb.mathpractice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class Subtraction extends AppCompatActivity {

    int subLevel = 1;
    int currentLevel = 1;
    String pemdas;
    int counter = 0;

    Button startButton;
    TextView resultTextView;
    TextView pointsTextView;
    TextView levelTV;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    TextView sumTextView;
    TextView timerTextView;
    Button playAgainButton;
    RelativeLayout gameRelativeLayout;
    RelativeLayout topHead;
    RelativeLayout levelsRL;
    Typeface chalkFont;
    Bundle i;
    double timer = 0;
    String timerString = "";

    ArrayList<Integer> answers = new ArrayList<Integer>();
    int locationOfCorrectAnswer;
    int score = 0;
    int numberOfQuestions = 0;
    double total = 0;
    String totalString;
    SharedPreferences storage;
    SharedPreferences results;

    //Results View
    TextView recentGradesTV;
    TextView scoreTextView;
    TextView correctAnsTV;
    TextView wrongAnsTV;
    TextView gradeTextView;
    TextView encourageTV;
    Button nextLevel;

    //Level View
    GridView levelGridView;
    public String[] buttonIds = new String[50];
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);
        setContentView(R.layout.mainlayout);
        pemdas = "Subtraction";
        storage = this.getSharedPreferences("com.jkb.mathpractice", Context.MODE_PRIVATE);
        subLevel = storage.getInt("subLevel", subLevel);
        //subLevel = 4;
        levelGridView = (GridView)findViewById(R.id.levelGrid);
        levelGridView.setAdapter(new ButtonAdapter(this));
        results = this.getSharedPreferences("com.jkb.mathpractice", Context.MODE_PRIVATE);

        //Level View
        for (int i = 0; i < buttonIds.length; i++) {
            buttonIds[i] = String.valueOf(i+1);
        }

        startButton = (Button) findViewById(R.id.startButton);
        sumTextView = (TextView) findViewById(R.id.sumTextView);
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        pointsTextView = (TextView) findViewById(R.id.pointsTextView);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        levelTV = (TextView)findViewById(R.id.levelTextView);
        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        gameRelativeLayout = (RelativeLayout) findViewById(R.id.gameRelativeLayout);
        topHead = (RelativeLayout) findViewById(R.id.topHeader);
        levelsRL = (RelativeLayout)findViewById(R.id.levels);

        chalkFont = Typeface.createFromAsset(getAssets(), "fonts/EraserDust.ttf");
        // set chalkFont to everything
        sumTextView.setTypeface(chalkFont);
        button0.setTypeface(chalkFont);
        button1.setTypeface(chalkFont);
        button2.setTypeface(chalkFont);
        button3.setTypeface(chalkFont);
        resultTextView.setTypeface(chalkFont);
        startButton.setTypeface(chalkFont);

        //resultRelativeLayout
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        correctAnsTV = (TextView) findViewById(R.id.correctAnsTV);
        wrongAnsTV = (TextView) findViewById(R.id.wrongAnsTV);
        gradeTextView = (TextView) findViewById(R.id.gradeTextView);
        encourageTV = (TextView) findViewById(R.id.encourageTV);
        recentGradesTV = (TextView) findViewById(R.id.recentGrades);
        nextLevel = (Button) findViewById(R.id.playHarder);

        i = getIntent().getExtras();
        if (getIntent().hasExtra("doubleTime")) {
            currentLevel = i.getInt("doubleTime");
            levelsRL.setVisibility(View.GONE);
            startButton.setVisibility(RelativeLayout.VISIBLE);
            playAgain();
        }
    }
    //Test Buttons for levels
    public class ButtonAdapter extends BaseAdapter {
        private Context mContext;

        // Gets the context so it can be used later
        public ButtonAdapter(Context c) {
            mContext = c;
        }

        // Total number of things contained within the adapter
        public int getCount() {
            return buttonIds.length;
        }

        // Require for structure, not really used in my code.
        public Object getItem(int position) {
            return null;
        }

        // Require for structure, not really used in my code. Can
        // be used to get the id of an item in the adapter for
        // manual control.
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position,
                            View convertView, ViewGroup parent) {
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                btn = new Button(mContext);
                //btn.setLayoutParams(new GridView.LayoutParams(100, 55));
                btn.setPadding(5, 5, 5, 5);
            }
            else {
                btn = (Button) convertView;
            }
            btn.setText(buttonIds[position]);
            // filenames is an array of strings
            btn.setTextColor(Color.WHITE);
            btn.setBackgroundResource(R.drawable.cirlce);
            btn.setId(position);
            btn.setTag(position + 1);
            btn.setTextSize(24);
            btn.setAlpha(.3f);
            btn.setEnabled(false);
            btn.setOnClickListener(new MyOnClickListener(position));

            if (position < subLevel) {
                btn.setAlpha(1f);
                btn.setEnabled(true);
            }

            return btn;
        }
    }

    class MyOnClickListener implements View.OnClickListener
    {
        private final int position;

        public MyOnClickListener(int position)
        {
            this.position = position;
        }

        public void onClick(View v)
        {
            String holder = v.getTag().toString();
            currentLevel = Integer.parseInt(holder);
            levelsRL.setVisibility(RelativeLayout.GONE);
            startButton.setVisibility(View.VISIBLE);
        }
    }

    public void playAgain() {
        score = 0;
        numberOfQuestions = 0;
        timerTextView.setText(":00");
        pointsTextView.setText("20");
        resultTextView.setText("");
        levelTV.setText("Level " + currentLevel);
        generateQuestion();

        new CountDownTimer(300100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //double timer = millisUntilFinished / 1000;
                timer++;
                timerString = ":" + String.valueOf(String.format("%.0f", timer));
                timerTextView.setText(timerString);

                if (timer < 10) {
                    timerString = ":" + "0" + String.valueOf(String.format("%.0f", timer));
                    timerTextView.setText(timerString);
                }
            }
            @Override
            public void onFinish() {
                counter++;
                if (counter == 1) {
                    goToResults();
                }
            }
        }.start();
    }
    public void goToResults() {
        if (total >= 80) {
            currentLevel++;
        }
        if (currentLevel > subLevel) {
            subLevel = currentLevel;
            storage.edit().putInt("subLevel", subLevel).apply();
        }

        Intent goToResults = new Intent(Subtraction.this, Results.class);
        goToResults.putExtra("correctAns", score);
        goToResults.putExtra("totalQuestions", numberOfQuestions);
        goToResults.putExtra("levelNumber", currentLevel);
        goToResults.putExtra("orderOfOperations", pemdas);
        goToResults.putExtra("totalScore", total);
        goToResults.putExtra("time", timerString);
        startActivity(goToResults);
    }

    public void generateQuestion() {
        int a, b, range, wRange, firstW, secondW, big, little, bW, lW;
        Random rand = new Random();
        //Level ranges
        if (currentLevel > 20) {
            range = currentLevel * 5;
            a = rand.nextInt(range);
            b = rand.nextInt(range);
        } else if (currentLevel > 10) {
            range = currentLevel * 3;
            a = rand.nextInt(range);
            b = rand.nextInt(range);
        } else {
            range = currentLevel + 5;
            a = rand.nextInt(range);
            b = rand.nextInt(5);
        }
        if (b > a) {
            big = b;
            little = a;
        } else {
            big = a;
            little = b;
        }

        sumTextView.setText(Integer.toString(big) + " - " + Integer.toString(little));
        locationOfCorrectAnswer = rand.nextInt(4);
        answers.clear();
        int incorrectAnswer;

        for (int i=0; i<4; i++) {
            if (i == locationOfCorrectAnswer) {
                answers.add(big - little);
            } else {
                if (currentLevel > 20) {
                    wRange = currentLevel * 5;
                    firstW = rand.nextInt(wRange);
                    secondW = rand.nextInt(wRange);
                } else if (currentLevel > 10) {
                    wRange = currentLevel * 3;
                    firstW = rand.nextInt(wRange);
                    secondW = rand.nextInt(wRange);
                } else {
                    wRange = currentLevel + 5;
                    firstW = rand.nextInt(wRange);
                    secondW = rand.nextInt(5);
                }
                if (secondW > firstW) {
                    bW = secondW;
                    lW = firstW;
                } else {
                    bW = firstW;
                    lW = secondW;
                }

                incorrectAnswer = Math.abs(rand.nextInt(bW - lW));
                while (incorrectAnswer == big - little && incorrectAnswer < 0) {
                    incorrectAnswer = rand.nextInt(bW - lW);
                }
                answers.add(incorrectAnswer);
            }
        }
        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));
    }

    public void chooseAnswer(View view) {

        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))) {
            score++;

            new CountDownTimer(1100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    topHead.setBackgroundColor(Color.GREEN);
                    resultTextView.setText("Correct!");
                }
                @Override
                public void onFinish() {
                    topHead.setBackgroundColor(Color.LTGRAY);
                    resultTextView.setText("");
                }
            }.start();
        } else {
            new CountDownTimer(1100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    topHead.setBackgroundColor(Color.RED);
                    resultTextView.setText("Wrong!");
                }
                @Override
                public void onFinish() {
                    topHead.setBackgroundColor(Color.LTGRAY);
                    resultTextView.setText("");
                }
            }.start();
        }
        numberOfQuestions++;
        total = Math.round((double)score / (double)numberOfQuestions * 100);
        totalString = String.format("%.0f", total) + "%";
        //pointsTextView.setText(totalString);
        pointsTextView.setText(String.valueOf(20 - score));
        if (score == 20) {
            goToResults();
        }
        generateQuestion();
    }

    public void start(View view) {
        startButton.setVisibility(View.GONE);
        gameRelativeLayout.setVisibility(RelativeLayout.VISIBLE);
        playAgain();
    }

    /** @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    //getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
    }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
     // Handle action bar item clicks here. The action bar will
     // automatically handle clicks on the Home/Up button, so long
     // as you specify a parent activity in AndroidManifest.xml.
     int id = item.getItemId();

     //noinspection SimplifiableIfStatement
     if (id == R.id.action_settings) {
     return true;
     }

     return super.onOptionsItemSelected(item);
     }**/
}