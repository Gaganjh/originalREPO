<%@ tag body-content="empty" import="java.util.ArrayList"
    import="com.manulife.pension.content.view.MutableBDForm"
    import="com.manulife.pension.bd.web.tools.forms.BDFormHelper"
    import="com.manulife.pension.bd.web.userprofile.BDUserProfile"
    import="com.manulife.pension.bd.web.util.BDSessionHelper"
    import="com.manulife.pension.bd.web.BDConstants"
    import="java.util.Collection" import="java.util.Iterator"
    import="java.util.Set" import="java.util.TreeSet"
    import="java.util.Collections"
    import="org.apache.commons.lang.StringUtils"
    import="org.apache.commons.beanutils.BeanComparator"%>

<%@ attribute name="scope" required="false"%>
<%@ attribute name="menuID" required="false"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%
    ArrayList<MutableBDForm> definedBenefit = new ArrayList<MutableBDForm>();
    ArrayList<MutableBDForm> definedContribution = new ArrayList<MutableBDForm>();
    boolean isSpanish = false;
    int headerColspan = 1;
    int subheaderColspan = 4;
    Collection bdFormList = (Collection) request
            .getAttribute(BDConstants.BD_FORMS);
    Iterator iter = bdFormList.iterator();
    Set<String> bdFormTabNavigationList = new TreeSet<String>();
    BDUserProfile userProfile = BDSessionHelper
            .getUserProfile((HttpServletRequest) request);
    BDFormHelper bdFormHelper = new BDFormHelper(userProfile,
            bdFormList);
    ArrayList<MutableBDForm> validBDFormList = bdFormHelper
            .getValidBDForms();

    if (BDConstants.SPACE_SYMBOL.equals(menuID)) {
        menuID = bdFormHelper.getBDFormTabNavigationList();
    }

    if (BDConstants.ADMINISTRATION.equals(menuID)
            || BDConstants.INVESTMENT.equals(menuID)) {
        isSpanish = true;
        headerColspan = 2;
        subheaderColspan = 5;
    }

    for (MutableBDForm bdFormContent : validBDFormList) {
        if (menuID.equals(bdFormContent.getCategory())) {
            if (StringUtils.isBlank(bdFormContent.getTitle())) {
                bdFormContent.setTitle(bdFormContent.getNyTitle());
            }
            if (StringUtils.isBlank(bdFormContent.getDescription())) {
                bdFormContent.setDescription(bdFormContent
                .getNyDescription());
            }

            if (BDConstants.DEFINED_BENEFIT.equals(bdFormContent
            .getFormClassification())) {
                definedBenefit.add(bdFormContent);
            } else {
                definedContribution.add(bdFormContent);
            }
        }
        bdFormTabNavigationList.add(bdFormContent.getCategory());
    }
    BeanComparator comp = new BeanComparator("orderNumber");
    Collections.sort(definedBenefit, comp);
    Collections.sort(definedContribution, comp);

    this.getJspContext().setAttribute("bdFormTabList",
            bdFormTabNavigationList);
    this.getJspContext().setAttribute("definedBenefitList",
            definedBenefit);
    this.getJspContext().setAttribute("definedContributionList",
            definedContribution);
    this.getJspContext().setAttribute("isSpanish", isSpanish);
    this.getJspContext().setAttribute("headerColspan", headerColspan);
    this.getJspContext().setAttribute("subheaderColspan",
            subheaderColspan);
%>


<div id="page_primary_nav" class="page_nav_contract">
<ul style="height:40px;">
    <c:if test="${empty menuID}">
        <c:forEach var="levelOneUserMenuItem" items="${bdFormTabList}"
            varStatus="status">
            <c:choose>
                <c:when test="${status.count eq 1}">
                    <li class="active"><em>${levelOneUserMenuItem}</em></li>
                </c:when>
                <c:otherwise>
                    <li><a onclick="return doMenuId('${levelOneUserMenuItem}')"
                        href="#">${levelOneUserMenuItem}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </c:if>
    <c:if test="${not empty menuID}">
        <c:forEach var="levelOneUserMenuItem" items="${bdFormTabList}"
            varStatus="status">
            <c:choose>
                <c:when test="${menuID eq levelOneUserMenuItem}">
                    <li class="active"><em>${levelOneUserMenuItem}</em></li>
                </c:when>
                <c:otherwise>
                    <li><a onclick="return doMenuId('${levelOneUserMenuItem}')"
                        href="#">${levelOneUserMenuItem}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </c:if>
