package com.mabe.productions.prisiukantraste;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class StartingActivity extends AwesomeSplash {



    @Override
    public void initSplash(ConfigSplash configSplash) {



        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        configSplash.setBackgroundColor(R.color.splashBackground); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1500); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP


        configSplash.setLogoSplash(R.drawable.splash_screen_icon); //or any other drawable
        configSplash.setAnimLogoSplashDuration(500); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: h

        configSplash.setTitleSplash("Prisiūk antraštę");
        configSplash.setTitleTextColor(R.color.text_color);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(500);
        configSplash.setAnimTitleTechnique(Techniques.SlideInUp);
        configSplash.setTitleFont("fonts/amitaRegular.ttf"); //provide string to your font located in assets
    }

    @Override
    public void animationsFinished() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);

                boolean first_time = sharedPreferences.getBoolean("first_time",true);

                if(first_time){

                    startActivity(new Intent(StartingActivity.this, IntroActivity.class));
                    sharedPreferences.edit().putBoolean("first_time", false).commit();

                }else {

                    int browsing_history = sharedPreferences.getInt("browsing_history", ChooseNewspapper.TYPE_NO_HISTORY);

                    switch (browsing_history) {


                        case ChooseNewspapper.TYPE_15MIN:
                            Intent mins15 = new Intent(StartingActivity.this, NewsFeedActivity.class);
                            mins15.putExtra("type", ChooseNewspapper.TYPE_15MIN);
                            mins15.putExtra("sender", "StartingActivity");
                            startActivity(mins15);
                            break;
                        case ChooseNewspapper.TYPE_ALFA:
                            Intent alfa = new Intent(StartingActivity.this, NewsFeedActivity.class);
                            alfa.putExtra("type", ChooseNewspapper.TYPE_ALFA);
                            alfa.putExtra("sender", "StartingActivity");
                            startActivity(alfa);
                            break;

                        case ChooseNewspapper.TYPE_LRYTAS:

                            Intent lrytas = new Intent(StartingActivity.this, NewsFeedActivity.class);
                            lrytas.putExtra("type", ChooseNewspapper.TYPE_LRYTAS);
                            lrytas.putExtra("sender", "StartingActivity");
                            startActivity(lrytas);
                            break;
                        case ChooseNewspapper.TYPE_DELFI:
                            Intent delfi = new Intent(StartingActivity.this, NewsFeedActivity.class);
                            delfi.putExtra("type", ChooseNewspapper.TYPE_DELFI);
                            delfi.putExtra("sender", "StartingActivity");
                            startActivity(delfi);
                            break;

                        case ChooseNewspapper.TYPE_NO_HISTORY:
                            startActivity(new Intent(StartingActivity.this, ChooseNewspapper.class));
                            break;

                    }

                }


            }
        }, 1000);

    }
}
