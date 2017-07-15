package com.mabe.productions.prisiukantraste;


import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CustomDialogAdapter extends  RecyclerView.Adapter<CustomDialogAdapter.ViewHolder>{

    private final Context context;
    SharedPreferences sharedPreferences;
    private String type;
    private RecyclerView recyclerView;
    private String url;
    private int showItems;
    public int global_position = -1;
    private String votedTitle = "";
    public ArrayList<TitleItem> titleItems;

    public CustomDialogAdapter(Context ctx, ArrayList<TitleItem> data, String url, String type, RecyclerView recyclerView, int showItems) {
        this.titleItems = data;
        this.context = ctx;
        this.url = url;
        this.type = type;
        this.showItems = showItems;
        this.recyclerView = recyclerView;
        sharedPreferences = ctx.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        votedTitle = checkVotedTitles(url);
    }




    public boolean reloadDataFromSharedPreferences(){

        titleItems.clear();

        SharedPreferences sharedPreferences = context.getSharedPreferences("titles_data", Context.MODE_PRIVATE);
        String rawData = sharedPreferences.getString(url, "");

        if(rawData.equals("")){
            return  false;
        }

        try {
            JSONArray titlesDataArray = new JSONArray(rawData);
            for (int i = 0; i<titlesDataArray.length(); i++){
                JSONObject titleData = titlesDataArray.getJSONObject(i);

                int points = titleData.getInt("points");
                String title = titleData.getString("title");
                int isOriginal = titleData.getInt("isOriginal");

                titleItems.add(new TitleItem(title, points, isOriginal));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        CheckingUtils.sortArray(titleItems);

        global_position=-1;
        notifyDataSetChanged();

        return true;
    }

    public void add(TitleItem info, int position) {
        titleItems.add(position,info);

        recyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width = recyclerView.getMeasuredWidth();
        int height = recyclerView.getMeasuredHeight();

        if(height>1400){
            ViewGroup.LayoutParams params=recyclerView.getLayoutParams();
            params.height= (int) CheckingUtils.convertDpToPx(300f,context);
            recyclerView.setLayoutParams(params);
        }


        CheckingUtils.sortArray(titleItems);

        notifyDataSetChanged();

        //recyclerView.scrollToPosition(titleItems.size()-1);

    }


    public void remove(int position) {
        titleItems.remove(position);
        notifyItemRemoved(position);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        CustomDialogAdapter.ViewHolder holder;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        view = layoutInflater.inflate(R.layout.custom_title_layout, parent, false);
        holder = new ViewHolder(view, viewType);
        return holder;


    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TitleItem title = titleItems.get(position);



                if(title.getTitle().equals(checkVotedTitles(url))){
                    global_position = position;
                }


                if(title.getIsOrigin()==1){
                    holder.title.setTextColor(Color.parseColor("#000000"));
                    holder.title.setTypeface(null, Typeface.BOLD);
                }else{
                    holder.title.setTextColor(Color.parseColor("#000000"));
                    holder.title.setTypeface(null, Typeface.NORMAL);
                }


                if(votedTitle.equals(title.getTitle())){
                    global_position = position;
                    votedTitle = "";
                }



                if(position == global_position){
                    holder.vote_button.setBackgroundResource(R.drawable.button_voted);
                    holder.vote_button.setTextColor(Color.WHITE);
                }else{
                    holder.vote_button.setBackgroundResource(R.drawable.button_no_voted);
                    holder.vote_button.setTextColor(Color.BLACK);
                }



                holder.vote_button.setText(String.valueOf(title.getPoints()));
                holder.title.setText(title.getTitle());

                holder.layout.setOnClickListener(new View.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {

                        if(isTitleAdded()){
                            return;
                        }

                        //Kai packlinik jau pabalsuota
                        if(global_position==position){
                            titleItems.get(global_position).setPoints(titleItems.get(global_position).getPoints()-1);
                            addPointToShared(-1, titleItems.get(position).getTitle());


                            scheduleTitleJob("VOTE_DOWN_TITLE", url, title.getTitle());

                            global_position = -1;
                        }else{

                            if(global_position != -1){
                                scheduleTitleJob("VOTE_DOWN_TITLE", url, titleItems.get(global_position).getTitle());
                                titleItems.get(global_position).setPoints(titleItems.get(global_position).getPoints()-1);
                                addPointToShared(-1, titleItems.get(global_position).getTitle());

                            }

                            global_position = position;
                            titleItems.get(global_position).setPoints(titleItems.get(global_position).getPoints()+1);
                            addPointToShared(+1, titleItems.get(global_position).getTitle());
                            scheduleTitleJob("VOTE_UP_TITLE", url, titleItems.get(global_position).getTitle());
                        }


                        if(global_position==-1){
                            addVotedTitleToSharedPrefs(url, null);
                        }else{
                            addVotedTitleToSharedPrefs(url, titleItems.get(global_position).getTitle());
                        }


                        CheckingUtils.sortArray(titleItems);
                        notifyItemRangeChanged(0, getItemCount());
                    }
                });




    }

    public void scheduleTitleJob(String jobId, String url, String title){

        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString("url", url);
        extras.putString("title", title);
        extras.putString("job_id", jobId);


        new JobRequest.Builder("TITLE_JOB")
                .setExecutionWindow(1L,  Long.MAX_VALUE / 3 * 2)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setExtras(extras)
                .build()
                .schedule();



    }

    //Returing title that has been voted
    public String checkVotedTitles(String url){

        String rawData = sharedPreferences.getString("voted_titles", new JSONArray().toString());

        try{
            JSONArray titlesDataArray = new JSONArray(rawData);
            for (int i = 0; i<titlesDataArray.length(); i++){
                JSONObject titleData = titlesDataArray.getJSONObject(i);
                String titleUrl = titleData.getString("url");

                if(titleUrl.equals(url)){

                    return titleData.getString("title");

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

    //Adds this post to posts_with_added_titles shared preference to prevent other post voting
    public void saveAddedTitle(){
        String rawData = sharedPreferences.getString("posts_with_added_titles", new JSONArray().toString());

        try {
            JSONArray titlesDataArray = new JSONArray(rawData);
            JSONObject json = new JSONObject();
            json.put("url", url);
            titlesDataArray.put(json);
            sharedPreferences.edit().putString("posts_with_added_titles", titlesDataArray.toString()).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //Checks if a title has been added to this post
    public boolean isTitleAdded(){
        String rawData = sharedPreferences.getString("posts_with_added_titles", new JSONArray().toString());

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


    //Add voted title to shared prefs of voted titles
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addVotedTitleToSharedPrefs(String url, @Nullable String title){

        String rawData = sharedPreferences.getString("voted_titles",  new JSONArray().toString());

        try {
            if(title==null){

                JSONArray titlesDataArray = new JSONArray(rawData);
                for (int i = 0; i<titlesDataArray.length(); i++){
                    JSONObject titleData = titlesDataArray.getJSONObject(i);
                    String titleUrl = titleData.getString("url");

                    if(titleUrl.equals(url)){
                        titlesDataArray.remove(i);
                        sharedPreferences.edit().putString("voted_titles", titlesDataArray.toString()).commit();
                    }
                }
            }else{


                JSONArray titlesDataArray = new JSONArray(rawData);
                for (int i = 0; i<titlesDataArray.length(); i++){
                    JSONObject titleData = titlesDataArray.getJSONObject(i);
                    String titleUrl = titleData.getString("url");

                    if(titleUrl.equals(url)){
                        titlesDataArray.remove(i);
                        sharedPreferences.edit().putString("voted_titles", titlesDataArray.toString()).commit();
                    }
                }


                rawData = sharedPreferences.getString("voted_titles",  new JSONArray().toString());

                JSONObject titleVoted = new JSONObject();
                titleVoted.put("url", url);
                titleVoted.put("title", title);

                JSONArray votedTitles = new JSONArray(rawData);
                votedTitles.put(titleVoted);

                sharedPreferences.edit().putString("voted_titles", votedTitles.toString()).commit();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void addPointToShared(int plusOrMinus, String given_title){

        JSONArray jsonArray = new JSONArray();
        SharedPreferences sharedPreferences = context.getSharedPreferences("titles_data", Context.MODE_PRIVATE);
        String rawData = sharedPreferences.getString(url, "");

        try {
            JSONArray titlesDataArray = new JSONArray(rawData);
            for (int i = 0; i<titlesDataArray.length(); i++){
                JSONObject titleData = titlesDataArray.getJSONObject(i);

                int points = titleData.getInt("points");
                String title = titleData.getString("title");
                int isOriginal = titleData.getInt("isOriginal");

                if(given_title.equals(title)){
                    points = points + plusOrMinus;
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", title);
                jsonObject.put("isOriginal", isOriginal);
                jsonObject.put("points", points);

                jsonArray.put(jsonObject);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        sharedPreferences.edit().putString(url, jsonArray.toString()).commit();


    }



    @Override
    public int getItemCount() {

        if(showItems == -1){
            return titleItems.size();
        }

        if(titleItems.size()<showItems){
            return titleItems.size();
        }

        return showItems;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private EditText add_custom_title_txt;

        private TextView title;
        private RelativeLayout layout;
        private AppCompatButton vote_button;
        private View rootView;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            this.rootView = itemView;
            this.vote_button = (AppCompatButton) rootView.findViewById(R.id.vote_button);
            this.title = (TextView) rootView.findViewById(R.id.custom_title);
            this.layout = (RelativeLayout) rootView.findViewById(R.id.child_root_layout);
            this.add_custom_title_txt = (EditText) rootView.findViewById(R.id.add_custom_title);

        }
    }
}
