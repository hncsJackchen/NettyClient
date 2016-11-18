package com.chen.nettyclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.chen.nettyclient.R;
import com.chen.nettyclient.entity.BaseMessage;
import com.chen.nettyclient.type.MsgDirect;
import java.util.List;

/**
 * Author： Jackchen
 * Time： 2016/11/18
 * Description:消息适配器
 */
public class MessageAdapter extends BaseAdapter {
    private Context mContext;
    private List<BaseMessage> mDatas;

    public MessageAdapter(Context context, List<BaseMessage> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_msg_receive, null);
            holder.mTvContent = (TextView) convertView.findViewById(R.id.tv_item_msg_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }*/

        //初始化控件
        BaseMessage msg = mDatas.get(position);
        if(msg.getMsg_direct() == MsgDirect.RECEIVE.getIndex()){
            //接收消息
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_msg_receive, null);
        }else {
            //发送消息
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_msg_send, null);
        }
        ViewHolder holder = new ViewHolder();
        holder.mTvContent = (TextView) convertView.findViewById(R.id.tv_item_msg_content);
        
        //初始化数据
        String content = msg.getContent();
        if (content != null) {
            holder.mTvContent.setText(content);
        }

        return convertView;
    }

    class ViewHolder {
        TextView mTvContent;
    }
}
