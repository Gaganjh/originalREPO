<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:attribute-set name="table_border_style">
<!--<xsl:attribute name="border-style">solid</xsl:attribute>-->
</xsl:attribute-set>

<xsl:attribute-set name="solid_blue_border">
	<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-start-style">solid</xsl:attribute>
	<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="solid_blue_border_1BottomSide_performance_table">
	<xsl:attribute name="border-after-color">#C9C8C2</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="solid_blue_border_1BottomSide_fundrank">
	<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="solid_blue_border_1BottomSide_supplementary">
	<xsl:attribute name="border-after-color">#BCBEC0</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
	<xsl:attribute name="text-align">center</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="fundrank_bg_border_1TopSide">
	<xsl:attribute name="border-before-color">#D9D8DA</xsl:attribute>
	<xsl:attribute name="border-before-style">solid</xsl:attribute>
	<xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_disclaimer" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="text-align">left</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_solid.blue.border.3sides">
	<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-start-style">solid</xsl:attribute>
	<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-end-style">solid</xsl:attribute>
	<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
	<xsl:attribute name="height">11px</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_border.1BottomSide">
	<xsl:attribute name="border-after-color">#231F20</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
	<xsl:attribute name ="padding-start">0.1cm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="header_default_font">
	<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
	<xsl:attribute name="color">#FFFFFF</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="supplementary_header_default_font">
	<xsl:attribute name="font-family">Frutiger 21 Light</xsl:attribute>
	<xsl:attribute name="color">#FFFFFF</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="header_block_cell1" use-attribute-sets="header_default_font">
	<xsl:attribute name="width">702px</xsl:attribute>
	<xsl:attribute name="height">5px</xsl:attribute>
	<xsl:attribute name="border-width">1mm</xsl:attribute>
	<xsl:attribute name="background-color">#CD5806</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="supplementary_header_block_cell1" use-attribute-sets="supplementary_header_default_font">
	<xsl:attribute name="width">126px</xsl:attribute>
	<xsl:attribute name="height">5px</xsl:attribute>
