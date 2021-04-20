package netty.buffer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * buffer的聚合与分散服务器端的实现
 */
public class ScatteringAndGatheringServer {

    public static void main(String[] args) throws Exception {

        // 建立服务端channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(7000));

        // 创建buffer数组
        ByteBuffer[] buffers = new ByteBuffer[2];

        buffers[0] = ByteBuffer.allocate(5);
        buffers[1] = ByteBuffer.allocate(3);

        SocketChannel socketChannel = serverSocketChannel.accept();

        // 假设从客户端读取8个字节
        int maxLength = 8;

        while (true) {
            int byteRead = 0;
            while (byteRead < maxLength) {
                long read = socketChannel.read(buffers);
                // 累计读取字节数
                byteRead += read;
                System.out.println("byteRead = " + byteRead);
                // 查看当前的buffer的position与limit
                Arrays.asList(buffers).stream().map(buffer -> "position = " + buffer.position() + ", limit = " + buffer.limit()).forEach(System.out::println);
            }
            // 将所有的buffer进行翻转
            Arrays.asList(buffers).forEach(byteBuffer -> byteBuffer.flip());

            // 将数据回显给客户端
            long byteWrite = 0;
            while (byteWrite < maxLength) {
                long l = socketChannel.write(buffers);
                byteWrite += l;
            }

            // 将所有的buffer进行clear
            Arrays.asList(buffers).forEach(byteBuffer -> byteBuffer.clear());
            System.out.println("byteRead total= " + byteRead + "byteWrite total= " + byteWrite);
        }

    }
}
