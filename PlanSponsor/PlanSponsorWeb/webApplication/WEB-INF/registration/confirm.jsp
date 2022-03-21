<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<table width="765" border="0" cellpadding="0" cellspacing="0">
	
	<tbody>
		<tr>
			<td width="15%"><img height="8" src="/assets/unmanaged/images/s.gif" width="10" border="0"></td>
			<td width="50%" valign="top" class="greyText"> <img src="/assets/unmanaged/images/s.gif" width="402" height="23"><br>
				<img src="<content:pageImage type="pageTitle" beanName="layoutPageBean"/>" alt="<content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>" width="215" height="34"><br>		
				<br>
				<table width="425" border="0" cellspacing="0" cellpadding="0">
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
								<content:errors scope="session" />
							</td>
						</tr>

					</tbody>
				</table>
				<img src="/assets/unmanaged/images/s.gif" width="1" height="20"> <br>
				<br>
				
				  <TABLE class=box cellSpacing=0 cellPadding=0 width=550 border=0>
					<TBODY>
					  <TR class=tablehead>
						<TD class=tableheadTD1 colSpan=3><B><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></B></TD>
					  </TR>
					  <TR>
						<TD class=boxborder width=1><IMG height=1src="/assets/unmanaged/images/s.gif" width=1></TD>
						<TD>
							<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
							<TBODY>

							  <TR class="datacell1">
								<td width="180"><strong>First name </strong></td>
								<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
<TD width="350" class="highlight">${registerForm.firstName}</TD>
								<td width="255" ><strong>Email</strong></td>
								<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
<TD width="250" class="highlight">${registerForm.email}</TD>
							  </TR>
<c:if test="${registerForm.tpa ==true}">
							  <TR class="datacell1">
								<td width="65"><strong>Last name </strong></td>
								<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
<TD width="100" class="highlight">${registerForm.lastName}</TD>
								
								<td width="135"><strong>Telephone number </strong></td>
								<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
								<TD width="240"><span class="highlight">
								<c:if test="${registerForm.phone!=''}">
								<render:phone property="registerForm.phone"/></c:if></span>
								<c:if test="${not empty registerForm.ext}">ext </c:if>
                                <span class="highlight">${registerForm.ext}</span></TD>
							
								
							  </TR>
							  <TR class="datacell1">
														
							  
							    <TR class="datacell1">
								<td width="65"><strong>&nbsp; </strong></td>
								<TD class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
<TD width="100" class="highlight">&nbsp;</TD>
								
								<td width="135"><strong>Fax number </strong></td>
								<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
								<TD width="240" class="highlight">
								<c:if test="${registerForm.fax != ''}">
								<render:fax property="registerForm.fax"/></c:if></TD>
							  </TR>
</c:if>

<c:if test="${registerForm.tpa !=true}">
							  <TR class="datacell1">
								<td width="65"><strong>Last name </strong></td>
								<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
