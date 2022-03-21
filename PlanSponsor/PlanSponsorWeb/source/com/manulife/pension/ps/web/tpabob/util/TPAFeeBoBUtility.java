package com.manulife.pension.ps.web.tpabob.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.fee.valueobject.TPAFeesBobHelper.TPAFeesBobElement;

/**
 * This class is a Helper class for TPA - FEE Block of Business page.
 * 
 * @author Ramakrishna
 * 
 */
public class TPAFeeBoBUtility implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static String appId = Environment.getInstance().getAppId();
	private static final String LINE_BREAK = System.getProperty("line.separator");
	 public static final String CSV_REPORT_NAME = "\"Fees_block_business_rpt_";
	 public static final String CSV_FILE_EXTENSION = ".csv";
	 public static final String TITLE = "TPA Fees Block of Business Report";

	public static byte[] getTPAFeeBoBCSVFileData(List<Integer> tpaIds) throws SystemException {

		StringBuffer buff = new StringBuffer(255);
		//Title
		buff.append(TITLE).append(LINE_BREAK).append(LINE_BREAK);
		buff.append("As of: ").append(new SimpleDateFormat("MMM/dd/yyyy").format(new Date())).append(LINE_BREAK)
									.append(LINE_BREAK).append(LINE_BREAK);
		buff.append(buildCsvHeaders());
		buff.append(LINE_BREAK);
		
		if(tpaIds != null && !tpaIds.isEmpty()){
			// Build TPA FEE BoB data 
			Map<Integer, Map<TPAFeesBobElement, String>> tpaFeeBoBDataMap = FeeServiceDelegate.getInstance(appId).getTpaFeeBoBCsvData(tpaIds);
			
			 for (Map.Entry<Integer, Map<TPAFeesBobElement, String>> tpaData : tpaFeeBoBDataMap.entrySet()){
				 
				 Map<TPAFeesBobElement, String> feeData = tpaData.getValue();
				 
				 for (TPAFeesBobElement field : TPAFeesBobElement.values()) {
						buff.append(feeData.get(field) == null ? "" : feeData
										.get(field)).append(",");
					}
					 buff.append(LINE_BREAK);
			 }
		}
		buff.append(getCsvString(ContentHelper.getContentText(
                ContentConstants.TPA_FEE_BOB_DISCLAIMER,
                ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
		
		return buff.toString().getBytes();
    }
	
	private static String getCsvString(Object s) {

        if (s == null) {
            return "";
        } else {
            return "\"" + s.toString() + "\"";
        }
    }

	private static String buildCsvHeaders() {

		StringBuffer buff = new StringBuffer(255);
		// Build Main header
		for (TPAFeesBobElement header : TPAFeesBobElement.values()) {

			buff.append(header.getColumnName());
			buff.append(",");

		}
		return buff.toString();
	}
	
	public static String getCsvFileName(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy");
		return CSV_REPORT_NAME+dateFormat.format(new Date())+CSV_FILE_EXTENSION;
	}
	
}
