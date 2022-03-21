/*
 * WithdrawalDeclarationsAction.java,v 1.1.2.1 2006/08/02 19:33:19 Paul_Glenn Exp
 * WithdrawalDeclarationsAction.java,v
 * Revision 1.1.2.1  2006/08/02 19:33:19  Paul_Glenn
 * Initial.
 *
 */
package com.manulife.pension.ps.web.withdrawal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.DynaForm;

/**
 * WithdrawalDeclarationsAction is the action class for the withdrawal
 * declarations page.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1.2.1 2006/08/02 19:33:19
 */
public class WithdrawalDeclarationsController extends BaseWithdrawalController {

    /**
     * {@inheritDoc}
     */
		public String doDefault(@Valid @ModelAttribute("dynaForm") DynaForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException { 
	
   

        return  ACTION_FORWARD_DEFAULT;
    }

    /**
     * doBack is called when the page 'back' button is pressed.
     * 
     * @param mapping
     *            The action mapping.
     * @param actionForm
     *            The action form.
     * @param request
     *            The HTTP request.
     * @param response
     *            The HTTP response.
     * @return ActionForward The forward to process.
     * @throws IOException
     *             When an IO problem occurs.
     * @throws ServletException
     *             When an Servlet problem occurs.
     * @throws SystemException
     *             When an generic application problem occurs.
     */
		public String doBack(@Valid @ModelAttribute("dynaForm") DynaForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException { 
	 
   

        return  ACTION_FORWARD_BACK;
    }

    /**
     * doNext is called when the page 'next' button is pressed.
     * 
     * @param mapping
     *            The action mapping.
     * @param actionForm
     *            The action form.
     * @param request
     *            The HTTP request.
     * @param response
     *            The HTTP response.
     * @return ActionForward The forward to process.
     * @throws IOException
     *             When an IO problem occurs.
     * @throws ServletException
     *             When an Servlet problem occurs.
     * @throws SystemException
     *             When an generic application problem occurs.
     */
		public String doNext(@Valid @ModelAttribute("dynaForm") DynaForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
    

        return  ACTION_FORWARD_NEXT;
    }

}
