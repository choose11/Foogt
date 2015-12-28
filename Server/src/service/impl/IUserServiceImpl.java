package service.impl;

import java.util.List;

import service.IUserService;
import dao.IUserDao;
import dao.impl.IUserDaoImpl;
import entity.User;

public class IUserServiceImpl implements IUserService {

	IUserDao dao = null;

	public IUserServiceImpl() {
		dao = new IUserDaoImpl();
	}

	public boolean changePW(User u, String newPW) {
		return dao.changePW(u, newPW);
	}

	public boolean checkAccountExist(User u) {
		return dao.checkAccountExist(u);
	}

	public boolean userLogin(User u) {
		return dao.userLogin(u);
	}

	/**
	 * register user info first to get userId, then register account.
	 */
	public boolean userRegister(User u) {
		if (!dao.checkAccountExist(u)) {
			// here checked so dao don't need to check again
			dao.userRegisterInfo(u);
			return dao.userRegisterAccount(u);
		} else {
			return false;
		}
	}

	/*
	 * search user,request a List
	 */
	public List<User> searchUser(String keyword) {
		return dao.searchUser(keyword);
	}

	/*
	 * edit user data
	 * 
	 * @see service.IUserService#dataEdit(int)
	 */

	public User searchData(int userId) {

		return dao.searchData(userId);
	}

	/**
	 * save use data
	 */
	public boolean updateUserData(int userId, String userName, String userIntro) {

		return dao.updateUserData(userId, userName, userIntro);
	}

	/**
	 * Test Mod
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		IUserServiceImpl i = new IUserServiceImpl();
		User u = new User("eric", "eric");
		// System.out.println("CheckExist\t" + i.checkAccountExist(u));
		// System.out.println("Regist\t" + i.userRegister(u));
		// System.out.println("Login\t" + i.userLogin(u));
		// System.out.println("ChangePW\t" + i.changePW(u, "passwd"));
		// System.out.println("Login\t" + i.userLogin(u));
		// u.setPassword("passwd");
		// System.out.println("newLogin\t" + i.userLogin(u));
		// List<User> user = i.searchUser("eric");
		// for (User string : user) {
		// System.out.println(string);
		// }
		// System.out.println(i.searchData(2).getUsername()+" : "+i.searchData(2).getUserIntro());

		System.out.println(i.updateUserData(12, "张三", "今天写了好多啊"));
	}
}
