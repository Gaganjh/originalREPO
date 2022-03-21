package com.manulife.pension.ps.web.resources;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.SystemException;

@Controller
@RequestMapping(value = "/resources")
public class searchLiteratureController {
	
	public static final Map<String,String> forwards = new HashMap<>() ;
	static{
		forwards.put("default","/resources/searchLiterature.jsp");
		}
	@RequestMapping(value ="/searchLiterature/", method =  {RequestMethod.GET}) 
	 public String doDefault(HttpServletRequest request,
				HttpServletResponse response) throws SystemException {
		return forwards.get("default");
	}
}
