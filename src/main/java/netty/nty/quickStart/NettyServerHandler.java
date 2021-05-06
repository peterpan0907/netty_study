package netty.nty.quickStart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 我们自定义一个handler，需要继续netty的业务处理，规定好的某个handlerAdapter
 * 将其加入到pipeline
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    private String string;

    /**
     * 读取客户端传送的数据
     *
     * @param ctx：上下文对象。含有通道、管道、
     * @param msg：客户端发送的数据，默认是object数据，当有数据的时候，就会触发
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 将msg转换为ByteBuf
        // 这个ByteBuf是netty提供的
        ByteBuf buf = (ByteBuf) msg;

        System.out.println("客户端发送消息是：" + buf.toString(CharsetUtil.UTF_8));
        string = buf.toString(CharsetUtil.UTF_8);
        System.out.println("客户端的地址：" + ctx.channel().remoteAddress());

    }

    /**
     * 数据读取完毕
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // write 和 flush 方法的合并
        // 将数据写入到缓冲并且刷新
        // 一般来讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("B : " + string, CharsetUtil.UTF_8));
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