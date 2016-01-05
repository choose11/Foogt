package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import factory.Factory;

public class GetHeadIMG extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		ServletContext cntx = getServletContext();
		int uid = Integer.valueOf(request.getParameter("uid"));
		boolean result = Factory.getIUserService().checkHeadImg(uid);
		String filename;
		if (result) {
			filename = cntx.getRealPath("HeaderImgs/" + "uid" + ".png");
		} else {
			filename = cntx.getRealPath("HeaderImgs/default.jpg");
		}
		File f = new File(filename);
		String mime = cntx.getMimeType(filename);
		response.setContentType(mime);
		response.setContentLength((int) f.length());

		FileInputStream fin = new FileInputStream(f);
		OutputStream out = response.getOutputStream();

		byte[] buf = new byte[1024];
		int count = 0;
		while ((count = fin.read(buf)) >= 0) {
			out.write(buf, 0, count);
		}
		out.close();
		fin.close();
	}

}
