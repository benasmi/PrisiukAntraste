package com.mabe.productions.prisiukantraste;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends  RecyclerView.Adapter<NewsAdapter.ViewHolder> implements StickyHeaderHandler{

    private final Context context;
    private ViewDialog viewDialog;
    private int type;
    int postsAnimated = 0;
    int headersAnimated = 0;
    SharedPreferences sharedPreferences;
    public ArrayList<NewsItem> newsItems;
    private int newspaper_logo_resource;
    private SharedPreferences sharedPreferencesTitles;

    public NewsAdapter(Context ctx, ArrayList<NewsItem> data, int type) {
        this.newsItems = data;
        this.context = ctx;
        this.type = type;
        viewDialog = new ViewDialog(context);
        sharedPreferencesTitles = context.getSharedPreferences("titles_data", Context.MODE_PRIVATE);

        switch (type){

            case ChooseNewspapper.TYPE_15MIN:
                newspaper_logo_resource = R.drawable.min15_logo;
                break;

            case ChooseNewspapper.TYPE_ALFA:
                newspaper_logo_resource = R.drawable.alfa_logo;
                break;

            case ChooseNewspapper.TYPE_LRYTAS:
                newspaper_logo_resource = R.drawable.lrytas_logo;
                break;
            case ChooseNewspapper.TYPE_DELFI:
                newspaper_logo_resource = R.drawable.delfi_logo;
                break;

        }

        sharedPreferences = ctx.getSharedPreferences("post_data", Context.MODE_PRIVATE);

    }

    public void add(NewsItem info, int position) {
        newsItems.add(position,info);

        notifyDataSetChanged();
    }


    private String checkVotedTitles(String url){

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private  void addVotedTitleToSharedPrefs(String url, @Nullable String title){

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


    public void refreshPosts(int type, boolean showAds){
        String rawData = null;

        switch (type){

            case ChooseNewspapper.TYPE_15MIN:
                rawData = sharedPreferences.getString("15min", "");
                break;
            case ChooseNewspapper.TYPE_ALFA:
                rawData = sharedPreferences.getString("alfa", "");
                break;

            case ChooseNewspapper.TYPE_LRYTAS:
                rawData = sharedPreferences.getString("lrytas", "");
                break;
           case ChooseNewspapper.TYPE_DELFI:
                rawData = sharedPreferences.getString("delfi", "");

                break;

        }


        try {
            JSONArray newsDataArray = new JSONArray(rawData);
            for (int i = 0; i<newsDataArray.length(); i++){
                JSONObject newsData = newsDataArray.getJSONObject(i);
                String description = newsData.getString("description");
                String title = newsData.getString("title");
                String image_url = newsData.getString("image_url");
                String url = newsData.getString("url");

                String first_title = newsData.getString("title_first");
                String second_title = newsData.getString("title_second");
                String titleCount = newsData.getString("titleCount");
                String date = newsData.getString("timestamp");

                int first_title_points = newsData.getInt("title_first_points");
                int first_title_isOrigin = newsData.getInt("title_first_isOrigin");

                int second_title_points = newsData.getInt("title_second_points");
                int second_title_isOrigin = newsData.getInt("title_second_isOrigin");

                int image_height = newsData.getInt("height");

                TitleItem firstTitle = new TitleItem(first_title, first_title_points, first_title_isOrigin);
                TitleItem secondTitle = new TitleItem(second_title,second_title_points, second_title_isOrigin);

                if(getItemCount() % 5 == 0 && getItemCount() != 0){
                    if(showAds){
//                        this.add(new NewsItem(NewsItem.TYPE_AD), getItemCount());
                    }
                }


                this.add(new HeaderItem(date, NewsItem.TYPE_HEADER), getItemCount());
                this.add(new NewsItem(url, image_url, title, description, NewsItem.TYPE_POST, image_height, firstTitle, secondTitle, titleCount, date), getItemCount());



            }


        } catch (JSONException e) {

            e.printStackTrace();
        }

    }

    public void remove(int position) {
        newsItems.remove(position);
        notifyItemRemoved(position);

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        NewsAdapter.ViewHolder holder;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        switch (viewType){

            //post layout
            case 0:
                view = layoutInflater.inflate(R.layout.news_item_layout, parent, false);
                holder = new ViewHolder(view, viewType);
                return holder;

            case 1:
                view = layoutInflater.inflate(R.layout.ad_item_layout, parent, false);
                holder = new ViewHolder(view, viewType);
                return holder;

            case 2:
                view = layoutInflater.inflate(R.layout.loading_item_view, parent, false);
                holder = new ViewHolder(view, viewType);
                return holder;

            case 3:
                view = layoutInflater.inflate(R.layout.header_item_layout, parent, false);
                holder = new ViewHolder(view, viewType);
                return holder;
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return newsItems.get(position).getType();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int post_type = newsItems.get(position).getType();


        final NewsItem item = newsItems.get(position);

        switch(post_type){





            case NewsItem.TYPE_POST:



                String description = item.getDescription();
                String image_url = item.getImageUrl();
                String title = item.getTitle();
                String url = item.getUrl();
                String titleCount = item.getTitleCount();

                //holder.date_textview.setText(CheckingUtils.getTimeAgo(CheckingUtils.getDateInMillis(item.getDate())));



                    ArrayList<TitleItem> titleItemArrayList = new ArrayList<TitleItem>();


                    holder.adapter = new CustomDialogAdapter(context, titleItemArrayList, url, String.valueOf(type), holder.custom_titles_recyclerView, 2);
                    holder.custom_titles_recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    holder.custom_titles_recyclerView.setAdapter(holder.adapter);

                    if(postsAnimated <=1){

                        postsAnimated++;


                        holder.rootView.setTranslationY(CheckingUtils.getScreenHeight(context));
                        holder.rootView.animate()
                                .translationY(0)
                                .setInterpolator(new DecelerateInterpolator(3.f))
                                .setDuration(700)
                                .start();


                    }else{
                        holder.rootView.setTranslationY(0);
                    }


                    Glide
                            .with(context)
                            .load(item.getImageUrl())
                            .fitCenter()
                            .crossFade()
                            .error(R.drawable.ic_no_image)
                            .animate(R.anim.fade)
                            .override(400,item.getImageHeight())
                            .into(holder.preview_img);




                    if(!holder.adapter.reloadDataFromSharedPreferences()){

                        holder.adapter.add(item.getFirstTitle(), holder.adapter.titleItems.size());


                        if(!item.getSecondTitle().getTitle().equals("")){
                            holder.adapter.add(item.getSecondTitle(), holder.adapter.titleItems.size());

                        }




                    }



                    holder.title.setText(title.trim());
                    holder.titleCount.setText(titleCount);

                    //TODO: load img
                    holder.rootView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            context.startActivity(new Intent(context, WebViewActivity.class).putExtra("url", item.getUrl()).putExtra("type", type).putExtra("title", item.getTitle()));

                        }
                    });






                break;

            case NewsItem.TYPE_AD:



                break;



            case NewsItem.TYPE_HEADER:

                Animation animation = AnimationUtils.loadAnimation(context,R.anim.first_item_animation);

                holder.header_news_icon.setImageResource(R.drawable.min15_logo);
                holder.header_date.setText(CheckingUtils.getTimeAgo(CheckingUtils.getDateInMillis(item.getDate())));
                holder.header_news_icon.setImageResource(newspaper_logo_resource);


//                    if(position==0){
//                        holder.headerLayout.startAnimation(animation);
//                    }


                    if(headersAnimated <=1 && position!=0){

                        headersAnimated++;


                        holder.headerLayout.setTranslationY(CheckingUtils.getScreenHeight(context));
                        holder.headerLayout.animate()
                                .translationY(0)
                                .setInterpolator(new DecelerateInterpolator(3.f))
                                .setDuration(700)
                                .start();


                    }else{
                        holder.headerLayout.setTranslationY(0);
                    }




                break;
        }

        if(position+1 == getItemCount()){ //TODO: compensate for ads

            OnFinishListener onFinishListener = new OnFinishListener() {
                @Override
                public void onFinish(int responseCode) {

                    if(responseCode == -1){
                        return;
                    }

                    if(responseCode != 0){
                        remove(newsItems.size()-1);
                        refreshPosts(type, true);

                        add(new NewsItem(NewsItem.TYPE_LOADING), newsItems.size());
                    }else{
                        if(getItemViewType(newsItems.size()-1) == NewsItem.TYPE_LOADING){
                            remove(newsItems.size()-1);
                        }
                        refreshPosts(type, true);
                    }
                }
            };


            int newPos = 0;

            if(position > 0 ){
                newPos = position+1;
            }else{
                newPos = position;
            }

            switch (type){

                case ChooseNewspapper.TYPE_15MIN:
                    new ServerManager(context, ServerManager.SERVER_ADDRESS_FETCH_15MIN, false, onFinishListener).execute(String.valueOf(newPos));
                    break;

                case ChooseNewspapper.TYPE_ALFA:
                    new ServerManager(context, ServerManager.SERVER_ADDRESS_FETCH_ALFA, false, onFinishListener).execute(String.valueOf(newPos));
                    break;

                case ChooseNewspapper.TYPE_LRYTAS:
                    new ServerManager(context, ServerManager.SERVER_ADDRESS_FETCH_LRYTAS, false, onFinishListener).execute(String.valueOf(newPos));
                    break;

                case ChooseNewspapper.TYPE_DELFI:
                    new ServerManager(context, ServerManager.SERVER_ADDRESS_FETCH_DELFI, false, onFinishListener).execute(String.valueOf(newPos));
                    break;

            }
        }

    }


    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    @Override
    public List<?> getAdapterData() {
        return newsItems;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView share_image;
        private ImageView ad_icon;
        private AppCompatButton ad_action_button;
        private TextView ad_description;
        private ViewSwitcher ad_corner_image;
        private TextView ad_title;
        private TextView title;
        private ImageView preview_img;
        private ImageView change_title_btn;
        private RecyclerView custom_titles_recyclerView;
        private CustomDialogAdapter adapter;
        private ImageView back_arrow;
        private ImageView ad_view;
        private GestureDetector gestureDetector;
        private View rootView;
        private TextView titleCount;
        private TextView date_textview;
        private ImageView newspaper_logo;


        //Headear Items
        private ImageView header_news_icon;
        private TextView header_date;
        private RelativeLayout headerLayout;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            this.rootView = itemView;

            switch (viewType){



                case NewsItem.TYPE_POST:
                    title = (TextView) itemView.findViewById(R.id.title);
                    preview_img = (ImageView) itemView.findViewById(R.id.news_preview);
                    custom_titles_recyclerView = (RecyclerView) itemView.findViewById(R.id.custom_titles_recyclerview);
                    titleCount = (TextView) itemView.findViewById(R.id.titleCount);
                    //date_textview = (TextView) itemView.findViewById(R.id.date_news_feed);
                    share_image = (ImageView) itemView.findViewById(R.id.share_icon);

                    change_title_btn = (ImageView) rootView.findViewById(R.id.change_title_ic);
                    //newspaper_logo = (ImageView) rootView.findViewById(R.id.news_icon);

                    //newspaper_logo.setImageResource(newspaper_logo_resource);

                    share_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, newsItems.get(getAdapterPosition()).getUrl());
//                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title.getText());
                            context.startActivity(Intent.createChooser(sharingIntent,"Siųsti naudojant..."));
                        }
                    });

                    change_title_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {


                            new ServerManager(context, ServerManager.SERVER_ADDRESS_FETCH_TITLES, false, new OnFinishListener() {
                                @Override
                                public void onFinish(final int responseCode) {



                                    if(!viewDialog.isDialogShowing()) {

                                        viewDialog.onCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialogInterface) {
                                                if(responseCode!=-1){
                                                    adapter.reloadDataFromSharedPreferences();
                                                    title.setText(adapter.titleItems.get(0).getTitle());

                                                    //Changing title count display
                                                    try {
                                                        JSONArray array = new JSONArray(sharedPreferencesTitles.getString(newsItems.get(getAdapterPosition()).getUrl(), ""));
                                                        titleCount.setText(CheckingUtils.howManyTitles(array.length()));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }

                                                    viewDialog.enableTitlidity();


                                            }
                                        });

                                        viewDialog.showDialog(newsItems.get(getAdapterPosition()).getUrl(), newsItems.get(getAdapterPosition()).getDate(), type);

                                        if(responseCode == -1){
                                            viewDialog.adapter.titleItems = adapter.titleItems;
                                        }
                                    }
                                }
                            }).execute(newsItems.get(getAdapterPosition()).getUrl());

                        }

                    });

                    break;

                case NewsItem.TYPE_AD:


                    ad_title = (TextView) rootView.findViewById(R.id.ad_title);
                    ad_view =  (ImageView) rootView.findViewById(R.id.ad_view);
                    ad_corner_image = (ViewSwitcher) rootView.findViewById(R.id.ad_corner_image_view_switcher);
                    ad_description = (TextView) rootView.findViewById(R.id.ad_description);
                    ad_action_button = (AppCompatButton) rootView.findViewById(R.id.ad_action_button);
                    ad_icon = (ImageView) rootView.findViewById(R.id.ad_icon);



                    break;


                case NewsItem.TYPE_LOADING:


                    break;



                case NewsItem.TYPE_HEADER:

                    header_news_icon = (ImageView) rootView.findViewById(R.id.news_icon_header);
                    header_date = (TextView) rootView.findViewById(R.id.date_news_feed_header);
                    headerLayout = (RelativeLayout) rootView.findViewById(R.id.header_layout);

                    break;
            }



            }


        }
    }



