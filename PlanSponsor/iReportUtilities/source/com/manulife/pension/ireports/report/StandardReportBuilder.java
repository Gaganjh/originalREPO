package com.manulife.pension.ireports.report;

import com.manulife.util.pdf.BasePdfReport;
import com.manulife.util.pdf.PdfElement;
import com.manulife.util.pdf.PdfElementContainer;
import com.manulife.util.pdf.PdfLayoutMasterSet;
import com.manulife.util.pdf.PdfPageSequence;

public class StandardReportBuilder extends BasePdfReport {

	private PdfLayoutMasterSet layoutMasterSet;

	public StandardReportBuilder() {
		super();

		layoutMasterSet = createLayoutMasterSet();
		pdfRoot.setFontFamily(ReportFormattingConstants.FRUTIGER_SR);
	}

	public PdfLayoutMasterSet getLayoutMasterSet() {
		return layoutMasterSet;
	}

	public StandardReportBuilder add(PdfPageSequence pageSequence) {
		pdfRoot.add(pageSequence);
		return this;
	}

	public void setLayoutMasterSet(PdfElementContainer container) {
		pdfRoot.add(container);
		PdfElement containerRootElement = container.getContainerRootElement();
		if (containerRootElement instanceof PdfLayoutMasterSet) {
			this.layoutMasterSet = (PdfLayoutMasterSet) containerRootElement;
		} else {
			throw new IllegalArgumentException("root container element is not an instance of PdfLayoutMasterSet. It was [" + containerRootElement.getClass() + "]");
		}
	}

	public void setLayoutMasterSet(PdfLayoutMasterSet layoutMasterSet) {
		pdfRoot.set(layoutMasterSet);
	}

	public StandardReportBuilder addPageSequence(PdfElementContainer pageSequence) {
		pdfRoot.add(pageSequence);
		return this;
	}

}
