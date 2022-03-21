package com.manulife.pension.ps.web.taglib.resources;

/*
  File: FormsTableTag.java

  Version   Date         Author           Change Description
  -------   ----------   --------------   ------------------------------------------------------------------

*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.manulife.pension.platform.web.taglib.util.TagUtils;

import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.content.view.MutableTpaForm;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.util.log.LogUtility;

/**
 * This class is the tag that will build the tpaforms table
 *
 * @author   
 * @version  )
 **/

public class TpaFormsTableTag extends TagSupport {

	private String formsName;
	private String layoutPage;
	private String typeOfForm;

	private Logger logger = Logger.getLogger(getClass());
	private Environment env = Environment.getInstance();
	
    private static final String JOHN_HANCOCK_TEXT = "John Hancock";
    private static final String JOHN_HANCOCK_NEWYORK_TEXT = "John Hancock New York";
	
    // mutual fund is obsolete
//	private static final String CLASSIFICATION_M = "Mutual Fund";
	private static final String CLASSIFICATION_G = "Group Annuity";
    private static final String CLASSIFICATION_D = "Defined Benefit";   	


	public void setCollection(String value){
		this.formsName = value;
	}

	public String getCollection(){
		return this.formsName;
	}

	public void setLayoutPage(String value){
		this.layoutPage = value;
	}

	public String getLayoutPage(){
		return this.layoutPage;
	}

