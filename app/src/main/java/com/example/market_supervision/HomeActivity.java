package com.example.market_supervision;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSON;
import com.example.utils.SPUtils;
import websocketclientdemo.MainActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    //UI Object
    private TextView txt_topbar;
    private TextView txt_channel;
    private TextView txt_message;
    private TextView txt_better;
//    private TextView txt_setting;
    private FrameLayout ly_content;

    //Fragment Object
    private MyFragment fg1,fg2,fg3,fg4;
    private WorkContent workContent;
    private UserInfo userInfo;
    private FragmentManager fManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_home);
//        LoginActivity.finsh();
        fManager = getFragmentManager();
        bindViews();
        txt_channel.performClick();   //模拟一次点击，既进去后选择第一项
        if(userInfo!=null) {
            findViewById(R.id.re_myinfo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, UserInfoEdit.class);
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onrestart");
        txt_channel.performClick();
    }

    //UI组件初始化与事件绑定
    private void bindViews() {
        txt_topbar = (TextView) findViewById(R.id.txt_topbar);
        txt_channel = (TextView) findViewById(R.id.txt_channel);
        txt_message = (TextView) findViewById(R.id.txt_message);
        txt_better = (TextView) findViewById(R.id.txt_better);
//        txt_setting = (TextView) findViewById(R.id.txt_setting);
        ly_content = (FrameLayout) findViewById(R.id.ly_content);

        txt_channel.setOnClickListener(this);
        txt_message.setOnClickListener(this);
        txt_better.setOnClickListener(this);
//        txt_setting.setOnClickListener(this);
    }

    //重置所有文本的选中状态
    private void setSelected(){
        txt_channel.setSelected(false);
        txt_message.setSelected(false);
        txt_better.setSelected(false);
//        txt_setting.setSelected(false);
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(workContent != null)fragmentTransaction.hide(workContent);
        if(userInfo != null)fragmentTransaction.hide(userInfo);
        if(fg2 != null)fragmentTransaction.hide(fg2);
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()){
            case R.id.txt_channel:
                setSelected();
                txt_channel.setSelected(true);
                if(workContent == null){
                    workContent = new WorkContent();
                    fTransaction.add(R.id.ly_content,workContent);
                }else{
                    fTransaction.show(workContent);
                }
                break;
            case R.id.txt_message:
                setSelected();
                txt_message.setSelected(true);
                if(fg2 == null){
                    fg2 = new MyFragment("第二个Fragment");
                    fTransaction.add(R.id.ly_content,fg2);
                }else{
                    fTransaction.show(fg2);
                }
                break;
            case R.id.txt_better:
                setSelected();
                txt_better.setSelected(true);
                if(userInfo == null){
                    userInfo = new UserInfo();
                    fTransaction.add(R.id.ly_content,userInfo);
                }else{
                    fTransaction.show(userInfo);
                }
                break;
            case R.id.re_myinfo:
                System.out.println("nihao");
                Intent intent = new Intent(HomeActivity.this, UserInfoEdit.class);
                startActivity(intent);
                break;
            case R.id.back_log:
                System.out.println(SPUtils.get(getApplicationContext(),"session","session"));
                Intent backLog = new Intent(HomeActivity.this, BackLog.class);
                startActivity(backLog);
                break;
            case R.id.task_distribution:
                Intent taskDistribution = new Intent(HomeActivity.this, TaskDistribution.class);
                startActivity(taskDistribution);
                break;
            case R.id.map:
                Intent map = new Intent(HomeActivity.this, Map.class);
                startActivity(map);
                break;
            case R.id.Mobile_Law:
                Intent mobileLaw = new Intent(HomeActivity.this,MobileLaw.class);
                startActivity(mobileLaw);
                break;
            case R.id.chat:
                Intent userSearch=new Intent(HomeActivity.this, UserSearch.class);
                startActivity(userSearch);
                break;
            case R.id.synthesize_query:
                Intent query=new Intent(HomeActivity.this,SynthesizeQuery.class);
                startActivity(query);
                break;
            case R.id.Patrol_Record:
                Intent patrolRecord=new Intent(HomeActivity.this,PatrolRecord.class);
                startActivity(patrolRecord);
                break;
            case R.id.service:
                Intent service=new Intent(HomeActivity.this,ServiceShowList.class);
                startActivity(service);
                break;
        }
        fTransaction.commit();
    }
}
