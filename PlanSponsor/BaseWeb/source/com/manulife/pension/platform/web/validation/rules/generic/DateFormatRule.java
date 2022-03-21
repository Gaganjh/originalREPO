/**
 * 
 * @ author kuthiha
 * Oct 17, 2006
 */
package com.manulife.pension.platform.web.validation.rules.generic;


import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * @author kuthiha
 *
 */
public class DateFormatRule extends ValidationRule {

   
    public DateFormatRule(int errorCode) {
        super(errorCode);
        
    }

    public boolean validate(String[] fieldIds, Collection validationErrors, Object objectToValidate) {
        boolean isValid = true;
        String charString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
        sdf.setLenient(false);
        if (objectToValidate == null) { 
            isValid = false;
        } else if (objectToValidate.toString().trim().length() != 10) { 
            
            isValid = false;
        }else if(isAlphaPresent(objectToValidate.toString().trim())) {
            
            isValid = false;
        }  else {    
                    charString = (String) objectToValidate;
            try { 
                Date date = sdf.parse(charString, new ParsePosition(0));
                if(date == null) {
                    isValid = false;
                 } 
            }catch (Exception e) {
                 isValid = false;
            }

        }
            
        if (!isValid) {
            validationErrors.add(new ValidationError(fieldIds, getErrorCode()));
         }
        
        return isValid;
    }
    
    private boolean isAlphaPresent (String expression) {
        boolean isAlpha = false;
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher m = pattern.matcher(expression);
        isAlpha = m.find();
        return isAlpha;
    }

}
