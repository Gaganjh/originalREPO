package com.manulife.pension.bd.web.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.GenericException;

/**
 * Base Action class for all reporting sub classes.
 */
public abstract class BDReportController extends BaseReportController {
	
	
	public static final Logger logger = Logger.getLogger(BDReportController.class);
		
	protected BDReportController(Class clazz) {
		super(clazz);
	}
	
    /**
     * This method needs to be overriden by any Report that needs to display any error, warning, informational
     * messages. This method will be used to check for conditions for displaying error, warning, 
     * informational messages. 
     * @param report
     * @param reportForm
     * @param request
     */
    protected void validateReportData(ReportData report, 
			BaseReportForm reportForm,
            HttpServletRequest request) throws SystemException {
    }
	/**
	 * The common method used by all other doXXX() methods. This method
	 * basically performs the following tasks:
	 * <ol>
	 * <li>Populates a report action form to its default state.
	 * (populateReportForm)</li>
	 * <li>If validateForm is true, invoke the validate() method on the action
	 * form.</li>
	 * <li>Populates a report criteria based on the action form.
	 * (getReportCriteria and populateReportCriteria)</li>
	 * <li>Obtains report data from the delegate (getReportData)</li>
	 * <li>Stores the report data object into the request</li>
	 * </ol>
	 * <p>
	 * All of the reporting framework tasks have a very similar workflow and
	 * they all reuse this method. Please refer to the individual Javadoc
	 * comments for any variation.
	 * 
	 * @param mapping
	 *            The Struts Action Mapping object.
	 * @param reportForm
	 *            The current report form.
	 * @param request
	 *            The current request object.
	 * @param response
	 *            The current response object.
	 * @return The ActionForward appropriate for this task. If validateForm is
	 *         true and the validation fails, the ActionMapping's input page is
	 *         returned.
	 * @throws IOException
	 * @throws ServletException
	 */
	protected String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws 
			SystemException {

		String reportId = getReportId();

		/*
		 * If the report form is stored in the session and its report ID is
		 * different from this Action's report ID, we clear the form's content.
		 */
		/*
		 * removed -- see resetForm() call in doDefault
		 * 
		 * if (mapping.getScope().equals("session") &&
		 * !reportId.equals(reportForm.getReportId())) { reportForm.clear();
		 * reportForm.setReportId(reportId); }
		 */

		String task = getTask(request);
		populateReportForm( reportForm, request);
		ReportCriteria criteria = getReportCriteria(reportId, reportForm,
				request);
		try {
			ReportData report = getReportData(reportId, criteria, request);
			request.setAttribute(BDConstants.REPORT_BEAN, report);
			populateCappingCriteria(report, reportForm, request);
			validateReportData(report, reportForm, request);
		} catch (ReportServiceException e) {
			logger.error("Received a Report service exception: ", e);
			List<GenericException> errors = new ArrayList<GenericException>();
			errors.add(new GenericException(Integer.parseInt(
                    e.getErrorCode())));
			setErrorsInRequest(request, errors);
			//TODO
			//return mapping.getInputForward();
			return "input";
		}
		
		return findForward(task);
	}
	
	protected static BDUserProfile getUserProfile(HttpServletRequest request) {
		return BDController.getUserProfile(request);
	}
	
	/**
	 * Returns the BOB Context associated with the given request 
	 * 
	 * @param request
	 *            The request object.
	 * @return The BOBContext object associated with the request (or null if
	 *         none is found).
	 */
	protected static BobContext getBobContext(HttpServletRequest request) {
		return BDController.getBobContext(request);
	}
	
    /**
     * This checks if any warnings already present in the session and adds the current warning
     * message to the existing messages.
     * 
     * @param request
     * @param warnings
     */
	@SuppressWarnings("unchecked")
    public static void setWarningsInRequest(final HttpServletRequest request,
			final Collection<GenericException> warnings) {
		if (warnings != null) {
			// check for warnings already in session scope
			Collection<GenericException> existingWarnings = (Collection<GenericException>) request
					.getAttribute(BDConstants.WARNING_MESSAGES);
			if (existingWarnings != null) {
				warnings.addAll(existingWarnings);
				request.removeAttribute(BDConstants.WARNING_MESSAGES);
			}

			request.setAttribute(BDConstants.WARNING_MESSAGES, warnings);
		}
	}
	
	/**
     * This checks if any warnings already present in the session and adds the current warning
     * message to the existing messages.
     * 
     * @param request
     * @param warnings
     */
	@SuppressWarnings("unchecked")
    public static void setReportWarningsInSession(final HttpServletRequest request,
			final Collection<GenericException> warnings) {
		
		if (warnings != null) {
			// check for warnings already in session scope
			Collection<GenericException> existingWarnings = (Collection<GenericException>) request
					.getSession(false).getAttribute(BDConstants.WARNING_MESSAGES);
			if (existingWarnings != null) {
				warnings.addAll(existingWarnings);
				request.getSession(false).removeAttribute(BDConstants.WARNING_MESSAGES);
			}

			request.getSession(false).setAttribute(BDConstants.WARNING_MESSAGES, warnings);
		}
	}

