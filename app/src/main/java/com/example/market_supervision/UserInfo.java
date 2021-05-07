package com.example.market_supervision;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.example.utils.SPUtils;

@SuppressLint("ValidFragment")
public class UserInfo extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_user_info,container,false);
        String userInfoSP= (String) SPUtils.get(getActivity().getApplicationContext(),"userInfo","userinfo");
        com.example.constant.UserInfo userInfos= JSON.parseObject(userInfoSP, com.example.constant.UserInfo.class);
        System.out.println(userInfos);
        TextView userName=(TextView)view.findViewById(R.id.yonghuxingming);
        userName.setText(userInfos.getUserName());
        TextView userId=(TextView)view.findViewById(R.id.dengruyonghu);
        userId.setText(userInfos.getUserId());
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {

    }
}
