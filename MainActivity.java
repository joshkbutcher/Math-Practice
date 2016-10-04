package com.jkb.mathpractice;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Typeface chalkFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);

        button0.setText(Html.fromHtml("&#43;"));
        button1.setText(Html.fromHtml("&#45;"));
        button2.setText(Html.fromHtml("&times;"));
        button3.setText(Html.fromHtml("&#0247;"));
        button4.setText(Html.fromHtml("&#43;" + " / " + "&#45;"));
        button5.setText(Html.fromHtml("&times;" + " / " + "&#0247;"));
        button6.setText(Html.fromHtml("Random"));


        chalkFont = Typeface.createFromAsset(getAssets(), "fonts/EraserDust.ttf");
        // set chalkFont to everything

        /**button0.setTypeface(chalkFont);
        button1.setTypeface(chalkFont);
        button2.setTypeface(chalkFont);
        button3.setTypeface(chalkFont);**/
    }

    public void add(View v) {
        Intent add = new Intent(MainActivity.this, Addition.class);
        //goToResults.putExtra("totalScore", total);
        startActivity(add);
    }

    public void minus(View v) {
        Intent minus = new Intent(MainActivity.this, Subtraction.class);
        //goToResults.putExtra("totalScore", total);
        startActivity(minus);
    }
    public void multiply(View v) {
        Intent multiply = new Intent(MainActivity.this, Multiplication.class);
        //goToResults.putExtra("totalScore", total);
        startActivity(multiply);
    }

    public void divide(View v) {
        Intent divide = new Intent(MainActivity.this, Division.class);
        //goToResults.putExtra("totalScore", total);
        startActivity(divide);
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