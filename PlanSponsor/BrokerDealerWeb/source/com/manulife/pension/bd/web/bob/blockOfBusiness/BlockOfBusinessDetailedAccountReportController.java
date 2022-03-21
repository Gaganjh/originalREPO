package com.manulife.pension.bd.web.bob.blockOfBusiness;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportData;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportVO;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessSummaryVO;
import com.manulife.pension.ps.service.report.bob.valueobject.BrokerInfoVO;
import com.manulife.pension.service.contract.util.SmallPlanFeature;
import com.manulife.pension.service.contract.valueobject.FeeDisclosureMailingAddress;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.BDFirmRep;
import com.manulife.pension.service.security.role.BDUserRole;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;

/**
 * action class for broker detail report
 * 
 * @author thomsib
 *
 */
@Controller
@RequestMapping( value = "/bob")
@SessionAttributes({"blockOfBusinessForm"})

public class BlockOfBusinessDetailedAccountReportController extends BDReportController {
	@ModelAttribute("blockOfBusinessForm")
	public BlockOfBusinessForm populateForm() 
	{
		return new BlockOfBusinessForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/bob/blockOfBusinessActive.jsp");
		forwards.put("activeTab","redirect:/do/bob/blockOfBusiness/Active/?task=refresh");
		forwards.put("outstandingProposalsTab","redirect:/do/bob/blockOfBusiness/OutstandingProposals/?task=refresh");
		forwards.put("pendingTab","redirect:/do/bob/blockOfBusiness/Pending/?task=refresh");
		forwards.put("discontinuedTab","redirect:/do/bob/blockOfBusiness/Discontinued/?task=refresh");
		forwards.put("detailedAccountReport","/bob/blockOfBusinessActive.jsp");}
	
    private Logger logger = Logger.getLogger(BlockOfBusinessDetailedAccountReportController.class);

    public static final String CONTRACTS = " Contracts";
    
    public static final String CSV_REPORT_NAME = "\"contract compensation report";
    
    public static final String CSV_EXTENSION = ".csv\"";
    
    public static final String ZERO = "0";
    
	private enum LEVEL_1_HEADER {
		
		FIRM("Firm"),
		CONTRACT("Contract"),
		TRUSTEE("Trustee"), 
		FR("FR"),
		COMPENSATION("Compensation Details"),
		RIA_FEE_SCHEDULE("RIA Fee Schedule"),
		FIDUCIARY_SERVICE_DETAIL("Fiduciary Services Details");
		
		private String value;
		
		LEVEL_1_HEADER (String value){
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
    private enum LEVEL_2_HEADER {
		
    	FIRM_NAME ("Name",  LEVEL_1_HEADER.FIRM, true),
    	CONTRACT_NUMBER ( "Number",  LEVEL_1_HEADER.CONTRACT , true),
    	CONTRACT_NAME ("Name" ,  LEVEL_1_HEADER.CONTRACT, true),
    	EFFECTIVE_DATE ("Effective Date",  LEVEL_1_HEADER.CONTRACT, true),
    	NUMBER_OF_PPT (	"Number of Lives",  LEVEL_1_HEADER.CONTRACT, true),
		TRUSTEE_NAME ("Name",  LEVEL_1_HEADER.TRUSTEE, true),
		TRUSTEE_ADDRESS_1 ("Mailing Address line 1",  LEVEL_1_HEADER.TRUSTEE, true),
		TRUSTEE_ADDRESS_2 ("Mailing Address line 2",  LEVEL_1_HEADER.TRUSTEE, true),
		TRUSTEE_CITY ("City",  LEVEL_1_HEADER.TRUSTEE, true),
		TRUSTEE_STATE ("State",  LEVEL_1_HEADER.TRUSTEE, true),
		TRUSTEE_ZIP ("ZIP",  LEVEL_1_HEADER.TRUSTEE, true),
		ASSET_SHARE ("Asset Share",  LEVEL_1_HEADER.CONTRACT, true),
		FR_CONTRACT_SHARE ("Contract Share",  LEVEL_1_HEADER.FR, true),
		FR_FIRST_NAME ("First Name",  LEVEL_1_HEADER.FR, true),
		FR_LAST_NAME ("Last Name",  LEVEL_1_HEADER.FR, true),
		FR_ADDRESS_1 ("Mailing Address line 1",  LEVEL_1_HEADER.FR, true),
		FR_ADDRESS_2 ("Mailing Address line 2",  LEVEL_1_HEADER.FR, true), 
		FR_ADDRESS_3 ("Mailing Address line 3",  LEVEL_1_HEADER.FR, true),
		FR_CITY ("City",  LEVEL_1_HEADER.FR, true),
		FR_STATE ("State",  LEVEL_1_HEADER.FR, true),
		FR_ZIP ("ZIP",  LEVEL_1_HEADER.FR, true),
		DB_TR_1 ("DB TR 1st year",  LEVEL_1_HEADER.COMPENSATION, false),
		DB_RG_1 ("DB RG 1st year",  LEVEL_1_HEADER.COMPENSATION, false),
		DB_RG_REN ("DB RG Ren",  LEVEL_1_HEADER.COMPENSATION, false),
		AB_1 ("AB 1st",  LEVEL_1_HEADER.COMPENSATION, false),
		AB_REN ("AB Ren",  LEVEL_1_HEADER.COMPENSATION, false),
		AB_ALL_YEAR ("AB",  LEVEL_1_HEADER.COMPENSATION, false),
		RIA_FIRM_NAME ("RIA Firm Name",  LEVEL_1_HEADER.RIA_FEE_SCHEDULE, false),
		RIA_FEE_PAID_BY_PLAN ("RIA AB",  LEVEL_1_HEADER.RIA_FEE_SCHEDULE, false),
		RIA_FEE_PAID_BY_JH ("RIA Fees - Paid by John Hancock",  LEVEL_1_HEADER.RIA_FEE_SCHEDULE, false),
		RIA_BPS_MIN ("RIA AB – Min",  LEVEL_1_HEADER.RIA_FEE_SCHEDULE, false),
		RIA_BPS_MAX ("RIA AB - Cap",  LEVEL_1_HEADER.RIA_FEE_SCHEDULE, false),
		RIA_AC_BLEND ("RIA AB - Blend",  LEVEL_1_HEADER.RIA_FEE_SCHEDULE, false),
		RIA_AC_TIERED ("RIA AB - Tier",  LEVEL_1_HEADER.RIA_FEE_SCHEDULE, false),
		RIA_FLAT_FEE_PER_HEAD ("RIA - Part Fee",  LEVEL_1_HEADER.RIA_FEE_SCHEDULE, false),
		RIA_FLAT_FEE_PRORATA ("RIA - Pro Rata",  LEVEL_1_HEADER.RIA_FEE_SCHEDULE, false),
		COFID_321_BPS_FEE ("3(21) BPS (%)",  LEVEL_1_HEADER.FIDUCIARY_SERVICE_DETAIL, false),
		COFID_321_PRORATA_FEE ("3(21) ($)",  LEVEL_1_HEADER.FIDUCIARY_SERVICE_DETAIL, false),
		COFID_338_BPS_FEE ("3(38) BPS (%)",  LEVEL_1_HEADER.FIDUCIARY_SERVICE_DETAIL, false),
		COFID_338_PRORATA_FEE ("3(38) ($)",  LEVEL_1_HEADER.FIDUCIARY_SERVICE_DETAIL, false),
		RVP ("RVP Name",  null, false),
		PRODUCT_TYPE ("Product Type",  null, false),
		US_NY ("USA/NY",  null, false),
		CLASS ("Class",  null, false),
    	SIGNATURE_FIDUCIARY_CONNECT (SmallPlanFeature.SFC.getTitle(),  null, false),
    	POOLED_EMPLOYER_PLAN (SmallPlanFeature.PEP.getTitle(),  null, false);    
    	
        private String value;
        private LEVEL_1_HEADER header;
        private boolean repeateHeaderForEachColumn;
		
        LEVEL_2_HEADER (String value, LEVEL_1_HEADER header, boolean repeateHeaderForEachColumn){
			this.value = value;
			this.header = header;
			this.repeateHeaderForEachColumn = repeateHeaderForEachColumn;
		}
		
        public LEVEL_1_HEADER getTopLevelHeader() {
			return this.header;
		}
        
		public String getColumnName() {
			return this.value;
		}
		
		public boolean getRepeatTopHeaderInd() {
			return this.repeateHeaderForEachColumn;
		}
	}
    
    /**
     * Constructor class.
     */
    public BlockOfBusinessDetailedAccountReportController() {
        super(BlockOfBusinessDetailedAccountReportController.class);
    }

    /**
     * The Default sort field.
     */
	@Override
	protected String getDefaultSort() {
		return BlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID;
	}

    /**
     * The Default sort direction.
     */
	@Override
	protected String getDefaultSortDirection() {
        return ReportSort.ASC_DIRECTION;        
	}

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		return null;
	}

