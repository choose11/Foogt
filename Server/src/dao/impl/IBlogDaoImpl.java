package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.bind.v2.TODO;

import util.LogUtil;
import dao.IBlogDao;
import db.ConnectionOracle;
import entity.BlogInfo;

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
		String sql = "select u.user_name, msg.content, to_char(msg.time_t,'yyyy-mm-dd hh:mi:ss') "
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
				// TODO: For test
				list.add(new BlogInfo(username + "", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
						.parse(postTime), content));
			}
			
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, pstmt, rs);
		}
		// list.add(new BlogInfo("eric",new Date(19920908),"Time"));
		return list;
	}
	
	/**
	 * 只搜索自己发送的微博
	 */
	public List<BlogInfo> selectUserOwnBlogs(int userId, int pageSize, int page) {
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement pstmt = null;
		String sql = "select u.user_name, msg.content, to_char(msg.time_t,'yyyy-mm-dd hh:mi:ss') "
				+ "from t_msg_info msg, t_user_info u "
				+ "where msg.user_id=u.user_id "
				+ "and msg_id in ("
				+ "select msg_id from( "
				+ "select rownum rn,msg_id from( "
				+ "select msg_id from t_user_msg_index "
				+ "where user_id=? and author_id=? "
				+ "order by time_t desc)  "
				+ "where rownum<=? ) "
				+ "where rn>? ) " 
				+ "and msg.user_id=?";
		ResultSet rs = null;
		ArrayList<BlogInfo> list1 = new ArrayList<BlogInfo>();
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
				
				list1.add(new BlogInfo(username + "", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
						.parse(postTime), content));
			}
			System.out.println("getuserblog");
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, pstmt, rs);
		}
		
		return list1;
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
