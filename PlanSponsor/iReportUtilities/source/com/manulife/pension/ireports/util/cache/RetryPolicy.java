package com.manulife.pension.ireports.util.cache;

import com.manulife.pension.ireports.util.propertymanager.PropertyManager;



public class RetryPolicy {
	
	private Clock clock;
	private int retryCount;
	private long lastRetryTime;
	private long minimumRetryWaitTimeMillis;
	private int maxRetryCount;
	
	public RetryPolicy() {
		this(new ClockImpl());
	}
	
	RetryPolicy(Clock clock) {
		this.clock = clock;
		retryCount = 0;
		lastRetryTime = clock.getCurrentTimeMillis();
		minimumRetryWaitTimeMillis = PropertyManager.getLong("standardreports.reportDataRepository.retryPolicy.minimumRetryWaitTimeMillis", 600000);
		maxRetryCount = PropertyManager.getInt("standardreports.reportDataRepository.retryPolicy.maxRetryCount", 10);
	}
	
	public boolean canRetry() {
		return isTimeToRetry() && !isExceedMaxRetry();
	}

	public boolean isExceedMaxRetry() {
		return retryCount >= maxRetryCount;
	}

	public void retry() {
		if (canRetry()) {
			retryCount++;
			lastRetryTime = clock.getCurrentTimeMillis();
		}
	}
	
	public int getMaxRetryCount() {
		return maxRetryCount;
	}
	void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}
	public long getMinimumRetryWaitTimeMillis() {
		return minimumRetryWaitTimeMillis;
	}
	void setMinimumRetryWaitTimeMillis(long retryInterval) {
		this.minimumRetryWaitTimeMillis = retryInterval;
	}

	public boolean isTimeToRetry() {
		return clock.getCurrentTimeMillis() - lastRetryTime > minimumRetryWaitTimeMillis;
	}

}
