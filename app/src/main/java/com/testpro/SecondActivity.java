package com.testpro;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * 测试界面2
 */
public class SecondActivity extends Activity {

    private TextView tv_1, tv_2;

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MyClass.test1();
            }
        });

        mWebView = (WebView) findViewById(R.id.webView);
        setWebviewAttributes();
        mWebView.loadUrl("file:///android_asset/h5file/test_a.html");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                /** 清除-辅助功能远程代码执行漏洞 */
                if (android.os.Build.VERSION.SDK_INT >= 11) {
                    view.removeJavascriptInterface("accessibility");
                    view.removeJavascriptInterface("accessibilityTraversal");
                    view.removeJavascriptInterface("searchBoxJavaBridge_");
                }
            }
        });
    }

    /**
     * 设置webview属性
     */
    public void setWebviewAttributes() {
        WebSettings webSettings = mWebView.getSettings();
        // 支持JavaScript
        webSettings.setJavaScriptEnabled(true);
        // 支持localStorage
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        // 无缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(false);
        // 设置不保存密码
        webSettings.setSavePassword(false);

    }
}
