package com.manulife.pension.ireports.report.streamingreport;

import java.io.IOException;
import java.io.OutputStream;

public interface StreamingReport {

	public void buildReport();
	/**
	 * Writes the PDF content to the output stream.
	 *
	 * @param out
	 *            The output stream to write to.
	 * @throws IOException
	 */
	public void writeTo(OutputStream out); 
}
