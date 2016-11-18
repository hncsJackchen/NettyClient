package com.chen.nettyclient.entity;

import com.chen.nettyclient.type.MsgDirect;

/**
 * Author： Jackchen
 * Time： 2016/11/18
 * Description:消息实体类
 */
public class BaseMessage {
    //发送或接收的时间
    private long msg_time;
    //消息内容
    private String content;
    //消息类型 0-文字
    private int msg_type;
    //消息方向 0-发送，1-接收
    private int msg_direct;

    {
        this.msg_time = System.currentTimeMillis();
        this.content = "";
        this.msg_type = 0;
        this.msg_direct = MsgDirect.SEND.getIndex();
    }

    public long getMsg_time() {
        return msg_time;
    }

    public void setMsg_time(long msg_time) {
        this.msg_time = msg_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public int getMsg_direct() {
        return msg_direct;
    }

    public void setMsg_direct(int msg_direct) {
        this.msg_direct = msg_direct;
    }

    @Override
    public String toString() {
        return "BaseMessage{" +
            "msg_time=" + msg_time +
            ", content='" + content + '\'' +
            ", msg_type=" + msg_type +
            ", msg_direct=" + msg_direct +
            '}';
    }
}
