package netty.nty.dobborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * 客户端的业务处理
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;

    private String result; // 返回的结构

    private String param; // 客户端调用方法的时候，传入的参数

    /**
     * 服务器端回复的数据
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead is called");
        // 服务器回复的消息，用于客户端返回
        result = msg.toString();
        // 唤醒等待的线程
        notify();

    }

    /**
     * 客户端连接服务器端就会被调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive is called");
        // 其他方法会使用到该context
        context = ctx;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    /**
     * 被代理对象调用，发送数据给服务器-->wait ---->等待唤醒--->返回结果
     */
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("call is called");
        if (param.equals("") || param == null) {
            return "无效参数";
        }

        context.writeAndFlush(param);

        // 等待channelRead读取数据后唤醒
        wait();

        return result;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
