package netty.nty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

/**
 * 服务器端的业务处理器
 */
public class GroupServerHandler extends SimpleChannelInboundHandler<String> {

    // 定义一个 channel 组 ， 管理所有的channel
    // GlobalEventExecutor 是一个全局的事件执行器，是一个单例
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    // 处理时间
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    // 表示连接建立，一旦连接，第一个被执行
    // 将当前的channel加入到 线程组里面去
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        System.out.println("客户端" + channel.remoteAddress() + "进行连接了...");

        // 将该客户端加入聊天的信息推送给在线的客户端
        // writeAndFlush该方法，会将所有的channel遍历并发送消息，这样的话，我们不需要进行遍历
        // 以前的话，需要自己遍历，这里的channelGroup的一个方法直接搞定
        channels.writeAndFlush("客户端" + channel.remoteAddress() + "加入聊天\n");

        // 加入到线程组里面去
        channels.add(channel);

    }

    //表示channel处于一个活动的状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        System.out.println("客户端" + channel.remoteAddress() + "上线了...");
    }

    //表示channel处于不活动的状态，提示xx离线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("客户端" + channel.remoteAddress() + "离线了...");
    }


    // 断开连接，这里的channel会自动从channels里面去除了
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("客户端" + channel.remoteAddress() + "断开连接了...");
        // 将某个客户端离线的信息推送给所有的客户端
        channels.writeAndFlush("客户端" + channel.remoteAddress() + "断开连接了\n");
    }

    // 读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        // 获取到当前的channel
        Channel channel = channelHandlerContext.channel();

        // 这里我们针对发送的客户端回送一条消息，针对其他客户端发送其他的消息
        // 所以我们这边遍历进行两种情况的实现

        channels.forEach((c) -> {
            // 如果不是当前的客户端channel，进行转发消息
            if (c != channel) {
                c.writeAndFlush("客户" + channel.remoteAddress() + "发送了消息，消息是：" + s + "\n");
            } else {
                c.writeAndFlush("自己发送了消息给自己，消息是：" + s + "\n");
            }
        });


    }

    // 发生异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        channels.close();
        ctx.close();
    }
}
