<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>


 
<%-- This jsp includes the following CMA content --%>

<table width="760" border="0" cellpadding="0" cellspacing="0">
  
	  <tbody>
		<ps:form method="POST" modelAttribute="registerForm" name="registerForm" action="/do/registration/tpaauthentication/" >
			<tr>
				<td width="5%">
					<img height="8" src="/assets/unmanaged/images/s.gif" width="10" border="0">
				</td>
				<td width="20%" valign="top">
					<table id="column1" cellspacing="0" cellpadding="0" border="0">
						<tbody>
							<tr valign="top">
								<td><img height="70" src="/assets/unmanaged/images/s.gif" width="100"></td>
							</tr>
							<tr>
								<td class="greyText">&nbsp; </td>
							</tr>
						</tbody>
				    </table>
			    </td>
				<td width="5%">
					<img height="1" src="/assets/unmanaged/images/s.gif" width="15" border="0">
				</td>
				<td width="50%" valign="top" class="greyText">
					<img src="/assets/unmanaged/images/s.gif" width="402" height="23">
					<br>
					<img src="<content:pageImage type="pageTitle" beanName="layoutPageBean"/>" alt="<content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>" width="215" height="34"><br>      <br>
					<table width="394" border="0" cellspacing="0" cellpadding="0">
						<tbody>
							<tr>
								<td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
								<td valign="top" width="495">
									<b><content:getAttribute beanName="layoutPageBean" attribute="subHeader"/></b>
									<br>
									<content:getAttribute attribute="introduction1" beanName="layoutPageBean"/>
									<br><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/>
								</td>
							</tr>
							<tr>
								<td colspan="2" align="left">
									<content:errors scope="request" />
								</td>
							</tr>
						</tbody>
					</table>
					<img src="/assets/unmanaged/images/s.gif" width="1" height="5"> <br>
					<table width="425" border="0" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
								<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
								<td><img src="/assets/unmanaged/images/s.gif" width="400" height="1"></td>
								<td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
								<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							</tr>
							<tr class="tablehead">
								<td colspan="5" class="tableheadTD1">&nbsp;<strong> <content:getAttribute beanName="layoutPageBean" attribute="body1Header"/> </strong></td>
							</tr>
							<tr class="datacell1">
								<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td colspan="2" align="center">
									<table>
											<tbody>
												<tr>
													<td  colspan="2">
													<!--***** -->
														<content:getAttribute beanName="layoutPageBean" attribute="body1"/>
													<!--***** -->
													</td>
												</tr>
												<tr>
													 <td colspan="2">&nbsp;</td>
												</tr>
												<tr>
													<td colspan="2" valign="top">
														<p class="boldText"><strong>Enter your TPA Firm ID, SSN and PIN and click continue:</strong></p></td>
												</tr>
												<tr>
													<td colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												<tr>
													<td width="135" class="highlight"><ps:label fieldId="firmId" mandatory="false">Firm ID</ps:label></td>
<td width="228"><input type="text" name="firmId" size="18" maxlength="5" autocomplete="off" class="inputField" value="${e:forHtmlAttribute(registerForm.firmId)}"></td>
												</tr>
												<tr>
													<td class="highlight"><ps:label fieldId="ssn" mandatory="false">Social security number</ps:label></td>
													<td>
													<form:password path="ssnOne" cssClass="inputField" autocomplete="off"  maxlength="3" size="3" />
													<form:password path="ssnTwo" cssClass="inputField" autocomplete="off"  maxlength="2" size="2" />
													<form:password path="ssnThree" cssClass="inputField" autocomplete="off"  size="4" maxlength="4"/>
												</tr>
												<tr>
													<td class="highlight"><ps:label fieldId="pin" mandatory="false">PIN</ps:label></td>
<td><input type="text" name="pin" size="32" maxlength="32" autocomplete="off" class="inputField" value="${e:forHtmlAttribute(registerForm.pin)}"></td>

												</tr>			
											</tbody>
										</table>
								</td>
								<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							</tr>
							<tr class="datacell1">
								<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
								<td colspan="2" class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td colspan="2" rowspan="2" align="right" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
							</tr>
							
							<tr>
								<td class="databorder" colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
								<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							</tr>
						</tbody>
					</table>
					<br>
					<table width="395" border="0" cellspacing="0" cellpadding="0">
						<tbody>
							<tr align="center">
								<td width="112">&nbsp;</td>
								<td width="139">
<input type="submit" class="button100Lg" value="cancel" name="action" />
								</td>								
								<td width="144">
<input type="submit" class="button100Lg" value="continue" name="action" />
								</td>
							</tr>
							<!--  <script type="text/javascript" >
							  var onenter = new OnEnterSubmit('action', 'continue');
							  onenter.install();
							</script>	-->						
						</tbody>
					</table>
				</td>
				<td width="5%" height="312" valign="top" class="fixedTable">
					<img src="/assets/unmanaged/images/s.gif" width="20" height="1">
				</td>
				<td width="15%" height="312" valign="top" class="fixedTable">
					<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer1" />
					<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer2" />
					<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer3" />
				</td>
			</tr>
			</ps:form>
			<tr>
					<td width="1%"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
					<td colspan="13" width="99%">
						<br>
						<p><content:pageFooter beanName="layoutPageBean"/></p>
						<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
						<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
					</td>
			</tr>
	</tbody>
</table>
<script>
	setFocusOnFirstInputField("registerForm");
</script>
