package netty.nty.dobborpc.customer;

import netty.dobborpc.netty.NettyClient;
import netty.dobborpc.publicinterface.Common;
import netty.dobborpc.publicinterface.HelloService;

/**
 * 启动客户端的启动类
 */
public class ClientBootStrap {

    public static void main(String[] args) throws Exception {

        // 创建一个消费者
        NettyClient nettyClient = new NettyClient();

        // 创建一个代理对象
        HelloService service = (HelloService) nettyClient.getBean(HelloService.class, Common.pre);

        for (; ; ) {
            String response = service.hello("你好，哈哈哈！！！");

            System.out.println("服务器端回复的数据是：" + response);

            Thread.sleep(1000);
        }


    }
}
