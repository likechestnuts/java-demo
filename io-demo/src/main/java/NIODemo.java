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
		// 设置为非阻塞模式
		// 思考1：阻塞和同步是一个概念吗？非阻塞=异步？
		ssc.configureBlocking(false);
		// 注册监听到selector上
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		while (!Thread.interrupted()) {
			// 因为是非阻塞模式，所以不论是否接收到请求，selector.select()都会立即返回。这里需要判断是否真正的accept
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
						// 继续注册写事件
						sc.register(selector, SelectionKey.OP_WRITE, new String(result.array(), StandardCharsets.UTF_8));
					} else if (key.isWritable()) {
						SocketChannel sc = (SocketChannel) key.channel();
						String attachment = (String) key.attachment();
						String response = ResponseUtils.getResponse(attachment);
						ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
						while (buffer.hasRemaining()) {
							sc.write(buffer);
						}
						// 回写数据完成，关闭channel
						sc.close();
					}
				}
			}
		}
	}
}
