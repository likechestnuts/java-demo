/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

/**
 * @author wangguodong
 * @since 2021/8/3
 */
public class PooledDataSource implements DataSource {

	private String url;
	private String username;
	private String password;

	public PooledDataSource(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	private Queue<PooledConnectionProxy> queue = new ArrayBlockingQueue<>(100);

	@Override
	public Connection getConnection() throws SQLException {
		PooledConnectionProxy conn = queue.poll();
		if (null == conn) {
			System.out.println("创建对象,空闲时放入队列....");
			Connection connection = DriverManager.getConnection(url, username, password);
			conn = new PooledConnectionProxy(queue, connection);
		}else{
			System.out.println("空闲队列中取对象");
		}
		return conn;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		PooledConnectionProxy conn = queue.poll();

		if (null == conn) {
			Connection connection = DriverManager.getConnection(url, username, password);
			conn = new PooledConnectionProxy(queue, connection);
		}
		return conn;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
}
