<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" >
	
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
	
	<xsl:attribute-set name="interim_word_group_default_font_1">
		<xsl:attribute name="font-family">Frutiger45Light</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
		<xsl:attribute name="color">#000000</xsl:attribute>
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
		<xsl:value-of select="/cofid_review_report/imagePath"/>
	</xsl:variable>
		
	<xsl:variable name="companyName">
		<xsl:value-of select='/cofid_review_report/contractName'/>
	</xsl:variable>
		
	<xsl:variable name="contractNumber">
		<xsl:value-of select="/cofid_review_report/contractNumber"/>
	</xsl:variable>
	
	<xsl:template name="checkBox">
		<fo:inline padding-start="0.3cm" padding-end="0.1cm">
			<fo:external-graphic content-height="7px"
				content-width="7px">
				<xsl:attribute name="src">
					<xsl:value-of select="concat($imagePath, 'plaincheckboxSmall.gif')" />
				</xsl:attribute>
			</fo:external-graphic>
		</fo:inline> 
    </xsl:template>
    
    <xsl:template match="/">
    	<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    		<fo:layout-master-set>
    			<fo:page-sequence-master master-name="Cofid338FundReplacementFormLayout">
					<fo:repeatable-page-master-alternatives>
				 		<fo:conditional-page-master-reference master-reference="Cofid338FundReplacementFirstFormLayout" page-position="first" />
				 		<fo:conditional-page-master-reference master-reference="Cofid338FundReplacementRestFormLayout" page-position="rest" />
				 	</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
				
				<fo:simple-page-master master-name="Cofid338FundReplacementFirstFormLayout"
					page-width="8.5in" page-height="11in" margin-top="18px"
					margin-bottom="18px" margin-left="35px" margin-right="35px">					
					<fo:region-body margin-top="70px" margin-bottom="50px" precedence="true"/>
					<fo:region-before extent="45px" precedence="true"/>
					<fo:region-after extent="45px" precedence="true"/>
				</fo:simple-page-master>
				
				<fo:simple-page-master master-name="Cofid338FundReplacementRestFormLayout"
					page-width="8.5in" page-height="11in" margin-top="18px"
					margin-bottom="18px" margin-left="35px" margin-right="35px">
					<fo:region-body margin-top="70px" margin-bottom="50px" precedence="true"/>
					<fo:region-before extent="45px" precedence="true"/>
					<fo:region-after extent="45px" precedence="true"/>
				</fo:simple-page-master>
    		
    		
    		<fo:page-sequence-master master-name="Cofid338FundReplacementsLayout">
					<fo:repeatable-page-master-alternatives >
						<fo:conditional-page-master-reference master-reference="Cofid338FundReplacementsLayout_odd"  odd-or-even="odd" />
						<fo:conditional-page-master-reference master-reference="Cofid338FundReplacementsLayout_even" odd-or-even="even" />
					</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
    		
    		<fo:simple-page-master master-name="Cofid338FundReplacementsLayout_odd"
					page-width="8.5in" page-height="11in" margin-top="18px"
					margin-bottom="18px" margin-left="35px" margin-right="35px" >
					<fo:region-body margin-top="50px" margin-bottom="50px" precedence="true"/>					
					<fo:region-after extent="45px" precedence="true"/>
				</fo:simple-page-master>
				
				<fo:simple-page-master master-name="Cofid338FundReplacementsLayout_even"
					page-width="8.5in" page-height="11in" margin-top="18px"
					margin-bottom="18px" margin-left="35px" margin-right="35px" >
					<fo:region-body margin-top="50px" margin-bottom="50px" precedence="true"/>					
					<fo:region-after extent="45px" precedence="true"/>
				</fo:simple-page-master>
    		</fo:layout-master-set>
    		
        
        <fo:page-sequence master-reference="Cofid338FundReplacementFormLayout" initial-page-number="1"  > 
        	<!-- Footer -->
        	
        	<fo:static-content flow-name="xsl-region-before">
                <fo:table table-layout="fixed" width="100%" padding-top="15px">
	                    <fo:table-body >
	                        <fo:table-row height="400px" >
	                         <fo:table-cell width="150px" >
	                                <fo:block>
	                                    <fo:external-graphic content-width="150px" content-height="300px">
											<xsl:attribute name="src">
	                                            <xsl:value-of select="concat($imagePath,'JH_SIG_black_300dpi.jpg')"/>
	                                        </xsl:attribute>
	                                    </fo:external-graphic>
	                                </fo:block>
	                            </fo:table-cell>
	                            <fo:table-cell width="380px" >
	                        		<fo:block padding-top="6px">
			                        	<fo:table table-layout="fixed" width="100%">
					                   		<fo:table-body>												
						                     	<fo:table-row display-align="center" height="50px">
						                     		<fo:table-cell >
														<fo:block xsl:use-attribute-sets="interim_header_title_font" start-indent="10" text-align="right">
															<xsl:value-of select="/cofid_review_report/pageTitle"/>					
														</fo:block>
														<xsl:if test="/cofid_review_report/companyId = '094'">
															<fo:block xsl:use-attribute-sets="interim_sub_header_font" font-size="9pt" padding-top="10pt">
																<xsl:value-of select="/cofid_review_report/sectionHeading"/>		
															
															  <!--   (hereinafter referred to as John Hancock New York or The Company)-->
														   </fo:block>
													   </xsl:if>
						                         	</fo:table-cell>
					                         	</fo:table-row>					                         	
					                        </fo:table-body>
					                      </fo:table>
				                      </fo:block>
			                      </fo:table-cell>	                            
	                        </fo:table-row>
	                	</fo:table-body>
	            	</fo:table>
	                                
	                                </fo:static-content>
			<fo:static-content flow-name="xsl-region-after">
                <fo:table table-layout="fixed" width="100%">
	                    <fo:table-body>
	                        <fo:table-row border-bottom-style="solid" border-bottom-width="0.5px">
	                            <fo:table-cell padding-after="2px" width="180px">
	                                <fo:block xsl:use-attribute-sets="interim_docket_section_default_font" >
	                                	<xsl:value-of select="/cofid_review_report/formAndDocket"/>
	                                	
	                                </fo:block>
	                            </fo:table-cell>
	                            <fo:table-cell padding-after="2px" width="310px">
	                                <fo:block xsl:use-attribute-sets="interim_docket_section_default_font" >
	                                    <xsl:value-of select="/cofid_review_report/generalDisclosure"/>
	                                </fo:block>
	                            </fo:table-cell>
	                              <fo:table-cell  width="60px">
	                                <fo:block xsl:use-attribute-sets="interim_docket_section_default_font" text-align="right" >
	                                	 Page <fo:page-number/> of  <fo:page-number-citation-last  ref-id="pageFooter"/>
	                                </fo:block>
	                            </fo:table-cell>
	                        </fo:table-row>
	                        <fo:table-row>
	                            <fo:table-cell number-columns-spanned = "3" >
	                                <fo:block xsl:use-attribute-sets="interim_footer_default_font">
	                                     <xsl:value-of select="/cofid_review_report/footerText"/>
	                                </fo:block>
	                            </fo:table-cell>
	                        </fo:table-row>
	                    </fo:table-body>
                	</fo:table>
            </fo:static-content>
            
            <fo:flow flow-name="xsl-region-body" text-align="justify" padding-top="10px">            
	            <fo:block>
	            	
	            	<fo:table table-layout="fixed" width="100%" space-before="20px" padding-top="5px">
                   		<fo:table-body>
	                     	<fo:table-row>
	                     		<fo:table-cell>
		                           <fo:block xsl:use-attribute-sets="interim_word_group_default_font" padding-top="10px" >
		                              
 									<xsl:value-of select="/cofid_review_report/sectionHeaderContent"/>
		                           </fo:block>
	                         	</fo:table-cell>
                         	</fo:table-row>
                        </fo:table-body>
                      </fo:table>
	            	
	            	<fo:table table-layout="fixed" width="100%" space-before="10px">
                   		<fo:table-body>
	                     	<fo:table-row>
	                     		<fo:table-cell>
		                           <fo:block xsl:use-attribute-sets="interim_word_group_default_font" font-weight="bold">
		                               Fax the completed form to our toll free number 1-866-377-9577<br/><br/>
		                           </fo:block>
	                         	</fo:table-cell>
                         	</fo:table-row>
                        </fo:table-body>
                      </fo:table>
                      <fo:table table-layout="fixed" width="100%" keep-with-next.within-page="always">
                   		<fo:table-body>
                   			<fo:table-row>                         		
                     			<fo:table-cell>
                           			<fo:block border-width="0.5px" xsl:use-attribute-sets="line_border_style"> 
                           			<br/>                              			
                           			</fo:block>
                         		</fo:table-cell>                         		
                         	</fo:table-row>
                   		</fo:table-body>
                      </fo:table>	            	 
                   	  <fo:table table-layout="fixed" width="100%" keep-with-next.within-page="always">
                   		<fo:table-body>
	                     	<fo:table-row>
                   				<fo:table-cell width="5px">
                   					<fo:block xsl:use-attribute-sets="interim_title_font" padding-top="3px"> 
                   						1.
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell>
                           			<fo:block start-indent="10px" padding-top="5px" xsl:use-attribute-sets="interim_sub_title_font"> 
                   						General Information<br/><br/>
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row>                   			
                   		</fo:table-body>
                   	  </fo:table>
                   	
	                   <fo:table table-layout="fixed" width="100%">
                   		<fo:table-body>
                   			<fo:table-row height="10px" keep-with-previous="always">
                   			
	                           <fo:table-cell width="67px">
                           			<fo:block start-indent="1px" padding-top="15px"  xsl:use-attribute-sets="interim_word_group_default_font" > 
                   						<b>The Trustee of</b>  
                           			</fo:block>
                           		</fo:table-cell> 
	                     		<fo:table-cell width="225px"  padding-top="15px" xsl:use-attribute-sets="line_border_style">
	                                <fo:block xsl:use-attribute-sets="interim_word_group_default_font" start-indent="1px"  font-weight="bold">
	                                    <xsl:value-of select="$companyName"/> 
	                                </fo:block>
	                            </fo:table-cell>
	                            <fo:table-cell>
                           			<fo:block width="100px" start-indent="5px" padding-top="15px" xsl:use-attribute-sets="interim_word_group_default_font"> 
                   						<b>Plan (the “Plan”)</b>  
                           			</fo:block>
                           		</fo:table-cell> 
	                            
	                            
								<!-- <fo:table-cell width="12px" padding-start="1px">
	                                <fo:block space-after="2px" xsl:use-attribute-sets="interim_word_group_default_font">
	                                    
	                                </fo:block>	                                
	                            </fo:table-cell> -->
	                            <fo:table-cell width="150px" padding-top="15px" xsl:use-attribute-sets="line_border_style">
	                                <fo:block xsl:use-attribute-sets="interim_word_group_default_font" font-weight="bold">
	                                    <xsl:value-of select="$contractNumber"/>
	                                </fo:block>	                                
	                            </fo:table-cell>
	                     	</fo:table-row>
	                     	<fo:table-row height="10px" keep-with-previous="always">
	                     		<!-- <fo:table-cell padding-start="1px" padding-right="8px">
	                                <fo:block space-after="2px" xsl:use-attribute-sets="interim_footer_default_font">
	                                    Contractholder Name
	                                </fo:block>
	                                
	                            </fo:table-cell>
								
	                            <fo:table-cell padding-start="1px" padding-right="8px">
	                                <fo:block space-after="2px" xsl:use-attribute-sets="interim_word_group_default_font">
	                                    
	                                </fo:block>
	                            </fo:table-cell>
	                            <fo:table-cell padding-start="1px">
	                                <fo:block space-after="2px" xsl:use-attribute-sets="interim_footer_default_font">
	                                    Contract Number
	                                </fo:block>
	                                
	                            </fo:table-cell> -->
	                             <fo:table-cell>
                           			<fo:block start-indent="1px"  xsl:use-attribute-sets="interim_footer_default_font" > 
                   						Contractholder Name
                           			</fo:block>
                           		</fo:table-cell> 
	                     		<fo:table-cell >
	                                <fo:block xsl:use-attribute-sets="interim_word_group_default_font">
	                                    
	                                </fo:block>
	                            </fo:table-cell>
	                            <fo:table-cell>
                           			<fo:block start-indent="5px" padding-top="15px" xsl:use-attribute-sets="interim_word_group_default_font"> 
                   						
                           			</fo:block>
                           		</fo:table-cell> 
	                            <fo:table-cell padding-start="1px" >
	                                <fo:block xsl:use-attribute-sets="interim_footer_default_font" >
	                                    Contract Number
	                                </fo:block>	                                
	                            </fo:table-cell>
	                     	</fo:table-row>
	                     </fo:table-body>
	                  </fo:table>
					
	                  <fo:table table-layout="fixed" width="100%" space-before="20px" keep-with-next.within-page="always">
                   		<fo:table-body>
                   			<fo:table-row>                         		
                     			<fo:table-cell>
                           			<fo:block border-width="0.5px" xsl:use-attribute-sets="line_border_style"> 
                           			<br/>                              			
                           			</fo:block>
                         		</fo:table-cell>                         		
                         	</fo:table-row>
                   		</fo:table-body>
                      </fo:table>	
                       <fo:table table-layout="fixed" width="100%" keep-with-next.within-page="always">
                   		<fo:table-body>
	                     	<fo:table-row>
                   				<fo:table-cell width="5px">
                   					<fo:block xsl:use-attribute-sets="interim_title_font" padding-top="3px"> 
                   						2.
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell>
                           			<fo:block start-indent="10px" padding-top="5px" xsl:use-attribute-sets="interim_sub_title_font"> 
                   						Implementation of Wilshire’s Investment Line-Up 
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row>                   			
                   		</fo:table-body>
                   	  </fo:table>
                   	  <fo:table table-layout="fixed" width="100%" space-before="10px">
                   		<fo:table-body>
	                     	<fo:table-row>
	                     		<fo:table-cell>
		                           <fo:block xsl:use-attribute-sets="interim_word_group_default_font">
		                             
		                      <xsl:value-of select="/cofid_review_report/changeYourProfileContent"/> <br/><br/>
		                           </fo:block>
	                         	</fo:table-cell>
                         	</fo:table-row>
                        </fo:table-body>
                      </fo:table>
                      <fo:table table-layout="fixed" width="100%" space-before="10px">
                   	  	
                   		<fo:table-body>
                   			<fo:table-row>
                   				<fo:table-cell width="100px">
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font" font-weight="bold" start-indent="15px" space-after="20px">
                   						 Effective Date:   
                   					</fo:block>
                   				</fo:table-cell>                   				
                   				<fo:table-cell width="18px" xsl:use-attribute-sets="left_line_border_style" start-indent="55px" >
                   					<fo:block >
                   					</fo:block>
                   				</fo:table-cell> 
                   				<fo:table-cell width="18px" >
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell>
								
								<fo:table-cell width="18px" >
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="18px" xsl:use-attribute-sets="left_line_border_style">
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell> 
								<fo:table-cell width="18px" >
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="18px" xsl:use-attribute-sets="left_line_border_style">
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell> 
								<fo:table-cell width="18px" >
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" >
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" >
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="left_line_border_style">
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell> 
                   			</fo:table-row>
							<fo:table-row>
                   				<fo:table-cell width="60px">
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font">
                   						
                   					</fo:block>
                   				</fo:table-cell>                   				
                   				<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>                     				
                   			</fo:table-row>                   			
                   		</fo:table-body>
                   	  </fo:table>
					  <fo:table table-layout="fixed" width="100%">
						<fo:table-body>
							<fo:table-row>
                   				<fo:table-cell width="90px">
                   					<fo:block start-indent="115px" xsl:use-attribute-sets="interim_footer_default_font"> 
                   						<b>Month</b>
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="80px">
                   					<fo:block start-indent="78px" xsl:use-attribute-sets="interim_footer_default_font"> 
                   						<b>Day</b>
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="70px">
                   					<fo:block start-indent="40px" xsl:use-attribute-sets="interim_footer_default_font"> 
                   						<b>Year</b> <br/><br/>
                           			</fo:block>
                   				</fo:table-cell>
                   			</fo:table-row>
						</fo:table-body>
					  </fo:table>
                      
                      <fo:table table-layout="fixed" width="100%" keep-with-next.within-page="always">
                   		<fo:table-body>
                   			<fo:table-row>                         		
                     			<fo:table-cell>
                           			<fo:block border-width="0.5px" xsl:use-attribute-sets="line_border_style"> 
                           			<br/>                              			
                           			</fo:block>
                         		</fo:table-cell>                         		
                         	</fo:table-row>
                   		</fo:table-body>
                      </fo:table>
                      <fo:table table-layout="fixed" width="100%" keep-with-next.within-page="always">
                   		<fo:table-body>
	                     	<fo:table-row>
                   				<fo:table-cell width="5px">
                   					<fo:block xsl:use-attribute-sets="interim_title_font" padding-top="3px"> 
                   						3.
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell>
                           			<fo:block start-indent="10px" padding-top="5px" xsl:use-attribute-sets="interim_sub_title_font"> 
                   						Default Investment Option
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row>                   			
                   		</fo:table-body>
                   	  </fo:table>
                   	  <fo:table table-layout="fixed" width="100%" space-before="10px">
                   		<fo:table-body>
	                     	<fo:table-row>
	                     		<fo:table-cell>
		                           <fo:block xsl:use-attribute-sets="interim_word_group_default_font">
		                              <!--  The default investment option(s) selected for your Contract by Wilshire is the target date suite applicable to your profile. When a suite of Target Date Portfolios is selected or used as a default investment option under your Contract, all Portfolios in the suite are used as the default investment option. Defaulted participant balances will be placed in the Portfolio corresponding to, or that is closest to, the year in which the defaulted participant attains age 67. If date of birth information is not provided for a defaulted participant, the participant's contributions will be invested in the most conservative Portfolio in the suite. If a date of birth is later provided for such participant, then all contributions previously default invested in the most conservative Portfolio in the suite, as well as all future default invested contributions, will be invested in the Portfolio determined as described above.<br/><br/> -->
		                       <xsl:value-of select="/cofid_review_report/defalutInvestmentOption"/><br/><br/> 
		                           </fo:block>
	                         	</fo:table-cell>
                         	</fo:table-row>
                        </fo:table-body>
                      </fo:table>
                      
                      <fo:table table-layout="fixed" width="100%" keep-with-next.within-page="always">
                   		<fo:table-body>
                   			<fo:table-row>                         		
                     			<fo:table-cell>
                           			<fo:block border-width="0.5px" xsl:use-attribute-sets="line_border_style"> 
                           			<br/>                              			
                           			</fo:block>
                         		</fo:table-cell>                         		
                         	</fo:table-row>
                   		</fo:table-body>
                      </fo:table>
                      <fo:table table-layout="fixed" width="100%" keep-with-next.within-page="always">
                   		<fo:table-body>
	                     	<fo:table-row>
                   				<fo:table-cell width="5px">
                   					<fo:block xsl:use-attribute-sets="interim_title_font" padding-top="3px"> 
                   						4.
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell>
                           			<fo:block start-indent="10px" padding-top="5px" xsl:use-attribute-sets="interim_sub_title_font"> 
                   						Directions for Stable Value Fund Transfer
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row>                   			
                   		</fo:table-body>
                   	  </fo:table>
                   	  <fo:table table-layout="fixed" width="100%" space-before="10px">
                   		<fo:table-body>
	                     	<fo:table-row>
	                     		<fo:table-cell>
		                           <fo:block xsl:use-attribute-sets="interim_word_group_default_font_1">
		                                <xsl:value-of select="/cofid_review_report/stableValueFundTransferText1"/>
		                                <fo:block padding-after="0.1cm" >
		                                <xsl:call-template name="checkBox" />
										<xsl:value-of select="/cofid_review_report/stableValueFundTransferText2"/>
										</fo:block>
										<fo:block padding-before="0.1cm" >&#160;</fo:block>
										<fo:block padding-before="0.1cm" >
										<xsl:call-template name="checkBox" />
										<xsl:value-of select="/cofid_review_report/stableValueFundTransferText3"/>
										</fo:block>
										<fo:block padding-after="0.3cm" >&#160;</fo:block>
									 </fo:block>
	                         	</fo:table-cell>
                         	</fo:table-row>
                        </fo:table-body>
                      </fo:table>
                      
                      <fo:table table-layout="fixed" width="100%" keep-with-next.within-page="always">
                   		<fo:table-body>
                   			<fo:table-row>                         		
                     			<fo:table-cell>
                           			<fo:block border-width="0.5px" xsl:use-attribute-sets="line_border_style"> 
                           			<br/>                              			
                           			</fo:block>
                         		</fo:table-cell>                         		
                         	</fo:table-row>
                   		</fo:table-body>
                      </fo:table>
                      <fo:table table-layout="fixed" width="100%" keep-with-next.within-page="always">
                   		<fo:table-body>
	                     	<fo:table-row>
                   				<fo:table-cell width="5px">
                   					<fo:block xsl:use-attribute-sets="interim_title_font" padding-top="3px"> 
                   						5.
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell>
                           			<fo:block start-indent="10px" padding-top="5px" xsl:use-attribute-sets="interim_sub_title_font"> 
                   						Payment of Wilshire’s Fees for the Wilshire 3(38) Investment Management Service
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row>                   			
                   		</fo:table-body>
                   	  </fo:table>
                   	  <fo:table table-layout="fixed" width="100%" space-before="10px">
                   		<fo:table-body>
	                     	<fo:table-row>
	                     		<fo:table-cell>
		                           <fo:block xsl:use-attribute-sets="interim_word_group_default_font">
		                            
									 <xsl:value-of select="/cofid_review_report/paymentofWilshire"/> <br/><br/>
		                           </fo:block>
	                         	</fo:table-cell>
                         	</fo:table-row>
                        </fo:table-body>
                      </fo:table>
                      
                        <fo:table table-layout="fixed" width="100%" keep-with-next.within-page="always">
                   		<fo:table-body>
                   			<fo:table-row>                         		
                     			<fo:table-cell>
                           			<fo:block border-width="0.5px" xsl:use-attribute-sets="line_border_style"> 
                           			<br/>                              			
                           			</fo:block>
                         		</fo:table-cell>                         		
                         	</fo:table-row>
                   		</fo:table-body>
                      </fo:table>
                      <fo:table table-layout="fixed" width="100%" keep-with-next.within-page="always">
                   		<fo:table-body>
	                     	<fo:table-row>
                   				<fo:table-cell width="5px">
                   					<fo:block xsl:use-attribute-sets="interim_title_font" padding-top="3px"> 
                   						6.
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell>
                           			<fo:block start-indent="10px" padding-top="5px" xsl:use-attribute-sets="interim_sub_title_font"> 
                   						Additional Terms &amp; Conditions Applicable to the Wilshire 3(38) Investment Management Service
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row>                   			
                   		</fo:table-body>
                   	  </fo:table>
	<fo:table table-layout="fixed" width="100%" space-before="10px">
		<fo:table-body>
			<fo:table-row>
				<fo:table-cell>
					<fo:block xsl:use-attribute-sets="interim_word_group_default_font">
						 <xsl:value-of select="/cofid_review_report/additionalTermsAndConditions" /> 

				</fo:block>
				<fo:block padding-after="0.3cm" >&#160;</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
                          
                                       	 
                   	                       
                       <fo:table table-layout="fixed" width="100%" keep-with-next.within-page="always">
                   		<fo:table-body>
                   			<fo:table-row>                         		
                     			<fo:table-cell>
                           			<fo:block border-width="0.5px" xsl:use-attribute-sets="line_border_style"> 
                           			<br/>                              			
                           			</fo:block>
                         		</fo:table-cell>                         		
                         	</fo:table-row>
                   		</fo:table-body>
                      </fo:table>
                      <fo:table table-layout="fixed" width="100%" keep-with-next.within-page="always">
                   		<fo:table-body>
	                     	<fo:table-row>
                   				<fo:table-cell width="5px">
                   					<fo:block xsl:use-attribute-sets="interim_title_font" padding-top="3px"> 
                   						7.
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell>
                           			<fo:block start-indent="10px" padding-top="5px" xsl:use-attribute-sets="interim_sub_title_font"> 
                   						Plan Trustee Authorization/Signature
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row>                   			
                   		</fo:table-body>
                   	  </fo:table>
                   	  <fo:table table-layout="fixed" width="100%" space-before="10px" keep-with-next.within-page="always" keep-together.within-page="always">
                   		<fo:table-body>
	                     	<fo:table-row>
	                     		<fo:table-cell>
		                           <fo:block xsl:use-attribute-sets="interim_word_group_default_font">
		                             
		                           <xsl:value-of select="cofid_review_report/authorizationText" />
		                           </fo:block>
	                         	</fo:table-cell>
                         	</fo:table-row>
                        </fo:table-body>
                      </fo:table>
                      
                      <fo:block padding-before="25px" keep-with-previous.within-page="always">
                   	  <fo:table table-layout="fixed" width="100%">
                   		<fo:table-body>
                   			 <fo:table-row>
                   				<fo:table-cell width="220px" padding-right="12px">
                   					<fo:block xsl:use-attribute-sets="interim_footer_default_font top_line_border_style">
                   						Signature of Trustee/Authorized Named Fiduciary 
                   					</fo:block>
                   				</fo:table-cell>                   				
                   				<fo:table-cell width="210px" padding-right="12px">
                   					<fo:block text-align="left" xsl:use-attribute-sets="interim_footer_default_font top_line_border_style">
                   						Print Name
                   					</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="110px">
                   					<fo:block text-align="left" xsl:use-attribute-sets="interim_footer_default_font top_line_border_style">
                   						Date<br/><br/>
                   					</fo:block>
                   				</fo:table-cell>                   				
                   			</fo:table-row> 
                   		</fo:table-body>
                   	  </fo:table>
	            </fo:block>
              </fo:block>
                   	
	        </fo:flow>
        </fo:page-sequence>
        
        
        <fo:page-sequence master-reference="Cofid338FundReplacementsLayout" id="pageFooter"> 
        	<!-- Footer -->
        	<fo:static-content flow-name="xsl-region-after">
                <fo:table table-layout="fixed" width="100%">
	                    <fo:table-body>
	                        <fo:table-row border-bottom-style="solid" border-bottom-width="0.5px">
	                            <fo:table-cell padding-after="2px" width="180px">
	                                <fo:block xsl:use-attribute-sets="interim_docket_section_default_font" >
	                                	<xsl:value-of select="/cofid_review_report/formAndDocket"/>
	                                	
	                                </fo:block>
	                            </fo:table-cell>
	                            <fo:table-cell padding-after="2px" width="310px">
	                                <fo:block xsl:use-attribute-sets="interim_docket_section_default_font" >
	                                	<xsl:value-of select="/cofid_review_report/generalDisclosure"/>
	                                </fo:block>
	                            </fo:table-cell>
	                              <fo:table-cell  width="60px">
	                                <fo:block xsl:use-attribute-sets="interim_docket_section_default_font" text-align="right">
	                                	 Page <fo:page-number/> of  <fo:page-number-citation-last  ref-id="pageFooter"/>
	                                </fo:block>
	                            </fo:table-cell>
	                        </fo:table-row>
	                        <fo:table-row>
	                            <fo:table-cell number-columns-spanned = "3" >
	                                <fo:block xsl:use-attribute-sets="interim_footer_default_font">
	                                     <xsl:value-of select="/cofid_review_report/footerText"/>
	                                </fo:block>
	                            </fo:table-cell>
	                        </fo:table-row>
	                    </fo:table-body>
                	</fo:table>
            </fo:static-content>
            
            <fo:flow flow-name="xsl-region-body">            
	            <fo:block>
	            
	            <fo:table table-layout="fixed" width="100%" space-before="10px">
                   		<fo:table-body>
	                     	<fo:table-row>
	                     		<fo:table-cell>
		                           <fo:block  start-indent="3px" padding-top="3px" xsl:use-attribute-sets="interim_sub_title_font" font-weight="bold">
		                              Initial Investment Line-Up Changes (<xsl:value-of select="/cofid_review_report/wilshire338ProfileName"/> Profile)
		                           </fo:block>
	                         	</fo:table-cell>
                         	</fo:table-row>
                        </fo:table-body>
                      </fo:table> 	 
                   	  
                   	  <fo:table table-layout="fixed" width="100%" space-before="10px">
                   		<fo:table-body>
	                     	<fo:table-row>
	                     		<fo:table-cell>
		                           <fo:block xsl:use-attribute-sets="interim_word_group_default_font" text-align="justify">
		                              <!-- The following Fund(s) will be removed and replaced or added to the Plan’s Fund line-up on the effective date indicated in the General Information section of this document. To learn more about the replacement Fund(s), such as its objectives, risks, performance, fees and expenses, go to the Investment Options page of John Hancock’s participant website. -->
		                           <xsl:value-of select="cofid_review_report/replacementFundText" />
		                           </fo:block>
	                         	</fo:table-cell>
                         	</fo:table-row>
                        </fo:table-body>
                      </fo:table>
	            	<fo:table table-layout="fixed" width="100%" space-before="5px">
                   	  	
						 <fo:table-column column-width="190px" /> 
                   	  	<fo:table-column column-width="81px" />
						 <fo:table-column column-width="190px" /> 
						<fo:table-column column-width="81px" />
                   		<fo:table-header>
                   			<fo:table-row height="10px" display-align="center">
								<fo:table-cell  padding-start="1px" padding-right="4px" xsl:use-attribute-sets="fund_instruction_box_border_header_style" >
                   					<fo:block start-indent="10px" padding-top="1px" padding-bottom="3px" text-align="left" xsl:use-attribute-sets="interim_word_group_default_font" font-weight="bold">
                   						Current Fund
                   					</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell padding-start="1px"  padding-right="4px" xsl:use-attribute-sets="fund_instruction_box_border_header_style">
                   					<fo:block start-indent="5px" padding-top="1px" padding-bottom="3px" text-align="center" xsl:use-attribute-sets="interim_word_group_default_font" font-weight="bold">
                   						Fund Code
                   					</fo:block>
                   					</fo:table-cell>
                   					<fo:table-cell  padding-start="1px" padding-right="4px" xsl:use-attribute-sets="fund_instruction_box_border_header_style" >
                   					<fo:block start-indent="10px" padding-top="1px" padding-bottom="3px" text-align="left" xsl:use-attribute-sets="interim_word_group_default_font" font-weight="bold">
                   						Replacement/New Fund
                   					</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell padding-start="1px"  padding-right="4px" xsl:use-attribute-sets="fund_instruction_box_border_header_style">
                   					<fo:block start-indent="5px" padding-top="1px" padding-bottom="3px" text-align="center" xsl:use-attribute-sets="interim_word_group_default_font" font-weight="bold">
                   						Fund Code
                   					</fo:block>
                   					</fo:table-cell>
                   					
                   				</fo:table-row>
                   		</fo:table-header>
                   		<fo:table-body>
	                		  <xsl:for-each select="cofid_review_report/recommendations/cofid_recommendation">
								
												<fo:table-row display-align="center"  height="15px">
													<fo:table-cell xsl:use-attribute-sets="fund_instruction_box_border_style" >
													<fo:block text-align="left" xsl:use-attribute-sets="interim_word_group_default_font" start-indent="3px">
															<xsl:value-of select="currentFundName" />
													 </fo:block>
													</fo:table-cell>
													<fo:table-cell xsl:use-attribute-sets="fund_instruction_box_border_style" >
														 <fo:block text-align="left" xsl:use-attribute-sets="interim_word_group_default_font" start-indent="3px"> 
															<xsl:value-of select="currentfundCode" />													 	
															</fo:block>
													</fo:table-cell> 
													<fo:table-cell xsl:use-attribute-sets="fund_instruction_box_border_style" >
														<fo:block text-align="left" xsl:use-attribute-sets="interim_word_group_default_font" start-indent="3px">
 															<xsl:value-of select="replacementFundName" />
													 	</fo:block>
													</fo:table-cell>
													<fo:table-cell xsl:use-attribute-sets="fund_instruction_box_border_style" >
														<fo:block text-align="left" xsl:use-attribute-sets="interim_word_group_default_font" start-indent="3px">
															<xsl:value-of select="replacementFundCode" />
															</fo:block>
													</fo:table-cell>
												</fo:table-row>
								
						  </xsl:for-each>
                   		</fo:table-body>
                   	  </fo:table>
	            </fo:block>
	            </fo:flow>
        	
        	</fo:page-sequence>
        
        </fo:root>
    </xsl:template>
</xsl:stylesheet>