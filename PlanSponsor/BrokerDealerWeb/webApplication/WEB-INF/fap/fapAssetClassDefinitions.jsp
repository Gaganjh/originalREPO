<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<link rel="stylesheet" type="text/css" media="all" href="/assets/unmanaged/stylesheet_nbdw/screen.css" />
<link rel="stylesheet" type="text/css" media="all" href="/assets/unmanaged/stylesheet_nbdw/contract_funds.css" />

<style>
#page_wrapper {
    background: #eceae3 url('/assets/unmanaged/imagesbackgrounds/background_tile.gif') repeat-y top center;
    height:100%;
    width:350px;
    height: 100%;
    min-height: 600px;
    padding: 0px 0px 0px 0px;
}

#page_content {
    background: #fff url('/assets/unmanaged/imagesbackgrounds/background_page_contents.gif') repeat-x top left;
    min-height: 300px;
    padding: 0;
    width: 350px;
    margin-top: 4px;
    margin-right: 1px;
    margin-bottom: 0;
    margin-left: 1px;
}
</style>
<div id="page_wrapper">
<div id="page_content">
    <!--.Start of Main Report-->
    <div class="page_section_subheader">
      <h3>Asset Class Legend</h3>
    </div>

    <div class="report_table">
        <table class="report_table_content">
        <thead>
            <tr>
                <th class="val_str"><b>Code</b></a></th>
                <th class="val_str"><b>Asset Class</b></th>
            </tr>
        </thead>
        <tbody>
<c:forEach items="${fapForm.assetClassList}" var="assetClass" varStatus="rowIndex">
<c:set var="indexValue" value="${rowIndex.index}"/> 
                <c:choose>
                    <c:when test="${(indexValue + 1) % 2 == 0}">
                        <tr class="spec">
                    </c:when>
                    <c:otherwise>
                        <tr>
                    </c:otherwise>
                </c:choose>
                
                    <td>${assetClass.value}</td>
                    <td>${assetClass.label}</td>            
                </tr>
</c:forEach>
        </tbody>
        </table>
    </div>
</div>  <!-- page_content -->
</div> <!-- page_wrapper -->
