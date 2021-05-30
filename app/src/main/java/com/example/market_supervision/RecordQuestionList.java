package com.example.market_supervision;

import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.alibaba.fastjson.JSON;
import com.example.constant.CommonConstant;
import com.example.constant.UserInfo;
import com.example.utils.SPUtils;

public class RecordQuestionList extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_question_list);
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
        System.out.println("sputeiteo"+ SPUtils.get(getApplicationContext(),"userInfo","userinfo").toString());
        String userInfoSP= (String) SPUtils.get(getApplicationContext(),"userInfo","userinfo");
        com.example.constant.UserInfo userInfo= JSON.parseObject(userInfoSP, UserInfo.class);
        System.out.println(userInfo);
        String url="http://"+ CommonConstant.srvIp +":12345/market/mobileLaw/recordQuestionList?userId="+userInfo.getUserId();
        webView.loadUrl(url);
        //调用loadUrl方法为WebView加入链接
        setContentView(webView);                           //调用Activity提供的setContentView将webView显示出来
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                // TODO: 2017-5-6 处理下载事件
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
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