package com.manulife.pension.bd.web.navigation;

/**
 * The constants for URLs
 * 
 * @author guweigu
 *
 */
public interface URLConstants {

    String PostLogin = "/do/postLogin";

    String ContactUs = "/do/contactUs/";

    String MyProfileExternal = "/do/myprofileDispatch";
    
    String MyProfileInternal = "/do/myprofile/internal";
    
    String UserManagement = "/do/usermanagement/search";

    String ContentManagement = "/do/firmRestrictions/management";

    String LogOut = "/do/logout/";

    String StartMimic = "/do/mimic/start";
    
    String ExitMimic = "/do/mimic/end";

    String LoginURL = "/do/home/";

    String HomeURL = "/do/home/";
    
    String ChangeTempPasswordURL = "/do/changeTempPassword/";
    
    String UpdatePasswordURL = "/do/updatePassword/";

    String BackToUserManagement = UserManagement + "?task=refresh";

    String ManageBasicBroker = "/do/manage/basicBroker";
    
    String ManageBroker = "/do/manage/broker";
    
    String ManageAssistant = "/do/manage/assisstant";
    
    String ManageFirmRep = "/do/manage/firmrep";
    
    String MyProfileBrokerPersonalTab = "/do/myprofile/brokerPersonalInfo";

    String MyProfilePersonalTab = "/do/myprofile/personalInfo";

    String MyProfileSecurityTab = "/do/myprofile/security";

    String MyProfileAssistantsTab = "/do/myprofile/assistants";

    String MyProfileLicenseTab = "/do/myprofile/license";

    String MyProfilePreferenceTab = "/do/myprofile/preference";

    String MyProfileAddBOBTab = "/do/myprofile/addBOB";
    
    String MyProfileCreateBOBTab = "/do/myprofile/createBOB";
    
    String ContentRuleMaintenance = "/do/firmRestrictions/maintenance";

    String ContentRuleManagement = "/do/firmRestrictions/management";

    String ExternalBrokerRegistration = "/do/registerExternalBroker/start";

	String GlobalMessages = "/do/globalMessages/";
	
	String MessageCenter = "/do/messagecenter/";
	
	String FundEvaluator = "/do/fundEvaluator";
	
	String PlanReview = "/do/bob/planReview/";
	
	String RiaEstatements = "/do/estatement/fetch";
	
	String RiaDirectEstatements = "/do/ViewRIAStatements";
	
	String StepupAuth = "/do/stepupPasscode/";
	
	String StepupTransition = "/do/stepupTransition/";
}
