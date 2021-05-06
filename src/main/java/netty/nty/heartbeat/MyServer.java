package netty.nty.heartbeat;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;

/**
 * netty心跳机制demo
 */
public class MyServer {

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
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 加入日志处理器
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 获取到pipeline
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 像pipeline里面增加一个IdleStateHandler
                            // IdleStateHandler 是netty提供的处理空闲状态的处理器
                            // 其实就是启动一个IdleStateEvent来处理，当其触发后，就会传递给管道pipeline的下一个handler去处理
                            // 通过调用（触发）下一个handler的userEventTriggered，在该方法中处理该event
                            // 四个参数：
                            // long readerIdleTime：表示多长时间没有读，就会发送一个心跳检测包检测是否连接
                            // long writerIdleTime：表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                            // long allIdleTime：表示多长时间没有读写，就会发送一个心跳检测包检测是否连接
                            // TimeUnit unit：时间单位
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            // 增加对一个空闲检测进一步处理的handler
                            pipeline.addLast(new MyServerHandler());

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
