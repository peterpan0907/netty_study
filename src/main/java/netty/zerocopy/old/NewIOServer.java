package netty.zerocopy.old;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 *
 */
public class NewIOServer {

    public static void main(String[] args) {

        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.bind(new InetSocketAddress(7002));

            ByteBuffer buffer = ByteBuffer.allocate(4096);

            while (true) {

                SocketChannel socketChannel = serverSocketChannel.accept();

                long read = 0;

                long totalCount = 0;

                while ((read = socketChannel.read(buffer)) > 0) {
                    totalCount = totalCount + read;
                    buffer.clear();
                }
                System.out.println("服务器端收到了客户端发来的数据，共 " + totalCount + "个字节");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
