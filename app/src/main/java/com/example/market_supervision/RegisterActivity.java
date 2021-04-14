package com.example.market_supervision;

import android.content.Intent;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerUsername;
    private EditText registerPassword;
    private EditText registerPhone;
    private Button registerSubmit;
    private ImageButton registerBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register_step_two);
        registerUsername=findViewById(R.id.et_register_username);
        registerPassword=findViewById(R.id.et_register_pwd_input);
        registerPhone=findViewById(R.id.et_register_phone_input);
        registerSubmit=findViewById(R.id.bt_register_submit);
        registerBack=findViewById(R.id.ib_navigation_back);
        registerSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(registerUsername.getText().toString()==null||"".equals(registerUsername.getText().toString())){
                    Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
                }else if(registerPassword.getText().toString() == null){
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                }else if(registerPhone.getText().toString() == null){
                    Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
                }
                HashMap<String,String> registerInfo=new HashMap<>();
                registerInfo.put("userName",registerUsername.getText().toString());
                registerInfo.put("userPassword",registerPassword.getText().toString());
                registerInfo.put("phoneNo",registerPhone.getText().toString());
                FormBody.Builder builder = new FormBody.Builder();
                for(String key:registerInfo.keySet()){
                    builder.add(key,registerInfo.get(key));
                }
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody=builder.build();
                String loginUrl="http://192.168.3.77:12345/init/register";
                System.out.println(loginUrl);
                Request request=new Request.Builder().url(loginUrl).post(requestBody).build();
                Call call=okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("false+1");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String respInfo= response.body().string();
//                        String loginInfo=response.body().string();
                        System.out.println(respInfo);
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), respInfo, Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                });
            }
        });

        registerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