<xsl:attribute name="border-width">1mm</xsl:attribute>	
	<xsl:attribute name="background-color">#C55927</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="header_block_cell2" use-attribute-sets="header_block_cell1">
	<xsl:attribute name="width">576px</xsl:attribute>
	<xsl:attribute name="start-indent">126px</xsl:attribute>
	<xsl:attribute name="background-color">#656872</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="supplementary_next_page_header_block_cell3" use-attribute-sets="header_block_cell1">
	<xsl:attribute name="background-color">#404A55</xsl:attribute>
	<xsl:attribute name="height">25px</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
	<xsl:attribute name="display-align">center</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="header_block_cell3" use-attribute-sets="header_block_cell1">
	<xsl:attribute name="background-color">#404A55</xsl:attribute>
	<xsl:attribute name="height">52px</xsl:attribute>
	<xsl:attribute name="font-size">21pt</xsl:attribute>
	<xsl:attribute name="display-align">center</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="header_block_cell3_continued" use-attribute-sets="header_block_cell3">
	<xsl:attribute name="height">25px</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="header_block_cell3_desc" use-attribute-sets="header_default_font">
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="font-size">16pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="header_block_cell3_desc_continued" use-attribute-sets="header_block_cell3_desc">
	<xsl:attribute name="font-size">8.5pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="footnote_style">
	<xsl:attribute name="font-family">Arial Narrow</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="color">#6D6F71</xsl:attribute>
	<xsl:attribute name="font-weight">regular</xsl:attribute>
	<xsl:attribute name="provisional-label-separation">5mm</xsl:attribute>
	<xsl:attribute name="provisional-distance-between-starts">5mm</xsl:attribute>
	<xsl:attribute name="start-indent">0mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="word_group_style">
	<xsl:attribute name="font-family">Frutiger</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="font-weight">light</xsl:attribute>
	<xsl:attribute name="color">#231F20</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="word_group_style_bold">
	<xsl:attribute name="font-family">Frutiger</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="color">#231F20</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="sub_header_style" use-attribute-sets="word_group_style">
	<xsl:attribute name="font-weight">bold</xsl:attribute>	
	<xsl:attribute name="color">#CD5806</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="FSW_word_group_2_style" use-attribute-sets="sub_header_style">
	<xsl:attribute name="font-family">Arial Narrow</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="FSW_disclaimer_style" use-attribute-sets="FSW_word_group_2_style">
	<xsl:attribute name="font-weight">regular</xsl:attribute>	
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="color">#6D6F71</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="word_group_big_style">
	<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
	<xsl:attribute name="font-size">11pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="cover_page_disclaimer_text">
	<xsl:attribute name="font-family">Arial Narrow</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="color">#FFFFFF</xsl:attribute>
	<xsl:attribute name="space-after">15px</xsl:attribute>
	<xsl:attribute name="start-indent">18px</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="cover_page_default_font">
	<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
	<xsl:attribute name="font-size">10pt</xsl:attribute> 
	<xsl:attribute name="color">#FFFFFF</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="cover_page_display_data_font" use-attribute-sets="cover_page_default_font">
	<xsl:attribute name="font-family">Frutiger Roman</xsl:attribute>
	<xsl:attribute name="font-size">10pt</xsl:attribute>
	<xsl:attribute name="font-weight">Bold</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="page_numbering_style">
	<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_body_default_font">
	<xsl:attribute name="font-family">Arial Narrow</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="color">#231F20</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="page_count">
	<xsl:attribute name="font-family">Arial Narrow</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="color">#FFFFFF</xsl:attribute>
	<xsl:attribute name="height">14px</xsl:attribute>
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name="display-align">center</xsl:attribute>
	<xsl:attribute name ="padding-before">3px</xsl:attribute>
	<xsl:attribute name ="background-color">#404A55</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_asset_class_title" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="color">#FFFFFF</xsl:attribute>
	<xsl:attribute name="text-align">left</xsl:attribute>
	<xsl:attribute name="height">13px</xsl:attribute>
	<xsl:attribute name ="padding-start">0.1cm</xsl:attribute>
	<xsl:attribute name="display-align">center</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_level_1_Class_column_header_cell" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="font-size">12pt</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="color">#FFFFFF</xsl:attribute>
	<xsl:attribute name ="background-color">#8D8E96</xsl:attribute>
	<xsl:attribute name ="padding-start">0.1cm</xsl:attribute>
	<xsl:attribute name ="padding-after">0.1cm</xsl:attribute>
	<xsl:attribute name ="display-align">before</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_level_1_column_header_cell" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="color">#FFFFFF</xsl:attribute>
	<xsl:attribute name ="background-color">#8D8E96</xsl:attribute>
	<xsl:attribute name ="padding-start">0.1cm</xsl:attribute>
	<xsl:attribute name ="padding-after">0.1cm</xsl:attribute>
	<xsl:attribute name ="display-align">before</xsl:attribute>
	<xsl:attribute name="text-align">center</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_level_1_column_header_cell_bottomAligned" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="color">#FFFFFF</xsl:attribute>
	<xsl:attribute name ="background-color">#8D8E96</xsl:attribute>
	<xsl:attribute name ="padding-start">0.1cm</xsl:attribute>
	<xsl:attribute name ="padding-end">0.1cm</xsl:attribute>
	<xsl:attribute name ="padding-after">0.1cm</xsl:attribute>
	<xsl:attribute name ="display-align">after</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_level_2_column_header_cell" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="color">#FFFFFF</xsl:attribute>
	<xsl:attribute name ="background-color">#8D8E96</xsl:attribute>
	<xsl:attribute name ="padding-start">0.1cm</xsl:attribute>
	<xsl:attribute name ="padding-end">0.1cm</xsl:attribute>
	<xsl:attribute name ="padding-after">0.1cm</xsl:attribute>
	<xsl:attribute name ="display-align">after</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_cell_ror_monthEnd" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name ="background-color">#F5F5D9</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_cell_ror_monthEnd_1month" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name ="background-color">#F5F5D9</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-after-color">#C9C8C2</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_cell_ror_monthEnd_3month" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name ="background-color">#F5F5D9</xsl:attribute>
	<xsl:attribute name="border-after-color">#C9C8C2</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_cell_ror_monthEnd_Ytd" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name ="background-color">#F5F5D9</xsl:attribute>
	<xsl:attribute name="border-after-color">#C9C8C2</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_cell_ror_monthEnd_hypothetical">
	<xsl:attribute name="font-weight">bold</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_cell_ror_quaterEnd" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name ="background-color">#F4ECDD</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_cell_ror_quaterEnd_1yr" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name ="background-color">#F4ECDD</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-after-color">#C9C8C2</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_cell_ror_quaterEnd_3yr" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name ="background-color">#F4ECDD</xsl:attribute>
	<xsl:attribute name="border-after-color">#C9C8C2</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_cell_ror_quaterEnd_5yr" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name ="background-color">#F4ECDD</xsl:attribute>
	<xsl:attribute name="border-after-color">#C9C8C2</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_cell_ror_quaterEnd_10yr" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name ="background-color">#F4ECDD</xsl:attribute>
	<xsl:attribute name="border-after-color">#C9C8C2</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_cell_ror_quaterEnd_hypothetical" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="font-weight">bold</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_cell_ror_sinceInception" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="font-style">italic</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_cell_ror_sinceInception_hypothetical" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="font-style">italic</xsl:attribute>	
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_cell_expRatio_total" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name ="background-color">#EBE6D0</xsl:attribute>
	<xsl:attribute name="border-after-color">#C9C8C2</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_table_cell_morningStar" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">#C9C8C2</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="assethouse_column">
	<xsl:attribute name="display-align">center</xsl:attribute>
 	<xsl:attribute name="background-color">#404A55</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="assethouse_row" use-attribute-sets="assethouse_column">
	<xsl:attribute name="padding-before">7px</xsl:attribute>
	<xsl:attribute name="padding-after">7px</xsl:attribute>	
</xsl:attribute-set>

<xsl:attribute-set name="assetbox" use-attribute-sets="assethouse_row">
 	<xsl:attribute name="background-color">#F4D28A</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="assethouse_group_name">
	<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>	
	<xsl:attribute name="font-size">10pt</xsl:attribute>
	<xsl:attribute name="color">#CD5806</xsl:attribute>
	<xsl:attribute name="text-align">left</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="assethouse_column_row_name" use-attribute-sets="assethouse_group_name">
	<xsl:attribute name="font-family">Arial Narrow</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="color">#FFFFFF</xsl:attribute>
	<xsl:attribute name="text-align">center</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="assethouse_fund_name" use-attribute-sets="assethouse_column_row_name">
	<xsl:attribute name="font-weight">regular</xsl:attribute>	
	<xsl:attribute name="color">#231F20</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="assethouse_table_border">
	<xsl:attribute name="border-style">solid</xsl:attribute>	
	<xsl:attribute name="border-width">0.2px</xsl:attribute>
</xsl:attribute-set>

