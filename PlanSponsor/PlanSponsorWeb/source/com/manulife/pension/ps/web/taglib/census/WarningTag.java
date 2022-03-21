package com.manulife.pension.ps.web.taglib.census;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.util.content.MessageProvider;

/**
 * The jsp tag to show a warning message or a list of warnings
 *  in a mouseover tooltip
 * 
 * @author patuadr
 *
 */
public class WarningTag extends TagSupport {

    private static final long serialVersionUID = -3513116366487148464L;

    private static final String BEFORE_TAX_EXCEEDS_PLAN_WARNING_ID = "2551";
    private static final String DEFERRAL_TOTAL_EXCEEDS_PLAN_WARNING_ID= "2552";
    private static final String DESIGNATED_ROTH_EXCEEDS_PLAN_WARNING_ID= "2553";
    private static final String CONTRIB_PLUS_INCREASE_EXCEEDS_PLAN_WARNING_ID= "2555";
    private static String BEFORE_TAX_EXCEEDS_PLAN_WARNING;
    private static String DEFERRAL_TOTAL_EXCEEDS_PLAN_WARNING;
    private static String DESIGNATED_ROTH_EXCEEDS_PLAN_WARNING;
    private static String CONTRIB_PLUS_INCREASE_EXCEEDS_PLAN_WARNING; 
    private static Map<String, String> messageMap = new HashMap<String, String>(); 
    private String name;
   
    static {
        try {
            BEFORE_TAX_EXCEEDS_PLAN_WARNING= 
                MessageProvider.getInstance().getMessage(2551, null);
            DEFERRAL_TOTAL_EXCEEDS_PLAN_WARNING= 
                MessageProvider.getInstance().getMessage(2552, null);
            DESIGNATED_ROTH_EXCEEDS_PLAN_WARNING= 
                MessageProvider.getInstance().getMessage(2553, null);
            CONTRIB_PLUS_INCREASE_EXCEEDS_PLAN_WARNING= 
                MessageProvider.getInstance().getMessage(2555, null);
        } catch (ContentException e) {            
            // It should not happen, but if it does the IDs instead of messages are going to be seen on the screen
            BEFORE_TAX_EXCEEDS_PLAN_WARNING = BEFORE_TAX_EXCEEDS_PLAN_WARNING_ID;
            DEFERRAL_TOTAL_EXCEEDS_PLAN_WARNING = DEFERRAL_TOTAL_EXCEEDS_PLAN_WARNING_ID;
            DESIGNATED_ROTH_EXCEEDS_PLAN_WARNING = DESIGNATED_ROTH_EXCEEDS_PLAN_WARNING_ID;
            CONTRIB_PLUS_INCREASE_EXCEEDS_PLAN_WARNING = CONTRIB_PLUS_INCREASE_EXCEEDS_PLAN_WARNING_ID; 
        } catch (NullPointerException e) {            
            // It should not happen, but if it does The Is instead of messages are going to be seen on the screen
            BEFORE_TAX_EXCEEDS_PLAN_WARNING = BEFORE_TAX_EXCEEDS_PLAN_WARNING_ID;
            DEFERRAL_TOTAL_EXCEEDS_PLAN_WARNING = DEFERRAL_TOTAL_EXCEEDS_PLAN_WARNING_ID;
            DESIGNATED_ROTH_EXCEEDS_PLAN_WARNING = DESIGNATED_ROTH_EXCEEDS_PLAN_WARNING_ID;
            CONTRIB_PLUS_INCREASE_EXCEEDS_PLAN_WARNING = CONTRIB_PLUS_INCREASE_EXCEEDS_PLAN_WARNING_ID; 
        } 
        
        messageMap.put(BEFORE_TAX_EXCEEDS_PLAN_WARNING_ID, BEFORE_TAX_EXCEEDS_PLAN_WARNING);
        messageMap.put(DEFERRAL_TOTAL_EXCEEDS_PLAN_WARNING_ID, DEFERRAL_TOTAL_EXCEEDS_PLAN_WARNING);
        messageMap.put(DESIGNATED_ROTH_EXCEEDS_PLAN_WARNING_ID, DESIGNATED_ROTH_EXCEEDS_PLAN_WARNING);
        messageMap.put(CONTRIB_PLUS_INCREASE_EXCEEDS_PLAN_WARNING_ID, CONTRIB_PLUS_INCREASE_EXCEEDS_PLAN_WARNING);
    }
    
    /**
     * The name for the page scope attribute which stores
     * the EmployeeChangeHistoryVO
     * @return
     */
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    /**
     * Tag expects a String or a Collection of Strings
     */
    public int doEndTag() throws JspException {
        Object value = pageContext.getAttribute(name);
        StringBuffer buf = new StringBuffer();        
        
        try {
            if (value == null) {
                pageContext.getOut().print("");
                return super.doEndTag();
            } else if (value instanceof Collection) { 
                buf.append("this.T_WIDTH=280; return escape('");
                for (Iterator iter = ((Collection)value).iterator(); iter.hasNext();) {
                    String element = (String) iter.next();                   
                    if(element != null) {
                        buf.append(messageMap.get(element.trim()));
                    }
                    buf.append("<br>");
                    if (iter.hasNext()) {
                        buf.append("________________________");
                        buf.append("<br>");
                    }                    
                }                
            } else {
                buf.append("this.T_WIDTH=180; return escape('");
                buf.append(value.toString());
            }
            
            buf.append("')");
            pageContext.getOut().print(buf);

        } catch (IOException e) {
            throw new JspException(e);
        } 
        
        return super.doEndTag();
    }
}
