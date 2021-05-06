package netty.nty.tcp;

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
public class TcpServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf msg) throws Exception {
        System.out.println("TcpServerHandler 被调用");
        byte[] b = new byte[msg.readableBytes()];
        msg.readBytes(b);

        String message = new String(b, Charset.forName("UTF-8"));
        System.out.println("从客户端" + channelHandlerContext.channel().remoteAddress() + "读取数据为" + message);

        System.out.println("服务器接收到消息量为：" + (++this.count));

        // 回复消息
        ByteBuf byteBuf = Unpooled.copiedBuffer(UUID.randomUUID().toString(), CharsetUtil.UTF_8);
        channelHandlerContext.channel().writeAndFlush(byteBuf);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
