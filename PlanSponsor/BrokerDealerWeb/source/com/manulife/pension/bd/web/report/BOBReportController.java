package com.manulife.pension.bd.web.report;

import static com.manulife.pension.bd.web.BDConstants.BOB_PAGE_FORWARD;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.BobContextUtils;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;

/**
 * This class should be extended by those Action classes which are Contract Specific pages in NBDW.
 * 
 * @author harlomte
 * 
 */
public abstract class BOBReportController extends BDReportController {

    protected BOBReportController(Class clazz) {
        super(clazz);
    }

    /**
     * The preExecute method has been overriden to see if the contractNumber is coming as part of
     * request parameter. If the contract Number is coming as part of request parameter, the
     * BobContext will be setup with contract information of the contract number passed in the
     * request parameter.
     * 
     */
    protected String preExecute( ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException, SystemException {
        super.preExecute( form, request, response);

        BobContextUtils.setUpBobContext(request);
        
        BobContext bob = BDSessionHelper.getBobContext(request) ;
        if (bob == null || bob.getCurrentContract() == null) {
            //return mapping.findForward(BOB_PAGE_FORWARD);
        	return BOB_PAGE_FORWARD;
        }
        
        BobContextUtils.setupProfileId(request);
        if (bob.getCurrentContract().getCompanyCode().equals(
				GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY)) {
			ApplicationHelper.setRequestContentLocation(request,
					Location.NEW_YORK);
		}
        return null;
    }
}
