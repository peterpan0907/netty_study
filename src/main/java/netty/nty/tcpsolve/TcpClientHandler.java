package netty.nty.tcpsolve;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * 客户端的业务处理handler
 */
public class TcpClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        System.out.println("TcpClientHandler 被调用");
        int len = messageProtocol.getLen();
        byte[] b = messageProtocol.getContent();
        String message = new String(b, Charset.forName("UTF-8"));
        System.out.println("从服务器端" + channelHandlerContext.channel().remoteAddress() + "读取数据，长度为" + len + ",内容为" + message);

        System.out.println("客户端接收到消息包数量为 + " + (++this.count));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("TcpClientHandler 发送数据...");
        // 使用客户端发送10 条数据 今天天气冷，吃火锅
        for (int i = 0; i < 10; i++) {
            String message = "今天天气冷，吃火锅 , 吃 " + (i + 1) + "顿";
            byte[] bytes = message.getBytes(Charset.forName("UTF-8"));
            int len = bytes.length;
            // 构成传送对象
            MessageProtocol protocol = new MessageProtocol(len, bytes);

            ctx.writeAndFlush(protocol);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }

}
