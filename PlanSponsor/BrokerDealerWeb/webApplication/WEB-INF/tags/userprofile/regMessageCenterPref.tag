<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_MESSAGE_LEARN_MORE%>" 
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="learnMoreText" />

<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_MESSAGE_CENTER_TEXT%>" 
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="messageCenterText" />

<script type="text/javascript">
  function doNothing() {
	  return false;
  }
</script>
<div style="margin-top:15px">
<div class="label" style="margin-top:0px">* Receive email notifications from Message Center? <br/>
</div>
<div class="inputText" style="margin-top:0px">
  <label>
  <form:radiobutton path="emailNotification" value="true"/>
    Yes
  </label>
  <label> 
  <form:radiobutton path="emailNotification" value="false"/>
    No     
  </label>  
   &nbsp; &nbsp; &nbsp; 
    <a class="LearnMore" href="#learnMore"  name="learnMore" onclick="return doNothing()" class="addToolTip" 
        onmouseout="UnTip()"
        onmouseover="return Tip('<content:getAttribute attribute='text' beanName='learnMoreText' filter='true'/>', WIDTH, -300)">Learn More</a>
  <br />
  <br/>
</div>
</div>    
<br class="clearFloat" />  
<div>
  <p><content:getAttribute attribute='text' beanName='messageCenterText'/></p>
</div>

<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/wz_tooltip.js"></script>
