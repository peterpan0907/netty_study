package netty.nty.tcpsolve;

/**
 * 自定义的协议
 */
public class MessageProtocol {
    // 长度
    private int len;
    // 内容
    private byte[] content;

    public MessageProtocol(int len , byte[] content){
        this.len = len;
        this.content = content;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
