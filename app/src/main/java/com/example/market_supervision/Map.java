package com.example.market_supervision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.LinearInterpolator;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.*;
import com.amap.api.maps.model.*;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.example.constant.CommonConstant;
import com.example.constant.RouteInfo;
import com.example.constant.UserInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Map extends AppCompatActivity implements AMapLocationListener{

    MapView mMapView = null;
    MyLocationStyle myLocationStyle;
    //初始化地图控制器对象
    AMap aMap;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    public AMapLocationClientOption mLocationOption = null;//声明AMapLocationClientOption对象
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        //设置地图的放缩级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        //显示定位蓝点
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(10000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        myLocationStyle.showMyLocation(false);
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
//        aMap.setLocationSource(this);//通过aMap对象设置定位数据源的监听
        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮
        mUiSettings.setCompassEnabled(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        //"权限已申请";
        showLocation();

//        //设置中心点和缩放比例

        List<LatLng> latLngs=new ArrayList<>();
        Intent intent=getIntent();
        String routeId=intent.getStringExtra("routeId");
        if(routeId!=null) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
                        .readTimeout(60000, TimeUnit.MILLISECONDS)
                        .build();
                String loginUrl = "http://" + CommonConstant.srvIp + ":12345/market/mobileLaw/getLatLng?routeId=" + routeId.substring(1);
                System.out.println(loginUrl);
                Request request = new Request.Builder().url(loginUrl).build();
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    //登录信息
                    String getRouteInfo = response.body().string();
                    JSONArray jsonArray = JSON.parseArray(getRouteInfo);
                    for (Object j :
                            jsonArray) {
                        RouteInfo routeInfo = JSONObject.parseObject(JSON.toJSONString(j), RouteInfo.class);
                        latLngs.add(new LatLng(routeInfo.getmLatitude(), routeInfo.getmLongitude()));
                        addMarkers(routeInfo.getmLatitude(), routeInfo.getmLongitude());
                        System.out.println(routeInfo);
                    }
                    if (latLngs != null) {
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLngs.get(0)));
                    }
                    aMap.setMapTextZIndex(2);
                    aMap.addPolyline((new PolylineOptions())
                            //手动数据测试
                            //.add(new LatLng(31.755090213041477, 117.25183707934592),new LatLng(31.753250606283274,117.25086836479817),new LatLng(31.751155, 117.254453))
                            //集合数据
                            .addAll(latLngs)
                            //线的宽度
                            .width(10).setDottedLine(true).geodesic(true)
                            //颜色
                            .color(Color.argb(255, 255, 20, 147)));
                    Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
                } else {
                    throw new IOException("Unexpected code " + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        latLngs.add(new LatLng(31.755090213041477, 117.25183707934592));
//        latLngs.add(new LatLng(31.753250606283274,117.25086836479817));
//        latLngs.add(new LatLng(31.751155, 117.254453));
//        addMarkers(31.755090213041477, 117.25183707934592);
//        addMarkers(31.753250606283274, 117.25086836479817);
//        addMarkers(31.751155, 117.254453);

//        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(0), 7));
//        LatLng marker1 = new LatLng(31.755090213041477, 117.25183707934592);



    }

    private void addMarkers(Double latitude,Double longitude) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.anchor(1.3f, 1.5f);//点标记的锚点
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.position);
        markerOptions.icon(BitmapDescriptorFactory
                .fromBitmap(bitmap));
        markerOptions.position(new LatLng(latitude, longitude));
        Marker growMarker = aMap.addMarker(markerOptions);
        growMarker.setClickable(true); //marker 设置是否可点击
        startGrowAnimation(growMarker);
        growMarker.showInfoWindow();
    }

    private void startGrowAnimation(Marker marker) {

        if (marker != null) {
            Animation animation = new ScaleAnimation(0, 1, 0, 1);
            animation.setInterpolator(new LinearInterpolator());
            //整个移动所需要的时间
            animation.setDuration(1000);
            animation.setFillMode(1);//动画保存之前的状态为1 之后为0
            //设置动画
            marker.setAnimation(animation);
            //开始动画
            marker.startAnimation();
            marker.showInfoWindow();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent mapBack=new Intent(Map.this,HomeActivity.class);
            startActivity(mapBack);
//            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
                    // 停止定位
//                    mLocationClient.stopLocation();
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
