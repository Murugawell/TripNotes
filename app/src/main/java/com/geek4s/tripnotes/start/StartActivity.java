package com.geek4s.tripnotes.start;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geek4s.tripnotes.MainActivity;
import com.geek4s.tripnotes.R;

/**
 * Created by Murugavel on 1/9/2018.
 */

public class StartActivity extends AppCompatActivity {
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        TextView startText1 = (TextView) findViewById(R.id.startText1);
        TextView startText2 = (TextView) findViewById(R.id.startText2);
        final ImageView imagecar = (ImageView) findViewById(R.id.imageviewcar);
        final ImageView imagetrain = (ImageView) findViewById(R.id.imageviewtrain);
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonti.ttf");
        startText1.setTypeface(custom_font1);
        startText1.setTextSize(120f);
        startText1.setText("T");

        startText2.setTypeface(custom_font1);
        startText2.setText("rip\nNotes");
        startText2.setTextSize(40f);


        final TranslateAnimation translateAnimation = new TranslateAnimation(0f, 30f, 0f, 0f);
        translateAnimation.setRepeatCount(3);
        translateAnimation.setDuration(700);
        translateAnimation.setRepeatMode(2);


        final TranslateAnimation translateAnimation2 = new TranslateAnimation(0f, getWindowManager().getDefaultDisplay().getWidth() + 100, 0f, 0f);
        translateAnimation2.setDuration(1000);
        translateAnimation2.setRepeatMode(2);
        translateAnimation2.setFillAfter(true);

        final TranslateAnimation translateAnimation3 = new TranslateAnimation(0f, 0f, 0f, getWindowManager().getDefaultDisplay().getHeight() / 2);
        translateAnimation3.setDuration(1000);
        translateAnimation3.setRepeatMode(2);
        translateAnimation3.setFillAfter(true);


        final Animation zoom = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom);
        zoom.setFillAfter(true);
        startText1.setAnimation(translateAnimation);
        startText2.setAnimation(translateAnimation);
        translateAnimation.start();


        final AnimationSet s = new AnimationSet(false);//false means don't share interpolators

//        startText1.animate().translationX(100).setDuration(1000).start();
//        startText2.animate().translationY(100).setDuration(1000).start();

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imagecar.setAnimation(translateAnimation2);
                imagetrain.setAnimation(translateAnimation3);

                s.addAnimation(translateAnimation3);
                s.addAnimation(zoom);
                s.setFillAfter(true);
                imagetrain.setAnimation(s);
                translateAnimation2.start();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        s.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                finish();
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
}
