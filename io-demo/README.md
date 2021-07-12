# JAVA NIO

## 使用步骤

### 1. 创建`Selector`

``` java 
Selector selector = Selector.open();
```

### 2. 创建一个`ServerSocketChannel`监听端口并注册到`Seleteor`上。

``` java
ServerSocketChannel ssc=ServerSocketChannel.open();
// 服务端开启对8082端口的监听
ssc.socket().bind(new InetSocketAddress(8082));
// 设置为非阻塞模式 必须设置为异步 不然无法使用Selector
ssc.configureBlocking(false);
// 注册监听到selector上 ServerSocketChannel只支持SelectionKey.OP_ACCEPT模式
ssc.register(selector,SelectionKey.OP_ACCEPT);
```

> 这个地方有俩个坑:
> 1. `configureBlocking`模式必须设置为异步，不然会报错。
> 2. `ServerSocketChannel`只支持`SelectionKey.OP_ACCEPT`类型,设置为其他类型也会报错。

### 3. 调用selector.select()方法;

共3个方法:

- int select() **阻塞到至少有一个通道在你注册的事件上就绪了。**
- int select(long timeout) **和select()一样，除了最长会阻塞timeout毫秒(参数)。**
- int selectNow() **不会阻塞，不管什么通道就绪都立刻返**

代码如下:

``` java
// Thread.interrupted()会判断当前线程是否中断,
// 在当前线程或者其他线程不调用interrupt()方法的情况下会一直是false
// 其实就是死循环 相当于while true select方法会阻塞线程所以循环不会一直执行
while(!Thread.interrupted()){
	// select方法会阻塞线程，直到有一个通道连接
    if(selector.select()>0){
    	// 具体的执行方法
    }
}
```
代码解释:
`Thread.interrupted()`会判断当前线程是否中断,
在当前线程或者其他线程不调用`interrupt()`方法的情况下会一直是false,
其实就是死循环 相当于`while(true)` ，`selector.select()`方法会阻塞线程,
所以循环不会一直执行.
### 为什么使用OP_WRITE
当操作系统写缓冲区有空闲空间时就绪。一般情况下写缓冲区都有空闲空间，小块数据直接写入即可，没必要注册该操作类型，否则该条件不断就绪浪费CPU；但如果是写密集型的任务，比如文件下载等，缓冲区很可能满，注册该操作类型就很有必要，同时注意写完后取消注册。


## NIO 操作类型

|操作类型|就绪条件及说明
|----|----
OP_ACCEPT|当接收到一个客户端连接请求时就绪。该操作只给服务器使用。
OP_CONNECT|当SocketChannel.connect()请求连接成功后就绪。该操作只给客户端使用。
OP_READ|当操作系统读缓冲区有数据可读时就绪。并非时刻都有数据可读，所以一般需要注册该操作，仅当有就绪时才发起读操作，有的放矢，避免浪费CPU。
OP_WRITE|当操作系统写缓冲区有空闲空间时就绪。一般情况下写缓冲区都有空闲空间，小块数据直接写入即可，没必要注册该操作类型，否则该条件不断就绪浪费CPU；但如果是写密集型的任务，比如文件下载等，缓冲区很可能满，注册该操作类型就很有必要，同时注意写完后取消注册。