	/**
	 * @return Returns the typeOfForm.
	 */
	public String getTypeOfForm() {
		return typeOfForm;
	}
	/**
	 * @param typeOfForm The typeOfForm to set.
	 */
	public void setTypeOfForm(String typeOfForm) {
		this.typeOfForm = typeOfForm;
	}

/**
  *doStartTag is called by the JSP container when the tag is encountered
*/
	public int doStartTag()throws JspException {

		//List gaForms = new ArrayList();
		//Group Annuity form types
		/*List gaInvestTpaSignature = new ArrayList();
		List gaAdminForms = new ArrayList();
		List gaContInstall = new ArrayList();
		List gaInvestTpaNa = new ArrayList();
		List gaInvestTpaHyb = new ArrayList();
		List gaInvestTpaRet = new ArrayList();
		List gaInvestTpaVen = new ArrayList();
		List gaInvestTpaVenRet = new ArrayList();	
		List gaInvestTpaBrdHyb = new ArrayList();*/
		
        //defined benefit form types
       /* List dbInvestTpaSignature = new ArrayList();
        List dbAdminForms = new ArrayList();
        List dbContInstall = new ArrayList();
        List dbInvestTpaNa = new ArrayList();
        List dbInvestTpaHyb = new ArrayList();
        List dbInvestTpaRet = new ArrayList();
        List dbInvestTpaVen = new ArrayList();
        List dbInvestTpaVenRet = new ArrayList();
        List dbInvestTpaBrdHyb = new ArrayList();*/
		
		//Mutual Fund form types
//		List mfAdminForms = new ArrayList();
//		List mfContInstall = new ArrayList();
		
		LayoutPage layoutPage = null;
		
		//String loc = getSite();
		String location = Environment.getInstance().getSiteLocation();
		
			try {

					Collection formlist = (Collection)(pageContext.getAttribute(this.formsName,PageContext.REQUEST_SCOPE));
									
					if (!formlist.isEmpty() ) {

						// the layout page is required for the section titles
					 	Object bean = TagUtils.getInstance().lookup(pageContext, getLayoutPage(), null);
						if (bean != null && bean instanceof LayoutPage) {
							layoutPage = (LayoutPage) bean;
						}


						if (layoutPage == null) {
							throw new IOException();
						}

						Iterator iter = formlist.iterator();
						
						
						HashMap<String, ArrayList<MutableTpaForm>> formsMapGa= new LinkedHashMap<String, ArrayList<MutableTpaForm>>();
						HashMap<String, ArrayList<MutableTpaForm>> formsMapDb=new LinkedHashMap<String, ArrayList<MutableTpaForm>>();
						
						HashMap<String, String> formTypeMap = new LinkedHashMap<String, String>();
						String s = new String("FORM_TYPE");
				 		try {
							formTypeMap = (BrowseServiceDelegate.getInstance()).getTpaFormType(s);
							
							for (Entry<String, String> entry : formTypeMap.entrySet()) {
								String tpaFormType = StringUtils.trimToEmpty(entry.getValue());
								ArrayList<MutableTpaForm> value = null;
								if(StringUtils.isNotBlank(tpaFormType)){
									formsMapGa.put(tpaFormType, value);
									formsMapDb.put(tpaFormType, value);
								}
							}
						} catch (ContentException e) {
							 logger.error(e.getMessage(), e);
						}
				 		
						while (iter.hasNext()) {
							MutableTpaForm tpaFormContent = (MutableTpaForm) iter.next();
							// defined contribution
							if(getTypeOfForm().equalsIgnoreCase("ga")) {
								if ((tpaFormContent.getFormName()!= null) && (tpaFormContent.getFormName().trim().length()>0)) {
									if (tpaFormContent.getTpaFormClassification().equals(CLASSIFICATION_G)) {
										ArrayList<MutableTpaForm> formsListGa= formsMapGa.get(StringUtils.trimToEmpty(tpaFormContent.getTpaFormType()));
										if(formsListGa==null){
											formsListGa=new ArrayList<MutableTpaForm>();
											formsMapGa.put(tpaFormContent.getTpaFormType(),formsListGa);
										}
										formsListGa.add(tpaFormContent);
									}
								}
                            // defined benefit
							} else if (getTypeOfForm().equalsIgnoreCase("db")) {
								if ((tpaFormContent.getFormName() != null) && (tpaFormContent.getFormName().trim().length() > 0)) {
									if (tpaFormContent.getTpaFormClassification().equals(CLASSIFICATION_D)) {
										ArrayList<MutableTpaForm> formsListDb= (ArrayList<MutableTpaForm>) formsMapDb.get(StringUtils.trimToEmpty(tpaFormContent.getTpaFormType()));
										if(formsListDb==null){
											formsListDb=new ArrayList<MutableTpaForm>();
											formsMapDb.put(tpaFormContent.getTpaFormType(),formsListDb);
										}
										formsListDb.add(tpaFormContent);
									}
								}
							}
                        } //end while loop
						
						
						 JspWriter out = pageContext.getOut();						
						 out.println("<tr>");
						 out.println("<td colspan=\"4\">");
				         out.println("<div id=\"accordion\">");	
				       
				        
                        //Create tables based on the types of forms in the cma
						//Defined contribution
						generateTable(formsMapGa,layoutPage, location );  
						//Defined benefit
						generateTable(formsMapDb,layoutPage, location );						 
						 out.print("</div>");
						 out.println("</td>");
						 out.println("</tr>");
						 
					} // end if (!formlist.isEmpty() )
										
				} catch (IOException ex){
					SystemException se = new SystemException(ex,"com.manulife.pension.ps.web.taglib.resources.FormTableTag", "doStartTag", "Exception when building the table: " + ex.toString() );
					LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
					throw new JspException(se.getMessage());
									}
										
				return SKIP_BODY;

									}
									
    /*
     *  add to the form lists according to the form type
     *
     */   
							
     private void generateTable(HashMap<String, ArrayList<MutableTpaForm>> formsMap,LayoutPage layoutPage, String location ) throws IOException {
  
         String JOHN_HANCOCK = "";
         if (location.equals(Constants.SITEMODE_USA)){
             JOHN_HANCOCK = JOHN_HANCOCK_TEXT;
		} else {
			JOHN_HANCOCK = JOHN_HANCOCK_NEWYORK_TEXT;
		}
         
         
         for(Map.Entry<String, ArrayList<MutableTpaForm>> formMapEntry : formsMap.entrySet()){
        	 if(formMapEntry.getValue() !=null){
        		 generateTable(formMapEntry.getValue(), layoutPage, formMapEntry.getKey() , "");
        	 }
		}

	}

