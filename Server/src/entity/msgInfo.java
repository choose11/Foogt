package entity;

import java.sql.Date;

public class msgInfo {
	private int userId;
	private int msgId;
	private String content;
	private int type;
	private int commentCount;
	private int transferCount;
	private String timeT;
	
	public String getTimeT() {
		return timeT;
	}
	public void setTimeT(String timeT) {
		this.timeT = timeT;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getMsgId() {
		return msgId;
	}
	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public int getTransferCount() {
		return transferCount;
	}
	public void setTransferCount(int transferCount) {
		this.transferCount = transferCount;
	}

	public msgInfo(int userId, int msgId, String content, int type,
			int commentCount, int transferCount, String timeT) {
		super();
		this.userId = userId;
		this.msgId = msgId;
		this.content = content;
		this.type = type;
		this.commentCount = commentCount;
		this.transferCount = transferCount;
		this.timeT = timeT;
	}
	public msgInfo(int userId, String content, int type, int commentCount,
			int transferCount, String timeT) {
		super();
		this.userId = userId;
		this.content = content;
		this.type = type;
		this.commentCount = commentCount;
		this.transferCount = transferCount;
		this.timeT = timeT;
	}
	public msgInfo(int msgId) {
		super();
		this.msgId = msgId;
	}
		
	
}
