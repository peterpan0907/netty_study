package netty.nty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 解码器
 * 不需要判断数据是否足够读取，内部自己会判断
 */
public class MyByeToLongDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("MyByeToLongDecoder 被调用");
        list.add(byteBuf.readLong());
    }
}
