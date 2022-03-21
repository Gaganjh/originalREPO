/*
 * StartupServlet.java,v 1.1.2.1 2006/08/22 17:28:12 Paul_Glenn Exp
 * StartupServlet.java,v
 * Revision 1.1.2.1  2006/08/22 17:28:12  Paul_Glenn
 * Add startup servlet to load initial configurations.
 *
 */
package com.manulife.pension.ps.web.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.manulife.util.converter.ConverterHelper;



/**
 * StartupServlet is used to initialize anything that we want to load before
 * the rest of the application starts.
 *
 * @author glennpa
 * @version 1.1.2.1 2006/08/22 17:28:12
 */
public class StartupServlet extends HttpServlet {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see javax.servlet.GenericServlet#init()
     */
    public void init() throws ServletException {
        super.init();
        
        // For beanutils Converter registration.
        ConverterHelper.registerConverters();
        
    }


}
