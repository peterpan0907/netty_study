package netty.nty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务器端若实现私聊，其业务处理器
 */
public class GroupServerToOneHandler extends SimpleChannelInboundHandler<String[]> {


    // 定义一个map，存放上线的channel
    private static Map<Integer, Channel> channelMap = new HashMap<>();

    private static int id = 0;

    // 表示连接建立，一旦连接，第一个被执行
    // 将当前的channel加入到 线程组里面去
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        System.out.println("客户端" + channel.remoteAddress() + "进行连接了...");

        channelMap.put(id++, channel);

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
    }

    // 读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String[] s) throws Exception {
        // 获取到当前的channel
        Channel channel = channelHandlerContext.channel();

        // 找到要私聊的人，然后直接进行消息的传送
        // 传入的s[0]是要发送消息给谁，也就是id
        // 传入的s[1]是发送的消息

        if (s.length != 2) {
            return;
        }
        Integer idd = Integer.parseInt(s[0]);

        // 得到要私聊的人
        Channel toChannel = channelMap.get(idd);

        if (toChannel != null) {
            toChannel.writeAndFlush("客户" + channel.remoteAddress() + "发送了消息，消息是：" + s[1] + "\n");
        }
    }

    // 发生异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
