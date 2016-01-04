package service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import service.IBlogService;
import servlet.MsgInfo;
import dao.IBlogDao;
import dao.impl.IBlogDaoImpl;
import entity.BlogInfo;
import entity.msgInfo;

public class IBlogServiceImpl implements IBlogService {
	public static final int pageSize = 5;
	IBlogDao dao = null;

	public IBlogServiceImpl() {
		dao = new IBlogDaoImpl();
	}

	@Override
	public List<BlogInfo> selectBlogs(int userId, int page) {
		return dao.selectBlogs(userId, pageSize, page);
	}

	@Override
	public List<BlogInfo> selectUserOwnBlogs(int userId, int page) {
		return dao.selectUserOwnBlogs(userId, pageSize, page);
	}

	@Override
	public boolean collectBlog(int uid, int msgId) {
		return dao.collectBlog(uid, msgId);
	}

	@Override
	public List<BlogInfo> getCollections(int userId, int page) {
		return dao.getCollections(userId, pageSize, page);
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
		msgInfo m = new msgInfo(userId, comment, msgInfo.COMMENT, 0, 0, new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss")
		.format(time));
		m = dao.insertTMsgInfo(m);
		System.out.println(msgId);
		boolean result = dao.insertTMsgMsgRelation(userId, m.getMsgId(),
				msgAuthorId, msgId, msgInfo.COMMENT, time);
		return result;
	};

}
