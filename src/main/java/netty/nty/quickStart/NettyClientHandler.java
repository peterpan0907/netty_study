package netty.nty.quickStart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 客户端的业务处理器，负责发送数据
 * 注册入客户端的业务处理中
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道就绪时，就可以进行数据的发送
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("A : hello", CharsetUtil.UTF_8));
    }


    /**
     * 当通道有读取事件的时候
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println("服务器端回复的信息是：" + byteBuf.toString(CharsetUtil.UTF_8));

        System.out.println("服务器的地址是" + ctx.channel().remoteAddress());

    }

    /**
     * 处理异常，一般就是关闭通道
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        ctx.close();
    }
}
