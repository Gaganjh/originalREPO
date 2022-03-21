package com.manulife.pension.service.withdrawal.valueobject;
/**
 * 
 * @author subraan
 *
 */
public class MultiPayeeTaxes {
	String payee;
	String payeeDesc;
	String nonTaxable = "N";
	String taxable = "N";
	String rothIRA = "N";
	String rothTaxable = "N";
	String rothNonTax = "N";
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getNonTaxable() {
		return nonTaxable;
	}
	public void setNonTaxable(String nonTaxable) {
		this.nonTaxable = nonTaxable;
	}
	public String getTaxable() {
		return taxable;
	}
	public void setTaxable(String taxable) {
		this.taxable = taxable;
	}
	public String getRothIRA() {
		return rothIRA;
	}
	public void setRothIRA(String rothIRA) {
		this.rothIRA = rothIRA;
	}
	public String getRothTaxable() {
		return rothTaxable;
	}
	public void setRothTaxable(String rothTaxable) {
		this.rothTaxable = rothTaxable;
	}
	public String getRothNonTax() {
		return rothNonTax;
	}
	public void setRothNonTax(String rothNonTax) {
		this.rothNonTax = rothNonTax;
	}
	
	
}
