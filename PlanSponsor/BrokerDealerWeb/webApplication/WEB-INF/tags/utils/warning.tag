<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ attribute name="contentId" type="java.lang.Integer" required="true" %>

<%@tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<content:contentBean contentId="${contentId}" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="warningText" />

<div class="message message_warning">
  <dl>
    <dt>Warning Message</dt>
    <dd>1&nbsp;&nbsp; 
       <content:getAttribute attribute="text" beanName="warningText"/>
     </dd>
    </dl>
</div>
