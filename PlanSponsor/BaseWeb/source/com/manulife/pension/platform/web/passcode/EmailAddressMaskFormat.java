package com.manulife.pension.platform.web.passcode;

import org.apache.commons.lang3.StringUtils;

public class EmailAddressMaskFormat {
    
    private final String tooShortDescription;
    public EmailAddressMaskFormat(final String tooShortDescription) { this.tooShortDescription = tooShortDescription; }
    
    public String format(final String emailAddress) {
        
        final String components[] = StringUtils.split(emailAddress, '@');
        final int domainSeparator = components[1].lastIndexOf('.');
        
        final String formattedLocalPart = formatComponent(components[0], 7);
        if (formattedLocalPart == tooShortDescription) {
            return tooShortDescription;
        }
        final String formattedDomain = formatComponent(components[1].substring(0, domainSeparator), 4);
        if (formattedDomain == tooShortDescription) {
            return tooShortDescription;
        }
        
        return formattedLocalPart + '@' + formattedDomain + components[1].substring(domainSeparator);
        
    }
    
    private String formatComponent(final String component, final int minimumLength) {
        
        return
                component.length() < minimumLength
                ? tooShortDescription
                : component.substring(0, 2) + "*****" + component.substring(component.length() - 2);
        
    }
    
}
