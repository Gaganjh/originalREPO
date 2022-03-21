/**
 * 
 */
package com.manulife.pension.ireports.report.util;

import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.pension.platform.utility.CommonConstants;
import com.manulife.util.pdf.PdfGraphic;

public class ImageBuilder {
	public static final String LOGO_IMAGE_NAME = "JH_SIG_PMS300_RGB-hi.jpg";
	
	// image constants
    private static final String IMAGE_PATH = "/images/";
	//private static final String IMAGE_PATH_ISF = IMAGE_PATH + "InvestmentSelectionForm/";
	
	private static final String CHECKBOX_IMAGE_NAME = "plaincheckboxSmall.gif";
	private static final String CHECKBOX_CHECKED_IMAGE_NAME = "checkedPlainCheckboxSmall.gif";
	
	StandardReportBuilder builder;
	
	public ImageBuilder(StandardReportBuilder builder) {
		super();
		this.builder = builder;
	}

	public PdfGraphic buildCheckbox(boolean checked) {
		PdfGraphic checkbox = (PdfGraphic) builder.createGraphic(this.getClass().getResource(
				IMAGE_PATH + ((checked) ? CHECKBOX_CHECKED_IMAGE_NAME : CHECKBOX_IMAGE_NAME)));
		return checkbox;
	}

	public PdfGraphic buildCheckmark() {
		return builder.createGraphic(this.getClass().getResource(IMAGE_PATH + "check.gif"));
	}

	public PdfGraphic buildLogo() {
         return builder.createGraphic(this.getClass().getResource(IMAGE_PATH + CommonConstants.JHRPS_LOGO_FILE))
         .setContentHeight("70px").setContentWidth("170px");
	}
}