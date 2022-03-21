package com.manulife.pension.ps.web.taglib;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.taglib.ChangeHistoryVO.ChangeUserInfo;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.valueobject.InvestmentPolicyStatementVO;
import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO;
import com.manulife.pension.service.employee.valueobject.UserIdType;

public abstract class AbstractChangeHistoryTag  extends TagSupport {

    private static final long serialVersionUID = -3513116366487148464L;

    private static final FastDateFormat SNAPSHOT_CHANGE_DATE_FORMAT = 
        FastDateFormat.getInstance("MM/dd/yyyy 'at' hh:mm:ss a 'ET'");

    private String name;

    private boolean current = true;

    protected static Map<String, String> SourceMap = new HashMap<String, String>();

    private static String PSDescription = "Website";

    private static String PADescription = "Participant Website";

    private static String LPDescription = "Administration";

    protected static String FileSubmission = "Census File";

    public final static String JOHN_HANCOCK_REPRESENTATIVE = "John Hancock Representative";
    
    public final static String SYSTEM = "System";

    public final static String EMPLOYEE = "Employee";

    public final static String PayRollCompanyDesc = "Payroll Company";
    
    public final static String BATCH_SOURCE_CHANNEL = "BA";
    
    public final static String SYSTEM_USER_ID = "1";
    
    public final static String EVENT_APP_USER_ID = "8";
    
    public final static String CONVERSION_USER_ID = "7";
    
    static {
        SourceMap.put("PS", PSDescription);
        SourceMap.put("PA", PADescription);
        SourceMap.put("LP", LPDescription);
    }

    /**
     * The name for the page scope attribute which stores the ChangeHistoryVO
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected abstract ChangeHistoryVO getChangeHistoryVO() throws SystemException;
    
    protected abstract void processList() throws JspException;
    
    public int doEndTag() throws JspException {

        Object value = pageContext.getAttribute(name);

		if (value instanceof List) {
			processList();
		} else if (value == null || value instanceof EmployeeChangeHistoryVO
				|| value instanceof InvestmentPolicyStatementVO) {
			processElement();
		} else {
			throw new JspException("Unknown page context attribute!");
		}

        return super.doEndTag();
    }

    /**
     * Processing just for one element
     * 
     * @throws JspException
     */
    private void processElement() throws JspException {
        try {
        	ChangeHistoryVO value = getChangeHistoryVO();
            if (value == null) {
                pageContext.getOut().print("");
            } else {
                Timestamp ts = current ? value.getCurrentUpdatedTs() : value.getPreviousUpdatedTs();
                if (ts == null) {
                    pageContext.getOut().print("");
                } else {
                    StringBuffer buf = new StringBuffer();
                    buf.append("this.T_WIDTH=320; return escape('");
                    buf.append("Last modified on : ");
                    // Business wants to show ET instead of EST or EDT
                    // This works if the server is in Easter time zone....
                    // It doesn't do time zone calculation
                    buf.append(SNAPSHOT_CHANGE_DATE_FORMAT.format(ts));
                    buf.append("<br>");
                    buf.append("Source: " + getSource(value));
                    buf.append("<br>");
                    buf.append("Last changed by: ");
                    buf.append(StringEscapeUtils.escapeJavaScript(getUserName(value)));
                    buf.append("')");
                    pageContext.getOut().print(buf);
                }
            }
        } catch (IOException e) {
            throw new JspException(e);
        } catch (SystemException e) {
        	  throw new JspException(e);
		}
    }

    protected String getUserNameNoCommas(ChangeHistoryVO value) {
        if (value.getCurrentUser() != null) {
            if (value.getCurrentUser().getFirstName() != null) {
                return value.getCurrentUser().getFirstName();
            }
        }

        return "";
    }

    protected String getSource(ChangeHistoryVO value) {
        String source = current ? value.getSourceChannelCode() : value
                .getPreviousSourceChannelCode();

        if (source != null) {
            String sysDesc = SourceMap.get(source.trim().toUpperCase());
            ChangeUserInfo userInfo = current ? value.getCurrentUser() : value.getPreviousUser();
            
			if (sysDesc == null) {
				if (UserIdType.SYS.equals(userInfo.getUserIdType())
						&& BATCH_SOURCE_CHANNEL.equals(source.trim())) {
					sysDesc = SYSTEM;
				} else {
					sysDesc = FileSubmission;
				}
			}
			
            return sysDesc;
        } else {
            return "";
        }
    }

    protected String getUserName(ChangeHistoryVO value) throws SystemException {

        UserProfile loginUser = SessionHelper.getUserProfile((HttpServletRequest) pageContext
                .getRequest());
        
        ChangeUserInfo userInfo = isCurrent() ? value.getCurrentUser() : value.getPreviousUser();
        
        if (loginUser.isInternalUser()) {
        	if (isSystemUser(userInfo.getUserIdType(), userInfo.getUserId())){
        		return userInfo.getUserId() + ", " + SYSTEM;
        	} else if (isInternalUser(userInfo.getUserIdType())) {
                if (!StringUtils.isEmpty(userInfo.getUserId())) {
                    return userInfo.getUserId() + ", " + JOHN_HANCOCK_REPRESENTATIVE;
                } else {
                    return formatUserName(userInfo.getLastName(),
                            userInfo.getFirstName()) + ", "
                            + JOHN_HANCOCK_REPRESENTATIVE;
                }
            } else {
                if (UserIdType.PAY.equals(userInfo.getUserIdType())) {
                    return PayRollCompanyDesc; 
                } else {
                    return formatUserName(userInfo.getLastName(), userInfo.getFirstName());
                }
            }
        } else {
        	if (isSystemUser(userInfo.getUserIdType(), userInfo.getUserId())){
        		return SYSTEM;
        	} else if (isInternalUser(userInfo.getUserIdType())) {
                return JOHN_HANCOCK_REPRESENTATIVE;
            } else {
                String source = isCurrent() ? value.getSourceChannelCode() : value
                        .getPreviousSourceChannelCode();
                if ("PA".equals(source)) {
                    return EMPLOYEE;
                } else {
                    if (UserIdType.PAY.equals(userInfo.getUserIdType())) {
                        return PayRollCompanyDesc; 
                    } else {
                        return formatUserName(userInfo.getLastName()
                                ,userInfo.getFirstName());
                    }
                }
            }
        }
    }

    protected String formatUserName(String lastName, String firstName) {
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
    
    protected boolean isInternalUser(String userIdType) {
        
        if (UserIdType.CAR.equals(userIdType) || UserIdType.UP_INTERNAL.equals(userIdType) || UserIdType.SYS.equals(userIdType)) {
            return true;
        } else {
            return false;
        }
    }
    
    protected boolean isSystemUser(String userIdType, String userId) {
       
		if (UserIdType.SYS.equals(userIdType)
				&& (SYSTEM_USER_ID.equals(userId) || CONVERSION_USER_ID
						.equals(userId)) || EVENT_APP_USER_ID.equals(userId)) {
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
