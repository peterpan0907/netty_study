package netty.nty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChatSever {

    // 监听端口
    private int port;


    public GroupChatSever(int port) {
        this.port = port;
    }


    // 编写一个run方法，处理客户端的请求
    public void run() {

        // 创建两个线程组
        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bs = new ServerBootstrap();

            // 配置信息的配置
            bs.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 获取到pipeline
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 像pipeline里面增加一个解码器
                            pipeline.addLast("decoder", new StringDecoder());

                            // 像pipeline里面增加一个编码器
                            pipeline.addLast("encoder", new StringEncoder());

                            // 像pipeline里面增加一个业务处理handler
                            pipeline.addLast(new GroupServerHandler());
                        }
                    });
            System.out.println("netty server start......");

            ChannelFuture channelFuture = bs.bind(port).sync();

            // 监听关闭
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new GroupChatSever(7000).run();
    }
}
