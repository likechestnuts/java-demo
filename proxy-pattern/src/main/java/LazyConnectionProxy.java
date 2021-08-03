/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * 懒加载代理类
 * @author wangguodong
 * @since 2021/8/3
 */
public class LazyConnectionProxy extends AbstractConnectionProxy {
	private Supplier<Connection> supplier;
	private Connection target = null;

	public LazyConnectionProxy(Supplier<Connection> supplier) {
		this.supplier = supplier;
	}

	@Override
	protected Connection getRealConnection() {
		if (target == null) {
			target = supplier.get();
		}
		return target;
	}

	@Override
	public void close() throws SQLException {
		if (this.target != null) {
			this.target.close();
		}
	}
}
