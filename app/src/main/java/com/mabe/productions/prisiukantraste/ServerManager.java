package com.mabe.productions.prisiukantraste;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ServerManager extends AsyncTask<String, String, String> {

    private final Context context;
    private final boolean isProgressDialogRequired;
    String operation_type;
    int responseCode;
    OnFinishListener listener;
    SharedPreferences sharedPreferences;
    SharedPreferences titlesSharedPrefences;


    private ProgressDialog progress_dialog;


    public static final String SERVER_ADDRESS_AUTHENTICATE = "http://prisiukantraste.us.lt/android/register.php";
    public static final String SERVER_ADDRESS_FETCH_15MIN = "http://prisiukantraste.us.lt/android/fetch_15min_rss.php";
    public static final String SERVER_ADDRESS_FETCH_ALFA = "http://prisiukantraste.us.lt/android/fetch_alfa_rss.php";
    public static final String SERVER_ADDRESS_FETCH_DELFI = "http://prisiukantraste.us.lt/android/fetch_delfi_rss.php";
    public static final String SERVER_ADDRESS_FETCH_LRYTAS = "http://prisiukantraste.us.lt/android/fetch_lrytas_rss.php";
    public static final String SERVER_ADDRESS_INSERT_DEVICE_TOKEN = "http://prisiukantraste.us.lt/android/insert_device_id.php";
    public static final String SERVER_ADDRESS_FETCH_TITLES = "http://prisiukantraste.us.lt/android/fetch_custom_titles.php";
    public static final String SERVER_ADDRESS_VOTING_UP = "http://prisiukantraste.us.lt/android/vote_for_custom_title.php";
    public static final String SERVER_ADDRESS_VOTING_DOWN = "http://prisiukantraste.us.lt/android/votedown_for_custom_title.php";
    public static final String SERVER_ADDRESS_INSERT_TITLE = "http://prisiukantraste.us.lt/android/insert_title.php";

    public ServerManager(Context ctx, String type, boolean isProgressDialogRequired, @Nullable OnFinishListener listener) {
        this.isProgressDialogRequired = isProgressDialogRequired;
        this.context = ctx;
        this.operation_type = type;
        sharedPreferences = ctx.getSharedPreferences("post_data", Context.MODE_PRIVATE);
        titlesSharedPrefences = ctx.getSharedPreferences("titles_data", Context.MODE_PRIVATE);
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {

        if(isProgressDialogRequired) {
            progress_dialog = CheckingUtils.progressDialog(context, "Prašome palaukti...", R.style.AppTheme);
            progress_dialog.show();
        }

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {

        if(!CheckingUtils.connectionToServer(1000)){
            responseCode = -1;
            return null;
        }

        if(operation_type.equals("AUTHENTICATE")){
            responseCode = login(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5]);
        }
        if(operation_type.equals(SERVER_ADDRESS_FETCH_15MIN)){
            responseCode = fetch15min(Integer.parseInt(strings[0]));
        }
        if(operation_type.equals(SERVER_ADDRESS_INSERT_DEVICE_TOKEN)){
            responseCode = insertToken(strings[0], strings[1], strings[2]);
        }
        if(operation_type.equals(SERVER_ADDRESS_FETCH_ALFA)){
            responseCode = fetchAlfa(Integer.parseInt(strings[0]));
        }
        if(operation_type.equals(SERVER_ADDRESS_FETCH_LRYTAS)){
            responseCode = fetchLrytas(Integer.parseInt(strings[0]));
        }
        if(operation_type.equals(SERVER_ADDRESS_FETCH_DELFI)){
            responseCode = fetchDelfi(Integer.parseInt(strings[0]));
        }
        if(operation_type.equals(SERVER_ADDRESS_FETCH_TITLES)){
            fetchTitles(strings[0]);
        }
        if(operation_type.equals(SERVER_ADDRESS_VOTING_UP)){
            voting_up(strings[0], strings[1]);
        }
        if(operation_type.equals(SERVER_ADDRESS_VOTING_DOWN)){
            voting_down(strings[0], strings[1]);
        }
        if(operation_type.equals(SERVER_ADDRESS_INSERT_TITLE)){
            responseCode = insertTitle(strings[0],strings[1], strings[2]);
        }
        return null;
    }

    private int fetchAlfa(int from){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS_FETCH_ALFA);

        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.putOpt("start_from", from);
        }catch (Exception e){
            e.printStackTrace();
        }


        EntityBuilder entity = EntityBuilder.create();
        entity.setText(jsonObject.toString());
        httpPost.setEntity(entity.build());


        JSONArray responseObject = null;

        try {
            //Getting response
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            responseObject = new JSONArray(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sharedPreferences.edit().putString("alfa", responseObject.toString()).commit();

        return responseObject.length();
    }

    private int fetchDelfi(int from){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS_FETCH_DELFI);

        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.putOpt("start_from", from);
        }catch (Exception e){
            e.printStackTrace();
        }


        EntityBuilder entity = EntityBuilder.create();
        entity.setText(jsonObject.toString());
        httpPost.setEntity(entity.build());


        JSONArray responseObject = null;

        try {
            //Getting response
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            responseObject = new JSONArray(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sharedPreferences.edit().putString("delfi", responseObject.toString()).commit();

        return responseObject.length();
    }

    private int fetchLrytas(int from){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS_FETCH_LRYTAS);

        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.putOpt("start_from", from);
        }catch (Exception e){
            e.printStackTrace();
        }


        EntityBuilder entity = EntityBuilder.create();
        entity.setText(jsonObject.toString());
        httpPost.setEntity(entity.build());


        JSONArray responseObject = null;

        try {
            //Getting response
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            responseObject = new JSONArray(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sharedPreferences.edit().putString("lrytas", responseObject.toString()).commit();

        return responseObject.length();
    }

    private int fetch15min(int from){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS_FETCH_15MIN);

        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.putOpt("start_from", from);
            Log.i("TEST", String.valueOf(from));
        }catch (Exception e){
            e.printStackTrace();
        }


        EntityBuilder entity = EntityBuilder.create();
        entity.setText(jsonObject.toString());
        httpPost.setEntity(entity.build());


        JSONArray responseObject = null;

        try {
            //Getting response
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            responseObject = new JSONArray(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sharedPreferences.edit().putString("15min", responseObject.toString()).commit();

        return responseObject.length();
    }

    private void fetchTitles(String url){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS_FETCH_TITLES);

        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.putOpt("url", url);
        }catch (Exception e){
            e.printStackTrace();
        }


        EntityBuilder entity = EntityBuilder.create();
        entity.setText(jsonObject.toString());
        httpPost.setEntity(entity.build());


        JSONArray responseObject = null;

        try {
            //Getting response
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            responseObject = new JSONArray(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        titlesSharedPrefences.edit().putString(url, responseObject.toString()).commit();



    }

    private int login(String user_id, String unique_token, String first_name, String last_name, String device_id, String mail){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS_AUTHENTICATE);

        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.putOpt("user_id", user_id);
            jsonObject.putOpt("unique_token", unique_token);
            jsonObject.putOpt("first_name", first_name);
            jsonObject.putOpt("last_name", last_name);
            jsonObject.putOpt("device_id", device_id);
            jsonObject.putOpt("mail", mail);


        }catch (Exception e){
            e.printStackTrace();
        }

        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        ContentType content_type = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        entity.addTextBody("json", jsonObject.toString(), content_type);
        httpPost.setEntity(entity.build());

        JSONObject responseObject = null;

        try {


            //Getting response
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());

            responseObject = new JSONObject(responseBody);

            return responseObject.getInt("code");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;

    }

    public static int insertTitle(String url, String title, String type){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS_INSERT_TITLE);

        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.putOpt("url", url);
            jsonObject.putOpt("title", title);
            jsonObject.putOpt("type", type);


        }catch (Exception e){
            e.printStackTrace();
        }

        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        ContentType content_type = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        entity.addTextBody("json", jsonObject.toString(), content_type);
        httpPost.setEntity(entity.build());

        JSONObject responseObject = null;

        try {


            //Getting response
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());

            responseObject = new JSONObject(responseBody);

            return responseObject.getInt("code");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;

    }

    public static int insertToken(String old_device_token, String device_token, String type){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS_INSERT_DEVICE_TOKEN);

        JSONObject jsonObject = new JSONObject();

        try{


            jsonObject.putOpt("old_device_id", old_device_token);
            jsonObject.putOpt("device_id", device_token);
            jsonObject.putOpt("type", type);


        }catch (Exception e){
            e.printStackTrace();
        }


        EntityBuilder entity = EntityBuilder.create();
        entity.setText(jsonObject.toString());
        httpPost.setEntity(entity.build());

        JSONObject responseObject = null;

        try {


            //Getting response
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());

            responseObject = new JSONObject(responseBody);

            return responseObject.getInt("code");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 1;

    }

    public static void voting_up(String url, String title){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS_VOTING_UP);

        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.putOpt("url", url);
            jsonObject.putOpt("title", title);




        }catch (Exception e){
            e.printStackTrace();
        }



        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        ContentType content_type = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        entity.addTextBody("json", jsonObject.toString(), content_type);
        httpPost.setEntity(entity.build());

        JSONObject responseObject = null;



        //Getting response
        try {
            HttpResponse response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void voting_down(String url, String title){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS_VOTING_DOWN);

        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.putOpt("url", url);
            jsonObject.putOpt("title", title);


        }catch (Exception e){
            e.printStackTrace();
        }

        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        ContentType content_type = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        entity.addTextBody("json", jsonObject.toString(), content_type);
        httpPost.setEntity(entity.build());

        JSONObject responseObject = null;


        try {
            HttpResponse response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    protected void onPostExecute(String s) {
        if(isProgressDialogRequired){
            progress_dialog.dismiss();
        }
        if(listener != null){
            listener.onFinish(responseCode);
        }


        if(operation_type.equals("AUTHENTICATE")){
            switch(responseCode){
                case 0:
                    Intent i = new Intent(context, ChooseNewspapper.class);
                    context.startActivity(i);
                    break;

                case 1:
                    CheckingUtils.createErrorBox("Įvyko klaida!", context, R.style.AppTheme);
                    break;
            }

        }



        super.onPostExecute(s);
    }


}
