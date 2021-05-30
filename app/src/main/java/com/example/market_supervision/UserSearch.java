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
import java.util.concurrent.TimeUnit;

public class UserSearch extends AppCompatActivity {
    EditText userSearchContext;
    Button searchBtn;
    ListView lvResults;
    List<UserInfo > userInfos=new ArrayList<>();
    List<HashMap<String, Object>> listitem;
    ListView listView;
    SimpleAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }



        lvResults=findViewById(R.id.search_result);
        listView = findViewById(R.id.search_result);
        searchBtn=findViewById(R.id.search_btn);
        userSearchContext=findViewById(R.id.search_et_input);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> title=new ArrayList<>();
                ArrayList<String> id=new ArrayList<>();
                ArrayList<Integer> head=new ArrayList<>();
                try {
                    if(myAdapter!=null) {
                        listitem = null;
                        System.out.println(listitem);
                        myAdapter.notifyDataSetChanged();
                        listView.setAdapter(myAdapter);
                    }
                    OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
                            .readTimeout(60000, TimeUnit.MILLISECONDS)
                            .build();
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
                            userInfos.add(userInfo);
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
                        myAdapter = new SimpleAdapter(getApplicationContext(), listitem, R.layout.item_bean_list, new String[]{"imgtou", "search_title", "search_content"}, new int[]{R.id.imgtou, R.id.search_title, R.id.search_content});
//                        listView = findViewById(R.id.search_result);
                        myAdapter.notifyDataSetChanged();
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
        ListView listView = findViewById(R.id.search_result);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView)parent;
                HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);
                String userid = map.get("search_content");
                String name = map.get("search_title");
                System.out.println(userid+":"+name);
                System.out.println("onldjvosdivnovnsoevnien************");
                Intent intent=new Intent(UserSearch.this,SendUserInfo.class);
                for (UserInfo u:userInfos){
                    if(u.getUserId()==userid){
                        intent.putExtra("userId",u.getUserId());
                        intent.putExtra("userName",u.getUserName());
                        intent.putExtra("phone",u.getPhoneNo());
                        intent.putExtra("userNameCN",u.getUserNameCN());
                    }
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }
}