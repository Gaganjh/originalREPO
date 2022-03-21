package com.manulife.pension.service.loan.event;


/**
 * Factory class that is responsible for giving loan event generator
 */
public class EventFactory {

	private static EventFactory eventFactory;

	/**
	 * provides the cached instance of factory class
	 * @return EventFactory
	 */
	public static EventFactory getInstance() {
		if (eventFactory == null) return new EventFactory();
		return eventFactory;
	}

	/**
	 * returns a LoanEventGenerator that can prepare any loans specific events
	 * @param contractId
	 * @param submissionId
	 * @param eventInitiatorId
	 * @return LoanEventGenerator
	 */
	public LoanEventGenerator getLoanEventGenerator(int contractId, int submissionId, int eventInitiatorId) {
		LoanEventGeneratorImpl loanEventGenerator = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);
		return loanEventGenerator;
	}

}
