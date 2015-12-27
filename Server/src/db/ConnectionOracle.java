package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionOracle {

	private static final String DRIVR = "oracle.jdbc.driver.OracleDriver";
	private static final String IP = "10.25.84.210";
	private static final String DBNAME = "oracle";
	private static final String PORT = "1521";
	private static final String URL = "jdbc:oracle:thin:@" + IP + ":" + PORT
			+ ":" + DBNAME;
	private static final String USER = "scott";
	private static final String PASSWORD = "scott";

	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(DRIVR);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	/**
	 * Test Mod
	 * @param args
	 */
	public static void main(String[] args) {
		new ConnectionOracle().getConnection();
		System.out.println("getConnection");
	}
}
