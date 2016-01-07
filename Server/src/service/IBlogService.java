package service;

import java.util.Date;
import java.util.List;

import entity.BlogInfo;
import entity.msgInfo;

public interface IBlogService {

	public List<BlogInfo> selectBlogs(int userId, int page);

	public List<BlogInfo> selectUserOwnBlogs(int userId, int page);
	
	public List<BlogInfo> getHotBlogs(int page);
	
	public boolean collectBlog(int uid, int msgId);

	public List<BlogInfo> getCollections(int userId, int page);

	public msgInfo insertTMsgInfo(msgInfo m);

	public boolean insertTUserMsgIndex(int followId, int authorId, int msgId,
			String timeT);
	
	public boolean commentBlog(int userId, int msgId, int msgAuthorId, String comment,Date time);
}
