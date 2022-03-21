package com.manulife.pension.platform.web.passcode;

import java.util.EnumMap;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.util.content.GenericException;

public enum PasscodeErrorMessage {
    
    RETRY,
    LOCKED,
    COOLING,
    EXPIRED,
    BLANK_PASSCODE,
    SYSTEM_ERROR_AT_RESEND,
    SYSTEM_ERROR_AT_LOGIN,
    EXPIRED_SMS,
    RETRY_SMS,
    SMS_SWITCH_ON,
    VOICE_SWITCH_ON;
    
    public static class PasscodeErrorMap {
        
        public static class Builder {
            
            private final EnumMap<PasscodeErrorMessage, ContentCodes> map =
                    new EnumMap<PasscodeErrorMessage, ContentCodes>(PasscodeErrorMessage.class);
            
            public Builder add(final PasscodeErrorMessage message, final int usa, final int ny) {
                map.put(message, new ContentCodes(usa, ny));
                return this;
            }
            
            public Builder add(final PasscodeErrorMessage message, final int code) {
                map.put(message,  new ContentCodes(code));
                return this;
            }
            
            public PasscodeErrorMap build() { return new PasscodeErrorMap(map); }
            
        }
        
        private final EnumMap<PasscodeErrorMessage, ContentCodes> map;
        
        private PasscodeErrorMap(EnumMap<PasscodeErrorMessage, ContentCodes> map) { this.map = map; }
        
        public GenericException withArguments(final PasscodeErrorMessage message, final String... args) {
            return map.get(message).withArguments(args);
        }
        
        private static class ContentCodes {
            
            private final int usa, ny;
            private ContentCodes(final int usa, final int ny) { this.usa = usa; this.ny = ny; }
            private ContentCodes(final int code) { this(code, code); }
            GenericException withArguments(final String... args) {
                return
                        args.length <= 0
                        ? new GenericException(getCode())
                        : new GenericException(getCode(), args);
            }
            private int getCode() {
                if (usa == ny) {
                    return usa;
                } else {
                    return CommonConstants.SITEMODE_NY.equals(CommonEnvironment.getInstance().getSiteLocation()) ? ny : usa;
                }
            }
            
        }
    }    
}