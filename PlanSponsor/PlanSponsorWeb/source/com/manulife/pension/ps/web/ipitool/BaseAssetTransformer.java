package com.manulife.pension.ps.web.ipitool;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.service.fee.valueobject.BasicAssetChargeRate;
import com.manulife.pension.service.fee.valueobject.BasicAssetChargeRate.ScheduleBuilder;

enum BaseAssetTransformer {
	INSTANCE;
	List<BasicAssetChargeRate> transform(List<BasicAssetChargeLine> bacScale) {
		ScheduleBuilder builder = new BasicAssetChargeRate.ScheduleBuilder();
		for (BasicAssetChargeLine bacRate : bacScale) {
			builder = builder.addBand(
					new BigDecimal(StringUtils.isNotBlank(bacRate.getBandStart()) ? bacRate.getBandStart(): Constants.DECIMAL_PATTERN), 
					new BigDecimal(StringUtils.isNotBlank(bacRate.getBandEnd())?bacRate.getBandEnd(): Constants.DECIMAL_PATTERN),
					new BigDecimal(StringUtils.isNotBlank(bacRate.getCharge())? bacRate.getCharge(): Constants.DECIMAL_PATTERN));
		}
		return builder.build();
	}
}
