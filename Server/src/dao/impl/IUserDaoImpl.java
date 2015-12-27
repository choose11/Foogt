package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.org.apache.regexp.internal.recompile;

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

	public boolean checkUserExist(User u) {
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		String sql = "select * from ticketuser where user_name=?";
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

	public boolean userRegister(User u) {
		boolean flag = false;
		if (checkUserExist(u)) {
			return flag;
		} else {
			Connection conn = new ConnectionOracle().getConnection();
			PreparedStatement pstmt = null;
			String sql = "insert into ticketuser values (u_id.nextval,?,?)";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, u.getUsername());
				pstmt.setString(2, u.getPassword());
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
	}

	@Override
	public int queryUid(User u) {
		Connection conn = new ConnectionOracle().getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int id = 0;
		String sql = "select u_id from ticketuser where user_name=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, u.getUsername());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, pstmt, rs);
		}
		return id;
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
