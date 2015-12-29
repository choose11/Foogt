package service.impl;

import java.util.List;

import service.IBlogService;
import dao.IBlogDao;
import dao.impl.IBlogDaoImpl;
import entity.BlogInfo;

public class IBlogServiceImpl implements IBlogService {
	IBlogDao dao = null;

	public IBlogServiceImpl() {
		dao = new IBlogDaoImpl();
	}

	public List<BlogInfo> selectBlogs(int userId, int pageSize, int page) {
		return dao.selectBlogs(userId,pageSize,page);
	}

}
