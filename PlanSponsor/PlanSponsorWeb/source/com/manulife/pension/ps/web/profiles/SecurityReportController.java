package com.manulife.pension.ps.web.profiles;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.util.content.GenericException;

abstract class SecurityReportController extends ReportController {

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
    
    protected SecurityReportController() {
        super();
    }

    protected SecurityReportController(Class className) {
        super(className);
    }

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

}
