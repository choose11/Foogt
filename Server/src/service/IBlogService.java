package service;

import java.util.List;

import entity.BlogInfo;

public interface IBlogService {

	public List<BlogInfo> selectBlogs(int userId, int pageSize, int page);

}
