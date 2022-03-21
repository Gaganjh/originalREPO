/**
 * 
 */
package com.manulife.pension.ps.web.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Customized String buffer to protect from CSV injection
 * 
 * @author shakhas
 *@
 */
public class ProtectedStringBuffer {

	
	private StringBuffer buff;
	private final Pattern p = Pattern.compile("^(\")*(=|-|@|\\+)(.*)$");

	public ProtectedStringBuffer() {
			this.buff = new StringBuffer();
	}

	public ProtectedStringBuffer(int i) {
			this.buff = new StringBuffer(i);
	}
	
	public ProtectedStringBuffer(String s) {
			this.buff = new StringBuffer(s);
	}
	public ProtectedStringBuffer(CharSequence c) {
			this.buff = new StringBuffer(c);
	}
	

	public ProtectedStringBuffer append(Object obj) {
		if (obj instanceof CharSequence) {
			Matcher m = p.matcher((CharSequence) obj);
			if (m.matches()) {
				buff.append(m.group(1)!=null ? m.group(1):"");
				buff.append(" ");
				buff.append(m.group(2)!=null ? m.group(2):"");
				buff.append(m.group(3)!=null ? m.group(3):"");
				return this;
			}
		}
		buff.append(obj);
		return this;
	}
	
	public String toString(){
		return this.buff.toString();
	}

}
