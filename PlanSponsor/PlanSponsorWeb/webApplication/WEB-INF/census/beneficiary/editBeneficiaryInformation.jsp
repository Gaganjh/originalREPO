<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="psw" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>

<%@page import="com.manulife.pension.ps.web.census.beneficiary.BeneficiaryProperties"%>
<%@ page import="com.manulife.pension.ps.web.census.beneficiary.BeneficiaryForm"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/beneficiary.js"></script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/censusValidation.js"></script>
<c:set var="relation" value="${beneficiaryForm.relationship}" /> 
<script type="text/javascript">
function validateBirthDate(field) {
	return validateField(field, new Array(validateDate), new Array("Invalid date of birth. The date must be in mm/dd/yyyy format."), true)
}
 function isFormChanged() {
	return changeTracker.hasChanged(); 
}
registerTrackChangesFunction(isFormChanged);


if (window.addEventListener) {
	window.addEventListener('load', protectLinksFromCancel, false);
} else if (window.attachEvent) {
	window.attachEvent('onload', protectLinksFromCancel);
}
</script>
<%--Error/Warning box to populate Error message--%>							
<table width="760" border="0" cellspacing="0" cellpadding="1">
	<tr>
		<td width="760" valign="top">
			<table>
					<tr>
					<td width="21"/>
						<td colspan="2">
						<div class="messagesBox"><%-- Override max height if print friendly is on so we don't scroll --%>
						<ps:messages scope="session" 
							maxHeight="${param.printFriendly ? '1000px' : '100px'}"
							suppressDuplicateMessages="true" />
						</div>
						</td>
					</tr><tr><td colspan="2">&nbsp;</td></tr>
				</table>
		</td>
	</tr>
</table>
<jsp:useBean id="beneficiaryForm" scope="session" type="com.manulife.pension.ps.web.census.beneficiary.BeneficiaryForm" />
<c:set var="relation" value="${beneficiaryForm.relationship}" />  

<%-- Beneficiary Information section start--%>
<!-- EDIT MODE -->
<ps:form method="post"  action="/do/census/beneficiary/editBeneficiaryInformation/" modelAttribute="beneficiaryForm" name="beneficiaryForm">

	
		<form:hidden path="beneficiaryType" />
		<form:hidden path="action" />
		<form:hidden path="beneficiaryRecordExisting" />
  <form:hidden path="profileId" />
      <table width="760" border="0" cellpadding="0" cellspacing="0">
	        <tbody>
		        <tr>
					 <td>
						<table width="500" border="0" cellpadding="0" cellspacing="0">
			               	<tbody>
			               	<!-- Employee information-->
			               	<tr>
			               	<td width="15">
				                	<img src="/assets/unmanaged/images/s.gif" width="30" border="0" height="1"></td>
				                <td valign="top" width="609">
									<table width="500" border="0" cellpadding="1" cellspacing="0">
										<tr>
											<td width="500" ><b>
${beneficiaryForm.employeeFirstName}

${employeeLastName}
												| <render:ssn property="beneficiaryForm.employeeSSN"
												defaultValue="" /></b>
											</td>
										</tr>
										<tr>
											<td><b>Last updated date : </b><render:date dateStyle="l"
																			property="beneficiaryForm.employeelastUpdatedDate" />
																			</td>
										</tr>
										<tr>
<td><b>Source of last update : </b>${employeelastUpdatedBy}</td>
										</tr>
									</table>
							<br></br>
							</tr>
<c:if test="${not empty beneficiaryForm.primaryBeneficiaries}">
			             	<!-- start primary beneficiary info -->
