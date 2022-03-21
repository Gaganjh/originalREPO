package com.manulife.pension.bd.web.controller.spring;

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

public class BdViewResolver extends InternalResourceViewResolver {

	private static Map<String, String> viewResolverMap = new HashMap<String, String>();
	private HttpServletRequest request = null;
	static {

		 viewResolverMap.put("commonLayout","/WEB-INF/layouts/common_layout.jsp");
		 viewResolverMap.put("common_layout_no_menu","/WEB-INF/layouts/common_layout_no_menu.jsp");
		 viewResolverMap.put("publicLayout","/WEB-INF/layouts/public_layout.jsp");
		 viewResolverMap.put("blankLayout","/WEB-INF/layouts/blank_layout.jsp");
		 viewResolverMap.put("printLayout","/WEB-INF/layouts/print_layout.jsp");
		 viewResolverMap.put("registrationLayout","/WEB-INF/layouts/registration_layout.jsp");
		 viewResolverMap.put("fapLayout","/WEB-INF/layouts/fap_layout.jsp");
		 viewResolverMap.put("fapTabLayout","/WEB-INF/layouts/fap_tab_layout.jsp");
		 viewResolverMap.put("noNavRegLayout","/WEB-INF/layouts/nonav_reg_layout.jsp");
		 viewResolverMap.put("template1BodyLayout","/WEB-INF/layouts/template1Body_layout.jsp");
		 viewResolverMap.put("template3BodyLayout","/WEB-INF/layouts/template3Body_layout.jsp");
		 viewResolverMap.put("noNavTemplate3BodyLayout","/WEB-INF/layouts/nonav_template3Body_layout.jsp");
		 viewResolverMap.put("searchResult","/WEB-INF/firmsearch/firmSearchResult.jsp");
		 viewResolverMap.put("errorPage","/WEB-INF/layouts/error_template_layout.jsp");
		 viewResolverMap.put("error","/home/public_home.jsp");
		 viewResolverMap.put("fail","/do/home/");
		 viewResolverMap.put("csrfErrorPage","/security/CSRFerrorpage.jsp");
		 viewResolverMap.put("systemErrorPage","/security/CSRFerrorpage.jsp");
		 
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
