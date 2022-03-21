
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

 <%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>     
<%@page import="com.manulife.pension.service.security.role.Trustee"%>
<%@page import="com.manulife.pension.service.security.role.AuthorizedSignor"%>
<%@page import="com.manulife.pension.service.security.role.AdministrativeContact"%>
<%@page import="com.manulife.pension.service.security.role.IntermediaryContact"%>

<script type="text/javascript" >

var plusIcon = "/assets/unmanaged/images/plus_icon.gif";
var minusIcon = "/assets/unmanaged/images/minus_icon.gif";

function expandSection(cid) {
	expandcontent(cid);
	document.getElementById(cid+"img").src=(document.getElementById(cid).style.display=="block")? minusIcon : plusIcon;
}

function expandAll() {
	expandAllSections();
	setIconImage("sc1img", minusIcon);
	setIconImage("sc2img", minusIcon);
	setIconImage("sc3img", minusIcon);
	setIconImage("sc4img", minusIcon);
}

function contractAll() {
	contractAllSections();
	setIconImage("sc1img", plusIcon);
	setIconImage("sc2img", plusIcon);
	setIconImage("sc3img", plusIcon);
	setIconImage("sc4img", plusIcon);
}

function setIconImage(imgId, iconImage) {
	imgField = document.getElementById(imgId);
	if (imgField != null) {
		imgField.src=iconImage;
	}
}

</script>

            <TABLE class=box cellSpacing=0 cellPadding=0 width=412 border=0>
              <TBODY>
              <TR class=tablehead>

				<logicext:if name="userPermissionsForm" property="action" op="equal" value="add">
					<logicext:then>
						<TD class=tableheadTD1 colSpan=3>
						 <logicext:if name="userPermissionsForm" property="tpaData" op="equal" value="true">
						   <logicext:then>
						     <B>Add profile permissions </B>
						   </logicext:then>
						   <logicext:else>
						     <B>Add Contract Permissions </B>
						   </logicext:else>
						 </logicext:if>
						</TD>
					</logicext:then>
					<logicext:elseif name="userPermissionsForm" property="action" op="equal" value="edit">
						<logicext:then>
							<TD class=tableheadTD1 colSpan=3>
							 <logicext:if name="userPermissionsForm" property="tpaData" op="equal" value="true">
							   <logicext:then>
							     <B>Edit profile permissions </B>
							   </logicext:then>
							   <logicext:else>
							     <B>Edit Contract Permissions </B>
							   </logicext:else>
							 </logicext:if>
							</TD>
						</logicext:then>
						<logicext:else>
							<TD class=tableheadTD1 colSpan=3>
							 <logicext:if name="userPermissionsForm" property="tpaData" op="equal" value="true">
							   <logicext:then>
							     <B>View profile permissions </B>
							   </logicext:then>
							   <logicext:else>
							     <B>View Permissions </B>
							   </logicext:else>
							 </logicext:if>
							</TD>
						</logicext:else>
					</logicext:elseif>
				</logicext:if>

              </TR>
              <TR>
                <TD class=boxborder width=1><IMG height=1
                  src="/assets/unmanaged/images/s.gif"
                  width=1></TD>
                <TD>
                  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>

				 <logicext:if name="userPermissionsForm" property="tpaData" op="equal" value="false">
				   <logicext:then>

                    <TR class="datacell1">
                      <td width="100"><strong>Contract number </strong></td>
                      <TD class=datadivider width=1><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
<TD class="highlightBold">${userPermissionsForm.contractNumber}</TD>
                    </TR>
                    <TR class="datacell1">
                      <td width="100"><strong>Contract name </strong></td>
                      <TD class=datadivider width=1><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
<TD><b class="highlightBold">${userPermissionsForm.contractName}</b></TD>
                    </TR>

                   </logicext:then>
                 </logicext:if>

                    <TR class="datacell1">
                      <td><strong>First name </strong></td>
                      <TD width="1" class=datadivider><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
<TD class="highlightBold">${userPermissionsForm.userFirstName}</TD>
                    </TR>
                    <TR class="datacell1">
                      <td><strong>Last name </strong></td>
                      <TD class=datadivider><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
