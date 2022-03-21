package com.manulife.pension.bd.web.tools.templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.DynaForm;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.navigation.BDFirmLandingPages;
import com.manulife.pension.bd.web.pagelayout.BDContentTemplateLayoutBeanImpl;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.pagelayout.ContentTemplatePageContext;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfileHelper;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWDynamicContent;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.content.view.MutableLayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.util.log.LogUtility;

/**
 *
 * Creating a common template jsp to load all static CMA related pages.
 * Partnering with us
 *
 * @author aambrose
 */
@Controller
@RequestMapping(value="/content")

public class DynamicContentTemplateController extends BDController {
	public DynaForm populateForm() 
	{ 
	return	new DynaForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("content","/WEB-INF/layouts/content_template_layout.jsp" );
		}

    private static final String CONTENT_ID = "id";

    private static final String MENU_NAME = "menuName";

    private static final String DYNAMIC_CONTENT_PATH = "content";

    private static final String BODY_ID_COMMON_ELEMENTS = "common_elements";

    private static final String DYNAMIC_TEMPLATE1_BODY = "/WEB-INF/layouts/dynamicTemplate1Body_layout.jsp";

    private static final String DYNAMIC_TEMPLATE3_BODY = "/WEB-INF/layouts/dynamicTemplate3Body_layout.jsp";

    private static final String PARTNERING_WITH_US = "partnering";

    private static final String FIND_LITERATURE_MENU_ID = "literature";

    private static final String PRIME_ELEMENTS_MENU_ID = "prime";

    private static final String UNDERSCORE = "_";

    private static final String DYNAMIC_TEMPLATE1 = "1";

    private static final String DYNAMIC_TEMPLATE3 = "3";


    //To initialize the dynamic content group ids
    private static final ArrayList<Integer> BD_DYNAMIC_CONTENT_GROUP_IDS = new ArrayList<Integer>(12);

    static {
        BD_DYNAMIC_CONTENT_GROUP_IDS.add(113); // BD DynCnt - Ameriprise
        BD_DYNAMIC_CONTENT_GROUP_IDS.add(114); // BD DynCnt - EdwardJones
        BD_DYNAMIC_CONTENT_GROUP_IDS.add(115); // BD DynCnt - JHFN
        BD_DYNAMIC_CONTENT_GROUP_IDS.add(116); // BD DynCnt - MerrillLynch
        BD_DYNAMIC_CONTENT_GROUP_IDS.add(117); // BD DynCnt - MorganStanley
        BD_DYNAMIC_CONTENT_GROUP_IDS.add(118); // BD DynCnt - NML
        BD_DYNAMIC_CONTENT_GROUP_IDS.add(119); // BD DynCnt - Others/Indep.
        BD_DYNAMIC_CONTENT_GROUP_IDS.add(120); // BD DynCnt - Shared
        BD_DYNAMIC_CONTENT_GROUP_IDS.add(121); // BD DynCnt - Smith Barney
        BD_DYNAMIC_CONTENT_GROUP_IDS.add(122); // BD DynCnt - UBS
        BD_DYNAMIC_CONTENT_GROUP_IDS.add(126); // BD DynCnt - Planning Corp. of America
        BD_DYNAMIC_CONTENT_GROUP_IDS.add(131); // BD DynCnt - MetLife
    }

