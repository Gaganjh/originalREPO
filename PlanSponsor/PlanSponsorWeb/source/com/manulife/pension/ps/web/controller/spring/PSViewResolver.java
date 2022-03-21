package com.manulife.pension.ps.web.controller.spring;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.manulife.pension.platform.utility.CommonConstants;
import com.manulife.pension.platform.web.controller.ApplicationFacade;

public class PSViewResolver extends InternalResourceViewResolver {

	private static Map<String, String> viewResolverMap = new HashMap<String, String>();
	private HttpServletRequest request = null;
	static {

		 viewResolverMap.put("defaultlayout","/WEB-INF/global/layout/defaultlayout.jsp");
		 viewResolverMap.put("standardlayout","/WEB-INF/global/layout/standardlayout.jsp");
		 viewResolverMap.put("publichomelayout","/WEB-INF/global/layout/publichomelayout.jsp");
		 viewResolverMap.put("headerfooterlayout","/WEB-INF/global/layout/headerfooterlayout.jsp");
		 viewResolverMap.put("tpabobcsvpagelayout","/WEB-INF/global/layout/tpabobcsvpagelayout.jsp");
		 viewResolverMap.put("registrationlayout","/WEB-INF/global/layout/registrationlayout.jsp");
	}
	
	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		View view;
		request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		viewName = getApplicationFacade(request).createLayoutBean(request, viewName);
		if (viewResolverMap.get(viewName) != null)
			view = super.resolveViewName(viewResolverMap.get(viewName), locale);
		else
			view = super.resolveViewName(viewName, locale);

		return view;
	}

	protected ApplicationFacade getApplicationFacade(HttpServletRequest request) {
		return (ApplicationFacade) request.getServletContext().getAttribute(CommonConstants.APPLICATION_FACADE_KEY);
	}

}
