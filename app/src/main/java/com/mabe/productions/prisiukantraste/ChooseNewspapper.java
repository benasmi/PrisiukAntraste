package com.mabe.productions.prisiukantraste;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;


public class ChooseNewspapper extends AppCompatActivity {

    private ImageButton min15;
    private ImageButton alfa;
    private ImageButton lrytas;
    private ImageButton delfi;
    private TextView prisiuk_txt;
    private TextView antraste_txt;

    public static final int TYPE_15MIN = 0;
    public static final int TYPE_ALFA = 1;
    public static final int TYPE_LRYTAS = 2;
    public static final int TYPE_DELFI = 3;
    public static final int TYPE_NO_HISTORY= 4;

    private SharedPreferences userData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_newspapper);

        userData = getSharedPreferences("user_data", MODE_PRIVATE);


        min15 = (ImageButton) findViewById(R.id.min15_btn);
        alfa = (ImageButton) findViewById(R.id.alfa_btn);
        lrytas = (ImageButton) findViewById(R.id.lrytas_btn);
        delfi = (ImageButton) findViewById(R.id.delfi_btn);
        prisiuk_txt = (TextView) findViewById(R.id.prisiuk_txt);
        antraste_txt = (TextView) findViewById(R.id.antraste_txt);
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/amitaBold.ttf");
        prisiuk_txt.setTypeface(tf);
        antraste_txt.setTypeface(tf);

        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in_with_delay);

        min15.startAnimation(anim);
        alfa.startAnimation(anim);
        lrytas.startAnimation(anim);
        delfi.startAnimation(anim);

        min15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userData.edit().putInt("browsing_history",TYPE_15MIN ).commit();
                Intent i = new Intent(ChooseNewspapper.this, NewsFeedActivity.class);
                i.putExtra("type", TYPE_15MIN);
                startActivity(i);

            }
        });
        alfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userData.edit().putInt("browsing_history",TYPE_ALFA ).commit();
                Intent i = new Intent(ChooseNewspapper.this, NewsFeedActivity.class);
                i.putExtra("type", TYPE_ALFA);
                startActivity(i);

            }
        });
        lrytas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userData.edit().putInt("browsing_history",TYPE_LRYTAS ).commit();
                Intent i = new Intent(ChooseNewspapper.this, NewsFeedActivity.class);
                i.putExtra("type", TYPE_LRYTAS);
                startActivity(i);

            }
        });
        delfi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userData.edit().putInt("browsing_history",TYPE_DELFI ).commit();
                Intent i = new Intent(ChooseNewspapper.this, NewsFeedActivity.class);
                i.putExtra("type", TYPE_DELFI);
                startActivity(i);

            }
        });
    }
}
