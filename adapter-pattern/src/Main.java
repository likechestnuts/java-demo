/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

import java.util.concurrent.Callable;

/**
 * 适配器模式 将一个类的接口转换成客户希望的另外一个接口，使得原本由于接口不兼容而不能一起工作的那些类可以一起工作。
 * 例：
 * 1. List<T> Arrays.asList(T[])就相当于一个转换器，它可以把数组转换为List。
 * 2. 假设我们持有一个InputStream，希望调用readText(Reader)方法，但它的参数类型是Reader而不是InputStream，怎么办？
 * InputStreamReader就是Java标准库提供的Adapter，它负责把一个InputStream适配为Reader。类似的还有OutputStreamWriter。
 * @author wangguodong
 * @since 2021/8/3
 */
public class Main {
	public static void main(String[] args) {
		Callable<Long> callable = new Task(12345000L);
		Thread thread = new Thread(new RunnableAdapter(callable));
		thread.start();
	}
}
