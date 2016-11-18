package com.chen.nettyclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    
    private Button mBtnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnTest = (Button) findViewById(R.id.btn_main_test);
        mBtnTest.setOnClickListener(this);
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_test:
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
                
                break;
            default:
                break;
        }
    }
}
