package com.manulife.pension.ps.web.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;



import com.manulife.pension.platform.web.controller.ApplicationFacade;
import com.manulife.pension.platform.web.util.CommonEnvironment;import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.pagelayout.LayoutBean;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.withdrawal.WebConstants;

public class PsApplicationFacade implements ApplicationFacade {
	private static final Logger logger = Logger.getLogger(PsApplicationFacade.class);
	private ServletContext context;

	public String createLayoutBean(HttpServletRequest request,
			String forward) {
        LayoutBean bean = null;

        if (forward != null) {
            bean = LayoutBeanRepository.getInstance().getPageBean(forward);
        }
        // if bean is null it means the request is not going
        // to be forwarded to jsp(one of the layout pages).
        if (bean != null) {
            forward = bean.getLayoutURL();
            request.setAttribute(Constants.LAYOUT_BEAN, bean);

            // for defined benefit contracts
            if ((SessionHelper.getUserProfile(request) != null)
                    && (SessionHelper.getUserProfile(request).getCurrentContract() != null)
                    && SessionHelper.getUserProfile(request).getCurrentContract().isDefinedBenefitContract()) {
                if (bean.getDefinedBenefitContentId() > 0) {
                    LayoutBean cloneBean = (LayoutBean) bean.clone();
                    cloneBean.setContentId(cloneBean.getDefinedBenefitContentId());
                    request.setAttribute(Constants.LAYOUT_BEAN, cloneBean);
                }
            }
            // for gifl version 3 content layout
            if ((SessionHelper.getUserProfile(request) != null)
                    && (SessionHelper.getUserProfile(request).getCurrentContract() != null)
                    && Constants.GIFL_VERSION_03.equals(SessionHelper.getUserProfile(request).getCurrentContract().getGiflVersion())) {
                if (bean.getGiflSelectContentId() > 0) {
                    LayoutBean cloneBean = (LayoutBean) bean.clone();
                    cloneBean.setContentId(cloneBean.getGiflSelectContentId());
                    request.setAttribute(Constants.LAYOUT_BEAN, cloneBean);
                }
            }
            
        } else {
        	if(request.getSession(false) != null){
        		request.getSession(false).setAttribute("loaded", false);}
            if (logger.isDebugEnabled()) {
                if (forward == null) {
                    logger.debug("createLayoutBean> Forward was NULL!");
                } else if (StringUtils.containsIgnoreCase(forward, ".jsp")) {
                    logger
                            .debug(new StringBuffer(
                                    "createLayoutBean> Layout bean not found for path containing '.jsp'.  Path [")
                                    .append(forward).append(
                                            "] may be missing from the page layout repository.")
                                    .toString());
                }
            }
        }

        return forward;
	}

	public CommonEnvironment getEnvironment() {
		return Environment.getInstance();
	}

	public void actionPreExecute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
        	session.removeAttribute(WebConstants.LAST_ACTIVE_PAGE_LOCATION);
        }
	}
	
	public void setServletContext(ServletContext context) {
		this.context = context;
	}

    public String getApplicationId() {
        return Constants.PS_APPLICATION_ID;
    }

}
