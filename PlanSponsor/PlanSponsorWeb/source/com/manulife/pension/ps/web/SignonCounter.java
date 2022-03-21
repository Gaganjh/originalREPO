package com.manulife.pension.ps.web;


public class SignonCounter {
	
	private static int counter = 0;
	private static SignonCounter instance = null;
	private static String logRecordProducer = null;

	static {
		/**
		 * Create a String that contains the machine id, clone id and web container id.
		 * Used to provide context for reporting on the Performance log records.
		 */
/******************************************************************************************
 * Just comment this stuff out for now because there's no sense logging the counters when they're totally wrong. 
 * It appears that the SessionBinding event can be fired in any clone, not just the one where the user was logged in, 
 * so our counts are all screwed up.

		StringBuffer sBuff = new StringBuffer(CloneName.getCloneName());
		sBuff.append(":");
		sBuff.append(Environment.getInstance().getSiteLocation());
		logRecordProducer = sBuff.toString();
		new Timer(true).scheduleAtFixedRate(new 
			TimerTask() {
				public void run() {
					PerformanceLogRecord.logCounter(counter, logRecordProducer);
				}
			},
		300000L, 300000L);
*******************************************************************************************/
		instance = new SignonCounter();
	}

	// This is a singleton
	private SignonCounter() {
	}

	public static SignonCounter getInstance() {
		return instance;
	}

	public static synchronized void increment() {
		counter++;
	}

	public static synchronized void decrement()	{
		if (counter > 0) counter--;
	}

	// in order not to slow down things I won't synchronize the read.
	public static int getCounter() {
		return counter;
	}
}
