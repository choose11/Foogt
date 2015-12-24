package dao;

import entity.User;

public interface IUserDao {
	public boolean checkAccountExist(User u);

	public boolean userRegisterAccount(User u);
	
	public boolean userRegisterInfo(User u);

	public boolean userLogin(User u);

	public boolean changePW(User u, String newPW);

}
