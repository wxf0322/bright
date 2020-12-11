/**  
 * 项目名称  ：  bright
 * 文件名称  ：  PushAction.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bright.push.net.Command;
import com.bright.push.net.Controller;
import com.bright.push.net.Invoker;
import com.bright.push.net.Restart;
import com.bright.push.net.ServerController;
import com.bright.push.net.Start;
import com.bright.push.net.Status;
import com.bright.push.net.Stop;

/**
 * 服务器管理服务。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class PushAction extends HttpServlet {

	/**
	 * serialVersionUID:序列
	 */
	private static final long serialVersionUID = 2295367252770928711L;
	/** 日志对象 */
	private Log log = LogFactory.getLog(PushAction.class);
	private Controller controller = new ServerController();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		new Invoker(new Start(controller)).action();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		log.debug("执行命令：" + action);
		Command command = null;
		switch (action) {
		case "start":
			command = new Start(controller);
			introspector(response);
			break;
		case "stop":
			command = new Stop(controller);
			introspector(response);
			break;
		case "restart":
			command = new Restart(controller);
			introspector(response);
			break;
		case "status":
			command = new Status(controller);
			introspector(response);
			break;
		default:
			command = new Status(controller);
			introspector(response);
			break;
		}
		new Invoker(command).action();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * introspector (显示状态)
	 * 
	 * @param response 响应
	 * @throws IOException void
	 */
	private void introspector(HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().append(controller.status());
	}

}
