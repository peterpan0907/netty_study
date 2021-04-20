package netty.nio.groupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 群聊系统的服务器端（单reactor单线程）
 * 服务器端
 * 实现：上线通知，客户端消息接收提醒、客户端消息接收转发、客户端下线提醒
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

            // 将channel注册到selector上，以接收的事件进行注册
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 监听客户端
    public void listen() {
        System.out.println("监听客户端是否上线的线程名称：" + Thread.currentThread().getName());
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
                            // 设置为非阻塞
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
                    System.out.println("服务器 " + listenChannel.getLocalAddress() + "在等待！！");
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

        SocketChannel channel = null;
        try {
            // 获取到关联的channel
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 读取数据
            int read = channel.read(buffer);
            while (read > 0) {
                String msg = new String(buffer.array());
                System.out.println("服务器端收到客户端" + channel.getRemoteAddress() + "传来的的数据：" + msg);
                sendToOtherClients(msg, channel);
                buffer.clear();
                read = channel.read(buffer);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了");
                // 取消注册
                key.channel();
                // 关闭通道
                channel.close();
            } catch (IOException ioException) {
            }
        }

    }

    /**
     * 发送消息给其他客户端
     *
     * @param msg：消息
     * @param channel：channel
     */
    public void sendToOtherClients(String msg, SocketChannel channel) {
        System.out.println("服务器端在转发消息.....");
        System.out.println("服务器转发消息的线程名称：" + Thread.currentThread().getName());
        try {
            // 获取到selector的关联的key
            Set<SelectionKey> keys = selector.keys();

            for (SelectionKey key : keys) {

                SelectableChannel keyChannel = key.channel();
                // 转发至其他客户端，除了自己
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
