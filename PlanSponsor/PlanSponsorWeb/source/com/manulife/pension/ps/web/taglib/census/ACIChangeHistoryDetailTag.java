package com.manulife.pension.ps.web.taglib.census;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO;
import com.manulife.pension.service.employee.valueobject.UserIdType;
import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO.ChangeUserInfo;

/**
 * The jsp tag to show the ACI Change History detail in a mouseover tooltip.
 * 
 * @author apatru
 * 
 */
public class ACIChangeHistoryDetailTag extends TagSupport {
    private static final long serialVersionUID = -3513116366487148464L;

    // this is thread safe formatter
    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance(
            "MMM dd, yyyy 'at' HH:mm");

    private static final FastDateFormat SNAPSHOT_CHANGE_DATE_FORMAT = 
        FastDateFormat.getInstance("MM/dd/yyyy 'at' hh:mm:ss a 'ET'");

    private String name;
    private String dataElementName;

    private boolean current = true;
    
    private static String PLAN_SPONSOR_WEBSITE_SINGLE_UPDATE = "PS";
    
    private static String PLAN_SPONSOR_WEBSITE_SERVICE_CHANGE = "PC";
    
    private static String PARTICIPANT_WEBSITE = "PA";
    
    private static String ENROLLMENT_APOLLO = "LP";
    
    private static String ENROLLMENT_APOLLO_OLD_CODE = "SP";
    
    private static String PSDescription = "Website";

    private static String PADescription = "Participant Website";
    
    private static String SDescription = "Enrollment";

    public final static String JOHN_HANCOCK_REPRESENTATIVE = "John Hancock Representative";
    
    public final static String PLAN_SPONSOR_REPRESENTATIVE = "Plan Sponsor Representative";

    public final static String EMPLOYEE = "Employee";
    
    private static String PARTICIPANT = "Participant";
    
    private static String AUTO_ENROLLMENT = "Auto Enrollment";


