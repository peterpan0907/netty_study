package netty.nty.quickStart;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * netty的快速开始demo
 * 客户端
 */
public class NettyClient {

    public static void main(String[] args) {
        // 客户端需要一个循环组
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();

            bootstrap
                    .group(clientGroup) // 设置线程组
                    .channel(NioSocketChannel.class) // 设置客户端通道的实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 在管道加入自己的处理器，用来发送数据
                            socketChannel.pipeline().addLast(new NettyClientHandler());
                        }
                    }); // 连接服务器端

            System.out.println("client is ok");

            // 异步连接
            ChannelFuture future = bootstrap.connect("127.0.0.1", 6668).sync();
            // 对关闭通道进行监听
            future.channel().closeFuture().sync();
        } catch (Exception e) {

        } finally {
            clientGroup.shutdownGracefully();
        }
    }
}
