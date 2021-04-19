package netty.nty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {


    public static void main(String[] args) {


        // 创建BossGroup与WorkerGroup

        // bossGroup只是处理连接请求，真正与客户端业务处理会交给workerGroup完成
        // 两个都是无限循环
        // 参数代表线程数目
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);


        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 创建服务器端得启动对象，配置对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();


        serverBootstrap.group(bossGroup,workerGroup) // 设置两个线程组
                .channel(NioServerSocketChannel.class) // 使用NioSocketChannel作为服务端的通道实现
                .option(ChannelOption.SO_BACKLOG , 128) // 设置线程队列得到连接个数
                .childOption(ChannelOption.SO_KEEPALIVE , true) // 设置保存活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道测试对象
                    // 给pipeLine 设置处理器
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast();
                    }
                });


    }
}
