package netty.zerocopy.old;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {

    public static void main(String[] args) {

        try {
            SocketChannel socketChannel = SocketChannel.open();

            socketChannel.connect(new InetSocketAddress("127.0.0.1", 7002));

            String fileName = "2.rar";

            FileChannel channel = new FileInputStream(fileName).getChannel();

            // 读取文件的开始时间
            long startTime = System.currentTimeMillis();

            long size = channel.size();
            // 因为window下一次只能传送8兆
            long oneTime = (8 * 1024 * 1024);
            long cs = size / oneTime + 1;

            long position = 0;

            while (cs >= 1) {
                channel.transferTo(position * oneTime, oneTime, socketChannel);
                position++;
                cs--;
            }
            // 读取的截至时间
            long endTime = System.currentTimeMillis();
            System.out.println("发送总字节数：" + size + "字节， 用时" + (endTime - startTime));
            channel.close();
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
