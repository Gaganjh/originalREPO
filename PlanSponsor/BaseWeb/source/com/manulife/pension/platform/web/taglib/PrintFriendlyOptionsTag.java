package com.manulife.pension.platform.web.taglib;


/**
 * @author Charles Chan
 */
public class PrintFriendlyOptionsTag extends OptionsTag {

	/**
	 *  
	 */
	public PrintFriendlyOptionsTag() {
		super();
	}

	protected void addOption(StringBuffer sb, String value, String label,
			boolean matched) {
		if (BaseTagHelper.isPrintFriendly(pageContext)) {
			if (matched) {
				sb.append(label).append("\r\n");
			}
		} else {
			super.addOption(sb, value, label, matched);
		}
	}
}
