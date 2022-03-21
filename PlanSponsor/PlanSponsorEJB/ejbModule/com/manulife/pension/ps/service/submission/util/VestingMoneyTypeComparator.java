package com.manulife.pension.ps.service.submission.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.withdrawal.valueobject.Activity;
import com.manulife.pension.submission.SubmissionError;

/**
 * @author Diana Macean
 * 
 */
public class VestingMoneyTypeComparator implements Serializable, Comparator {
    
    private static Map priority = new HashMap();
    static {
        priority.put("ERMAT", new Integer(0));
        priority.put("ERPS", new Integer(1));
        priority.put("ERCON",  new Integer(2));
        priority.put("ERMP", new Integer(3));
        priority.put("EEDEF", new Integer(91));
        priority.put("EEROT", new Integer(92));
    }
    private static final Integer MAX_PRIORITY_ER = new Integer(90);
    private static final Integer MAX_PRIORITY_EE= new Integer(99);
    private static final Integer MAX_PRIORITY = new Integer(100);

    /**
     * Constructor
     */
    public VestingMoneyTypeComparator() {
        super();
    }
    
    /**
     * {@inheritDoc}
     */
    public int compare(Object obj1, Object obj2) {
        
        MoneyTypeVO e1 = null;
        MoneyTypeVO e2 = null;
        
        Integer p1 = Integer.MIN_VALUE;
        Integer p2 = Integer.MIN_VALUE;
        
        if (obj1 != null && obj1 instanceof MoneyTypeVO) {
            e1 = (MoneyTypeVO)obj1;
            p1 = getVestingPriority(e1);
        }
        
        if (obj2 != null && obj2 instanceof MoneyTypeVO) {
            e2 = (MoneyTypeVO)obj2;
            p2 = getVestingPriority(e2);
        }
        
        int result = p1.compareTo(p2);
            
        // if same priority, sort in alphabetical order by contractShortName
        if (result == 0) {
            result = e1.getContractShortName().compareTo(e2.getContractShortName());
        }
        
        return result;
    }
    
    /**
     * Returns the priority of a money type 
     */
    private Integer getVestingPriority(MoneyTypeVO vo) {
        Integer result = (Integer) priority.get(vo.getContractShortName());
        if (result == null) {
            if (vo.getMoneyGroup().equals("ER"))
                result = MAX_PRIORITY_ER;
            else if (vo.getMoneyGroup().equals("EE"))
                result = MAX_PRIORITY_EE;
            else
                result = MAX_PRIORITY;
        }
        return result;
    }

}
