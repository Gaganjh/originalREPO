

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<%-- imports --%>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.bd.web.userprofile.BDUserProfile" %>
<%@ page import="com.manulife.pension.service.security.role.BDInternalUser"%>
<%@page import="com.manulife.pension.service.fund.valueobject.Fund"%>

<%@page import="org.owasp.encoder.Encode"%>
<%@page import="java.util.List"%>
<%@page import="com.manulife.pension.platform.web.util.CommonEnvironment"%>
<un:useConstants var="bdContentConstants"
	className="com.manulife.pension.bd.web.content.BDContentConstants" />

<% 

BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>	
	
	
<style>
.select_color {color:#FFFFFF;}

</style>
<jsp:useBean id="bdPerformanceChartInputForm" scope="session" class="com.manulife.pension.bd.web.fap.BDPerformanceChartInputForm" />

<script language="javascript" type="text/javascript">

	<% List funds = (List)session.getAttribute("viewFunds"); %>
	<% List indexFunds = (List)session.getAttribute("indexFunds"); %>
	<% List jhiFunds = (List)session.getAttribute("jhiFunds"); %>
	 var fundsArray = new Array(<%= funds.size()%>);
 	 var indexFundsArray = new Array(<%= indexFunds.size()%>);
	 var jhiFundsArray= new Array(<%= jhiFunds.size()%>);
	 
	window.onload= loadFunds;
 	var fundErrorArray = new Array(6);
		
	function doSubmit(button){		
		document.forms.bdPerformanceChartInputForm.fund1.value = document.forms.bdPerformanceChartInputForm.fundSelection1.value;
		document.forms.bdPerformanceChartInputForm.fund2.value = document.forms.bdPerformanceChartInputForm.fundSelection2.value;
		document.forms.bdPerformanceChartInputForm.fund3.value = document.forms.bdPerformanceChartInputForm.fundSelection3.value;
		document.forms.bdPerformanceChartInputForm.fund4.value = document.forms.bdPerformanceChartInputForm.fundSelection4.value;
		document.forms.bdPerformanceChartInputForm.fund5.value = document.forms.bdPerformanceChartInputForm.fundSelection5.value;
		document.forms.bdPerformanceChartInputForm.fund6.value = document.forms.bdPerformanceChartInputForm.fundSelection6.value;

		document.forms.bdPerformanceChartInputForm.button.value=button;
		document.forms.bdPerformanceChartInputForm.submit();
	}		
		
	function fundObj(id, name, company, rib, nml) { 
		this.RIBInd = rib;
		this.NMLInd = nml;
		this.fundId = id;
		this.fundName = name;
		this.companyId = company;
	}
		
	function loadFunds(){
		var e = document.getElementById('fundClass');
    	var value = e.options[e.selectedIndex].value;
    	if(value == 'CX0'){
    		signaturePlusFundslabel.style.display='';
    	}else{
    		signaturePlusFundslabel.style.display='none';
    		document.getElementById('SignaturePlusFunds').checked = false;
    	}

		fundErrorArray[0] = '<%= Encode.forJavaScript(Encode.forHtml(bdPerformanceChartInputForm.getFund1())) %>';
		fundErrorArray[1] = '<%= Encode.forJavaScript(Encode.forHtml(bdPerformanceChartInputForm.getFund2())) %>';
		fundErrorArray[2] = '<%= Encode.forJavaScript(Encode.forHtml(bdPerformanceChartInputForm.getFund3())) %>';
		fundErrorArray[3] = '<%= Encode.forJavaScript(Encode.forHtml(bdPerformanceChartInputForm.getFund4())) %>';
		fundErrorArray[4] = '<%= Encode.forJavaScript(Encode.forHtml(bdPerformanceChartInputForm.getFund5())) %>';
		fundErrorArray[5] = '<%= Encode.forJavaScript(Encode.forHtml(bdPerformanceChartInputForm.getFund6())) %>';
		
		<% 
			bdPerformanceChartInputForm.setFund1("");
			bdPerformanceChartInputForm.setFund2("");
			bdPerformanceChartInputForm.setFund3("");
			bdPerformanceChartInputForm.setFund4("");
			bdPerformanceChartInputForm.setFund5("");
			bdPerformanceChartInputForm.setFund6("");
	
		%>
		
		 <% for(int i=0;i<funds.size();i++)	{ %>
			fundsArray[<%=i%>] = new fundObj(
				'<%=((Fund)funds.get(i)).getFundId()%>',
				"<%=((Fund)funds.get(i)).getFundName()%>",
				'<%=((Fund)funds.get(i)).getCompanyId()%>',
				'<%=((Fund)funds.get(i)).getFundCategory()%>',
				'<%=((Fund)funds.get(i)).isNml()%>') ;
		<%}%>
	
		<% for(int i=0;i<indexFunds.size();i++)	{ %>
			indexFundsArray[<%=i%>] = new fundObj(
				'<%=((Fund)indexFunds.get(i)).getFundId()%>',
				"<%=((Fund)indexFunds.get(i)).getFundName()%>",
				'<%=((Fund)indexFunds.get(i)).getCompanyId()%>',
				'<%=((Fund)indexFunds.get(i)).getFundCategory()%>',
				'<%=((Fund)indexFunds.get(i)).isNml()%>') ;
		 <%}%>
		  
		 <% for(int i=0;i<jhiFunds.size();i++)	{ %>
				jhiFundsArray[<%=i%>] = new fundObj(
					'<%=((Fund)jhiFunds.get(i)).getFundId()%>',
					"<%=((Fund)jhiFunds.get(i)).getFundName()%>",
					'<%=((Fund)jhiFunds.get(i)).getCompanyId()%>',
					'<%=((Fund)jhiFunds.get(i)).getFundCategory()%>',
					'<%=((Fund)jhiFunds.get(i)).isNml()%>') ;
			<%}%>

		if(document.getElementById('NYInd').checked == true){
			companyID = '094';
		}else{
			companyID = '019';
		}	
		
    	if(document.getElementById('NMLAllowed')!= null){
			var isNML = document.getElementById('NMLAllowed').checked;
			
			if(isNML == true){
				optNMLInd = 'Y';
			}else{
				optNMLInd = 'N';
			}
		}else{
			<% if(bdPerformanceChartInputForm.isUserNMLAssociated()){ %>
    			optNMLInd = 'Y';
	    	<% }else{ %>
	    		optNMLInd = 'N';
			<%}%>
		}			
		
		fundMenu = 'RIBS';	
		var isSigPlus = document.getElementById('SignaturePlusFunds').checked;
		 if(isSigPlus == true){ 
			var sigInd = 'Y';
			reloadOptionBoxesForJhiFunds(companyID,sigInd);
		 }else{
		reloadOptionBoxes(companyID, optNMLInd, fundMenu);
		 }
		// If any error message comes the funds selected in the dropdown should be shown by default.
		for(var cal=0;cal < document.getElementById('selectFunds1').options.length; cal++){
			for(var num=1;num<=6;num++){
				if(document.getElementById('selectFunds1').options[cal].value == fundErrorArray[num-1] ){
					document.getElementById('selectFunds'+num).selectedIndex = cal;
				}
			}
		}			
	}
	  		
	function emptyOutAllFundOptionBoxes(){
		for(var num = 1; num <= 6; num++){
			var elementId = 'selectFunds'+num;
			document.getElementById(elementId).options.length = 0;
		}
	}
		
	function loadFundDropDowns(){	
	
		var isSigPlus = document.getElementById('SignaturePlusFunds').checked;
		if(isSigPlus != true){
			if(document.getElementById('NMLAllowed')!= null){
				var isNML = document.getElementById('NMLAllowed').checked;
				if(isNML == true){
					optNMLInd = 'Y';
				}else{
					optNMLInd = 'N';
				}
			}else{
				<% if(bdPerformanceChartInputForm.isUserNMLAssociated()){ %>
	    			optNMLInd = 'Y';
		    	<% }else{ %>
		    		optNMLInd = 'N';
				<%}%>
			}
			var companyID;
			//var fundMenu = document.getElementById('fundType').value;
			var fundMenu = 'RIBS';
			if(document.getElementById('NYInd').checked == true){
				companyID = '094';
			}else{
				companyID = '019';
			}
			reloadOptionBoxes(companyID, optNMLInd, fundMenu);
		}
		for(var percent = 1; percent <=6 ; percent++ ){
			document.getElementById('fundPercent'+percent).value = '';
		}
	}
		
	function loadJhiFundsDropDowns(){
			var isSigPlus = document.getElementById('SignaturePlusFunds').checked;
			if(isSigPlus == true){
				isSigPlus = 'Y';
			}else{
				
				isSigPlus = 'N';
			}
		var companyID;
		if(document.getElementById('NYInd').checked == true){
			companyID = '094';
		}else{
			companyID = '019';
		}
		
		reloadOptionBoxesForJhiFunds(companyID,isSigPlus);
		
		for(var percent = 1; percent <=6 ; percent++ ){
			document.getElementById('fundPercent'+percent).value = '';
		}
		
	}
	function reloadOptionBoxesForJhiFunds(companyID,sigPlusInd){
		emptyOutAllFundOptionBoxes();
		if(sigPlusInd =='Y'){
						
				
			for(var num=1; num <= 6; num ++){
				var newOption0 = document.createElement("OPTION");
				newOption0.value = "";
				newOption0.text = "Select investment option or index";
				var elementInd = 'selectFunds'+num; 
				document.getElementById(elementInd).options.add(newOption0);				
			}
			
			for( var ind=0; ind < jhiFundsArray.length; ind++ ){			
				if (jhiFundsArray[ind].companyId == companyID){					
					for(var num=1; num <= 6; num ++){
						var newOption = document.createElement("OPTION");
						newOption.value = jhiFundsArray[ind].fundId;
						newOption.text = jhiFundsArray[ind].fundName;
						var elementInd = 'selectFunds'+num; 
						document.getElementById(elementInd).options.add(newOption);
					}						
				}
			}
			loadIndexFunds();
		}else{			
			loadFundDropDowns();
		}
	}	
	
		
	// To load the dropdwon boxes with the selected filter criteria.
	function reloadOptionBoxes(companyID, optNMLInd, fundMenu) {
		emptyOutAllFundOptionBoxes();
				
		for(var num=1; num <= 6; num ++){
			var newOption0 = document.createElement("OPTION");
			newOption0.value = "";
			newOption0.text = "Select investment option or index";
			var elementInd = 'selectFunds'+num; 
			document.getElementById(elementInd).options.add(newOption0);
			
		}
		for( var ind=0; ind < fundsArray.length; ind++ ){
		
			if (fundsArray[ind].companyId == companyID){
				
				if (fundMenu.indexOf(fundsArray[ind].RIBInd) !=-1){
					if (fundsArray[ind].NMLInd == 'false' ){
						for(var num=1; num <= 6; num ++){
							var newOption = document.createElement("OPTION");
							newOption.value = fundsArray[ind].fundId;
							newOption.text = fundsArray[ind].fundName;
							var elementInd = 'selectFunds'+num; 
							document.getElementById(elementInd).options.add(newOption);
						}
					}else if (optNMLInd == "Y"){
						for(var num=1; num <= 6; num ++){
							var newOption = document.createElement("OPTION");
							newOption.value = fundsArray[ind].fundId;
							newOption.text = fundsArray[ind].fundName;
							var elementInd = 'selectFunds'+num; 
							document.getElementById(elementInd).options.add(newOption);
						}
					}
				}
			}
		}
		loadIndexFunds();
	}
	
	function loadIndexFunds(){	
		for(var num=1; num <= 6; num ++){
			var newOption1 = document.createElement("OPTION");
			newOption1.value = " ";
			newOption1.text = "-------------- Market Indices --------------";
			var elementInd = 'selectFunds'+num; 
			document.getElementById(elementInd).options.add(newOption1);
		}
		
		// To load the index funds into the dropdowns.
		for(var ind = 0; ind < indexFundsArray.length; ind++){
			for(var num=1; num <= 6; num ++){
				var newOption = document.createElement("OPTION");
				newOption.value = indexFundsArray[ind].fundId;
				newOption.text = indexFundsArray[ind].fundName;
				var elementInd = 'selectFunds'+num; 
				document.getElementById(elementInd).options.add(newOption);
			}
		}
	}
	
	function getSelectedClassValueforCharting(){  
	
    	var e = document.getElementById('fundClass');
    	var value = e.options[e.selectedIndex].value;
    	if(value == 'CX0'){
    		signaturePlusFundslabel.style.display='';
    	}else{
    		signaturePlusFundslabel.style.display='none';
    		document.getElementById('SignaturePlusFunds').checked = false;
    	}
    	loadFundDropDowns();
   	}

</script>

<content:contentBean contentId="${bdContentConstants.FAP_GENERIC_VIEW_DISCLOSURE}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="GenericViewDisclosure"/>

<h2><content:getAttribute id="layoutPageBean" attribute="name"/></h2>
<table>
	<tr>
		<td>
			<content:getAttribute id="layoutPageBean" attribute="introduction1"/>
		</td>
	</tr>
	<tr>
		<td>
			<content:getAttribute id="layoutPageBean" attribute="introduction2"/>
		</td>
	</tr>
	<tr>
		<td>
			<content:getAttribute attribute="text" beanName="GenericViewDisclosure"/>
		</td>
	</tr>
</table>
	
<report:formatMessages scope="request" />

<form   name="bdPerformanceChartInputForm" method="get" action="/do/fap/performanceChartInput/">
<input type="hidden" name="button" />
<input type="hidden" name="fromInput" value="true" />
		
<input type="hidden" name="fund1" />
<input type="hidden" name="fund2" />
<input type="hidden" name="fund3" />
<input type="hidden" name="fund4" />
<input type="hidden" name="fund5" />
<input type="hidden" name="fund6" />
<div class="page_section_subheader controls">
			<h3>Step 1: Select Fund Criteria </h3>
</div>
<div class="report_table">

			<table class="report_table_content">
			  <thead>            
            <tr>
               <%-- <th width="150" align="left" valign="bottom" class="val_str">Funds</th>--%>
              <th width="106" valign="bottom" class="val_str">Class</th>

              <th width="646" valign="bottom" class="val_str">Region</th>
              </tr>
				</thead>
					<tbody>
            <tr>
                
                <td class="name">
                <form:select path="bdPerformanceChartInputForm.fundClass" onclick="getSelectedClassValueforCharting()" >
					<form:options items="${fundClassList}" itemValue="code" itemLabel="description"></form:options>
</form:select>

                </td>

                <td class="name">
				<label>
                <form:radiobutton onclick="loadFundDropDowns()" path="bdPerformanceChartInputForm.userPreference"   id="USInd" value="US" />
                  		US
                  	</label>
                  	<label>
<form:radiobutton onclick="loadFundDropDowns()" path="bdPerformanceChartInputForm.userPreference"  id="NYInd" value="NY" />
                    	NY
                    </label>
                    <% if (userProfile.getRole() instanceof BDInternalUser && !userProfile.isInMimic()) { %>
                    <label>
 <form:checkbox onclick="loadFundDropDowns()" path="bdPerformanceChartInputForm.NMLSelection"  id="NMLAllowed"  /> 
                		Include NML
                	</label>
                	<%} %>
                	 <label id="signaturePlusFundslabel">
 <form:checkbox onclick="loadJhiFundsDropDowns()" path="bdPerformanceChartInputForm.jhiIndicator"  id="SignaturePlusFunds"  /> 
                		Display only SIG+ Funds
                	</label>
                </td>
              </tr>
      </tbody></table>
</div>
<br />

		  <div class="page_section_subheader controls">
			<h3>Step 2: Select Date Range</h3>
		  </div>

<div class="report_table">
  <table class="report_table_content">
    <thead>
      <tr>
        <th width="206" align="left" valign="bottom" class="val_str">Start Date</th>
        <th width="700" valign="bottom" class="val_str">End Date</th>
        </tr>
    </thead>

    <tbody>
      <tr class="spec">
        <td class="name">
        <label>
<form:input path="bdPerformanceChartInputForm.startDate" maxlength="12" size="7" cssClass="greyText" id="startDate" />
        </label>          
        <label>
        	(mm/yyyy)
        </label>
        </td>
        <td class="name">
        <label>
<form:input path="bdPerformanceChartInputForm.endDate" maxlength="12" size="7" cssClass="greyText" id="endDate" />
        </label>          
        <label>
		(mm/yyyy)
        </label>
        </td>

        </tr>
    </tbody>
  </table>
</div>

         <br />
 <div class="page_section_subheader controls">
			<h3>Step 3: Select Fund Options</h3>
          </div>

						<div class="report_table">

			<table class="report_table_content">
			  <thead>            
            <tr>
                <th width="316" align="left" valign="bottom" class="val_str">Select Investment Option or Index</th>
              <th valign="bottom" class="val_str">Optional</th>
              </tr>
				</thead>

					<tbody>
            <tr class="spec">
                <td class="name">1. 
<form:select path="bdPerformanceChartInputForm" name="fundSelection1"  cssClass="greyText" size="1" id="selectFunds1" >
					
</form:select>
				</td>

                <td class="name">1.
 <form:input  path="bdPerformanceChartInputForm.fundPercentage1" size="3" maxlength="3" cssClass="greyText" id="fundPercent1" /> %
                </td>
            </tr>
            <tr>
                <td class="name">2. 
<form:select path="bdPerformanceChartInputForm" name="fundSelection2" cssClass="greyText" size="1" id="selectFunds2" >
					
</form:select>
				</td>
                <td class="name">2. 
 <form:input  path="bdPerformanceChartInputForm.fundPercentage2" size="3" maxlength="3" cssClass="greyText" id="fundPercent2" /> %
                </td>
            </tr>
            <tr class="spec">

                <td class="name">3. 
<form:select  path="bdPerformanceChartInputForm" name="fundSelection3" cssClass="greyText" size="1" id="selectFunds3" >
					 
</form:select>
				</td>
                <td class="name">3. 
 <form:input  path="bdPerformanceChartInputForm.fundPercentage3" size="3" maxlength="3" cssClass="greyText" id="fundPercent3" /> %
                </td>

                </tr>
            <tr>
                <td class="name">4. 
<form:select path="bdPerformanceChartInputForm" name="fundSelection4" cssClass="greyText" size="1" id="selectFunds4" >
					
</form:select>
				</td>

                <td class="name">4. 
 <form:input  path="bdPerformanceChartInputForm.fundPercentage4" size="3" maxlength="3" cssClass="greyText" id="fundPercent4" /> %
                </td>
            </tr>
            <tr class="spec">
                <td class="name">5. 
<form:select path="bdPerformanceChartInputForm" name="fundSelection5" cssClass="greyText" size="1" id="selectFunds5"> 
					
</form:select>
				</td>
                <td class="name">5. 
 <form:input  path="bdPerformanceChartInputForm.fundPercentage5" size="3" maxlength="3" cssClass="greyText" id="fundPercent5" /> %
                </td>
            </tr>
            <tr>

                <td class="name">6. 
<form:select path="bdPerformanceChartInputForm" name="fundSelection6" cssClass="greyText" size="1" id="selectFunds6" >
						
</form:select>
				</td>
                <td class="name">6. 
 <form:input  path="bdPerformanceChartInputForm.fundPercentage6" size="3" maxlength="3" cssClass="greyText" id="fundPercent6" /> %
                </td>
                </tr>
      </tbody></table>
</div>
</form>
				<!--.report_table-->

	<div class="clear_footer">&nbsp;</div>
	
	<!--  Action buttons -->
	<div class="button_regular" style="float : right" >
		<a href="#" onclick="doSubmit('view'); return false;">View Chart</a>
	</div>
	<div class="button_regular" style="float : right">
		<a href="#" onclick="javascript:doSubmit('reset');">Reset</a>
	</div>
	
	<div class="clear_footer">&nbsp;</div>
	
	<layout:pageFooter/>
	
