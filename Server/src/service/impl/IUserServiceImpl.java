package service.impl;

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

	public boolean checkUserExist(User u) {
		return dao.checkUserExist(u);
	}

	public boolean userLogin(User u) {
		return dao.userLogin(u);
	}

	/**
	 * register user info first to get userId, then register account.
	 */
	public boolean userRegister(User u) {
		if (!dao.checkUserExist(u)) {
			dao.userRegisterInfo(u);
			return dao.userRegisterAccount(u);
		} else {
			return false;
		}
	}

	/**
	 * Test Mod
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		IUserServiceImpl i = new IUserServiceImpl();
		User u = new User("eric", "eric");
		System.out.println("CheckExist\t" + i.checkUserExist(u));
		System.out.println("Regist\t" + i.userRegister(u));
		System.out.println("Login\t" + i.userLogin(u));
		System.out.println("ChangePW\t" + i.changePW(u, "passwd"));
		System.out.println("Login\t" + i.userLogin(u));
		u.setPassword("passwd");
		System.out.println("newLogin\t" + i.userLogin(u));
	}
}
