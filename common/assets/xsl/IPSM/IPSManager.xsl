<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:attribute-set name="header_default_font">
	<xsl:attribute name="font-family">Frutiger45Light</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="word_group_font">
	<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="font-weight">Light</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
	<xsl:attribute name="line-height">1.5</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="subheader_default_font">
	<xsl:attribute name="font-family">Frutiger45Light</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="color">#000000</xsl:attribute>
</xsl:attribute-set>
	
<xsl:attribute-set name="header_block_cell3" use-attribute-sets="header_default_font">
	<xsl:attribute name="font-size">24pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="line_border_style">
	<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
	<xsl:attribute name="border-width">0.5px</xsl:attribute>
	<xsl:attribute name="border-color">#000000</xsl:attribute>
	<xsl:attribute name="padding-after">1px</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="sub_header_block">
<xsl:attribute name="font-family">Frutiger57Cn</xsl:attribute>
	<xsl:attribute name="color">#CB5A27</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="word_group_a_style">
	<xsl:attribute name="font-family">Frutiger45Light</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="color">#000000</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="sub_header_block1">
<xsl:attribute name="font-family">Frutiger67BoldCn</xsl:attribute>
	<xsl:attribute name="color"> #FFFFFF</xsl:attribute>
	<xsl:attribute name="text-align">center</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="sub_header_block2">
	<xsl:attribute name="font-family">Frutiger45Light</xsl:attribute>
	<xsl:attribute name="color"> #FFFFFF</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="solid_blue_border">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-top-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-top-style">solid</xsl:attribute>
		<xsl:attribute name="border-top-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-bottom-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="page_count">
	<xsl:attribute name="font-family">Frutiger67BoldCn</xsl:attribute>
	<xsl:attribute name="font-size">7pt</xsl:attribute>
	<xsl:attribute name="color">#FFFFFF</xsl:attribute>
	<xsl:attribute name="text-align">left</xsl:attribute>
	<xsl:attribute name="display-align">center</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="page_count_disclaimer">
	<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="color">#FFFFFF</xsl:attribute>
	<xsl:attribute name="text-align">left</xsl:attribute>
	<xsl:attribute name="display-align">center</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="footer_disclaimer">
	<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="solid_white_bottom_border">		
		<xsl:attribute name="border-bottom-color">#DADBC8</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">1px</xsl:attribute>		
</xsl:attribute-set>

<xsl:attribute-set name="solid_white_border">
		<xsl:attribute name="border-start-color">#FFFFFF</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">1px</xsl:attribute>
		<xsl:attribute name="border-end-color">#FFFFFF</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">1px</xsl:attribute>		
		
		<xsl:attribute name="border-bottom-color">#FFFFFF</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">1px</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="footnotes">
	<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
	<xsl:attribute name="font-size">7pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="bold_Text">
	<xsl:attribute name="font-family">Frutiger45Light</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="bold_">
	<xsl:attribute name="font-family">Frutiger45Light</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="contents">
	<xsl:attribute name="font-family">Frutiger67BoldCn</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="static_content_font1">
<xsl:attribute name="font-family">Frutiger67BoldCn</xsl:attribute>
	<xsl:attribute name="color"> #FFFFFF</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="static_content_font2">
	<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
	<xsl:attribute name="color"> #FFFFFF</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="criteria_display_table">
	<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="analysis_review_display_table">
	<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="solid_white_end_border">		
		<xsl:attribute name="border-end-color">#FFFFFF</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">1px</xsl:attribute>		
</xsl:attribute-set>

<xsl:attribute-set name="solid_white_start_end_border">		
		<xsl:attribute name="border-start-color">#FFFFFF</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">1px</xsl:attribute>	
		<xsl:attribute name="border-end-color">#FFFFFF</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">1px</xsl:attribute>		
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="subsection_title_font">
	<xsl:attribute name="font-family">Frutiger45Light</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="color">#000000</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="contract_box_border_left_style">
	<xsl:attribute name="border-top-style">solid</xsl:attribute>
	<xsl:attribute name="border-left-style">solid</xsl:attribute>
	<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
	<xsl:attribute name="border-width">0.5px</xsl:attribute>
	<xsl:attribute name="border-color">#000000</xsl:attribute>
	<xsl:attribute name="padding-after">1px</xsl:attribute>
</xsl:attribute-set>
	
<xsl:attribute-set name="contract_box_border_right_style">
	<xsl:attribute name="border-top-style">solid</xsl:attribute>
	<xsl:attribute name="border-right-style">solid</xsl:attribute>
	<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
	<xsl:attribute name="border-width">0.5px</xsl:attribute>
	<xsl:attribute name="border-color">#000000</xsl:attribute>
	<xsl:attribute name="padding-after">1px</xsl:attribute>
