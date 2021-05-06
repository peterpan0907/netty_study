package netty.nty.byteBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * Netty中的buffer——ByteBuffer使用
 */
public class NettyByteBuffer {

    public static void main(String[] args) {
        // 0 -----> readIndex -----> writeIndex ------->capacity

        // 创建一个byteBuf
        // 创建对象，该对象包含一个数组arr，是一个byte[10]
        ByteBuf byteBuf = Unpooled.buffer(10);

        // readIndex保持不变，writeIndex逐渐增加
        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i * 2);
        }
        // 在netty的buffer不需要进行flip进行读写切换，因为底部维护了readIndex,writeIndex
        for (int i = 0; i < 10; i++) {
            // readIndex保持不变
            System.out.println(byteBuf.getByte(i));
        }
        for (int i = 0; i < 10; i++) {
            // readIndex逐渐增加
//            System.out.println(byteBuf.readerIndex(i).readByte());
            System.out.println(byteBuf.readByte());
        }
    }


    public static void study() {
        // 0 -----> readIndex -----> writeIndex ------->capacity

        // 创建一个byteBuf，直接赋值的类型进行创建，该字符串长度为21
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello , good morning!", CharsetUtil.UTF_8);


        // 使用byteBuf的api
        if (byteBuf.hasArray()) {
            byte[] array = byteBuf.array();

            System.out.println(new String(array, CharsetUtil.UTF_8));
            // 获取byetBuf的信息
            System.out.println("byteBuf = " + byteBuf);

            // 偏移量
            System.out.println(byteBuf.arrayOffset());
            // readIndex 的 值  这里是0
            System.out.println(byteBuf.readerIndex());
            // writeIndex 的 值 ，这里是21
            System.out.println(byteBuf.writerIndex());
            // capacity容量的值，这里为63，因为一个占3个字节
            System.out.println(byteBuf.capacity());
            // 可读的数量，是writeIndex - readIndex
            System.out.println(byteBuf.readableBytes());

            // 这里，就不止输出hello , good morning!，因为capacity为63，还会输出很多空字符
            for (int i = 0; i < byteBuf.capacity(); i++) {
                System.out.println((char) byteBuf.getByte(i));
            }
            // 这里，就只会输出hello , good morning!
            int len = byteBuf.readableBytes();
            for (int i = 0; i < len; i++) {
                System.out.println((char) byteBuf.getByte(i));
            }
            // 这里就会报错，因为readByte会导致readIndex的移动，而i又大于writeIndex，所以readIndex就会超过writeIndex，导致报错
//            for (int i = 0; i < byteBuf.capacity(); i++) {
//                System.out.println((char) byteBuf.readByte());
//            }
            // 按照范围读取,从0开始，读取5个，若i+i1大于capacity也会报错，下标越界
            CharSequence charSequence = byteBuf.getCharSequence(0, 410, CharsetUtil.UTF_8);
            System.out.println(charSequence);
        }
    }
}