<c:forEach items="${beneficiaryForm.primaryBeneficiaries}" var="beneficary" varStatus="indexVal" >
<c:set var="index" value="${indexVal.index}"/>
<%String index = pageContext.getAttribute("index").toString();  %>
				              
				              <tr>
				                <td width="15">
				                	<img src="/assets/unmanaged/images/s.gif" width="30" border="0" height="1"></td>
				                <td valign="top" width="609">
										<table class="box" width="500" border="0" cellpadding="0"
											cellspacing="0">
											<tbody>
												<tr class="tablehead">
													<td class="tableheadTD1" width="475" nowrap="nowrap"><b>
													${index+1}. Primary beneficiary</b></td>
												</tr>
											</tbody>
										</table>
								<div style="display: block;" id="basic">
								
								
					<form:hidden path="primaryBeneficiaries[${index}].beneficiaryNo" value="${index+1}" />
					<form:hidden path= "primaryBeneficiaries[${index}].beneficiaryTypeCode" value="1" />
								
				                  	<table class="greyborder" width="500" border="0" cellpadding="1" cellspacing="0">
				                    	<tbody>
						                    <!-- First Name -->
						                    <tr class="dataCell2">
							                      <td class="databorder" width="1"></td>
							                      <td width="123">First name 
							                      </td>
							                      <td width="26" align="right">
							                      <ps:IteratorfieldHilight type="primary" index="${index+1}"  singleDisplay="true" className="errorIcon" displayToolTip="true" name="<%=BeneficiaryProperties.FIRST_NAME%>"  />
							                      </td>
							                      <td class="greyborder" width="1">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
							                       </td>
							                      <td class="highlight"   width="150">
							                      <form:input path="primaryBeneficiaries[${index}].firstName"  maxlength="30"  cssClass="form-control" />
							                       </td>
							                        <ps:trackChanges   property='<%= "primaryBenefeciary["+Integer.parseInt(index)+"].firstName" %>' name="beneficiaryForm"/>
							                       <td class="highlight" width="97"></td>
							                       <td class="highlight" width="100"></td>
							                      <td class="databorder" width="1">
							                     	 <img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1"> 
							                      </td>
						                    </tr>
				                        
						                        <!-- Last Name -->
						                    <tr class="dataCell1">
							                      <td class="databorder">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1"> 
							                      </td>
							                      <td width="123">
							                      	Last name 
							                      </td>
							                      <td width="26" align="right">
							                       <ps:IteratorfieldHilight type="primary" index="${index+1}" displayToolTip="true" name="<%=BeneficiaryProperties.LAST_NAME%>"  />
							                      </td>
							                      <td class="greyborder">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1"> 
							                      </td>
							
							                      <td class="highlight" colspan="3">
							                       <form:input path="primaryBeneficiaries[${index}].lastName"  maxlength="30"  cssClass="form-control" />
							                     </td>
							                     <ps:trackChanges   property='<%= "primaryBenefeciary["+Integer.parseInt(index)+"].lastName" %>' name="beneficiaryForm"/>
							                       <td class="databorder" width="1">
							                      </td>
						                    </tr>
						                    <!-- Date of Birth -->
						                    <tr class="dataCell2">
							                      <td class="databorder">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
													</td>
							                      <td width="123">
							                      	Date of birth 
							                      </td>
							                      <td width="26" align="right">
							                       <ps:IteratorfieldHilight type="primary" index="${index+1}" displayToolTip="true" name="<%=BeneficiaryProperties.DATE_OF_BIRTH%>"  />
							                      </td>
							                      <td class="greyborder">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
							                       </td>
							                      <td class="highlight"colspan="3" >
							                       <form:input path="primaryBeneficiaries[${index}].stringBirthDate"  id='${10+index}' cssClass="form-control" value="${primaryBeneficiary.stringBirthDate}" maxlength="10" size="10"/>
							                     	 <a  href="javascript://cal"> <img onclick="return handleDateIconClicked(event, '${10+index}');"
																 src="/assets/unmanaged/images/cal.gif" width="16" border="0" height="16"
															 	alt="Use the Calendar to pick the date"/></a>(mm/dd/yyyy) 
							                      </td>
							                       <ps:trackChanges   property='<%= "primaryBeneficiaries["+Integer.parseInt(index)+"].stringBirthDate" %>' name="beneficiaryForm"/>
							                       <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1"> 
							                      </td>
						                    </tr>
				                    
						                    <!-- Relationship -->
						                    <tr class="dataCell1">
							                      <td class="databorder">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1"> 
							                      </td>
							
							                      <td width="123">Relationship 
							                      </td>
							                      <td width="26" align="right"> 
							                      	<ps:IteratorfieldHilight type="primary" index="${index+1}"  displayToolTip="true" name="<%=BeneficiaryProperties.BENEFICIARY_RELATION%>"  />
							                      </td>
							                      <td class="greyborder">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1"> 
							                      </td>
							                      <td class="highlight" >
												 <form:select  styleId='${110+index}'  id='${110+index}' onchange="return enableOther(this,'${40+index}');" path="primaryBeneficiaries[${index}].relationshipCode">
												
																											<form:option value="">-Select-</form:option>
																											<c:forEach var="relationMap" items="${relation}">
												<c:if test="${relationMap.value !=None}">
																											<form:option  value="${relationMap.key}">
																											<c:out value="${relationMap.value}"/>
																											</form:option>
												</c:if>
																											</c:forEach>
												</form:select>
																										</td>
														<ps:trackChanges   property='<%= "primaryBenefeciary["+Integer.parseInt(index)+"].relationshipCode" %>' name="beneficiaryForm"/>
														<td class="highlight" colspan="2" nowrap="nowrap">
														<div id='${40+index}'>
							                              <font color="#000000">If other</font> &nbsp;&nbsp;
								                       		 <ps:IteratorfieldHilight type="primary" index="${index+1}"  displayToolTip="true" name="<%=BeneficiaryProperties.OTHER_BENEFICIARY_RELATION%>"  />
  
 							 <form:input path="primaryBeneficiaries[${index}].otherRelationshipDesc"  cssClass="form-control"  />
								                       		 <script language="JavaScript" > 
																disableOthers('${40+index}','${110+index}')
																</script>
																<ps:trackChanges   property='<%= "primaryBenefeciary["+Integer.parseInt(index)+"].otherRelationshipDesc" %>' name="beneficiaryForm"/>
								                       	</div>
								                  </td>
							                       <td class="databorder" width="1">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1"> 
							                      </td>
							                </tr>
					               			 <!-- Percent Share -->
						                    <tr class="dataCell2">
							                      <td class="databorder" width="1"> 
							                      </td>
							                      <td width="123">
							                      	Percent share 
							                      </td>
							                      <td width="26" align="right">
							                       <ps:IteratorfieldHilight type="primary" index="${index+1}"  displayToolTip="true" name="<%=BeneficiaryProperties.BENEFICIARY_SHARE_PERCENTAGE%>"  />
							                      </td>
							                      <td class="greyborder">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
							                      </td>
							                      <td class="highlight" colspan="3">
							                     <form:input path="primaryBeneficiaries[${index}].stringSharePct"   cssClass="form-control" maxlength="6" size="6" />%
							                      </td>
							                      <ps:trackChanges   property='<%= "primaryBenefeciary["+Integer.parseInt(index)+"].stringSharePct" %>' name="beneficiaryForm"/>
							                      <td class="databorder">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
							                      </td>
							                </tr>
						                    <tr>
						                      <td class="databorder" colspan="8" height="1">
							                      </td>
						                    </tr>
										</tbody>
									</table>
									<table>
							            <tbody>
							            	<tr>
							            	</tr><tr>
							                	<td valign="top">
							                	  <form:checkbox path="primaryBeneficiaries[${index}].beneficiaryDeleted" value="true"/>
							                 	 </td>
							                    <td>Delete primary</td>
												  <ps:trackChanges   property='<%= "primaryBenefeciary["+Integer.parseInt(index)+"].beneficiaryDeleted" %>' name="beneficiaryForm"/>
							            	</tr>
							                	<tr><td><img src="/assets/unmanaged/images/s.gif" width="10">
							                		</td>
												</tr>
										</tbody>
									</table>
									</div>
							</td>
							</tr>
