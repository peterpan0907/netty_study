package netty.buffer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Scanner;

public class ScatteringAndGatheringTestClient {

    public static void main(String[] args) throws Exception {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1" , 7000));
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            socketChannel.write(ByteBuffer.wrap(scanner.next().getBytes()));
        }

    }
}
