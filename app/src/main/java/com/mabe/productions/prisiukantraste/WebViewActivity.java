package com.mabe.productions.prisiukantraste;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    private TextView title_txt;
    private ImageView back_arrow;
    private ImageView add_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        webView = (WebView) findViewById(R.id.webview);

        Bundle extras = getIntent().getExtras();
        final int type = extras.getInt("type");
        String title = extras.getString("title");
        final String date = extras.getString("date");
        final String url = extras.getString("url");


        CheckingUtils.changeNotifBarColor("#FFFFFF", getWindow());
        CheckingUtils.changeIconsColor(getWindow());
        Toolbar myToolbar = (Toolbar) findViewById(R.id.webview_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#000000"));

        title_txt = (TextView) myToolbar.findViewById(R.id.webview_txt);
        back_arrow = (ImageView) myToolbar.findViewById(R.id.webview_back_icon);
        add_title = (ImageView) myToolbar.findViewById(R.id.add_title_from_webview);


        Typeface tfBold = Typeface.createFromAsset(getAssets(),
                "fonts/openSansBold.ttf");

        Typeface tfLight = Typeface.createFromAsset(getAssets(),
                "fonts/openSans.ttf");

        title_txt.setTypeface(tfBold);

        title_txt.setText(trimToSize(title, 24));

        add_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ServerManager(WebViewActivity.this, ServerManager.SERVER_ADDRESS_FETCH_TITLES, false, new OnFinishListener() {
                    @Override
                    public void onFinish(@Nullable int responseCode) {
                        if(responseCode==-1){
                            Toast.makeText(WebViewActivity.this,"Jūs neprisijungęs prie interneto. Bandykite vėliau", Toast.LENGTH_LONG).show();
                            return;
                        }

                        ViewDialog viewDialog = new ViewDialog(WebViewActivity.this);
                        viewDialog.showDialog(url,date,type);
                    }
                }).execute(url);



            }
        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    WebViewActivity.this.finish();
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);




        switch (type){

            case ChooseNewspapper.TYPE_15MIN:
                webView.getSettings().setUserAgentString("Android WebView");
                webView.loadUrl(url);
                ObjectAnimator anim = ObjectAnimator.ofInt(webView, "scrollY", 0, (int) CheckingUtils.convertDpToPx(450, this));
                anim.setDuration(2200);
                anim.start();

                break;
            case ChooseNewspapper.TYPE_ALFA:
                webView.getSettings().setUserAgentString("Android WebView");
                webView.loadUrl(url);
                break;

            case ChooseNewspapper.TYPE_LRYTAS:
                webView.getSettings().setUserAgentString("Android WebView");
                webView.loadUrl(url);
                break;
            case ChooseNewspapper.TYPE_DELFI:
                webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.2; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
                Log.i("TEST", url);
                webView.loadUrl(url);
                break;

        }





    }

    private String trimToSize(String string, int maxSize){
        if(string.length() < maxSize){
            return string;
        }

        return string.substring(0, maxSize) + "...";
    }



}
