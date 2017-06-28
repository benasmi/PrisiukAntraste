package com.mabe.productions.prisiukantraste;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.util.support.PersistableBundleCompat;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class NewsFeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<NewsItem> info = new ArrayList<NewsItem>();
    private TextView toolbar_title;
    private ImageView back_icon;
    private ImageView settings;
    private String type_txt;
    private NewsAdapter adapter;
    private boolean doubleBackToExitPressedOnce = false;
    private SharedPreferences userData;
    private PullRefreshLayout layout;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Paspauskite dar kartą, kad grįžtumėte atgal.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        userData = getSharedPreferences("user_data", MODE_PRIVATE);
        JobManager.create(this).addJobCreator(new JobCreator() {
            @Override
            public Job create(String tag) {


                switch(tag){
                    case "TITLE_JOB":
                        return new Job() {
                            @NonNull
                            @Override
                            protected Result onRunJob(Params params) {
                                PersistableBundleCompat extras = params.getExtras();

                                String job_id = extras.getString("job_id", "");
                                String url = extras.getString("url", "NO_URL");
                                String title = extras.getString("title", "NO_TITLE");

                                Log.i("TEST", job_id);

                                switch (job_id){
                                    case "ADD_TITLE":

                                        String type = extras.getString("type", "NO_TYPE");
                                        ServerManager.insertTitle(url, title, type);
                                        break;

                                    case "VOTE_UP_TITLE":
                                        ServerManager.voting_up(url, title);
                                        break;

                                    case "VOTE_DOWN_TITLE":
                                        ServerManager.voting_down(url, title);
                                        break;
                                }

                                return Result.SUCCESS;

                            }
                        };

                }
                return null;
            }
        });

        CheckingUtils.changeNotifBarColor("#FFFFFF", getWindow());
        CheckingUtils.changeIconsColor(getWindow());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        Bundle extras = getIntent().getExtras();
        final int type = extras.getInt("type");

        Animation hamburgerAnim = AnimationUtils.loadAnimation(this, R.anim.top_down_hamburger);
        Animation titleAnim = AnimationUtils.loadAnimation(this, R.anim.top_down_title);
        Animation settingsAnim = AnimationUtils.loadAnimation(this, R.anim.top_down_settings);



        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar_title = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        back_icon = (ImageView) myToolbar.findViewById(R.id.back_icon);
        settings = (ImageView) myToolbar.findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(NewsFeedActivity.this, SettingsActivity.class),0);
            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userData.edit().putInt("browsing_history",ChooseNewspapper.TYPE_NO_HISTORY).commit();
                startActivity(new Intent(NewsFeedActivity.this, ChooseNewspapper.class));
                NewsFeedActivity.this.finish();

            }
        });


        back_icon.startAnimation(hamburgerAnim);
        toolbar_title.startAnimation(titleAnim);
        settings.startAnimation(settingsAnim);

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/amitaBold.ttf");
        toolbar_title.setTypeface(tf);


        OnFinishListener onFinishListener = new OnFinishListener() {
            @Override
            public void onFinish(int responseCode) {
                layout.setRefreshing(false);
                adapter.refreshPosts(type, false);
                adapter.add(new NewsItem(NewsItem.TYPE_LOADING), adapter.newsItems.size());
            }
        };

        switch (type){

            case ChooseNewspapper.TYPE_15MIN:
                new ServerManager(this, ServerManager.SERVER_ADDRESS_FETCH_15MIN, false, onFinishListener).execute("0");
                type_txt = ServerManager.SERVER_ADDRESS_FETCH_15MIN;
                break;

            case ChooseNewspapper.TYPE_ALFA:
                new ServerManager(this, ServerManager.SERVER_ADDRESS_FETCH_ALFA, false, onFinishListener).execute("0");
                type_txt = ServerManager.SERVER_ADDRESS_FETCH_ALFA;
                break;

            case ChooseNewspapper.TYPE_LRYTAS:

                new ServerManager(this, ServerManager.SERVER_ADDRESS_FETCH_LRYTAS, false, onFinishListener).execute("0");
                type_txt = ServerManager.SERVER_ADDRESS_FETCH_LRYTAS;
                break;

            case ChooseNewspapper.TYPE_DELFI:
                new ServerManager(this, ServerManager.SERVER_ADDRESS_FETCH_DELFI, false, onFinishListener).execute("0");
                type_txt = ServerManager.SERVER_ADDRESS_FETCH_DELFI;
                break;
        }

        adapter = new NewsAdapter(this, info, type);



        layout = (PullRefreshLayout) findViewById(R.id.refresh_layout);
        layout.setEnabled(true);
        layout.setRefreshing(true);
        layout.setColor(Color.parseColor("#3498db"));
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ServerManager(NewsFeedActivity.this, type_txt, false, new OnFinishListener() {
                    @Override
                    public void onFinish(@Nullable int responseCode) {
                        adapter.newsItems.clear();
                        adapter.refreshPosts(type, false);
                        adapter.newsItems.add(new NewsItem(NewsItem.TYPE_LOADING));
                        layout.setRefreshing(false);
                    }
                }).execute("0");
            }
        });

        final StickyLayoutManager layoutManager = new TopSnappedStickyLayoutManager(this, adapter);
        layoutManager.elevateHeaders(true);
        layoutManager.setStickyHeaderListener(new StickyHeaderListener() {
            @Override
            public void headerAttached(View headerView, int adapterPosition) {
                Log.i("TEST", "Attached with position: " + adapterPosition);
            }

            @Override
            public void headerDetached(View headerView, int adapterPosition) {
                Log.i("TEST", "Detached with position: " + adapterPosition);
            }
        });


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Tocheck if  recycler is on top
                if(layoutManager.findFirstCompletelyVisibleItemPosition()==0){
                    layout.setEnabled(true);
                }else{
                    layout.setEnabled(false);
                }
        }
    });




    }


}
