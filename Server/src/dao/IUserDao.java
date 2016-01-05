package dao;

import java.util.List;

import entity.User;
import entity.msgInfo;

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

	public List<User> searchFans(int userId, int pageSize, int page);

	public List<User> searchFocus(int userId, int pageSize, int page);

	public boolean insertTUserRelation(int cuid, int fuid, int type);

	public List<Integer> selectFollowId(int userId);

	public User selectUserId(String username);

	public int getUserId(String userName);
	
	public boolean checkHeadImg(int uid);
}
