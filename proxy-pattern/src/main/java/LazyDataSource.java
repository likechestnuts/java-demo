/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @author wangguodong
 * @since 2021/8/3
 */
public class LazyDataSource implements DataSource {
	private String url;
	private String username;
	private String password;

	public LazyDataSource(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	@Override
	public Connection getConnection() throws SQLException {
		System.out.println("懒加载....");
		return new LazyConnectionProxy(()->{
			System.out.println("真正获取数据源....");
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(url,username,password);
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
			return connection;
		});
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return null;
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
