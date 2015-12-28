package dao;

import java.util.List;

import entity.User;

public interface IUserDao {
	public boolean checkAccountExist(User u);

	public boolean userRegisterAccount(User u);

	public boolean userRegisterInfo(User u);

	public boolean userLogin(User u);

	public boolean changePW(User u, String newPW);

	public List<User> searchUser(String keyword);
	
	public User searchData(int userId);
	
	public boolean updateUserData(int userId,String userName,String userIntro);

}