</xsl:attribute-set>
	
<xsl:attribute-set name="top_line_border_style">
	<xsl:attribute name="border-top-style">solid</xsl:attribute>
	<xsl:attribute name="border-width">0.5px</xsl:attribute>
	<xsl:attribute name="border-color">#000000</xsl:attribute>
	<xsl:attribute name="padding-after">1px</xsl:attribute>
</xsl:attribute-set>
	
<xsl:attribute-set name="right_line_border_style">
	<xsl:attribute name="border-right-style">solid</xsl:attribute>
	<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
	<xsl:attribute name="border-width">0.5px</xsl:attribute>
	<xsl:attribute name="border-color">#000000</xsl:attribute>
	<xsl:attribute name="padding-after">1px</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="left_bottom_line_border_style">
	<xsl:attribute name="border-left-style">solid</xsl:attribute>
	<xsl:attribute name="border-bottom-style">solid</xsl:attribute>	
	<xsl:attribute name="border-width">0.5px</xsl:attribute>
	<xsl:attribute name="border-color">#000000</xsl:attribute>
	<xsl:attribute name="padding-after">1px</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="left_line_border_style">
	<xsl:attribute name="border-left-style">solid</xsl:attribute>	
	<xsl:attribute name="border-width">0.5px</xsl:attribute>
	<xsl:attribute name="border-color">#000000</xsl:attribute>
	<xsl:attribute name="padding-after">1px</xsl:attribute>
</xsl:attribute-set>
	
<xsl:attribute-set name="fund_instruction_box_border_header_style">
	<xsl:attribute name="border-top-style">solid</xsl:attribute>
	<xsl:attribute name="border-left-style">solid</xsl:attribute>
	<xsl:attribute name="border-right-style">solid</xsl:attribute>
	<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
	<xsl:attribute name="border-width">0.5px</xsl:attribute>
	<xsl:attribute name="border-color">#000000</xsl:attribute>
	<xsl:attribute name="padding-after">1px</xsl:attribute>
</xsl:attribute-set>
	
<xsl:attribute-set name="fund_instruction_box_border_style">
	<xsl:attribute name="border-left-style">solid</xsl:attribute>
	<xsl:attribute name="border-right-style">solid</xsl:attribute>
	<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
	<xsl:attribute name="border-width">0.5px</xsl:attribute>
	<xsl:attribute name="border-color">#000000</xsl:attribute>
	<xsl:attribute name="padding-after">1px</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="interim_title_font">
	<xsl:attribute name="font-family">Frutiger</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="font-size">16pt</xsl:attribute>
	<xsl:attribute name="color">#000000</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="interim_sub_title_font">
	<xsl:attribute name="font-family">Frutiger</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="font-size">11pt</xsl:attribute>
	<xsl:attribute name="color">#000000</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="interim_header_default_font">
	<xsl:attribute name="font-family">Frutiger</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
	<xsl:attribute name="color">#000000</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="interim_header_title_font">
	<xsl:attribute name="font-family">Frutiger</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="font-size">14pt</xsl:attribute>
	<xsl:attribute name="color">#000000</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="interim_sub_header_font">
	<xsl:attribute name="font-family">FrutigerSR</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="color">#000000</xsl:attribute>
	<xsl:attribute name="font-style">italic</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="interim_word_group_default_font">
	<xsl:attribute name="font-family">Frutiger</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="color">#000000</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="interim_footer_default_font">
	<xsl:attribute name="font-family">Frutiger</xsl:attribute>
	<xsl:attribute name="font-size">7pt</xsl:attribute>
	<xsl:attribute name="color">#000000</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="interim_docket_section_default_font">
	<xsl:attribute name="font-family">Frutiger</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="color">#000000</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="investment_ranking_header_font">
	<xsl:attribute name="font-family">Frutiger45Light</xsl:attribute>
	<xsl:attribute name="font-size">24pt</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="investment_ranking_sub_header_font">
	<xsl:attribute name="font-family">Frutiger45Light</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
	<xsl:attribute name="color">#CB5A27</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="quarter_end_dates">
	<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="annual_report_table_contents">
	<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="unmonitored_fund_name">
	<xsl:attribute name="font-family">Frutiger67BoldCn</xsl:attribute>
	<xsl:attribute name="font-size">10pt</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="unmonitored_fund_description">
	<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
	<xsl:attribute name="font-size">10pt</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="summary_page_table_contents">
	<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="invst_opt_rank_table_contents">
	<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="invst_opt_rank_table_bold_contents">
	<xsl:attribute name="font-family">Frutiger67BoldCn</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="disclaimer">
	<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="italic">
	<xsl:attribute name="font-family">Frutiger46LightItalic</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="line-height">1.5</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="word_group_font_italic">
	<xsl:attribute name="font-family">Frutiger46LightItalic</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="font-weight">Light</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
	<xsl:attribute name="line-height">1.5</xsl:attribute>