    /**
     * This checks if any informational messages already present in the session and adds the current
     * message to the existing messages.
     * 
     * @param request
     * @param messages
     */
	@SuppressWarnings("unchecked")
    public static void setMessagesInRequest(final HttpServletRequest request,
			final Collection<GenericException> messages) {
		if (messages != null) {
			// check for messages already in session scope
			Collection<GenericException> existingMessages = (Collection<GenericException>) request
					.getAttribute(BDConstants.INFO_MESSAGES);
			if (existingMessages != null) {
				messages.addAll(existingMessages);
				request.removeAttribute(BDConstants.INFO_MESSAGES);
			}

			request.setAttribute(BDConstants.INFO_MESSAGES, messages);
		}
	}

    /* (non-Javadoc)
     * @see com.manulife.pension.platform.web.controller.BaseAction#setErrorsInRequest(javax.servlet.http.HttpServletRequest, java.util.Collection)
     */
    @SuppressWarnings("unchecked")
    protected void setErrorsInRequest(HttpServletRequest request, Collection errors) {
        super.setErrorsInRequest(request, errors);
        if (errors != null && !errors.isEmpty()) {
            request.setAttribute(BDConstants.IS_ERROR, Boolean.TRUE);
        }
    }
    
    /**
     * This method gets layout page for the given layout id.
     * 
     * @param path
     * @return LayoutPage
     */
   
    protected LayoutPage getLayoutPage(String id, HttpServletRequest request) {
        //BDLayoutBean bean = ApplicationHelper.getLayoutStore(getServlet().getServletContext()).getLayoutBean(id, request);
    	 ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    		HttpServletRequest req = attr.getRequest();
    	BDLayoutBean bean = ApplicationHelper.getLayoutStore(req.getServletContext()).getLayoutBean(id, request);
        LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();
        return layoutPageBean;
    }

    /**
     * This method is used by PDF Generation functionality.
     * 
     * This sets Logo, PathName, Introduction-1 & 2 XML elements common for reports.
     * 
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setIntroXMLElements(LayoutPage layoutPageBean, PDFDocument doc,
            Element rootElement, HttpServletRequest request) {
        Contract currentContract = BDSessionHelper.getBobContext(request).getCurrentContract();
        setIntroXMLElements(layoutPageBean, doc, rootElement, currentContract);
    }
    
    /**
     * This method is used by PDF Generation functionality.
     * 
     * This sets footer, footnotes and disclaimer XML elements common for reports
     * 
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setFooterXMLElements(LayoutPage layoutPageBean, PDFDocument doc,
            Element rootElement, HttpServletRequest request, String[]... params) {
        Location location = ApplicationHelper.getRequestContentLocation(request);
        String pptTnsHistRoot = rootElement.toString();
        
        if(null!=pptTnsHistRoot && pptTnsHistRoot.contains("pptTxnHistory")) {
        	BDPdfHelper.setFooterXMLElements(layoutPageBean, doc, rootElement, location,
                    BDContentConstants.BD_PARTICIPANT_REPORTS_GLOBAL_DISCLOSURE, params);
        }else {
        BDPdfHelper.setFooterXMLElements(layoutPageBean, doc, rootElement, location,
                BDContentConstants.BD_GLOBAL_DISCLOSURE, params);
        }

    }

    /**
     * This sets Global Disclosure XML element common for reports
     * 
     * @param doc
     * @param rootElement
     */
    public static void setGlobalDisclosureXMLElement(PDFDocument pdfDoc, Element rootElement,
            Location location) {
        BDPdfHelper.setGlobalDisclosureXMLElement(pdfDoc, rootElement, location,
                BDContentConstants.BD_GLOBAL_DISCLOSURE);
    }

    /**
     * This method is used by PDF Generation functionality.
     * 
     * This sets XML element for Roth message.
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setRothMessageElement(PDFDocument doc, Element rootElement,
            HttpServletRequest request) {
        Contract currentContract = BDSessionHelper.getBobContext(request).getCurrentContract();
        setRothMessageElement(doc, rootElement, currentContract);
    }

    /**
     * This method is used by PDF Generation functionality.
     * 
     * This sets XML element for Signature Plus Disclosure message.
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setSigPlusDisclosureMessageElement(PDFDocument doc, Element rootElement) {        
        String sigPlusText = ContentHelper.getContentText(CommonContentConstants.BOB_INV_ALLOCATION_SIG_PLUS_DISCLOSURE_TEXT, ContentTypeManager.instance().MISCELLANEOUS, null);
        setSigPlusDisclosureMessageElement(doc, rootElement, sigPlusText);
    }

    /**
     * This sets XML elements of Informational messages common for reports
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    @SuppressWarnings("unchecked")
    public void setInfoMessagesXMLElements(PDFDocument doc, Element rootElement,
            HttpServletRequest request) {
        
        Collection infoMessageCollection = (Collection) request.getAttribute(BDConstants.INFO_MESSAGES);
        setInfoMessagesXMLElements(doc, rootElement, infoMessageCollection);
        
    }
    
}