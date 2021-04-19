package netty.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class BioClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket();

            socket.connect(new InetSocketAddress("127.0.0.1", 6666));

            sendData(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     */
    public static void sendData(Socket socket) {
        try {
            OutputStream out = null;

            out = socket.getOutputStream();

            Scanner sca = new Scanner(System.in);

            while (sca.hasNextLine()) {
                String str = sca.next();
                System.out.println("客户端发送了一个数据：" + str);
                // 主要就是这个方法，进行数据的发送
                out.write(str.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
