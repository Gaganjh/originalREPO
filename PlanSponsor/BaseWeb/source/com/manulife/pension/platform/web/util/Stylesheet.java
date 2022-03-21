package com.manulife.pension.platform.web.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * @author Charles Chan
 */
public class Stylesheet {

	/*
	 * The CSS stylesheet says: font-family: Arial, Helvetica, sans-serif;
	 * font-size: 11px;
	 * 
	 * We don't want to rely on having Arial, so we picked Sans Serif as the
	 * font. Also, since we're running on the server, the AffineTransform
	 * matrix in FontRenderContext is an identity matrix (meaning 1pt = 1px). So
	 * an 11 pt font is used.
	 */
	private static final Font BODY_FONT = new Font("sansserif", Font.PLAIN, 11);

	/**
	 * Constructor.
	 */
	private Stylesheet() {
		super();
	}

	public static final Font getBodyFont() {
		return BODY_FONT;
	}

	/**
	 * To obtain string width, use getFontMetrics(font).stringWidth. The
	 * FontMetrics object can be cached by the client for future reuse.
	 */
	public static FontMetrics getFontMetrics(Font font) {
		BufferedImage image = new BufferedImage(1024, 1024,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D graphics = image.createGraphics();
		return graphics.getFontMetrics(font);
	}
}