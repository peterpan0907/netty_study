package netty.nty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 业务处理handler
 */
public class HandlerServerHandler extends SimpleChannelInboundHandler<Long> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) throws Exception {
        System.out.println("HandlerServerHandler 被调用");
        System.out.println("从客户端" + channelHandlerContext.channel().remoteAddress() + "读取一个数据，数据为" + aLong);

        // 给客户端回复一个long
        channelHandlerContext.writeAndFlush(98765L);

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
