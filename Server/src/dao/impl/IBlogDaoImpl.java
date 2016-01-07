package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import util.LogUtil;
import dao.IBlogDao;
import db.ConnectionOracle;
import entity.BlogInfo;
import entity.msgInfo;

public class IBlogDaoImpl implements IBlogDao {

	/**
	 * 
	 * @param userId
	 * @param pageSize
	 * @param page
	 *            from 0
	 * @return
	 */
	public List<BlogInfo> selectBlogs(int userId, int pageSize, int page) {
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement pstmt = null;
		String sql = "select u.user_name, msg.content, to_char(msg.time_t,'yyyy-mm-dd hh24:mi:ss'), msg.msg_id, u.user_id "
				+ "from t_msg_info msg, t_user_info u "
				+ "where msg.user_id=u.user_id "
				+ "and msg_id in ("
				+ "select msg_id from( "
				+ "select rownum rn,msg_id from( "
				+ "select msg_id from t_user_msg_index "
				+ "where user_id=? "
				+ "order by time_t desc) "
				+ "where rownum<=?) "
				+ "where rn>? " + ")";
		ResultSet rs = null;
		ArrayList<BlogInfo> list = new ArrayList<BlogInfo>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, (page + 1) * pageSize);
			pstmt.setInt(3, page * pageSize);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String username = rs.getString(1);
				String content = rs.getString(2);
				String postTime = rs.getString(3);
				int msgId = rs.getInt(4);
				int authorId = rs.getInt(5);
				list.add(new BlogInfo(msgId, username, new SimpleDateFormat(
						"yyyy-MM-dd kk:mm:ss").parse(postTime), content,
						authorId));
			}

		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, pstmt, rs);
		}
		return list;
	}

	/**
	 * 只搜索自己发送的微博
	 */
	public List<BlogInfo> selectUserOwnBlogs(int userId, int pageSize, int page) {
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement pstmt = null;
		String sql = "select u.user_name, msg.content, to_char(msg.time_t,'yyyy-mm-dd hh24:mi:ss'), msg.msg_id "
				+ "from t_msg_info msg, t_user_info u "
				+ "where msg.user_id=u.user_id "
				+ "and msg_id in ("
				+ "select msg_id from( "
				+ "select rownum rn,msg_id from( "
				+ "select msg_id from t_user_msg_index "
				+ "where user_id=? and author_id=? "
				+ "order by time_t desc)  "
				+ "where rownum<=? ) "
				+ "where rn>? ) " + "and msg.user_id=?";
		ResultSet rs = null;
		ArrayList<BlogInfo> list = new ArrayList<BlogInfo>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, userId);
			pstmt.setInt(3, (page + 1) * pageSize);
			pstmt.setInt(4, page * pageSize);
			pstmt.setInt(5, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String username = rs.getString(1);
				String content = rs.getString(2);
				String postTime = rs.getString(3);
				int msgId = rs.getInt(4);
				list.add(new BlogInfo(msgId, username + "",
						new SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
								.parse(postTime), content, userId));
			}
			System.out.println("getuserblog");
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, pstmt, rs);
		}

		return list;
	}

	@Override
	public boolean collectBlog(int uid, int blogId) {
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement pstmt = null;
		boolean flag = false;
		String sql = "insert into t_blog_collection values(blog_collection_sequence.nextval,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, uid);
			pstmt.setInt(2, blogId);
			int i = pstmt.executeUpdate();
			if (i == 1) {
				flag = true;
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, pstmt, null);
		}
		return flag;
	}

	@Override
	public List<BlogInfo> getCollections(int userId, int pageSize, int page) {
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement pstmt = null;
		String sql = "select u.user_name, msg.content, to_char(msg.time_t,'yyyy-mm-dd hh24:mi:ss'), msg.msg_id, u.user_id from "
				+ "T_MSG_INFO msg, T_BLOG_COLLECTION col, T_USER_INFO u "
				+ "where msg.msg_id = col.msg_id "
				+ "and msg.user_id = u.user_id "
				+ "and col.msg_id in ( "
				+ "select msg_id "
				+ "from ( "
				+ "select rownum rn, col.msg_id "
				+ "from T_MSG_INFO msg, T_BLOG_COLLECTION col "
				+ "where msg.msg_id = col.msg_id "
				+ "and col.user_id = ? "
				+ "and rownum<=? "
				+ "order by time_t desc "
				+ ") "
				+ "where rn>? " + ")";
		ResultSet rs = null;
		ArrayList<BlogInfo> list = new ArrayList<BlogInfo>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, (page + 1) * pageSize);
			pstmt.setInt(3, page * pageSize);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String username = rs.getString(1);
				String content = rs.getString(2);
				String postTime = rs.getString(3);
				int msgId = rs.getInt(4);
				int authorId = rs.getInt(5);
				list.add(new BlogInfo(msgId, username + "",
						new SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
								.parse(postTime), content, authorId));
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, pstmt, rs);
		}

		return list;
	}

	// 插入消息元并且获得msgId
	@Override
	public msgInfo insertTMsgInfo(msgInfo m) {
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "insert into t_msg_info values(?,msg_id_sequence.nextval,?,?,?,?,to_date(?,'yyyy-MM-dd-HH24-mi-ss'))";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, m.getUserId());
			ps.setString(2, m.getContent());
			ps.setInt(3, m.getType());
			ps.setInt(4, m.getCommentCount());
			ps.setInt(5, m.getTransferCount());
			ps.setString(6, m.getTimeT());
			System.out.println("传输完毕");
			int i = ps.executeUpdate();
			ps = conn
					.prepareStatement("select msg_id_sequence.currval from dual");
			rs = ps.executeQuery();
			if (rs.next()) {
				int msgId = rs.getInt(1);
				m.setMsgId(msgId);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(conn, ps, null);
		}
		return m;
	}

	// insert into t_user_msg_index values()
	@Override
	public boolean insertTUserMsgIndex(int followId, int authorId, int msgId,
			String timeT) {
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement ps = null;
		String sql = "insert into t_user_msg_index values(?,?,?,to_date(?,'yyyy-MM-dd-HH24-mi-ss'))";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, followId);
			ps.setInt(2, authorId);
			ps.setInt(3, msgId);
			ps.setString(4, timeT);
			int i = ps.executeUpdate();
			return true;
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		} finally {
			close(conn, ps, null);
		}
	}

	@Override
	public boolean insertTMsgMsgRelation(int referenceId, int referenceMsgId,
			int referencedId, int referencedMsgId, int type, Date time) {
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement ps = null;
		boolean flag = false;
		String sql = "insert into t_msg_msg_relation values(?,?,?,?,?,to_date(?,'yyyy-MM-dd-HH24-mi-ss'))";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, referenceId);
			ps.setInt(2, referenceMsgId);
			ps.setInt(3, referencedId);
			ps.setInt(4, referencedMsgId);
			ps.setInt(5, type);
			ps.setString(6, new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss")
					.format(time));
			int i = ps.executeUpdate();
			if (i == 1) {
				flag = true;
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, ps, null);
		}
		return flag;
	}

	private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			LogUtil.e(e);
		}
	}

}
