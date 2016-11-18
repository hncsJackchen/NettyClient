package com.chen.nettyclient.chat;

import android.util.Log;
import com.chen.nettyclient.ClientConfig;
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

/**
 * Author： Jackchen
 * Time： 2016/11/18
 * Description:连接服务器并进行交互的管理类
 */
public class ConnectManager extends Thread {
    private static final String TAG = "ConnectManager";
    private static ConnectManager instance;

    //Netty变量
    private ClientBootstrap clientBootstrap;
    private ChannelFactory channelFactory;
    private ChannelFuture channelFuture = null;
    private Channel channel = null;

    //服务参数
    private String strHost = null;
    private int nPort = 0;

    /**
     * @return
     */
    public static ConnectManager getInstance() {
        if (instance == null) {
            synchronized (ConnectManager.class) {
                if (instance == null) instance = new ConnectManager();
            }
        }
        return instance;
    }

    //构造函数
    private ConnectManager() {
        this.strHost = ClientConfig.IP;
        this.nPort = ClientConfig.PORT;
        init(new MsgHandler());
    }

    /**
     * 初始化变量
     *
     * @param handler
     */
    private void init(final SimpleChannelHandler handler) {
        channelFactory = new NioClientSocketChannelFactory(Executors.newSingleThreadExecutor(),
            Executors.newSingleThreadExecutor());
        clientBootstrap = new ClientBootstrap(channelFactory);
        clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new MsgHandler());
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
            if ((channel == null || (channel != null && !channel.isConnected()))
                && null != this.strHost
                && this.nPort > 0) {
                // Start the connection attempt.
                channelFuture = clientBootstrap.connect(new InetSocketAddress(strHost, nPort));
                // Wait until the connection attempt succeeds or fails.
                channel = channelFuture.awaitUninterruptibly().getChannel();
                if (!channelFuture.isSuccess()) {
                    channelFuture.getCause().printStackTrace();
                    clientBootstrap.releaseExternalResources();
                    //TODO  连接失败
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

    /**
     * 发送消息
     * @param msg 要发送的消息
     * @return
     */
    public boolean sendMessage(BaseMessage msg) {
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(msg.getContent().getBytes());
        if (null != buffer && null != channelFuture.getChannel()) {
            /**底层的状态要提前判断，netty抛出的异常上层catch不到*/
            Channel currentChannel = channelFuture.getChannel();
            boolean isW = currentChannel.isWritable();
            boolean isC = currentChannel.isConnected();
            if (!(isW && isC)) {
                Log.i(TAG, "sendMessage 4");
                throw new RuntimeException("#sendRequest#channel is close!");
            }
            channelFuture.getChannel().write(buffer);
            Log.i(TAG, "sendMessage ok");
            return true;
        } else {
            Log.i(TAG, "sendMessage failed");
            return false;
        }
    }
}