</ul>
</div>
<div class="page_nav_footer"></div>
<div class="salesToolsTable">
<table class="report_table_content">
    <thead>
        <tr style="border-top-width: 0px;">
            <th rowspan='2' valign="bottom" width="60%" nowrap="nowrap" class="val_str">
            Form Name</th>

            <th colspan="${headerColspan}" valign="bottom"  width="20%" nowrap="nowrap"
                class="val_str">
            <div align="center"><strong>USA</strong></div>
            </th>

            <th colspan="${headerColspan}" valign="bottom"  width="20%"  nowrap="nowrap"
                class="val_str">
            <div align="center"><strong>New York</strong></div>
            </th>
        </tr>
        <tr>
            <th class="sub" align="center">English</th>
            <c:if test="${isSpanish eq 'true'}">
                <th class="val_str">Spanish</th>
            </c:if>
            <th class="val_str" align="center">English</th>
            <c:if test="${isSpanish eq 'true'}">
                <th class="val_str">Spanish</th>
            </c:if>
        </tr>
    </thead>
    <tbody>
        <c:if test="${not empty definedContributionList}">
            <tr>
                <th colspan="${subheaderColspan}" class="zero">
                <div class="page_section_subsubheader">
                <h4><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></h4>
                </div>
                </th>
            </tr>
            <c:forEach var="definedContributionItem"
                items="${definedContributionList}" varStatus="status">
                <c:choose>
                    <c:when test="${status.count % 2 eq 1}">
                        <tr class="spec">
                    </c:when>
                    <c:otherwise>
                        <tr>
                    </c:otherwise>
                </c:choose>
                <td class="name">
                <h2>${definedContributionItem.title}</h2>
                ${definedContributionItem.description}</td>
                <c:choose>
                    <c:when test="${not empty definedContributionItem.englishPDFForm}">
                        <td><a href="javascript://"
                            onclick="PDFWindow('${definedContributionItem.englishPDFForm.path}'); return false;">${definedContributionItem.englishFormNumber}</a></td>
                    </c:when>
                    <c:otherwise>
                        <td>&nbsp;</td>
                    </c:otherwise>
                </c:choose>
                <c:if test="${isSpanish eq 'true'}">
                    <c:choose>
                        <c:when test="${not empty definedContributionItem.spanishPDFForm}">
                            <td><a href="javascript://"
                                onclick="PDFWindow('${definedContributionItem.spanishPDFForm.path}'); return false;">${definedContributionItem.spanishFormNumber}</a></td>
                        </c:when>
                        <c:otherwise>
                            <td>&nbsp;</td>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <c:choose>
                    <c:when
                        test="${not empty definedContributionItem.nyEnglishPDFForm}">
                        <td><a href="javascript://"
                            onclick="PDFWindow('${definedContributionItem.nyEnglishPDFForm.path}'); return false;">${definedContributionItem.nyEnglishFormNumber}</a></td>
                    </c:when>
                    <c:otherwise>
                        <td>&nbsp;</td>
                    </c:otherwise>
                </c:choose>
                <c:if test="${isSpanish eq 'true'}">
                    <c:choose>
                        <c:when
                            test="${not empty definedContributionItem.nySpanishPDFForm}">
                            <td><a href="javascript://"
                                onclick="PDFWindow('${definedContributionItem.nySpanishPDFForm.path}'); return false;">${definedContributionItem.nySpanishFormNumber}</a></td>
                        </c:when>
                        <c:otherwise>
                            <td>&nbsp;</td>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                </tr>
            </c:forEach>
        </c:if>
        <c:if test="${not empty definedBenefitList}">
            <tr>
                <th colspan="${subheaderColspan}" class="zero">
                <div class="page_section_subsubheader">
                <h4><content:getAttribute attribute="body2Header" beanName="layoutPageBean"/></h4>
                </div>
                </th>
            </tr>
            <c:forEach var="definedBenefitItem" items="${definedBenefitList}"
                varStatus="status">
                <c:choose>
                    <c:when test="${status.count % 2 eq 1}">
                        <tr class="spec">
                    </c:when>
                    <c:otherwise>
                        <tr>
                    </c:otherwise>
                </c:choose>
                <td class="name">
                <h2>${definedBenefitItem.title}</h2>
                ${definedBenefitItem.description}</td>
                <c:choose>
                    <c:when test="${not empty definedBenefitItem.englishPDFForm}">
                        <td><a href="javascript://"
                            onclick="PDFWindow('${definedBenefitItem.englishPDFForm.path}'); return false;">${definedBenefitItem.englishFormNumber}</a></td>
                    </c:when>
                    <c:otherwise>
                        <td>&nbsp;</td>
                    </c:otherwise>
                </c:choose>
                <c:if test="${isSpanish eq 'true'}">
                    <c:choose>
                        <c:when test="${not empty definedBenefitItem.spanishPDFForm}">
                            <td><a href="javascript://"
                                onclick="PDFWindow('${definedBenefitItem.spanishPDFForm.path}'); return false;">${definedBenefitItem.spanishFormNumber}</a></td>
                        </c:when>
                        <c:otherwise>
                            <td>&nbsp;</td>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <c:choose>
                    <c:when test="${not empty definedBenefitItem.nyEnglishPDFForm}">
                        <td><a href="javascript://"
                            onclick="PDFWindow('${definedBenefitItem.nyEnglishPDFForm.path}'); return false;">${definedBenefitItem.nyEnglishFormNumber}</a></td>
                    </c:when>
                    <c:otherwise>
                        <td>&nbsp;</td>
                    </c:otherwise>
                </c:choose>
                <c:if test="${isSpanish eq 'true'}">
                    <c:choose>
                        <c:when test="${not empty definedBenefitItem.nySpanishPDFForm}">
                            <td><a href="javascript://"
                                onclick="PDFWindow('${definedBenefitItem.nySpanishPDFForm.path}'); return false;">${definedBenefitItem.nySpanishFormNumber}</a></td>
                        </c:when>
                        <c:otherwise>
                            <td>&nbsp;</td>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                </tr>
            </c:forEach>
        </c:if>
    </tbody>
</table>
<%--.report_table_content--%></div>
<%--.report_table--%>

