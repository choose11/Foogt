package dao;

import java.util.List;

import entity.BlogInfo;

public interface IBlogDao {

	public List<BlogInfo> selectBlogs(int userId, int pageSize, int page);

}
