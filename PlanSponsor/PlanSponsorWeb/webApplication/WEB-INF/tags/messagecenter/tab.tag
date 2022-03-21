<%@ tag
	import="com.manulife.pension.service.message.valueobject.MessageCenterComponent"
	import="com.manulife.pension.ps.web.messagecenter.model.MCAbstractReportModel"
	import="com.manulife.pension.ps.web.messagecenter.MCConstants"
	import="com.manulife.pension.ps.web.messagecenter.util.MCUtils"
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="tab"
	type="com.manulife.pension.service.message.valueobject.MessageCenterComponent"
	required="true" rtexprvalue="true"%>

<%@ attribute name="model"
	type="com.manulife.pension.ps.web.messagecenter.model.MCAbstractReportModel"
	required="true" rtexprvalue="true"%>


<c:choose>
	<c:when test="<%=model.isSelectedTab(tab)%>">
		<LI class="on">
		<span>
		<strong><c:out value="${tab.name}"/></strong><c:out value="<%= model.getTabDisplayCount(tab) %>" />
		<span>
		</LI>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="<%=model.getSummaryTab().equals(tab) || model.getTabMessageCount(tab) != 0 %>">
			  <LI onMouseOver="this.className='on';" onMouseOut="this.className='';" onclick="javascript:gotoLink('<%=model.getUrlGenerator().getTabUrl(tab)%>');" >
				<A style="color:#000099;" onclick="return false;" href="#">
				  <strong><c:out value="${tab.name}"/></strong><c:out value="<%= model.getTabDisplayCount(tab) %>" />
				</A>
			  </LI>
			</c:when>
			<c:otherwise>
		  		<LI class="nolink"><span><strong><c:out value="${tab.name}"/></strong><c:out value="<%=model.getTabDisplayCount(tab)%>"/></span></LI>		
			</c:otherwise>
		</c:choose>			
	</c:otherwise>
</c:choose>
