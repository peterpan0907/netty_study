package netty.nty.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 客户端的业务处理handler
 */
public class HandlerClientHandler extends SimpleChannelInboundHandler<Long> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) throws Exception {
        System.out.println("从服务器端" + channelHandlerContext.channel().remoteAddress() + "收到数据，数据为" + aLong);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("HandlerClientHandler 发送数据...");
        // 直接发送一个long，decoder之后调用一次。编码器会调用
        ctx.writeAndFlush(123456789L);

        // 发送，decoder之后调用多次，编码器不会被调用
        // 分析：
        // 1、abcdefghijklmnop 是16个字节
        // 2、pipeline该handler的前一个就是HandlerClientEncoder
        // 3、HandlerClientEncoder 的父类 是 MessageToByteEncode
        // 4、这里面会判断是否是自己支持的数据，若是，才会调用encode，复杂不会调用
//        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdefghijklmnop", CharsetUtil.UTF_8));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
