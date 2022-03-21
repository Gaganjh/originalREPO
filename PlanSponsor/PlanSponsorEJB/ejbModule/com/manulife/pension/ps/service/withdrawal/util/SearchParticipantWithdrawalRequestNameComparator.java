/**
 * 
 * 
 * Sep 12, 2006
 */
package com.manulife.pension.ps.service.withdrawal.util;

import com.manulife.pension.ps.service.withdrawal.valueobject.SearchParticipantWithdrawalRequestItem;

/**
 * @author Harsh Kuthiala
 *
 */
public class SearchParticipantWithdrawalRequestNameComparator extends SearchParticipantWithdrawalRequestComparator {

   public SearchParticipantWithdrawalRequestNameComparator() {
       super();
   }
   
   
   public int compare(Object arg0, Object arg1) {
    
        String ParticipantName0 = null;
        String ParticipantName1 = null;

        if (arg0 != null) {
            ParticipantName0 = ((SearchParticipantWithdrawalRequestItem)(arg0)).getParticipantFullName();
        }
        if (arg1 != null) {
            ParticipantName1 = ((SearchParticipantWithdrawalRequestItem)(arg1)).getParticipantFullName();
        }
        
        if (ParticipantName0 == null) {
            ParticipantName0 = EMPTY_STRING;
        }
        if (ParticipantName1 == null) {
            ParticipantName1 = EMPTY_STRING;
        }
        int result = (isAscending() ? 1 : -1) * ParticipantName0.compareTo(ParticipantName1);
        
        return result;
        
    }

}