<!-- Investment Policy Statement attributes -->
<xsl:attribute-set name="form_title">
	<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
	<xsl:attribute name="font-size">28pt</xsl:attribute>
	<xsl:attribute name="color">#58595B</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="section_title" use-attribute-sets="form_title">
	<xsl:attribute name="font-weight">bold</xsl:attribute>	
	<xsl:attribute name="font-size">11pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="IPS_word_group" use-attribute-sets="form_title">
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="line-height">1.5</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="measurement_column_headers" use-attribute-sets="section_title">
	<xsl:attribute name="font-size">10pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="measurement_table_style">
	<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.1px</xsl:attribute>
	<xsl:attribute name="padding-before">2px</xsl:attribute>
	<xsl:attribute name="padding-after">3px</xsl:attribute>
</xsl:attribute-set>

<!-- Investment Selection form attributes -->
<xsl:attribute-set name="IVSF_form_title">
	<xsl:attribute name="font-family">Arial Narrow</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>	
	<xsl:attribute name="font-size">14pt</xsl:attribute>
	<xsl:attribute name="color">#000000</xsl:attribute>
	<!-- <xsl:attribute name="display-align">center</xsl:attribute> -->
</xsl:attribute-set>

<xsl:attribute-set name="IVSF_footer_text" use-attribute-sets="IVSF_form_title">
	<xsl:attribute name="font-weight">Regular</xsl:attribute>	
	<xsl:attribute name="font-size">8pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="information_page_word_group" use-attribute-sets="IVSF_footer_text">
	<xsl:attribute name="font-size">10pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="IVSF_word_group" use-attribute-sets="IVSF_footer_text">
	<xsl:attribute name="font-size">9pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="IVSF_authorization_text" use-attribute-sets="IVSF_footer_text">
	<xsl:attribute name="font-size">7pt</xsl:attribute>
	<xsl:attribute name="space-after">2px</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="website_address_style">
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="text-decoration">underline</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="contract_box_border_style">
	<xsl:attribute name="border-left-style">solid</xsl:attribute>
	<xsl:attribute name="border-right-style">solid</xsl:attribute>
	<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
	<xsl:attribute name="border-width">0.5px</xsl:attribute>
	<xsl:attribute name="border-color">#000000</xsl:attribute>
	<xsl:attribute name="padding-after">1px</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="IVSF_asset_class_name" use-attribute-sets="information_page_word_group">
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="text-align">left</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="investment_options_box" use-attribute-sets="contract_box_border_style IVSF_asset_class_name">
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name="padding-before">2px</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="IVSF_form_instruction" use-attribute-sets="IVSF_form_title">
	<xsl:attribute name="font-style">italic</xsl:attribute>
	<xsl:attribute name="font-weight"></xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="IVSF_disclosure_text" use-attribute-sets="IVSF_footer_text">
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="font-style">italic</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="IVSF_asset_box_cell">
	<xsl:attribute name="border-start-style">solid</xsl:attribute>
	<xsl:attribute name="border-start-width">2.5px</xsl:attribute>
	<xsl:attribute name="border-start-color">#C0C0C0</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="IVSF_asset_box">
	<xsl:attribute name="border-top-style">solid</xsl:attribute>
	<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
	<xsl:attribute name="border-end-style">solid</xsl:attribute>
	<xsl:attribute name="border-width">2.5px</xsl:attribute>
	<xsl:attribute name="border-color">#C0C0C0</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="fund_ranking_column_header_height">
	<xsl:attribute name="height">27px</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="fund_ranking_class_name" use-attribute-sets="assethouse_group_name">
	<xsl:attribute name="font-size">9pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="fund_ranking_overall_rank" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="color">#333333</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>	
</xsl:attribute-set>

<xsl:attribute-set name="overall_rank_bg_color">
	<xsl:attribute name="background-color">#D9D8DA</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="fund_ranking_fund_detail" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="font-weight">regular</xsl:attribute>	
</xsl:attribute-set>

<!-- Table Of Contents attributes -->
<xsl:attribute-set name="fundEvaluator_disclaimer_text" use-attribute-sets="IVSF_footer_text">
	<xsl:attribute name="color">#FFFFFF</xsl:attribute>
	<xsl:attribute name="line-height">1</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="TOC_section_name_text">
	<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
	<xsl:attribute name="color">#231F20</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="TOC_section_page_number_text" use-attribute-sets="TOC_section_name_text">
	<xsl:attribute name="text-align">right</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="Glossary_term-sub-header">
	<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>	
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="color">#CD5806</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="Glossary_term-definition" use-attribute-sets="Glossary_term-sub-header">
	<xsl:attribute name="font-weight">light</xsl:attribute>	
	<xsl:attribute name="color">#231F20</xsl:attribute>
	<xsl:attribute name="space-after">13px</xsl:attribute>
	<xsl:attribute name="line-height">1.5</xsl:attribute>
</xsl:attribute-set>

<!-- Investment Replacement Form attributes -->
<xsl:attribute-set name="IREP_section_title" use-attribute-sets="IVSF_form_title">
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="border-top-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.5px</xsl:attribute>
	<xsl:attribute name="padding-before">2px</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="managing_managers_word_group_style">
	<xsl:attribute name="font-family">Arial Narrow</xsl:attribute>
	<xsl:attribute name="font-size">11pt</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="color">#CD5806</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="managing_managers_call_out_text">
	<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
	<xsl:attribute name="font-size">10.5pt</xsl:attribute>
	<xsl:attribute name="color">#CD5806</xsl:attribute>
	<xsl:attribute name="line-height">1.15</xsl:attribute>
	<xsl:attribute name="text-align">right</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="selecting_investments_word_group">
	<xsl:attribute name="font-family">Frutiger Roman</xsl:attribute>
	<xsl:attribute name="font-size">10.5pt</xsl:attribute>
	<xsl:attribute name="color">#91905E</xsl:attribute>
	<xsl:attribute name="line-height">1.5</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="legend_icon_text_style">
	<xsl:attribute name="font-family">Arial Narrow</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="font-weight">Regular</xsl:attribute>
	<xsl:attribute name="color">#231F20</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="Investment_supplementary_table_body_default_font">
	<xsl:attribute name="font-family">Arial Narrow</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="color">#231F20</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="Investment_Supplementary_table_disclaimer" use-attribute-sets="performance_table_body_default_font">
	<xsl:attribute name="text-align">left</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="Investment_Supplementary_table_level_1_Class_column_header_cell" use-attribute-sets="assethouse_group_name">
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="color">#C55927</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="Investment_Supplementary_table_level_2_Class_column_header_cell" use-attribute-sets="fund_ranking_overall_rank">	
	<xsl:attribute name="border-left-color">#BCBEC0</xsl:attribute>
	<xsl:attribute name="border-left-style">solid</xsl:attribute>		
	<xsl:attribute name="border-left-width">0.5px</xsl:attribute>
	<xsl:attribute name="padding-after">0.2cm</xsl:attribute>	
