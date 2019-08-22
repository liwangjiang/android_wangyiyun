package netease.li.com.wangyiyun.splash.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import netease.li.com.wangyiyun.R;
import netease.li.com.wangyiyun.splash.bean.Action;

public class WebActivity extends Activity{
    public static final String H5_PATH="src_path";
    private Action action;//超链接路径
    WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        action=(Action)getIntent().getSerializableExtra(H5_PATH);
        setContentView(R.layout.web_activity);
        webView=findViewById(R.id.webViewId);
        //加载网页
        webView.getSettings().setJavaScriptEnabled(true);
        //web全屏设置
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.loadUrl(action.getLink_url());

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    //处理回退键
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
        webView.goBack();
        return;
        }
        super.onBackPressed();
    }
}
