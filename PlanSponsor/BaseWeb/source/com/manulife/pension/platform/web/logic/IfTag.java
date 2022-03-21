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

import java.security.Principal;
import javax.servlet.jsp.JspException;


import com.manulife.pension.platform.web.taglib.util.BaseBundle;
import com.manulife.pension.platform.web.taglib.util.ConditionalTagBase;
import com.manulife.pension.platform.web.taglib.util.TagUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Custom tag to evaluate whether a specified condition is true. This tag
 * must be used in conjunction with an embedded <strong>ThenTag</strong>. If the condition is
 * true the body content of the embedded ThenTag is included, otherwise it
 * is ignored.</p>
 *
 * <p>This tag can also be used optionally with the following embedded tags:</p>
 *
 * <ul>
 * <li><strong>ElseTag</strong> - embedding an ElseTag means that if the condition
 * on the parent IfTag is false then the body content of the ElseTag is
 * included, otherwise it is ignored.</br></li>
 *
 * <li><strong>ElseIfTag</strong> - embedding an ElseIfTag performs the same
 * processing of an ElseTag but additionally further conditions can be
 * specified in the same way as an IfTag.</br></li>
 *
 * <li><strong>AndTag</strong> - AndTags should be specified straight after
 * IfTag and before ThenTag, ElseTag or ElseIfTag tags. They allow additional
 * conditions to be specified which are used in conjunction with the IfTag
 * to determine how the ThenTag or ElseTag (or ElseIfTag) tags body
 * content are processed. AndTag must be embedded either in an IfTag,
 * and ElseIfTag or an OrTag only</br></li>
 *
 * <li><strong>OrTag</strong> - OrTags should be specified straight after
 * IfTag and before ThenTag, ElseTag or ElseIfTag tags. They allow additional
 * conditions to be specified which are used in conjunction with the IfTag
 * to determine how the ThenTag or ElseTag (or ElseIfTag) tags body
 * content are processed. OrTag must be embedded either in an IfTag,
 * and ElseIfTag or an AndTag only</br></li>
 *
 * </ul>
 * <p>This tag is based the existing Struts logic tags and equivalent
 * processing can be achieved by setting the <strong>"op"</strong> value as follows:</p>
 *
 * <ul>
 * <li> "Equal" - variable = value (equivalent to the EqualTag)</li>
 * <li> "NotEqual" - variable != value (equivalent to the NotEqualTag)</li>
 * <li> "LessThan" - variable < value (equivalent to the LessThanTag)</li>
 * <li> "LessEqual" - variable <= value (equivalent to the LessEqualTag)</li>
 * <li> "GreaterThan" - variable > value (equivalent to the GreaterThanTag)</li>
 * <li> "GreaterEqual" - variable >= value (equivalent to the GreaterEqualTag)</li>
 * <li> "Match" - value is substring of variable (equivalent to the MatchTag)</li>
 * <li> "NoMatch" - value is NOT substring of variable (equivalent to the NoMatchTag)</li>
 * <li> "Present" - bean etc. is present for request (equivalent to the PresentTag)</li>
 * <li> "NotPresent" - bean etc. is NOT present for request (equivalent to the NotPresentTag)</li>
 * </ul>
 *
 * <p><strong>Example 1 - Simple</strong></p>
 *
 * <p><code>
 * &lt;logic:if name="calendarBean" property="month" op="equal" value="January" &gt;             </br>
 * &lt;logic:or name="calendarBean" property="year"  op="LessEqual" value="1999"/&gt;            </br>
 * &lt;logic:or name="calendarBean" property="day"   op="equal" value="Monday"/&gt;               </br></br>&nbsp;&nbsp;
 *     &lt;logic:then&gt;                                                                        </br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        At least one of the above conditions is true.                                          </br>&nbsp;&nbsp;
 *     &lt;/logic:then&gt;                                                                       </br></br>&nbsp;&nbsp;
 *     &lt;logic:else&gt;                                                                        </br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        All of Above conditions are false                                                      </br>&nbsp;&nbsp;
 *     &lt;/logic:else&gt;</br></br>
 * &lt;/logic:if&gt;</br></br>
 * </code></p>
 * <p><strong>Example 2 - ElseifTag, AndTag and OrTag</strong></p>
 *
 * <p><code>
 * &lt;logic:if name="personBean" op="present"&gt;                                               </br></br>&nbsp;&nbsp;
 *     &lt;logic:then&gt;                                                                        </br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        No details found for this Person                                                       </br>&nbsp;&nbsp;
 *     &lt;/logic:then&gt;                                                                       </br></br>&nbsp;&nbsp;
 *     &lt;logic:elseif name="personBean" property="age" op="GreaterEqual" value="16"&gt;        </br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        &lt;logic:and name="personBean" property="age" op="LessEqual" value="65"&gt;           </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *           &lt;logic:or name="personBean" property="age" op="LessEqual" value="75"&gt;         </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *              &lt;logic:and name="personBean" property="job" op="equal" value="Doctor"&gt;     </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *              &lt;logic:or name="personBean" property="job" op="equal" value="Dentist"/&gt;    </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *              &lt;logic:or name="personBean" property="job" op="equal" value="Lawyer"/&gt;     </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *              &lt;/logic:and&gt;                                                               </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *           &lt;/logic:or&gt;                                                                   </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *           &lt;logic:or name="personBean" property="job" op="equal" value="President"/&gt;     </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *           &lt;logic:or name="personBean" property="job" op="equal" value="Senator"/&gt;       </br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        &lt;/logic:and&gt;                                                                     </br></br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        &lt;logic:then&gt;                                                                     </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *            This person is of working age - that is between the ages of 16 and 65,             </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *            except for Doctors, Dentists and Lawyers who retire at 55                          </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *            and presidents and senators who can be any age over 16.                            </br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        &lt;/logic:then&gt;                                                                    </br></br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        &lt;logic:else&gt;                                                                     </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *            This person is either too young to work or past retirement age.                    </br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        &lt;/logic:else&gt;                                                                    </br></br>&nbsp;&nbsp;
 *     &lt;/logic:elseif&gt;</br></br>
 * &lt;/logic:if&gt;</br>
 * </code></p>
 *
 * <p><strong>Example 3 - Multiple Elseif tags</strong></p>
 *
 * <p><code>
 * &lt;logic:if name="calendarBean" property="month" op="equal" value = "January" &gt;           </br>
 * &lt;logic:or name="calendarBean" property="month" op="equal" value = "February"/&gt;          </br>
 * &lt;logic:or name="calendarBean" property="month" op="equal" value = "March"/&gt;             </br></br>&nbsp;&nbsp;
 *     &lt;logic:then&gt;                                                                        </br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        This if the first quarter of the year.                                                 </br>&nbsp;&nbsp;
 *     &lt;/logic:then&gt;                                                                       </br></br>&nbsp;&nbsp;
 *     &lt;logic:elseif name="calendarBean" property="month" op="equal" value = "April"&gt;      </br>&nbsp;&nbsp;
 *     &lt;logic:or name="calendarBean" property="month" op="equal" value = "May"/&gt;           </br>&nbsp;&nbsp;
 *     &lt;logic:or name="calendarBean" property="month" op="equal" value = "June"/&gt;          </br></br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        &lt;logic:then&gt;                                                                     </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *           This if the second quarter of the year.                                             </br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        &lt;/logic:then&gt;                                                                    </br></br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        &lt;logic:elseif name="calendarBean" property="month" op="equal" value = "July"&gt;    </br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        &lt;logic:or name="calendarBean" property="month" op="equal" value = "August"/&gt;     </br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        &lt;logic:or name="calendarBean" property="month" op="equal" value = "September"/&gt;  </br></br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *           &lt;logic:then&gt;                                                                  </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *              This if the third quarter of the year.                                           </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *           &lt;/logic:then&gt;                                                                 </br></br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *           &lt;logic:else&gt;                                                                  </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *              This if the fourth quarter of the year.                                          </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *           &lt;/logic:else&gt;                                                                 </br></br>&nbsp;&nbsp;&nbsp;&nbsp;
 *        &lt;/logic:elseif&gt;                                                                  </br></br>&nbsp;&nbsp;
 *     &lt;/logic:elseif&gt;                                                                     </br></br>
 * &lt;/logic:if&gt;</br>
 * </code></p>
 *
 * @author Niall Pemberton
 * @version $Revision: 1.0 $ $Date: 2001/06/23 02:13:00 $
 */
