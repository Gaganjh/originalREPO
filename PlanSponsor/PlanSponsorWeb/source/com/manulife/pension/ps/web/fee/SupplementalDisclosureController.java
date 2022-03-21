package com.manulife.pension.ps.web.fee;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.documents.model.FeeDisclosureDocumentInfo;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.order.delegate.OrderServiceDelegate;

/**
 * This is action class to the supplemental disclosure details pop up page that is available only to
 * internal staff.
 * 
 * @See SupplementalDisclosureForm, supplementalDisclosure.jsp
 * 
 * @author Mark Eldridge
 * 
 */

public class SupplementalDisclosureController extends PsController {
    public static final String SUPPLEMENTAL_DISCLOSURE = "supplementalDisclosure";

    private static final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Constructor
     */
    public SupplementalDisclosureController() {
        super(SupplementalDisclosureController.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.platform.web.controller.BaseAction#doExecute(org
     * .apache.struts.action.ActionMapping, org.apache.struts.action.Form,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */

    /**
     * This action is forwarded to from /do/fee/SupplementalDisclosure It gathers data required to
     * display supplementalDisclosure.jsp which it then forwards to.
     */
    public String doExecute(ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug(" entered SupplementalDisclosureAction.doExecute");
        }
        // Cast form
        SupplementalDisclosureForm supplementalDisclosureForm = (SupplementalDisclosureForm) form;
        // Get instance of FeeServiceDelegate
        FeeServiceDelegate feeServiceDelegate = FeeServiceDelegate.getInstance(Environment.getInstance().getAppId());
        // Obtain contract number
        int contractNumber = getContractNumber(request);
        // Obtain the effective date for the latest supplemental disclosure PDF
        Date reportAsOfDate = getLatestDisclosureEffectiveDate(contractNumber);
        
        // Get record keeping fee data details for this contract and effect date.
        Map<String, BigDecimal> feeDetails = null;
        try {
          feeDetails = feeServiceDelegate.getEstimatedCostOfRecordKeepingFee(contractNumber, reportAsOfDate);
        } catch (Exception e) {
        	throw new SystemException(e, "Error getting fee data. Contract number is " + contractNumber);
        }
        
        // Populate fee data object into the form.
        supplementalDisclosureForm.setRecordKeepingFeeBreakdown(feeDetails);
        supplementalDisclosureForm.setEffectiveDate(reportAsOfDate);
		supplementalDisclosureForm.setPinpointContract(FeeServiceDelegate
				.getInstance(Environment.getInstance().getAppId())
				.isPinpoinContract(contractNumber));
        // Forward to the supplementalDisclosure JSP.
       // return mapping.findForward(SUPPLEMENTAL_DISCLOSURE);
		return SUPPLEMENTAL_DISCLOSURE;
    }

    /**
     * Obtains the latest effective date for the supplemental disclosure pdf.
     * 
     * @param contractNumber - The contract number to look up the disclosure's information.
     * 
     * @return - Effective date of the latest PDF.
     * @throws SystemException
     */
    private Date getLatestDisclosureEffectiveDate(int contractNumber) throws SystemException {
        // Obtain the latest effective date from the fee disclosure document
        FeeDisclosureDocumentInfo documentInfo = OrderServiceDelegate.getInstance().getInforceFeeDisclosurePdfDetails(contractNumber, 0);
        String effectiveDate = documentInfo.getDocumentEffectiveDate();
        Date reportAsOfDate;
        try {
            synchronized (effectiveDate) {
                reportAsOfDate = dateFormatter.parse(effectiveDate);
            }
        } catch (ParseException e) {
            throw new SystemException("Report Effective date invalid for contract  # " + contractNumber + ". Report Effective Date is "
                    + effectiveDate);
        }        
        return reportAsOfDate;
    }
    
    /**
     * Retrieves the contract number for the supplied request.
     * 
     * @param request
     * @return - int contract number
     */
    private int getContractNumber(HttpServletRequest request){ 
        UserProfile userProfile = getUserProfile(request);
        Contract currentContract = userProfile.getCurrentContract();
        return currentContract.getContractNumber();
    }
}
