/**
 * 
 */
package com.manulife.pension.ireports.report.streamingreport.impl;

import org.apache.commons.lang.StringUtils;

import com.manulife.util.pdf.BasePdfReport;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfText;

public class TextStyle {
	public static final String WEIGHT_BOLD = "bold";
	public static final String WEIGHT_NORMAL = "normal";
	
	BasePdfReport builder;
	String fontFamily;
	int fontSize;
	String weight;
	boolean wrap = true; 
	
	public TextStyle(BasePdfReport builder, String fontFamily, int fontSize, String weight, boolean wrap) {
		super();
		this.builder = builder;
		this.fontFamily = fontFamily;
		this.fontSize = fontSize;
		this.weight = weight;
		this.wrap = wrap;
	}

	public TextStyle(BasePdfReport builder, String fontFamily, int fontSize) {
		super();
		this.builder = builder;
		this.fontFamily = fontFamily;
		this.fontSize = fontSize;
	}
	
	public PdfParagraph createParagraph(String inputText) {
		PdfParagraph result = builder.createParagraph(inputText);
		
		return apply(result);
	}
	
	public PdfParagraph createParagraph(BasePdfReport builder, String inputText) {
		PdfParagraph result = builder.createParagraph(inputText);
		
		return apply(result);
	}

	public PdfText createText(String inputText) {
		PdfText result = builder.createText(inputText);
		
		return apply(result);
	}

	public PdfParagraph apply(PdfParagraph result) {
		if (StringUtils.isNotEmpty(fontFamily)) {
			result.setFontFamily(fontFamily);
		}
		if (fontSize > 0) {
			result.setFontSize(fontSize);
		}
		if (weight != null) {
			result.setFontWeight(weight);
		}
		if (wrap) {
			result.setWrapOption("wrap");
		}
		return result;
	}
	
	public PdfText apply(PdfText result) {
		if (StringUtils.isNotEmpty(fontFamily)) {
			result.setFontFamily(fontFamily);
		}
		if (fontSize > 0) {
			result.setFontSize(fontSize);
		}
		if (weight != null) {
			result.setFontWeight(weight);
		}
		return result;
	}
}