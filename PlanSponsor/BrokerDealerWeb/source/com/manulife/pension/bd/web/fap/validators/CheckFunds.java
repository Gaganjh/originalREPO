package com.manulife.pension.bd.web.fap.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.fap.BDPerformanceChartInputForm;
import com.manulife.pension.platform.web.util.DataValidationHelper;
import com.manulife.pension.service.account.entity.FundOffering;
import com.manulife.pension.service.fund.valueobject.Fund;
import com.manulife.pension.util.content.GenericException;

public class CheckFunds {

	private static int MAX_FUNDS = 6;
	private static int MIN_VALUE = 0;
	private static int MAX_VALUE = 99;
	private static String INDEX = "INDEX";

	public static Collection<GenericException> checkFunds(Object formBean, HttpServletRequest request) {

		Collection<GenericException> errors = new ArrayList<GenericException>();

    	BDPerformanceChartInputForm form = (BDPerformanceChartInputForm) formBean;
    	//get percentages
		String valueFundPct6 = form.getFundPercentage6();
		String valueFundPct1 = form.getFundPercentage1();
		String valueFundPct2 = form.getFundPercentage2();
		String valueFundPct3 = form.getFundPercentage3();
		String valueFundPct4 = form.getFundPercentage4();
		String valueFundPct5 = form.getFundPercentage5();
		//get selectd funds
		String valueFundSel1 = form.getFundSelection1();
		String valueFundSel2 = form.getFundSelection2();
		String valueFundSel3 = form.getFundSelection3();
		String valueFundSel4 = form.getFundSelection4();
		String valueFundSel5 = form.getFundSelection5();
		String valueFundSel6 = form.getFundSelection6();


		//ensure the first fund selection is not blank
		if (DataValidationHelper.isBlankOrNull(valueFundSel1)) {
			errors.add( new GenericException(BDErrorCodes.CHART_REQUIRED_FUND_SELECTION1));
		}

		//ensure that all entered percentages are whole numbers
  		if ((!DataValidationHelper.isBlankOrNull(valueFundPct1) && !DataValidationHelper.isInt(valueFundPct1)) ||
 	 		(!DataValidationHelper.isBlankOrNull(valueFundPct2) && !DataValidationHelper.isInt(valueFundPct2)) ||
  	 		(!DataValidationHelper.isBlankOrNull(valueFundPct3) && !DataValidationHelper.isInt(valueFundPct3)) ||
   			(!DataValidationHelper.isBlankOrNull(valueFundPct4) && !DataValidationHelper.isInt(valueFundPct4)) ||
   			(!DataValidationHelper.isBlankOrNull(valueFundPct5) && !DataValidationHelper.isInt(valueFundPct5)) ||
   			(!DataValidationHelper.isBlankOrNull(valueFundPct6) && !DataValidationHelper.isInt(valueFundPct6))) {
			errors.add( new GenericException(BDErrorCodes.CHART_INTEGER_FUND_PERCENTAGE));
			//errors.add( new GenericException(BDErrorCodes.CHART_RANGE_FUND_PERCENTAGE));
			return errors;
   		}

		int pct1 = Integer.valueOf(DataValidationHelper.isBlankOrNull(valueFundPct1)?"0":valueFundPct1).intValue();
		int pct2 = Integer.valueOf(DataValidationHelper.isBlankOrNull(valueFundPct2)?"0":valueFundPct2).intValue();
		int pct3 = Integer.valueOf(DataValidationHelper.isBlankOrNull(valueFundPct3)?"0":valueFundPct3).intValue();
		int pct4 = Integer.valueOf(DataValidationHelper.isBlankOrNull(valueFundPct4)?"0":valueFundPct4).intValue();
		int pct5 = Integer.valueOf(DataValidationHelper.isBlankOrNull(valueFundPct5)?"0":valueFundPct5).intValue();
		int pct6 = Integer.valueOf(DataValidationHelper.isBlankOrNull(valueFundPct6)?"0":valueFundPct6).intValue();

		//ensure that all entered percentages fall in a range
 		if ((DataValidationHelper.isInRange(pct1,MIN_VALUE,MAX_VALUE)) &&
			(DataValidationHelper.isInRange(pct2,MIN_VALUE,MAX_VALUE)) &&
   			(DataValidationHelper.isInRange(pct3,MIN_VALUE,MAX_VALUE)) &&
  			(DataValidationHelper.isInRange(pct4,MIN_VALUE,MAX_VALUE)) &&
  			(DataValidationHelper.isInRange(pct5,MIN_VALUE,MAX_VALUE)) &&
  			(DataValidationHelper.isInRange(pct6,MIN_VALUE,MAX_VALUE)) &&
   			(DataValidationHelper.isInRange(pct3,MIN_VALUE,MAX_VALUE))) {
			//continue
   		} else {
			errors.add( new GenericException(BDErrorCodes.CHART_RANGE_FUND_PERCENTAGE));
   		}

 		Fund[] fundsArray =
            (Fund[]) request.getSession(false)
            .getAttribute(BDConstants.VIEW_FUNDS_ORIGINAL);
        
       
		//check for duplicate funds , assume fundIds are not a,b,c,d,e,f
		HashSet<String> sFunds = new HashSet<String>();
		sFunds.add(DataValidationHelper.isBlankOrNull(valueFundSel1)?"a":valueFundSel1);
		sFunds.add(DataValidationHelper.isBlankOrNull(valueFundSel2)?"b":valueFundSel2);
		sFunds.add(DataValidationHelper.isBlankOrNull(valueFundSel3)?"c":valueFundSel3);
		sFunds.add(DataValidationHelper.isBlankOrNull(valueFundSel4)?"d":valueFundSel4);
		sFunds.add(DataValidationHelper.isBlankOrNull(valueFundSel5)?"e":valueFundSel5);
		sFunds.add(DataValidationHelper.isBlankOrNull(valueFundSel6)?"f":valueFundSel6);

		if (sFunds.size() != MAX_FUNDS){
			errors.add(new GenericException(BDErrorCodes.CHART_CHECKFUNDS_FUND_DUPLICATES));
		}

		if ((!DataValidationHelper.isBlankOrNull(valueFundPct1) && DataValidationHelper.isInt(valueFundPct1)) ||
			(!DataValidationHelper.isBlankOrNull(valueFundPct2) && DataValidationHelper.isInt(valueFundPct2)) ||
			(!DataValidationHelper.isBlankOrNull(valueFundPct3) && DataValidationHelper.isInt(valueFundPct3)) ||
			(!DataValidationHelper.isBlankOrNull(valueFundPct4) && DataValidationHelper.isInt(valueFundPct4)) ||
			(!DataValidationHelper.isBlankOrNull(valueFundPct5) && DataValidationHelper.isInt(valueFundPct5)) ||
			(!DataValidationHelper.isBlankOrNull(valueFundPct6) && DataValidationHelper.isInt(valueFundPct6))
			) {

				//ensure that for every percentage added, there is a fund selected
				if ((!DataValidationHelper.isBlankOrNull(valueFundPct1) && DataValidationHelper.isBlankOrNull(valueFundSel1)) ||
					(!DataValidationHelper.isBlankOrNull(valueFundPct2) && DataValidationHelper.isBlankOrNull(valueFundSel2)) ||
					(!DataValidationHelper.isBlankOrNull(valueFundPct3) && DataValidationHelper.isBlankOrNull(valueFundSel3)) ||
					(!DataValidationHelper.isBlankOrNull(valueFundPct4) && DataValidationHelper.isBlankOrNull(valueFundSel4)) ||
					(!DataValidationHelper.isBlankOrNull(valueFundPct5) && DataValidationHelper.isBlankOrNull(valueFundSel5)) ||
					(!DataValidationHelper.isBlankOrNull(valueFundPct6) && DataValidationHelper.isBlankOrNull(valueFundSel6))
				   ){

					errors.add(new GenericException(BDErrorCodes.CHART_CHECKFUNDS_FUND_MISSING));
				}

				if ((!DataValidationHelper.isBlankOrNull(valueFundPct1) && isIndex(valueFundSel1, fundsArray)) ||
					(!DataValidationHelper.isBlankOrNull(valueFundPct2) && isIndex(valueFundSel2, fundsArray)) ||
					(!DataValidationHelper.isBlankOrNull(valueFundPct3) && isIndex(valueFundSel3, fundsArray)) ||
					(!DataValidationHelper.isBlankOrNull(valueFundPct4) && isIndex(valueFundSel4, fundsArray)) ||
					(!DataValidationHelper.isBlankOrNull(valueFundPct5) && isIndex(valueFundSel5, fundsArray)) ||
					(!DataValidationHelper.isBlankOrNull(valueFundPct6) && isIndex(valueFundSel6, fundsArray))
				   ){
				   	errors.add(new GenericException(BDErrorCodes.CHART_CHECKFUNDS_PCT_ASSIGNED_TO_INDEX));
				}
				//get rid of 100% possibly entered
				if ("100".equals(valueFundPct1) || "0".equals(valueFundPct1)){
					valueFundPct1 = "";
					form.setFundPercentage1("");
				}
				if ("100".equals(valueFundPct2) || "0".equals(valueFundPct2)){
					valueFundPct2 = "";
					form.setFundPercentage2("");
				}
				if ("100".equals(valueFundPct3) || "0".equals(valueFundPct3)){
					valueFundPct3 = "";
					form.setFundPercentage3("");
				}
				if ("100".equals(valueFundPct4)|| "0".equals(valueFundPct4)){
					valueFundPct4 = "";
					form.setFundPercentage4("");
				}
				if ("100".equals(valueFundPct5)|| "0".equals(valueFundPct5)){
					valueFundPct5 = "";
					form.setFundPercentage5("");
				}
				if ("100".equals(valueFundPct6)|| "0".equals(valueFundPct6)){
					valueFundPct6 = "";
					form.setFundPercentage6("");
				}

				int total = Integer.valueOf(DataValidationHelper.isBlankOrNull(valueFundPct1)||!DataValidationHelper.isInt(valueFundPct1)||isIndex(valueFundSel1, fundsArray)?"0":valueFundPct1).intValue()+
   	       				Integer.valueOf(DataValidationHelper.isBlankOrNull(valueFundPct2)||!DataValidationHelper.isInt(valueFundPct2)||isIndex(valueFundSel2, fundsArray)?"0":valueFundPct2).intValue()+
   	       				Integer.valueOf(DataValidationHelper.isBlankOrNull(valueFundPct3)||!DataValidationHelper.isInt(valueFundPct3)||isIndex(valueFundSel3, fundsArray)?"0":valueFundPct3).intValue()+
   	       				Integer.valueOf(DataValidationHelper.isBlankOrNull(valueFundPct4)||!DataValidationHelper.isInt(valueFundPct4)||isIndex(valueFundSel4, fundsArray)?"0":valueFundPct4).intValue()+
   	       				Integer.valueOf(DataValidationHelper.isBlankOrNull(valueFundPct5)||!DataValidationHelper.isInt(valueFundPct5)||isIndex(valueFundSel5, fundsArray)?"0":valueFundPct5).intValue()+
   	       				Integer.valueOf(DataValidationHelper.isBlankOrNull(valueFundPct6)||!DataValidationHelper.isInt(valueFundPct6)||isIndex(valueFundSel6, fundsArray)?"0":valueFundPct6).intValue();

 	      		if (total != 0 && total != 100) {
   	      			errors.add(new GenericException(BDErrorCodes.CHART_CHECKFUNDS_100PCT));
   	       		}
  	       	}
		return errors;
	}

	private static boolean isIndex(String fundId, Fund[] funds){
		Fund fund = CheckDates.findFundById( fundId, funds );
		return fund == null?false:INDEX.equals(fund.getFundType());
	}
}


