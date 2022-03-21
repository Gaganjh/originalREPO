package com.manulife.pension.ireports.report;

import com.manulife.util.pdf.PdfRGB;

/**
 * Defines constants for: Footnote symbols, Date patterns, Fonts, Font Sizes and Colours
 */
public interface ReportFormattingConstants {
	// Footnote symbols
	public static final String FOOTNOTE_PERFORMANCE_HISTORY = "*1";
	public static final String FOOTNOTE_INVESTMENT_OPTIONS = "*2";
	public static final String FOOTNOTE_FUND_MANAGER = "*3";

    //public static final String FOOTNOTE_EXPENSES = "*4";
    //public static final String FOOTNOTE_MAINTAINENANCE_CHARGE = "*5";
	public static final String FOOTNOTE_AIC = "*6";
	public static final String FOOTNOTE_MORNINGSTAR_CATEGORY = "*7";
	public static final String FOOTNOTE_MORNINGSTAR_DATA_EXPENSE_RISK = "*8";
	public static final String FOOTNOTE_MORNINGSTAR_PEERGROUP = "*9";
	public static final String FOOTNOTE_INCEPTION_DATE = "*10";
	public static final String FOOTNOTE_ASSET_CLASS_LSFLCF = "*11";
	public static final String FOOTNOTE_MARKET_INDEX = "*12";
	public static final String FOOTNOTE_TICKER_SYMBOL = "*13";
	public static final String FOOTNOTE_MORNINGSTART_RATING_AND_CATEGORY = "*14";
	public static final String FOOTNOTE_STANDARD_DEVIATION = "*15";
	public static final String FOOTNOTE_3Y_5Y_10Y_GIC = "*16";
	public static final String FOOTNOTE_RETIREMENT_CHOICES_MARKET_INDEX = "*17";
	//public static final String FOOTNOTE_MODIFIED_CONTRACT= "*18";
	//public static final String FOOTNOTE_MODIFIED_SHORTLIST = "*19";
	public static final String FOOTNOTE_VALUE_SHORTLIST = "*20";
	public static final String FOOTNOTE_TOP_PERFORM_SHORTLIST = "*21";
	public static final String FOOTNOTE_RETIREMENT_LIVING_MARKET_INDEX = "*22";
	public static final String FOOTNOTE_LIFESTYLE_MARKET_INDEX = "*23";
	public static final String FOOTNOTE_CLASS_DISCLOSURE_PLAN_ADMIN = "*24";
	public static final String FOOTNOTE_CLASS_DISCLOSURE_GENERIC_INFORCE = "*25";
	public static final String FOOTNOTE_3YR_PERFORM_SHORTLIST = "*26";
	public static final String FOOTNOTE_5YR_PERFORM_SHORTLIST = "*27";
	public static final String FOOTNOTE_RETURNS_MAJOR_INVESTMENT = "*28";
    public static final String FOOTNOTE_LIFESTYLE = "*29";
	public static final String FOOTNOTE_LIFECYCLE = "*30";
	
	public static final String FOOTNOTE_SUPERSCRIPT_31 = "*31";
	public static final String FOOTNOTE_SUPERSCRIPT_32 = "*32";
	public static final String FOOTNOTE_SUPERSCRIPT_33 = "*33";
	public static final String FOOTNOTE_SUPERSCRIPT_34 = "*34";
	public static final String FOOTNOTE_SUPERSCRIPT_35 = "*35";
	
	public static final String FOOTNOTE_UFNC = "*40";
    public static final String FOOTNOTE_RFUF = "*41";
    public static final String FOOTNOTE_RFSA = "*42";
    public static final String FOOTNOTE_TRUTPC = "*43";

	public static final String BASE_LINE_SHIFT_SUPER = "super";
	public static final String SHIFT_CENTER = "center";
	public static final String STYLE_SOLID = "solid";
	
	// Date patterns
	public static final String DATE_PATTERN_FUND_DATA = "MM/dd/yy";
	public static final String DATE_PATTERN_AS_OF_DATE = "MMM dd, yyyy";

