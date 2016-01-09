package factory;

import service.IBlogService;
import service.IUserService;
import service.impl.IBlogServiceImpl;
import service.impl.IUserServiceImpl;

public class Factory {
	public static IUserService getIUserService() {
		return new IUserServiceImpl();
	}
	
	public static IBlogService getIBlogService() {
		return new IBlogServiceImpl();
	}
}