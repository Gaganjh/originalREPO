package com.manulife.pension.ps.web.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.delegate.LoadTestServiceDelegate;
import com.manulife.pension.util.BaseEnvironment;

/**
 * This servlet is used to do load test for Stored Proc via Web Load test tool
 * 
 * The environment variable of "storedProcInvocationEnabled" has to be true
 * to enable this servlet
 * 
 * @author guweigu
 *
 */
public class StoredProcInvocationServlet extends HttpServlet {

	private static final String EnableStoredProcInvocation = "storedProcInvocationEnabled";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
		boolean enabled = false;
		try {
			BaseEnvironment env = new BaseEnvironment();
			String value = env.getNamingVariable(EnableStoredProcInvocation, null);
			if (StringUtils.equalsIgnoreCase("true", value)) {
				enabled = true;
			}
		} catch (Exception e) {
			// the naming variable is not found
			// just keep it disabled
		}
		if (!enabled) {
			throw new UnavailableException("This servlet is not available in this environment");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, String> inputValues = new HashMap<String, String>();
		String schemaName = req.getParameter("schemaName");
		String storedProcName = req.getParameter("storedProcName");
		@SuppressWarnings("rawtypes")
		Enumeration parameters = req.getParameterNames();
		while (parameters.hasMoreElements()) {
			String name = (String) parameters.nextElement();
			inputValues
					.put(StringUtils.upperCase(name), req.getParameter(name));
		}
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		try {
			Map<String, Object> output = LoadTestServiceDelegate
					.getInstance().invokeStoredProc(schemaName, storedProcName,
							inputValues);
			writer.println("Result:");
			for (String name : output.keySet()) {
				writer.print(name);
				writer.print("=");
				writer.println(output.get(name));
			}
		} catch (SystemException ex) {
			writer.println("Failed!");
			ex.printStackTrace(writer);
		}
		writer.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
