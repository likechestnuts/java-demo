/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

import java.util.concurrent.Callable;

/**
 * @author wangguodong
 * @since 2021/8/3
 */
public class Task implements Callable<Long> {
	private long num;

	public Task(long num) {
		this.num = num;
	}

	@Override
	public Long call() throws Exception {
		long r = 0;
		for (int n = 1; n < this.num; n++) {
			r = r + n;
		}
		System.out.println("Result:"+r);
		return r;
	}
}
