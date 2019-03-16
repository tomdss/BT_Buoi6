package com.t3h.bt_buoi6;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebviewActivity extends AppCompatActivity {
    private WebView wvMain;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        wvMain=findViewById(R.id.wv_main);


        wvMain.setWebViewClient(new myBrowser());


        showUrl(getExtra());

//        getExtra();


    }




    private void showUrl(String url) {
        wvMain.getSettings().setLoadsImagesAutomatically(true);
        wvMain.getSettings().setJavaScriptEnabled(true);
        wvMain.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvMain.loadUrl(url);

    }

    private class myBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String Url) {
//            return super.shouldOverrideUrlLoading(view, request);
            view.loadUrl(Url);
//            edtUrl.setText(Url);
            return true;
        }
    }

    private String getExtra() {

        Intent intent = this.getIntent();
        String link = intent.getStringExtra(MainActivity.REQUEST_LINK);

        return link;


//        tvHello.setText("Hello! "+id+" - "+pass);

    }




}
