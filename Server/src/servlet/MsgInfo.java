package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.IBlogService;
import service.IUserService;

import dao.impl.IUserDaoImpl;
import entity.msgInfo;
import factory.Factory;

public class MsgInfo extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public MsgInfo() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String userId1= request.getParameter("userId");
		int userId=Integer.valueOf(userId1);
		System.out.println(userId);
		String content= request.getParameter("content");
		System.out.println(content);
		String type1= request.getParameter("type");
		int type=Integer.valueOf(type1);
		System.out.println(type);
		String commentCount1= request.getParameter("commentCount");
		int commentCount=Integer.valueOf(commentCount1);
		System.out.println(commentCount);
		String transferCount1= request.getParameter("transferCount");
		int transferCount=Integer.valueOf(transferCount1);
		System.out.println(transferCount);
		String timeT= request.getParameter("timeT");
		System.out.println(timeT);	
//		 insert data and get msgId
		msgInfo m=new msgInfo(userId, content, type, commentCount, transferCount, timeT);
		IBlogService blogService = Factory.getIBlogService();
		IUserService userService = Factory.getIUserService();
		msgInfo re = blogService.insertTMsgInfo(m);
		int msgId=re.getMsgId();
		//select follow_id 
		List<Integer>l=  userService.selectFollowId(userId);
		for (int j = 0; j < l.size(); j++) {
			int followId= l.get(j);
		boolean b=	blogService.insertTUserMsgIndex(followId, userId, msgId, timeT);
			if (b==true) {
				System.out.println("成功");
				continue;
			}else if (b==false){
			System.out.println("失败");
			}
		}
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
