<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ attribute name="contentId" type="java.lang.Integer" required="true" %>

<%@tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<content:contentBean contentId="${contentId}" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="infoText" />

<div class="message message_info">
  <dl>
    <dt>Information Message</dt>
    <dd>1&nbsp;&nbsp; 
       <content:getAttribute attribute="text" beanName="infoText"/>
     </dd>
    </dl>
</div>
