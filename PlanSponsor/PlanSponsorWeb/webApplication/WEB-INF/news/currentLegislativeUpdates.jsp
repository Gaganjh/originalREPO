<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>


<link rel="stylesheet"
	href="/assets/unmanaged/stylesheet/psMainTabNav.css" type="text/css">
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psTabNav.css"
	type="text/css">
<un:useConstants var="contentConstants"
	className="com.manulife.pension.ps.web.content.ContentConstants" />
<%// Since the tab's title is coming dynamically from content database, giving the name as tab 1,tab2.... %>
	<c:choose>
		<c:when test="${param.page == 'tab2'}">
			<c:set var="contentIdForTabContent" value="${contentConstants.PSW_LEGISLATIVE_UPDATES_TAB2}"  />
		</c:when>
		<c:when test="${param.page == 'tab3'}">
			<c:set var="contentIdForTabContent" value="${contentConstants.PSW_LEGISLATIVE_UPDATES_TAB3}"  />
		</c:when>
		<c:when test="${param.page == 'tab4'}">
			<c:set var="contentIdForTabContent" value="${contentConstants.PSW_LEGISLATIVE_UPDATES_TAB4}"  />
		</c:when>
		<c:when test="${param.page == 'tab5'}">
			<c:set var="contentIdForTabContent" value="${contentConstants.PSW_LEGISLATIVE_UPDATES_TAB5}"  />
		</c:when>
		<c:when test="${param.page == 'tab6'}">
			<c:set var="contentIdForTabContent" value="${contentConstants.PSW_LEGISLATIVE_UPDATES_TAB6}"  />
		</c:when>
	</c:choose>


	<content:contentBean contentId="${contentIdForTabContent}"
		type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="Legislative" />	
		
	<content:contentBean contentId="${contentConstants.PSW_LEGISLATIVE_UPDATES_TAB2}"
		type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="Tab2" />	
		
	<content:contentBean contentId="${contentConstants.PSW_LEGISLATIVE_UPDATES_TAB3}"
		type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="Tab3" />
		
	<content:contentBean contentId="${contentConstants.PSW_LEGISLATIVE_UPDATES_TAB4}"
		type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="Tab4" />
		
	<content:contentBean contentId="${contentConstants.PSW_LEGISLATIVE_UPDATES_TAB5}"
		type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="Tab5" />
		
	<content:contentBean contentId="${contentConstants.PSW_LEGISLATIVE_UPDATES_TAB6}"
		type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="Tab6" />

<content:errors scope="session"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="4%" border="0" cellspacing="0" cellpadding="0"><img
			src="/assets/unmanaged/images/s.gif" width="10" height="1"></td>

		<td width="90%">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<%-- TAB section --%>
			<tr>
				<td valign="bottom" colspan="4">
				<DIV class="nav_Main_display" id="div">
				<UL class="">
					<c:choose>
						<c:when test="${param.page==null || param.page == 'tab1'}">
							<LI id="tab1" class="on">														
								<content:getAttribute attribute="body1" beanName="layoutPageBean" />
							</LI>
						</c:when>
						<c:otherwise>
							<LI id="tab1" onmouseover="this.className='off_over';"
								onmouseout="this.className='';">
								<A href="?page=tab1">																
								<content:getAttribute attribute="body1" beanName="layoutPageBean" />
								</a>
							</LI>
						</c:otherwise>
					</c:choose>

					<c:if test="${ param.page=='tab2'}">
						<LI id="tab2" class="on"><content:getAttribute
							attribute="title" beanName="Tab2" /></LI>
</c:if>
					<c:if test="${ param.page!='tab2'}">
						<LI id="tab2" onmouseover="this.className='off_over';"
							onmouseout="this.className='';">
							<A href="?page=tab2">
								<content:getAttribute attribute="title" beanName="Tab2" />
							</A>
						</LI>
</c:if>
					<c:if test="${ param.page=='tab3'}">
						<LI id="tab3" class="on">
								<content:getAttribute attribute="title" beanName="Tab3" />
						</LI>
</c:if>
					<c:if test="${ param.page!='tab3'}">
						<LI id="tab3" onmouseover="this.className='off_over';"
							onmouseout="this.className='';">
							<A href="?page=tab3">
								<content:getAttribute attribute="title" beanName="Tab3" />
							</A>
						</LI>
</c:if>
					<c:if test="${ param.page=='tab4'}">
						<LI id="tab4" class="on">
								<content:getAttribute attribute="title" beanName="Tab4" />
						</LI>
</c:if>
					<c:if test="${ param.page!='tab4'}">
						<LI id="tab4" onmouseover="this.className='off_over';"
							onmouseout="this.className='';">
							<A href="?page=tab4">
								<content:getAttribute attribute="title" beanName="Tab4" />
							</A>
						</LI>
</c:if>
					<c:if test="${ param.page=='tab5'}">
						<LI id="tab5" class="on">
								<content:getAttribute attribute="title" beanName="Tab5" />
						</LI>
</c:if>
					<c:if test="${ param.page!='tab5'}">
						<LI id="tab5" onmouseover="this.className='off_over';"
							onmouseout="this.className='';">
							<A href="?page=tab5">
								<content:getAttribute attribute="title" beanName="Tab5" />
							</A>
						</LI>
</c:if>
					<c:if test="${ param.page=='tab6'}">
						<LI id="tab5" class="on">
						<content:getAttribute attribute="title" beanName="Tab6" />
						</LI>
</c:if>
					<c:if test="${ param.page!='tab6'}">
						<LI id="tab6" onmouseover="this.className='off_over';"
							onmouseout="this.className='';">
							<A href="?page=tab6">
							<content:getAttribute attribute="title" beanName="Tab6" />
							</A>
						</LI>
</c:if>
				</UL>
				</DIV>
				</td>
			</tr>
			<tr>
				<td class="tablesubhead" colspan="4" height="25"></td>
			</tr>
			<tr>
				<td class="filterSearch" colspan="4" height="12"></td>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${param.page==null || param.page == 'tab1' || param.page == ''}">
						<content:getAttribute attribute="body2" beanName="layoutPageBean" />
					</c:when>
					<c:otherwise>
						<content:getAttribute attribute="text" beanName="Legislative" />
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		<table cellpadding="0" cellspacing="0" border="0" width="765" class="fixedTable" height="">
			<tr>
				<td width="30">&nbsp;</td>
				<td>
					<p><content:pageFooter beanName="layoutPageBean"/></p>
					<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
			 		<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
				</td>
			</tr>
		</table>
 	</td>
	<td width="23%"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	</tr>
</table>
