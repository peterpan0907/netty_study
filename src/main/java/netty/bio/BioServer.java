package netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO实例得服务器端
 */
public class BioServer {


    public static void main(String[] args) {
        try {
            // 建立一个线程池
            ExecutorService executorService = Executors.newCachedThreadPool();

            ServerSocket serverSocket = new ServerSocket(6666);

            System.out.println("服务器端启动了！！！");
            while (true) {

                System.out.println("服务器在等待连接");

                Socket socket = serverSocket.accept();

                System.out.println("服务端连接到了一个客户端");

                // 接收到了一个客户端，就开启一个线程与客户端进行通讯
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        // 跟客户端通讯
                        try {
                            handler(socket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取数据，一次读取1024个字节
     * @param socket
     */
    public static void handler(Socket socket) throws IOException {
        // 一次读取1024个字节
        byte[] bytes = new byte[1024];
        // 建立输入流
        InputStream in = socket.getInputStream();
        while (true) {
            System.out.println("服务器在等待客户端读写");
            int read = in.read(bytes);
            if (read != -1) {
                System.out.println(Thread.currentThread().getName() + "服务器收到了一个数据：" + new String(bytes, 0, read));
            } else {
                break;
            }
        }
    }
}
