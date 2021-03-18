package com.example.market_supervision;

import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.utils.HttpURLConnectionUtil;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login_Submit;

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
        login_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpURLConnectionUtil httpURLConnectionUtil=new HttpURLConnectionUtil();
                String URL="http://192.168.3.77:12345/hello";
                String result=HttpURLConnectionUtil.doGet(URL);
                System.out.println("nihao"+result);
            }
        });
    }
}