	// Fonts
	public static final String FRUTIGER_SR = "FrutigerSR";
	public static final String FRUTIGER65BOLD = "Frutiger65Bold";
	public static final String FRUTIGER55ROMAN = "Frutiger55Roman";
	public static final String FRUTIGER57CN = "Frutiger57Cn";
	public static final String FRUTIGER56ITALIC = "Frutiger56Italic";
	public static final String FRUTIGER67BOLD_CN = "Frutiger67BoldCn";
	public static final String FRUTIGER67CN = "Frutiger67Cn";
	public static final String FRUTIGER47LIGHT_CN = "Frutiger47LightCn";
	public static final String FRUTIGER66BOLD_ITALIC = "Frutiger66BoldItalic";

	// Font sizes
	public static final int FONT_SIZE_CONTRACT_OR_SHORTLIST_DESC = 12;
	public static final int FONT_SIZE_FUND_ROW = 8;
	public static final int FONT_SIZE_COLUMN_HEADING = 8;
	public static final int FONT_SIZE_STYLE_BOX_HEADING = 9;
	public static final int FONT_SIZE_FOOTNOTE_SYMBOL = 5;
	public static final int FONT_SIZE_FOOTNOTE_TEXT = 8;
	public static final int FONT_SIZE_FUND_GROUP_HEADING = 9;

	// Colours
	public static final String COLOUR_WHITE = "#FFFFFF";
	public static final String COLOUR_NAVY_BLUE = "#003366";
	public static final String COLOUR_BLUEISH_GREY = "#89A2B3";
	public static final String COLOUR_GREY = "#CCCCCC";
	public static final PdfRGB PDF_RGB_ROW_BACKGROUND_SHADE = new PdfRGB(216, 224, 228);
	public static final PdfRGB PDF_RGB_ROW_BORDER = new PdfRGB(188, 190, 192);
	public static final PdfRGB PDF_RGB_ROW_BORDER_3 = new PdfRGB( 88, 89, 91);
	public static final PdfRGB PDF_RGB_SHADING_1 = new PdfRGB(247,241,220);
	public static final PdfRGB PDF_RGB_SHADING_2 = new PdfRGB(237,244,181);
	public static final PdfRGB PDF_RGB_GROUP_HEADING = new PdfRGB(64,102,121);

	// Asset Class Ids
	public static final String LARGE_CAP_VALUE = "LCV";
	public static final String LARGE_CAP_BLEND = "LCB";
	public static final String LARGE_CAP_GROWTH = "LCG";
	public static final String MID_CAP_VALUE = "MCV";
	public static final String MID_CAP_GROWTH = "MCG";
	public static final String MID_CAP_BLEND = "MCB";
	public static final String MULTI_CAP_VALUE = "MCF";
	public static final String MULTI_CAP_BLEND = "MBC";
	public static final String MULTI_CAP_GROWTH = "MGC";
	public static final String SMALL_CAP_VALUE = "SCV";
	public static final String SMALL_CAP_GROWTH = "SCG";
	public static final String SMALL_CAP_BLEND = "SCB";
	public static final String INTERNATIONAL_GLOBAL_VALUE = "IGV";
	public static final String INTERNATIONAL_GLOBAL_GROWTH = "IGG";
	public static final String INTERNATIONAL_GLOBAL_BLEND = "IGB";
	public static final String INDEX = "IDX";
	public static final String SECTOR = "SEC";
	public static final String BALANCED = "BAL";
	public static final String LIFE_CYCLE = "LCF";
	public static final String LIFE_STYLE = "LSF";
	public static final String HIGH_QUALITY_SHORT_TERM = "FXS";
	public static final String HIGH_QUALITY_INTERMEDIATE_TERM = "FXI";
	public static final String HIGH_QUALITY_LONG_TERM = "FXL";
	public static final String MEDIUM_QUALITY_INTERMEDIATE_TERM = "FXM";
	public static final String HIGH_YIELD_BOND = "HYF";
	public static final String GLOBAL_BOND = "GLB";
	public static final String GUA_THREE = "GA3";
	public static final String GUA_FIVE = "GA5";
	public static final String GUA_TEN = "G10";
	//CR 20 - Asset class for Guaranteed Income Feature
	public static final String GUARANTEED_INCOME_FEATURE = "LSG";
	
	public static final String NONE = "none";
	public static final String SELECTED_VALUE= "filterAssetClassFunds";

}
