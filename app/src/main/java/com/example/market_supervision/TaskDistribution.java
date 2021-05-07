package com.example.market_supervision;

import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.constant.CommonConstant;
import com.example.utils.SPUtils;

public class TaskDistribution extends AppCompatActivity {

    private WebView webView;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_distribution);

        webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {
            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url == null) return false;
                if (url.startsWith("http:") || url.startsWith("https:") ){
                    view.loadUrl(url);
                    return false;
                }else{
                    try{
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }catch (Exception e){
                        //ToastUtils.showShort("暂无应用打开此链接");
                    }
                    return true;
                }
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);  //设置WebView属性,运行执行js脚本
        webView.loadUrl("http://"+ CommonConstant.srvIp +":12345/market/work/taskDistribution?userId="+ (String)SPUtils.get(getApplicationContext(),"userId","1"));          //调用loadUrl方法为WebView加入链接
        setContentView(webView);                           //调用Activity提供的setContentView将webView显示出来
        webView.addJavascriptInterface(new JavascriptCloseInterface(), "activity");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();//返回上个页面
            finish();
            return true;
        }
        finish();
        return super.onKeyDown(keyCode, event);//退出H5界面
    }

    public class JavascriptCloseInterface {
        @JavascriptInterface
        public void over() {
            finish();
        }

    }

}