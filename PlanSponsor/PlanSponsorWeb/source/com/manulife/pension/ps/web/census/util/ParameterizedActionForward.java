package com.manulife.pension.ps.web.census.util;


/**
 * This is to support adding request parameter in the
 * forward URL to make the ActionForward dynamic
 * 
 * @author guweigu
 *
 */
public class ParameterizedActionForward  {
    /**
     * 
     */
    private static final long serialVersionUID = -6103722728167847801L;
    
    private StringBuffer buf;
    public String forward;
    public ParameterizedActionForward(String fixed) {
    	forward=fixed;
    }
    
    public void addParameter(String name, String value) {
        if (buf == null) {
            buf = new StringBuffer();
        } else {
            buf.append("&");
        }
        buf.append(name);
        buf.append("=");
        buf.append(value);
    }

    public String getPath() {
        if (buf == null) {
            return forward;
        } else {
            return forward +"?" + buf.toString();
        }
    }
    
    
}
