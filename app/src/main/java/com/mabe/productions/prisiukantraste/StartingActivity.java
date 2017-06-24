package com.mabe.productions.prisiukantraste;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

public class StartingActivity extends AppCompatActivity {

    private ImageView top_patch;
    private ImageView bottom_patch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        FirebaseApp.initializeApp(this);
        Log.i("TEST", "ID: " + FirebaseInstanceId.getInstance().getToken());


        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        top_patch = (ImageView) findViewById(R.id.top_patch);
        bottom_patch = (ImageView) findViewById(R.id.bottom_patch);

        Animation bottom_patch_from_right = AnimationUtils.loadAnimation(this, R.anim.top_patch_from_left);
        Animation top_patch_from_left = AnimationUtils.loadAnimation(this, R.anim.bottom_patch_right);
        final Animation bottom_patch_go_down = AnimationUtils.loadAnimation(StartingActivity.this, R.anim.bottom_patch_go_down);
        final Animation top_patch_go_up = AnimationUtils.loadAnimation(StartingActivity.this, R.anim.top_patch_go_up);

        bottom_patch_from_right.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                bottom_patch.startAnimation(bottom_patch_go_down);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        top_patch_from_left.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                top_patch.startAnimation(top_patch_go_up);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        bottom_patch_go_down.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent i = new Intent(StartingActivity.this, ChooseNewspapper.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in_no_delay, R.anim.fade_in_no_delay);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        top_patch.startAnimation(top_patch_from_left);
        bottom_patch.startAnimation(bottom_patch_from_right);




    }
}
