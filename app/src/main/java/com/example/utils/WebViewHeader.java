//package com.example.utils;
//
//import android.content.Context;
//import android.os.Build;
//import android.webkit.CookieManager;
//import android.webkit.CookieSyncManager;
//
//
//public class WebViewHeader {
//    public static void synCookies(Context context, String url) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            CookieSyncManager.createInstance(context);
//        }
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();// 移除
//        cookieManager.removeAllCookie();
//        String session = "session";
//        cookieManager.setCookie(url, (String) SPUtils.get(context,"session",session));//为url设置cookie
//        CookieSyncManager.getInstance().sync();//同步cookie
//}
//}
