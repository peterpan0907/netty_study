package netty.nio.tcp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 服务器端
 * 实现：上线通知，客户端消息转发
 */
public class GroupChatServer {

    private final int port = 6677;

    private Selector selector;

    private ServerSocketChannel listenChannel;

    public GroupChatServer() {
        try {
            // 建立一个selector
            selector = Selector.open();

            // 建立一个服务器channel
            listenChannel = ServerSocketChannel.open();

            // 绑定端口号
            listenChannel.bind(new InetSocketAddress(port));

            // 设置服务器端为非阻塞模式
            listenChannel.configureBlocking(false);

            // 将channel注册到selector上
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 监听客户端
    public void listen() {
        try {
            while (true) {
                // count表示是有事件要进行处理
                int count = selector.select();

                if (count > 0) {

                    //selector 与 channel 是通过selectedKey进行联接的
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                    while (iterator.hasNext()) {
                        // 获取到这个关联的key
                        SelectionKey key = iterator.next();
                        // 监听到accept,SelectionKey的类型
                        if (key.isAcceptable()) {
                            // 接收到的socketChannel
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);

                            // 读事件进行selector注册
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println("用户 " + socketChannel.getRemoteAddress() + "上线了！！！");
                        }

                        // 通道发送read时间，既通道是可读的状态
                        if (key.isReadable()) {
                            readData(key);
                        }
                        // 移除当前的key，放在重复计算
                        iterator.remove();
                    }
                } else {
                    System.out.println(listenChannel.getLocalAddress() + "在等待！！");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取数据
     *
     * @param key
     */
    public void readData(SelectionKey key) {
        // 获取到关联的channel
        SocketChannel channel = (SocketChannel) key.channel();
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);
            if (read > 0) {
                String msg = new String(buffer.array());
                sendToOtherClients(msg, channel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送消息给其他客户端
     */
    public void sendToOtherClients(String msg, SocketChannel channel) {
        System.out.println("服务器端在转发消息.....");
        try {

            Set<SelectionKey> keys = selector.keys();

            for (SelectionKey key : keys) {

                SelectableChannel keyChannel = key.channel();

                if (keyChannel instanceof SocketChannel && keyChannel != channel) {
                    SocketChannel cha = (SocketChannel) keyChannel;
                    // 通过消息转换为buffer
                    ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());

                    // 将数据写入通道
                    cha.write(buffer);

                }
            }
            System.out.println("服务器端转发消息完毕.....");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        GroupChatServer server = new GroupChatServer();

        server.listen();
    }


}
