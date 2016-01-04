package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.LogUtil;

public class DBOperation {
	public static final String dropTUserAccount = "drop table t_user_account";

	public static final String dropTUserInfo = "drop table t_user_info";

	public static final String dropSequence = "drop sequence user_id_sequence";

	public static final String dropSequenceMsgId = "drop sequence msg_id_sequence";

	public static final String dropTUserRelation = "drop table t_user_relation";

	public static final String dropTUserMsgIndex = "drop table t_user_msg_index";

	public static final String dropTBlogCollection = "drop table TBlogCollection";
	
	public static final String dropSequenceBlogCollection = "drop sequence blog_collection_sequence";
	
	public static final String dropTMsgMsgRelation = "drop table t_msg_msg_relation";

	public static final String createTUserAccount = "create table t_user_account("
			+ "account varchar2(20) primary key,"
			+ "password varchar2(60) not null,"
			+ "user_id int references t_user_info(user_id)" + ")";

	public static final String createTUserInfo = "create table t_user_info("
			+ "user_id int primary key," + "user_name varchar2(140) not null,"
			+ "user_intro varchar2(140) not null," + "msg_count int,"
			+ "fans_count int," + "focus_count int" + ")";

	public static final String createSequence = "create sequence user_id_sequence "
			+ "increment by 1 "
			+ "minvalue 0 "
			+ "start with 0 "
			+ "maxvalue 9999999999 " + "cache 10";

	public static final String createSequenceMsgId = "create sequence msg_id_sequence "
			+ "increment by 1 "
			+ "minvalue 0 "
			+ "start with 0 "
			+ "maxvalue 9999999999 " + "cache 10";

	public static final String createSequenceBlogCollection = "create sequence blog_collection_sequence "
			+ "increment by 1 "
			+ "minvalue 0 "
			+ "start with 0 "
			+ "maxvalue 9999999999 " + "cache 10";

	public static final String createTMsgInfo = "create table t_msg_info("
			+ "user_id int not null," + "msg_id  int primary key,"
			+ "content varchar(80) not null," + "type int not null,"
			+ "comment_count int not null," + "transfer_count int not null,"
			+ "time_t date not null" + ")";

	public static final String createTUserRelation = "create table t_user_relation("
			+ "user_id int ," + "follow_id int," + "type int not null" + ")";

	public static final String createTUserMsgIndex = "create table t_user_msg_index("
			+ "user_id int  references t_user_info(user_id),"
			+ "Author_id int," + "msg_id int," + "time_t date not null" + ")";

	public static final String createTBlogCollection = "create table t_blog_collection("
			+ "collection_id int primary key, "
			+ "user_id int references t_user_info(user_id), "
			+ "msg_id int references t_msg_info(msg_id)" + ") ";
	
	public static final String createTMsgMsgRelation = "create table t_msg_msg_relation(" +
			"reference_id int references t_user_info(user_id), " +
			"reference_msg_id int references t_msg_info(msg_id), " +
			"referenced_id int, " +
			"referenced_msg_id int, " +
			"type int, " +
			"time_t date" +
			")";

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

	public static void createTables() {
		Connection conn = new ConnectionOracle().getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			// stmt.execute(createTUserInfo);
			// stmt.execute(createTUserAccount);
			// stmt.execute(createSequence);
			// stmt.execute(createTMsgInfo);
			// stmt.execute(createSequenceMsgId);
			// stmt.execute(createTUserRelation);
			// stmt.execute(createTUserMsgIndex);
			// stmt.execute(createSequenceBlogCollection);
//			stmt.execute(createTBlogCollection);
			stmt.execute(createTMsgMsgRelation);

			System.out.println("Complete");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, stmt, null);
		}
	}

	public static void dropTables() {
		Connection conn = new ConnectionOracle().getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			// stmt.execute(dropTUserAccount);
			// stmt.execute(dropTUserInfo);
			// stmt.execute(dropSequence);
			// stmt.execute(dropSequenceMsgId);
			// stmt.execute(dropTUserRelation);
//			stmt.execute(dropTUserMsgIndex);
//			stmt.execute(dropTBlogCollection);
			stmt.execute(dropSequenceBlogCollection );
			System.out.println("Complete");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, stmt, null);
		}
	}

	public static void showUsers() {
		Connection conn = new ConnectionOracle().getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from t_user_info");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + "\t" + rs.getString(2) + "\t"
						+ rs.getString(3));
			}
			System.out.println("Complete");
			System.out.println("******************************************");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, stmt, null);
		}
	}

	public static void showAccounts() {
		Connection conn = new ConnectionOracle().getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from t_user_account");
			while (rs.next()) {
				System.out.println(rs.getString(1) + "\t" + rs.getString(2)
						+ "\t" + rs.getInt(3));
			}
			System.out.println("Complete");
			System.out.println("******************************************");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, stmt, null);
		}
	}

	public static void main(String[] args) {
		createTables();
		// showAccounts();
		// showUsers();
		// dropTables();
	}
}
