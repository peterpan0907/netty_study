package netty.nty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳处理的handler
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 处理IdleStateEvent事件
     * 处理心跳
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 如果是IdleStateEvent，进行处理
        // 进行相应的打印
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventState = null;
            switch (event.state()) {
                // 读空闲
                case READER_IDLE:
                    eventState = "读空闲";
                    break;
                // 写空闲
                case WRITER_IDLE:
                    eventState = "写空闲";
                    break;
                // 读写空闲
                case ALL_IDLE:
                    eventState = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "---超时事件----" + eventState);
            System.out.println("服务器做相应的处理");

        }
    }
}
