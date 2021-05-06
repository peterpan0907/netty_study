package netty.nty.tcpsolve;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 解码器
 */
public class MyMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("MyMessageDecoder 被调用....");

        //需要将二进制字节码转换为MessageProtocol协议包
        int len = byteBuf.readInt();

        byte[] content = new byte[len];

        byteBuf.readBytes(content);

        MessageProtocol messageProtocol = new MessageProtocol(len, content);

        list.add(messageProtocol);
    }
}
