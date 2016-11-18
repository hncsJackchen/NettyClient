package com.chen.nettyclient.chat;

import android.util.Log;
import org.greenrobot.eventbus.EventBus;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * Author： Jackchen
 * Time： 2016/11/18
 * Description:
 */
public class MsgHandler extends SimpleChannelHandler {

    private static final String TAG = "MsgHandler";

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelConnected(ctx, e);
        Log.i(TAG, "channelConnected");
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
        throws Exception {
        super.channelDisconnected(ctx, e);
        Log.i(TAG, "channelDisconnected");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Log.i(TAG, "messageReceived 接收到服务器的消息...");
        super.messageReceived(ctx, e);
        ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
        byte[] b = new byte[0];
        if (buffer != null) {
            buf.writeBytes(buffer);
            b = new byte[buf.readableBytes()];
            buf.readBytes(b);
            String result = new String(b);
            EventBus.getDefault().post(result);
            Log.i(TAG, Thread.currentThread().getName() + "--接收到服务器的消息..." + result);
        } else {
            Log.i(TAG, Thread.currentThread().getName() + "--接收到服务器的消息...buffer==null");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        super.exceptionCaught(ctx, e);
        Log.i(TAG, "exceptionCaught");
    }
}
