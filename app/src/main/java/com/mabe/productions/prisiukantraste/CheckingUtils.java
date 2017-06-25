package com.mabe.productions.prisiukantraste;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Benas on 19/04/2017.
 */

public class CheckingUtils {


    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static void changeNotifBarColor(String color, Window window){

        if (Build.VERSION.SDK_INT >= 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor(color));
        }

    }

    public static void changeNotifBarColor(int color, Window window){

        if (Build.VERSION.SDK_INT >= 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }

    }

    public static void changeIconsColor(Window window){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = window.getDecorView();

            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
    }

    public static boolean connectionToServer(int timeoutMs) {
        try {
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }


    //Error box to inform UI
    public static void createErrorBox(String message, Context context, int theme){

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            new AlertDialog.Builder(context, theme)
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }else{
            new AlertDialog.Builder(context)
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }


    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }


    public static ProgressDialog progressDialog(Context context, String message, int theme){

        ProgressDialog progressDialog = null;

        if(Build.VERSION.SDK_INT>=21){
            progressDialog = new ProgressDialog(context, theme);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }else{
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        return progressDialog;
    }

    public static float convertDpToPx(float px, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resources.getDisplayMetrics());

    }

    public static void sortArray(ArrayList<TitleItem> arrayList){
        Collections.sort(arrayList, new Comparator<TitleItem>() {
            @Override
            public int compare(TitleItem o1, TitleItem o2) {
                return  o2.getPoints() - o1.getPoints();
    }
});
}

    public static Drawable resizeDrawable(Context context, int imageResource){

        Drawable drawable = ContextCompat.getDrawable(context, imageResource);
        int pixelDrawableSize = (int) convertDpToPx(7, context); // Or the percentage you like (0.8, 0.9, etc.)
        drawable.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize);

        return drawable;
    }




    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "ką tik";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "prieš minutę";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return  "Prieš " + diff / MINUTE_MILLIS + " min";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "Prieš valandą";
        } else if (diff < 24 * HOUR_MILLIS) {
            return "Prieš " +  diff / HOUR_MILLIS + " val";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "vakar";
        } else {
            return "Prieš " + diff / DAY_MILLIS + " d";
        }
    }

    public static long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static String howManyTitles(int titleCount){
        if(titleCount==1){
            return titleCount + " antraštė";
        }
        if(titleCount>1 && titleCount <10){

            return titleCount + " antraštės";
        }

        if(titleCount>9 && titleCount<21){
            return titleCount + " antraščių";
        }


        if(titleCount%10==0){

            return titleCount + " antraščių";
        }

        if(titleCount%10==1){

            return titleCount + " antraštė";
        }

        return titleCount + " antraštės";

    }


}
