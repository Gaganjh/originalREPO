package com.manulife.pension.bd.web.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.pagelayout.LayoutStore;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.platform.web.controller.ApplicationFacade;
import com.manulife.pension.platform.web.util.CommonEnvironment;

public class BDApplicationFacade implements ApplicationFacade {

	private static final Logger logger = Logger
			.getLogger(BDApplicationFacade.class);
	private ServletContext context;

	public String createLayoutBean(HttpServletRequest request,
			String forward) {
		BDLayoutBean bean = null;
		
		if (forward != null) {
			LayoutStore layoutStore = ApplicationHelper.getLayoutStore(context);
			bean = layoutStore.getLayoutBean(forward,
					request);
		}
		// if bean is null it means the request is not going
		// to be forwarded to jsp(one of the layout pages).
		if (bean != null) {
			//forward = mapping.findForward(bean.getLayout());
			forward=bean.getLayout();
			request.setAttribute(BDConstants.LAYOUT_BEAN, bean);
		}

		if (logger.isDebugEnabled()) {
			if (forward == null) {
				logger.debug("createLayoutBean> Forward was NULL!");
			} else if (StringUtils
					.containsIgnoreCase(forward, ".jsp")) {
				logger
						.debug(new StringBuffer(
								"createLayoutBean> Layout bean not found for path containing '.jsp'.  Path [")
								.append(forward)
								.append("] may be missing from the pages xml .")
								.toString());
			}
		}

		return forward;
	}

	public CommonEnvironment getEnvironment() {
		return Environment.getInstance();
	}

	public void setServletContext(ServletContext context) {
		this.context = context;
	}

	public void actionPreExecute(HttpServletRequest request) {
	}
	
    public String getApplicationId() {
        return BDConstants.BD_APPLICATION_ID;
    }
}
