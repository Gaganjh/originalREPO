<%@ page import="com.manulife.util.barchart.taglib.BarChartBean" %>
<%@ page import="com.manulife.util.barchart.*" %>
<%@ page import="java.awt.Color" %>
<%@ page import="java.awt.Font" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<jsp:useBean id="assetGrowthBarChart" class="com.manulife.util.barchart.taglib.BarChartBean" scope="request"/>

 <tr class="tablesubhead">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><b>Asset growth</b></td>
          <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="2" valign="top"><b>Contributions &amp; withdrawals</b></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><ps:barChart width="360" name="assetGrowthBarChart" mode="imagemap" scope="request" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Asset Growth BarChart"/> </td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="2" valign="top" ><ps:barChart width="350"  name="contrWithdrawalsBarChart" mode="imagemap" scope="request" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Contributions &amp; Withdrawls BarChart"/> </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
 </tr>