package factory;

import service.impl.IUserServiceImpl;

public class Factory {
	public static IUserServiceImpl getIUserService() {
		return new IUserServiceImpl();
	}
}