public class IfTag extends ConditionalTagBase {	

    /**
     * <p>The operator used in the condition e.g. "equal"</p>
     * The required operator values for the If condtion are:</br>
     * <ul>
     * <li> "Equal" - variable = value
     * <li> "NotEqual" - variable != value
     * <li> "LessThan" - variable < value
     * <li> "LessEqual" - variable <= value
     * <li> "GreaterThan" - variable > value
     * <li> "GreaterEqual" - variable >= value
     * <li> "Match" - value is substring of variable
     * <li> "NoMatch" - value is NOT substring of variable
     * <li> "Present" - bean etc. is present for request
     * <li> "NotPresent" - bean etc. is NOT present for request
     * </ul>
     */
    protected String op;

    public String getOp() {
      return op;
    }

    public void setOp(String newOp) {
      op = newOp;
    }


    /**
     * The location where the match must exist (<code>start</code> or
     * <code>end</code>), or <code>null</code> for anywhere.
     */
    protected String location = null;

    public String getLocation() {
        return (this.location);
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public static final int AND_CONDITION = 1;
    public static final int OR_CONDITION  = 2;

    /**
     * Whether the tags at the same level are all "AND" or "OR" tags
     */
    protected int andOr = 0;

    public boolean setAndOr(int andOr) {

      // initialise if this hasn't been set before
      if (this.andOr == 0) {
        this.andOr = andOr;
        return true;
      }

      // Check the child tag that calls this method is the
      // same as all other child tags that have called this method
      if (this.andOr == andOr)
        return true;
      else
        return false;

    }

    /**
     * Whether the condition evaluates to <code>true</code> or <code>false</code>.
     */
    protected boolean condition;

    public boolean isCondition() {
      return condition;
    }

    /**
     * Set this tags condition. This method is used by embedded tags
     * to re-set their parents condition.
     *
     * @param childCondition Sets the condition from an embedded tag
     */
    public void setCondition(boolean childCondition) {

      // Process "AND" conditions - set to false if one is false
      if (andOr == AND_CONDITION) {
        if (condition && !childCondition)
          condition = false;
      } else {

      // Process "OR" conditions - set to true if one is true
        if (!condition && childCondition)
          condition = true;
      }

    }

    /**
     * Process the start of this tag.
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

      condition     = condition();

      return (EVAL_BODY_INCLUDE);

    }

    /**
     * Process the required condition specified by the op variable.
     *
     * @exception JspException if a JSP exception has occurred
     */
    protected boolean condition() throws JspException {

      if (op.equalsIgnoreCase("equal")) {
        checkEqualType();
        return condition(0, 0);
      }
      else if (op.equalsIgnoreCase("notequal")) {
        checkEqualType();
        return condition(-1, +1);
      }
      else if (op.equalsIgnoreCase("lessthan")) {
        checkEqualType();
        return condition(-1, -1);
      }
      else if (op.equalsIgnoreCase("lessequal")) {
        checkEqualType();
        return condition(-1, 0);
      }
      else if (op.equalsIgnoreCase("greaterthan")) {
        checkEqualType();
        return condition(+1, +1);
      }
      else if (op.equalsIgnoreCase("greaterequal")) {
        checkEqualType();
        return condition(+1, 0);
      }
      else if (op.equalsIgnoreCase("match")) {
        checkMatchType();
        return conditionMatch(true);
      }
      else if (op.equalsIgnoreCase("notmatch")) {
        checkMatchType();
        return conditionMatch(false);
      }
      else if (op.equalsIgnoreCase("present")) {
        checkPresentType();
        return conditionPresent(true);
      }
      else if (op.equalsIgnoreCase("notpresent")) {
        checkPresentType();
        return conditionPresent(false);
      }
      else {
        throw new JspException(BaseBundle.getMessage("logic.op", op));
      }

    }
    /**
     * <p>Validate the absence/presence of certain attributes for
     * the following operator values
     * (Some attributes must be present and some must NOT be specified).</br>
     * </p>
     * <ul>
     * <li>Equal</li>
     * <li>NotEqual</li>
     * <li>GreaterThan</li>
     * <li>GreaterEqual</li>
     * <li>LessThan</li>
     * <li>LessEqual</li>
     * </ul>
     * </br>
     *
     * @exception JspException if a JSP exception occurs
     */
    protected void checkEqualType() throws JspException {

      if (value == null) {
        throw new JspException(BaseBundle.getMessage("attribute.missing", "value", op));
      }

      if (role != null) {
        throw new JspException(BaseBundle.getMessage("attribute.present", "role", op));
      }

      if (user != null) {
        throw new JspException(BaseBundle.getMessage("attribute.present", "user", op));
      }

      if (location != null) {
        throw new JspException(BaseBundle.getMessage("attribute.present", "location", op));
      }

    }

    /**
     * <p>Validate the absence/presence of certain attributes for
     * the following operator values
     * (Some attributes must be present and some must NOT be specified).</br>
     * </p>
     * <ul>
     * <li>Match</li>
     * <li>NoMatch</li>
     * </ul>
     * </br>
     *
     * @exception JspException if a JSP exception occurs
     */
    protected void checkMatchType() throws JspException {

      if (value == null) {
        throw new JspException(BaseBundle.getMessage("attribute.missing", "value", op));
      }

      if (role != null) {
        throw new JspException(BaseBundle.getMessage("attribute.present", "role", op));
      }

      if (user != null) {
        throw new JspException(BaseBundle.getMessage("attribute.present", "user", op));
      }

    }

    /**
     * <p>Validate the absence/presence of certain attributes for
     * the following operator values
     * (Some attributes must be present and some must NOT be specified).</br>
     * </p>
     * <ul>
     * <li>Present</li>
     * <li>NotPresent</li>
     * </ul>
     * </br>
     *
     * @exception JspException if a JSP exception occurs
     */
    protected void checkPresentType() throws JspException {

      if (location != null) {
        throw new JspException(BaseBundle.getMessage("attribute.present", "location", op));
      }

      if (value != null) {
        throw new JspException(BaseBundle.getMessage("attribute.present", "value", op));
      }

    }

    /**
     * Evaluate the Match condition and if matched return <code>true</code> if
     * match desired and <code>false</code> if it is not (i.e. NoMatch).
     *
     * @param desired Desired value for a true result
     *
     * @exception JspException if a JSP exception occurs
     */
    protected boolean conditionMatch(boolean desired) throws JspException {

        // Acquire the specified variable
        String variable = null;
        if (cookie != null) {
            Cookie cookies[] =
                ((HttpServletRequest) pageContext.getRequest()).
                getCookies();
            if (cookies == null)
                cookies = new Cookie[0];
            for (int i = 0; i < cookies.length; i++) {
                if (cookie.equals(cookies[i].getName())) {
                    variable = cookies[i].getValue();
                    break;
                }
            }
        } else if (header != null) {
            variable =
                ((HttpServletRequest) pageContext.getRequest()).
                getHeader(header);
        } else if (name != null) {
            Object value =
                TagUtils.getInstance().lookup(pageContext, name, property, scope);
            if (value != null)
                variable = value.toString();
        } else if (parameter != null) {
            variable = pageContext.getRequest().getParameter(parameter);
        } else {
            JspException e = new JspException
                (BaseBundle.getMessage("logic.selector"));
            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }
        if (variable == null) {
            JspException e = new JspException
                (BaseBundle.getMessage("logic.variable", value));
            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }

        // Perform the comparison requested by the location attribute
        boolean matched = false;
        if (location == null) {
            matched = (variable.indexOf(value) >= 0);
        } else if (location.equals("start")) {
            matched = variable.startsWith(value);
        } else if (location.equals("end")) {
            matched = variable.endsWith(value);
        } else {
            JspException e = new JspException
                (BaseBundle.getMessage("logic.location", location));
            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }

        // Return the final result
        return (matched == desired);

    }


    /**
     * Evaluate the Present condition and if present return <code>true</code> if
     * present desired and <code>false</code> if it is not (i.e. NotPresent).
     *
     * @param desired Desired outcome for a true result
     *
     * @exception JspException if a JSP exception occurs
     */
    protected boolean conditionPresent(boolean desired) throws JspException {

        // Evaluate the presence of the specified value
        boolean present = false;
        if (cookie != null) {
            Cookie cookies[] =
                ((HttpServletRequest) pageContext.getRequest()).
                getCookies();
            if (cookies == null)
                cookies = new Cookie[0];
            for (int i = 0; i < cookies.length; i++) {
                if (cookie.equals(cookies[i].getName())) {
                    present = true;
                    break;
                }
            }
        } else if (header != null) {
            String value =
                ((HttpServletRequest) pageContext.getRequest()).
                getHeader(header);
            present = (value != null);
        } else if (name != null) {
            Object value = null;
            try {
                value =
                		TagUtils.getInstance().lookup(pageContext, name, property, scope);
            } catch (JspException e) {
                value = null;
            }
            present = (value != null);
        } else if (parameter != null) {
            String value =
                pageContext.getRequest().getParameter(parameter);
            present = (value != null);
        } else if (role != null) {
            HttpServletRequest request =
                (HttpServletRequest) pageContext.getRequest();
            present = request.isUserInRole(role);
        } else if (user != null) {
            HttpServletRequest request =
                (HttpServletRequest) pageContext.getRequest();
            Principal principal = request.getUserPrincipal();
            present = (principal != null) &&
                user.equals(principal.getName());
        } else {
            JspException e = new JspException
                (BaseBundle.getMessage("logic.selector"));
            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }

        return (present == desired);

    }


    /**
     * Release resources after Tag processing has finished.
     */
    public void release() {

	super.release();

	condition = false;
        location = null;
        andOr    = 0;

    }

}