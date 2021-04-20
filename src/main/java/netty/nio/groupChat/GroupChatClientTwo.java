package netty.nio.groupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 群聊系统的而客户端
 * 客户端负责向服务器端进行数据的发送
 * 处理其他客户端转发的数据
 */
public class GroupChatClientTwo {


    private final String HOST = "127.0.0.1";
    private final int PORT = 6677;

    private Selector selector;

    private SocketChannel socketChannel;

    private String username;


    public GroupChatClientTwo() {
        try {
            // 建立一个selector
            selector = Selector.open();
            // 建立一个socket
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));

            socketChannel.configureBlocking(false);
            // 注册
            socketChannel.register(selector, SelectionKey.OP_READ);

            username = socketChannel.getLocalAddress().toString().substring(1);

            System.out.println(username + "is ok");
        } catch (IOException e) {
            System.out.println("服务器未上线");
        }


    }

    /**
     * 发送数据
     *
     * @param info
     */
    public void sendInfo(String info) {
        info = username + " 发送了：" + info + "消息！";
        try {
            // 写入消息到通道
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取数据
     */
    public void readInfo() {
        try {
            // 获取到是否有事件发生
            int count = selector.select();


            if (count > 0) {
                // 遍历这些事件
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();


                while (iterator.hasNext()) {

                    SelectionKey key = iterator.next();
                    // 是否可读
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();

                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        // 读取数据
                        int read = channel.read(buffer);
                        while (read > 0) {
                            String msg = new String(buffer.array());
                            System.out.println("客户端  " + username + "收到数据为:" + msg.trim());
                            buffer.clear();
                            read = channel.read(buffer);
                        }

                    }
                    iterator.remove();
                }
            } else {
                System.out.println("客户端没有收到服务端转发的数据");
            }
        } catch (IOException e) {
            System.out.println("服务器未启动");
        }
    }


    public static void main(String[] args) {
        GroupChatClient client = new GroupChatClient();

        // 开启一个线程来读取其他客户端转发过来的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    client.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        Scanner sca = new Scanner(System.in);

        /**
         * 客户端像服务器端发送数据
         */
        while (sca.hasNextLine()) {
            String s = sca.nextLine();

            client.sendInfo(s);
        }


    }
}
