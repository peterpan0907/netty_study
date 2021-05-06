package netty.nty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 管道的childHandler
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 获取到通道
        ChannelPipeline pipeline = socketChannel.pipeline();

        // HttpServerCodec 是netty提供的处理http的编码解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());

        // 加入自定义的处理器
        pipeline.addLast("MyTestServerHandler" ,new TestServerHandler());
    }
}
