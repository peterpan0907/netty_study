package netty.nty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * 客户端的业务处理handler
 */
public class TcpClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        byte[] b = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(b);

        String message = new String(b, Charset.forName("UTF-8"));

        System.out.println("客户端从服务器端" + channelHandlerContext.channel().remoteAddress() + "收到数据，数据为" + message);
        System.out.println("客户端接收到消息量为：" + (++this.count));

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("TcpClientHandler 发送数据...");
        // 使用客户端发送10 条数据 hello server
        for (int i = 0; i < 10; i++) {
            ByteBuf byteBuf = Unpooled.copiedBuffer("hellp server " + i, CharsetUtil.UTF_8);
            // 直接发送一个long，decoder之后调用一次。编码器会调用
            ctx.writeAndFlush(byteBuf);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
