package com.example.market_supervision;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.utils.SPUtils;
import websocketclientdemo.MainActivity;

public class SendUserInfo extends AppCompatActivity {
    TextView tuserId;
    TextView tuserName;
    TextView tphone;
    TextView tuserNameCN;
    Button sendChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_user_info);
        Intent intent=getIntent();
        final String userId=intent.getStringExtra("userId");
        String userName=intent.getStringExtra("userName");
        String phone=intent.getStringExtra("phone");
        final String userNameCN=intent.getStringExtra("userNameCN");

        tuserId=findViewById(R.id.send_userId);
        tuserName=findViewById(R.id.send_userName);
        tphone=findViewById(R.id.send_phone);
        tuserNameCN=findViewById(R.id.send_userNameCN);
        sendChat=findViewById(R.id.send_chat);

        tuserId.setText(userId);
        tuserName.setText(userName);
        tphone.setText(phone);
        tuserNameCN.setText(userNameCN);
        sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userChat=new Intent(SendUserInfo.this, MainActivity.class);
                userChat.putExtra("userId",userId);
                userChat.putExtra("userNameCN",userNameCN);
                SPUtils.remove(getApplicationContext(),"sendUserId");
                SPUtils.put(getApplicationContext(),"sendUserId",userId);
                System.out.println("usernameid"+userId);
                startActivity(userChat);
            }
        });
    }
}