	/*
	 * generate the html table for the forms
	 *
	 */

	private void generateTable(List tpaForms, LayoutPage layoutPage, String headingText, String subHeadingText) throws IOException	{

		JspWriter out = pageContext.getOut();

		
		if(headingText.length()> 0){
			createSubHeadings(headingText,out, tpaForms); 
			}
	}
	 
 
	
 private void createSubHeadings(String subHeading, JspWriter out, List formList)throws IOException {
	
	 	out.print("<div class=\"accordion-toggle\">");
	 	out.println("<table width=\"720\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		out.println("<tr height=\"30\">");
		
		out.println("<td class=\"tableheadTD2\"  width=\"720\" >");
		
		out.println("<a>");
		out.println("&nbsp;");
		out.println("<img class=\"plus_icon1\" src=\"/assets/unmanaged/images/plus_icon.gif\" >");
		out.println("<img class=\"minus_icon1\" src=\"/assets/unmanaged/images/minus_icon.gif\"></a> ");
		out.println("&nbsp;<b>");
		out.println(subHeading);
		out.println("</b>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div>");
		
		
		createCollapsableItemContent(subHeading, out,formList);
		
		
  }
 
 private void createCollapsableItemContent(String subHeading,JspWriter out, List formList)throws IOException {
	 	out.println("<div class=\"accordion-content\">");
		out.println("<table width=\"720\" cellspacing=\"0\" cellpadding=\"0\">");
		out.println("<tr>");
		out.println("<td width=\"0\"></td>"); 
		out.println("<td width=\"20\"></td>");
		out.println("<td width=\"560\"></td>");
		out.println("<td width=\"0\"></td>");
		out.println("</tr>");

		 boolean isShade = false;
		 
		if (!formList.isEmpty()) {
			isShade = populateDetails(formList, isShade, out);
		}
		
		out.println("</table >");

		out.println("</div>");
		

		//Break Layer for each accordion section
		out.println("<div>");
		out.println("<table width=\"720\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>");
		out.println("<tr><td>");
		out.println("<img src=\"/assets/unmanaged/images/spacer.gif\" width=\"0\" height=\"1\" border=\"0\">");
		out.println("</td></tr>");
		out.println("</tbody></table>");
		out.println("</div>");
		

  }
 
 
	private boolean populateDetails(List formList, boolean isShade, JspWriter out) throws IOException {

		Iterator iter = formList.iterator();

		String loc = getSite();
		String location = Environment.getInstance().getSiteLocation();
		
		
		while (iter.hasNext()) {

			MutableTpaForm formContent = (MutableTpaForm) iter.next();
			
			if (formContent.getFormName()!= null && formContent.getFormName().length() > 0) {
				
				// common log 50322
			  if (hasPDFForms(location, formContent)) {
				if (isShade) {
              		out.println("<tr class=\"datacell2\">");
					isShade = false;
				} else {
					out.println("<tr class=\"datacell1\">");
					isShade = true;
				}

				//generate acrobat image and link
				StringBuffer buf = new StringBuffer();
				buf.append("<td class=\"databorder\" width=\"0\"><img src=\"/assets/unmanaged/images/s.gif\" height=\"1\"></td>");
				buf.append("<td class=\"tdContent\" align=\"center\"></td>");
                String englishForm = null; //Spanish Forms are no longer valid. Hence we have removed the relevant code as part of this common log #135896
  
                if (location.equals(Constants.SITEMODE_USA)) {
                	if (formContent.getEnglishPDFForm() != null) {
	                	 englishForm = formContent.getEnglishPDFForm().getPath();
                	}
                } else {
                	if (formContent.getNyEnglishPDFForm() != null) {
                	 	englishForm = formContent.getNyEnglishPDFForm().getPath();
                	}
                }
                
				out.println(buf.toString());

				//generate second column title + description
                buf = new StringBuffer();
 	            buf.append("<td class=\"tdContent\" align=\"left\" valign=\"top\" width = \"700\" cmaKey = "+formContent.getKey() + ">");
                buf.append(getFormattedPDFLink(englishForm,loc,null));
				buf.append(formContent.getTitle());
			    buf.append("</a> (");
 	            buf.append(formContent.getFormNumber());
 	            buf.append(")");
 	            buf.append("<br>");
				buf.append(formContent.getDescription());
				buf.append("</td>");
				out.println(buf.toString());

              /*  out.println("<td class=\"tdContent\" colspan=\"2\" align=\"center\" valign=\"middle\">");
                out.println(generateHowToLink(formContent, howToUseBean, false));

				out.println("</td>");*/
				out.println(getFormattedRow("databorder","1","1"));
              	out.println("</tr>");
    		}
		  }
		}
		
		out.println("<tr><td class=\"databorder\" colspan=\"7\" width=\"1\" height=\"1\"></td></tr>");
		
		return isShade;
	}
	private boolean hasPDFForms(String location, MutableTpaForm formContent ) {
		
		boolean hasEnglishPDFForm = false;
		
        if (location.equals(Constants.SITEMODE_USA)) {
        	if (formContent.getEnglishPDFForm() != null) {
        		hasEnglishPDFForm = true;
        	}
        } else {
        	if (formContent.getNyEnglishPDFForm() != null) {
        		hasEnglishPDFForm = true;
        	}
        }
        return (hasEnglishPDFForm? true : false);
	}
	/*
	 * This method will generate the link to the how to page depending on whether it displays the text or whether
	 * it displays the read icon
	 */
	private String generateHowToLink(MutableTpaForm formContent, Miscellaneous howToUse, boolean isLinkText) {

		StringBuffer buf = new StringBuffer("");

		if (formContent.getHowToLayoutPage() != null) {
	 		buf.append("<a href=\"javascript:doHowTo(\'");
	        buf.append(formContent.getHowToLayoutPage().getKey());

	        buf.append("\',\'F\');\" onMouseOver='self.status=\"Go to the How To Page\"; return true'>");

			if (isLinkText) {
			 	buf.append(formContent.getFormName());
			} else {
	            buf.append("<img src=\"");
	            buf.append(howToUse.getImage().getPath());
	            buf.append("\" border=\"0\" title=\"How to use\">");
			}
			buf.append("</a>");

        } else {
        	if (isLinkText) {
        		buf.append(formContent.getFormName());
        	}
           	buf.append("&nbsp;");
        }

        return buf.toString();
	}

