package com.manulife.pension.ps.web.ipitool;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolJhChargeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolRecoveryMethodField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolSalesAndServiceChargeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolStatementTypeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolTrusteeChargesField;
import com.manulife.pension.service.fee.util.Constants;
import com.manulife.pension.service.fee.util.Constants.PaymentTypes;
import com.manulife.pension.service.fee.util.Constants.StatementType;
import com.manulife.pension.service.fee.valueobject.AdministrativeExpenseParameter;
import com.manulife.pension.service.fee.valueobject.AdministrativeExpenseParameterMap;

public enum SummarizeProcessTransformer {

	INSTANCE;

	// transform only following: IpiToolRecoveryMethodField,
	// IpiToolJhChargeField, IpiToolSalesAndServiceChargeField
	// use InputFieldSpec.getCalculationParameterKey to convert Map key
	// use InputFieldSpec.getType to convert Map value
	// return EnumMap

	AdministrativeExpenseParameterMap transform(
			Map<IpiToolField<?>, String> mapValue) {

		AdministrativeExpenseParameterMap.Builder aepmBuilder = new AdministrativeExpenseParameterMap.Builder();

		for (AdministrativeExpenseParameter.RecoveryMethod apc : AdministrativeExpenseParameter.RecoveryMethod
				.values()) {
			for (IpiToolRecoveryMethodField ipi : IpiToolRecoveryMethodField
					.values()) {
				if (apc.equals(ipi.getSpec().getCalculationParameterKey())) {
					if (StringUtils.isNotBlank(mapValue.get(ipi))) {
						aepmBuilder.put(apc, PaymentTypes.getFromValue(mapValue
								.get(ipi)));
					} else if (StringUtils.isBlank(mapValue.get(ipi))) {
						aepmBuilder.put(apc, PaymentTypes.EMPTY_SPACE);
					}
				}
			}
		}

		for (AdministrativeExpenseParameter.StatementType apc : AdministrativeExpenseParameter.StatementType
				.values()) {
			for (IpiToolStatementTypeField istf : IpiToolStatementTypeField
					.values()) {
				if (apc.equals(istf.getSpec().getCalculationParameterKey())) {
					if (StringUtils.isNotBlank(mapValue.get(istf))) {
						aepmBuilder.put(apc, StatementType
								.getFromValue(mapValue.get(istf)));
					} else if (StringUtils.isBlank(mapValue.get(istf))) {
						aepmBuilder.put(apc, StatementType.EMPTY_SPACE);
					}

				}
			}
		}

		for (AdministrativeExpenseParameter.Charge apc : AdministrativeExpenseParameter.Charge
				.values()) {
			for (IpiToolJhChargeField ipc : IpiToolJhChargeField.values()) {
				if (apc.equals(ipc.getSpec().getCalculationParameterKey())) {
					if (StringUtils.isNotBlank(mapValue.get(ipc))) {
						aepmBuilder.put(apc, new BigDecimal(mapValue.get(ipc)));
					}
				}
			}
		}

		for (AdministrativeExpenseParameter.Charge apc : AdministrativeExpenseParameter.Charge
				.values()) {
			for (IpiToolSalesAndServiceChargeField ipsc : IpiToolSalesAndServiceChargeField
					.values()) {
				if (apc.equals(ipsc.getSpec().getCalculationParameterKey())) {
					if (StringUtils.isNotBlank(mapValue.get(ipsc))) {
						aepmBuilder
								.put(apc, new BigDecimal(mapValue.get(ipsc)));
					}

				}
			}
		}

		for (AdministrativeExpenseParameter.Charge apc : AdministrativeExpenseParameter.Charge
				.values()) {
			for (IpiToolTrusteeChargesField iptc : IpiToolTrusteeChargesField
					.values()) {
				if (apc.equals(iptc.getSpec().getCalculationParameterKey())) {
					if (StringUtils.isNotBlank(mapValue.get(iptc))) {
						aepmBuilder
								.put(apc, new BigDecimal(mapValue.get(iptc)));
					}

				}
			}
		}

		return aepmBuilder.build();

	}

}
