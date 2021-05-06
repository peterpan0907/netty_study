package netty.nty.quickStart;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.SystemPropertyUtil;

/**
 * netty的快速开始demo
 * 服务器端
 */
public class NettyServer {


    public static void main(String[] args) {
        // 创建BossGroup与WorkerGroup，类型都是NioEventLoopGroup
        // bossGroup只是处理连接请求，真正与客户端业务处理会交给workerGroup完成
        // 两个都是无限循环
        // 参数代表线程数目
        // bossGroup与workerGroup 含有的子线程的个数默认是CPU的核数成语
        // DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {


            // 创建服务器端得启动对象，配置对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();


            serverBootstrap.group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用NioServerSocketChannel作为服务端的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列等待连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保存活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { //给我们的workerGroup的EventLoop创建一个通道测试对象
                        // 给workerGroup里面的pipeLine 设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            socketChannel.pipeline().addLast(new NettyServerHandler());
                            socketChannel.pipeline().addLast(new NettyServerLongTaskHandler());
                        }

                    });
            System.out.println("server is ready ");

            // 绑定一个端口并且同步，生成一个ChannelFuture对象
            // 这里如果调用了sync，则是阻塞进行绑定端口的，如果不加，是非阻塞的方式
//            ChannelFuture future = serverBootstrap.bind(6668);
            ChannelFuture future = serverBootstrap.bind(6668).sync();


            // 如果想判断监听是否成功，注册监听器，监控我们关心的事件
            future.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("监听端口成功");
                    } else {
                        System.out.println("监听端口失败");
                    }
                }
            });
            // 对关闭通道进行监听
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
