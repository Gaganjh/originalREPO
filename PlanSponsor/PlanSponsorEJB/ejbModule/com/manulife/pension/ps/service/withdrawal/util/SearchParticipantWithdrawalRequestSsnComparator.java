/**
 * 
 * 
 * Sep 12, 2006
 */
package com.manulife.pension.ps.service.withdrawal.util;

import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalItem;
import com.manulife.pension.ps.service.withdrawal.valueobject.SearchParticipantWithdrawalRequestItem;

/**
 * @author Harsh Kuthiala
 *
 */
public class SearchParticipantWithdrawalRequestSsnComparator extends SearchParticipantWithdrawalRequestComparator {

   public SearchParticipantWithdrawalRequestSsnComparator() {
       super();
   }
   
   
   public int compare(Object arg0, Object arg1) {
    
        String Ssn0 = null;
        String Ssn1 = null;

        if (arg0 != null) {
            Ssn0 = ((SearchParticipantWithdrawalRequestItem)(arg0)).getSsn();
        }
        if (arg1 != null) {
            Ssn1 = ((SearchParticipantWithdrawalRequestItem)(arg1)).getSsn();
        }
        
        if (Ssn0 == null) {
            Ssn0 = EMPTY_STRING;
        }
        if (Ssn1 == null) {
            Ssn1 = EMPTY_STRING;
        }
        int result = (isAscending() ? 1 : -1) * Ssn0.compareTo(Ssn1);
        
        return result;
        
    }

}