</c:forEach>
</c:if>
							<tr>
				                <td width="15">
				                	<img src="/assets/unmanaged/images/s.gif" width="30" border="0" height="1"></td>
				                <td valign="top" width="609">
<a href="javascript:doAddBeneficiary( '1');"><u>Add primary</u></a>
				                </td>
				             </tr>
							<!-- start contingent beneficiary info -->
<c:if test="${not empty beneficiaryForm.contingentBeneficiaries}">
<c:forEach items="${beneficiaryForm.contingentBeneficiaries}" var="beneficary" varStatus="indexVal" >
<c:set var="index" value="${indexVal.index}"/>
<%String index = pageContext.getAttribute("index").toString();  %>
				              <tr>
				                <td width="15">
				                	<img src="/assets/unmanaged/images/s.gif" width="30" border="0" height="1"></td>
				                <td valign="top" width="609">
				                	<br/>

				                	<br/>
									<table class="box" width="500" border="0" cellpadding="0"
										cellspacing="0">
										<!-- start contingent beneficiary info -->
	
										<tbody>
											<tr class="tablehead">
												<td class="tableheadTD1" width="475" nowrap="nowrap"><b>
												${index+1}. Contingent beneficiary</b></td>
											</tr>
										</tbody>
									</table>
				
				
									<div style="display: block;" id="basic">
						<form:hidden path="contingentBeneficiaries[${index}].beneficiaryNo" value="${indexValue+1}" />
						<form:hidden path="contingentBeneficiaries[${index}].beneficiaryTypeCode" value="2" />
				                  	<table class="greyborder" width="500" border="0" cellpadding="1" cellspacing="0">
				                    	<tbody>
						                    <!-- First Name -->
						                    <tr class="dataCell2">
							                      <td class="databorder" width="1"></td>
							                      <td width="123">First name 
							                      </td>
							                      <td width="26" align="right">
							                        <ps:IteratorfieldHilight type="contingent" index="${index+1}"  displayToolTip="true" name="<%=BeneficiaryProperties.FIRST_NAME%>"  />
							                      </td>
							                      <td class="greyborder" width="1">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
							                       </td>
							                      <td class="highlight" width="150">
								<form:input path="contingentBeneficiaries[${index}].firstName"  maxlength="30"  cssClass="form-control" />
							                       </td>
							                        <ps:trackChanges   property='<%= "contingentBenefeciary["+Integer.parseInt(index)+"].firstName" %>' name="beneficiaryForm"/>
							                       <td class="highlight" width="97"></td>
							                       <td class="highlight" width="100"></td>
							                      <td class="databorder" width="1">
							                     	 <img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1"> 
							                      </td>
						                    </tr>
				                        
						                        <!-- Last Name -->
						                    <tr class="dataCell1">
							                      <td class="databorder">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1"> 
							                      </td>
							                      <td width="123">
							                      	Last name 
							                      </td>
							                      <td width="26" align="right">
							                       <ps:IteratorfieldHilight type="contingent" index="${index+1}" displayToolTip="true" name="<%=BeneficiaryProperties.LAST_NAME%>"  />
							                      </td>
							                      <td class="greyborder">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1"> 
							                      </td>
							
							                      <td class="highlight" colspan="3" >
													<form:input path="contingentBeneficiaries[${index}].lastName"  maxlength="30"  cssClass="form-control" />
							                     </td>
							                       <ps:trackChanges   property='<%= "contingentBenefeciary["+Integer.parseInt(index)+"].lastName" %>' name="beneficiaryForm"/>
							                        <td class="databorder" width="1">
							                      </td>
						                    </tr>
						                    <!-- Date of Birth -->
						                    <tr class="dataCell2">
							                      <td class="databorder">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
													</td>
							                      <td width="123">
							                      	Date of birth 
							                      </td>
							                      <td width="26" align="right">
							                      <ps:IteratorfieldHilight type="contingent" index="${index+1}" displayToolTip="true" name="<%=BeneficiaryProperties.DATE_OF_BIRTH%>"  />
							                      </td>
							                      <td class="greyborder">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
							                       </td>
							                      <td class="highlight" colspan="3">
							                      
							                      
							                     <form:input path="contingentBeneficiaries[${index}].stringBirthDate"  id='${200+index}' cssClass="form-control" maxlength="10" size="10"/>
