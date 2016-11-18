package com.chen.nettyclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.chen.nettyclient.chat.ConnectManager;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private Button mBtnConn;
    private Button mBtnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnConn = (Button) findViewById(R.id.btn_main_conn);
        mBtnConn.setOnClickListener(this);
        mBtnTest = (Button) findViewById(R.id.btn_main_test);
        mBtnTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_conn:
                Toast.makeText(this, "开始连接服务器", Toast.LENGTH_SHORT).show();
                ConnectManager.getInstance().start();
                break;
            case R.id.btn_main_test:
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
