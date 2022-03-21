package com.manulife.pension.bd.web;


import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.service.security.AbstractPrincipal;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequest.Type;
import com.manulife.pension.service.security.role.BDUserRole;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidateCatalogHelper;
import com.manulife.pension.validator.ValidateCatalogLaunch;
import com.manulife.pension.validator.valueobject.ViolationVO;

public class FrwValidation extends ValidateCatalogLaunch  {
	private static FrwValidation theInstance = null;
	private static final Logger logger = Logger.getLogger(FrwValidation.class);

	//public final static String BdBaseAction.ERROR_KEY = CommonEnvironment.getInstance().getErrorKey();

	//
	// Instantiate catalog launch upon load.
	//
	static {
		synchronized(FrwValidation.class) {
			FrwValidation.getInstance();
		}
	}

    private FrwValidation(String validateSanitize) {
    	super(validateSanitize, FRW_APPL);
    }
    
    /**
     * A singleton to obtain application-specific catalog properties and to load a catalog.
     * 
     * @return Catalog API handle.
     */
    public static synchronized FrwValidation getInstance()  {
        if (theInstance == null) {
        	try {
        		theInstance = new FrwValidation(ValidateCatalogHelper.getInstance().getPenetrationDetectionDirective(PENETRATION_FEATURE_FRW));
        	} 
        	catch (Exception e) {
        		logger.error("Exception when trying to instantiate FrwValidation(): " + e.toString(), e);
        	}
        }
        return theInstance;
    }
    
    /**
     * Subclass and override this mocked-up method with application-specific error handling.
     * 
     * @param before - Number of errors before sanitation.
     * @param after - Number of errors after sanitation.
     * @param violationKeyType - Violation key types.
     * @param badFields - CSV of violated fields.
     * @param violations - Error/Violations list (i/o) to append application-specific errors.
     * @param request - Current web request.
     * 
     * @return True - if violation exception found, false otherwise.
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected boolean assignMessageTypeToVoilations(int before, int after, int violationKeyType, String badFields, Collection violations, HttpServletRequest request) {
    	 boolean violationFound = false;
    	 if (after > before && violationKeyType != 0) {
    		 violationFound = true;
    		 GenericException frwe = null;
    		 switch (violationKeyType) {
	    		 case ERROR_CODE_WITH_GUI_FIELD_NAME:
                     	frwe = new GenericException(CommonErrorCodes.ERROR_FRWVALIDATION_WITH_GUI_FIELD_NAME, new Object[] { badFields });
	    		 		break;
	    		 case ERROR_CODE_WITH_GUI_FIELD_NAMES:
                  		frwe = new GenericException(CommonErrorCodes.ERROR_FRWVALIDATION_WITH_GUI_FIELD_NAMES, new Object[] { badFields });
	    		 		break;
	    		 case ERROR_CODE_WITHOUT_GUI_FIELD_NAME:
                     	frwe = new GenericException(CommonErrorCodes.ERROR_FRWVALIDATION_WITHOUT_GUI_FIELD_NAME);
	    		 		break;
	    		 case ERROR_CODE_WARNING:
	    		 	    break;
	    		 default:
	    			 	logger.debug("assignMessageTypeToVoilations() has unsupported Violation Key Type: " + violationKeyType);
	    			 	break;
    		 }    		 
    		 //
    		 // Log errors & warnings from the current request into MRL database.
    		 //
              if (violations != null) {
            	  String profileAndContract =  getUserProfileIdAndContract(request);
            	  for (Object e: violations) {
            		  if (e instanceof ViolationVO) {
                		  ViolationVO v = (ViolationVO) e;
                		  if (v.isError()) {
                			  logger.error("ERROR! Security violation detected: " + v.toString() + profileAndContract);
                		  }
                		  else {
                			  logger.debug("WARNING! Security violation suspected: " + v.toString() + profileAndContract);
                		  }
            		  }
            	  }
              }
              
              //
              // For GUI reporting, violations list must contain a single error from the current request.
              //
              if (violations != null) {
            	  //
            	  // Prior to sanitation check errors are retained.
            	  //
            	  ArrayList<Object> errors = new ArrayList<Object>();
            	  for (Object e : violations) {
            		  if (!(e instanceof ViolationVO)) {
            			  errors.add(e);
            		  }
            	  }
            	  //
            	  // Only single per current request error is reported to the GUI.
            	  //
            	  if (frwe != null) {
            		  errors.add(frwe);
            	  }
            	  
            	  //
            	  // Final list of violations contain earlier errors and
            	  // current error (warnings excluded).
            	  //
            	  if (errors.size() > 0) {
            		  violations.clear();
            		  violations.addAll(errors);
            	  }
     		 }
    	}
    	return violationFound;
    }
    
    /**
     * Obtain user profile and other tracking info to be used for error reporting.
     * 
     * @param request - Current web request.
     * @return CSV text of profile and contract, or empty if not found.
     */
	protected String getUserProfileIdAndContract(HttpServletRequest request) {

		String profileIdAndContract = "";
		BDUserProfile userProfile = null;
		userProfile = BDSessionHelper.getUserProfile(request);

		if (userProfile != null) {
			long profId = -1;
			long aprofId = -1;
			String roleId = "";
			String aTypeCode = "";
			
			BDPrincipal princ = userProfile.getBDPrincipal();
			if (princ != null) {
				profId = userProfile.getBDPrincipal().getProfileId();
			}
			
			AbstractPrincipal aprinc = userProfile.getAbstractPrincipal();
			if (aprinc != null) {
				aprofId = aprinc.getProfileId();
			}
			
			BDUserRole role = userProfile.getRole();
			if (role != null) {
				roleId = role.getRoleId();
			}
			
			Type aType = userProfile.getActivationType();
			if (aType != null) {
				aTypeCode = aType.getCode();
			}
			
			if (profId != -1 || aprofId != -1) {
				profileIdAndContract = ", ProfileID:" + profId + ", PrincipalID:" + aprofId + ", RoleID:" + roleId + ", ActivationType: " + aTypeCode;
			}
		}
		return profileIdAndContract;
	}

