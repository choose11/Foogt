package service;

import entity.User;

public interface IUserService {
	public boolean checkUserExist(User u);
	public boolean userRegister(User u);
	public boolean userLogin(User u);
	public boolean changePW(User u,String newPW);
	public int queryUid(User u);
}
