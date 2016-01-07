package service.impl;

import java.util.List;

import service.IUserService;
import dao.IUserDao;
import dao.impl.IUserDaoImpl;
import entity.User;

public class IUserServiceImpl implements IUserService {

	public static final int SEARCH_USER_LIMIT = 5;
	public static final int USER_RELATION_FAN = 0;
	public static final int USER_RELATION_FOCUS = 1;

	IUserDao dao = null;

	public IUserServiceImpl() {
		dao = new IUserDaoImpl();
	}

	@Override
	public boolean changePW(User u, String newPW) {
		return dao.changePW(u, newPW);
	}

	@Override
	public boolean checkAccountExist(User u) {
		return dao.checkAccountExist(u);
	}

	@Override
	public boolean userLogin(User u) {
		return dao.userLogin(u);
	}

	/**
	 * register user info first to get userId, then register account.
	 */
	@Override
	public boolean userRegister(User u) {
		if (!dao.checkAccountExist(u)) {
			// here checked so dao don't need to check again
			dao.userRegisterInfo(u);
			return dao.userRegisterAccount(u);
		} else {
			return false;
		}
	}

	/**
	 * search user,request a List
	 */
	@Override
	public List<User> searchUser(String keyword) {
		return dao.searchUser(keyword, SEARCH_USER_LIMIT);
	}

	/**
	 * edit user data
	 * 
	 * @see service.IUserService#dataEdit(int)
	 */
	@Override
	public User searchData(int userId) {

		return dao.searchData(userId);
	}

	/**
	 * save use data
	 */
	@Override
	public boolean updateUserData(int userId, String userName, String userIntro) {

		return dao.updateUserData(userId, userName, userIntro);
	}

	/**
	 * Add Focus User.
	 * 
	 * @param cuid
	 *            currentUserId
	 * @param fuid
	 *            focusUserId
	 */
	@Override
	public boolean insertTUserRelation(int cuid, int fuid) {
		boolean i1 = dao.insertTUserRelation(cuid, fuid, USER_RELATION_FOCUS);
		boolean i2 = dao.insertTUserRelation(fuid, cuid, USER_RELATION_FAN);
		
		return i1 && i2;
	}

	/**
	 * search user account
	 */
	@Override
	public User searchAccount(int userId) {

		return dao.searchAccount(userId);
	}

	/**
	 * seatch focus
	 */
	@Override
	public List<User> searchFocus(int userId, int pageSize, int page) {

		return dao.searchFocus(userId, pageSize, page);
	}

	/**
	 *search fans
	 */
	@Override
	public List<User> searchFans(int userId, int pageSize, int page) {

		return dao.searchFans(userId, pageSize, page);
	}

	@Override
	public List<Integer> selectFollowId(int userId) {
		return dao.selectFollowId(userId);
	}
	

	@Override
	public boolean checkHeadImg(int uid) {
		return dao.checkHeadImg(uid);
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
		// System.out.println(i.searchData(10).getUsername() + " : "
		// + i.searchData(10).getUserIntro());
		// System.out.println(i.updateUserData(12, "张三", "今天写了好多啊"));
		//System.out.println("updateMsgCount\t"+i.updateMsgCount(2));

	}

	@Override
	public boolean updateMsgCount(int userId) {
		// TODO Auto-generated method stub
		return dao.updateMsgCount(userId);
	}

}
