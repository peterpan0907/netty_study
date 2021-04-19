package netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class DemoServer {

    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            Selector selector = Selector.open();

            //绑定端口
//            serverSocketChannel.socket().bind(new InetSocketAddress(6666));
            serverSocketChannel.bind(new InetSocketAddress(6666));

            //设置为非阻塞
            serverSocketChannel.configureBlocking(false);

            // 在selector注册,注册后就会生成对应的selectionKey
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            // 通道注册的事件
//            selector.keys();
            // 当前通道上的事件
//            selector.selectedKeys();

            while (true) {
                // 没有客户端进行连接
                if (selector.select(1000) == 0) {
//                    System.out.println("还未进行连接");
                    continue;
                }
                // 找到连接的所有客户端
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                // 遍历
                while (iterator.hasNext()) {
                    // 获取到一个selectionKey
                    SelectionKey key = iterator.next();
                    // 如果是连接的请求
                    if (key.isAcceptable()) {
                        // 服务器端有一个客户端连接请求
                        SocketChannel channel = serverSocketChannel.accept();
                        // 设置为非阻塞
                        channel.configureBlocking(false);
                        // 向selector注册
                        channel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    }
                    //如果是读
                    else if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();

                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        // 读取数据
                        channel.read(buffer);

                        System.out.println("服务器端收到客户端发送的消息：" + new String(buffer.array()));
                        //清楚上次读取的数据，不然每次收到的消息会包含之前的数据
                        buffer.clear();
                    }
                }
                // 手动从集合中进行输出元素
                iterator.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
