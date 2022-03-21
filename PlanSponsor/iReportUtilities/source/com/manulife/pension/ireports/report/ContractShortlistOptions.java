package com.manulife.pension.ireports.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ireports.model.ShortlistKey;

/**
 * Tag for adding shortlist/contract description to header/footer of reports.
 * Also has some funny serialization tools for the web layer.
 */
public class ContractShortlistOptions {
	private static final String CONTRACT_USED = "C";
	private static final String SHORTLIST_USED = "S";
	
	private String contractNumber;
	private ShortlistKey shortlistCode; 
	private boolean userModified;

	protected ContractShortlistOptions(String contractNumber, ShortlistKey shortlistKey, boolean userModified) {
		this.contractNumber = contractNumber;
		this.shortlistCode = shortlistKey;
		this.userModified = userModified;
	}

	/**
	 * Constructor from contract
	 * @param contractNumber
	 * @param userModified
	 */
	public ContractShortlistOptions(String contractNumber, boolean userModified) {
		this(contractNumber, null, userModified);
	}
	
	/**
	 * Constructor from Shortlist
	 * @param shortlistKey
	 * @param userModified
	 */
	public ContractShortlistOptions(ShortlistKey shortlistKey, boolean userModified) {
		this(null, shortlistKey, userModified);
	}

	/**
	 * Constructor for nothing - standard report, no modifications
	 */
	public ContractShortlistOptions(boolean isUserModified) {
        this(null, null, isUserModified);
	}
	
	public String getContractNumber() {
		return isContract() ? contractNumber : "";
	}

	/**
	 * Did the user start with a contract or shortlist? 
	 * @return true if the user loaded something from the db
	 */
	public boolean isContractOrShortlistUsed() { return isContract() || isShortlist(); }
	
	protected boolean isUserModifiedContract() { return userModified && isContract(); }
	protected boolean isUserModifiedShortlist() { return userModified && isShortlist(); }
	
	protected boolean isUserModified() {
        return userModified;
    }
	
	protected boolean isShortlist() {
		return shortlistCode != null;
	}
	
	public boolean isContract() {
		return contractNumber != null;
	}
	
    public static boolean isContract(String contractOrShortlistUsed) {
        return StringUtils.isNotBlank(contractOrShortlistUsed)
                && contractOrShortlistUsed.startsWith(CONTRACT_USED + "_");
    }

    public static String parseContractNumber(String contractOrShortlistUsed) {
        return contractOrShortlistUsed.substring(2);
    }

    protected List<String> getFootnotes() {
        List<String> result = new ArrayList<String>();
		/*if (isUserModifiedContract()) {
			result.add(ReportFormattingConstants.FOOTNOTE_MODIFIED_CONTRACT);
        } else if (isUserModified()) { 
            result.add(ReportFormattingConstants.FOOTNOTE_MODIFIED_SHORTLIST);
		}*/
		
        if (isShortlist() && !isUserModified()) {
			if (ShortlistKey.TYPE_VALUE.equals(shortlistCode.getShortlistType())) {
				result.add(ReportFormattingConstants.FOOTNOTE_VALUE_SHORTLIST);
				result.add(ReportFormattingConstants.FOOTNOTE_CLASS_DISCLOSURE_PLAN_ADMIN);
			} else if (ShortlistKey.TYPE_PERFORM.equals(shortlistCode.getShortlistType())) {
				result.add(ReportFormattingConstants.FOOTNOTE_TOP_PERFORM_SHORTLIST);
			} else if (ShortlistKey.TYPE_3YR.equals(shortlistCode.getShortlistType())) {
				result.add(ReportFormattingConstants.FOOTNOTE_3YR_PERFORM_SHORTLIST);
			} else if (ShortlistKey.TYPE_5YR.equals(shortlistCode.getShortlistType())) {
				result.add(ReportFormattingConstants.FOOTNOTE_5YR_PERFORM_SHORTLIST);
			}
		}
		return result;
	}
	
    /**
     * Calculate the required footnote reference(s) for use in super-scripts on a report. E.g.
     * "*2, *3". May be empty.
     * 
     * @return the footnote symbol(s)
     */
    public String getFootnoteReference() {
        List<String> footnotes = getFootnotes();
        if (footnotes.isEmpty()) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        if (footnotes != null) {
            for (String footnoteReference : footnotes) {
                if (result.length() > 0) {
                    result.append(", ");
                }
                result.append(footnoteReference);
            }
        }
        return result.toString();
    }

    /**
     * Add any contract or shortlist footnotes to the footnote table.
     * 
     * @param staticFootnotes String[] of footnote symbols (e.g. "*1", "*15", ...)
     * @return the (possibly) modified footnote table
     */
    public String[] addToFootnotes(String[] staticFootnotes) {
        List<String> result = new ArrayList<String>(Arrays.asList(staticFootnotes));
        result.addAll(getFootnotes());
        return (String[]) result.toArray(new String[result.size()]);
    }

	public static String createContractOrShortlistString(ShortlistKey shortlistKey) {
		return SHORTLIST_USED + "_" + shortlistKey.toString();
	}

	public static String createContractOrShortlistContractString( String contractNumber) {
		return CONTRACT_USED + "_" + contractNumber;
	}

	// The contractOrShortlist string used here is created either in the loadContract or loadShortlist methods in this class. 
    public static ContractShortlistOptions createContractOrShortlistOptions(
			String contractOrShortlist, boolean isUserModified) {
		String contractNumber = null;
		
		if (StringUtils.isEmpty(contractOrShortlist)) {
			return new ContractShortlistOptions(isUserModified);
		} else if (isContract(contractOrShortlist)){
			contractNumber = parseContractNumber(contractOrShortlist);
			return new ContractShortlistOptions(contractNumber, isUserModified);
		} else {
			ShortlistKey shortlistKey = new ShortlistKey(contractOrShortlist.substring(2));
			return new ContractShortlistOptions(shortlistKey, isUserModified);
		}
	}

}
