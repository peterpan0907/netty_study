package netty.nty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * 客户端的Initializer,定义编码器及其自定义的逻辑处理handler
 */
public class HandlerClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 入栈的handler，对数据进行编码
        pipeline.addLast(new HandlerClientEncoder());

        // 作为接收服务器回复消息的解码器
        pipeline.addLast(new HandlerClientDecoder());

        // 加入自定义的handler，处理业务逻辑
        pipeline.addLast(new HandlerClientHandler());
    }
}