	/*
	 * Validate for PenTest penetration for action FrwAutoAction validation.
	 
	@SuppressWarnings("rawtypes")
	public static Collection doValidatePenTestAction(Form form, HttpServletRequest request) {
		return doValidatePenTestAction(form, request, CommonConstants.ERROR_PAGE);
	}*/

	/*@SuppressWarnings("rawtypes")
	public static Collection doValidatePenTestAction(Form form, HttpServletRequest request, String errRdrct) {
		return doValidatePenTestActionCore(form, request, errRdrct, null, false);
	}*/

	/**
	 * Validate for PenTest penetration for auto-action FrwAutoAction
	 * validation.
	 */
	/*@SuppressWarnings("rawtypes")
	public static Collection doValidatePenTestAutoAction(Form form, 
			HttpServletRequest request) {
		return doValidatePenTestAutoAction(form,  request, CommonConstants.ERROR_PAGE);
	}*/

	/*@SuppressWarnings("rawtypes")
	public static Collection doValidatePenTestAutoAction(Form form, 
			HttpServletRequest request, String errRdrct) {
		return doValidatePenTestActionCore(form, request, errRdrct,  true);
	}*/

	/**
	 * Validate against PenTest attack for Action and AutoAction validations.
	 * 
	 * @return Collection of errors, if any. Session attributes for errors and navigation set.
	 */
	/*@SuppressWarnings("rawtypes")
	private static Collection doValidatePenTestActionCore(Form form, HttpServletRequest request, String errRdrct,
			ActionMapping mapping, boolean autoAction) {
		Form valForm = ((form != null) ? form : new DynaForm());
		ArrayList errors = new ArrayList();
		if (FrwValidation.getInstance().validateSanitizeCatalogedFormFields(valForm, errors, request) == false) {
			request.getSession().setAttribute(CommonEnvironment.getInstance().getErrorKey(), errors);
			request.getSession().setAttribute(CommonErrorCodes.ERROR_FRW_VALIDATION, CommonErrorCodes.ERROR_FRW_VALIDATION);
			if (autoAction == false || mapping == null) {
				request.getSession().setAttribute(CommonConstants.ERROR_RDRCT,
						((!StringUtils.isBlank(errRdrct)) ? errRdrct : CommonConstants.ERROR_PAGE));
			} else {
				request.getSession().setAttribute(CommonConstants.ERROR_RDRCT, mapping
						.findForward(((!StringUtils.isBlank(errRdrct)) ? errRdrct : CommonConstants.ERROR_PAGE)));
			}
		}
		return errors;
	}*/
}
