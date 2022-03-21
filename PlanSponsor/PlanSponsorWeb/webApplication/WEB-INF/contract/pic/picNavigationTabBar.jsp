
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
			<c:when test="${selectedTab == 'moneyType'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('general');" id="general">General Information</a></LI>
				<LI class="on">Money Types</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('eligibility');" id="eligibility">Eligibility</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contribution');" id="contribution">Contributions</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('vesting');" id="vesting">Vesting</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('forfeitures');" id="forfeitures">Forfeitures</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('withdrawals');" id="withdrawals">Withdrawals</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('loans');" id="loans">Loans</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('otherPlanInformation');" id="otherPlanInformation">Other Plan Information</a></LI>
			</c:when>
			<c:when test="${selectedTab == 'eligibility'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('general');" id="general">General Information</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('moneyType');" id="moneyType">Money Types</a></LI>
				<LI class="on">Eligibility</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contribution');" id="contribution">Contributions</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('vesting');" id="vesting">Vesting</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('forfeitures');" id="forfeitures">Forfeitures</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('withdrawals');" id="withdrawals">Withdrawals</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('loans');" id="loans">Loans</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('otherPlanInformation');" id="otherPlanInformation">Other Plan Information</a></LI>				
			</c:when>
			<c:when test="${selectedTab == 'contribution'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('general');" id="general">General Information</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('moneyType');" id="moneyType">Money Types</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('eligibility');" id="eligibility">Eligibility</a></LI>
				<LI class="on">Contributions</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('vesting');" id="vesting">Vesting</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('forfeitures');" id="forfeitures">Forfeitures</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('withdrawals');" id="withdrawals">Withdrawals</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('loans');" id="loans">Loans</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('otherPlanInformation');" id="otherPlanInformation">Other Plan Information</a></LI>				
			</c:when>	
			<c:when test="${selectedTab == 'vesting'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('general');" id="general">General Information</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('moneyType');" id="moneyType">Money Types</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('eligibility');" id="eligibility">Eligibility</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contribution');" id="contribution">Contributions</a></LI>
				<LI class="on">Vesting</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('forfeitures');" id="forfeitures">Forfeitures</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('withdrawals');" id="withdrawals">Withdrawals</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('loans');" id="loans">Loans</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('otherPlanInformation');" id="otherPlanInformation">Other Plan Information</a></LI>				
			</c:when>	
			<c:when test="${selectedTab == 'forfeitures'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('general');" id="general">General Information</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('moneyType');" id="moneyType">Money Types</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('eligibility');" id="eligibility">Eligibility</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contribution');" id="contribution">Contributions</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('vesting');" id="vesting">Vesting</a></LI>
				<LI class="on">Forfeitures</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('withdrawals');" id="withdrawals">Withdrawals</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('loans');" id="loans">Loans</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('otherPlanInformation');" id="otherPlanInformation">Other Plan Information</a></LI>				
			</c:when>
			<c:when test="${selectedTab == 'withdrawals'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('general');" id="general">General Information</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('moneyType');" id="moneyType">Money Types</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('eligibility');" id="eligibility">Eligibility</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contribution');" id="contribution">Contributions</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('vesting');" id="vesting">Vesting</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('forfeitures');" id="forfeitures">Forfeitures</a></LI>
				<LI class="on">Withdrawals</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('loans');" id="loans">Loans</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('otherPlanInformation');" id="otherPlanInformation">Other Plan Information</a></LI>				
			</c:when>
			<c:when test="${selectedTab == 'loans'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('general');" id="general">General Information</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('moneyType');" id="moneyType">Money Types</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('eligibility');" id="eligibility">Eligibility</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contribution');" id="contribution">Contributions</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('vesting');" id="vesting">Vesting</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('forfeitures');" id="forfeitures">Forfeitures</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('withdrawals');" id="withdrawals">Withdrawals</a></LI>
				<LI class="on">Loans</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('otherPlanInformation');" id="otherPlanInformation">Other Plan Information</a></LI>				
			</c:when>
			<c:when test="${selectedTab == 'otherPlanInformation'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('general');" id="general">General Information</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('moneyType');" id="moneyType">Money Types</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('eligibility');" id="eligibility">Eligibility</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contribution');" id="contribution">Contributions</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('vesting');" id="vesting">Vesting</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('forfeitures');" id="forfeitures">Forfeitures</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('withdrawals');" id="withdrawals">Withdrawals</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('loans');" id="loans">Loans</a></LI>
				<LI class="on">Other Plan Information</LI>				
			</c:when>			
			<c:otherwise>
				<LI class="on">General Information</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('moneyType');" id="moneyType">Money Types</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('eligibility');" id="eligibility">Eligibility</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('contribution');" id="contribution">Contributions</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('vesting');" id="vesting">Vesting</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('forfeitures');" id="forfeitures">Forfeitures</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('withdrawals');" id="withdrawals">Withdrawals</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('loans');" id="loans">Loans</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="#" onclick="javascript:getTabData('otherPlanInformation');" id="otherPlanInformation">Other Plan Information</a></LI>
			</c:otherwise>		
		</c:choose>
		</UL> 
	</DIV>
</td>	
</tr>
</tbody>
</table>
