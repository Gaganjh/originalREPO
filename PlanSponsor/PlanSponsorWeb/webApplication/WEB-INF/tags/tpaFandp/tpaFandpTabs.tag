<%@ tag body-content="empty"
import="java.util.ArrayList"
import="com.manulife.pension.platform.web.navigation.BaseMenuItem"
import="com.manulife.pension.platform.web.fap.constants.FapConstants"
import="com.manulife.pension.ps.web.Constants"
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%
	if (session.getAttribute("tpaFandpTabNavigation") == null ) {
		ArrayList<BaseMenuItem> baseMenuItemArrayList = new ArrayList<BaseMenuItem>(8);
	
		baseMenuItemArrayList.add(new BaseMenuItem(FapConstants.FUND_INFORMATION_TAB_ID,
				FapConstants.TPA_FUND_INFORMATION_TAB, FapConstants.FUND_INFORMATION_URL, false));
		
		baseMenuItemArrayList.add(new BaseMenuItem(FapConstants.PRICES_YTD_TAB_ID, 
				FapConstants.TPA_PRICES_YTD_TAB, FapConstants.PRICES_YTD_URL, false));
		
		baseMenuItemArrayList.add(new BaseMenuItem(FapConstants.PERFORMANCE_FEES_TAB_ID, 
				FapConstants.TPA_PERFORMANCE_FEES_TAB, FapConstants.PERFORMANCE_FEES_URL, false));
		
		baseMenuItemArrayList.add(new BaseMenuItem(FapConstants.STANDARD_DEVIATION_TAB_ID, 
				FapConstants.TPA_STANDARD_DEVIATION_TAB, FapConstants.STANDARD_DEVIATION_URL,false));
		
		baseMenuItemArrayList.add(new BaseMenuItem(FapConstants.FUND_CHAR_I_TAB_ID, 
				FapConstants.TPA_FUND_CHAR_I_TAB, FapConstants.FUND_CHAR_I_URL, true));
		
		baseMenuItemArrayList.add(new BaseMenuItem(FapConstants.FUND_CHAR_II_TAB_ID, 
				FapConstants.TPA_FUND_CHAR_II_TAB, FapConstants.FUND_CHAR_II_URL, true));
		
		baseMenuItemArrayList.add(new BaseMenuItem(FapConstants.MORNINGSTAR_TAB_ID, 
				FapConstants.TPA_MORNINGSTAR_TAB, FapConstants.MORNINGSTAR_URL, false));
		
		baseMenuItemArrayList.add(new BaseMenuItem(FapConstants.FUNDSCORECARD_TAB_ID, 
				FapConstants.TPA_FUNDSCORECARD_TAB, FapConstants.FUNDSCORECARD_URL, true));
		
		session.setAttribute("tpaFandpTabNavigation", baseMenuItemArrayList);
	}
%>

<c:set var="tabs" value="${tpaFandpTabNavigation}" scope="session" />

<div class="fandp_nav">
	<ul class="">
	<c:forEach items="${tabs}" var="tabItem" >
			<%-- enabled means it is the "selected tab".--%>
			<c:choose>
				<c:when test="${tabItem.id eq fapForm.tabSelected}">
					<c:choose>
						<c:when test="${tabItem.largerTitle}">
							<li class="on_large" >${tabItem.title}</li>
						</c:when>
						<c:otherwise>
							<li class="on" >${tabItem.title}</li>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:when test="${empty param['printFriendly']}">
					<c:choose>
						<c:when test="${tabItem.largerTitle}">
							<li class="large" onMouseOver="this.className='large_off_over';" onMouseOut="this.className='large';" >
								<a href="#" onClick="applyFilter('tabsClick${tabItem.id}', '${tabItem.actionURL}');" >
									${tabItem.title}
								</a>
							</li>
						</c:when>
						<c:otherwise>
							<li onMouseOver="this.className='off_over';" onMouseOut="this.className='';" >
								<a class="large_anchor" href="#" onClick="applyFilter('tabsClick${tabItem.id}', '${tabItem.actionURL}');" >
									${tabItem.title}
								</a>
							</li>
						</c:otherwise>
					</c:choose>
					
				</c:when>
				<c:otherwise></c:otherwise>
			</c:choose>
		</c:forEach>
	</ul>
</div>