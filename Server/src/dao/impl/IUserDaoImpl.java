package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.LogUtil;

import dao.IUserDao;
import db.ConnectionOracle;
import entity.User;

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
	 * @return true if user exist
	 */
	public boolean checkUserExist(User u) {
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		String sql = "select * from t_user_info where user_name=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, u.getUsername());
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
		String sql = "select * from ticketuser where user_name=? and user_password=?";
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
	 * TODO: show method obviously userId is set after userRegisterInfo()
	 * executed. so this method must execute after userRegisterInfo()
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
		if (checkUserExist(u)) {
			return flag;
		} else {
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

}
