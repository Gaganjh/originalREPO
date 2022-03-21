<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<div id="content">
	<h1>Content Rules </h1>
	<div class="label">Content Name: </div>  
	<div class="inputText">
		<label>
			<strong>
${contentRuleMaintenanceForm.contentRule.name}
			</strong>
		</label>
	</div>
	<div class="label">Key: </div>
	<div class="inputText">
		<label>
			<strong>
${contentRuleMaintenanceForm.contentRule.contentId}
			</strong>
		</label>
	</div>  
	<div class="label">Rule:</div>
	<div class="inputText">
	  <label>
		  <strong>
${contentRuleMaintenanceForm.contentRule.ruleType}
		  </strong>
	  </label>
	</div>
	<br class="clearFloat"/>
	<br class="clearFloat"/>
	<br class="clearFloat"/>
	<div class="BottomBorder">
<c:if test="${contentRuleMaintenanceForm.contentRule.ruleType =='Include'}">
			<div class="SubTitle Gold Left">
		Included Firms
			</div>
</c:if>
<c:if test="${contentRuleMaintenanceForm.contentRule.ruleType =='Exclude'}">
			<div class="SubTitle Gold Left">
		Excluded Firms
			</div>
</c:if>
	</div> 
	<div id="firms" style="overflow:auto">
	<c:forEach var="firm" items="${contentRuleMaintenanceForm.contentRule.brokerDealerFirms}" varStatus="status">
		<div class="label" style="width:350px">
			<div align="left"><c:out value="${status.count}"/>. <c:out value="${firm.firmName}"/>  </div>
		</div>  
	</c:forEach>
	</div>
	<br class="clearFloat"/>
	<div class="formButtons" sytle="clear:both">
		<div class="formButton">
	      <input type="button" class="grey-btn" 
	             onmouseover="this.className +=' btn-hover'" 
	             onmouseout="this.className='grey-btn'"
	             name="cancel" value="Back"
	             onclick="document.location='/do/firmRestrictions/management'"> 
	    </div>	
    </div>
    <br class="clearFloat"/>
<layout:pageFooter/>
</div>

