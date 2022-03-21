package com.manulife.pension.bd.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * RequestHandler to handle special url
 * 
 * @author guweigu
 *
 */
public interface RequestHandler {
	void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException;
}
