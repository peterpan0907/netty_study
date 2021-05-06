package netty.nty.dobborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import netty.dobborpc.publicinterface.Common;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 客户端
 */
public class NettyClient {

    //创建线程池
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);


    private static NettyClientHandler nettyClientHandler;


    //编写方法使代理模式，获取一个代理对象
    public Object getBean(final Class<?> serviceClass, final String providerName) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass}, (proxy, method, args) -> {
                    if (nettyClientHandler == null) {
                        initClient0();
                    }
                    // 设置发送给服务器端的信息
                    // providerName ： 就是服务器端处理客户端发送的数据的协议头
                    nettyClientHandler.setParam(providerName + args[0]);
                    return executorService.submit(nettyClientHandler).get();
                });
    }

    // 初始化客户端
    private static void initClient0() {

        nettyClientHandler = new NettyClientHandler();

        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(nettyClientHandler);
                        }
                    });
            bootstrap.connect(Common.hostName, Common.port).sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            group.shutdownGracefully();
        }
    }

}