    /**
     * The name for the page scope attribute which stores the EmployeeChangeHistoryVO
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getDataElementName() {
        return dataElementName;
    }

    public void setDataElementName(String dataElementName) {
        this.dataElementName = dataElementName;
    }

    public int doEndTag() throws JspException {
        Object value = pageContext.getAttribute(name);

        if (value == null || value instanceof EmployeeChangeHistoryVO) {
            processElement();
        } else if (value instanceof List) {
            processList();
        } else {
            throw new JspException("Unknown page context attribute!");
        }

        return super.doEndTag();
    }

    /**
     * Processing for a list of VOs
     * 
     * @throws JspException
     */
    private void processList() throws JspException {
        // Test if there is anything to display
        if (pageContext.getAttribute(name) != null
                && !((List) pageContext.getAttribute(name)).isEmpty()) {
            StringBuffer buf = new StringBuffer();
            buf.append("this.T_WIDTH=280; return escape('");

            for (Iterator iter = ((List) pageContext.getAttribute(name)).iterator(); iter.hasNext();) {
                EmployeeChangeHistoryVO value = (EmployeeChangeHistoryVO) iter.next();

                if (value != null) {
                    buf.append("Last modified on : ");
                    Timestamp ts = value.getCurrentUpdatedTs();
                    if (ts != null) {
                        buf.append(DATE_FORMAT.format(ts));
                    }
                    buf.append("<br>");
                    buf.append("Source: " + getSource(value));
                    buf.append("<br>");
                    buf.append("Last changed by: ");
                    buf.append(StringEscapeUtils.escapeJavaScript(getUserNameNoCommas(value)));
                    buf.append("<br>");
                    if (iter.hasNext()) {
                        buf.append("________________________");
                        buf.append("<br>");
                    }
                }
            }

            buf.append("')");
            try {
                pageContext.getOut().print(buf);
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
    }

    /**
     * Processing just for one element
     * 
     * @throws JspException
     */
    private void processElement() throws JspException {
        EmployeeChangeHistoryVO value = (EmployeeChangeHistoryVO) pageContext.findAttribute(name);
        String dataElementValue = (String) pageContext.findAttribute(dataElementName);
        
        try {
            if (value == null) {
                if("NO_ACTION".equals(dataElementValue)) {
                    StringBuffer buf = new StringBuffer();
                    buf.append("this.T_WIDTH=320; return escape('");
                    buf.append("The participant has not taken action");
                    buf.append("')");
                    pageContext.getOut().print(buf);
                } else {
                    pageContext.getOut().print("");
                }
            } else {
                Timestamp ts = current ? value.getCurrentUpdatedTs() : value.getPreviousUpdatedTs();
                if (ts == null) {
                    pageContext.getOut().print("");
                } else {
                    StringBuffer buf = new StringBuffer();
                    buf.append("this.T_WIDTH=320; return escape('");
                    buf.append(dataElementValue + " changed from: ");
                    buf.append(decode(value.getCurrentValue()));
                    buf.append("<br>");
                    buf.append("Changed on: ");
                    // Business wants to show ET instead of EST or EDT
                    // This works if the server is in Easter time zone....
                    // It doesn't do time zone calculation
                    SNAPSHOT_CHANGE_DATE_FORMAT.format(ts);
                    buf.append(SNAPSHOT_CHANGE_DATE_FORMAT.format(ts));
                    buf.append("<br>");
                    buf.append("Changed by: ");
                    buf.append(StringEscapeUtils.escapeJavaScript(getUserName(value)));                    
                    buf.append("<br>");
                    buf.append("Source of change: " + getSource(value));
                    buf.append("')");
                    pageContext.getOut().print(buf);
                }
            }
        } catch (IOException e) {
            throw new JspException(e);
        }
    }

    
    // don't think we ever want to show the user N Y for the settings. 
    // rather On, Off.
    private String decode(String input) {
    	if ("N".equals(input)) return "Off";
    	if ("Y".equals(input)) return "On";
    	
    	return input; // leave as is.
    }
    
    private String getUserNameNoCommas(EmployeeChangeHistoryVO value) {
        if (value.getCurrentUser() != null) {
            if (value.getCurrentUser().getFirstName() != null) {
                return value.getCurrentUser().getFirstName();
            }
        }

        return "";
    }

    /**
     * Calculates what needs to be displayed based on Appendix B from Deferral page DFS
     * 
     * @param value
     * @return
     */
    private String getSource(EmployeeChangeHistoryVO value) {
        String source = current ? value.getSourceChannelCode() : value
                .getPreviousSourceChannelCode();
        
        if (source != null) {
            if(PARTICIPANT_WEBSITE.equalsIgnoreCase(source)) { 
                return PADescription;
            } if(ENROLLMENT_APOLLO.equalsIgnoreCase(source) ||
                    ENROLLMENT_APOLLO_OLD_CODE.equalsIgnoreCase(source)) {
                return SDescription;
            } else {
                return PSDescription;
            }            
        } else {
            return "";
        }
    }

    /**
     * Calculates what needs to be displayed based on Appendix B from Deferral page DFS
     * 
     * @param value
     * @return
     */
    private String getUserName(EmployeeChangeHistoryVO value) {
        UserProfile loginUser = SessionHelper.getUserProfile((HttpServletRequest) pageContext
                .getRequest());
        ChangeUserInfo userInfo = current ? value.getCurrentUser() : value.getPreviousUser();
        String source = current ? value.getSourceChannelCode() : value
                .getPreviousSourceChannelCode();
        
        // For this it does not matter who is viewing the data
        if(PARTICIPANT_WEBSITE.equalsIgnoreCase(source)) { 
            if (StringUtils.isEmpty(userInfo.getUserId())) {
                // TODO - define behaviour 
                return JOHN_HANCOCK_REPRESENTATIVE;
            } else {
                return formatUserName(userInfo.getLastName(), userInfo.getFirstName());
            }
        } 
        if(ENROLLMENT_APOLLO.equalsIgnoreCase(source) || ENROLLMENT_APOLLO_OLD_CODE.equalsIgnoreCase(source)) {
            return SDescription;
        } 
        
        // Internal user is viewing the data
        if (loginUser.isInternalUser()) {            
            if (isInternalUser(userInfo)) {                
                if(PLAN_SPONSOR_WEBSITE_SINGLE_UPDATE.equalsIgnoreCase(source)) {
                    // Internal User - Single Update
                    if (StringUtils.isEmpty(userInfo.getUserId())) {
                        return JOHN_HANCOCK_REPRESENTATIVE;
                    } else {
                        return JOHN_HANCOCK_REPRESENTATIVE + 
                        "(" + 
                        formatUserName(userInfo.getLastName(),
                                userInfo.getFirstName()) + 
                        ")";
                    }
                } else if(PLAN_SPONSOR_WEBSITE_SERVICE_CHANGE.equalsIgnoreCase(source)) {
                    // Internal User - Service Change
                    return PLAN_SPONSOR_REPRESENTATIVE;
                }
            } else { 
                // External user did the update
                if (StringUtils.isEmpty(userInfo.getUserId())) {
                    // TODO - define behaviour 
                    return JOHN_HANCOCK_REPRESENTATIVE;
                } else {
                    return formatUserName(userInfo.getLastName(), userInfo.getFirstName());
                }
            }
        } else {
            // External user is viewing the data
            if (isInternalUser(userInfo)) {
                if(PLAN_SPONSOR_WEBSITE_SINGLE_UPDATE.equalsIgnoreCase(source)) {
                    return JOHN_HANCOCK_REPRESENTATIVE;
                } else if(PLAN_SPONSOR_WEBSITE_SERVICE_CHANGE.equalsIgnoreCase(source)) {
                    return PLAN_SPONSOR_REPRESENTATIVE;
                }                
            } else {
                if (StringUtils.isEmpty(userInfo.getUserId())) {
                    // TODO - define behaviour 
                    return JOHN_HANCOCK_REPRESENTATIVE;
                } else {
                    return formatUserName(userInfo.getLastName(), userInfo.getFirstName());
                } 
            }
        }
        
        // This should always be un-reacheable
        return "";
    }

    private String formatUserName(String lastName, String firstName) {
        String l = StringUtils.trimToEmpty(lastName);
        String f = StringUtils.trimToEmpty(firstName);
        
        if (StringUtils.isEmpty(l)) {
            return f;
        } else if (StringUtils.isEmpty(f)) {
            return l;
        } else {
            return l + ", " + f;
        }
    }
    
    private boolean isInternalUser(ChangeUserInfo user) {
        String userIdType = user.getUserIdType();
        if (UserIdType.CAR.equals(userIdType) || UserIdType.UP_INTERNAL.equals(userIdType)) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
