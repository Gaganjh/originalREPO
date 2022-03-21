package com.manulife.pension.ireports.report.util;

import com.manulife.util.pdf.PdfElement;
import com.manulife.util.pdf.PdfElementDecorator;

public class BottomAlignCellDecorator extends PdfElementDecorator {

	public BottomAlignCellDecorator(PdfElement element) {
		super(element);
	}

	public void decorate(PdfElement element) {
		element.setElementAttribute("display-align", "after");
	}

}
