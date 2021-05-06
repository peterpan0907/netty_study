package netty.nty.dobborpc.provider;


import netty.dobborpc.publicinterface.Common;
import netty.nty.dobborpc.netty.NettyServer;

//启动一个服务提供者，就是nettyServer
public class ServerBootStrap {

    public static void main(String[] args) {
        NettyServer.startServer(Common.hostName, Common.port);
    }
}
