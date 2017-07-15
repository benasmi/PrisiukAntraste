package com.mabe.productions.prisiukantraste;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class ViewDialog {


    private ArrayList<TitleItem> info = new ArrayList<TitleItem>();
    RecyclerView recyclerView;
    public CustomDialogAdapter adapter;
    private SharedPreferences sharedPreferences;
    private Context context;
    private String url;
    private EditText new_title_txt;
    private ImageView add_new_title;
    private int titleCount=0;
    private String type;
    private LinearLayout layout;
    private Dialog dialog;
    private TextView date_textview;
    private String titleTxt;


    public ViewDialog(Context context){
        this.context = context;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_titles_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        layout = (LinearLayout) dialog.findViewById(R.id.main_layout);
        date_textview = (TextView) dialog.findViewById(R.id.date_textview);


        new_title_txt = (EditText) dialog.findViewById(R.id.add_custom_title);

    }


    public void showDialog( final String url, String date, int code){
        this.url = url;

        info.clear();

        date_textview.setText(CheckingUtils.getTimeAgo(CheckingUtils.getDateInMillis(date)));

        sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);

        switch (code){

            case ChooseNewspapper.TYPE_15MIN:
                type = "15min";
                break;

            case ChooseNewspapper.TYPE_ALFA:
                type = "alfa";
                break;

            case ChooseNewspapper.TYPE_LRYTAS:
                type = "lrytas";
                break;

            case ChooseNewspapper.TYPE_DELFI:
                type = "delfi";
                break;

        }

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/openSans.ttf");



        date_textview.setTypeface(tf);



        add_new_title = (ImageView) dialog.findViewById(R.id.add_custom_title_btn);


        addTitles();

        CheckingUtils.sortArray(info);

        //int desiredHeight = titleCount *

        this.recyclerView = (RecyclerView) dialog.findViewById(R.id.custom_titles_recyclerview);


        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        recyclerView.setItemAnimator(animator);
        recyclerView.getItemAnimator().setAddDuration(1000);
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        recyclerView.getItemAnimator().setMoveDuration(1000);
        recyclerView.getItemAnimator().setChangeDuration(500);
        adapter = new CustomDialogAdapter(context, info, url, type,recyclerView,-1);

        recyclerView.requestLayout();

        if(checkPostedTitle(url)){
            disableTitlidity();
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        recyclerView.setAdapter(adapter);

        recyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width = recyclerView.getMeasuredWidth();
        int height = recyclerView.getMeasuredHeight();

        if(height>1400){
            ViewGroup.LayoutParams params=recyclerView.getLayoutParams();
            params.height= (int) CheckingUtils.convertDpToPx(300f,context);
            recyclerView.setLayoutParams(params);
        }





        add_new_title.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                titleTxt = new_title_txt.getText().toString().trim();

                if(checkPostedTitle(url)){
                    disableTitlidity();

                }else{


                    if(!titleTxt.equals("")){



//                            new ServerManager(context, ServerManager.SERVER_ADDRESS_INSERT_TITLE, false, new OnFinishListener() {
//                                @Override
//                                public void onFinish(int responseCode) {
//                                    switch (responseCode){
//                                        case 0:
//                                            addTitleToSharedPrefs(url,titleTxt);
//                                            new_title_txt.setText("");
//                                            new_title_txt.setSelected(false);
//                                            disableTitlidity();
//                                            adapter.add(new TitleItem(titleTxt,0, 0),adapter.titleItems.size());
//                                            addNewTitleItemToSharedPrefs(0, titleTxt);
//                                            break;
//
//                                        case 1:
//
//                                            Toast.makeText(context, "Tokia antraštė jau egzistuoja", Toast.LENGTH_LONG).show();
//
//                                            break;
//                                        case -1:
//                                            Toast.makeText(context, "Jūs neprisijungęs prie interneto!", Toast.LENGTH_LONG).show();
//                                            break;
//                                    }
//
//                                }
//                            }).execute(url,titleTxt, type);


                        scheduleAddTitleJob(url, titleTxt, type); //TODO: check if title already exists
                        addTitleToSharedPrefs(url,titleTxt);
                        new_title_txt.setText("");
                        new_title_txt.setSelected(false);
                        disableTitlidity();
                        adapter.add(new TitleItem(titleTxt,1, 0),adapter.titleItems.size());
                        addNewTitleItemToSharedPrefs(0, titleTxt);
                        adapter.saveAddedTitle();



                        //Downwoting previous title
                        String votedTitle = adapter.checkVotedTitles(url);
                        if(!votedTitle.equals("")){
                            adapter.scheduleTitleJob("VOTE_DOWN_TITLE", url, votedTitle);
                            adapter.addPointToShared(-1, votedTitle);
                        }

                        //upvoting new title
                        adapter.addPointToShared(+1, titleTxt);
                        adapter.addVotedTitleToSharedPrefs(url, titleTxt);


                        adapter.reloadDataFromSharedPreferences();


                    }
                }




            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up_dialog);
                layout.startAnimation(animation);
            }
        });



        if(!dialog.isShowing()){
            dialog.show();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up_dialog);
                    layout.startAnimation(animation);
                }
            });
        }

    }

    public boolean isDialogShowing(){
        if(dialog==null){
            return false;
        }
        return dialog.isShowing();
    }

    private void scheduleAddTitleJob(String url, String title, String type){

        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString("url", url);
        extras.putString("title", title);
        extras.putString("type", type);
        extras.putString("job_id", "ADD_TITLE");


        new JobRequest.Builder("TITLE_JOB")
                .setExecutionWindow(1L,  Long.MAX_VALUE / 3 * 2)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setExtras(extras)
                .build()
                .schedule();



    }



    public void addTitles(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("titles_data", Context.MODE_PRIVATE);
        String rawData = sharedPreferences.getString(url, "");

        try {
            JSONArray titlesDataArray = new JSONArray(rawData);
            titleCount = titlesDataArray.length();
            for (int i = 0; i<titlesDataArray.length(); i++){
                JSONObject titleData = titlesDataArray.getJSONObject(i);

                int points = titleData.getInt("points");
                String title = titleData.getString("title");
                int isOriginal = titleData.getInt("isOriginal");

                info.add(new TitleItem(title, points, isOriginal));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void addNewTitleItemToSharedPrefs(int points, String title){
        SharedPreferences sharedPreferences = context.getSharedPreferences("titles_data", Context.MODE_PRIVATE);
        String rawData = sharedPreferences.getString(url, "");

        try {
            JSONArray titlesDataArray = new JSONArray(rawData);

            JSONObject obj = new JSONObject();
            obj.put("points",  points);
            obj.put("title", title);
            obj.put("isOriginal",  0);

            titlesDataArray.put(obj);

            sharedPreferences.edit().putString(url, titlesDataArray.toString()).commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void disableTitlidity(){
        new_title_txt.clearFocus();
        new_title_txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, 0, 0);
        new_title_txt.setText("Jūs jau pasiūlėte savo antraštę");
        new_title_txt.setEnabled(false);
        new_title_txt.setTextColor(Color.parseColor("#2c3e50"));
        new_title_txt.setTextSize(14);
    }

    public void enableTitlidity(){
        new_title_txt.setEnabled(true);
        new_title_txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        new_title_txt.setText("");
        new_title_txt.setTextColor(Color.parseColor("#000000"));
        new_title_txt.setTextSize(14);
    }



    private boolean checkPostedTitle(String url){

        String rawData = sharedPreferences.getString("posted_titles", new JSONArray().toString());

        try{
            JSONArray titlesDataArray = new JSONArray(rawData);
            for (int i = 0; i<titlesDataArray.length(); i++){
                JSONObject titleData = titlesDataArray.getJSONObject(i);
                String titleUrl = titleData.getString("url");

                if(titleUrl.equals(url)){

                    return true;

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private  void addTitleToSharedPrefs(String url, @Nullable String title){

        String rawData = sharedPreferences.getString("posted_titles",  new JSONArray().toString());

        try {
            if(title==null){

                JSONArray titlesDataArray = new JSONArray(rawData);
                for (int i = 0; i<titlesDataArray.length(); i++){
                    JSONObject titleData = titlesDataArray.getJSONObject(i);
                    String titleUrl = titleData.getString("url");

                    if(titleUrl.equals(url)){
                        titlesDataArray.remove(i);
                        sharedPreferences.edit().putString("posted_titles", titlesDataArray.toString()).commit();
                    }
                }
            }else{


                JSONArray titlesDataArray = new JSONArray(rawData);
                for (int i = 0; i<titlesDataArray.length(); i++){
                    JSONObject titleData = titlesDataArray.getJSONObject(i);
                    String titleUrl = titleData.getString("url");

                    if(titleUrl.equals(url)){
                        titlesDataArray.remove(i);
                        sharedPreferences.edit().putString("posted_titles", titlesDataArray.toString()).commit();
                    }
                }

                JSONObject titleVoted = new JSONObject();
                titleVoted.put("url", url);
                titleVoted.put("title", title);

                JSONArray votedTitles = new JSONArray(rawData);
                votedTitles.put(titleVoted);
                sharedPreferences.edit().putString("posted_titles", votedTitles.toString()).commit();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onCancelListener(DialogInterface.OnCancelListener cancelListener){
        dialog.setOnCancelListener(cancelListener);
    }


    public void exitAnimation(){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_down);
        layout.startAnimation(animation);
    }



}