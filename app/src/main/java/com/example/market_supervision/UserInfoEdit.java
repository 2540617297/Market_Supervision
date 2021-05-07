package com.example.market_supervision;

import android.content.Intent;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.alibaba.fastjson.JSON;
import com.example.constant.CommonConstant;
import com.example.constant.UserInfo;
import com.example.utils.SPUtils;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class UserInfoEdit extends AppCompatActivity {

    EditText userName;
    EditText userPassword;
    EditText email;
    EditText roleId;
    EditText phoneNo;
    EditText userNameCN;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserInfoEdit.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        userName=findViewById(R.id.tv_name);
        userPassword=findViewById(R.id.tv_password);
        email=findViewById(R.id.tv_youxiang);
        roleId=findViewById(R.id.tv_role);
        userNameCN=findViewById(R.id.tv_CN);
        phoneNo=findViewById(R.id.tv_dianhua);

        String userInfoSP= (String) SPUtils.get(getApplicationContext(),"userInfo","userinfo");
        System.out.println("userinfoSp"+userInfoSP);
        com.example.constant.UserInfo userInfo= JSON.parseObject(userInfoSP, UserInfo.class);
        userName.setText(userInfo.getUserName());
        userPassword.setText(userInfo.getUserPassword());
        email.setText(userInfo.getEmail());
        if(userInfo.getRoleId().equals("1")) {
            roleId.setText("管理员");
        }else{
            roleId.setText("用户");
        }
        userNameCN.setText(userInfo.getUserNameCN());
        phoneNo.setText(userInfo.getPhoneNo());
        submit=findViewById(R.id.baocun);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> routInfo=new HashMap<>();
                routInfo.put("userName",userName.getText().toString());
                routInfo.put("userPassword",userPassword.getText().toString());
                routInfo.put("email",email.getText().toString());
                routInfo.put("roleId",roleId.getText().toString());
                routInfo.put("userNameCN",userNameCN.getText().toString());
                routInfo.put("phoneNo",phoneNo.getText().toString());
                String userId= (String) SPUtils.get(getApplicationContext(),"userId","1");
                routInfo.put("userId",userId);
                FormBody.Builder builder = new FormBody.Builder();
                for (String key : routInfo.keySet()) {
                    //追加表单信息
                    builder.add(key, routInfo.get(key));
                }
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody formBody=builder.build();
                String userInfoSP= (String) SPUtils.get(getApplicationContext(),"userInfo","userinfo");
                com.example.constant.UserInfo userInfo= JSON.parseObject(userInfoSP, UserInfo.class);
                String url="http://"+ CommonConstant.srvIp +":12345/market/init/updateUserInfo?power="+userInfo.getRoleId();
                System.out.println(url);
                Request request=new Request.Builder().url(url).post(formBody).build();
                Call call=okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("false+1");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String respInfo= response.body().string();
                        System.out.println(respInfo);
                        if(respInfo.equals("修改信息成功！")){
                            UserInfo updateUserInfo=new UserInfo();
                            updateUserInfo.setUserName(userName.getText().toString());
                            updateUserInfo.setUserPassword(userPassword.getText().toString());
                            updateUserInfo.setEmail(email.getText().toString());
                            if (roleId.getText().toString().equals("用户")) {
                                updateUserInfo.setRoleId("0");
                            } else {
                                updateUserInfo.setRoleId("1");
                            }
                            updateUserInfo.setUserNameCN(userNameCN.getText().toString());
                            updateUserInfo.setPhoneNo(phoneNo.getText().toString());
                            String userId= (String) SPUtils.get(getApplicationContext(),"userId","1");
                            updateUserInfo.setUserId(userId);
                            SPUtils.put(getApplicationContext(),"userInfo",JSON.toJSONString(updateUserInfo));
                            System.out.println(updateUserInfo.toString());
                        }
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), respInfo, Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                });
            }
        });
    }
}