package netty.nio.tcp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {


    private final String HOST = "127.0.0.1";
    private final int PORT = 6677;

    private Selector selector;

    private SocketChannel socketChannel;

    private String username;


    public GroupChatClient() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void sendInfo(String info) {
        info = username + " 发送了：" + info + "消息！";
        try {
            // 写入消息到通道
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void readInfo() {
        try {
            // 获取到是否有事件发生
            int count = selector.select();


            if (count > 0) {
                // 遍历这些事件
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();


                while (iterator.hasNext()) {

                    SelectionKey key = iterator.next();

                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();


                        ByteBuffer buffer = ByteBuffer.allocate(1024);


                        channel.read(buffer);

                        byte[] array = buffer.array();

                        System.out.println("客户端  " + username + "收到数据为:" + new String(array).trim());
                    }
                    iterator.remove();
                }
            } else {
                System.out.println("客户端没有收到服务端转发的数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        GroupChatClient client = new GroupChatClient();


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

        while (sca.hasNextLine()) {
            String s = sca.nextLine();

            client.sendInfo(s);
        }


    }
}
