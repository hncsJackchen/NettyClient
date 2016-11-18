package com.chen.nettyclient.chat;

import android.util.Log;
import com.chen.nettyclient.entity.BaseMessage;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

/**
 * Author： Jackchen
 * Time： 2016/11/18
 * Description:
 */
public class ConnectManager extends Thread {
    private static final String TAG = "ConnectManager";
    private static ConnectManager instance;

    private ClientBootstrap clientBootstrap;
    private ChannelFactory channelFactory;
    private ChannelFuture channelFuture = null;
    private Channel channel = null;

    private String strHost = null;
    private int nPort = 0;

    public static ConnectManager getInstance() {
        if (instance == null) {
            synchronized (ConnectManager.class) {
                if (instance == null) instance = new ConnectManager();
            }
        }
        return instance;
    }

    private ConnectManager() {
        this.strHost = "192.168.1.197";
        this.nPort = 5001;
        init(new MsgHandler());
    }

    private void init(final SimpleChannelHandler handler) {
        channelFactory = new NioClientSocketChannelFactory(Executors.newSingleThreadExecutor(),
            Executors.newSingleThreadExecutor());

        clientBootstrap = new ClientBootstrap(channelFactory);
        clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                // 接收的数据包解码

                pipeline.addLast("decoder",
                    new LengthFieldBasedFrameDecoder(400 * 1024, 0, 2, -2, 0));
                // 发送的数据包编码
                //pipeline.addLast("encoder", new PacketEncoder());
                // 具体的业务处理，这个handler只负责接收数据，并传递给dispatcher
                pipeline.addLast("handler", handler);
                return pipeline;
            }
        });

        clientBootstrap.setOption("tcpNoDelay", true);
        clientBootstrap.setOption("keepAlive", true);
        clientBootstrap.setOption("connectTimeoutMillis", 5000);

        // clientBootstrap.setOption("keepIdle", 20);
        // clientBootstrap.setOption("keepInterval", 5);
        // clientBootstrap.setOption("keepCount", 3);
    }

    @Override
    public void run() {
        doConnect();
    }

    private boolean doConnect() {
        try {
            if ((null == channel || (null != channel && !channel.isConnected()))
                && null != this.strHost
                && this.nPort > 0) {
                // Start the connection attempt.
                channelFuture = clientBootstrap.connect(new InetSocketAddress(strHost, nPort));
                // Wait until the connection attempt succeeds or fails.
                channel = channelFuture.awaitUninterruptibly().getChannel();
                if (!channelFuture.isSuccess()) {
                    channelFuture.getCause().printStackTrace();
                    clientBootstrap.releaseExternalResources();

                    //IMReconnectManager.instance().setOnRecconnecting(false);
                    return false;
                }
            }

            // Wait until the connection is closed or the connection attemp
            // fails.
            channelFuture.getChannel().getCloseFuture().awaitUninterruptibly();
            // Shut down thread pools to exit.
            clientBootstrap.releaseExternalResources();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "do connect failed. e:" + e.getStackTrace().toString());
            //IMSocketManager.instance().triggerEvent(SocketEvent.CONNECT_MSG_SERVER_FAILED);
            return false;
        }
    }

    /////////////////////////
    public boolean sendMessage(BaseMessage msg) {
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(msg.getContent().getBytes());
        Log.i(TAG,"sendMessage 1");

        if (null != buffer && null != channelFuture.getChannel()) {
            Log.i(TAG,"sendMessage 2");
            /**底层的状态要提前判断，netty抛出的异常上层catch不到*/
            Channel currentChannel = channelFuture.getChannel();
            boolean isW = currentChannel.isWritable();
            boolean isC = currentChannel.isConnected();
            if (!(isW && isC)) {
                Log.i(TAG,"sendMessage 4");
                throw new RuntimeException("#sendRequest#channel is close!");
            }
            Log.i(TAG,"sendMessage 5");
            channelFuture.getChannel().write(buffer);
            Log.i(TAG, "packet#send ok");
            return true;
        } else {
            Log.i(TAG, "packet#send failed");
            Log.i(TAG,"sendMessage 2");
            return false;
        }
    }
}