<TD class="highlightBold">${userPermissionsForm.userLastName}</TD>
                    </TR>

				 <logicext:if name="userPermissionsForm" property="tpaData" op="equal" value="true">
				   <logicext:then>

                    <TR class="datacell1">
                      <td width="100"><strong>TPA firm ID</strong></td>
                      <TD class=datadivider width=1><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
<TD class="highlightBold">${userPermissionsForm.tpaFirmID}</TD>
                    </TR>
                    <TR class="datacell1">
                      <td width="100"><strong>TPA firm name </strong></td>
                      <TD class=datadivider width=1><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
<TD><b class="highlightBold">${userPermissionsForm.tpaFirmName}</b></TD>
                    </TR>

                   </logicext:then>
                 </logicext:if>

				 <logicext:if name="userPermissionsForm" property="tpaData" op="equal" value="false">
				   <logicext:then>

                    <TR class="datacell1">
                      <td width="100"><strong>Role</strong></td>
                      <TD width="1" class=datadivider><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
<TD><b class="highlightBold">${userPermissionsForm.role.label}</b></TD>
                    </TR>

<c:if test="${userPermissionsForm.businessConverted ==true}">
						<logicext:if name="userPermissionsForm" property="role.value" op="equal" value="<%=IntermediaryContact.ID%>">
						<logicext:then>
						<TR class="datacell1">
						  <td><strong>Contact type </strong></td>
						  <TD class=datadivider><IMG height=1
							src="/assets/unmanaged/images/s.gif"
							width=1></TD>
<TD class="highlightBold">${userPermissionsForm.contactType}</TD>
						</TR>
					  </logicext:then>
					  </logicext:if>

						<logicext:if name="userPermissionsForm" property="role.value" op="equal" value="<%=Trustee.ID%>">
						<logicext:or name="userPermissionsForm" property="role.value" op="equal" value="<%=AuthorizedSignor.ID%>" />
						<logicext:or name="userPermissionsForm" property="role.value" op="equal" value="<%=AdministrativeContact.stringID%>" />
						<logicext:then>
						<TR class="datacell1">
						  <td><strong>Primary contact </strong></td>
						  <TD class=datadivider><IMG height=1
							src="/assets/unmanaged/images/s.gif"
							width=1></TD>
						  <TD class="highlightBold">
						  <logicext:if name="userPermissionsForm" property="primaryContact"  op="equal" value="true">
						  	<logicext:then>Yes</logicext:then>
						  	<logicext:else>No</logicext:else>
						  </logicext:if>
						</TD>
						  </TR>
						<TR class="datacell1">
						  <td><strong>Mail recipient </strong></td>
						  <TD class=datadivider><IMG height=1
							src="/assets/unmanaged/images/s.gif"
							width=1></TD>
						  <TD class="highlightBold">
						    <logicext:if name="userPermissionsForm" property="mailRecipient"  op="equal" value="true">
						  	<logicext:then>Yes</logicext:then>
						  	<logicext:else>No</logicext:else>
							</logicext:if>
						  </TD>
						  </TR>
						  </logicext:then>
						  </logicext:if>
</c:if>

                   </logicext:then>
                 </logicext:if>


                    <TR class="beigeborder">
                      <td colspan="3"><IMG height=1
                  src="/assets/unmanaged/images/s.gif"
                  width=1></td>
                      </TR>
                    </TBODY>
                </TABLE>
                </TD>
                <TD class=boxborder width=1><IMG height=1
                  src="/assets/unmanaged/images/s.gif"
                  width=1></TD></TR>
              <TR>
                <TD colSpan=3>
                  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                   </TBODY>
                 </TABLE>
                </TD>
              </TR>
              </TBODY></TABLE>
            <Br>
            <TABLE cellSpacing=0 cellPadding=0 width="412" border=0>
              <TBODY>
                <TR>
                  <TD width="9"/>
                  <TD width="357" align=right><div align="left"><STRONG><A
                  href="javascript:expandAll()"><img src="/assets/unmanaged/images/plus_icon_all.gif" border="0"></A>/<A
                  href="javascript:contractAll()"><img src="/assets/unmanaged/images/minus_icon_all.gif" border="0"></A> All
                    Sections </STRONG></div></TD>
                </TR>
              </TBODY>
            </TABLE>
