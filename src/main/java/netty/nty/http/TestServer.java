package netty.nty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * http服务的demo。服务器端
 * 客户端采用浏览器
 */
public class TestServer {

    private static final int serverPort = 8080;

    public static void main(String[] args) {
        EventLoopGroup boosGroup = new NioEventLoopGroup(1);

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建服务器端得启动对象，配置对象
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new TestServerInitializer());

            // 绑定端口
            ChannelFuture future = bootstrap.bind(serverPort).sync();

            // 增加监听器
            future.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("监听" + serverPort + "端口成功！");
                    } else {
                        System.out.println("监听" + serverPort + "失败 ！");
                    }
                }
            });

            // 对关闭通道进行监听
            ChannelFuture close = future.channel().closeFuture().sync();

            close.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("关闭通道成功！");
                    } else {
                        System.out.println("关闭通道失败！");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
