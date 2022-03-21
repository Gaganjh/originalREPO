package com.manulife.pension.ps.web.sendservice.util;

/**
 * 
 * @author krishta
 *
 */

public class PlanDataWebUtility {
    
    
    public static boolean isNullOrEmpty(String s){
        boolean isNullorEmpty = false;
        if(s==null || "".equals(s)){
            isNullorEmpty = true; 
        }
        return isNullorEmpty;
    }
    
    public static boolean isNull(Object obj){
        boolean isNull = false;
        if(obj==null){
            isNull = true; 
        }
        return isNull;
    }

    public static boolean isZero(int obj){
        boolean isNull = false;
        if(obj==0){
            isNull = true; 
        }
        return isNull;
    }

}
