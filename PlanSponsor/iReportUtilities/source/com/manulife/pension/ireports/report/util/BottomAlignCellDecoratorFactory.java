package com.manulife.pension.ireports.report.util;

import com.manulife.util.pdf.PdfElement;
import com.manulife.util.pdf.PdfElementDecorator;
import com.manulife.util.pdf.PdfElementDecoratorFactory;

public class BottomAlignCellDecoratorFactory implements PdfElementDecoratorFactory {

	public PdfElementDecorator createDecorator(PdfElement element) {
		return new BottomAlignCellDecorator(element);
	}
}
