package com.example.market_supervision;

import android.content.Intent;
import android.os.Looper;
import android.os.StrictMode;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.constant.CommonConstant;
import com.example.constant.UserInfo;
import com.example.utils.SPUtils;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserSearch extends AppCompatActivity {
    EditText userSearchContext;
    Button searchBtn;
    ListView lvResults;
    HashMap<String,Object> hashMapInfo;
    List<HashMap<String, Object>> listitem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final ArrayList<String> title=new ArrayList<>();
        final ArrayList<String> id=new ArrayList<>();
        final ArrayList<Integer> head=new ArrayList<>();

        lvResults=findViewById(R.id.search_result);
        searchBtn=findViewById(R.id.search_btn);
        userSearchContext=findViewById(R.id.search_et_input);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    String loginUrl="http://"+ CommonConstant.srvIp +":12345/market/init/searchUser?search="+userSearchContext.getText().toString();
                    System.out.println(loginUrl);
                    Request request=new Request.Builder().url(loginUrl).build();
                    Response response=okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        //登录信息
                        String loginInfo=response.body().string();
                        System.out.println(loginInfo);
//                        System.out.println(SPUtils.get(getApplicationContext(),"userInfo","userInfo"));
                        JSONArray jsonArray = JSON.parseArray(loginInfo);
                        for (Object j :
                                jsonArray) {
                            UserInfo userInfo=JSONObject.parseObject(JSON.toJSONString(j),UserInfo.class);
                            title.add(userInfo.getUserName());
                            id.add(userInfo.getUserId());
                            head.add(R.mipmap.head2);
                        }
                        listitem = new ArrayList<HashMap<String, Object>>();
                        for (int i = 0; i < title.size(); i++) {
                            HashMap<String, Object> showitem = new HashMap<String, Object>();
                            showitem.put("search_title", title.get(i));
                            showitem.put("search_content", id.get(i));
                            showitem.put("imgtou", head.get(i));
                            listitem.add(showitem);
                        }
                        SimpleAdapter myAdapter = new SimpleAdapter(getApplicationContext(), listitem, R.layout.item_bean_list, new String[]{"imgtou", "search_title", "search_content"}, new int[]{R.id.imgtou, R.id.search_title, R.id.search_content});
                        ListView listView = findViewById(R.id.search_result);
                        System.out.println(myAdapter);
                        listView.setAdapter(myAdapter);
                        Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}