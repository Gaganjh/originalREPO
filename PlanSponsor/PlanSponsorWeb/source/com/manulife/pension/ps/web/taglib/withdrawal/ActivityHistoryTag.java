package com.manulife.pension.ps.web.taglib.withdrawal;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import com.manulife.pension.platform.web.taglib.util.TagUtils;

import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.withdrawal.WithdrawalForm;
import com.manulife.pension.service.withdrawal.helper.WithdrawalFieldDef;
import com.manulife.pension.service.withdrawal.valueobject.Activity;

/**
 * @author shinchr
 * 
 */
public class ActivityHistoryTag extends BodyTagSupport {

    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(ActivityHistoryTag.class);

    private String itemNumber;

    private String secondNumber;

    private String secondName;

    public final String getItemNumber() {
        return itemNumber;
    }

    public final void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public final String getSecondName() {
        return secondName;
    }

    public final void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public final String getSecondNumber() {
        return secondNumber;
    }

    public final void setSecondNumber(String secondNumber) {
        this.secondNumber = secondNumber;
    }

    @Override
    public int doStartTag() throws JspException {

        try {
            int itemNumber = new Integer(getItemNumber()).intValue();

            boolean moneyType = itemNumber == WithdrawalFieldDef.DYN_MONEY_TYPE;
            boolean declaration = itemNumber == WithdrawalFieldDef.DYN_DECLARATION_TYPE;
            boolean payee = itemNumber == WithdrawalFieldDef.DYN_PAYEE_TYPE;
            boolean withdrawal = !moneyType && !declaration && !payee;
            boolean showArrow = false;
            WithdrawalForm form = (WithdrawalForm) TagUtils.getInstance().lookup(
                    pageContext, Constants.WITHDRAWAL_ACTION_FORM, "session");

            // Check and see if activity history is enabled
            if (BooleanUtils.isFalse(form.getWithdrawalRequestUi().getWithdrawalRequest()
                    .getActivityHistoryEnabled())) {
                return SKIP_BODY;
            }

            if (withdrawal) {
                for (Activity activity : form.getWithdrawalRequestUi().getWithdrawalRequest()
                        .getActivityHistory().getActivities()) {
                    if (activity.getItemNo().intValue() == itemNumber) {
                        showArrow = true;
                        break;
                    }

                }
            } else if (moneyType || payee) {
                int secondNumber = new Integer(getSecondNumber()).intValue();
                for (Activity activity : form.getWithdrawalRequestUi().getWithdrawalRequest()
                        .getActivityHistory().getActivities()) {
                    if (activity.getItemNo().intValue() == itemNumber
                            && activity.getSecondaryNo().intValue() == secondNumber
                            && activity.getSecondaryName().equals(getSecondName())) {
                        showArrow = true;
                        break;
                    }
                }

            } else if (declaration) {
                for (Activity activity : form.getWithdrawalRequestUi().getWithdrawalRequest()
                        .getActivityHistory().getActivities()) {
                    if (activity.getItemNo() == itemNumber
                            && activity.getSecondaryName().equals(getSecondName())) {
                        showArrow = true;
                        break;
                    }
                }
            }
            if (showArrow) {
                StringBuffer html = new StringBuffer()
                        .append("<a href=\"javascript:showActivityHistory()\">")
                        .append(
                                "<img style=\"border:0px none;\" src=\"/assets/unmanaged/images/arrow_green.gif\">")
                        .append("</a>");
                pageContext.getOut().print(html.toString());
            }

        } catch (Exception ex) {
            logger.warn("Got an exception while processing tag", ex);
            throw new JspException(ex.toString());
        }
        return SKIP_BODY;
    }
}
