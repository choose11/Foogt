package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.LogUtil;
import dao.IUserDao;
import db.ConnectionOracle;
import entity.User;
import entity.msgInfo;

public class IUserDaoImpl implements IUserDao {

	public boolean changePW(User u, String newPW) {
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		String sql = "select * from ticketuser where user_name=? and user_password=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, u.getUsername());
			pstmt.setString(2, u.getPassword());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				sql = "update ticketuser set user_password=? where user_name=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, newPW);
				pstmt.setString(2, u.getUsername());
				int i = pstmt.executeUpdate();
				if (i == 1) {
					flag = true;
				}
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, pstmt, rs);
		}
		return flag;
	}

	/**
	 * @return true if account exist
	 */
	public boolean checkAccountExist(User u) {
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		String sql = "select * from t_user_account where account=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, u.getAccount());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, pstmt, rs);
		}
		return flag;
	}

	public boolean userLogin(User u) {
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		String sql = "select * from t_user_account where account=? and password=?";
		System.out.println(1);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, u.getUsername());
			pstmt.setString(2, u.getPassword());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, pstmt, rs);
		}
		return flag;
	}

	/**
	 * userId is set after userRegisterInfo() executed. so this method must
	 * execute after userRegisterInfo()
	 */
	public boolean userRegisterAccount(User u) {
		boolean flag = false;
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement pstmt = null;
		String sql = "insert into t_user_account values (?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, u.getAccount());
			pstmt.setString(2, u.getPassword());
			pstmt.setInt(3, u.getUserId());
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

	/**
	 * register user info first to get userId.
	 */
	public boolean userRegisterInfo(User u) {
		boolean flag = false;
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement pstmt = null;
		String sql = "insert into t_user_info values (user_id_sequence.nextval,?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, u.getUsername());
			pstmt.setString(2, u.getUserIntro());
			pstmt.setInt(3, u.getMsgCount());
			pstmt.setInt(4, u.getFansCount());
			pstmt.setInt(5, u.getFocusCount());
			int i = pstmt.executeUpdate();
			if (i == 1) {
				flag = true;
				u.setUserId(getUserId(u.getUsername()));
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, pstmt, null);
		}
		return flag;
	}

	/**
	 * get userId from userName
	 * 
	 * @param userName
	 * @return userId, -1 when user not exist.
	 */
	public int getUserId(String userName) {
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement pstmt = null;
		String sql = "select user_id from t_user_info where user_name=?";
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, pstmt, rs);
		}
		return -1;
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

	//插入消息元并且获得msgId
		public msgInfo insertTMsgInfo(msgInfo m){
			Connection conn=new ConnectionOracle().getConnection();
			PreparedStatement ps=null;			
			ResultSet rs=null;
			msgInfo m1 = null;
			String sql="insert into t_msg_info values(?,msg_id_sequence.nextval,?,?,?,?,to_date(?,'yyyy-MM-dd-HH-mi-ss'))";
			try {
				ps=conn.prepareStatement(sql);
				ps.setInt(1, m.getUserId());
				ps.setString(2, m.getContent());
				ps.setInt(3, m.getType());
				ps.setInt(4, m.getCommentCount());
				ps.setInt(5, m.getTransferCount());
				ps.setString(6,m.getTimeT());
				System.out.println("传输完毕");
				int i=ps.executeUpdate();
				ps=conn.prepareStatement("select msg_id_sequence.currval from dual");
				rs=ps.executeQuery();
				while (rs.next()) {
					int msgId= rs.getInt(1);
					m1=new msgInfo(msgId);
					System.out.println(m1.getMsgId());		
				}	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();	
			}finally{
				close(conn, ps, null);
			}
			return m1;
		}
	//select user_id from  t_user_account
		public User selectUserId(String username){
			Connection conn= new ConnectionOracle().getConnection();
			PreparedStatement ps=null;
			ResultSet rs=null;
			User u = null;
			String sql="select user_id from t_user_account where account=?";
			try {
				ps=conn.prepareStatement(sql);
				ps.setString(1, username);
				rs=ps.executeQuery();
				while (rs.next()) {
					int userId=rs.getInt(1);
					 u=new User(userId);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				close(conn, ps, rs);
			}
			return u;
		}
//		select follow_id from t_user_relation
		public List<Integer> selectFollowId(int userId){
			List<Integer>l=new ArrayList<Integer>();
			Connection conn=new ConnectionOracle().getConnection();
			PreparedStatement ps=null;
			ResultSet rs=null;
			String sql="select follow_id from t_user_relation where user_id=?and type=0";
			try {
				ps=conn.prepareStatement(sql);
				ps.setInt(1, userId);
				rs=ps.executeQuery();
				while (rs.next()) {
				int followId= rs.getInt(1);
				l.add(followId);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				close(conn, ps, rs);
			}
			return l;
		}
	
		//insert into t_user_msg_index values()
	public boolean insertTUserMsgIndex(int followId,int authorId,int msgId, String timeT){
		Connection conn=new ConnectionOracle().getConnection();
		PreparedStatement ps=null;
		String sql="insert into t_user_msg_index values(?,?,?,to_date(?,'yyyy-MM-dd-HH-mi-ss'))";
		try {
			ps=conn.prepareStatement(sql);
			ps.setInt(1, followId);
			ps.setInt(2, authorId);
			ps.setInt(3, msgId);
			ps.setString(4, timeT);
			int i=ps.executeUpdate();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			close(conn, ps, null);
		}
		
	}
}
