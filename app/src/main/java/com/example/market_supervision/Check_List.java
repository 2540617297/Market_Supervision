package com.example.market_supervision;

import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.alibaba.fastjson.JSON;
import com.example.constant.CommonConstant;
import com.example.constant.UserInfo;
import com.example.utils.SPUtils;

import java.util.HashMap;

public class Check_List extends AppCompatActivity {

    private WebView webView;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check__list);
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
        webView.getSettings().setBuiltInZoomControls(false);
//        HashMap<String,Object> userInfoHashMap= (HashMap<String, Object>) SPUtils.get(getApplicationContext(),"userInfo","userinfo");
        System.out.println("sputeiteo"+SPUtils.get(getApplicationContext(),"userInfo","userinfo").toString());
        String userInfoSP= (String) SPUtils.get(getApplicationContext(),"userInfo","userinfo");
        UserInfo userInfo= JSON.parseObject(userInfoSP, UserInfo.class);
        System.out.println(userInfo);
        String url="http://"+ CommonConstant.srvIp +":12345/market/mobileLaw/checkList?userId="+userInfo.getUserId();
        webView.loadUrl(url);
        //调用loadUrl方法为WebView加入链接
        setContentView(webView);                           //调用Activity提供的setContentView将webView显示出来
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
//            webView.goBack();//返回上个页面
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);//退出H5界面
//    }
}