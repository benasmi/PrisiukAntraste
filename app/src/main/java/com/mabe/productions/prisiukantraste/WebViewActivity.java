package com.mabe.productions.prisiukantraste;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        webView = (WebView) findViewById(R.id.webview);

        Bundle extras = getIntent().getExtras();
        int type = extras.getInt("type");
        //final String title = extras.getString("title");

        String url = extras.getString("url");





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
                //url = url.replace("delfi.lt", "m.delfi.lt");
                Log.i("TEST", url);
                webView.loadUrl(url);
                break;

        }





    }



}