<TD width="100" class="highlight">${registerForm.lastName}</TD>
								<td width="135"><strong>Telephone number </strong></td>
								<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
								<TD width="240"><span class="highlight">
								<c:if test="${registerForm.phone!=''}">
								<render:phone property="registerForm.phone"/></c:if></span>
								<c:if test="${not empty registerForm.ext}">ext </c:if>
                                <span class="highlight">${registerForm.ext}</span></TD>
							
								
							  </TR>
							   </c:if>

							</TBODY>
							</TABLE>
						</TD>
						<TD class=boxborder width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					  </TR>
					  <TR>
						<TD colSpan=3>
							<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
							<TBODY>
							<TR>
								<TD width="1" class=boxborder><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
								<TD class=boxborder><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
						   </TR>
						   </TBODY>
						   </TABLE>
						</TD>
					  </TR>
					</TBODY>
				  </TABLE>
				 

				<img src="/assets/unmanaged/images/s.gif" width="1" height="20"> <br>
				<br>
				
				  <TABLE class=box cellSpacing=0 cellPadding=0 width=550 border=0>
					<TBODY>
					 <content:contentBean contentId="<%=ContentConstants.NEW_SECTION_TITLE_CONFIRM%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
									beanName="NEW_SECTION_TITLE_CONFIRM" />
					<tr class="tablehead">
						<td colspan="8" class="tableheadTD1">
													
							<strong><content:getAttribute beanName="NEW_SECTION_TITLE_CONFIRM" attribute="title" /></strong>
						</td>
					</tr>
					  <TR>
						<TD class=boxborder width=1><IMG height=1src="/assets/unmanaged/images/s.gif" width=1></TD>
						<TD>
							<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
							<TBODY>
							<tr>
							 <td width="100" valign="top"><strong>Mobile number </strong></td>
								<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
								<TD width="150" valign="top"><span class="highlight">
								<c:if test="${registerForm.mobile!=''}">
								<render:phone property="registerForm.mobile"/></c:if></span>

								
								 <td width="180" valign="top"><strong>You have elected to receive your security code by </strong></td>
								<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
							
								<TD width="200" class="highlight" >
								<logicext:if name="registerForm" property="passcodeDeliveryPreference" op="equal" value="SMS">
			<logicext:then>
				Text message to mobile number
			</logicext:then>
			</logicext:if>
			<logicext:if name="registerForm" property="passcodeDeliveryPreference" op="equal" value="VOICE_TO_MOBILE">
			<logicext:then>
				Voice message to mobile number
			</logicext:then>
			</logicext:if>
			<logicext:if name="registerForm" property="passcodeDeliveryPreference" op="equal" value="VOICE_TO_PHONE">
			<logicext:then>
				Voice message to telephone number
			</logicext:then>
			</logicext:if>
			<logicext:if name="registerForm" property="passcodeDeliveryPreference" op="equal" value="EMAIL">
			<logicext:then>
				Email
			</logicext:then>
			</logicext:if>
			
			
			</TD>
								

							  </TR>
							  <TR>
									<td width="65"><strong>Username</strong></td>
								<TD class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
<TD width="100" class="highlight">${registerForm.userName}</TD>
							  


<c:if test="${registerForm.tpa !=true}">
							
							

<td width=""><strong>Challenge question </strong></td>
								<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
<TD  class="highlight">${registerForm.challengeQuestion}</TD>

								
							  
</c:if>
</TR>


							</TBODY>
							</TABLE>
							
						</TD>
						<TD class=boxborder width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					  </TR>
					  <TR>
						<TD colSpan=3>
							<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
							<TBODY>
							<TR>
								<TD width="1" class=boxborder><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
								<TD class=boxborder><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
						   </TR>
						   </TBODY>
						   </TABLE>
						</TD>
					  </TR>
					</TBODY>
				  </TABLE>
				<table border="0" cellpadding="0" cellspacing="0" width="525">
					<tbody>
						<tr>
						</tr>
					</tbody>
				</table>
				<img src="/assets/unmanaged/images/s.gif" width="1" height="20"> <br>
				<br>
				<TABLE cellSpacing=0 cellPadding=0 width=525 border=0>
				  <TBODY>
<c:if test="${registerForm.tpa ==false}">
							<jsp:include page="contractPermission.jsp" flush="true" />
</c:if>
<c:if test="${registerForm.tpa ==true}">
				            <jsp:include page="firmAccess.jsp" flush="true" />
</c:if>
				
					<TR>
					  <TD class=databorder width=1 height=1></TD>
					  <TD class=databorder colSpan=8 height=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					</TR>
				  </TBODY>
				</TABLE>
				<br>

				<ps:form method="POST" modelAttribute="registerForm" name="registerForm" action="/do/registration/confirm/">
				<table border="0" cellpadding="0" cellspacing="0" width="550">
				  <tbody>
					<tr>
					  <td width="295" align="right">
						<input name="actionLabel" value="print" onClick="javascript:window.print()" class="button100Lg" type="submit">
					  </td>
					  <td width="191" align="right">
<input type="submit" class="button100Lg" value="continue" name="action"/>
					  </td>
					</tr>
				  </tbody>
				</table>
				</ps:form>

			</td>
			<td width="5%" height="312" valign="top" class="fixedTable"><img src="/assets/unmanaged/images/spacer.gif" width="20" height="1"></td>
			<td width="15%" height="312" valign="top" class="fixedTable">
				<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer1" />
				<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer2" />
				<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer3" />
			</td>
		</tr>
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
