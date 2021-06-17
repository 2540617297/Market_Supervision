package com.example.market_supervision;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MobileLaw extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_law);
    }

    public void onClick(View view) {
        Intent mapLocation=new Intent(MobileLaw.this,MapService.class);
        switch (view.getId()){
            case R.id.Check_list:
                Intent checkList=new Intent(MobileLaw.this,Check_List.class);
                startActivity(checkList);
                break;
            case R.id.Location_Sign:
                System.out.println("startloctionsign");
                startService(mapLocation);
                Toast.makeText(getApplicationContext(), "开始定位，祝工作顺利！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Cancle_Location_Sign:
                stopService(mapLocation);
                Toast.makeText(getApplicationContext(), "定位结束，辛苦了！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.record_question_write:
                Intent recordQuestionWrite=new Intent(MobileLaw.this,RecordQuestion.class);
                startActivity(recordQuestionWrite);
                break;
            case R.id.Spot_Notice:
                Intent spotNotice = new Intent(MobileLaw.this,SpotNotice.class);
                startActivity(spotNotice);
                break;
        }
    }
}