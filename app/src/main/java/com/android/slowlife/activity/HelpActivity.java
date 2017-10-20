package com.android.slowlife.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.slowlife.R;
import com.interfaceconfig.Config;

public class HelpActivity extends AppCompatActivity {
    private WebView webView;
    public static final String URI = "uri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        webView = (WebView) findViewById(R.id.webview);
        init();
    }

    private void init() {
        String url = getIntent().getStringExtra(URI);
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(false);
        setting.setAppCacheEnabled(true);
        setting.setUseWideViewPort(true);//关键点
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setting.setDisplayZoomControls(false);
        setting.setAllowFileAccess(false); // 允许访问文件
        setting.setBuiltInZoomControls(false); // 设置显示缩放按钮
        setting.setSupportZoom(false); // 支持缩放
        setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        setting.setUserAgentString(setting.getUserAgentString() );
        System.out.println(setting.getUserAgentString());
        webView.addJavascriptInterface(new JavaScriptCallAndroid(), "Android");
//        webView.loadUrl("file:///android_asset/test.html");
        webView.loadUrl(Config.Url.getUrl(url));
//        webView.loadDataWithBaseURL(Config.LOCAL_HOST, null, "html/text", "utf-8",
//                Config.Url.getUrl("slowlife/app/appGeneral/ruleGZ.html"));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                view.loadUrl(url);
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
        });
    }

    class JavaScriptCallAndroid {
        @JavascriptInterface
        public void goBack() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (webView.canGoBack())
                        webView.goBack();
                    else finish();
                }
            });
        }

        @JavascriptInterface
        public void tel(String phone) {
            //用intent启动拨打电话
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:02372721515"));
            startActivity(intent);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) webView.goBack();
            else finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
