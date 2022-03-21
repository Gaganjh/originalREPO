package com.manulife.pension.service.withdrawal.helper;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.service.withdrawal.valueobject.Activity;

/**
 * This class defines the sort order of the activites list. 
 * There is no easy way to do this since we are creating a list
 * based off of 2 tables and mashing all of the data together. 
 * Also, the sort order is arbitrary so it must be explicity defined. 
 * 
 * Whenever a new logical section starts i will add 100 (which is an arbitrary
 * value that hopefully leaves enough room for any future fields)
 * 
 * so its:
 * withdrawal part 1: 1-99
 * money Type vesting and and amount: 100-199
 * withdrawal part 2: 200-299
 * payee:  300-399
 * withdrawal part3 : 400-499;
 * declarations : 500
 * 
 */

public final class ActivitiesSortDef {

	private static ActivitiesSortDef instance;
	private Map<WithdrawalFieldDef, Integer> wMap = new HashMap<WithdrawalFieldDef, Integer>();
	private Map<PayeeFieldDef, Integer> pMap = new HashMap<PayeeFieldDef, Integer>();
	private Map<MoneyTypeFieldDef, Integer> mtMap = new HashMap<MoneyTypeFieldDef, Integer>();
    private static final int WITHDRAWAL_1 = 1;
    private static final int WITHDRAWAL_2 = 200;
    private static final int WITHDRAWAL_3 = 400;
    private static final int MONEY_TYPE_1 = 1;
    private static final int PAYEE_TYPE_1 = 1;
    private static final int PAYEE_OFFSET = 300;
    private static final int NONEY_TYPE_OFFSET = 100;
    private static final int DECLARATION_OFFSET = 500;

    
	
