package com.manulife.pension.bd.web.fap.validators;

/**
 * Plug-in validator to check that startDate and endDate have minimum range of 6 months
 *
 * @author 	Dmitry Kobets
 * @version
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.fap.BDPerformanceChartInputForm;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.investment.ChartDataBean;
import com.manulife.pension.platform.web.util.DataValidationHelper;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.Fund;
import com.manulife.pension.service.fund.valueobject.FundUnitValueHistory;
import com.manulife.pension.service.fund.valueobject.HistoricalUnitValue;
import com.manulife.pension.util.content.GenericException;

public class CheckDates {

    private static Logger logger = Logger.getLogger(CheckDates.class);

    /**
     * Callback invoked by struts Validator (commons-validator.jar)
     */
    @SuppressWarnings("unchecked")
	public static Collection<GenericException> checkDates(BDPerformanceChartInputForm form, HttpServletRequest request) {

    	SimpleDateFormat f = new SimpleDateFormat("MM/yyyy");
        SimpleDateFormat longFormat = new SimpleDateFormat("MM/dd/yyyy");
        Collection<GenericException> errors = new ArrayList<GenericException>();

        try {
            String endDateString = form.getEndDate();
            String startDateString = form.getStartDate();

            String format = BDConstants.CHART_DATE_PATTERN;

            if (!DataValidationHelper.isBlankOrNull(endDateString) &&
                !DataValidationHelper.isBlankOrNull(startDateString) &&
                format != null) {

                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setLenient(false);
                Date endDate = sdf.parse(endDateString);
                Date startDate = sdf.parse(startDateString);

                Calendar c1 = new GregorianCalendar();
                Calendar c2 = new GregorianCalendar();
                c1.setTime(startDate);
                c2.setTime(endDate);

                //make sure end is after start
                if (startDate.after(endDate)) {
                    errors.add(new GenericException(BDErrorCodes.CHART_START_DATE_EXCEEDS_END_DATE));
                }
                else {
                    c1.add(GregorianCalendar.MONTH, 6);
                    //check 6 months range
                    if (c1.after(c2)) {
                        errors.add(new GenericException(BDErrorCodes.CHART_CHECKFUNDS_SIX_MONTHS));
                    }
                }

				if (errors.size() > 0) {
					return errors;
				}
	 
               	Date today = new Date();
	   	        if (endDate.after(today)) {
      	          	errors.add(new GenericException(BDErrorCodes.CHART_CHECKDATES_END_DATE_IN_THE_FUTURE, new Object[]{f.format(today)}));
					return errors;
              	}

				if (!isAFundSelected(form)){
					return errors;
				}

            	//reset day to be the last day of the month
            	c1.setTime(startDate);
            	c2.setTime(endDate);
            	c1.set(Calendar.DAY_OF_MONTH, c1.getActualMaximum(Calendar.DAY_OF_MONTH));
            	c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH));
            	startDate = c1.getTime();
            	endDate = c2.getTime();

            	AccountServiceDelegate delegate = AccountServiceDelegate.getInstance();

            	// retrieve contract number from User profile
            	BDUserProfile profile = BDSessionHelper.getUserProfile(request);

            	if (profile == null) {
                	errors.add(new GenericException(BDErrorCodes.CHART_GENERIC, new Object[]{"missing profile"}));
					return errors;
            	}

				// We need to have contractNumber as a String here since,
				// FundUnitValueHistory needs it as a String, and I do not have 
				// time to change that code at this time.

                Fund[] fundsArray =
                    (Fund[]) request.getSession(false)
                    .getAttribute(BDConstants.VIEW_FUNDS_ORIGINAL);
            
                //determine which funds were selected
                List<String> fundNames = new ArrayList<String>();
                List<String> fundPctLabels = new ArrayList<String>();
                List fundFootnotes = new ArrayList();
                
                
                
                String[] fundIds = getFunds(form, fundsArray, fundNames, fundPctLabels);
                
                FundServiceDelegate fundDelegate = FundServiceDelegate.getInstance();
                Map<String,String> footNotes = fundDelegate.getFundFootNotesForCharting(fundIds);
                
                for(int i=0;i<fundIds.length;i++){
                	//System.out.println("FundId<-->FundNotes = "+fundIds[i]+"<-->"+footNotes.get(fundIds[i]));
                	String[] allSymbolsArray =  StringUtils.split(((footNotes.get(fundIds[i])==null)?"":footNotes.get(fundIds[i])), ',');
                	fundFootnotes.add(allSymbolsArray);
                }
                
                String allSymbols = new String();
                for (String symbol : footNotes.values()) {
        			if (StringUtils.isNotBlank(allSymbols)){
        				allSymbols += ",";
        			}
        			allSymbols += symbol;  
        		}
                String[] allSymbolsArray =  StringUtils.split(allSymbols, ',');
                
                
                
                List footNotesList = new ArrayList();
                footNotesList.add(footNotes.values());
                

                for (int i = 0; i < fundIds.length; i++)
                	if (logger.isDebugEnabled()) {
	                    logger.debug("Fund: " + fundIds[i] + "/" + (String) fundNames.get(i));
                	}
                // retrieve history for contract
                FundUnitValueHistory[] history = fundDelegate.getFundUnitValueHistories(
                    form.getFundClass(), fundIds, startDate, endDate);

                
               // The below code has been implemented in PlanSponsor. 
               /* if (fundIds.length != history.length) {
                    errors.add(new GenericException(BDErrorCodes.CHART_GENERIC,
                        new Object[]{"data inconsistency"}));
					return errors;
                }*/
                
                List<Date> effectiveDatesArr = new ArrayList<Date>();
                List<double[]> unitValuesArr = new ArrayList<double[]>();
                List<GenericException> interrors = checkDates(startDate, endDate, fundIds,
                    history, effectiveDatesArr, unitValuesArr);

                if (interrors != null)
                    errors.addAll(interrors);

                // return if there are any errors
                if (errors.size() > 0) return errors;

                ChartDataBean chartBean = (ChartDataBean) request.getSession(false).getAttribute(BDConstants.CHART_DATA_BEAN);
                if (chartBean == null) {
                    chartBean = new ChartDataBean();
                    request.getSession(false).setAttribute(BDConstants.CHART_DATA_BEAN, chartBean);
                }

                chartBean.setValid(new Boolean(false));
                chartBean.setUnitValues(unitValuesArr);
                chartBean.setEffectiveDates(effectiveDatesArr);
                chartBean.setFundIds(fundIds);
                chartBean.setFundNames(fundNames);
                /*Map<String, String> footnotes = new HashMap<String, String>();
                for (Iterator it = fundFootnotes.iterator(); it.hasNext();) {
                    String[] notes = (String[]) it.next();
                    for (int i = 0; i < notes.length; i++) {
                        footnotes.put(notes[i], notes[i]);
                    }
                }*/
                chartBean.setFundFootnotes(allSymbolsArray);
                
                //fundFootnotes.add(new HashSet(Arrays.asList(allSymbolsArray)).toArray(new String[0]));
                
                chartBean.setFundFootnotesByFund(fundFootnotes);

                List<Double> pctValues = new ArrayList<Double>();
                if (determinePctValues(fundPctLabels, pctValues))
                    chartBean.setPctValues(pctValues);
                else
                    chartBean.setPctValues(null);

                // set start & end dates
                // the following was moved to the top and renamed because of naming collision
                // SimpleDateFormat f = new SimpleDateFormat("MMMMMM dd, yyyy");
                if (effectiveDatesArr.size() > 0) {
                    chartBean.setStartDate(longFormat.format((Date) effectiveDatesArr.get(0)));
                    chartBean.setEndDate(longFormat.format((Date) effectiveDatesArr.get(effectiveDatesArr.size() - 1)));
                }
            }
        } catch (ParseException e) {
        	if (logger.isDebugEnabled()) {
        		logger.debug("ParseException in checkDates:", e);
        	}
          	errors.add(new GenericException(BDErrorCodes.CHART_GENERIC, new Object[]{e.toString()}));
        
        }catch(SystemException e){
        	if (logger.isDebugEnabled()) {
        		logger.debug("Exception in checkDates:", e);
        	}
          	errors.add(new GenericException(BDErrorCodes.CHART_GENERIC, new Object[]{e.toString()}));
        }



        return errors;
    }


    /**
     * Filters array of funds and fund history by fund ids we are interested in.
     *
     *
     */
    @SuppressWarnings("unchecked")
	private static boolean determinePctValues(List<String> pctLabels,
        List<Double> pctValues) {

        //now find all selected funds
        boolean pctExists = false;

        for (Iterator it = pctLabels.iterator(); it.hasNext();) {
            String label = (String) it.next();

            pctExists = true;
            double pct = 0;
            try {
                pct = Double.parseDouble(label);
                pctExists = true;
            }
            catch (NumberFormatException e) {
                //keep it at 0
            }

            pctValues.add(new Double(pct));
        }

        return pctExists;
    }

    /**
     *
     *
     * @return boolean false if there are errors
     */
    private static List<GenericException> checkDates(Date periodStartDate,
        Date periodEndDate,
        String[] fundIds,
        FundUnitValueHistory[] history,
        List<Date> effectiveDates,
        List<double[]> unitValues) {

		if (logger.isDebugEnabled()) {
	        logger.debug("Start checkDates");
		}

    	SimpleDateFormat f = new SimpleDateFormat("MM/yyyy");
        ArrayList<GenericException> errors = new ArrayList<GenericException>();

        // now check for equal dates
        HistoricalUnitValue[][] datesAndUnitValues = new HistoricalUnitValue[history.length][];
        
        if(fundIds.length!=history.length){
        	for(int i=0; i < fundIds.length; i++){
        		boolean ind = false;
        		for(int j=0; j<history.length; j++){
	        		if(fundIds[i].equals(history[j].getFundId())){
	        			ind = true;
	        			break;
	        		}
	        	}
        		if(!ind){
	        		errors.add(new GenericException(BDErrorCodes.CHART_MISSING_UNIT_VALUE_DATA, new Object[]{new Integer(i+1)}));
	                return errors;
        		}
        	}
        }

        for (int i = 0; i < fundIds.length; i++) {
            for (int j = 0; j < history.length; j++) {
                if (fundIds[i].equals(history[j].getFundId())) {
                    datesAndUnitValues[i] = history[j].getHistoricalValues();
                    break;
                }
            }
        }

        for (int i = 0; i < datesAndUnitValues.length; i++) {
            if (datesAndUnitValues[i].length > 0)
            	if (logger.isDebugEnabled()) {
	                logger.debug("1st date: " + datesAndUnitValues[i][0].getDate());
            	}
            else
            	if (logger.isDebugEnabled()) {
	                logger.debug("No dates for fund");
            	}
        }

        // verify that we got unit value information for each of the funds for each of the dates we requested.
        // i.e. we must have the same date range for all of the funds.
        // if not, we throw an exception telling the requestor what went wrong.
        if (datesAndUnitValues.length == 0 || datesAndUnitValues[0].length == 0) {
            //throw new PUBChartingException("There is missing unit value data for selected fund #" + (minPointsIndex+1) + " in the given date range. Please select different dates.");
			if (logger.isDebugEnabled()) {
	            logger.debug("Throwing missingUnitValue data for 1st fund");
			}
            errors.add(new GenericException(BDErrorCodes.CHART_MISSING_UNIT_VALUE_DATA, new Object[]{new Integer(1)}));
            return errors;
        }

        int minPoints = datesAndUnitValues[0].length;
        Date maxStartDate = datesAndUnitValues[0][0].getDate();
        Date minEndDate = datesAndUnitValues[0][minPoints - 1].getDate();

        int minPointsIndex = -1;
        int maxStartDateIndex = -1;
        int minEndDateIndex = -1;
        boolean houstonWeHaveAProblem = false;

		if (logger.isDebugEnabled()) {
	        logger.debug("Going into loop: maxStartDate=" + maxStartDate + " minEndDate=" + minEndDate + " minPoints=" + minPoints);
		}

        for (int i = 0; i < datesAndUnitValues.length; i++) {
            int numPoints = datesAndUnitValues[i].length;

			if (logger.isDebugEnabled()) {
	            logger.debug("Fund (" + i + ") numpoints =" + numPoints);
			}

            if (numPoints < 1) {
                houstonWeHaveAProblem = true;
                minPoints = numPoints;
	            minPointsIndex = i;

				if (logger.isDebugEnabled()) {
	                logger.debug("No points for fund " + i);
				}

                continue;
            }

            Date startDate = datesAndUnitValues[i][0].getDate();
            Date endDate = datesAndUnitValues[i][numPoints - 1].getDate();

			if (logger.isDebugEnabled()) {
	            logger.debug("Start=" + startDate + " end=" + endDate);
			}


            if (numPoints < minPoints) {
                houstonWeHaveAProblem = true;
                minPoints = numPoints;
                minPointsIndex = i;

				if (logger.isDebugEnabled()) {
	                logger.debug("Less points for fund " + i);
				}
            }

            if ((i == 1) && startDate.before(maxStartDate)) {
                houstonWeHaveAProblem = true;
                maxStartDateIndex = 0;
				if (logger.isDebugEnabled()) {
	                logger.debug("(i == 1) && startDate.before(maxStartDate)");
				}
            }
            else if (startDate.after(maxStartDate)) {
                // found fund with start date shifted right
                houstonWeHaveAProblem = true;
                maxStartDate = startDate;
                maxStartDateIndex = i;

				if (logger.isDebugEnabled()) {
					logger.debug("(startDate.after(maxStartDate)");
				}
            }
            if ((i == 1) && endDate.after(minEndDate)) {
                houstonWeHaveAProblem = true;
                minEndDateIndex = 0;
				if (logger.isDebugEnabled()) {
	                logger.debug("(i == 1) && endDate.after(minEndDate)");
				}
            }
            else if (endDate.before(minEndDate)) {
                //found fund with end date shifted left
                houstonWeHaveAProblem = true;
                minEndDate = endDate;
                minEndDateIndex = i;
				if (logger.isDebugEnabled()) {
	                logger.debug("(endDate.before(minEndDate)");
				}
            }
            
        }

        if (houstonWeHaveAProblem) {
            if (maxStartDateIndex > -1) {
				if (logger.isDebugEnabled()) {
	                logger.debug("There is no unit value data available for selected fund #" + (maxStartDateIndex + 1) + " before " + maxStartDate + ". Use this date as the start date.");
				}
                errors.add(new GenericException(BDErrorCodes.CHART_NO_FUND_UNIT_VALUE_BEFORE, new Object[]{new Integer(maxStartDateIndex + 1), f.format(maxStartDate)}));
                return errors;
            }
            else if (minEndDateIndex > -1) {
				if (logger.isDebugEnabled()) {
	                logger.debug("There is no unit value data available for selected fund #" + (minEndDateIndex + 1) + " after " + minEndDate + ". Use this date as the end date.");
				}
                errors.add(new GenericException(BDErrorCodes.CHART_NO_FUND_UNIT_VALUE_AFTER, new Object[]{new Integer(minEndDateIndex + 1), f.format(minEndDate)}));
                return errors;
            }
            else if (minPointsIndex > -1) {
				if (logger.isDebugEnabled()) {
	                logger.debug("There is missing unit value data for selected fund #" + (minPointsIndex + 1) + " in the given date range. Please select different dates.");
				}
                errors.add(new GenericException(BDErrorCodes.CHART_MISSING_UNIT_VALUE_DATA, new Object[]{new Integer(minPointsIndex + 1)}));
                return errors;
            }
            else {
				if (logger.isDebugEnabled()) {
	                logger.debug("Houston, we have a problem! Internal error in getChartUnitValueInfo().");
				}
                errors.add(new GenericException(BDErrorCodes.CHART_HOUSTON_PROBLEM, new Object[]{}));
                return errors;
            }
        }
        else {

            if (!datesRepresentSameMonth(maxStartDate, periodStartDate)) {
                //throw new PUBChartingException("There is no unit value data available for any of the selected funds before" + maxStartDate + ". Use this date as the start date.");
                errors.add(new GenericException(BDErrorCodes.CHART_NO_UNIT_VALUE_DATA_BEFORE, new Object[]{f.format(maxStartDate)}));
                return errors;
            }
            else if (!datesRepresentSameMonth(minEndDate, periodEndDate)) {
                //throw new PUBChartingException("There is no unit value data available for any of the selected funds after " + minEndDate + ". Use this date as the end date.");
                errors.add(new GenericException(BDErrorCodes.CHART_NO_UNIT_VALUE_DATA_AFTER, new Object[]{f.format(minEndDate)}));
                return errors;
            }
        }

        // reorganize the UnitValue information
        for (int i = 0; i < minPoints; i++) {
            effectiveDates.add(datesAndUnitValues[0][i].getDate());
        }

        for (int i = 0; i < datesAndUnitValues.length; i++) {
            double[] values = new double[minPoints];
            for (int j = 0; j < minPoints; j++) {
                values[j] = datesAndUnitValues[i][j].getUnitValue().doubleValue();
            }
            unitValues.add(values);
        }

        return errors;
    }


    /**
     * Returns a boolean indication of whether or not the two dates represent that same month and year.
     *
     * @param d1
     *     The first date.
     *
     * @param d2
     *     The second date.
     *
     * @return
     *     True if the dates represent the same month and year.
     **/

    private static boolean datesRepresentSameMonth(Date d1, Date d2) {

        Calendar c1 = GregorianCalendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = GregorianCalendar.getInstance();
        c2.setTime(d2);
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
            c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH));
    }


    /**
     * Determines id(s) and names of selected funds.
     * @param form CharInputForm form bean as populated by Struts
     * //@param viewFunds array of ViewFund beans representing all available funds
     * @param fundNames List of fund names to be populated
     * @return String[] array of fund ids
     */
    private static String[] getFunds(BDPerformanceChartInputForm form,
        Fund[] funds,
        List<String> fundNames, List<String> fundPcts) {

        HashMap<String, String> idsHash = new HashMap<String, String>();
        ArrayList<FundPct> ids = new ArrayList<FundPct>();

        String fundId = form.getFundSelection1();
        if (fundId != null && fundId.length() > 0 && !fundId.equals(" ") &&
            !idsHash.containsKey(fundId)) {
            idsHash.put(fundId, fundId);
            ids.add(new FundPct(fundId, form.getFundPercentage1()));
        }

        fundId = form.getFundSelection2();
        if (fundId != null && fundId.length() > 0 && !fundId.equals(" ") &&
            !idsHash.containsKey(fundId)) {
            idsHash.put(fundId, fundId);
            ids.add(new FundPct(fundId, form.getFundPercentage2()));
        }

        fundId = form.getFundSelection3();
        if (fundId != null && fundId.length() > 0 && !fundId.equals(" ") &&
            !idsHash.containsKey(fundId)) {
            idsHash.put(fundId, fundId);
            ids.add(new FundPct(fundId, form.getFundPercentage3()));
        }

        fundId = form.getFundSelection4();
        if (fundId != null && fundId.length() > 0 && !fundId.equals(" ") &&
            !idsHash.containsKey(fundId)) {
            idsHash.put(fundId, fundId);
            ids.add(new FundPct(fundId, form.getFundPercentage4()));
        }

        fundId = form.getFundSelection5();
        if (fundId != null && fundId.length() > 0 && !fundId.equals(" ") &&
            !idsHash.containsKey(fundId)) {
            idsHash.put(fundId, fundId);
            ids.add(new FundPct(fundId, form.getFundPercentage5()));
        }

        fundId = form.getFundSelection6();
        if (fundId != null && fundId.length() > 0 && !fundId.equals(" ") &&
            !idsHash.containsKey(fundId)) {
            idsHash.put(fundId, fundId);
            ids.add(new FundPct(fundId, form.getFundPercentage6()));
        }


        List<String> fundIds = new ArrayList<String>();
        Fund fund = null;
        for (Iterator it = ids.iterator(); it.hasNext();) {
            FundPct fpct = (FundPct) it.next();

            if ((fund = findFundNameById(fpct.fundId, funds, fundNames)) != null) {
                fundIds.add(fpct.fundId);
                fundPcts.add(fpct.fundPct);
            }
        }

        return (String[]) fundIds.toArray(new String[0]);
    }

    /**
     * Finds fund by its id.
     *
     * @return boolean true if fund was found, otherwise false
     */
    public static Fund findFundById(String fundId,
        Fund[] funds) {
    	
    	
    	for(int i =0;i<funds.length;i++){
    		if(fundId.equals(funds[i].getFundId())){
    			return funds[i];
    		}
    	}
        
        return null;
    }

    /**
     * Finds fund by its id.
     *
     * @return boolean true if fund was found, otherwise false
     */
    private static Fund findFundNameById(String fundId,
        Fund[] funds,
        List<String> fundNames) {

        Fund fund = findFundById(fundId, funds);

        if (fund != null) {
            fundNames.add(fund.getFundName());
            return fund;
        }

        return null;
    }

	private static boolean isAFundSelected(BDPerformanceChartInputForm inputForm) {
		
		boolean fundSelected = false;
		
		if (inputForm.getFundSelection1().trim().length()>0 ||
			inputForm.getFundSelection2().trim().length()>0 ||
			inputForm.getFundSelection3().trim().length()>0 ||
			inputForm.getFundSelection4().trim().length()>0 ||
			inputForm.getFundSelection5().trim().length()>0 ||
			inputForm.getFundSelection6().trim().length()>0) {
			fundSelected = true;
		} 
		
		return fundSelected;
	}

}

class FundPct {
    String fundId;
    String fundPct;

    public FundPct(String fundId, String fundPct) {
        this.fundId = fundId;
        this.fundPct = fundPct;
    }
}

