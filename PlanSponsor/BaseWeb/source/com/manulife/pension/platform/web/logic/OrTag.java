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
import javax.servlet.jsp.tagext.Tag;

import com.manulife.pension.platform.web.taglib.util.BaseBundle;


/**
 * <p>This tag is for use with the IfTag/ElseIfTag/AndTag.
 *
 * <p>This tag MUST be embedded in either an IfTag or an AndTag</p>
 *
 * <p><strong>See the IfTag for an example of using this tag.</strong></p>
 * </br>
 *
 * @author Niall Pemberton
 * @version $Revision: 1.0 $ $Date: 2001/06/23 02:13:00 $
 */
public class OrTag extends IfTag {


    protected IfTag parentTag;

    /**
     * Check the parent "IfTag". If the condition for the parent
     * IfTag is NOT true evaluate the condition for this ElseIfTag and
     * include body content of this tag, otherwise ignore it.
     *
     * The required operator values for the ElseIf condtion are:
     * <ul>
     * <li> "Equal" - variable == value
     * <li> "NotEqual" - variable != value
     * <li> "LessThan" - variable < value
     * <li> "LessEqual" - variable <= value
     * <li> "GreaterThan" - variable > value
     * <li> "GreaterEqual" - variable >= value
     * <li> "Match" - value is substring of variable
     * <li> "NoMatch" - value is NOT substring of variable
     * <li> "Present" - value is present for request
     * <li> "NotPresent" - value is NOT present for request
     * </ul>
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        // Get the parent Tag
	Tag tag = getParent();

        // OrTag must be embedded in either an IfTag, ElseifTag or AndTag
        if (tag == null || !(tag instanceof IfTag) || (tag instanceof OrTag))
          throw new JspException(BaseBundle.getMessage("logic.parent", "IfTag/ElseIf/AndTag"));

        parentTag = (IfTag)tag;

        // Set fact that tags at the same level must be all OrTags
        // If this returns "false" then tags are mixed (i.e. AndTags and OrTags)
        if (!(parentTag.setAndOr(IfTag.OR_CONDITION)))
          throw new JspException(BaseBundle.getMessage("logic.andor"));

        // Determine the condition is true/false for this tag
        condition     = condition();

        return (EVAL_BODY_INCLUDE);

    }

    /**
     * Process the end of this tag.
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

      // if the condition is "true" for this tag (having processed its
      // embedded tags) and the parent condition is "false"
      // then re-set the parent condition to "true"
      if (condition && !(parentTag.isCondition()))
          parentTag.setCondition(true);

      return EVAL_PAGE;

    }

    /**
     * Set this tags condition. This method is used by embedded tags
     * to re-set their parents condition.
     *
     * @param childCondition Sets the condition from an embedded tag
     */
    public void setCondition(boolean childCondition) {

        condition = childCondition;

    }

    /**
     * Release resources after Tag processing has finished.
     */
    public void release() {

	super.release();
        parentTag = null;

    }


}