package dao;

import java.util.Date;
import java.util.List;

import entity.BlogInfo;
import entity.msgInfo;

public interface IBlogDao {

	public List<BlogInfo> selectBlogs(int userId, int pageSize, int page);

	public List<BlogInfo> selectUserOwnBlogs(int userId, int pageSize, int page);

	public boolean collectBlog(int uid, int blogId);
	
	public List<BlogInfo> getCollections(int userId,int pageSize, int page);
	
	public msgInfo insertTMsgInfo(msgInfo m);
	
	public boolean insertTUserMsgIndex(int followId, int authorId, int msgId,
			String timeT);
	
	public boolean insertTMsgMsgRelation(int referenceId,int referenceMsgId,int referencedId,int referencedMsgId,int type,Date time);

	public List<BlogInfo> getHotBlogs(int page);
}
