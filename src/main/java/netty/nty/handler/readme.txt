自定义编码器与解码器来说明Netty的handler调用机制

1、客户端发送long ----> 服务器端

客户端发送的执行顺序：
HandlerClientHandler----> HandlerClientEncoder

服务器接收的执行顺序：
HandlerServerDecoder ----> HandlerServerHandler

2、服务器端回复long -----> 客户端

服务器回复的执行顺序：
HandlerServerHandler----> HandlerServerEncoder

客户端接收的执行顺序：
HandlerClientDecoder ----> HandlerClientHandler
