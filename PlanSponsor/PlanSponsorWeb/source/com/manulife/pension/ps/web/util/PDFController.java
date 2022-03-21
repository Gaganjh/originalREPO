package com.manulife.pension.ps.web.util;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.HashMap;
import javax.validation.Valid;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;

/**
 * ContractStatementsAction Action class This class is used to forward the
 * users's request to PDF page
 * 
 * @author Ludmila Stern
 */
@Controller
@RequestMapping(value = "/util")
public class PDFController extends PsController {

	@ModelAttribute("pdfUtility")
	public DynaForm populateForm() {
		return new DynaForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/resources/amortizationScheduleGenerator.jsp");
	}

	public PDFController() {
		super(PDFController.class);
	}

	@RequestMapping(value = "/pdf/", method = { RequestMethod.GET })
	public String doExecute(@Valid @ModelAttribute("pdfUtility") DynaForm actionForm, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		OutputStream out = response.getOutputStream();
		try {
			byte[] pdf = (byte[]) request.getSession(false).getAttribute("pdf");
			response.setContentType(Constants.MIME_TYPE_PDF);
			response.setContentLength(pdf.length);
			out = response.getOutputStream();
			out.write(pdf);
		} finally {
			out.close();
			request.getSession(false).removeAttribute("pdf");
		}
		return null;
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations
	 */

	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
}