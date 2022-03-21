package com.manulife.pension.platform.web.passcode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manulife.pension.exception.SystemException;

public interface AuthenticatedSessionInitialization<LoginVo> {
    
    public final String USERID_KEY = "USERID";
    
    void execute(
            String userName,
            HttpServletRequest request,
            HttpServletResponse response,
            LoginVo loginVo)
    throws SystemException;
    
}