<%-- <input name="contingentBeneficiaries[${index}].stringBirthDate"  id='${100+indexValue}' class="form-control" value="${contingentBeneficiary.stringBirthDate}"/>  --%>&nbsp;
							                     	 <a  href="javascript://cal"> <img onclick="return handleDateIconClicked(event,'${200+index}');"
																 src="/assets/unmanaged/images/cal.gif" width="16" border="0" height="16"
															 	alt="Use the Calendar to pick the date"/></a>(mm/dd/yyyy) 
							                      </td>
							                      <ps:trackChanges   property='<%= "contingentBeneficiaries["+Integer.parseInt(index)+"].stringBirthDate" %>' name="beneficiaryForm"/>
							                         <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1"> 
							                      </td>
						                    </tr>
				                    
						                    <!-- Relationship -->
						                    <tr class="dataCell1">
							                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1"> </td>
							
							                      <td width="123">Relationship </td>
							                      <td width="26" align="right"><ps:IteratorfieldHilight type="contingent" displayToolTip="true" index="${index+1}" name="<%=BeneficiaryProperties.BENEFICIARY_RELATION%>"  /></td>
							                      <td class="greyborder"><img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1"> </td>
							                     <td class="highlight" >
 <form:select styleId='${150+index}'  id='${150+index}' onchange="return enableOther(this,'${71+index}');" path="contingentBeneficiaries[${index}].relationshipCode">

																<form:option value="">-Select-</form:option>
																<c:forEach var="relationMaps" items="${relation}">
								<c:if test="${relationMaps.value !=None}">
																<form:option  value="${relationMaps.key}"><c:out value="${relationMaps.value}"/></form:option>
</c:if>
																</c:forEach>