</xsl:attribute-set>

<xsl:attribute-set name="Investment_Supplementary_table_level_1_column_header_cell" use-attribute-sets= "Investment_supplementary_table_body_default_font">
	<xsl:attribute name="font-weight">Regular</xsl:attribute>
	<xsl:attribute name="color">#231F20</xsl:attribute>	
	<xsl:attribute name ="display-align">before</xsl:attribute>
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="Investment_Supplementary_table_level_2_column_header_cell" use-attribute-sets= "Investment_supplementary_table_body_default_font">
	<xsl:attribute name="font-weight">Regular</xsl:attribute>
	<xsl:attribute name="color">#231F20</xsl:attribute>	
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="border-left-color">#BCBEC0</xsl:attribute>
	<xsl:attribute name="border-left-style">solid</xsl:attribute>		
	<xsl:attribute name="border-left-width">1.2px</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="Investment_Supplementary_table_level_3_column_header_cell" use-attribute-sets="Investment_supplementary_table_body_default_font">
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="font-weight">Regular</xsl:attribute>
	<xsl:attribute name="color">#231F20</xsl:attribute>
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name="border-left-color">#BCBEC0</xsl:attribute>
	<xsl:attribute name="border-left-style">solid</xsl:attribute>		
	<xsl:attribute name="border-left-width">0.5px</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="Managing_word_group_style">
	<xsl:attribute name="font-family">Frutiger</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="font-weight">light</xsl:attribute>
	<xsl:attribute name="color">#231F20</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="Managing_footnote_word_text_style">
	<xsl:attribute name="font-family">Arial Narrow</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="font-weight">Regular</xsl:attribute>
	<xsl:attribute name="color">#6D6F71</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="performance_footnote_word_text_style">
	<xsl:attribute name="font-family">Arial Narrow</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="font-weight">Regular</xsl:attribute>
	<xsl:attribute name="color">#6D6F71</xsl:attribute>
</xsl:attribute-set>

<!-- Global Variables -->

<xsl:variable name="imagePath">
	<xsl:value-of select='/iEvaluatorReport/customization/imagePath'/>
</xsl:variable>

<xsl:variable name="showContractFundIcon">
	<xsl:if test="/iEvaluatorReport/customization/legendIconsToInclude/icons[icon = 'showContractFundIcon']">
		<xsl:value-of select="'yes'"/>
	</xsl:if>
</xsl:variable>
<xsl:variable name="showCheckedIcon">
	<xsl:if test="/iEvaluatorReport/customization/legendIconsToInclude/icons[icon = 'showCheckedIcon']">
		<xsl:value-of select="'yes'"/>
	</xsl:if>
</xsl:variable>
<xsl:variable name="showCalculatedFund">
	<xsl:if test="/iEvaluatorReport/customization/legendIconsToInclude/icons[icon = 'showCalculatedFund']">
		<xsl:value-of select="'yes'"/>
	</xsl:if>
</xsl:variable>
<xsl:variable name="showManuallyAddedFund">
	<xsl:if test="/iEvaluatorReport/customization/legendIconsToInclude/icons[icon = 'showManuallyAddedFund']">
		<xsl:value-of select="'yes'"/>
	</xsl:if>
</xsl:variable>
<xsl:variable name="showManuallyRemovedFund">
	<xsl:if test="/iEvaluatorReport/customization/legendIconsToInclude/icons[icon = 'showManuallyRemovedFund']">
		<xsl:value-of select="'yes'"/>
	</xsl:if>
</xsl:variable>
<xsl:variable name="showIndexFundIcon">
	<xsl:if test="/iEvaluatorReport/customization/legendIconsToInclude/icons[icon = 'showIndexFundIcon']">
		<xsl:value-of select="'yes'"/>
	</xsl:if>
</xsl:variable>
<xsl:variable name="showIndexFundIconSelectedOnly">
	<xsl:if test="/iEvaluatorReport/customization/legendIconsToInclude/icons[icon = 'showIndexFundIconSelectedOnly']">
		<xsl:value-of select="'yes'"/>
	</xsl:if>
</xsl:variable>
<xsl:variable name="showClosedToNBIcon">
	<xsl:if test="/iEvaluatorReport/customization/legendIconsToInclude/icons[icon = 'showClosedToNBIcon']">
		<xsl:value-of select="'yes'"/>
	</xsl:if>
</xsl:variable>
<xsl:variable name="showClosedToNBIconSelectedOnly">
	<xsl:if test="/iEvaluatorReport/customization/legendIconsToInclude/icons[icon = 'showClosedToNBIconSelectedOnly']">
		<xsl:value-of select="'yes'"/>
	</xsl:if>
