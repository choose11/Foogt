package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.LogUtil;

public class DBOperation {
	public static final String dropTUser = "drop table t_user_account";
	public static final String createTUser = "create table t_user_account("
			+ "account varchar2(20) primary key,"
			+ "password varchar2(60) not null," + "user_id int not null)";

	private static void close(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			LogUtil.e(e);
		}
	}

	public static void main(String[] args) {
		Connection conn = new ConnectionOracle().getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			boolean flag = stmt.execute(createTUser);
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, stmt, null);
		}
	}
}