</form:select>
															</td>
															  <ps:trackChanges   property='<%= "contingentBenefeciary["+Integer.parseInt(index)+"].relationshipCode" %>' name="beneficiaryForm"/>
															 <td class="highlight" colspan="2"  >
															<div id='${71+index}'>
							                              	<font color="#000000">If other</font> &nbsp;&nbsp;
							                             	 <ps:IteratorfieldHilight type="contingent" index="${index+1}" displayToolTip="true" name="<%=BeneficiaryProperties.OTHER_BENEFICIARY_RELATION%>"  />
<form:input path="contingentBeneficiaries[${index}].otherRelationshipDesc"  maxlength="30"  cssClass="form-control" />
								                       			 <script language="JavaScript" > 
																disableOthers('${71+index}','${150+index}')
																</script>
																 <ps:trackChanges   property='<%= "contingentBenefeciary["+Integer.parseInt(index)+"].otherRelationshipDesc" %>' name="beneficiaryForm"/>
								                       		</div>
								                  </td>
							                        <td class="databorder" width="1">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1"> 
							                      </td>
							                </tr>
					                
					               			 <!-- Percent Share -->
						                    <tr class="dataCell2">
							                      <td class="databorder" width="1"> 
							                      </td>
							                      <td width="123">
							                      	Percent share 
							                      </td>
							                      <td width="26" align="right">
							                      <ps:IteratorfieldHilight type="contingent" index="${index+1}" displayToolTip="true" name="<%=BeneficiaryProperties.BENEFICIARY_SHARE_PERCENTAGE%>"  />
							                      </td>
							                      <td class="greyborder">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
							                      </td>
							                      <td class="highlight" colspan="3">
							<form:input path="contingentBeneficiaries[${index}].stringSharePct"  cssClass="form-control" maxlength="6" size="6" />%
							                      </td>
							                      <ps:trackChanges   property='<%= "contingentBenefeciary["+Integer.parseInt(index)+"].stringSharePct" %>' name="beneficiaryForm"/>
							                      <td class="databorder">
							                      	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
							                      </td>
							                </tr>
						                    <tr>
							                      <td class="databorder" colspan="8" height="1">
							                      </td>
						                    </tr>
										</tbody>
									</table>
									<table>
							            <tbody>
							            	<tr>
							            	</tr><tr>
							                	<td valign="top">
												<form:checkbox path="contingentBeneficiaries[${index}].beneficiaryDeleted" value="true"/>
						                 	 </td>
							                    <td>Delete contingent</td>
							 					 <ps:trackChanges   property='<%= "contingentBenefeciary["+Integer.parseInt(index)+"].beneficiaryDeleted" %>' name="beneficiaryForm"/>
							            	</tr>
							                	<tr><td><img src="/assets/unmanaged/images/s.gif" width="10">
							                		</td>
												</tr>
										</tbody>
									</table>
							</div>
							</td>
							</tr>
</c:forEach>
</c:if>
							<tr>
				                <td width="15">
				                	<img src="/assets/unmanaged/images/s.gif" width="30" border="0" height="1"></td>
				                <td valign="top" width="609">
<a href="javascript:doAddBeneficiary( '2');">
				                 <u>Add contingent</u>
</a>
				                </td>
				             </tr>
						
						   </tbody>
					   </table>
						<table width="615">
						<!-- start buttons -->
						<tbody>
			
							<tr>
								<td><img src="/assets/unmanaged/images/s.gif" width="30"
									border="0" height="1"></td>
								<td><br>
								<table width="500" border="0" cellpadding="0" cellspacing="0">
									<tbody>
										<tr>
			
<td align="left"><input type="button" onclick="return doConfirmAndSubmit('cancel')" name="button" class="button134" id="cancel" value="cancel"/></td>

<td align="right"><input type="button" onclick="return doSubmit('save');" name="button" class="button134" id="save" value="save"/></td>

										</tr>
									</tbody>
								</table>
								</td>
							</tr>
						</tbody>
					 </table>
			      </td>
		
		 </tr> 
	</tbody>
</table>
	
</ps:form>
<!-- footer table -->
		<BR>
		<table height=25 cellSpacing=0 cellPadding=0 width=760 border=0>
		  <tr>
			  <td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
		              <tr>
					    <td width="30" valign="top">
					        <img src="/assets/unmanaged/images/s.gif" width="30" height="1">
					    </td>
						<td>
						<br>
							<p><content:pageFooter beanName="layoutPageBean"/></p>
		 					<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
		 					<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p> 
		 				</td>
		 			</tr>
				</table>
		    </td>
		    <td width="3%"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
		    <td width="15%" ><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
		  </tr> 
		</table>

		