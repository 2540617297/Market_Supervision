package com.example.market_supervision;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SynthesizeQuery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synthesize_query);
    }

    public void onClick(View view) {
        switch (view.getId()){

            case R.id.EnterPriseQuery:
                Intent enterPriseQuery=new Intent(SynthesizeQuery.this,EnterPriseList.class);
                startActivity(enterPriseQuery);
                break;
            case R.id.Patrol_Record:
                Intent patrolRecord=new Intent(SynthesizeQuery.this,PatrolRecord.class);
                startActivity(patrolRecord);
                break;
            case R.id.map:
                Intent map = new Intent(SynthesizeQuery.this, Map.class);
                startActivity(map);
                break;
        }
    }
}