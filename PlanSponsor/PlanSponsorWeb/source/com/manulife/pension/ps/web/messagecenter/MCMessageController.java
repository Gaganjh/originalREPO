package com.manulife.pension.ps.web.messagecenter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.util.MCEnvironment;
import com.manulife.pension.ps.web.messagecenter.util.MCUrlGenerator;
import com.manulife.pension.ps.web.messagecenter.util.MCUrlGeneratorImpl;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessageDetail;
import com.manulife.pension.service.message.valueobject.MessageUrl;
import com.manulife.pension.service.message.valueobject.RecipientMessageInfo;

/**
 * Action for a particular Message
 * 
 * @author guweigu
 * 
 */
abstract public class MCMessageController extends MCAbstractController implements
		MCConstants {

	private MCUrlGenerator urlGenerator = new MCUrlGeneratorImpl();

	protected MCMessageController() {
		logger = Logger.getLogger(MCMessageController.class);
	}

	abstract protected void doAction(HttpServletRequest request,
			RecipientMessageInfo message) throws SystemException;

	public String doExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		//this function must work for internal and external users
		//since only 1 url exists in the database. 
		if (SessionHelper.getUserProfile(request).getRole().isInternalUser()
				&& !SessionHelper.getUserProfile(request).isBundledGACAR()) {
			return doInternalUser(request, form,  response, request.getServletContext());
		}
		
		
		
		if (!MCEnvironment.isMessageCenterAvailable(request)) {
			//return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD);
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		int messageId = MCUtils.getId(request, ParamMessageId);

		if (messageId == -1) {
			return gotoTab( request, response, null, request.getServletContext());
		}

		UserProfile user = SessionHelper.getUserProfile(request);

		RecipientMessageInfo message = MessageServiceFacadeFactory.getInstance(request.getServletContext()).getRecipientMessageById(messageId, user);

		if (message != null
				&& (validateContract(user.getMessageCenterAccessibleContracts(), message) || validateFirm(user
						.getMessageCenterTpaFirms(), message))) {
			doAction(request, message);
			//ActionForward forward = gotoNext( request, response, message);
			String  forward = gotoNext( request, response, message, request.getServletContext());
			return forward;
		} else if (SessionHelper.getUserProfile(request).isBundledGACAR()
				&& message == null) {
			
			// BGA users will not get message for a non BGA contract and BGA
			// users should act as an internal user for non BGA contracts.
			return doInternalUser(request, form,  response, request.getServletContext());
		} else {
			// not a message that can be accessed
			ControllerRedirect forward = new ControllerRedirect(urlGenerator.getTabUrl(SummaryTab));
			return forward.getPath();
		}

	}

	private boolean validateFirm(Collection<Integer> messageCenterTpaFirms, RecipientMessageInfo message) {
		if ( message.getContractId() != null )
			return false; //firm messages must have contract id null
		if ( message.getTpaFirmId() == null ) 
			return false;
		return messageCenterTpaFirms.contains(message.getTpaFirmId());
	}

	private boolean validateContract(Set<Integer> messageCenterAccessibleContracts, RecipientMessageInfo message) {
		if ( message.getContractId() == null ) 
			return false;  //for firm messages
		return messageCenterAccessibleContracts.contains(new Integer(message.getContractId()));
	}

	/**
	 * 
	 * 
	 * @param request
	 * @param form
	 * @param mapping 
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	private String doInternalUser(HttpServletRequest request, ActionForm form,
			HttpServletResponse response,ServletContext servlet) throws SystemException {
		int messageId = MCUtils.getId(request, ParamMessageId);
		String urlType = request.getParameter(ParamUrlType);

		MessageDetail detail = MessageServiceFacadeFactory.getInstance(servlet).getMessageDetails(messageId,
				(int) getUserProfile(request).getPrincipal().getProfileId(), Environment.getInstance().getAppId());
		//should never be action url
		if ( StringUtils.equals("INF", urlType)) {
			MessageUrl url = detail.getInfoUrl();
			ControllerRedirect forward= new ControllerRedirect(url.getUrl());
			return forward.getPath();
		} else {
			ControllerRedirect forward= new ControllerRedirect(MCConstants.CarViewUrl);
			return forward.getPath();
		}
	}
	
	protected String gotoNext(
			HttpServletRequest request, HttpServletResponse response,
			RecipientMessageInfo message,ServletContext servlet) throws IOException, ServletException,
			SystemException {
		return gotoTab( request, response, message, servlet);
	}
	
	protected String gotoTab(
			HttpServletRequest request, HttpServletResponse response,
			RecipientMessageInfo message,ServletContext servlet) throws IOException, ServletException,
			SystemException {
		int tabId = MCUtils.getId(request, ParamTabId);

		MessageCenterComponent top = MCUtils.getMessageCenterTree(servlet);
		MessageCenterComponent selectedTab = top.getChild(tabId);

		if (selectedTab == null) {
			return gotoTabMessage(request, SummaryTab);
		} else {
			int sectionId = MCUtils.getId(request, ParamSectionId);
			if (sectionId > 0) {
				ControllerRedirect forward = new ControllerRedirect(urlGenerator.getDetailSectionUrl(selectedTab, selectedTab.getChild(sectionId)));
				return forward.getPath();
			} else {
				return gotoTabMessage(request, selectedTab);
			}
		}
	}

	private String gotoTabMessage(HttpServletRequest request,
			MessageCenterComponent tab) {
		String messageAnchor = request.getParameter(ParamMessageAnchor);
		ControllerRedirect forward = new ControllerRedirect(urlGenerator.getTabMessageUrl(tab, messageAnchor));
		return forward.getPath();
	}
}
