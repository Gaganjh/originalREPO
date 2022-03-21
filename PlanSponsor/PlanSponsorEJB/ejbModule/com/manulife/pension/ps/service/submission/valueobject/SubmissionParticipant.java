package com.manulife.pension.ps.service.submission.valueobject;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.SortedMap;

import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantContribution;
/**
 * 
 * @author adamthj
 *
 */
public class SubmissionParticipant extends ParticipantContribution {
	
	private int recordNumber;
	private boolean markedForDelete = false;


	private static final String DESCENDING_INDICATOR = "DESC";
	
	//The following fields are sortable
	public final static String SORT_RECORD_NUMBER = "recordNumber";
	public final static String SORT_IDENTIFIER = "identifier";
	public final static String SORT_NAME = "name";
	
	public SubmissionParticipant() {
	}
	
	public SubmissionParticipant(String identifier, String name, int recordNumber, 
				SortedMap moneyTypeAmounts, SortedMap loanAmounts) {
		super(identifier, name, moneyTypeAmounts, loanAmounts);
		this.recordNumber = recordNumber;
	}
	
	/**
	 * @return Returns the recordNumber.
	 */
	public int getRecordNumber() {
		return recordNumber;
	}
	
	/**
	 * Given a numeric sortable field, this function will return 
	 * the value for that sortable field. This function is
	 * used by the sorting routine in InvestmentAllocationReportData.
	 * 
	 * @param sortableField
	 *	The name of the sortable field. The allowable values 
	 * 	are:
	 *    	- PARTICIPANTS_INVESTED_CURRENT
	 * 		- EMPLOYEE_ASSETS
	 * 		- EMPLOYER_ASSETS
	 * 		- TOTAL_ASSETS
	 * 		- PERCENTAGE_OF_TOTAL
	 * 		- MARKETING_SORT_ORDER
	 *
	 * @return
	 *     The value of the field. If the sortableField does not exist
	 * 	  return -99;
	 *
	 **/
	public double getSortableValue(String sortableField) {
		if(SORT_RECORD_NUMBER.equals(sortableField))
			return recordNumber;
		else
			return -99;		//TODO: Throw an exception here!!
	}
	
	/**
	 * Given a numeric sortable field, this function will check 
	 * if the field in valid.
	 * 
	 * @param sortableField
	 *	The name of the sortable field. The allowable values 
	 * 	are:
	 *    	- RECORD_NUMBER
	 * 		- IDENTIFIER
	 * 		- NAME
	 *
	 * @return
	 *     Return true if the sortable field is valid. Or
	 * 	  else return false.
	 *
	 **/
	public static boolean validateSortableField(String sortableField) {
		if(SORT_RECORD_NUMBER.equals(sortableField) ||
				SORT_IDENTIFIER.equals(sortableField) ||
				SORT_NAME.equals(sortableField))
			return true;
		else
			return false;
	}
	
	public BigDecimal getParticipantTotal() {
		BigDecimal total = new BigDecimal(0d);
		
		
		SortedMap moneyTypeAmounts = getMoneyTypeAmounts();
		if (moneyTypeAmounts != null) {
			Iterator moneyIterator = moneyTypeAmounts.values().iterator();
			while(moneyIterator.hasNext()) {
				BigDecimal amount = (BigDecimal) moneyIterator.next();
				total = total.add(amount);
			}
		}
		
		SortedMap loanAmounts = getLoanAmounts();
		if (loanAmounts != null) {
			Iterator loanIterator = loanAmounts.values().iterator();
			while(loanIterator.hasNext()) {
				BigDecimal amount = (BigDecimal) loanIterator.next();
				total = total.add(amount);
			}
		}
		
		return total;
	}
	public boolean isMarkedForDelete() {
		return this.markedForDelete;
	}
	public void setMarkedForDelete(boolean markedForDelete) {
		this.markedForDelete = markedForDelete;
	}
}
