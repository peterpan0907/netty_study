package netty.nty.tcpsolve;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * 业务处理handler
 */
public class TcpServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        System.out.println("TcpServerHandler 被调用");
        int len = messageProtocol.getLen();
        byte[] b = messageProtocol.getContent();
        String message = new String(b, Charset.forName("UTF-8"));
        System.out.println("从客户端" + channelHandlerContext.channel().remoteAddress() + "读取数据，长度为" + len + ",内容为" + message);

        System.out.println("服务器接收到消息包数量为 + " + (++this.count));
        // 回复消息
        String response = UUID.randomUUID().toString();
        int resLen = response.getBytes("UTF-8").length;
        MessageProtocol protocol = new MessageProtocol(resLen, response.getBytes(Charset.forName("UTF-8")));
        channelHandlerContext.writeAndFlush(protocol);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


}
