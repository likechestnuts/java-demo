/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 为其他对象提供一种代理以控制对这个对象的访问。
 * @author wangguodong
 * @since 2021/8/3
 */
public class Main {
	public static void main(String[] args) {
//		LazyDataSource lazyDataSource = new LazyDataSource("111","", "");
//		try(Connection connection = lazyDataSource.getConnection()){
//			connection.getNetworkTimeout();
//		} catch (SQLException throwables) {
//			throwables.printStackTrace();
//		}

		DataSource pooledDataSource = new PooledDataSource("jdbc:mysql://rm-m5e90k929g572chi5fo.mysql.rds.aliyuncs.com:3306/mysys","my_system","Wgg1940845501!");
		try (Connection connection = pooledDataSource.getConnection()) {
//			connection.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		try (Connection conn = pooledDataSource.getConnection()) {
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		try (Connection conn = pooledDataSource.getConnection()) {
			// 获取到的是同一个Connection
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		try (Connection conn = pooledDataSource.getConnection()) {
			// 获取到的是同一个Connection
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
}
