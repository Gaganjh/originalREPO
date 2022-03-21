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

import javax.servlet.jsp.tagext.TagSupport;


import com.manulife.pension.platform.web.taglib.util.BaseBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * <p>Custom tag to perform default processing in conjunction
 * with the SwitchTag.</p>
 *
 * <p>The DefaultTag checks if the condition on the Switch tag has been
 * matched by a preceeding CaseTag. If not the Body of the CaseTag is evaluated,
 * otherwise it is ignored.</p>
 *
 * <p><strong>See the SwitchTag for an example of using this tag.</strong></p>
 * </br>
 *
 *
 * @author Niall Pemberton
 * @version $Revision: 1.0 $ $Date: 2001/06/23 02:13:00 $
 */

public class DefaultTag extends TagSupport {

    
    /**
     * Process the start of this tag.
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        // Get the parent SwitchTag
	Tag parentTag = getParent();
        if (parentTag == null || !(parentTag instanceof SwitchTag))
          throw new JspException(BaseBundle.getMessage("logic.parent", "SwitchTag"));

        SwitchTag switchTag = (SwitchTag)parentTag;

        // Check if the any of the preceeding CaseTags have matched
        if (switchTag.isDone()) {

          return (SKIP_BODY);

        } else {

          return (EVAL_BODY_INCLUDE);

        }

    }

}