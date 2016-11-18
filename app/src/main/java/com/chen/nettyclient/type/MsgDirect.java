package com.chen.nettyclient.type;

/**
 * Author： Jackchen
 * Time： 2016/11/18
 * Description:消息方向
 */
public enum  MsgDirect {
    RECEIVE(0,"接收"),SEND(1,"发送");
    private int index;
    private String info;

    MsgDirect(int index, String info) {
        this.index = index;
        this.info = info;
    }

    public int getIndex() {
        return index;
    }

    public String getInfo() {
        return info;
    }
}
