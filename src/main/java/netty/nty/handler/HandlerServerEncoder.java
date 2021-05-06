package netty.nty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 * 如果发送的数据的类型不是自己定义的数据类型，不会调用encode
 */
public class HandlerServerEncoder extends MessageToByteEncoder<Long> {


    /**
     * @param ctx：上下文对象
     * @param aLong：数据
     * @param byteBuf：入栈的ByteBuf
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Long aLong, ByteBuf byteBuf) throws Exception {
        System.out.println("HandlerServerEncoder 被调用");

        System.out.println("msg : " + aLong);
        // 写入数据
        byteBuf.writeLong(aLong);
    }
}
