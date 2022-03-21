package com.manulife.pension.ps.web.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping( value ="/login/")
public final class LoginController {
	
	@RequestMapping(method = RequestMethod.GET) 
	public String doDefault(HttpServletRequest request,HttpServletResponse response) {
		return "/login/login.jsp";
	}
	
}
