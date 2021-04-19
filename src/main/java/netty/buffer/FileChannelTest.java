package netty.buffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {
    private final static String fileName = "1.txt";


    private final static String fileNameCopy = "2.txt";

    public static void main(String[] args) {
        // 写入数据入文件
//        writeData();
        // 从文件读取数据
//        readData();
        // 使用一个buffer读取数据
//        writeAndReadData();

        // 文件拷贝
        copyFile();
    }

    /**
     * 复制文件
     */
    public static void copyFile() {

        try {
            FileInputStream in = new FileInputStream("3.txt");

            FileOutputStream out = new FileOutputStream("6.txt");

            FileChannel readChannel = in.getChannel();

            FileChannel writeChannel = out.getChannel();

            // 文件拷贝,transferFrom
//            writeChannel.transferFrom(readChannel, 0, readChannel.size());


            // 文件拷贝,transferTo
            readChannel.transferTo(0, readChannel.size(), writeChannel);

            readChannel.close();

            writeChannel.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 读写数据
     */
    public static void writeAndReadData() {
        try {
//            String con = "ni hao ya xiong die";
//            File file = new File(fileName);
//            File toFile = new File(fileNameCopy);
//            if (!file.exists() || !toFile.exists()) {
//                System.out.println("文件不存在");
//                return;
//            }
            FileInputStream in = new FileInputStream(fileName);

            FileOutputStream out = new FileOutputStream(fileNameCopy);

            FileChannel readChannel = in.getChannel();

            FileChannel writeChannel = out.getChannel();


            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (true) {
                // 上一次读取的数据清空
                buffer.clear();
                int read = readChannel.read(buffer);
                if (read != -1) {
                    // 读写翻转
                    buffer.flip();
                    writeChannel.write(buffer);
                } else {
                    break;
                }
            }
            readChannel.close();

            writeChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写数据入文件
     */
    public static void writeData() {
        try {
            String content = "你好！";
            File file = new File(fileName);
            if (file.exists()) {
                FileOutputStream out = new FileOutputStream(file);
                // 存数据用输出流
                FileChannel channel = out.getChannel();
                // 将写入的内容转换为buffer
                ByteBuffer buffer = ByteBuffer.wrap(content.getBytes());
                // 向channel写入数据
                channel.write(buffer);
                channel.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 从文件中读取数据
     */
    public static void readData() {
        try {

            File file = new File(fileName);
            if (!file.exists()) {
                System.out.println("文件不存在");
                return;
            }
            // 读取文件数据用输入流
            FileChannel channel = new FileInputStream(file).getChannel();
            // 一次读取1024个字节
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                int read = channel.read(buffer);
                if (read != -1) {
                    System.out.println(new String(buffer.array()));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
