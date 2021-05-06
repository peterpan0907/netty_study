package netty.nty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * 基于webSocket实现长连接的服务器端使用的自定义的业务处理器
 * 因为webSocket是根据frame来进行传递数据的
 * TextWebSocketFrame 是其子类，表示是文本帧
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 打印出收到的消息
        System.out.println("服务器端收到消息" + msg.text());

        // 回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器事件" + LocalDateTime.now()));
    }

    // 当web客户端连接后就会触发
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // id 表示唯一的值
        // asLongText 是唯一的值
        // asShortText 不是唯一的值，有可能重复
        System.out.println("handler add 被调用" + ctx.channel().id().asLongText());
    }

    // 当web客户端断开后就会触发
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler remove 被调用" + ctx.channel().id().asLongText());
    }

    // 异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