</xsl:variable>
<xsl:variable name="showContractFundIconForGuaranteed">
	<xsl:if test="/iEvaluatorReport/fundDetails/fund[@id='3YC']/@isContractFund ='yes' or /iEvaluatorReport/fundDetails/fund[@id='5YC']/@isContractFund ='yes' or /iEvaluatorReport/fundDetails/fund[@id='10YC']/@isContractFund ='yes'">
		<xsl:value-of select="'yes'"/>
	</xsl:if>
</xsl:variable>
<xsl:variable name="showCheckedFundIconForGuaranteed">
	<xsl:if test="/iEvaluatorReport/fundDetails/fund[@id='3YC']/@isChecked ='yes' or /iEvaluatorReport/fundDetails/fund[@id='5YC']/@isChecked ='yes' or /iEvaluatorReport/fundDetails/fund[@id='10YC']/@isChecked ='yes'">
		<xsl:value-of select="'yes'"/>
	</xsl:if>
</xsl:variable>


<xsl:variable name="contractNumber">
	<xsl:value-of select="/iEvaluatorReport/fundLineUp/contract"/>
</xsl:variable>

<xsl:variable name="companyCode">
	<xsl:choose>
		<xsl:when test="/iEvaluatorReport/fundLineUp/companyCode = 'John Hancock USA'">
			<xsl:value-of select="'USA'"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="'New York'"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="criteria_Selected_1_3_5_10YR">
	<xsl:value-of select="/iEvaluatorReport/criteriaSelections/criteria_Selected_1_3_5_10YR"/>
</xsl:variable>
	
<xsl:variable name="companyName">
	<xsl:value-of select='/iEvaluatorReport/customization/preparedForCompanyName'/>
</xsl:variable>

<xsl:variable name="numberOfSelectedCriterion_var">
	<xsl:value-of select="/iEvaluatorReport/criteriaSelections/numberOfSelectedCriterion"/>
</xsl:variable>
					
<!-- Fund Class Variable -->
<xsl:variable name="fundClass">
	<xsl:choose>
		<xsl:when test="$contractNumber = ''">
			<xsl:value-of select="/iEvaluatorReport/fundLineUp/fundClass"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="/iEvaluatorReport/fundLineUp/contract/@contractBaseClass"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>


<xsl:variable name="hasGA3Funds">
	<xsl:choose>
		<xsl:when test ="count(/iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='GA3']/funds) &gt; 0">
			<xsl:value-of select="'Yes'"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="'No'"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<xsl:variable name="hasGA5Funds">
	<xsl:choose>
		<xsl:when test ="count(/iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='GA5']/funds) &gt; 0">
			<xsl:value-of select="'Yes'"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="'No'"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<xsl:variable name="hasGA10Funds">
	<xsl:choose>
		<xsl:when test ="count(/iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='G10']/funds) &gt; 0">
			<xsl:value-of select="'Yes'"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="'No'"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="hasGAFunds">
	<xsl:choose>
		<xsl:when test ="$hasGA3Funds = 'Yes' or $hasGA5Funds = 'Yes' or $hasGA10Funds = 'Yes'">
			<xsl:value-of select="'Yes'"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="'No'"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="gaDisclosure">
	<xsl:value-of select="/iEvaluatorReport/customization/gaFundDisclosure"/>
</xsl:variable>

<xsl:include href="TableOfContents.xsl"/>
<xsl:include href="Results_SelectedInvestmentOptions.xsl"/>
<xsl:include href="ImportantNotes.xsl"/>
<!--ISF is replaced by Contract investment adminstration form ME:CL-127244 change-->
<!--<xsl:include href="InvestmentSelectionForm.xsl"/>-->
<xsl:include href="CustomMeasurementCriteria.xsl"/>
<xsl:include href="RankingMethodology.xsl"/>
<xsl:include href="CoverPage.xsl"/>
<xsl:include href="BackCoverPage.xsl"/>
<xsl:include href="InvestmentPolicyStatement.xsl"/>
<xsl:include href="Glossary.xsl"/>
<!-- SVFA: Removed as part of SVFA project Changes -->
<!-- <xsl:include href="InvestmentReplacementForm.xsl"/> -->
<xsl:include href="SelectingInvestmentOptions.xsl"/>
<xsl:include href="DocumentingDueDiligence.xsl"/>
<xsl:include href="FundRanking.xsl"/>
<xsl:include href="FundRanking_Selected.xsl"/>
<xsl:include href="PerformanceExpenses.xsl"/>
<xsl:include href="PerformanceExpenses_RiskCategory.xsl"/>
<xsl:include href="HtmlToFoTransform.xsl"/>
<xsl:include href="GIFLSelectInformation.xsl"/>
<xsl:include href="InvestmentSupplementary.xsl"/>
<xsl:include href="InvestmentSupplementary_Selected.xsl"/>
<xsl:include href="FundRanking_AdditonalCriterias.xsl"/>
<xsl:include href="FundRanking_Selected_AdditonalCriterias.xsl"/>

