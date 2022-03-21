package com.manulife.pension.ps.service.report.plandata.util;


import com.manulife.pension.ps.service.report.plandata.valueobject.TPAPlanDataContract;

/**
 * 
 * Comparator to sort by date
 * 
 * @author Dheepa Poongol
 *
 */
public class TPAPlanDataContractNameComparator extends TPAPlanDataContractComparator {

	public TPAPlanDataContractNameComparator() {
		super();
	}
	
	@Override
	public int compare(TPAPlanDataContract arg0, TPAPlanDataContract arg1) {
		String name0 = arg0.getContractName();
		String name1 = arg1.getContractName();
		return (isAscending() ? 1 : -1) * name0.compareToIgnoreCase(name1);
	}
	
	public int compare(TPAPlanDataContract arg0, TPAPlanDataContract arg1, boolean ascending) {
		String name0 = arg0.getContractName();
		String name1 = arg1.getContractName();
		return name0.compareToIgnoreCase(name1);
	}
}