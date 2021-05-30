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
        }
    }
}