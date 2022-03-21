
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	

<table width="730" border="0" cellspacing="0" cellpadding="0">
<tbody>
<tr>
<td>
	<DIV class="fandp_nav">
		<UL class="">
		<c:choose>				
			<c:when test="${selectedTab == 'contributionAndDistribution'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('summary');" id="summary">Summary</a></LI>				
				<LI class="on">Contributions and Distributions</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('safeHarbor');" id="safeHarbor">Safe Harbor</a></LI>			
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('automaticContribution');" id="automaticContribution">Automatic Contributions</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('investmentInformation');" id="investmentInformation">Investment Information</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contactInformation');" id="contactInformation">Notice Contact</a></LI>				
			</c:when>
			<c:when test="${selectedTab == 'safeHarbor'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('summary');" id="summary">Summary</a></LI>				
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contributionAndDistribution');" id="contributionAndDistribution">Contributions and Distributions</a></LI>
				<LI class="on">Safe Harbor</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('automaticContribution');" id="automaticContribution">Automatic Contributions</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('investmentInformation');" id="investmentInformation">Investment Information</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contactInformation');" id="contactInformation">Notice Contact</a></LI>				
			</c:when>
			<c:when test="${selectedTab == 'automaticContribution'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('summary');" id="summary">Summary</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contributionAndDistribution');" id="contributionAndDistribution">Contributions and Distributions</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('safeHarbor');" id="safeHarbor">Safe Harbor</a></LI>
				<LI class="on">Automatic Contributions</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('investmentInformation');" id="investmentInformation">Investment Information</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contactInformation');" id="contactInformation">Notice Contact</a></LI>								
			</c:when>
			<c:when test="${selectedTab == 'investmentInformation'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('summary');" id="summary">Summary</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contributionAndDistribution');" id="contributionAndDistribution">Contributions and Distributions</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('safeHarbor');" id="safeHarbor">Safe Harbor</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('automaticContribution');" id="automaticContribution">Automatic Contributions</a></LI>
				<LI class="on">Investment Information</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contactInformation');" id="contactInformation">Notice Contact</a></LI>				
			</c:when>	
			<c:when test="${selectedTab == 'contactInformation'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('summary');" id="summary">Summary</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contributionAndDistribution');" id="contributionAndDistribution">Contributions and Distributions</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('safeHarbor');" id="safeHarbor">Safe Harbor</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('automaticContribution');" id="automaticContribution">Automatic Contributions</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('investmentInformation');" id="investmentInformation">Investment Information</a></LI>
				<LI class="on">Notice Contact</LI>				
			</c:when>		
			<c:otherwise>
				<LI class="on">Summary</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contributionAndDistribution');" id="contributionAndDistribution">Contributions and Distributions</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('safeHarbor');" id="safeHarbor">Safe Harbor</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('automaticContribution');" id="automaticContribution">Automatic Contributions</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('investmentInformation');" id="investmentInformation">Investment Information</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contactInformation');" id="contactInformation">Notice Contact</a></LI>
			</c:otherwise>		
		</c:choose>
		</UL> 
	</DIV>
</td>	
</tr>
</tbody>
</table>
