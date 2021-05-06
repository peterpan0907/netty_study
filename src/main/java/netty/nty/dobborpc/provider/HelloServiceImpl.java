package netty.nty.dobborpc.provider;

import netty.dobborpc.publicinterface.HelloService;

/**
 * 实现类
 */
public class HelloServiceImpl implements HelloService {

    private int count;

    // 当有消费方进行消费的时候，就返回一个结果
    @Override
    public String hello(String msg) {
        System.out.println("服务器收得到客户端的消息 ， 消息为：[" + msg + "]");
        if (msg != null) {
            return "你好，我已经收到你的消息，消息是：[" + msg + "]" + "，次数[" + (++count) + "]";
        } else {
            return "你好，我已经收到你的消息";
        }
    }
}
