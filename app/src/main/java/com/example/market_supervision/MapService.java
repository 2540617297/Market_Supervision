package com.example.market_supervision;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.constant.CommonConstant;
import com.example.utils.SPUtils;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MapService extends Service implements  AMapLocationListener {
    public AMapLocationClientOption mLocationOption = null;//声明AMapLocationClientOption对象
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    String routeId;

    @Override
    public void onCreate() {
        super.onCreate();
        //"权限已申请";
        showLocation();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("startservice");
        Date date=new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        routeId = dateFormat.format(date);
        System.out.println(routeId);
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("stopservice");
        // 停止定位
        mLocationClient.stopLocation();
        mLocationClient.disableBackgroundLocation(true);
        mLocationClient.unRegisterLocationListener(this);
        mLocationClient = null;
        mLocationOption = null;
    }

    // TODO:
    private void showLocation() {
        try {
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mLocationClient.setLocationListener(this);
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(50000);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
        } catch (Exception e) {

        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        try {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息

                    //获取当前定位结果来源，如网络定位结果，详见定位类型表
                    Log.i("定位类型", amapLocation.getLocationType() + "");
                    Log.i("获取纬度", amapLocation.getLatitude() + "");
                    Log.i("获取经度", amapLocation.getLongitude() + "");
                    Log.i("获取精度信息", amapLocation.getAccuracy() + "");

                    //如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    Log.i("地址", amapLocation.getAddress());
                    Log.i("国家信息", amapLocation.getCountry());
                    Log.i("省信息", amapLocation.getProvince());
                    Log.i("城市信息", amapLocation.getCity());
                    Log.i("城区信息", amapLocation.getDistrict());
                    Log.i("街道信息", amapLocation.getStreet());
                    Log.i("街道门牌号信息", amapLocation.getStreetNum());
                    Log.i("城市编码", amapLocation.getCityCode());
                    Log.i("地区编码", amapLocation.getAdCode());
                    Log.i("获取当前定位点的AOI信息", amapLocation.getAoiName());
                    Log.i("获取当前室内定位的建筑物Id", amapLocation.getBuildingId());
                    Log.i("获取当前室内定位的楼层", amapLocation.getFloor());
                    Log.i("获取GPS的当前状态", amapLocation.getGpsAccuracyStatus() + "");

                    //获取定位时间
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    Log.i("获取定位时间", df.format(date));

                    HashMap<String,String> routInfo=new HashMap<>();
                    routInfo.put("routeId",routeId);
                    routInfo.put("mLatitude",amapLocation.getLatitude()+"");
                    routInfo.put("mLongitude",amapLocation.getLongitude()+"");
                    routInfo.put("mHorizontalAccuracyMeters",amapLocation.getAccuracy()+"");
                    routInfo.put("address",amapLocation.getAddress());
                    routInfo.put("addTime",df.format(date));
                    String userId= (String) SPUtils.get(getApplicationContext(),"userId","1");
                    routInfo.put("addUser",userId);
                    FormBody.Builder builder = new FormBody.Builder();
                    for (String key : routInfo.keySet()) {
                        //追加表单信息
                        builder.add(key, routInfo.get(key));
                    }
                    OkHttpClient okHttpClient=new OkHttpClient();
                    RequestBody formBody=builder.build();
                    String url="http://"+ CommonConstant.srvIp +":12345/market/mobileLaw/saveRouteInfo";
                    System.out.println(url);
                    Request request=new Request.Builder().url(url).post(formBody).build();
                    Call call=okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //请求失败的处理
                            System.out.println("false");
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            System.out.println("true");
                        }
                    });

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        } catch (Exception e) {
        }
    }

}
