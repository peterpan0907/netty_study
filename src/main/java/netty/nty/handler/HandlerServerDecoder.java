package netty.nty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器
 */
public class HandlerServerDecoder extends ByteToMessageDecoder {

    /**
     * decode 会根据接收的数据被调用多次，直到确定没有新的元素被添加到list
     * 或者是ByteBuf 没有更多的可读字节为止
     * 如果list out 不为空，就会将list的内容传递给下一个ChannelInBountHandler处理，该处理器的方法也会被调用多次
     *
     * @param ctx：上下文对象
     * @param byteBuf：入栈的ByteBuf
     * @param list：集合，将解码器的数据传给下一个handler
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List list) throws Exception {
        System.out.println("HandlerServerDecoder 被调用");
        //需要判断是否有8个字节，才能读取一个long
        if (byteBuf.readableBytes() >= 8) {
            list.add(byteBuf.readLong());
        }
    }
}
