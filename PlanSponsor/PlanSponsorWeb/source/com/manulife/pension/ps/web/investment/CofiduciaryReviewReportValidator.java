package com.manulife.pension.ps.web.investment;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.contract.ContractDocumentsHelper;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.contract.valueobject.CofidFundRecommendVO;
import com.manulife.pension.util.content.GenericException;
@Component
public class CofiduciaryReviewReportValidator extends ValidatorUtil implements CommonErrorCodes,Validator{

	

private static Logger logger = Logger.getLogger(CofiduciaryReviewReportValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {
		return CofiduciaryReviewScreenPageForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		Collection penErrors = new ArrayList();
		CofiduciaryReviewScreenPageForm cofiduciaryReviewScreenPageForm = (CofiduciaryReviewScreenPageForm) target;
		
		if (cofiduciaryReviewScreenPageForm !=null && cofiduciaryReviewScreenPageForm.getCofidFundRecommendDetails() != null) {

			for (CofidFundRecommendVO cofidFundRecommendVo : cofiduciaryReviewScreenPageForm
					.getCofidFundRecommendDetails()) {
					
				String value = cofidFundRecommendVo.getOptOutIndicator();
				if (!(ContractDocumentsHelper.OPT_OUT_IND_YES.equalsIgnoreCase(value.trim()) ||  ContractDocumentsHelper.OPT_OUT_IND_NO.equalsIgnoreCase(value.trim()))){
					//someother value is coming in request that needs to be cleaned up
						cofidFundRecommendVo.setOptOutIndicator(cofidFundRecommendVo.getOptOutIndicatorTemp());
						GenericException pse = new GenericException(CommonErrorCodes.ERROR_PSVALIDATION_WITH_GUI_FIELD_NAME, new Object[] { "Actions Selected"});
						if(null!=penErrors)
						penErrors.add(pse);

					}

			}

		}

		if (!penErrors.isEmpty()) {
		//TODO need to move to controller	request.removeAttribute(PsBaseAction.ERROR_KEY);
			
		}
	
	
	
	
	
	}	
}
