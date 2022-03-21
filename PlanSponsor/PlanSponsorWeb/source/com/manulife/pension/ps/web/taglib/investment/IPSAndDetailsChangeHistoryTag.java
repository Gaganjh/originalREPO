package com.manulife.pension.ps.web.taglib.investment;

import javax.servlet.jsp.JspException;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.taglib.AbstractChangeHistoryTag;
import com.manulife.pension.ps.web.taglib.ChangeHistoryVO;
import com.manulife.pension.service.contract.valueobject.InvestmentPolicyStatementVO;

public class IPSAndDetailsChangeHistoryTag extends AbstractChangeHistoryTag{

	private static final long serialVersionUID = 1L;

	/**
	 * Populate the change history VO by taking the values from InvestmentPolicyStatementVO
	 */
	@Override
	protected ChangeHistoryVO getChangeHistoryVO() throws SystemException {
		ChangeHistoryVO changeHistoryVO = new ChangeHistoryVO();
		InvestmentPolicyStatementVO investmentPolicyStatementVO = null;
		Object value = pageContext.getAttribute(getName());

		if (value instanceof InvestmentPolicyStatementVO) {
			investmentPolicyStatementVO = (InvestmentPolicyStatementVO) value;

			changeHistoryVO.setCurrentUpdatedTs(investmentPolicyStatementVO
					.getStartTS());
			changeHistoryVO.setPreviousUpdatedTs(investmentPolicyStatementVO
					.getStartTS());
			changeHistoryVO.setCurrentUser(new ChangeHistoryVO().new ChangeUserInfo(
							investmentPolicyStatementVO.getCreatedUserId(),
							investmentPolicyStatementVO.getCreatedUserIdType(),
							investmentPolicyStatementVO.getCreatedUserLastName(), investmentPolicyStatementVO.getCreatedUserFirstName()));
			changeHistoryVO.setPreviousUser(new ChangeHistoryVO().new ChangeUserInfo(
							investmentPolicyStatementVO.getCreatedUserId(),
							investmentPolicyStatementVO.getCreatedUserIdType(),
							investmentPolicyStatementVO.getCreatedUserLastName(), investmentPolicyStatementVO.getCreatedUserFirstName()));
			changeHistoryVO.setPreviousSourceChannelCode(investmentPolicyStatementVO
							.getCreatedSourceChannelCode());
			changeHistoryVO.setSourceChannelCode(investmentPolicyStatementVO
					.getCreatedSourceChannelCode());
		}
		return changeHistoryVO;}

	@Override
	protected void processList() throws JspException {
		// No Implementation required
	}
}
