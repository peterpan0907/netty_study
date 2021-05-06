package netty.nty.dobborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.dobborpc.provider.HelloServiceImpl;
import netty.dobborpc.publicinterface.Common;

/**
 * 业务处理
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取客户端发送的消息并调用我们的服务
        System.out.println("msg = " + msg);

        // 客户端调用服务器端的api时候，我们需要定义一个协议
        // 比如我们要求每次发送消息都是必须以某个字符串开头，"PMLISCOLL"
        if (msg.toString().startsWith(Common.pre)) {
            String hello = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().indexOf(Common.pre) + Common.pre.length()));
            ctx.writeAndFlush(hello);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
