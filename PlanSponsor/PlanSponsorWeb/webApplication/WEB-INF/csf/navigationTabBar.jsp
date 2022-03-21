<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%@ taglib prefix="content" uri="manulife/tags/content" %>
	
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_CONTRACT_SERVICE_FEATURE_TAB%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="csfTab"/>  

                     
<td colspan="3" bgcolor="#FFF9F2">
	<DIV class="nav_Main_display">
		<UL class="">
			<LI class="on"><content:getAttribute beanName="csfTab" attribute="text"/></LI>
		</UL> 
	</DIV>
</td>	