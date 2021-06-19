package com.example.market_supervision;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class StatisticsForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_form);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Form_Check_List:
                Intent formCheck=new Intent(StatisticsForm.this,Lists_Checks.class);
                startActivity(formCheck);
                break;
            case R.id.record_question_list:
                Intent rqList=new Intent(StatisticsForm.this,RecordQuestionList.class);
                startActivity(rqList);
                break;
            case R.id.SF_SpotNotice:
                Intent spotNotice=new Intent(StatisticsForm.this,SpotNoticeList.class);
                startActivity(spotNotice);
                break;
            case R.id.IAApply:
                Intent iAApply=new Intent(StatisticsForm.this,IAList.class);
                startActivity(iAApply);
                break;
        }
    }
}