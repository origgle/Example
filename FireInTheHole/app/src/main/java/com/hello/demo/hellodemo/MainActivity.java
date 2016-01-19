package com.hello.demo.hellodemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTxtData;
    private WebView mWebView;

    private String mUDPData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxtData = (TextView) findViewById(R.id.text_data);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                String reply = UDPSender.send("Hello");

                mUDPData = reply;
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mTxtData.setText(mUDPData);
            mWebView.loadUrl(mUDPData);
        }
    };
}
