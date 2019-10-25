package com.example.myshape;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LinearLayout mScreen;
    TextView tr;
    TextView tg;
    TextView tb;
    TextView ta;
    SeekBar sbA;
    SeekBar sbR;
    SeekBar sbG;
    SeekBar sbB;
    int red = 255;
    int green = 255;
    int blue = 255;
    int alfa = 255;
    String textRed = "R: " + red;
    String textGreen = "G: " + green;
    String textBlue = "B: " + blue;
    String textAlfa = "A: " + alfa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScreen = (LinearLayout) findViewById(R.id.llCerchio);
        tr = findViewById(R.id.textView);
        tg = findViewById(R.id.textView2);
        tb = findViewById(R.id.textView3);
        ta = findViewById(R.id.textView4);

        tr.setText(textRed);
        tg.setText(textGreen);
        tb.setText(textBlue);
        ta.setText(textAlfa);
        sbA = (SeekBar) findViewById(R.id.seekBarA);
        sbR = (SeekBar) findViewById(R.id.seekBarR);
        sbG = (SeekBar) findViewById(R.id.seekBarG);
        sbB = (SeekBar) findViewById(R.id.seekBarB);
        sbA.setOnSeekBarChangeListener(seekBarChangeListenerA);
        sbR.setOnSeekBarChangeListener(seekBarChangeListenerR);
        sbG.setOnSeekBarChangeListener(seekBarChangeListenerG);
        sbB.setOnSeekBarChangeListener(seekBarChangeListenerB);
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListenerA
            = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {

            GradientDrawable shapeDrawable = (GradientDrawable)mScreen.getBackground();
            shapeDrawable.setAlpha(progress);
            alfa = progress;
            textAlfa = "A: " + alfa;
            ta.setText(textAlfa);

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
     };

    private SeekBar.OnSeekBarChangeListener seekBarChangeListenerR
            = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {

            String hex = "#";
            if(progress > 15) hex += Integer.toHexString(progress);
            else hex += "0" + Integer.toHexString(progress);
            if(green > 15) hex += Integer.toHexString(green);
            else hex += "0" + Integer.toHexString(green);
            if(blue > 15) hex += Integer.toHexString(blue);
            else hex += "0" + Integer.toHexString(blue);

            red = progress;
            textRed = "R: " + red;
            tr.setText(textRed);
            mScreen.getBackground().setColorFilter(Color.parseColor(hex), PorterDuff.Mode.SRC_ATOP);


        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarChangeListenerG
            = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {

            String hex = "#";
            if(red > 15) hex += Integer.toHexString(red);
            else hex += "0" + Integer.toHexString(red);
            if(progress > 15) hex += Integer.toHexString(progress);
            else hex += "0" + Integer.toHexString(progress);
            if(blue > 15) hex += Integer.toHexString(blue);
            else hex += "0" + Integer.toHexString(blue);

            green = progress;
            textGreen = "G: " + green;
            tg.setText(textGreen);
            mScreen.getBackground().setColorFilter(Color.parseColor(hex), PorterDuff.Mode.SRC_ATOP);

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarChangeListenerB
            = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {

            String hex = "#";
            if(red > 15) hex += Integer.toHexString(red);
            else hex += "0" + Integer.toHexString(red);
            if(green > 15) hex += Integer.toHexString(green);
            else hex += "0" + Integer.toHexString(green);
            if(progress > 15) hex += Integer.toHexString(progress);
            else hex += "0" + Integer.toHexString(progress);

            blue = progress;
            textBlue = "B: " + blue;
            tb.setText(textBlue);
            mScreen.getBackground().setColorFilter(Color.parseColor(hex), PorterDuff.Mode.SRC_ATOP);

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };
}
