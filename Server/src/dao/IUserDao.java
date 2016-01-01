package dao;

import java.util.List;

import entity.User;

public interface IUserDao {
	public boolean checkAccountExist(User u);

	public boolean userRegisterAccount(User u);

	public boolean userRegisterInfo(User u);

	public boolean userLogin(User u);

	public boolean changePW(User u, String newPW);

	public List<User> searchUser(String keyword, int limit);

	public User searchData(int userId);

	public User searchAccount(int userId);

	public boolean updateUserData(int userId, String userName, String userIntro);

	public boolean insertTUserRelation(int cuid, int fuid, int type);
}
