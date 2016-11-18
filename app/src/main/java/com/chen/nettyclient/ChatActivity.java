package com.chen.nettyclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.chen.nettyclient.adapter.MessageAdapter;
import com.chen.nettyclient.chat.ConnectManager;
import com.chen.nettyclient.entity.BaseMessage;
import com.chen.nettyclient.type.MsgDirect;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ChatActivity";
    
    private ListView mListView;
    private EditText mEtContent;
    private Button mBtnSend;
    
    private List<BaseMessage> mMsgListData;
    private MessageAdapter mAdapter;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initViews();
        initData();
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.lv_chat_msg);
        mEtContent = (EditText) findViewById(R.id.et_chat_content);
        mBtnSend = (Button) findViewById(R.id.btn_chat_send);
        mBtnSend.setOnClickListener(this);
    }


    private void initData() {
        mMsgListData = new ArrayList<BaseMessage>();
        mAdapter = new MessageAdapter(this,mMsgListData);
        mListView.setAdapter(mAdapter);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_chat_send:
                String content = mEtContent.getText().toString();
                sendTextMsg(content);
                mEtContent.setText("");
                break;
            default:
                break;
        }
    }
    
    private void sendTextMsg(String content){
        Log.i(TAG,"将要发送的信息为："+content);
        BaseMessage message = new BaseMessage();
        message.setMsg_direct(MsgDirect.SEND.getIndex());
        message.setContent(content);
        addMsg(message);
        ConnectManager.getInstance().sendMessage(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    
    
    @Subscribe
    public void onEventMainThread(String rev){
        BaseMessage message = new BaseMessage();
        message.setMsg_direct(MsgDirect.RECEIVE.getIndex());
        message.setContent(rev);
        addMsg(message);
    }
    
    private void addMsg(BaseMessage message){
        mMsgListData.add(message);
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mMsgListData.size()-1);
    }
}
