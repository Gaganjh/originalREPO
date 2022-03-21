/*
 * $Header:
 * $Revision: 1.0 $
 * $Date: 2001/06/23 02:13:00 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Struts", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.manulife.pension.platform.web.logic;

import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.util.BaseBundle;
import com.manulife.pension.platform.web.taglib.util.ConditionalTagBase;



/**
 * <p>Custom tag to perform switch statement processing. This tag
 * should be used in conjunction with the CaseTag and DefaultTag tags.</p>
 *
 * <p>Each nested CaseTag calls the <code>switch</code> tag to check if
 * the condtition is true for its value and operator. If it is true the
 * Body content is evaluated and all other Case and Default Tags are ignored.
 *
 * <p>This tag is based the existing Struts logic processing</p>
 *
 *
 * <p>The operators (i.e. "op") specified on the CaseTags are optional (equal is assumed
 * if not specified) and can have the following values</p>
 *
 * <ul>
 * <li> "Equal" - variable = value</li>
 * <li> "NotEqual" - variable Not = value</li>
 * <li> "LessThan" - variable < value</li>
 * <li> "LessEqual" - variable <= value</li>
 * <li> "GreaterThan" - variable > value</li>
 * <li> "GreaterEqual" - variable >= value</li>
 * </ul>
 *
 *
 * <p><strong>Example Usage of switch/case/default tags</strong></p>
 *
 * <p><code>
 * &lt;logic:switch name="employeeBean" property="salary"&gt;</br></br>&nbsp;&nbsp;
 *     &lt;logic:case op="LessEqual" value="10000"&gt;</br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        Salary Band 0 - 10,000</br>&nbsp;&nbsp;
 *     &lt;/logic:case&gt;</br></br>&nbsp;&nbsp;
 *     &lt;logic:case op="LessEqual" value="30000"&gt;</br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        Salary Band 10,001 - 30,000</br>&nbsp;&nbsp;
 *     &lt;/logic:case&gt;</br></br>&nbsp;&nbsp;
 *     &lt;logic:case op="LessEqual" value="60000"&gt;</br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        Salary Band 30,001 - 60,000</br>&nbsp;&nbsp;
 *     &lt;/logic:case&gt;</br></br>&nbsp;&nbsp;
 *     &lt;logic:case op="LessEqual" value="100000"&gt;</br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        Salary Band 60,001 - 100,000</br>&nbsp;&nbsp;
 *     &lt;/logic:case&gt;</br></br>&nbsp;&nbsp;
 *     &lt;logic:default&gt;</br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        Salary Band - Loads and loads!!!!</br>&nbsp;&nbsp;
 *     &lt;/logic:default&gt;</br></br>
 * &lt;/logic:switch&gt;</br>
 * </code></p>
 *
 *
 * @author Niall Pemberton
 * @version $Revision: 1.0 $ $Date: 2001/06/23 02:13:00 $
 */

public class SwitchTag extends ConditionalTagBase {

    /**
     * Indicates whether a value from a CaseTag has been matched
     */
    protected boolean done;

    public boolean isDone() {
      return done;
    }

    /**
     * Process the start of this tag.
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

      done = false;

      return (EVAL_BODY_INCLUDE);

    }

    /**
     * <p>Check if the condition is true for the passed value and operator. If
     * no operator is set the operator defaults to "equal".</p>
     *
     * Valid operators are:
     * <ul>
     * <li> "Equal" - variable = value</li>
     * <li> "NotEqual" - variable Not = value</li>
     * <li> "LessThan" - variable < value</li>
     * <li> "LessEqual" - variable <= value</li>
     * <li> "GreaterThan" - variable > value</li>
     * <li> "GreaterEqual" - variable >= value</li>
     * </ul>
     */
    public boolean condition(String op, String value) throws javax.servlet.jsp.JspException {

      if (done)
        return false;

      setValue(value);

      if (op == null || op.equalsIgnoreCase("equal")) {
        done = condition(0, 0);
      }
      else if (op.equalsIgnoreCase("notequal")) {
        done = condition(-1, +1);
      }
      else if (op.equalsIgnoreCase("lessthan")) {
        done = condition(-1, -1);
      }
      else if (op.equalsIgnoreCase("lessequal")) {
        done = condition(-1, 0);
      }
      else if (op.equalsIgnoreCase("greaterthan")) {
        done = condition(+1, +1);
      }
      else if (op.equalsIgnoreCase("greaterequal")) {
        done = condition(+1, 0);
      }
      else {
        throw new JspException(BaseBundle.getMessage("logic.op", op));
      }

      return done;

    }

    /**
     * Method not implemented - throws JspException if called
     *
     * @exception JspException if a JSP exception occurs
     */
    protected boolean condition() throws javax.servlet.jsp.JspException {

        throw new JspException(BaseBundle.getMessage("logic.method", "condition()"));

    }

    /**
     * Release resources after Tag processing has finished.
     */
    public void release() {

	super.release();

	done = false;

    }

}