package com.manulife.pension.platform.web.fap.tabs;

import java.io.Serializable;

public class FundScoreCardMetricsSelection implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean showMorningstarScorecardMetrics = true;
	private boolean showFi360ScorecardMetrics = true;
	private boolean showRpagScorecardMetrics = true;

	public FundScoreCardMetricsSelection(
			boolean showMorningstarScorecardMetrics,
			boolean showFi360ScorecardMetrics,
			boolean showRpagScorecardMetrics) {
		this.showMorningstarScorecardMetrics = showMorningstarScorecardMetrics;
		this.showFi360ScorecardMetrics = showFi360ScorecardMetrics;
		this.showRpagScorecardMetrics = showRpagScorecardMetrics;
	}

	public boolean isShowMorningstarScorecardMetrics() {
		return showMorningstarScorecardMetrics;
	}

	public boolean isShowFi360ScorecardMetrics() {
		return showFi360ScorecardMetrics;
	}


	public boolean isShowRpagScorecardMetrics() {
		return showRpagScorecardMetrics;
	}

}
