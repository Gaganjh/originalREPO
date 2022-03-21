<%@ tag
	import="com.manulife.pension.ps.web.messagecenter.model.MCAbstractReportModel"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="mc" tagdir="/WEB-INF/tags/messagecenter"%>

<%@ attribute name="model"
	type="com.manulife.pension.ps.web.messagecenter.model.MCAbstractReportModel"
	required="true" rtexprvalue="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<td colspan="3" bgcolor="#FFF9F2">	
	<c:if test="${fn:length(model.top.children) == 8}" >
	  	<style>
			DIV.nav_Main_display_short LI {
			    width: 84px !important;
			    height: 44px !important;
			    background-size: 84px 44px;
			}
			DIV.nav_Main_display_short LI.off_over {
			    background-image: url("/assets/unmanaged/images/1line_on_tab_short.gif");
			    padding-top: 0px;
			}
		</style>
		<!--[if IE]>
			<style>
				DIV.nav_Main_display_short LI {
				    width: 84px !important;
				    height: 44px !important;
				    background-size: 84px 43px;
				    filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(
						src='/assets/unmanaged/images/1line_off_tab_short.gif',
						sizingMethod='scale');
						
						-ms-filter: "progid:DXImageTransform.Microsoft.AlphaImageLoader(
						src='/assets/unmanaged/images/1line_off_tab_short.gif',
						sizingMethod='scale')";
				}
				DIV.nav_Main_display_short LI.on {
				    filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(
						src='/assets/unmanaged/images/1line_on_tab_short.gif',
						sizingMethod='scale');
						
						-ms-filter: "progid:DXImageTransform.Microsoft.AlphaImageLoader(
						src='/assets/unmanaged/images/1line_on_tab_short.gif',
						sizingMethod='scale')";
				}
			</style>
		<![endif]-->
	</c:if>
	<c:if test="${fn:length(model.top.children) > 8}" >
	  	<style>
			DIV.nav_Main_display_short LI {
			    width: 75.6px !important;
			    height: 44px !important;
			    background-size: 75.6px 44px;
			}
			DIV.nav_Main_display_short LI.off_over {
			    background-image: url("/assets/unmanaged/images/1line_on_tab_short.gif");
			    padding-top: 0px;
			}
		</style>
		<!--[if IE]>
			<style>
				DIV.nav_Main_display_short LI {
				    width: 75px !important;
				    height: 44px !important;
				    background-size: 75.6px 43px;
				    filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(
						src='/assets/unmanaged/images/1line_off_tab_short.gif',
						sizingMethod='scale');
						
						-ms-filter: "progid:DXImageTransform.Microsoft.AlphaImageLoader(
						src='/assets/unmanaged/images/1line_off_tab_short.gif',
						sizingMethod='scale')";
				}
				DIV.nav_Main_display_short LI.on {
				    filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(
						src='/assets/unmanaged/images/1line_on_tab_short.gif',
						sizingMethod='scale');
						
						-ms-filter: "progid:DXImageTransform.Microsoft.AlphaImageLoader(
						src='/assets/unmanaged/images/1line_on_tab_short.gif',
						sizingMethod='scale')";
				}
			</style>
		<![endif]-->
	</c:if>
	
	<DIV class="nav_Main_display_short mc_Tab_container">
		<UL>
			<mc:tab tab="${model.summaryTab}" model="${model}" />						
			<c:forEach var="tab" items="${model.top.children}" >						    
				<mc:tab tab="${tab}" model="${model}" />
			</c:forEach>
		</UL>
	</DIV>
</td>
