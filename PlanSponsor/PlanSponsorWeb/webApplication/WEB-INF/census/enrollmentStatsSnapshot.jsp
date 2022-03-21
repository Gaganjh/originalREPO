<%-- taglibs --%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.census.EmployeeEnrollmentSummaryReportForm" %>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>


<content:contentBean contentId="<%=ContentConstants.ENROLLMENT_STATS_SNAPSHOT_CONDITION%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="conditionalStatement"/> 

<jsp:useBean id="employeeEnrollmentSummaryReportForm" scope="session" type="com.manulife.pension.ps.web.census.EmployeeEnrollmentSummaryReportForm" />

<table class="box" border="0" cellpadding="0" cellspacing="0" width="517">
  <tbody>
    <tr class="tablehead">
      <td colspan="3" class="tableheadTD"><strong>Enrollment Snapshot</strong> as of <render:date value='<%=new java.util.Date().toString() %>' patternOut="MMMM dd, yyyy"/></td>
    </tr>
    <tr>
      <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
      <td>
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tbody>
          <tr valign="top">
            <td colspan="4" class="tablesubhead"><strong>Enrollment method </strong></td>
            <td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
            <td colspan="4" class="tablesubhead"><strong>Employee account status at a glance</strong></td>
          </tr>          
          <tr valign="top">
            <!-- From here the legend -->
            <td>
              <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td colspan="2"><br/></td>                  
                </tr>
                <tr>
                  <td>
                   	 <table border="0" cellpadding="0" cellspacing="0">
                   	   <tr><td height="11" width="11" style="background: <%= Constants.EnrollmentMethodPieChart.AUTO %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
                   	 </table>
                  </td>
                  <td>&nbsp; Auto</td>            
                </tr>
                <tr>
                  <td colspan="2"></td>                  
                </tr>
                <tr>
                  <td>
                   	 <table border="0" cellpadding="0" cellspacing="0">
                   	   <tr><td height="11" width="11" style="background: <%= Constants.EnrollmentMethodPieChart.DEFAULT %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
                   	 </table>
                  </td>
                  <td>&nbsp; Default</td>            
                </tr>
                <tr>
                  <td colspan="2"></td>                  
                </tr>
                <tr>
                  <td>
                   	 <table border="0" cellpadding="0" cellspacing="0">
                   	   <tr><td height="11" width="11" style="background: <%= Constants.EnrollmentMethodPieChart.PAPER %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
                   	 </table>
                  </td>
                  <td>&nbsp; Paper</td>            
                </tr>
                <tr>
                  <td colspan="2"></td>                  
                </tr>  
                <tr>
                  <td>
                   	 <table border="0" cellpadding="0" cellspacing="0">
                   	   <tr><td height="11" width="11" style="background: <%= Constants.EnrollmentMethodPieChart.INTERNET %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
                   	 </table>
                  </td>
                  <td>&nbsp; Internet</td>            
                </tr>
                <tr>
                  <td colspan="2"><br/></td>                  
                </tr>
              </table>
            </td>
            <!-- End the legend -->
            <td colspan="3" class="datacell1">
            	<ps:pieChart beanName="<%= Constants.ENROLLMENT_METHOD_PIECHART %>" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Asset Allocation Chart"/>
            </td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/images/s.gif" height="1" width="1" /></td>
            <!-- From here the legend -->
            <td>
              <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td colspan="2"><br/></td>                  
                </tr>
                <tr>
                  <td>
                   	 <table border="0" cellpadding="0" cellspacing="0">
                   	   <tr><td height="11" width="11" style="background: <%= Constants.ParticipationRatePieChart.PARTICIPANTS %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
                   	 </table>
                  </td>
                  <td>&nbsp; Participants</td>            
                </tr>
                <tr>
                  <td colspan="2"></td>                  
                </tr>
                <tr>
                  <td>
                   	 <table border="0" cellpadding="0" cellspacing="0">
                   	   <tr><td height="11" width="11" style="background: <%= Constants.ParticipationRatePieChart.OPT_OUT %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
                   	 </table>
                  </td>
                  <td>&nbsp; Opt-out</td>            
                </tr>
                <tr>
                  <td colspan="2"></td>                  
                </tr>
                <tr>
                  <td>
                   	 <table border="0" cellpadding="0" cellspacing="0">
                   	   <tr><td height="11" width="11" style="background: <%= Constants.ParticipationRatePieChart.PENDING_ELIGIBILITY %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
                   	 </table>
                  </td>
                  <td>&nbsp; Pending eligibility</td>            
                </tr>
                <tr>
                  <td colspan="2"></td>                  
                </tr>  
                <tr>
                  <td>
                   	 <table border="0" cellpadding="0" cellspacing="0">
                   	   <tr><td height="11" width="11" style="background: <%= Constants.ParticipationRatePieChart.PENDING_ENROLLMENT %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
                   	 </table>
                  </td>
                  <td>&nbsp; Pending enrollment</td>            
                </tr>
                <tr>
                  <td colspan="2"></td>                  
                </tr>
                <tr>
                  <td>
                   	 <table border="0" cellpadding="0" cellspacing="0">
                   	   <tr><td height="11" width="11" style="background: <%= Constants.ParticipationRatePieChart.NOT_ELIGIBLE %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
                   	 </table>
                  </td>
                  <td>&nbsp; Not eligible</td>            
                </tr>
                <tr>
                  <td colspan="2"><br/></td>                  
                </tr>
              </table>
            </td>
            <!-- End the legend -->
            <td colspan="3" class="datacell1">             
              <ps:pieChart beanName="<%= Constants.PARTICIPATION_RATE_PIECHART %>" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Asset Allocation Chart"/>
            </td>
          </tr>
		  <tr>
		    <td colspan="8">
				&nbsp;
			</td>
		  </tr>
          <tr >
            <td colspan="8" align="center"><content:getAttribute id="conditionalStatement" attribute="text"/></td>
            <td></td>
           
          </tr> 
        </tbody>
      </table>
      </td>
      <td width="1" class="boxborder"><img src="/assets/unmanaged/images/images/s.gif" height="1" width="1" /></td>
    </tr>
    <tr>
      <td colspan="3">
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tbody>          
          <tr>
            <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
            <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
          </tr>
        </tbody>
        </table>
      </td>
    </tr>
  </tbody>
</table>
<br />
