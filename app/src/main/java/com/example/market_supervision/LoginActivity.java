package com.example.market_supervision;

import android.content.Intent;
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
import com.example.utils.HttpClientUtil;
import com.example.utils.HttpURLConnectionUtil;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login_Submit;
    private Button login_Register;

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
        login_Submit=findViewById(R.id.bt_login_submit);
        login_Register=findViewById(R.id.bt_login_register);
        login_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(username.getText().toString()==null||"".equals(username.getText().toString())){
                    Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString() == null){
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                }
                OkHttpClient okHttpClient = new OkHttpClient();
                String loginUrl="http://192.168.3.77:12345/init/login?username="+username.getText().toString()+"&password="+password.getText().toString();
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
                        String loginInfo=response.body().string();
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), loginInfo, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
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