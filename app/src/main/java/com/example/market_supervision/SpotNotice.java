package com.example.market_supervision;

import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.constant.CommonConstant;
import com.example.utils.SPUtils;

public class SpotNotice extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_notice);
        webView = findViewById(R.id.spotNotice);
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
        webView.getSettings().setBuiltInZoomControls(false);
        String url="http://"+ CommonConstant.srvIp +":12345/market/mobileLaw/spotNotice?userId="+ SPUtils.get(getApplicationContext(),"userId","1");
        webView.loadUrl(url);
        findViewById(R.id.back_log_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SpotNotice.this, MobileLaw.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出H5界面
    }


}