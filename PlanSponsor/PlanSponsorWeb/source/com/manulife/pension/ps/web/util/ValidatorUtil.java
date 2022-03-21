package com.manulife.pension.ps.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.util.content.GenericException;

public class ValidatorUtil {
	protected static UserProfile getUserProfile(HttpServletRequest request) {
		return PsController.getUserProfile(request);
	}
	protected static final String FILTER_TASK = "filter";

    protected static final String SHOWALL_TASK = "showAll";

    protected static final String SORT_TASK = "sort";

    protected static final String PAGE_TASK = "page";

    protected static final String DOWNLOAD_TASK = "download";

    protected static final String DOWNLOAD_ALL_TASK = "downloadAll";

    protected static final String PRINT_TASK = "print";

    protected static final String DEFAULT_TASK = "default";

    protected static final String PRINT_PDF_TASK = "printPDF";

	protected static final String RESET_TASK = "reset";
	/**
     * Gets the current task for this request.
     *
     * @param request
     *            The current request object.
     * @return The task for this request.
     */
    protected String getTask(HttpServletRequest request) {
        String task = request.getParameter(TASK_KEY);
        if (task == null) {
            task = DEFAULT_TASK;
        }
        return task;
    }
	
	  protected static final String TASK_KEY = "task";
	  
	  
	  
	  /**
		 * Validates date range
		 * 
		 * @param fromDate
		 * @param toDate
		 * @param errors
		 */
		protected void validateDates(String fromDateStr, String toDateStr, Collection<GenericException> errors) {

			if (StringUtils.isEmpty(fromDateStr) && StringUtils.isEmpty(toDateStr)) {
				errors.add(new GenericException(2264));
			} else {
				Date fromDateDt = null;
				if (StringUtils.isEmpty(fromDateStr)) {
					errors.add(new GenericException(2265));
				} else {
					try {
						fromDateDt = dateParser(fromDateStr);
					} catch (Exception e) {
					}
				}

				Date toDateDt = null;
				if (!StringUtils.isEmpty(toDateStr)) {
					try {
						toDateDt = dateParser(toDateStr);
					} catch (Exception e) {
					}
				}

				if ((!StringUtils.isEmpty(fromDateStr) && fromDateDt == null) || (!StringUtils.isEmpty(toDateStr) && toDateDt == null)) {
					errors.add(new GenericException(2268));
				}

				Date today = new Date(System.currentTimeMillis());
				if (fromDateDt != null && fromDateDt.after(today)) {
					errors.add(new GenericException(3015));
				} else if (toDateDt != null && toDateDt.after(today)) {
					errors.add(new GenericException(3015));
				}

				if (fromDateDt != null && toDateDt != null) {
					if (fromDateDt.after(toDateDt)) {
						errors.add(new GenericException(2266));
					}
				}
			}
		}
		
		/**
		 * Default page size
		 */
		protected static final int DEFAULT_PAGE_SIZE = 35;

		protected static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
		static {
			DATE_FORMATTER.setLenient(false);
		}

		//synchronizes this method to avoid race condition.
		protected static synchronized String  dateFormatter(Date inputDate){ 
			return DATE_FORMATTER.format(inputDate); 
		} 

		protected static synchronized Date  dateParser(String value) throws ParseException{ 
			return DATE_FORMATTER.parse(value); 
		}
}
