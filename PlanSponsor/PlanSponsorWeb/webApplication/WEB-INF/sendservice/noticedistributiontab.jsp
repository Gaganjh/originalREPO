<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

 <td colspan="3">
 <DIV class="nav_Main_display" id="div">
                  <UL>
                    <!-- Upload and Share -->
                    <LI class="on" id="tab1" style="color: black !important;"><SPAN>Active Mailings</SPAN></LI>
                    <LI id="tab2" style="color: black !important;" onmouseover="this.className='off_over';" onmouseout="this.className='';">
                    	
                   	<c:choose>
                    	<c:when test="${param.printFriendly}">
                    		<SPAN>Completed Mailings</SPAN>
                    	</c:when>
                    	<c:otherwise>
                    		<A href="/do/sendservice/completedMail/"><SPAN>Completed Mailings</SPAN></A>
                    	</c:otherwise>
                   	</c:choose>
                    	</LI>
                    <LI id="tab3" style="color: black !important;" onmouseover="this.className='off_over';" onmouseout="this.className='';">
                    <c:choose>
                    	<c:when test="${param.printFriendly}">
                    		<SPAN>Plan Details</SPAN>
                    	</c:when>
                    	<c:otherwise>
                    		<A href="/do/sendservice/planData/"><SPAN>Plan Details</SPAN></A>
                    	</c:otherwise>
                   	</c:choose>
                   	</LI>
                    <!-- Build Your Package --> <!-- Order Status -->
                  </UL>
         </DIV>
         </td>


<style>
DIV.nav_Main_display LI.on {
 	color: black !important;
    font-size: 12px !important;
}
DIV.nav_Main_display LI {
    font-size: 12px !important;
    color: black !important;
    line-height: 13px;
}
DIV.nav_Main_display #tab1.on {
    height: 35px;    
}
DIV.nav_Main_display A:hover {
    color: black !important;
}
DIV.nav_Main_display A:visited {
    color: black !important;
}
DIV.nav_Main_display SPAN {
    color: black !important;
}
</style>
