package com.mabe.productions.prisiukantraste;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {


    private ImageView back_arrow;
    private TextView settings_txt;
    private TextView pranesimai;
    private TextView get_msg_from_15min;
    private TextView get_msg_from_alfa;
    private TextView get_msg_from_delfi;
    private TextView get_msg_from_lrytas;

    //Notification switchers
    private SwitchCompat min15_switch;
    private SwitchCompat delfi_switch;
    private SwitchCompat alfa_switch;
    private SwitchCompat lrytas_switch;

    //Open with switchers
    private SwitchCompat open_with;

    private SharedPreferences sharedPreferences;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("user_data",MODE_PRIVATE);


        CheckingUtils.changeNotifBarColor("#FFFFFF", getWindow());
        CheckingUtils.changeIconsColor(getWindow());
        Toolbar myToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#000000"));

        Typeface tfBold = Typeface.createFromAsset(getAssets(),
                "fonts/openSansBold.ttf");

        Typeface tfLight = Typeface.createFromAsset(getAssets(),
                "fonts/openSans.ttf");

        Animation arrow = AnimationUtils.loadAnimation(this, R.anim.top_down_hamburger);
        Animation options = AnimationUtils.loadAnimation(this, R.anim.top_down_title);

        settings_txt = (TextView) myToolbar.findViewById(R.id.settings_txt);
        pranesimai = (TextView)findViewById(R.id.pranesimai);

        get_msg_from_15min = (TextView) findViewById(R.id.getMessages_from15min);
        get_msg_from_alfa = (TextView) findViewById(R.id.getMessages_fromalfa);
        get_msg_from_delfi = (TextView)findViewById(R.id.getMessages_fromDelfi);
        get_msg_from_lrytas = (TextView) findViewById(R.id.getMessages_fromLRYTAS);

        min15_switch = (SwitchCompat) findViewById(R.id.min15_switch);
        delfi_switch = (SwitchCompat) findViewById(R.id.delfi_switch);
        alfa_switch = (SwitchCompat) findViewById(R.id.alfa_switch);
        lrytas_switch = (SwitchCompat) findViewById(R.id.lrytas_switch);

        open_with = (SwitchCompat) findViewById(R.id.open_with_switch);



        settings_txt.setTypeface(tfBold);
        pranesimai.setTypeface(tfBold);
        get_msg_from_15min.setTypeface(tfLight);
        get_msg_from_alfa.setTypeface(tfLight);
        get_msg_from_delfi.setTypeface(tfLight);
        get_msg_from_lrytas.setTypeface(tfLight);


        back_arrow = (ImageView) myToolbar.findViewById(R.id.back_icon);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TEST", "triggerd vegan");
                SettingsActivity.this.finish();
            }
        });


        back_arrow.startAnimation(arrow);
        settings_txt.startAnimation(options);

        //Switchers
        min15_switch.setChecked(sharedPreferences.getBoolean("min15_state", true));
        delfi_switch.setChecked(sharedPreferences.getBoolean("delfi_state", true));
        alfa_switch.setChecked(sharedPreferences.getBoolean("alfa_state", true));
        lrytas_switch.setChecked(sharedPreferences.getBoolean("lrytas_state", true));
        open_with.setChecked(sharedPreferences.getBoolean("lrytas_state", false));


        min15_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sharedPreferences.edit().putBoolean("min15_state", b).commit();
            }
        });

        delfi_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sharedPreferences.edit().putBoolean("delfi_state", b).commit();
            }
        });

        alfa_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sharedPreferences.edit().putBoolean("alfa_state", b).commit();
            }
        });

        lrytas_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sharedPreferences.edit().putBoolean("lrytas_state", b).commit();
            }
        });


        open_with.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sharedPreferences.edit().putBoolean("open_with", b).commit();
            }
        });

    }
}
