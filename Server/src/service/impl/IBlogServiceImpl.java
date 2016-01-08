package service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import service.IBlogService;
import servlet.MsgInfo;
import dao.IBlogDao;
import dao.impl.IBlogDaoImpl;
import entity.BlogInfo;
import entity.User;
import entity.msgInfo;
import entity.msgRelation;

public class IBlogServiceImpl implements IBlogService {
	public static final int pageSize = 5;
	IBlogDao dao = null;

	public IBlogServiceImpl() {
		dao = new IBlogDaoImpl();
	}

	@Override
	public List<BlogInfo> selectBlogs(int userId, int page) {
		List<BlogInfo> list = dao.selectBlogs(userId, pageSize, page);
		for (BlogInfo b : list) {
			b.setCollected(checkCollected(userId, b.getMsgId()));
		}
		return list;
	}

	@Override
	public List<BlogInfo> selectUserOwnBlogs(int userId, int page) {
		return dao.selectUserOwnBlogs(userId, pageSize, page);
	}

	@Override
	public boolean collectBlog(int uid, int msgId) {
		if (checkCollected(uid, msgId)) {
			return true;
		}
		return dao.collectBlog(uid, msgId);
	}

	@Override
	public List<BlogInfo> getCollections(int userId, int page) {
		List<BlogInfo> list = dao.getCollections(userId, pageSize, page);
		for (BlogInfo b : list) {
			b.setCollected(true);
		}
		return list;
	}

	@Override
	public msgInfo insertTMsgInfo(msgInfo m) {
		return dao.insertTMsgInfo(m);
	}

	@Override
	public boolean insertTUserMsgIndex(int followId, int authorId, int msgId,
			String timeT) {
		return dao.insertTUserMsgIndex(followId, authorId, msgId, timeT);
	}

	@Override
	public boolean commentBlog(int userId, int msgId, int msgAuthorId,
			String comment, Date time) {
		System.out.println(msgId);
		msgInfo m = new msgInfo(userId, comment, msgInfo.COMMENT, 0, 0,
				new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss").format(time));
		m = dao.insertTMsgInfo(m);
		System.out.println(msgId);
		boolean result = dao.insertTMsgMsgRelation(userId, m.getMsgId(),
				msgAuthorId, msgId, msgInfo.COMMENT, time);
		return result;
	}

	@Override
	public List<BlogInfo> getHotBlogs(int page) {
		return dao.getHotBlogs(page);
	}
	
	@Override			
	public List<msgRelation> selectRelation(int userId) {
		// TODO Auto-generated method stub
		return dao.selectRelation(userId);
	}

	@Override
	public boolean checkCollected(int userId, int msgId) {
		return dao.checkCollected(userId, msgId);
	}

	
	public static void main(String[] args) {
		IBlogServiceImpl i = new IBlogServiceImpl();
//		User u = new User("eric", "eric");
//		List<msgRelation>l= i.selectRelation(2);
//		System.out.println(l.get(0).getContent2());
	

	}
}
