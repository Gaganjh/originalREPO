package com.manulife.pension.ireports.report.streamingreport.impl;
import com.manulife.pension.ireports.report.ReportFormattingConstants;
import com.manulife.util.pdf.BasePdfReport;
import com.manulife.util.pdf.PdfText;

public class Footnotes {
	public static PdfText createFootnote(BasePdfReport report, String footnoteSymbol) {
		return report.createText(footnoteSymbol)
					.setBaseLineShift(ReportFormattingConstants.BASE_LINE_SHIFT_SUPER)
					.setFontSize(ReportFormattingConstants.FONT_SIZE_FOOTNOTE_SYMBOL);
	}

}
