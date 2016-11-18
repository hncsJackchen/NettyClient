package com.chen.nettyclient.chat;

import android.util.Log;
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
        super.messageReceived(ctx, e);
        Log.i(TAG, "messageReceived");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        super.exceptionCaught(ctx, e);
        Log.i(TAG, "exceptionCaught");
    }
}
