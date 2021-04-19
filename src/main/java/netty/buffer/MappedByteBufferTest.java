package netty.buffer;


import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.RandomAccess;

/**
 * MappedByteBufferTest : 可以直接让文件在内存中修改，操作系统不需要再拷贝一次
 */
public class MappedByteBufferTest {

    public static void main(String[] args) throws Exception {

        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt" , "rw");

        // 获取到对应的通道
        FileChannel channel = randomAccessFile.getChannel();

        // 三个参数：
        // 第一个参数：使用的模式，这里是用读写模式
        // 第二个参数：可以直接修改文件的起始位置
        // 第三个参数：是映射到内存的大小，即是1.txt文件有多少个字节映射到内存，最多修改多少个字节
        // 可以直接进行修改的范围是0 - 5
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        map.put(0 , (byte)'h');

        map.put(1 , (byte)'e');

        randomAccessFile.close();
    }
}
