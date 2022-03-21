package com.manulife.pension.ps.service.submission.valueobject;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantContribution;
/**
 * 
 * @author adamthj
 *
 */
public class AddableParticipant extends ParticipantContribution {
	
	private Boolean includedInSubmission = null;


	private static final String DESCENDING_INDICATOR = "DESC";
	
	//The following fields are sortable
	public final static String SORT_IDENTIFIER = "identifier";
	public final static String SORT_NAME = "name";
	public final static String SORT_INCLUDED = "includedInSubmission";
	
	public AddableParticipant() {
	}
	
	public AddableParticipant(String identifier, String name, 
				Boolean includedInSubmission) {
		super(identifier, name, null, null);
		this.includedInSubmission = includedInSubmission;
	}
	
	/**
	 * @return includedInSubmission. if value is null the 
	 * participant is already included in the submission 
	 * (and the value shouldn't change) 
	 */
	public Boolean getIncludedInSubmission() {
		return includedInSubmission;
	}
	
	/**
	 * @return includedInSubmission. if value is null the 
	 * participant is already included in the submission 
	 * (and the value shouldn't change) 
	 */
	public Boolean isIncludedInSubmission() {
		return includedInSubmission;
	}
	
	public void setIncludedInSubmission(Boolean includedInSubmission) {
		this.includedInSubmission = includedInSubmission;
	}
	
	/**
	 * Given an alphanumeric sortable field, this function will check 
	 * if the field in valid.
	 * 
	 * @param sortableField
	 *	The name of the sortable field. The allowable values 
	 * 	are:
	 * 		- IDENTIFIER
	 * 		- NAME
	 * 		- INCLUDED
	 *
	 * @return
	 *     Return true if the sortable field is valid. Or
	 * 	  else return false.
	 *
	 **/
	public static boolean validateSortableField(String sortableField) {
		if(SORT_IDENTIFIER.equals(sortableField) ||
				SORT_NAME.equals(sortableField) ||
				SORT_INCLUDED.equals(sortableField))
			return true;
		else
			return false;
	}
}
