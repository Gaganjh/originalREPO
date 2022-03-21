package com.manulife.pension.ps.web.plandata.util;

/**
 * Author: Dheepa Poongol
 */

public class TPAPlanDataWebUtility {
    
    public static String isSelected(String value, String variable) {
        if ( value.equals(variable) ) {
            return "selected";
        }
        return "";
    }
    
    public static boolean isNullOrEmpty(String s){
        boolean isNullorEmpty = false;
        if(s==null || "".equals(s) || " ".equals(s)){
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
