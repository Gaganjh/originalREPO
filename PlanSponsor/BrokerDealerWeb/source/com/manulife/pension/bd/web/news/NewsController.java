package com.manulife.pension.bd.web.news;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWNews;
import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.view.MutableUpdate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.util.DateComparator;

/**
 * This the action class for BD News Page
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping( value ="/news")

public class NewsController extends BaseAutoController {
	@ModelAttribute("newsForm") 
	public NewsForm populateForm() 
	{
		return new NewsForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("news","/news/news.jsp" );
		forwards.put("newsArchive","/news/newsArchive.jsp");
		forwards.put("newsArticle","/news/newsArticle.jsp"); 
		forwards.put("newsArchiveArticle","/news/newsArchiveArticle.jsp");
		}

	
    public static final String NEWS_FORWARD = "news";

    public static final String NEWS_ARCHIVE_FORWARD = "newsArchive";

    public static final String NEWS_ARTICLE_FORWARD = "newsArticle";

    public static final String NEWS_ARCHIVE_ARTICLE_FORWARD = "newsArchiveArticle";

    public static final int[] comparisonFields = new int[] { Calendar.MONTH, Calendar.YEAR };

    /**
     * Constructor
     */
    public NewsController() {
        super(NewsController.class);
    }

    /**
     * The method will be called when the action parameter is null or default. This will forward the
     * user to the BD News page.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    
    @RequestMapping(value ="/",  method =  {RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("newsForm") NewsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              forwards.get("news");//if input forward not //available, provided default
	       }
		}
   
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDefault");
        }
        BDSessionHelper.removeBOBTabSelectionFromSession(request);
        return forwards.get(NEWS_FORWARD);
    }

    /**
     * This method will be called when the action parameter is archive. This will retrieve the news
     * items archived in the the given month & year from CMA. If month & year parameters are not
     * present then the news archived in the current month & year will be retrievd from CMA
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @RequestMapping(value ="/" ,params={"action=archive"} , method =  {RequestMethod.GET}) 
    public String doArchive (@Valid @ModelAttribute("newsForm") NewsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws  SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              forwards.get("news");//if input forward not //available, provided default
	       }
		}
    
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doArchive");
        }
        Collection news = null;
        List<Content> archivedNewsByMonthYear = new ArrayList<Content>();
       
        String month = null;
        String year = null;
        try {
            Date archiveDate = new Date();
            month = actionForm.getMonth();
            year = actionForm.getYear();
            if (!StringUtils.isEmpty(month) && !StringUtils.isEmpty(year)) {
                Calendar calendar = Calendar.getInstance();
                try {
                    // For Calendar, month starts from 0
                    calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                } catch (NumberFormatException nfe) {
                    throw new SystemException("Invalid archived month or year value - Month : "
                            + month + " Year : " + year + " " + nfe.toString());
                }
                archiveDate = calendar.getTime();
            }
            actionForm.setSelectedArchiveDate(archiveDate);
            news = Arrays.asList((BrowseServiceDelegate.getInstance()).findContent(true,
                    ContentTypeManager.instance().UPDATE));
            Iterator iter = news.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (obj instanceof Content) {
                    Content content = (Content) obj;
                    MutableUpdate mutableUpdate = (MutableUpdate) content;
                    if (mutableUpdate.getParentId() == BDContentConstants.BD_NEWS_GROUP
                            && DateComparator.compare(archiveDate, mutableUpdate.getDate(),
                                    comparisonFields) == 0) {
                        archivedNewsByMonthYear.add(content);
                    }
                }
            }
            actionForm.setArchivedNews(archivedNewsByMonthYear);
            actionForm.setArchiveDatesList(getArchiveDatesList(Calendar.getInstance()));
            if (logger.isDebugEnabled()) {
                logger.debug("exit -> doArchive");
            }
        } catch (ContentException ce) {
            logger.error("Exception while retrieving the archived news ", ce);
            throw new SystemException("Exception while retrieving the archived news "
                    + ce.toString());
        }
        return forwards.get(NEWS_ARCHIVE_FORWARD);
    }

    /**
     * This method will be called when the action parameter is article. This will forward the user
     * to BD News Article page
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @RequestMapping(value ="/", params={"action=article"} , method =  {RequestMethod.GET}) 
    public String doArticle (@Valid @ModelAttribute("newsForm") NewsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws  SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              forwards.get("news");//if input forward not //available, provided default
	       }
		}
    
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doArticle");
        }
        return forwards.get(NEWS_ARTICLE_FORWARD);
    }

    /**
     * This method will be called when the action parameter is archiveArticle. This will create a
     * list of 12 dates and set it in the form object and then forward the user to BD News
     * Article(with archived new item) page.
     * 
     * @param newsForm
     * @param request
     * @return String a String the represents the specific forward
     * @throws SystemException
     */
    @RequestMapping(value ="/", params={"action=archiveArticle"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doArchiveArticle (@Valid @ModelAttribute("newsForm") NewsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              forwards.get("news");//if input forward not //available, provided default
	       }
		}
   
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doArchiveArticle");
        }
        
        actionForm.setArchiveDatesList(getArchiveDatesList(Calendar.getInstance()));
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doArchiveArticle");
        }
        return forwards.get(NEWS_ARCHIVE_ARTICLE_FORWARD);
    }

    /**
     * This method returns a list of 12 dates starting from the given date and then reducing the
     * month field by 1. The dates will be formatted in the jsp to display only the month and year
     * portion. (Example : July 2008)
     * 
     * @param calendar a Calendar object with start date
     * @return List a list of Date objects
     * @throws SystemException
     */
    public List<Date> getArchiveDatesList(Calendar calendar) throws SystemException {
        try {
            List<Date> archiveDatesList = new ArrayList<Date>(12);
            for (int i = 0; i < 12; i++) {
                archiveDatesList.add(calendar.getTime());
                calendar.add(Calendar.MONTH, -1);
            }
            return archiveDatesList;
        } catch (Exception e) {
            logger.error("Exception while retrieving the month list ", e);
            throw new SystemException("Exception while retrieving the month list " + e.toString());
        }
	}

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */
    
    @Autowired
	   private BDValidatorFWNews  bdValidatorFWNews;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWNews);
	}
}
