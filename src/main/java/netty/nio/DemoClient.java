package netty.nio;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class DemoClient {

    public static void main(String[] args) {
        try {
            SocketChannel channel = SocketChannel.open();

            channel.configureBlocking(false);

            channel.connect(new InetSocketAddress("127.0.0.1", 6666));

            // 如果还未连接成功
            if (!channel.isConnected()) {
                while (!channel.finishConnect()) {
                    System.out.println("客户端正在连接服务器");
                }
            }
            System.out.println("客户端连接服务器成功！");

            Scanner scan = new Scanner(System.in);

            // 连接成功就发送数据
            while (scan.hasNextLine()) {
                ByteBuffer buffer = ByteBuffer.wrap(scan.next().getBytes());
                channel.write(buffer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