	/*
	 * This method will generate the <td></td> row depending on the width and height
	 */
	 private String getFormattedRow(String classStyle, String cellWidth, String cellHeight) {

		StringBuffer buff = new StringBuffer();

		buff.append("<td ");

		if (classStyle != null) {
			buff.append("class=\"");
			buff.append(classStyle);
			buff.append("\" ");
		}

		buff.append(" width=\"");
		buff.append(cellWidth);
		buff.append("\"><img src=\"/assets/unmanaged/images/s.gif\" height=\"");
		buff.append(cellHeight);
		buff.append("\"></td>");

		return buff.toString();
	}

	/*
	 * This method is required to get the domain and protocol as required by the FDF framework
	 */
	private String getSite() {

		StringBuffer loc = new StringBuffer();
		loc.append(env.getSiteProtocol());
		loc.append("://");
		loc.append(env.getSiteDomain());

		return loc.toString();
	}

	private String getFormattedPDFLink(String PDFPath, String loc,Miscellaneous adobe) {

		StringBuffer link = new StringBuffer();

	 	link.append("<a href=\"javascript:");
		link.append("PDFWindow(\'");
		link.append(PDFPath);
  
        link.append("\'); \" onMouseOver='self.status=\"Go to the PDF\"; return true'>");
        
		
	
		if (adobe!= null) {
			link.append("<img src=\"");
			link.append(adobe.getImage().getPath());
			link.append("\" border=\"0\">");
		}

		return link.toString();
	}

	private String getStringValue(String text) {

		if (text== null) {
			return "";
		} else {
			return text;
		}
	}

	
	
	
}
