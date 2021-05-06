package netty.nty.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务器端的handler，定义解码器及其自定义的逻辑处理handler
 */
public class HandlerServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 入栈的handler，对数据进行解码
        pipeline.addLast(new HandlerServerDecoder());

        // 作为回复客户端消息的编码器
        pipeline.addLast(new HandlerServerEncoder());

        // 无需判断数据是否传送完毕，内部会自己判断
//        pipeline.addLast(new MyByeToLongDecoder());


        // 加入自定义的handler，处理业务逻辑
        pipeline.addLast(new HandlerServerHandler());
    }
}