</xsl:attribute-set>

<!-- Global Variables -->
<xsl:variable name="imagePath">
	<xsl:value-of select="/annual_review_report/imagePath"/>
</xsl:variable>

<xsl:variable name="processingDate">
	<xsl:value-of select='/annual_review_report/processingDate'/>
</xsl:variable>

<xsl:variable name="isInterimReport">
	<xsl:value-of select='/annual_review_report/isInterim'/>
</xsl:variable>
	
<xsl:variable name="companyName">
	<xsl:value-of select='/annual_review_report/contractName'/>
</xsl:variable>
	
<xsl:variable name="contractNumber">
	<xsl:value-of select="/annual_review_report/contractNumber"/>
</xsl:variable>

<xsl:variable name="currentDate">
	<xsl:value-of select="/annual_review_report/currentDate"/>
</xsl:variable>

<xsl:variable name="companyId">
	<xsl:value-of select="/annual_review_report/companyId"/>
</xsl:variable>

<xsl:include href="ExecutiveSummaryofFindings.xsl"/>
<xsl:include href="YourIpsManagerCriteria.xsl"/>
<xsl:include href="AnnualReviewResults.xsl"/>
<xsl:include href="UnMonitoredFunds.xsl"/>
<xsl:include href="SummaryAndNextSteps.xsl"/>
<xsl:include href="NextSteps.xsl"/>
<xsl:include href="ImportantNotesWithInvstOptRankTable.xsl"/>
<xsl:include href="LastPage.xsl"/>
<xsl:include href="CoverPage.xsl"/>
<xsl:include href="ContractInvestmentForm.xsl" />
<xsl:include href="ContractInvestmentAdministrationForm.xsl" />


	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			
			<fo:layout-master-set>
				
				<fo:simple-page-master master-name="CommonFirstPageLayout" page-width="8.5in" page-height="11in"
					margin-top="18px" margin-bottom="18px" margin-left="18px">
				 <fo:region-body margin-left="115px" margin-bottom="58px"/>
				 <fo:region-before extent="22px" precedence="true" />
				 <fo:region-after extent="58px" precedence="true" />
				 <fo:region-start extent="115px" height="696px" background-color="#E2E3D5"/>
				</fo:simple-page-master>
				
				<fo:simple-page-master master-name="CommonRestPageLayout" page-width="8.5in" page-height="11in"
					margin-top="18px" margin-bottom="18px" margin-left="18px">
				 <fo:region-body margin-left="115px" margin-bottom="58px" margin-top="50px"/>
				 <fo:region-before extent="22px" precedence="true" />
				 <fo:region-after extent="58px" precedence="true" />
				 <fo:region-start extent="115px" height="696px" background-color="#E2E3D5"/>
				</fo:simple-page-master>
				
				<fo:page-sequence-master master-name="CommonPageLayout">
					<fo:repeatable-page-master-alternatives>
						<fo:conditional-page-master-reference master-reference="CommonFirstPageLayout" page-position="first" />
						<fo:conditional-page-master-reference master-reference="CommonRestPageLayout" page-position="rest" />
					</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
				
				<fo:simple-page-master master-name="AnnualReviewResultFirst" page-width="8.5in" page-height="11in"
					margin-top="18px" margin-bottom="18px" margin-left="18px">
				 <fo:region-body margin-left="26px" margin-bottom="72px"/>
				 <fo:region-before extent="22px" precedence="true" />
				 <fo:region-after extent="58px" precedence="true" />
				 <fo:region-start extent="115px" height="696px" background-color="#E2E3D5"/>
				</fo:simple-page-master>
				
				<fo:simple-page-master master-name="RestPageLayout" page-width="8.5in" page-height="11in"
					margin-top="18px" margin-bottom="18px" margin-left="18px">
				 <fo:region-body margin-left="26px" margin-top="90px" margin-bottom="75px"/>
				 <fo:region-before extent="22px" precedence="true" />
				 <fo:region-after extent="58px" precedence="true" />
				 <fo:region-start extent="115px" height="691px" background-color="#E2E3D5"/>
				</fo:simple-page-master>
						
				<fo:page-sequence-master master-name="AnnualReviewResultFirstAndRestPageLayout">
					<fo:repeatable-page-master-alternatives>
						<fo:conditional-page-master-reference master-reference="AnnualReviewResultFirst" page-position="first" />
						<fo:conditional-page-master-reference master-reference="RestPageLayout" page-position="rest" />
					</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
				
				<fo:page-sequence-master master-name="SummaryLayout">
					<fo:repeatable-page-master-alternatives>
						<fo:conditional-page-master-reference master-reference="SummaryFirstPageLayout" page-position="first" />
						<fo:conditional-page-master-reference master-reference="SummaryRestPagesLayout" page-position="rest" />
					</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
				
				<fo:simple-page-master master-name="SummaryFirstPageLayout" page-width="8.5in" page-height="11in"
					margin-top="18px" margin-bottom="18px" margin-left="18px">
					<fo:region-body margin-left="26px" margin-bottom="80px"/>
					<fo:region-before extent="22px" precedence="true" />
				 	<fo:region-after extent="58px" precedence="true"/>
					<fo:region-start extent="115px" height="691px" background-color="#E2E3D5"/>
				</fo:simple-page-master>
				
				<fo:simple-page-master master-name="SummaryRestPagesLayout" page-width="8.5in" page-height="11in"
					margin-top="18px" margin-bottom="18px" margin-left="18px">
					<fo:region-body margin-left="26px" margin-top="90px" margin-bottom="80px"/>
					<fo:region-before extent="22px" precedence="true" />
				 	<fo:region-after extent="58px" precedence="true"/>
					<fo:region-start extent="115px" height="691px" background-color="#E2E3D5"/>
				</fo:simple-page-master>
				
				<fo:simple-page-master master-name="LastPageLayout" page-width="8.5in" page-height="11in"
					margin-top="18px" margin-bottom="18px" margin-left="18px">
				 <fo:region-body  />
				 <fo:region-start extent="573px" background-color="#A9AB81"/>
				</fo:simple-page-master>
				
				<fo:simple-page-master master-name="CoverPageLayout" page-width="8.5in" page-height="11in"
					margin-top="24px" margin-bottom="25px" margin-left="18px">
				 <fo:region-body margin-bottom="135px" margin-top="146px"/>
				 <fo:region-before extent="148px" precedence="true" />
				 <fo:region-after extent="128px" precedence="true"/>
				 <fo:region-start extent="128px" height="468px" background-color="#C4C7AB"/>
				</fo:simple-page-master>
				
				<fo:page-sequence-master master-name="FootNotesLayoutWithInvstOptRank">
					<fo:repeatable-page-master-alternatives>
				 		<fo:conditional-page-master-reference master-reference="FootNotesFirstPageLayoutWithInvstOptRank" page-position="first" />
				 		<fo:conditional-page-master-reference master-reference="FootNotesRestPageLayoutWithInvstOptRank" page-position="rest" />
				 	</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
				
				<fo:simple-page-master master-name="FootNotesFirstPageLayoutWithInvstOptRank" page-width="8.5in" page-height="11in"
					margin-top="18px" margin-bottom="18px" margin-left="18px" >
				 <fo:region-body margin-left="26px" margin-bottom="58px" margin-right="28px" />
				 <fo:region-before extent="22px" precedence="true" />
				 <fo:region-after extent="58px" precedence="true" />
				 <fo:region-start extent="115px" height="696px" background-color="#E2E3D5"/>
				</fo:simple-page-master>
				
				<fo:simple-page-master master-name="FootNotesRestPageLayoutWithInvstOptRank" page-width="8.5in" page-height="11in"
					margin-top="18px" margin-bottom="18px" margin-left="18px">
				 <fo:region-body margin-left="26px" margin-bottom="58px" margin-top="50px"/>
				 <fo:region-before extent="22px" precedence="true" />
				 <fo:region-after extent="58px" precedence="true" />
				 <fo:region-start extent="115px" height="696px" background-color="#E2E3D5"/>
				</fo:simple-page-master>
				
				<fo:page-sequence-master master-name="FootNotesWithOutInvstOptRankLayout">
					<fo:repeatable-page-master-alternatives>
				 		<fo:conditional-page-master-reference master-reference="FootNotesFirstPageLayoutWithOutInvstOptRank" page-position="first" />
				 		<fo:conditional-page-master-reference master-reference="FootNotesRestPageLayoutWithOutInvstOptRank" page-position="rest" />
				 	</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
				
				<fo:simple-page-master master-name="FootNotesFirstPageLayoutWithOutInvstOptRank" page-width="8.5in" page-height="11in"
					margin-top="18px" margin-bottom="18px" margin-left="18px" >
				 <fo:region-body margin-left="128px" margin-bottom="58px" margin-right="28px" margin-top="20px" column-count="2"/>
				 <fo:region-before extent="22px" precedence="true" />
				 <fo:region-after extent="58px" precedence="true" />
				 <fo:region-start extent="115px" height="696px" background-color="#E2E3D5"/>
				</fo:simple-page-master>
				
				<fo:simple-page-master master-name="FootNotesRestPageLayoutWithOutInvstOptRank" page-width="8.5in" page-height="11in"
					margin-top="18px" margin-bottom="18px" margin-left="18px">
				 <fo:region-body margin-left="128px" margin-bottom="58px" margin-top="20px" column-count="2"/>
				 <fo:region-before extent="22px" precedence="true" />
				 <fo:region-after extent="58px" precedence="true" />
				 <fo:region-start extent="115px" height="696px" background-color="#E2E3D5"/>
				</fo:simple-page-master>
				
				<fo:simple-page-master master-name="ContractInvestmentFormLayout"
					page-width="8.5in" page-height="11in" margin-top="18px"
					margin-bottom="18px" margin-left="35px" margin-right="35px">
					<fo:region-body extent="22px" precedence="true"/>
					<fo:region-after extent="45px" precedence="true"/>
				</fo:simple-page-master>
								
				<fo:page-sequence-master master-name="ContractInvestmentAdministrationFormLayout">
					<fo:repeatable-page-master-alternatives>
				 		<fo:conditional-page-master-reference master-reference="ContractInvestmentAdministrationFirstFormLayout" page-position="first" />
				 		<fo:conditional-page-master-reference master-reference="ContractInvestmentAdministrationRestFormLayout" page-position="rest" />
				 	</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
				
				<fo:simple-page-master master-name="ContractInvestmentAdministrationFirstFormLayout"
					page-width="8.5in" page-height="11in" margin-top="18px"
					margin-bottom="18px" margin-left="35px" margin-right="35px">
					<fo:region-body margin-bottom="50px" precedence="true"/>
					<fo:region-after extent="45px" precedence="true"/>
				</fo:simple-page-master>
				
				<fo:simple-page-master master-name="ContractInvestmentAdministrationRestFormLayout"
					page-width="8.5in" page-height="11in" margin-top="18px"
					margin-bottom="18px" margin-left="35px" margin-right="35px">
					<fo:region-body margin-top="50px" margin-bottom="50px" precedence="true"/>
					<fo:region-after extent="45px" precedence="true"/>
				</fo:simple-page-master>
				
				<fo:simple-page-master master-name="InvestmentOptionRankingPageLayout" page-width="8.5in" page-height="11in"
					margin-top="18px" margin-bottom="18px" margin-left="18px">
				 <fo:region-body margin-left="18px" margin-bottom="80px"/>
				 <fo:region-before extent="22px" precedence="true" />
				 <fo:region-after extent="58px" precedence="true" />
				 <fo:region-start extent="115px" height="691px" background-color="#E2E3D5"/>
				</fo:simple-page-master>
				
				
			</fo:layout-master-set>	
			
			<xsl:call-template name="CoverPageTemplate"/>
			<xsl:call-template name="ExecutiveSummaryTemplate"/>
			<xsl:call-template name="YourIpsManagerCriteriaTemplate"/>
			<xsl:call-template name="AnnualReviewResultsTemplate"/>
			<xsl:call-template name="UnMonitoredFunds"/>
			
			<xsl:if test="annual_review_report/ipsFundInstructionInfo/ips_fund_instruction_info">
				<xsl:call-template name="SummaryAndNextStepsTemplate"/> 
			</xsl:if>
						
			<xsl:if test="annual_review_report/nextStepSectionAvailable = 'true'">
				<xsl:call-template name="NextStepsSection"/>
			</xsl:if>
			<xsl:call-template name="ImportantNotesWithInvstOptRankTemplate"/>
			<xsl:call-template name="LastPage"/>
			
			<xsl:if test="annual_review_report/isInterim = 'true' and annual_review_report/isValidFundInstAvailableForAdminForm = 'true'">
				<xsl:call-template name="ContractInvestmentFormTemplate" />
				<xsl:call-template name="ContractInvestmentAdministrationFormTemplate" />
			</xsl:if>
			
		</fo:root>
		
	</xsl:template>
</xsl:stylesheet>