package com.mabe.productions.prisiukantraste;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }


        showSkipButton(false);
        addSlide(SampleSlide.newInstance("Sveiki prisijungę!", "Prisiūk antraštę - tai aplikacija, kuri taps jūsų geriausiu draugu skaitant naujienas", R.drawable.app_icon_white, Color.parseColor("#315016")));
        addSlide(SampleSlide.newInstance("Sutaupykite laiko!","Nešvaistykite laiko clickbait'ams ir skaityke straipsnius, kurių antraštės atitinka jų turinį", R.drawable.ic_clock, Color.parseColor("#018191")));

        addSlide(SampleSlide.newInstance("Prisidėkite ir Jūs!","Pasiūlykite savo arba balsuokite už kitų antraštes", R.drawable.ic_pencil, Color.parseColor("#236a99")));

        addSlide(SampleSlide.newInstance("Tai viskas","Naujienos jūsų jau laukia!", R.drawable.ic_heart, Color.parseColor("#dab10d")));


    }


    @Override
    public void onSkipPressed(Fragment currentFragment) {
        Intent i = new Intent(IntroActivity.this, ChooseNewspapper.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in_no_delay, R.anim.fade_in_no_delay);
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        Intent i = new Intent(IntroActivity.this, ChooseNewspapper.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in_no_delay, R.anim.fade_in_no_delay);

        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }



    public static class SampleSlide extends Fragment{

        private static final String ARG_LAYOUT_RES_ID = "layoutResId";
        private int layoutResId;
        private int color;
        String title;
        String description;
        int image;

        private View rootView;


        public static SampleSlide newInstance(String title, String description, int image, int color) {
            SampleSlide sampleSlide = new SampleSlide();

            Bundle args = new Bundle();
            args.putInt(ARG_LAYOUT_RES_ID, R.layout.app_intro_layout_1);
            sampleSlide.setArguments(args);
            sampleSlide.description = description;
            sampleSlide.title = title;
            sampleSlide.image = image;
            sampleSlide.color = color;

            return sampleSlide;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID)) {
                layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
            }



        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            rootView = inflater.inflate(layoutResId, container, false);

            TextView title = (TextView) rootView.findViewById(R.id.intro_title);
            TextView description = (TextView) rootView.findViewById(R.id.intro_description);
            ImageView image = (ImageView) rootView.findViewById(R.id.intro_image);


            Typeface tfLight = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/openSans.ttf");

            Typeface tfBold = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/openSansBold.ttf");

            title.setTypeface(tfBold);
            description.setTypeface(tfLight);

            title.setText(this.title);
            description.setText(this.description);
            image.setImageResource(this.image);


            return rootView;
        }


    }






}