    public DynamicContentTemplateController() {
        super(DynamicContentTemplateController.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.manulife.pension.platform.web.controller.BaseAction#doExecute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.Form,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @RequestMapping( method =  {RequestMethod.GET}) 
   	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   			throws IOException,ServletException, SystemException {
       	if(bindingResult.hasErrors()){
       		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
       		String contentIdParam = (String) request.getSession().getAttribute("pId");
		    String menuName = (String) request.getSession().getAttribute("pMenuName");
		    BDLayoutBean bdLayoutBean =   (BDLayoutBean) request.getSession().getAttribute("pLayoutBean");

			    if (contentIdParam != null|| menuName != null) {
			    	String forward = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			    	String path = forwards.get(forward);
			    	StringBuffer buff= new StringBuffer();
			    	buff.append(path);
			    	buff.append("?");
			    	buff.append(CONTENT_ID);
			    	buff.append("=");
			    	buff.append(contentIdParam!= null ?contentIdParam:StringUtils.EMPTY);
			    	buff.append("&");
			    	buff.append(MENU_NAME);
			    	buff.append("=");
			    	buff.append(menuName);
			    	;
			    	request.getSession().setAttribute(CommonConstants.ERROR_RDRCT,new ControllerForward(buff.toString(), false));
				   request.setAttribute(CONTENT_ID, contentIdParam!= null ?contentIdParam:StringUtils.EMPTY);
				   request.setAttribute(MENU_NAME, menuName);
				   request.setAttribute(BDConstants.LAYOUT_BEAN, bdLayoutBean);
				   request.setAttribute(GlobalConstants.CONTENT_LOCATION_ATTRIBUTE_NAME, Location.USA);
				   request.getSession().removeAttribute("pId");
				   request.getSession().removeAttribute("pMenuName");
				   request.getSession().removeAttribute("pLayoutBean");
				   BDSessionHelper.removeBOBTabSelectionFromSession(request);
				}
		        
       		
       		if(errDirect!=null){
       			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
       		return	forwards.get("content");//if input forward not //available, provided default
       		}
       	}  

        String contentIdParam = request.getParameter(CONTENT_ID);
        String menuName = request.getParameter(MENU_NAME);

	    if (contentIdParam != null|| menuName != null) {
		   request.getSession().setAttribute("pId", contentIdParam);
		   request.getSession().setAttribute("pMenuName", menuName);
		}
        
        String approvingFirmCode = BDConstants.BLANK;
        int contentId = BDConstants.NUM_MINUS_1;
        ContentTemplatePageContext contentTemplatePageContext = null;

        BDUserProfile bdUserProfile = BDSessionHelper.getUserProfile(request);
        ApplicationHelper.setRequestContentLocation(request, Location.USA);
        request.getSession().setAttribute(GlobalConstants.CONTENT_LOCATION_ATTRIBUTE_NAME, Location.USA);
        if (!bdUserProfile.isInternalUser()) {
            approvingFirmCode = bdUserProfile.getAssociatedApprovingFirmCode();
            if (BDUserProfileHelper.isBasicFinancialRep(bdUserProfile)) {
                ApplicationHelper.setRequestContentLocation(request,
                        Location.NEW_YORK);
            }
        }
        // TODO: Setting the location must be revisited. currently default to US

        Location locaiton = ApplicationHelper
                .getRequestContentLocation(request);

        // TODO: This section will be reviewed with actual content ids.
        // start of the Landing page
        if (StringUtils.equalsIgnoreCase(menuName, PARTNERING_WITH_US)) {
            // user select the Partnering with us tab
            contentId = BDFirmLandingPages.getInstance()
                    .getPartneringLandingPageCMAKey(approvingFirmCode);
            contentTemplatePageContext = new ContentTemplatePageContext(
                    contentId, PARTNERING_WITH_US,
                    BODY_ID_COMMON_ELEMENTS, DYNAMIC_TEMPLATE1_BODY);
        } else if (StringUtils.equalsIgnoreCase(menuName,
                FIND_LITERATURE_MENU_ID)) {
            // user select the Find Literature tab
            contentId = BDFirmLandingPages.getInstance()
                    .getLiteratureLandingPageCMAKey(approvingFirmCode);
            contentTemplatePageContext = new ContentTemplatePageContext(
                    contentId, FIND_LITERATURE_MENU_ID, BODY_ID_COMMON_ELEMENTS,
                    DYNAMIC_TEMPLATE1_BODY);
        } else if (StringUtils.equalsIgnoreCase(menuName,
                PRIME_ELEMENTS_MENU_ID)) {
            // user select the Prime Elements tab
            contentId = BDFirmLandingPages.getInstance()
                    .getPrimeLandingPageCMAKey(approvingFirmCode);
            contentTemplatePageContext = new ContentTemplatePageContext(
                    contentId, PRIME_ELEMENTS_MENU_ID, BODY_ID_COMMON_ELEMENTS,
                    DYNAMIC_TEMPLATE1_BODY);
            // end of landing page
        } else if (StringUtils.isNotEmpty(contentIdParam)
                && StringUtils.isNumeric(contentIdParam)) {
            // start of dynamic link page
            // user select the dynamic link in landing page.
            contentId = Integer.parseInt(contentIdParam);
            LayoutPage layoutPage = getLayoutPageBean(locaiton, Integer
                    .parseInt(contentIdParam));
            String pageId = layoutPage.getPageId();

            if (StringUtils.isNotEmpty(pageId)) {
                String menuId = StringUtils.substringBefore(pageId, UNDERSCORE);
                String dynamicTemplate = StringUtils.substringAfter(pageId,
                        UNDERSCORE);
                if (DYNAMIC_TEMPLATE1.equals(dynamicTemplate)
                        || DYNAMIC_TEMPLATE3.equals(dynamicTemplate)) {
                    contentTemplatePageContext = new ContentTemplatePageContext(
                            Integer.parseInt(contentIdParam), menuId,
                            BODY_ID_COMMON_ELEMENTS,
                            StringUtils.equalsIgnoreCase(dynamicTemplate,
                                    DYNAMIC_TEMPLATE1) ? DYNAMIC_TEMPLATE1_BODY
                                    : DYNAMIC_TEMPLATE3_BODY);
                }
            }
        }// end of dynamic link page
        if (contentTemplatePageContext != null && BD_DYNAMIC_CONTENT_GROUP_IDS
                .contains(getContentParentId(contentId))) {
        } else {
            // to load the error page.
            contentTemplatePageContext = new ContentTemplatePageContext(
                    BDContentConstants.BDW_STANDARD_ERROR_PAGE, BDConstants.BLANK, BODY_ID_COMMON_ELEMENTS,
                    DYNAMIC_TEMPLATE1_BODY);
        }
        BDLayoutBean bdLayoutBean = new BDContentTemplateLayoutBeanImpl(
                ApplicationHelper.getRequestContentLocation(request),
                contentTemplatePageContext);
        request.setAttribute(BDConstants.LAYOUT_BEAN, bdLayoutBean);
        request.getSession().setAttribute("pLayoutBean",bdLayoutBean);
        BDSessionHelper.removeBOBTabSelectionFromSession(request);
        return forwards.get(DYNAMIC_CONTENT_PATH);
    }

    /**
     * To retrieve the layout page bean for the given content id If not return
     * empty MutableLayoutPage bean
     *
     * @param Location
     *            location
     * @param int
     *            contentId
     * @return LayoutPage layoutPage
     */
    private LayoutPage getLayoutPageBean(Location location, int contentId) {
        LayoutPage layoutPage = null;
        Content bean = null;
        try {
            if (location == null) {
                bean = ContentCacheManager.getInstance().getContentById(
                        contentId, ContentTypeManager.instance().LAYOUT_PAGE);
            } else {
                bean = ContentCacheManager.getInstance().getContentById(
                        contentId, ContentTypeManager.instance().LAYOUT_PAGE,
                        location);
            }

        } catch (Exception e) {
            SystemException se = new SystemException(e, this.getClass()
                    .getName(), "getLayoutPageBean", "Content id:" + contentId);
            LogUtility.logSystemException("BD", se);
        }

        if (bean != null) {
            if (bean instanceof LayoutPage)
                layoutPage = (LayoutPage) bean;
        }

        // This is modified to return at least empty LayoutPage object.
        // Otherwise we won't be able to set the
        // null object in the jsp. This is helpful during the development. We
        // can take it out when we
        // finished development or have all the content ready. (IC)
        return layoutPage == null ? (LayoutPage) new com.manulife.pension.content.view.MutableLayoutPage()
                : layoutPage;
    }

    /**
     * Retrieve the parent id for the given content id content id.
     *
     * @param int
     *            contentId
     * @return int parentId
     */
    private int getContentParentId(int contentId) {
        Content content = null;
        int parentId = 0;
        try {
            content = ContentCacheManager.getInstance().getContentById(
                    contentId, ContentTypeManager.instance().LAYOUT_PAGE);
            MutableLayoutPage layoutPage = (MutableLayoutPage) content;
            parentId = layoutPage.getParentId();
        } catch (Exception e) {
            SystemException se = new SystemException(e, this.getClass()
                    .getName(), "getContentParentId", "Content id:" + contentId);
            LogUtility.logSystemException("BD", se);
        }
        return parentId;
	}

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
    
	 @Autowired
	   private BDValidatorFWDynamicContent  bdValidatorFWDynamicContent;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWDynamicContent);
	}
}
