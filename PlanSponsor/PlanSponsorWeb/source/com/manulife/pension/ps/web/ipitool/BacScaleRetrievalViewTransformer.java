package com.manulife.pension.ps.web.ipitool;

import java.util.ArrayList;
import java.util.List;
import com.manulife.pension.service.fee.valueobject.BasicAssetChargeRate;

enum BacScaleRetrievalViewTransformer {
	INSTANCE;
	List<BasicAssetChargeLine> transform(List<BasicAssetChargeRate> bacScale) {
		List<BasicAssetChargeLine> list = new ArrayList<BasicAssetChargeLine>();
		BasicAssetChargeLine basicAssetChargeLine = null;
		for (BasicAssetChargeRate bacRate : bacScale) {
			basicAssetChargeLine = new BasicAssetChargeLine();
			
			// FIXME formatting needs to be customized to IPI Tool
			basicAssetChargeLine.setBandStart(bacRate
					.getBandStart().toPlainString());
			basicAssetChargeLine.setBandEnd(bacRate
					.getBandEnd().toPlainString());
			basicAssetChargeLine.setCharge(bacRate
					.getRate().toPlainString());
			list.add(basicAssetChargeLine);
		}
		return list;
	}
}
