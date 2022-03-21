package com.manulife.pension.ps.service.report.submission.valueobject;

import java.util.Collection;

/**
 * @author parkand
 */
public interface SubmissionErrorCollection {

	public Collection getErrors();
	public int getNumErrors();
	public int getNumWarnings();
	public int getNumSyntaxErrors();
	public boolean isDiscardFlag();
	public boolean isSyntaxErrorIndicator();
}