	/**
	 * @return the {@link ActivitiesSortDef} instance
	 */
	public static ActivitiesSortDef getInstance() {
		if (instance == null) {
            instance = new ActivitiesSortDef();
        }
		return instance;
	}
	/**
	 * constructs the sort definition.
	 */
	private ActivitiesSortDef() {
		int i = WITHDRAWAL_1;
		wMap.put(WithdrawalFieldDef.STATE_OF_RESIDENCE, i++);
		wMap.put(WithdrawalFieldDef.DATE_OF_BIRTH, i++);
		wMap.put(WithdrawalFieldDef.EXPIRATION_DATE, i++);
		wMap.put(WithdrawalFieldDef.HARDSHIP_REASON, i++);
//		wMap.put(WithdrawalFieldDef.HARDSHIP_REASON_EXPLANATION, i++);
		wMap.put(WithdrawalFieldDef.IRA_PROVIDER, i++);
		wMap.put(WithdrawalFieldDef.EVENT_DATE, i++);
		wMap.put(WithdrawalFieldDef.FINAL_CONTRIBUTION_DATE, i++);
		wMap.put(WithdrawalFieldDef.OUTSTANDING_LOANS_OPTION, i++);
		wMap.put(WithdrawalFieldDef.IRS_DIST_CODE_FOR_LOANS, i++);
		wMap.put(WithdrawalFieldDef.AMOUNT_TYPE_CODE, i++);
		wMap.put(WithdrawalFieldDef.AMOUNT_VALUE, i++);

		i = WITHDRAWAL_2;
		wMap.put(WithdrawalFieldDef.TPA_FEE_AMOUNT, i++);
		wMap.put(WithdrawalFieldDef.TPA_FEE_TYPE, i++);
		wMap.put(WithdrawalFieldDef.OPTION_FOR_UNVESTED_AMOUNTS, i++);
		wMap.put(WithdrawalFieldDef.FED_TAX_RATE, i++);
		wMap.put(WithdrawalFieldDef.STATE_TAX_RATE, i++);

		i = WITHDRAWAL_3;
		wMap.put(WithdrawalFieldDef.TEN99R_ADDRESS_LINE1, i++);
		wMap.put(WithdrawalFieldDef.TEN99R_ADDRESS_LINE2, i++);
		wMap.put(WithdrawalFieldDef.TEN99R_CITY, i++);
		wMap.put(WithdrawalFieldDef.TEN99R_STATE, i++);
		wMap.put(WithdrawalFieldDef.TEN99R_ZIP, i++);
		wMap.put(WithdrawalFieldDef.TEN99R_COUNTRY, i++);
		wMap.put(WithdrawalFieldDef.TEN99R_US_CITIZEN_IND, i++);		

		i = MONEY_TYPE_1;
		mtMap.put(MoneyTypeFieldDef.MT_VESTING_PERCENT, i++);
		mtMap.put(MoneyTypeFieldDef.MT_WITHDRAWAL_AMOUNT, i++);

		i = PAYEE_TYPE_1;
		pMap.put(PayeeFieldDef.P_ROLLOVER_ACCOUNT_NO, i++);
		pMap.put(PayeeFieldDef.P_ROLLOVER_PLAN_NAME, i++);
		pMap.put(PayeeFieldDef.P_IRS_DIST_CODE, i++);
		pMap.put(PayeeFieldDef.P_PAYMENT_METHOD_CODE, i++);
		pMap.put(PayeeFieldDef.P_BANKACCOUNT_TYPE_CODE, i++);

        pMap.put(PayeeFieldDef.P_ORGANIZATION_NAME, i++);
		pMap.put(PayeeFieldDef.P_LAST_NAME, i++);
		pMap.put(PayeeFieldDef.P_FIRST_NAME, i++);
		pMap.put(PayeeFieldDef.P_LINE1, i++);
		pMap.put(PayeeFieldDef.P_LINE2, i++);
		pMap.put(PayeeFieldDef.P_CITY, i++);
		pMap.put(PayeeFieldDef.P_STATE, i++);
		pMap.put(PayeeFieldDef.P_ZIP, i++);
		pMap.put(PayeeFieldDef.P_COUNTRY, i++);

		pMap.put(PayeeFieldDef.P_BANKNAME, i++);
		pMap.put(PayeeFieldDef.P_BANK_TRANSIT_NUMBER, i++);
		pMap.put(PayeeFieldDef.P_BANK_ACCOUNT_NUMBER, i++);
		pMap.put(PayeeFieldDef.P_CREDIT_PARTY_NAME, i++);

		pMap.put(PayeeFieldDef.P_SEND_CHECK_COURIER, i++);
		pMap.put(PayeeFieldDef.P_COURIER_COMPANY_CODE, i++);
		pMap.put(PayeeFieldDef.P_COURIER_NUMBER, i++);
		pMap.put(PayeeFieldDef.P_MAIL_CHECK_TO_ADDRESS, i++);
		



	}
	/**
     * 
	 * @param def the withdrawal field 
	 * @return the sort number
	 */
	public Integer getSortValue(final WithdrawalFieldDef def) {
		return wMap.get(def);
	}
	/**
	 * @param def the payee field definition
	 * @param payeeNo 1 or 2
	 * @return the sort number
	 */
	public Integer getSortValue(final PayeeFieldDef def, final String payeeNo) {
        Integer payeeNumber = new Integer(payeeNo);
        return PAYEE_OFFSET + pMap.size() * (payeeNumber - 1) + pMap.get(def);
    }
	/**
	 * assures that the sort number for the money type is 
	 * assigned a proper value withing the context of the
	 * overall activity list.
	 * 
	 * 
	 * @param itemNumber the field id
	 * @return the sort number
	 */
	public Integer getMoneyTypeSortValue(final Integer itemNumber) {
		return NONEY_TYPE_OFFSET +  itemNumber;
	}
	/**
	 * @param itemNumber the field id
	 * @return the sort number
	 */
	public Integer getDeclarationSortValue(final int itemNumber) {
		return DECLARATION_OFFSET +  itemNumber;
	}
	
	

}
