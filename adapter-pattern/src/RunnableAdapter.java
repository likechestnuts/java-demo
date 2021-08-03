/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

import java.util.concurrent.Callable;

/**
 * @author wangguodong
 * @since 2021/8/3
 */
public class RunnableAdapter implements Runnable {
	private Callable<?> callable;

	public RunnableAdapter(Callable<?> callable) {
		this.callable = callable;
	}

	@Override
	public void run() {
		try {
			callable.call();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
