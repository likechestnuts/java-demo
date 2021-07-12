/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wangguodong
 */
public class NIODemo {
	public static void main(String[] args) throws IOException {
		// 全局selector，至关重要
		Selector selector = Selector.open();
		ServerSocketChannel ssc = ServerSocketChannel.open();
		// 服务端开启对8082端口的监听
		ssc.socket().bind(new InetSocketAddress(8082));
		// 设置为非阻塞模式 必须设置为异步 不然无法使用Selector
		ssc.configureBlocking(false);
		// 注册监听到selector上 ServerSocketChannel只支持SelectionKey.OP_ACCEPT类型
		ssc.register(selector, SelectionKey.OP_ACCEPT);

		while (!Thread.interrupted()) {
			// select方法会阻塞线程，直到有一个通道连接
			if (selector.select() > 0) {
				// 处理接收到的事件
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectionKeys.iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					// 因为selector.selectedKeys()是获取到的所有可连接的通道，如果不remove的话 下次还会遇到
					iterator.remove();

					if (key.isAcceptable()) {
						ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
						SocketChannel channel = serverSocketChannel.accept();
						// 只有设置成不阻塞才能注册
						channel.configureBlocking(false);
						// 注册读事件
						channel.register(selector, SelectionKey.OP_READ);
					} else if (key.isReadable()) {
						// 我们不会直接从channel中取出字节，而是将channel中的数据写入Buffer缓冲区
						SocketChannel sc = (SocketChannel) key.channel();
						ByteBuffer result = ByteBuffer.allocate(102400);
						ByteBuffer buffer = ByteBuffer.allocate(10);
						while (sc.read(buffer) > 0) {
							// 把写到buffer中的数据切换到可以读的状态
							buffer.flip();
							result.put(buffer);
							// 清空buffer 变成可写状态
							buffer.clear();
						}
						// 继续注册写事件 为什么会有写事件，在读里面就可以写入返回啊

						// 写操作的就绪条件为底层缓冲区有空闲空间，
						// 而写缓冲区绝大部分时间都是有空闲空间的，所以当你注册写事件后，写操作一直是就绪的，选择处理线程全占用整个CPU资源。
						// 所以，只有当你确实有数据要写时再注册写操作，并在写完以后马上取消注册。
						// 就是说注册写事件会有空闲或者可写的校验
						sc.register(selector, SelectionKey.OP_WRITE, new String(result.array(), StandardCharsets.UTF_8));
					} else if (key.isWritable()) {
						SocketChannel sc = (SocketChannel) key.channel();
						String attachment = (String) key.attachment();
						String response = ResponseUtils.getResponse(attachment);
						ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
						while (buffer.hasRemaining()) {
							sc.write(buffer);
						}
						sc.shutdownOutput();
						sc.shutdownInput();
						// 回写数据完成，关闭channel
						sc.close();
					}
				}
			}
		}
	}
}
