<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psMainTabNav.css" type="text/css">
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psTabNav.css" type="text/css">
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/manulife.css" type="text/css">
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/plansponsor.css" type="text/css">
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/buttons.css" type="text/css">

<div id="errordivcs"><content:errors scope="session"/></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<ps:form action="/do/contacts/" >
	
		<%-- TAB section - later will be added in the custom tag --%>
    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
    		<tr>
	      		<td colspan="3" valign="bottom">
	      			<DIV class="nav_Main_display" id="div">
		      			<UL class="">
		        			<LI id="tab1" class="on">John Hancock Contacts</LI>
					        <LI id="tab2" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						        <c:if test="${empty param.printFriendly }" >
						          	<A href="/do/contacts/planSponsorContacts">
						        </c:if>
						        Plan Sponsor
						        <c:if test="${empty param.printFriendly }" >
						        	</A>
						        </c:if>
					        </LI>
		        			<LI id="tab3" onmouseover="this.className='off_over';" onmouseout="this.className='';">
			        			<c:if test="${empty param.printFriendly }" >
			        				<A href="/do/contacts/thirdPartyAdministrator">
			        			</c:if>
			        			Third Party Administrator
			        			<c:if test="${empty param.printFriendly }" >
						        	</A>
						        </c:if>
		        			</LI>
		      			</UL>
	      			</DIV>
	      		</td>
    		</tr>
		
		
			
			<tr>
				<TD colspan="3" height="25" class="tablesubhead"><b>Plan Contacts</b>
				</TD>
			</tr>
			
			<tr>
			    <td colspan="3" class="pgNumBack">
					<b>Your Client Accout Representative </b>
				</td>
            </tr>
            <tr>
                <td width="150" class="datacell1">
					ELAINE CLARKE 
				</td>
                <td width="1" class="greyborder">
					<img src="assets/spacer.gif" border="0" height="1" width="1" />
				</td>
                  <td width="550" class="datacell1">1.800.333.0963 ext. 12345 </td>
             </tr>
			 
			<tr>
                <td width="150" class="datacell2">E-mail</td>
                <td width="1" class="greyborder"><img src="assets/spacer.gif" border="0" height="1" width="1"></td>
                <td width="550" class="datacell2"><A href="mailto:CLARELA@jhancock.com">CLARELA@jhancock.com</A></td>
            </tr>
            
			<tr>
				<td colspan="3" class="pgNumBack"><B>Customer Service Toll-Free Fax Line</B></td>
             </tr>
              
			<tr>
                <td width="150" class="datacell1">For enrollment forms: </td>
                <td width="1" class="greyborder"><img src="assets/spacer.gif" border="0" height="1" width="1"></td>
                <td width="550" class="datacell1">1.866.377.8846</td>
            </tr>
            
			<tr>
				<td width="150" class="datacell2">For other documents: </td>
                <td width="1" class="greyborder"><img src="assets/spacer.gif" border="0" height="1" width="1"></td>
                <td width="550" class="datacell2">1.866.377.9577 </td>
            </tr>
                
			<tr>
                <td colspan="3" class="pgNumBack"><b>Participant toll-free service line </b></td>
            </tr>
            
			<tr>
                <td class="datacell1">&nbsp;</td>
                <td width="1" rowspan="2" class="greyborder"><img src="assets/spacer.gif" border="0" height="1" width="1"></td>
                <td class="datacell1">1.800.395.1113</td>
            </tr>
			
			<tr>
                <td class="datacell1">&nbsp;</td>
                <td class="datacell1">
					Your opinion is important to us. We encourage you to contact us if you have any   questions or comments. 
				</td>
            </tr>
			 
			 
			 
    	</table>