	@Override
	protected String getReportId() {
		return BlockOfBusinessReportData.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return BlockOfBusinessReportData.CSV_REPORT_NAME;
	}
	
    /**
     * Sorting is done by Contract Number in Ascending Order.
     *
     * @see #populateSortCriteria(ReportCriteria criteria, BaseReportForm form)
     */
	@Override
    protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
        String sortField = BlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID;
        String sortDirection = ReportSort.ASC_DIRECTION;
    	criteria.insertSort(sortField, sortDirection);
    }

    /**
     * Populates report criteria.
     *
     * @see #populateReportCriteria(ReportCriteria criteria,
	 *		BaseReportForm form, HttpServletRequest request)
     */
	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateReportCriteria()");
        }

        BlockOfBusinessForm reportForm = (BlockOfBusinessForm) form;
        
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        if (userProfile == null) {
            throw new SystemException("UserProfile is null");
        }

        addUserProfileRelatedFilterCriteria(userProfile, criteria, reportForm, request);
        
        addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_CONTRACT_STATUS_CODES,
        		BDConstants.ACTIVE_TAB_VAL);
        
        criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> populateReportCriteria()");
        }
		
	}

	/**
	 * Add userProfile specific filter criteria such as UserProfileID, userRole.
	 * @param userProfile
	 * @param criteria
	 * @param reportForm
	 * @param request
	 * @throws SystemException
	 */
    private void addUserProfileRelatedFilterCriteria(BDUserProfile userProfile,
            ReportCriteria criteria, BlockOfBusinessForm reportForm, HttpServletRequest request)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> addUserProfileRelatedFilterCriteria()");
        }
        
        // In Mimic mode, the user profile id is of the user who is mimicking. When not in mimic
        // mode, it is the user profile id of the user currently logged in.
        String userProfileID = getFilterValue(BlockOfBusinessReportData.FILTER_USER_PROFILE_ID,
                reportForm, userProfile, request);
        if (userProfileID != null) {
            addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_USER_PROFILE_ID, Long
                    .valueOf(userProfileID));
        }

        // In Mimic mode, the user role is of the user who is mimicking. When not in mimic
        // mode, it is the user role of the user currently logged in.
        addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_USER_ROLE, getFilterValue(
                BlockOfBusinessReportData.FILTER_USER_ROLE, reportForm, userProfile, request)); 

        if (userProfile.isInMimic()) {
            // In Mimic mode, the user profile id is of the user who is mimicked.
            userProfileID = getFilterValue(BlockOfBusinessReportData.FILTER_MIMIC_USER_PROFILE_ID,
                    reportForm, userProfile, request);
            if (userProfileID != null) {
                addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_MIMIC_USER_PROFILE_ID,
                        Long.valueOf(userProfileID));
            }

            // In Mimic mode, the user role is of the user who is mimicked.
            addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_MIMIC_USER_ROLE,
                    getFilterValue(BlockOfBusinessReportData.FILTER_MIMIC_USER_ROLE, reportForm,
                            userProfile, request));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> addUserProfileRelatedFilterCriteria()");
        }
    }

    @RequestMapping(value ="/detailedAccountReport/Active/", method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doDefault (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    		throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	String forward=super.doDefault( form, request, response);
    	 return StringUtils.contains(forward, "/")?forward:forwards.get(forward); 
    }
    @RequestMapping(value ="/detailedAccountReport/Active/",params={"task=filter"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doFilter (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	String forward=super.doFilter( form, request, response);
    	 return StringUtils.contains(forward, "/")?forward:forwards.get(forward); 
    }
    
    @RequestMapping(value ="/detailedAccountReport/Active/", params={"task=page"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doPage (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	String forward=super.doPage( form, request, response);
    	 return StringUtils.contains(forward, "/")?forward:forwards.get(forward); 
    }
    
    @RequestMapping(value ="/detailedAccountReport/Active/", params={"task=sort"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSort (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	String forward=super.doSort( form, request, response);
    	 return StringUtils.contains(forward, "/")?forward:forwards.get(forward); 
    }
    @RequestMapping(value ="/detailedAccountReport/Active",
    params={"task=download"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doDownload (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	String forward=super.doDownload( form, request, response);
    	 return StringUtils.contains(forward, "/")?forward:forwards.get(forward); 
    }
    @RequestMapping(value ="/detailedAccountReport/Active",
    	    params={"task=downloadAll"}, method ={RequestMethod.POST,RequestMethod.GET}) 
    	    public String doDownloadAll (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    	    throws IOException,ServletException, SystemException {
    	    	if(bindingResult.hasErrors()){
    	    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    	    		if(errDirect!=null){
    	    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    	    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    	    		}
    	    	}
    	    	String forward=super.doDownloadAll( form, request, response);
    	    	 return StringUtils.contains(forward, "/")?forward:forwards.get(forward); 
    	    }
    	    
    
    
    /**
     * This method returns the Filter value submitted by the user, given the filter name.
     * 
     * @param filterID - the filter name
     * @param reportForm - BlockOfBusinessForm object.
     * @param userProfile - BDUSerProfile object.
     * @param request - the HttpServletRequest object
     * @return - the filter value.
     * @throws SystemException
     */
    private String getFilterValue(String filterID, BlockOfBusinessForm reportForm,
            BDUserProfile userProfile, HttpServletRequest request)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("inside getFilterValue()");
        }
        try {
            // Filters common to both Quick, Advanced filtering sections.
            if (BlockOfBusinessReportData.FILTER_USER_PROFILE_ID.equals(filterID)) {
                // If the Internal user is mimicking a external user, the user Profile ID is of that
                // Internal user. If the user is not in mimick mode, then, the user Profile ID is of
                // the current user logged in.
                if (userProfile.isInMimic()) {
                    BDUserProfile mimickingInternalUserProfile = BlockOfBusinessUtility
                            .getMimckingUserProfile(request);
                    if (mimickingInternalUserProfile == null) {
                        return null;
                    }
                    return String.valueOf(mimickingInternalUserProfile.getBDPrincipal()
                            .getProfileId());
                }
				return String.valueOf(userProfile.getBDPrincipal().getProfileId());
            } 
            else if (BlockOfBusinessReportData.FILTER_USER_ROLE.equals(filterID)) {
                // If the Internal user is mimicking a external user, the user Role is of that
                // Internal user. If the user is not in mimick mode, then, the user Role is of
                // the current user logged in.
                if (userProfile.isInMimic()) {
                    BDUserProfile mimickingInternalUserProfile = BlockOfBusinessUtility
                            .getMimckingUserProfile(request);
                    if (mimickingInternalUserProfile == null) {
                        return null;
                    }
                    return mimickingInternalUserProfile.getRole().getRoleType().getUserRoleCode();
                }
				return userProfile.getRole().getRoleType().getUserRoleCode();
            } 
            else if (BlockOfBusinessReportData.FILTER_MIMIC_USER_PROFILE_ID.equals(filterID)) {
                // If the Internal user is mimicking a external user, the Mimicking user Profile ID
                // is of that External user being mimicked.
                if (userProfile.isInMimic()) {
                    return String.valueOf(userProfile.getBDPrincipal().getProfileId());
                }
            } 
            else if (BlockOfBusinessReportData.FILTER_MIMIC_USER_ROLE.equals(filterID)) {
                // If the Internal user is mimicking a external user, the Mimicking user Role is of
                // that External user being mimicked.
                if (userProfile.isInMimic()) {
                    return userProfile.getRole().getRoleType().getUserRoleCode();
                }
            } 
        } catch (NullPointerException ne) {
            // Do Nothing.
        }
        return null;
    }
    
    /**
     * Invokes the download task. The first half of this task uses the common
     * workflow with validateForm set to true. The second half of this task
     * takes the populated report data object and create the CSV file.
     *
     * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
     *      HttpServletResponse, boolean)
     * @see #populateDownloadData(PrintWriter, BaseReportForm, ReportData,
     *      HttpServletRequest)
     * @return null so that Struts will not try to forward to another page.
     */
    @RequestMapping(value ="/detailedAccountReport/Active/", params={"task=detailedAccountReport"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doDetailedAccountReport(@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDetailedAccountReport");
        }
        byte[] downloadData = null;

        doCommon( actionForm, request, response);
        
        

        // check for errors
        // if any just stream an error message, otherwise deliver the report
        // Check for Error Messages.
        boolean isErrorMsgPresent = checkForErrorConditions(request, actionForm);
        if (isErrorMsgPresent) {
            if (logger.isDebugEnabled()) {
                logger.debug("exit -> doCommon() in BlockOfBusinessAction. Error Messages Found.");
            }
            // forward it to current tab
            return forwards.get(actionForm.getCurrentTab());
        }
        
		if (logger.isDebugEnabled()) {
			logger.debug("Did not find any errors -- streaming the report data");
		}
		
		downloadData = getDetailedAccountReport(actionForm, request);

        streamDownloadData(request, response, getContentType(),
                getFileName(actionForm,request), downloadData);

        /**
         * No need to forward to any other JSP or action. Returns null will make
         * Struts to return controls back to server immediately.
         */
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doDetailedAccountReport");
        }
        return null;
    }
    
    /**
     * Check if any error presents
     * @param request
     * @param blockOfBusinessForm
     * @return
     */
    @SuppressWarnings("unchecked")
	private boolean checkForErrorConditions(HttpServletRequest request, BlockOfBusinessForm blockOfBusinessForm) {
    	
    	boolean isErrorMsgPresent = false;
    	BlockOfBusinessReportData reportData = (BlockOfBusinessReportData) request.getAttribute(BDConstants.REPORT_BEAN);
    	ArrayList<BlockOfBusinessReportVO> bobReportVOList = (ArrayList<BlockOfBusinessReportVO>) reportData.getDetails();
    	
    	if(bobReportVOList == null || bobReportVOList.isEmpty()) {
    		blockOfBusinessForm.setNoContractsForDetailedBrokerReport(true);
    		isErrorMsgPresent = true;
    	}
    	
		return isErrorMsgPresent;
	}
    
    /**
     * Returns file name
     *
     * @see #getFileName(BaseReportForm form, HttpServletRequest request)
     */
	@Override
    protected String getFileName(BaseReportForm form, HttpServletRequest request) {
    	 return    CSV_REPORT_NAME
                  + BDConstants.SINGLE_SPACE_SYMBOL
                  + DateRender.format(new Date(),
                    RenderConstants.MEDIUM_MDY_SLASHED).replace(BDConstants.SLASH_SYMBOL,
                    BDConstants.SPACE_SYMBOL) 
                  + CSV_EXTENSION;
   }
    
    /**
     * This method is called when the user clicks on "DetailedAccountReport" link. 
     * This method generates the CSV file to be given back to the user.
     */
    protected byte[] getDetailedAccountReport(BaseReportForm form, 
            HttpServletRequest request) throws SystemException {
    	
    	if (logger.isDebugEnabled()) {
            logger.debug("entry -> getDetailedAccountReport.");
        }
        
        BlockOfBusinessForm reportForm = (BlockOfBusinessForm) form;
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        BDUserRole userRole = userProfile.getRole();

        BlockOfBusinessReportData reportData = (BlockOfBusinessReportData) request
                .getAttribute(BDConstants.REPORT_BEAN);

        String buff = populateDetailedAccountReportData(userProfile, userRole,
                reportData, reportForm, request);

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getDetailedAccountReport.");
        }
        
        return buff.getBytes();
    	
    }
    
    /**
     * This method is called for generating CSV file.
     * 
     * @param userProfile - BDUserProfile object.
     * @param userRole - BDUserRole object.
     * @param reportData - report data containing the information to be displayed in CSV.
     * @param reportForm - BlockOfBusinessForm object
     * @param request - the HttpServletRequest object.
     * @return - the string having CSV representation of data.
     * @throws SystemException
     */
    private String populateDetailedAccountReportData(BDUserProfile userProfile,
            BDUserRole userRole, BlockOfBusinessReportData reportData,
            BlockOfBusinessForm reportForm, HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateDetailedAccountReportData().");
        }
        
        StringBuffer buff = new StringBuffer(255);

        if (reportData == null) {
            buff.append(BDConstants.SINGLE_SPACE_SYMBOL);
            return buff.toString();
        }
        
        //disclaimer CMA
        buff.append(getCsvString(ContentHelper.getContentText(
                        BDContentConstants.DISCLAIMER_DETAILED_ACCOUNT_REPORT,
                        ContentTypeManager.instance().DISCLAIMER, null))).append(LINE_BREAK);
        
        // report title
        buff.append(LINE_BREAK).append("Plan Compensation Report").append(LINE_BREAK);
        
        buff.append(LINE_BREAK).append(
        	 showDetailedAccountSummaryInformationInCSV(userProfile, userRole, reportData, reportForm));
        
        // effective date
        buff.append("Effective as of ").append( DateRender.format(new Date(),
                RenderConstants.MEDIUM_MDY_SLASHED)).append(LINE_BREAK).append(LINE_BREAK);
        
        //legend
        buff.append(showLegendInCSV(reportData, userRole));
        
        buff.append(LINE_BREAK).append(LINE_BREAK).append(LINE_BREAK);
        
        //headers
        buff.append(getDetailedAccountHeadingRowsInCSV(reportData.getBobSummaryVO(), userRole)).append(LINE_BREAK);
        
        
        //main report
        buff.append(showMainDetailedAccountReportInCSV(userProfile, userRole, 
        		reportForm, reportData, request));
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> populateDetailedAccountReportData().");
        }
        return buff.toString();

    }
    
    /**
     * This method gets the Detailed Account Summary Information, to be displayed in CSV file.
     * @param userProfile
     * @param userRole
     * @param reportData
     * @param form
     * @return String having summary information
     */
    private String showDetailedAccountSummaryInformationInCSV(BDUserProfile userProfile, BDUserRole userRole,
            BlockOfBusinessReportData reportData, BlockOfBusinessForm form){
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> showDetailedAccountSummaryInformationInCSV().");
        }
        StringBuffer buff = new StringBuffer(255);
        
        // If the user is a BDFirmRep, show his Name
        if (userRole instanceof BDFirmRep) {
            ArrayList<String> firmNamesList = form.getAssociatedFirmNames();
            if (firmNamesList != null && !firmNamesList.isEmpty()) {
                buff.append(getCsvString(firmNamesList.get(0))).append(CONTRACTS).append(LINE_BREAK);
            }
        // If the user is a FinancialRep, FinancialRep's assistant 
            //then show the Financial Rep User Name and list of Producer codes.
        } else {
            buff.append(getCsvString(form.getFinancialRepUserName())).append(CONTRACTS).append(LINE_BREAK);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> showDetailedAccountSummaryInformationInCSV().");
        }
        return buff.toString();
    }
   
    /**
     * Legend Information to be displayed 
     * @param userRole 
     * @return String containing Legend information
     */
    private String showLegendInCSV(BlockOfBusinessReportData reportData, BDUserRole userRole){
    	if (logger.isDebugEnabled()) {
            logger.debug("entry -> showLegendInCSV().");
        }
    	
    	StringBuffer buff = new StringBuffer(255);
    	
    	buff.append(getCsvString(BDConstants.LEGEND)).append(LINE_BREAK);
    	buff.append(getCsvString(BDConstants.LEGEND_1)).append(LINE_BREAK);
    	buff.append(getCsvString(BDConstants.LEGEND_2)).append(LINE_BREAK);
    	buff.append(getCsvString(BDConstants.LEGEND_3)).append(LINE_BREAK);
    	buff.append(getCsvString(BDConstants.LEGEND_4)).append(LINE_BREAK);
    	buff.append(getCsvString(BDConstants.LEGEND_5)).append(LINE_BREAK);
    	buff.append(getCsvString(BDConstants.LEGEND_6)).append(LINE_BREAK);
    	buff.append(getCsvString(BDConstants.LEGEND_7)).append(LINE_BREAK);
    	
    	BlockOfBusinessSummaryVO summary = reportData.getBobSummaryVO();
    	
    	// dynamic legends
    	if(!summary.getHasAllPinpointOrPresigContract()) {
    		buff.append(getCsvString(ContentHelper.getContentText(
                    BDContentConstants.AB_1ST_YEAR_LEGEND,
                    ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
    		buff.append(getCsvString(ContentHelper.getContentText(
                    BDContentConstants.AB_REN_YEAR_LEGEND,
                    ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
    	} 
        if(displayColumn(LEVEL_2_HEADER.AB_ALL_YEAR, summary, userRole)) {
        	buff.append(getCsvString(ContentHelper.getContentText(
                    BDContentConstants.AB_ALL_YEAR_LEGEND,
                    ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
    	}
        if(displayColumn(LEVEL_2_HEADER.RIA_FIRM_NAME, summary, userRole) 
        		&& summary.getHasContractWithMulipleRiaAssociated()) {
        	buff.append(getCsvString(ContentHelper.getContentText(
                    BDContentConstants.MORE_THAN_ONE_RIA_LEGEND,
                    ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
        }
        if(displayColumn(LEVEL_2_HEADER.RIA_FEE_PAID_BY_PLAN, summary, userRole)
     		   && summary.getHasContractWithRiaPaidByJH()) {
     	   buff.append(getCsvString(ContentHelper.getContentText(
                    BDContentConstants.RIA_ASSET_BASED_BPS_FEE_AMT,
                    ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
     	}
        if(displayColumn(LEVEL_2_HEADER.RIA_FEE_PAID_BY_JH, summary, userRole)
    		   && summary.getHasContractWithRiaPaidByJH()) {
    	   buff.append(getCsvString(ContentHelper.getContentText(
                   BDContentConstants.RIA_FEE_PAID_BY_JH_LEGEND,
                   ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
    	} 
        if(displayColumn(LEVEL_2_HEADER.RIA_BPS_MIN, summary, userRole)
      		   && summary.getHasContractWithRiaAssocaited()) {
      	   buff.append(getCsvString(ContentHelper.getContentText(
                     BDContentConstants.LEGEND_RIA_ASSET_BASED_BPS_MIN_FEE_AMT,
                     ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
      	}
        if(displayColumn(LEVEL_2_HEADER.RIA_BPS_MAX, summary, userRole)
     		   && summary.getHasContractWithRiaAssocaited()) {
     	   buff.append(getCsvString(ContentHelper.getContentText(
                    BDContentConstants.RIA_ASSET_BASED_BPS_MAX_FEE_AMT,
                    ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
     	}
        if(displayColumn(LEVEL_2_HEADER.RIA_AC_BLEND, summary, userRole)
      		   && summary.getHasContractWithRiaAssocaited()) {
      	   buff.append(getCsvString(ContentHelper.getContentText(
                     BDContentConstants.RIA_ASSET_BASED_BLENDED_FEE_AMT,
                     ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
      	}
        if(displayColumn(LEVEL_2_HEADER.RIA_AC_TIERED, summary, userRole)
      		   && summary.getHasContractWithRiaAssocaited()) {
      	   buff.append(getCsvString(ContentHelper.getContentText(
                     BDContentConstants.RIA_ASSET_BASED_TIERED_FEE_AMT,
                     ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
      	}
        if(displayColumn(LEVEL_2_HEADER.RIA_FLAT_FEE_PER_HEAD, summary, userRole)
       		   && summary.getHasContractWithRiaAssocaited()) {
       	   buff.append(getCsvString(ContentHelper.getContentText(
                      BDContentConstants.RIA_FLAT_PER_HEAD_FEE_AMT,
                      ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
       	}
        if(displayColumn(LEVEL_2_HEADER.RIA_FLAT_FEE_PRORATA, summary, userRole)
        		   && summary.getHasContractWithRiaAssocaited()) {
        	   buff.append(getCsvString(ContentHelper.getContentText(
                       BDContentConstants.RIA_FLAT_PRORATA_FEE_AMT,
                       ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
        }
        if(displayColumn(LEVEL_2_HEADER.COFID_321_BPS_FEE, summary, userRole)
       		   && summary.getHasContractsWithCofidSelected()) {
       	   buff.append(getCsvString(ContentHelper.getContentText(
                      BDContentConstants.LEGEND_COFID_321_ASSET_BASED_BPS_FEE,
                      ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
       	}
        if(displayColumn(LEVEL_2_HEADER.COFID_321_PRORATA_FEE, summary, userRole)
       		   && summary.getHasContractsWithCofidSelected()) {
       	   buff.append(getCsvString(ContentHelper.getContentText(
                      BDContentConstants.LEGEND_COFID_321_DOLLAR_BASED_FEE_AMT,
                      ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
       	}
        if(displayColumn(LEVEL_2_HEADER.COFID_338_BPS_FEE, summary, userRole)
       		   && summary.getHasContractsWithCofidSelected()) {
       	   buff.append(getCsvString(ContentHelper.getContentText(
                      BDContentConstants.LEGEND_COFID_338_ASSET_BASED_BPS_FEE,
                      ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
       	}
        if(displayColumn(LEVEL_2_HEADER.COFID_338_PRORATA_FEE, summary, userRole)
       		   && summary.getHasContractsWithCofidSelected()) {
       	   buff.append(getCsvString(ContentHelper.getContentText(
                      BDContentConstants.LEGEND_COFID_338_DOLLAR_BASED_FEE_AMT,
                      ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
       	}
    	if (logger.isDebugEnabled()) {
            logger.debug("exit -> showLegendInCSV().");
        }
        return buff.toString(); 
    }
    
    /**
     * This method creates Heading Row 1 & Heading Row 1 in Detailed Account Report CSV
     * @return String object
     */
    private String getDetailedAccountHeadingRowsInCSV(BlockOfBusinessSummaryVO summary, BDUserRole userRole){
    	
    	if (logger.isDebugEnabled()) {
            logger.debug("entry -> getDetailedAccountHeadingRowsInCSV().");
        }
    	
    	StringBuffer buff = new StringBuffer(255);
    	
    	// * Detailed Account Heading Row 1
    	LEVEL_1_HEADER previousLevel1Header = null;
    	for(LEVEL_2_HEADER header : LEVEL_2_HEADER.values()) {
    		if(displayColumn(header, summary, userRole)){
    			if(header.getTopLevelHeader() != null) {
    				if(previousLevel1Header == null || header.getRepeatTopHeaderInd()
    						|| !previousLevel1Header.equals(header.getTopLevelHeader())) {
    					buff.append(header.getTopLevelHeader().getValue());
    				}
    			}
    			previousLevel1Header = header.getTopLevelHeader();
    			buff.append(COMMA);
    		}
    	}
        
        
		buff.append(LINE_BREAK);
		
		// * Detailed Account Heading Row 2
    	for(LEVEL_2_HEADER header : LEVEL_2_HEADER.values()) {
    		if(displayColumn(header, summary, userRole)){
    			buff.append(header.getColumnName());
    			buff.append(COMMA);
    		}
    	}
		
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getDetailedAccountHeadingRowsInCSV().");
        }
        return buff.toString();    	
    }
    
    
    /**
     * display column
     * 
     * @param columns
     * @param dataConditions
     * 
     * @return boolean
     */
    private boolean displayColumn(LEVEL_2_HEADER column, BlockOfBusinessSummaryVO sumary, BDUserRole role) {
    	switch (column) {
    	case  AB_ALL_YEAR:
    		            return sumary.getHasPresigContract() 
    		            		&& sumary.getHasContractWithABAllYearCompensation();
    	case  RIA_FIRM_NAME:
    	case  RIA_FEE_PAID_BY_PLAN:
    	case  RIA_FEE_PAID_BY_JH:
    	case  RIA_BPS_MIN:
    	case  RIA_BPS_MAX:
    	case  RIA_AC_BLEND:
    	case  RIA_AC_TIERED:
    	case  RIA_FLAT_FEE_PER_HEAD:
    	case  RIA_FLAT_FEE_PRORATA:
    		           return sumary.getHasContractWithRiaAssocaited();
    		           
    	case  COFID_321_BPS_FEE:
    	case  COFID_321_PRORATA_FEE:
    	case  COFID_338_BPS_FEE:
    	case  COFID_338_PRORATA_FEE:
    		           return sumary.getHasContractsWithCofidSelected();	           
    		           
    	default : 
    		           return true;
    	}
    }
    
    
    /**
     * 
     * create main report
     * 
     * @param userProfile
     * @param userRole
     * @param reportForm
     * @param reportData
     * @param request
     * 
     * @return String
     * 
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
	private String showMainDetailedAccountReportInCSV(BDUserProfile userProfile,
            BDUserRole userRole, BlockOfBusinessForm reportForm,
			BlockOfBusinessReportData reportData, HttpServletRequest request) throws SystemException {

		StringBuffer buff = new StringBuffer(255);
		ArrayList<BlockOfBusinessReportVO> bobReportVOList = (ArrayList<BlockOfBusinessReportVO>) reportData.getDetails();
		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
		BlockOfBusinessSummaryVO summary = reportData.getBobSummaryVO();
		
		if (bobReportVOList != null) {
			
			for (BlockOfBusinessReportVO bobReportVO : bobReportVOList) {
				
				Integer contractID = new Integer(bobReportVO.getContractNumber());
				
				// get producer code list
				String[] producerCodeList = new String[0];
				if(StringUtils.isNotEmpty(bobReportVO.getProducerCodes())) {
					producerCodeList = bobReportVO.getProducerCodes().split(COMMA);
				}
				
				boolean passiveTrusteeContract = contractServiceDelegate.isPassiveTrusteeContract(contractID);
				
				FeeDisclosureMailingAddress feeDisclosureMailingAddress = null;
				try{
					feeDisclosureMailingAddress = contractServiceDelegate
							.findFeeDisclosureRecipientAddress(contractID, passiveTrusteeContract, 0);
				} catch(Exception e){
					feeDisclosureMailingAddress = null;
					logger.error("No Trustee record for Contract - "+contractID);
				}
				
				BlockOfBusinessSummaryVO bobSummaryVO = reportData.getBobSummaryVO();
		        ArrayList<BrokerInfoVO> brokerInfoList = bobSummaryVO.getBrokerInfoVO();
		        
		        String contractShareList = bobReportVO.getFrContractShareList();
		        String[] frContractShare = contractShareList.split(COMMA);
		        
		        int producerCount = 0;
				
				// for each producer code in the list
				for(String producerCode : producerCodeList) {
					
					producerCode = StringUtils.trimToEmpty(producerCode);
					
					// if producer code is empty , ignore it
					if(StringUtils.isEmpty(producerCode)) {
						continue;
					}
					
					BrokerInfoVO brokerInfo = getBrokerInfo(producerCode, brokerInfoList);
					
					buff.append(getCsvString(brokerInfo.getBdFirmName()));
					
					buff.append(COMMA);
					buff.append(contractID);
					
					buff.append(COMMA);
					buff.append(getCsvString(bobReportVO.getContractName()));
					
					buff.append(COMMA);
					buff.append(getCsvString(bobReportVO.getContractEffectiveDate()));
					
					buff.append(COMMA);
					buff.append(getCsvString(bobReportVO.getNumOfLives()));
					
					if(feeDisclosureMailingAddress != null){
						
						String trusteeFullName = feeDisclosureMailingAddress.getFirstName() + WHITE_SPACE_CHAR 
						                         + feeDisclosureMailingAddress.getLastName();
						
						buff.append(COMMA);
						buff.append(getCsvString(trusteeFullName));
						
						buff.append(COMMA);
						buff.append(getCsvString(feeDisclosureMailingAddress.getAddress().getLine1()));
						
						buff.append(COMMA);
						buff.append(getCsvString(feeDisclosureMailingAddress.getAddress().getLine2()));
						
						buff.append(COMMA);
						buff.append(getCsvString(feeDisclosureMailingAddress.getAddress().getCity()));
						
						buff.append(COMMA);
						buff.append(getCsvString(feeDisclosureMailingAddress.getAddress().getStateCode()));
						
						buff.append(COMMA);
						buff.append(getCsvString(getCSVZipCode(feeDisclosureMailingAddress.getAddress().getZipCode())));
					} else{
						buff.append(COMMA).append(COMMA).append(COMMA);
						buff.append(COMMA).append(COMMA).append(COMMA);
					}
					
					buff.append(COMMA);
					if(bobReportVO.getTotalAssets() != null 
							&& frContractShare.length > producerCount 
							     && (frContractShare[producerCount] != null) ) {
						double contractAssetShareDouble=(bobReportVO.getTotalAssets().doubleValue()*Float.parseFloat(frContractShare[producerCount]))/100.00;
						String contractAssetShareString = Double.toString(contractAssetShareDouble);
							buff.append(getCsvString(
									BDConstants.DOLLAR_SIGN 
								+	NumberRender.formatByType(contractAssetShareString,null, RenderConstants.CURRENCY_TYPE, Boolean.FALSE)));
					}
					else{
						buff.append(getCsvString(BDConstants.DOLLAR_SIGN + ZERO));
					}
					
					
					buff.append(COMMA);
					if(frContractShare.length > producerCount){
						buff.append(getCsvString(frContractShare[producerCount]+ BDConstants.PERCENTAGE_SIGN));
					}
					
					
					buff.append(COMMA);
					buff.append(getCsvString(brokerInfo.getBrokerFirstName()));
					
					buff.append(COMMA);
					buff.append(getCsvString(brokerInfo.getBrokerLastName()));
					
					buff.append(COMMA);
					buff.append(getCsvString(brokerInfo.getFrAddressLine1()));
					
					buff.append(COMMA);
					buff.append(getCsvString(brokerInfo.getFrAddressLine2()));
					
					buff.append(COMMA);
					buff.append(getCsvString(brokerInfo.getFrAddressLine3()));
					
					buff.append(COMMA);
					buff.append(getCsvString(brokerInfo.getCityName()));
					
					buff.append(COMMA);
					buff.append(getCsvString(brokerInfo.getStateCode()));
					
					buff.append(COMMA);
					buff.append(getCsvString(getCSVZipCode(brokerInfo.getZipCode())));
					
					buff.append(COMMA);
					buff.append(checkZeroOrNull(bobReportVO.getCommissionDepositTr1yr()));
					
					buff.append(COMMA);
					buff.append(checkZeroOrNull(bobReportVO.getCommissionDepositReg1Yr()));
					
					buff.append(COMMA);
					buff.append(checkZeroOrNull(bobReportVO.getCommissionDepositRegRen()));
					
					buff.append(COMMA);
					buff.append(checkZeroOrNull(bobReportVO.getCommissionAsset1Year()));
					
					buff.append(COMMA);
					buff.append(checkZeroOrNull(bobReportVO.getCommissionAssetRen()));
					
					if(displayColumn(LEVEL_2_HEADER.AB_ALL_YEAR, summary, userRole)) {
					  buff.append(COMMA);
					  buff.append(checkZeroOrNull(bobReportVO.getCommissionAssetAllYrs()));
					}
					
					if(displayColumn(LEVEL_2_HEADER.RIA_FIRM_NAME, summary, userRole)) {
					  buff.append(COMMA);
					  buff.append(getCsvString(bobReportVO.getRiaFirmName()));
					}
					
					if(displayColumn(LEVEL_2_HEADER.RIA_FEE_PAID_BY_PLAN, summary, userRole)) {
					  buff.append(COMMA);
					  buff.append(getRiaValue(bobReportVO.getRiaFirmName(), bobReportVO.getRiaFeePaidByPlan()));
					}
					
					if(displayColumn(LEVEL_2_HEADER.RIA_FEE_PAID_BY_JH, summary, userRole)) {
					  buff.append(COMMA);
					  buff.append(getRiaValue(bobReportVO.getRiaFirmName(), bobReportVO.getRiaFeePaidByJH()));
					}
					
					if (displayColumn(LEVEL_2_HEADER.RIA_BPS_MIN, summary, userRole)) {
						buff.append(COMMA);
						buff.append(getRiaFeeValue(bobReportVO.getRiaFirmName(),
								bobReportVO.getRiaBpsMin() == null ? null : bobReportVO.getRiaBpsMin().toString()));
					}
					
					if(displayColumn(LEVEL_2_HEADER.RIA_BPS_MAX, summary, userRole)) {
						  buff.append(COMMA);
						  buff.append(getRiaFeeValue(bobReportVO.getRiaFirmName(), bobReportVO.getRiaBpsMax()));
					}
						
					if(displayColumn(LEVEL_2_HEADER.RIA_AC_BLEND, summary, userRole)) {
						  buff.append(COMMA);
						  buff.append(getRiaFeeValue(bobReportVO.getRiaFirmName(), bobReportVO.getRiaAcBlend()));
					}
					
					if(displayColumn(LEVEL_2_HEADER.RIA_AC_TIERED, summary, userRole)) {
							buff.append(COMMA);
							buff.append(getRiaFeeValue(bobReportVO.getRiaFirmName(), bobReportVO.getRiaAcTiered()));
					}
					
					if(displayColumn(LEVEL_2_HEADER.RIA_FLAT_FEE_PER_HEAD, summary, userRole)) {
							buff.append(COMMA);
							buff.append(getRiaFeeValue(bobReportVO.getRiaFirmName(), bobReportVO.getRiaFlatFeePerHead()));
					}
					
					if(displayColumn(LEVEL_2_HEADER.RIA_FLAT_FEE_PRORATA, summary, userRole)) {
							buff.append(COMMA);
							buff.append(getRiaFeeValue(bobReportVO.getRiaFirmName(), bobReportVO.getRiaFlatFeeProrata()));
					}
					
					if (displayColumn(LEVEL_2_HEADER.COFID_321_BPS_FEE, summary, userRole)) {
						buff.append(COMMA);
						buff.append(checkZeroOrNull(bobReportVO.getCofid321ABFee()));
					}

					if (displayColumn(LEVEL_2_HEADER.COFID_321_PRORATA_FEE, summary, userRole)) {
						buff.append(COMMA);
						buff.append(checkZeroOrNull(bobReportVO.getCofid321DBFee()));
					}

					if (displayColumn(LEVEL_2_HEADER.COFID_338_BPS_FEE, summary, userRole)) {
						buff.append(COMMA);
						buff.append(checkZeroOrNull(bobReportVO.getCofid338ABFee()));
					}

					if (displayColumn(LEVEL_2_HEADER.COFID_338_PRORATA_FEE, summary, userRole)) {
						buff.append(COMMA);
						buff.append(checkZeroOrNull(bobReportVO.getCofid338DBFee()));
					}
					
					buff.append(COMMA);
					buff.append(getCsvString(bobReportVO.getRvpName()));
					
					buff.append(COMMA);
					buff.append(getCsvString(bobReportVO.getProductType()));
					
					buff.append(COMMA);
					buff.append(getCsvString(bobReportVO.getUsOrNy()));
					
					buff.append(COMMA);
					buff.append(getCsvString(bobReportVO.getFundClass()));
					
					buff.append(COMMA);
					buff.append(SmallPlanFeature.SFC.equals(bobReportVO.getSmallPlanOptionCode()) ? CommonConstants.YES : CommonConstants.NO);

					buff.append(COMMA);
					buff.append(SmallPlanFeature.PEP.equals(bobReportVO.getSmallPlanOptionCode()) ? CommonConstants.YES : CommonConstants.NO);
					
					buff.append(LINE_BREAK);
					
					producerCount++;
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> showMainReportInCSV().");
		}
		
		return buff.toString();
	}
    
    /**
     * 
     * @param riaFirmName
     * @param value
     * 
     * @return String
     */
    private String getRiaValue(String riaFirmName, BigDecimal value){
    	if(StringUtils.equals(riaFirmName, BDConstants.AESTRIK)) {
    		return riaFirmName;
    	} else {
    		return checkZeroOrNull(value);
    	}
    }
    
    /**
     * 
     * @param riaFirmName
     * @param value
     * 
     * @return String
     */
    private String getRiaFeeValue(String riaFirmName, String value){
    	if(StringUtils.equals(riaFirmName, BDConstants.AESTRIK) && value != null) {
    		return riaFirmName;
    	} else if (StringUtils.isEmpty(StringUtils.trimToEmpty(riaFirmName)) || value == null){
    		return BDConstants.HYPHON_SYMBOL;
    	} else if (StringUtils.isNotEmpty(StringUtils.trimToEmpty(riaFirmName)) && value != null){
    		return checkZeroOrNull(value);
    	}else {
    		return StringUtils.EMPTY;
    	}
    }
    
    /**
     * Returns broker information from list of brokers 
     * @param producerCode
     * @param brokerInfoList
     * 
     * @return BrokerInfoVO
     */
    private BrokerInfoVO getBrokerInfo(String producerCode, ArrayList<BrokerInfoVO> brokerInfoList ) {
    	for (BrokerInfoVO vo : brokerInfoList) {
    		if(vo.getProducerCode().equals(producerCode)) {
    			return vo;
    		}
    	}
    	return null;
    }
    
    /**
     * Checks whether given value is Zero or Null.
     * If the value is zero or null returns Hyphon symbol else return actual value
     * @param value
     * @return Hyphon symbol or actual value
     */
    private String checkZeroOrNull(String value){
    	if(value == null)
    		return StringUtils.EMPTY;
    	else if(value != null && Double.parseDouble(value) <= 0)
    		return StringUtils.EMPTY;
    	return getCsvString(value);
    }
    
    /**
     * Checks whether given value is Zero or Null.
     * If the value is zero or null returns Hyphon symbol else return actual value
     * @param value
     * @return Hyphon symbol or actual value
     */
    private String checkZeroOrNull(BigDecimal value){
    	if(value == null)
    		return StringUtils.EMPTY;
    	else if(value != null && value.compareTo(new BigDecimal(0.0)) <= 0)
    		return StringUtils.EMPTY;
    	return getCsvString(value);
    }
    
    /**
     * Returns the ZIP code in NNNNN-NNNN format
     * @param zipCode
     * @return
     */
    private String getCSVZipCode(String zipCode){
    	String formattedValue = StringUtils.EMPTY;
		try {
			if (zipCode == null)
				return formattedValue;
			zipCode = zipCode.trim();
			int valueLength = zipCode.length();
			switch(valueLength){
				case 5:
					formattedValue = zipCode;
					break;
				case 9:								
					StringBuffer strbf = new StringBuffer(zipCode.substring(0,5))
					.append("-")
					.append(zipCode.substring(5));
					formattedValue = strbf.toString();
					break;
			}
					
		} catch (Exception e) {
			logger.error("Exception thrown formatting zip code " + zipCode);
		}
		return formattedValue;
	}
    
    /**This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
    @Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
}
