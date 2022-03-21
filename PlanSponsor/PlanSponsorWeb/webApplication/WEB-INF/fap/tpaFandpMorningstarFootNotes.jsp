<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>

<un:useConstants var="contentConstants"
	className="com.manulife.pension.ps.web.content.ContentConstants" />

	<c:set var="MORNINGSTAR_3_5_10_YR_FOOTNOTE" value="${contentConstants.MORNINGSTAR_3_5_10_YR_FOOTNOTE}"/>
	<c:set var="MORNINGSTAR_3_5_YR_FOOTNOTE" value="${contentConstants.MORNINGSTAR_3_5_YR_FOOTNOTE}"/>
	<c:set var="MORNINGSTAR_3_YR_FOOTNOTE" value="${contentConstants.MORNINGSTAR_3_YR_FOOTNOTE}"/>
<%-- 
	Iterate the HashMap to get Morningstar Value Object. 	
--%>	
<c:forEach items='${currentTabObject}' var='currentTabObjectMap'>
	<%--  Iterate through the Value Object List --%>
	<c:forEach items='${currentTabObjectMap.value}' var='tabObject' varStatus='tabObjectStatus'>
		<%-- 
			Get the Morningstar Foot Notes from the Value Object(FundBaseInformation)
		--%>
<c:if test="${tabObject.morningstarFootNoteCMAId !=0}">
			<P>
<c:if test="${tabObject.morningstarFootNoteCMAId == MORNINGSTAR_3_5_10_YR_FOOTNOTE}">

					<content:contentBean contentId='${tabObject.morningstarFootNoteCMAId}'
						type="${contentConstants.TYPE_MISCELLANEOUS}"
						id="MorningstarFootNotes_3_5_10_Yr" />
					<content:getAttribute id='MorningstarFootNotes_3_5_10_Yr'
						attribute='text'>
						<c:forEach items='${tabObject.morningstarFootNoteParamsList}'
							var='tabFootNoteObject'>
							<content:param>
								<c:out value='${tabFootNoteObject}'></c:out>
							</content:param>
						</c:forEach>
					</content:getAttribute>
</c:if>
<c:if test="${tabObject.morningstarFootNoteCMAId == MORNINGSTAR_3_5_YR_FOOTNOTE}">

					<content:contentBean contentId='${tabObject.morningstarFootNoteCMAId}'
						type="${contentConstants.TYPE_MISCELLANEOUS}"
						id="MorningstarFootNotes_3_5_Yr" />
					<content:getAttribute id='MorningstarFootNotes_3_5_Yr'
						attribute='text'>
						<c:forEach items='${tabObject.morningstarFootNoteParamsList}'
							var='tabFootNoteObject'>
							<content:param>
								<c:out value='${tabFootNoteObject}'></c:out>
							</content:param>
						</c:forEach>
					</content:getAttribute>
</c:if>
<c:if test="${tabObject.morningstarFootNoteCMAId == MORNINGSTAR_3_YR_FOOTNOTE}">

					<content:contentBean contentId='${tabObject.morningstarFootNoteCMAId}'
						type="${contentConstants.TYPE_MISCELLANEOUS}"
						id="MorningstarFootNotes_3_Yr" />
					<content:getAttribute id='MorningstarFootNotes_3_Yr'
						attribute='text'>
						<c:forEach items='${tabObject.morningstarFootNoteParamsList}'
							var='tabFootNoteObject'>
							<content:param>
								<c:out value='${tabFootNoteObject}'></c:out>
							</content:param>
						</c:forEach>
					</content:getAttribute>
</c:if>
			</P>
</c:if>
	</c:forEach>
</c:forEach>
