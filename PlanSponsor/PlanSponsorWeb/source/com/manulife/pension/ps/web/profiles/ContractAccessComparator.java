package com.manulife.pension.ps.web.profiles;

import java.util.Comparator;

public class ContractAccessComparator implements Comparator {


	private boolean ascending;
	
	public ContractAccessComparator(boolean ascending)
	{
		this.ascending = ascending;
	}
	
	public int compare(Object o1, Object o2)
	{
		ContractAccess u1 = (ContractAccess)o1;
		ContractAccess u2 = (ContractAccess)o2;
		
		if(ascending)
		{
			return u1.getContractNumber().compareTo(u2.getContractNumber());
		}
		else
		{
			return u2.getContractNumber().compareTo(u1.getContractNumber());
		}
	}	
}