<xsl:template match="iEvaluatorReport">

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<fo:layout-master-set>
		<fo:simple-page-master margin-bottom="36px" margin-left="8px" margin-right="36px" margin-top="22px" master-name="PerformanceAndExpensesLayout" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="70px" margin-bottom="28px" />
			<fo:region-before extent="70px" />
			<fo:region-after extent="28px" />
			<fo:region-start/>
		</fo:simple-page-master>
		
		<fo:simple-page-master master-name="TableOfContentsLayout" margin-top="22px" margin-bottom="36px" margin-left="54px" margin-right="36px" page-width="11in" page-height="8.5in">
		   <fo:region-body margin-top="58px" margin-bottom="180px" background-color="#D9D8DA"/>
		   <fo:region-before extent="58px"/>
		   <fo:region-after extent="180px" background-color="#CD5806"/>
		</fo:simple-page-master>
		
		<fo:simple-page-master master-name="ImportantNotesLayout" margin-top="22px" margin-bottom="36px" margin-left="54px" margin-right="36px" page-width="11in" page-height="8.5in">
			<fo:region-body margin-bottom="17px" margin-top="75px" column-count="3" column-gap="4px"/>
			<fo:region-before extent="75px"/>
			<fo:region-after extent="14px"/>
		</fo:simple-page-master>
		
		<fo:simple-page-master master-name="GIFLSelectInformationLayout" margin-top="22px" margin-bottom="36px" margin-left="54px" margin-right="36px" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="100px"/>
			<fo:region-before extent="60px" precedence="true"/>
			<fo:region-after extent="14px" precedence="true"/>
		</fo:simple-page-master>

		<fo:simple-page-master master-name="RankingMethodologyLayout" margin-top="22px" margin-bottom="36px" margin-left="54px" margin-right="36px" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="100px"/>
			<fo:region-before extent="60px"/>
			<fo:region-after extent="55px" region-name="last_page_footer"/>
		</fo:simple-page-master>		
		
		<fo:simple-page-master master-name="CoverPageLayout" margin-top="26px" margin-bottom="41px" margin-left="18px" margin-right="18px" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="70px"/>
			<fo:region-before extent="70px"/>
		</fo:simple-page-master>
		
		<fo:simple-page-master master-name="Results_first_page_layout" margin-top="22px" margin-bottom="36px" margin-left="54px" margin-right="36px" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="100px" margin-bottom="42px"/>
			<fo:region-before extent="80px"/>
			<fo:region-after extent="42px" region-name="rest_page_footer"/>
		</fo:simple-page-master>
		
		<fo:simple-page-master master-name="Results_rest_page_layout" margin-top="22px" margin-bottom="36px" margin-left="54px" margin-right="36px" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="77px" margin-bottom="42px"/>
			<fo:region-before extent="80px"/>
			<fo:region-after extent="42px" region-name="rest_page_footer"/>
		</fo:simple-page-master>
		
		<fo:simple-page-master master-name="Results_last_page_layout" margin-top="22px" margin-bottom="36px" margin-left="54px" margin-right="36px" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="77px" margin-bottom="42px"/>
			<fo:region-before extent="80px"/>
			<fo:region-after extent="42px" region-name="last_page_footer"/>
		</fo:simple-page-master>
		
		<fo:page-sequence-master master-name="ResultsInvestmentOptionsLayout">
			<fo:repeatable-page-master-alternatives>
				<fo:conditional-page-master-reference master-reference="Results_first_page_layout" page-position="first"/>
				<fo:conditional-page-master-reference master-reference="Results_rest_page_layout" page-position="rest"/>
				<fo:conditional-page-master-reference master-reference="Results_last_page_layout" page-position="last"/>
			</fo:repeatable-page-master-alternatives>
		</fo:page-sequence-master>	
		<fo:simple-page-master master-name="IPS_first_page_layout" margin-top="70px" margin-bottom="70px" margin-left="43px" margin-right="43px" page-width="8.5in" page-height="11in">
			<fo:region-body column-count="2" column-gap="9px"/>
		</fo:simple-page-master>
		
		<fo:simple-page-master master-name="IPS_rest_page_layout" margin-top="70px" margin-bottom="70px" margin-left="43px" margin-right="43px" page-width="8.5in" page-height="11in">
			<fo:region-body  column-count="2" column-gap="9px"/>
			<fo:region-after extent="20px"/>
		</fo:simple-page-master>
		
		<fo:page-sequence-master master-name="InvestmentPolicyStatementLayout">
			<fo:repeatable-page-master-alternatives>
				<fo:conditional-page-master-reference master-reference="IPS_first_page_layout" page-position="first"/>
				<fo:conditional-page-master-reference master-reference="IPS_rest_page_layout" page-position="rest"/>
			</fo:repeatable-page-master-alternatives>
		</fo:page-sequence-master>
		
		<fo:simple-page-master master-name="InformationPageLayout" margin-top="19px" margin-bottom="24px" margin-left="22px" margin-right="21px" page-width="8.5in" page-height="11in">
			<fo:region-body margin-top="102px" margin-bottom="60px" margin-left="68px" margin-right="69px"/>
			<fo:region-before extent="100px"/>
			<fo:region-after extent="25px"/>
		</fo:simple-page-master>
		
		<!--  Conditional Page masters to address varying header and footer lengths.
				  Header for First page varies from that of remaining pages.
				  Footer for last page varies from that of remaining pages.
			  Hence different simple-page-master layouts defined for first, last and remaining pages.
			  Later these layouts are referred in page-sequence-master which is then used in page-sequence  -->
		<fo:simple-page-master master-name="IVSF_first_page_layout" margin-top="19px" margin-bottom="24px" margin-left="22px" margin-right="21px" page-width="8.5in" page-height="11in">
			<fo:region-body margin-top="240px" margin-bottom="50px" column-count="2" column-gap="12px"/>
			<fo:region-before extent="240px"/>
			<fo:region-after extent="45px"/>
		</fo:simple-page-master>
		
		<fo:simple-page-master master-name="IVSF_rest_page_layout" margin-top="19px" margin-bottom="24px" margin-left="22px" margin-right="21px" page-width="8.5in" page-height="11in">
			<fo:region-body margin-top="135px" margin-bottom="50px" column-count="2" column-gap="12px"/>
			<fo:region-before extent="135px"/>
			<fo:region-after extent="45px"/>
		</fo:simple-page-master>
		
		<fo:simple-page-master master-name="IVSF_last_page_layout" margin-top="19px" margin-bottom="24px" margin-left="22px" margin-right="21px" page-width="8.5in" page-height="11in">
			<fo:region-body margin-top="135px" margin-bottom="180px" column-count="2" column-gap="12px"/>
			<fo:region-before extent="135px"/>
			<fo:region-after extent="172px" region-name="last_page_footer"/>
		</fo:simple-page-master>
		
		<fo:simple-page-master master-name="IVSF_blank_last_page_layout" margin-top="19px" margin-bottom="24px" margin-left="22px" margin-right="21px" page-width="8.5in" page-height="11in">
			<fo:region-body margin-top="135px" margin-bottom="180px" column-count="2" column-gap="12px"/>
			<fo:region-before extent="135px" region-name="blank_last_page_header"/>
			<fo:region-after extent="172px" region-name="last_page_footer"/>
		</fo:simple-page-master>
		
		<fo:page-sequence-master master-name="InvestmentSelectionFormLayout">
			<fo:repeatable-page-master-alternatives>
				<fo:conditional-page-master-reference master-reference="IVSF_first_page_layout" page-position="first"/>
				<fo:conditional-page-master-reference master-reference="IVSF_rest_page_layout" page-position="rest"/>
				<fo:conditional-page-master-reference master-reference="IVSF_last_page_layout" page-position="last" blank-or-not-blank="not-blank"/>
				<fo:conditional-page-master-reference master-reference="IVSF_blank_last_page_layout" page-position="last" blank-or-not-blank="blank"/>
			</fo:repeatable-page-master-alternatives>
		</fo:page-sequence-master>

		<fo:simple-page-master master-name="GlossaryLayout" margin-top="22px" margin-bottom="36px" margin-left="54px" margin-right="36px" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="75px" margin-bottom="14px"/>
			<fo:region-before extent="75px"/>
			<fo:region-after extent="14px"/>
		</fo:simple-page-master>
		
		<fo:simple-page-master master-name="InvestmentReplacementFormLayout" margin-top="19px" margin-bottom="14px" margin-left="22px" margin-right="21px" page-width="8.5in" page-height="11in">
			<fo:region-body margin-top="118px" margin-bottom="22px"/>
			<fo:region-before extent="118px"/>
			<fo:region-after extent="22px"/>
		</fo:simple-page-master>
		
		<fo:simple-page-master master-name="SelectingInvestmentOptionsLayout" margin-top="22px" margin-bottom="36px" margin-left="54px" margin-right="36px" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="100px" margin-left="128px"/>
			<fo:region-before extent="60px" precedence="true"/>
			<fo:region-after extent="14px" precedence="true"/>
			<fo:region-start extent="128px"/>
		</fo:simple-page-master>
		
		<fo:simple-page-master master-name="DocumentingDueDiligenceLayout" margin-top="22px" margin-bottom="36px" margin-left="54px" margin-right="36px" page-width="11in" page-height="8.5in">
			<fo:region-body margin-bottom="14px" margin-top="100px"/>
			<fo:region-before extent="60px"/>
			<fo:region-after extent="14px"/>
		</fo:simple-page-master>

		<fo:page-sequence-master master-name="FundRankingLayout">
			<fo:repeatable-page-master-alternatives>
				<fo:conditional-page-master-reference master-reference="FundRanking_rest_page_layout" page-position="first"/>
				<fo:conditional-page-master-reference master-reference="FundRanking_rest_page_layout" page-position="rest"/>
				<fo:conditional-page-master-reference master-reference="FundRanking_last_page_layout" page-position="last"/>
			</fo:repeatable-page-master-alternatives>
		</fo:page-sequence-master>	
		
		<fo:simple-page-master margin-bottom="36px" margin-left="8px" margin-right="36px" margin-top="22px" master-name="FundRanking_rest_page_layout" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="88px" margin-bottom="28px" />
			<fo:region-before extent="88px" />
			<fo:region-after extent="28px" region-name="last_page_footer"/>
		</fo:simple-page-master>
		
		<fo:simple-page-master margin-bottom="36px" margin-left="8px" margin-right="36px" margin-top="22px" master-name="FundRanking_last_page_layout" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="88px"/>
			<fo:region-before extent="88px"/>
			<fo:region-after extent="28px" region-name="last_page_footer"/>
		</fo:simple-page-master>
		
		<fo:page-sequence-master master-name="CustomMeasurementCriteriaLayout">
			<fo:repeatable-page-master-alternatives>
				<fo:conditional-page-master-reference master-reference="CustomMeasurementCriteria_only_page_layout" page-position="only"/>
				<fo:conditional-page-master-reference master-reference="CustomMeasurementCriteria_rest_page_layout" page-position="first"/>
				<fo:conditional-page-master-reference master-reference="CustomMeasurementCriteria_rest_page_layout" page-position="rest"/>
				<fo:conditional-page-master-reference master-reference="CustomMeasurementCriteria_last_page_layout" page-position="last"/>
			</fo:repeatable-page-master-alternatives>
		</fo:page-sequence-master>
		
		<fo:simple-page-master margin-bottom="36px" margin-left="54px" margin-right="36px" margin-top="22px" master-name="CustomMeasurementCriteria_rest_page_layout" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="100px" margin-bottom="14px" />
			<fo:region-before extent="60px" />
			<fo:region-after extent="28px"  region-name="rest_page_footer" />
		</fo:simple-page-master>
		
		<fo:simple-page-master margin-bottom="36px" margin-left="54px" margin-right="36px" margin-top="22px" master-name="CustomMeasurementCriteria_last_page_layout" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="70px" margin-bottom="28px" />
			<fo:region-before extent="70px" />
			<fo:region-after extent="28px" region-name="last_page_footer" />
		</fo:simple-page-master>
		
		<fo:simple-page-master margin-bottom="36px" margin-left="54px" margin-right="36px" margin-top="22px" master-name="CustomMeasurementCriteria_only_page_layout" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="100px" margin-bottom="14px" />
			<fo:region-before extent="60px" />
			<fo:region-after extent="28px" region-name="last_page_footer" />
		</fo:simple-page-master>
		
		<fo:page-sequence-master master-name="InvestmentOptionSupplementaryLayout">
			<fo:repeatable-page-master-alternatives>
				<fo:conditional-page-master-reference master-reference="Supplementary_rest_page_layout" page-position="first"/>
				<fo:conditional-page-master-reference master-reference="Supplementary_rest_page_layout" page-position="rest"/>
				<fo:conditional-page-master-reference master-reference="Supplementary_last_page_layout" page-position="last"/>
			</fo:repeatable-page-master-alternatives>
		</fo:page-sequence-master>
		
		<fo:simple-page-master margin-bottom="36px" margin-left="54px" margin-right="36px" margin-top="22px" master-name="Supplementary_rest_page_layout" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="70px" margin-bottom="28px" />
			<fo:region-before extent="70px" />
			<fo:region-after extent="28px"  region-name="last_page_footer" />
		</fo:simple-page-master>
		
		<fo:simple-page-master margin-bottom="36px" margin-left="54px" margin-right="36px" margin-top="22px" master-name="Supplementary_last_page_layout" page-width="11in" page-height="8.5in">
			<fo:region-body margin-top="70px" margin-bottom="28px" />
			<fo:region-before extent="60px" />
			<fo:region-after extent="38px" region-name="last_page_footer" />
		</fo:simple-page-master>
		
		<fo:simple-page-master margin-bottom="18px" margin-left="18px" margin-right="18px" margin-top="18px" master-name="BackCoverPageLayout" page-width="11in" page-height="8.5in">
			<fo:region-body background-color="#CD5806"/>
		</fo:simple-page-master>
		
	</fo:layout-master-set>
 
	<!-- Call templates of included XSL -->
	<xsl:call-template name="CoverPageTemplate"/>
	
	<xsl:call-template name="TableOfContentsTemplate"/>
	<xsl:call-template name="SelectingInvestmentOptionsTemplate"/>
	
	<xsl:if test="//reportLayout/sections/section[@sectionId='GIFL']">
		<xsl:call-template name="GIFLSelectInformationTemplate"/>
	</xsl:if>
	
	<xsl:call-template name="CustomMeasurementCriteriaTemplate"/>
	
	<xsl:call-template name="ResultsSelectedInvestmentOptionsTemplate"/>
	
	<xsl:if test="//reportLayout/sections/section[@sectionId='IPST']">
		<xsl:call-template name="DocumentingDueDiligenceTemplate"/>
	</xsl:if>
	
	<xsl:if test="//reportLayout/sections/section[@sectionId='FRAV']">
		<xsl:call-template name="RankingMethodologyTemplate"/>
		<xsl:if test="$numberOfSelectedCriterion_var &lt;= 6">
		   <xsl:call-template name="FundRankingTemplate"/>
	    </xsl:if>
	    <xsl:if test="$numberOfSelectedCriterion_var &gt; 6">
	         <xsl:call-template name="FundRankingAddditonalCriteriaTemplate"/>
	     </xsl:if>
		<xsl:if test="$criteria_Selected_1_3_5_10YR ='Yes'">
			<xsl:call-template name="InvestmentSupplementaryTemplate"/>
		</xsl:if>
	</xsl:if>
	
	<xsl:if test="//reportLayout/sections/section[@sectionId='FRSE']">
		<xsl:call-template name="RankingMethodologyTemplate"/>
		<xsl:if test="$numberOfSelectedCriterion_var &lt;= 6">
		   <xsl:call-template name="FundRankingSelectedTemplate"/>
	    </xsl:if>
	    <xsl:if test="$numberOfSelectedCriterion_var &gt; 6">
	         <xsl:call-template name="FundRankingSelectedAddtionalCriteriaTemplate"/>
	     </xsl:if>
		<xsl:if test="$criteria_Selected_1_3_5_10YR ='Yes'">
			<xsl:call-template name="InvestmentSupplementarySelectedTemplate"/>
		</xsl:if>
	</xsl:if>	
	 

	<xsl:if test="//reportLayout/sections/section[@sectionId='PEAC']">
		<xsl:call-template name="PerformanceAndExpensesTemplate"/>
	</xsl:if>
	<xsl:if test="//reportLayout/sections/section[@sectionId='PERC']">
		<xsl:call-template name="PerformanceAndExpensesRiskCategoryTemplate"/>
	</xsl:if>

	<xsl:if test="//reportLayout/sections/section[@sectionId='GLOS']">
		<xsl:call-template name="GlossaryTemplate"/>
	</xsl:if>

	<xsl:call-template name="ImportantNotesTemplate"/>
	<xsl:call-template name="BackCoverTemplate"/>
	
	<!--<xsl:if test="//reportLayout/sections/section[@sectionId='IPST']">
		<xsl:call-template name="InvestmentPolicyStatementTemplate"/>
	</xsl:if>-->

	<xsl:if test="//reportLayout/sections/section[@sectionId='IVSF']">
		<!--ISF is replaced by Contract investment adminstration form ME:CL-127244 change-->
		<!--<xsl:call-template name="InvestmentSelectionFormTemplate"/>-->
		<!-- This section should render only for existing client -->
		<!-- <xsl:if test="$contractNumber != ''">
			<xsl:call-template name="InvestmentReplacementFormTemplate"/>
	 	</xsl:if> -->
	</xsl:if>

  </fo:root>
</xsl:template>
	
</xsl:stylesheet>