package netty.nty.http;


import com.sun.jndi.toolkit.url.Uri;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * 我们自定义一个handler，需要继续netty的业务处理，规定好的某个handlerAdapter
 * 将其加入到pipeline
 * HttpObject 表示 客户端与服务器端相互通讯的数据封装后的类型
 * SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter 的子类，可以指定处理数据的泛型
 */
public class TestServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * 读取客户端的数据
     *
     * @param channelHandlerContext
     * @param httpObject
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {

        if (httpObject instanceof HttpRequest) {

            System.out.println("msg type is " + httpObject.getClass().getName());

            System.out.println("client address is " + channelHandlerContext.channel().remoteAddress());

            System.out.println(channelHandlerContext.pipeline().hashCode() + "\t" + this.hashCode());

            // 回复信息给浏览器
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello ,my name is server ", CharsetUtil.UTF_8);


            HttpRequest httpRequest = (HttpRequest) httpObject;

            // 对特定的资源进行过滤
            if ("/favicon.ico".equals(httpRequest.uri())) {
                System.out.println("请求了favicon.ico ，不做响应");
                return;
            }
            // 构建response
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,byteBuf);


            // 指定内容类型
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=utf8");
            // 指定content-length
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            // 构建好的response返回
            channelHandlerContext.writeAndFlush(response);

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        System.out.println("错误");
    }
}
