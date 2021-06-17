package com.example.market_supervision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.constant.CommonConstant;
import com.example.constant.UserInfo;
import com.example.utils.HttpClientUtil;
import com.example.utils.HttpURLConnectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.utils.SPUtils;
import okhttp3.*;
import okhttp3.internal.http2.Header;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.jetbrains.annotations.NotNull;

import javax.sql.CommonDataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login_Submit;
    private Button login_Register;
    private HashMap<String , Object> hashMapInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        username=findViewById(R.id.et_login_username);
        password=findViewById(R.id.et_login_pwd);
        username.setText((String)SPUtils.get(getApplicationContext(),"username","username"));
        password.setText((String) SPUtils.get(getApplicationContext(),"password","123"));
        login_Submit=findViewById(R.id.bt_login_submit);
        login_Register=findViewById(R.id.bt_login_register);
        System.out.println(SPUtils.get(getApplicationContext(),"username","username"));
        login_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(username.getText().toString()==null||"".equals(username.getText().toString())){
                    Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString() == null){
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                }
                OkHttpClient okHttpClient = new OkHttpClient();
                String loginUrl="http://"+ CommonConstant.srvIp +":12345/market/init/login?username="+username.getText().toString()+"&password="+password.getText().toString();
                System.out.println(loginUrl);
                Request request=new Request.Builder().url(loginUrl).build();
                Call call=okHttpClient.newCall(request);

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("false+1");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                        Boolean respBoolean= Boolean.parseBoolean(response.body().string());
                        //session数据
                        Headers headers=response.headers();
                        List<String> cookies = headers.values("Set-Cookie");
                        String session = cookies.get(0);
                        System.out.println("session:"+session);
                        String COOKIE = session.substring(0, session.indexOf(";"));//截取sessionid
                        System.out.println(COOKIE);
                        SPUtils.put(getApplicationContext(), "session",COOKIE);

                        //登录信息
                        String loginInfo=response.body().string();
                        hashMapInfo=JSON.parseObject(loginInfo, HashMap.class);
//                        System.out.println(SPUtils.get(getApplicationContext(),"userInfo","userInfo"));

                        //数据重写
                        SPUtils.put(getApplicationContext(),"username",username.getText().toString());
                        SPUtils.put(getApplicationContext(),"password",password.getText().toString());
                        Looper.prepare();
                        if(hashMapInfo.get("userInfo")!=null) {
                            System.out.println("hashmap"+hashMapInfo.get("userInfo"));
                            SPUtils.put(getApplicationContext(),"userInfo",hashMapInfo.get("userInfo"));
                            String userInfoSP= (String) SPUtils.get(getApplicationContext(),"userInfo","userinfo");
                            com.example.constant.UserInfo userInfo= JSON.parseObject(userInfoSP, UserInfo.class);
                            SPUtils.put(getApplicationContext(),"userId",userInfo.getUserId());
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                        Toast.makeText(getApplicationContext(), (String) hashMapInfo.get("loginInfo"), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                });

            }
        });

        login_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}