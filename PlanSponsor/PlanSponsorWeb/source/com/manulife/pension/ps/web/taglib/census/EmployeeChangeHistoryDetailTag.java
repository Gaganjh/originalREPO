package com.manulife.pension.ps.web.taglib.census;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.FastDateFormat;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.taglib.AbstractChangeHistoryTag;
import com.manulife.pension.ps.web.taglib.ChangeHistoryVO;
import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO;
import com.manulife.pension.service.employee.valueobject.UserIdType;
import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO.ChangeUserInfo;

/**
 * The jsp tag to show the Employee Change History detail in a mouseover tooltip.
 * 
 * @author guweigu
 * 
 */
public class EmployeeChangeHistoryDetailTag extends AbstractChangeHistoryTag {
    /**
     * 
     */
    private static final long serialVersionUID = -3513116366487148464L;

    // this is thread safe formatter
    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance(
            "MMM dd, yyyy 'at' HH:mm");

    /**
     * Processing for a list of VOs
     * 
     * @throws JspException
     */
    protected void processList() throws JspException {
        // Test if there is anything to display
        if (pageContext.getAttribute(getName()) != null
                && !((List) pageContext.getAttribute(getName())).isEmpty()) {
            StringBuffer buf = new StringBuffer();
            buf.append("this.T_WIDTH=280; return escape('");

            for (Iterator iter = ((List) pageContext.getAttribute(getName())).iterator(); iter.hasNext();) {
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

    private String getUserNameNoCommas(EmployeeChangeHistoryVO value) {
        if (value.getCurrentUser() != null) {
            if (value.getCurrentUser().getFirstName() != null) {
                return value.getCurrentUser().getFirstName();
            }
        }

        return "";
    }

    private String getSource(EmployeeChangeHistoryVO value) {
        String source = isCurrent() ? value.getSourceChannelCode() : value
                .getPreviousSourceChannelCode();

        if (source != null) {
            String sysDesc = SourceMap.get(source.trim().toUpperCase());
            ChangeUserInfo userInfo = isCurrent() ? value.getCurrentUser() : value.getPreviousUser();
            
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

	@Override
	protected ChangeHistoryVO getChangeHistoryVO() throws SystemException{
		ChangeHistoryVO changeHistoryVO = new ChangeHistoryVO();
		EmployeeChangeHistoryVO employeeChangeHistoryVO = null;
		
		Object value = pageContext.getAttribute(getName());
		
		if (value instanceof EmployeeChangeHistoryVO) {

			employeeChangeHistoryVO = (EmployeeChangeHistoryVO) value;

			changeHistoryVO.setCurrentUpdatedTs(employeeChangeHistoryVO.getCurrentUpdatedTs());
			changeHistoryVO.setPreviousUpdatedTs(employeeChangeHistoryVO.getPreviousUpdatedTs());
			changeHistoryVO.setCurrentUser(new ChangeHistoryVO().new ChangeUserInfo(
							employeeChangeHistoryVO.getCurrentUser()
									.getUserId(), employeeChangeHistoryVO
									.getCurrentUser().getUserIdType(),
							employeeChangeHistoryVO.getCurrentUser()
									.getLastName(), employeeChangeHistoryVO
									.getCurrentUser().getFirstName()));
			
			if (employeeChangeHistoryVO.getPreviousUser() != null){
				changeHistoryVO.setPreviousUser(new ChangeHistoryVO().new ChangeUserInfo(
							employeeChangeHistoryVO.getPreviousUser()
									.getUserId(), employeeChangeHistoryVO
									.getPreviousUser().getUserIdType(),
							employeeChangeHistoryVO.getPreviousUser()
									.getLastName(), employeeChangeHistoryVO
									.getPreviousUser().getFirstName()));
			}
			
			changeHistoryVO.setPreviousSourceChannelCode(employeeChangeHistoryVO.getPreviousSourceChannelCode());
			changeHistoryVO.setSourceChannelCode(employeeChangeHistoryVO.getSourceChannelCode());
		}
		
		return changeHistoryVO;
	}
}
