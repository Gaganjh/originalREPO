package com.manulife.pension.ireports.util.cache;

public class ClockImpl implements Clock {

	public long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}

}
