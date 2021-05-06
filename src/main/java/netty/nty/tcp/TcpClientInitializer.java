package netty.nty.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


/**
 * 客户端的Initializer,自定义逻辑处理handler
 */
public class TcpClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 加入自定义的handler，处理业务逻辑
        pipeline.addLast(new TcpClientHandler());
    }
}
