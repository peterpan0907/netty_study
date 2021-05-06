package netty.nty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 基于webSocket实现长连接的服务器端
 */
public class WebSocketServer {

    // 监听端口
    private static int port = 7000;


    public static void main(String[] args) {

        // 创建两个线程组
        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bs = new ServerBootstrap();

            // 配置信息的配置
            bs.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 加入日志处理器
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 获取到pipeline
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            // 因为是基于http协议，使用http的编码解码器
                            pipeline.addLast(new HttpServerCodec());

                            // http 是以块方式写，添加ChunkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            /**
                             * 1、http数据在传输工程中是分段，HttpObjectAggregator就是可以将多个段聚合
                             * （这就是为什么浏览器在发送大量的数据的时候，就会发送多次http请求）
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));


                            /**
                             * 1、对应webSocket，它的数据是以帧（frame）的形式传递
                             * 浏览器请求时候，ws://localhost:7000/xxx，请求的uri，也就是针对哪个地址
                             * WebSocketServerProtocolHandler 的核心功能是 将http协议 升级为webSocket协议，保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            // 自定义handler，来处理业务逻辑
                            pipeline.addLast(new WebSocketServerHandler());
                        }
                    });
            System.out.println("netty server start......");

            ChannelFuture channelFuture = bs.bind(port).sync();

            // 监听关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
