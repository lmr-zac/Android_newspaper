package qz.rg.newspaper.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import qz.rg.newspaper.R;

public class FeedbackActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // 初始化返回按钮
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        // 初始化WebView
        webView = findViewById(R.id.webview);
        setupWebView();
        loadFeedbackUrl();
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        // 启用JavaScript（重要：部分网页需要）
        settings.setJavaScriptEnabled(true);
        // 允许网页自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        // 设置WebViewClient（关键：防止跳转到系统浏览器）
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url); // 在当前WebView内加载新URL
                return true;
            }
        });
    }

    private void loadFeedbackUrl() {
        // 加载目标URL（用户需求中的地址）
        webView.loadUrl("https://www.sina.com.cn/contactus.html");
    }

    // 重写返回键：优先返回WebView历史记录
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy(); // 释放WebView资源
    }
}