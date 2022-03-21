package com.manulife.pension.ps.web.tools.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for Docket Number GA code 
 * @author Eswar
 *
 */
public class DocketNumber {
	
	private static Map<String, String> docketNumbeNY = null;
	private static Map<String, String> docketNumbeUSA = null;
	private static Map<String, String> gaCodeNY = null;
	private static Map<String, String> gaCodeUSA = null;
	
	public static final String CLASS_1 = "CL5";
	public static final String CLASS_2 = "005";
	public static final String CLASS_3 = "010";
	public static final String CLASS_4 = "CL4";
	public static final String CLASS_5 = "CL3";
	public static final String CLASS_6 = "CL2";
	public static final String CLASS_7 = "CL1";
	public static final String CLASS_8 = "075";
	public static final String CLASS_9 = "100";
	
	
	static {
		
		docketNumbeNY = new HashMap<String, String>();
		docketNumbeNY.put(CLASS_1, "<strong>PS20055-NY-CL1</strong> 08/14-20055");
		docketNumbeNY.put(CLASS_2, "<strong>PS20056-NY-CL2</strong> 08/14-20056");
		docketNumbeNY.put(CLASS_3, "<strong>PS20057-NY-CL3</strong> 08/14-20057");
		docketNumbeNY.put(CLASS_4, "<strong>PS20058-NY-CL4</strong> 08/14-20058");
		docketNumbeNY.put(CLASS_5, "<strong>PS20059-NY-CL5</strong> 08/14-20059");
		docketNumbeNY.put(CLASS_6, "<strong>PS20060-NY-CL6</strong> 08/14-20060");
		docketNumbeNY.put(CLASS_7, "<strong>PS20061-NY-CL7</strong> 08/14-20061");
		docketNumbeNY.put(CLASS_8, "<strong>PS20062-NY-CL8</strong> 08/14-20062");
		docketNumbeNY.put(CLASS_9, "<strong>PS20063-NY-CL9</strong> 08/14-20063");
		
		docketNumbeUSA = new HashMap<String, String>();
		docketNumbeUSA.put(CLASS_1, "<strong>PS20001-USA-CL1</strong> 08/14-20001");
		docketNumbeUSA.put(CLASS_2, "<strong>PS20047-USA-CL2</strong> 08/14-20047");
		docketNumbeUSA.put(CLASS_3, "<strong>PS20048-USA-CL3</strong> 08/14-20048");
		docketNumbeUSA.put(CLASS_4, "<strong>PS20049-USA-CL4</strong> 08/14-20049");
		docketNumbeUSA.put(CLASS_5, "<strong>PS20050-USA-CL5</strong> 08/14-20050");
		docketNumbeUSA.put(CLASS_6, "<strong>PS20051-USA-CL6</strong> 08/14-20051");
		docketNumbeUSA.put(CLASS_7, "<strong>PS20052-USA-CL7</strong> 08/14-20052");
		docketNumbeUSA.put(CLASS_8, "<strong>PS20053-USA-CL8</strong> 08/14-20053");
		docketNumbeUSA.put(CLASS_9, "<strong>PS20054-USA-CL9</strong> 08/14-20054");
		
		gaCodeNY = new HashMap<String, String>();
		gaCodeNY.put(CLASS_1, "GA12051110272");
		gaCodeNY.put(CLASS_2, "GA12051110273");
		gaCodeNY.put(CLASS_3, "GA12051110274");
		gaCodeNY.put(CLASS_4, "GA12051110275");
		gaCodeNY.put(CLASS_5, "GA12051110276");
		gaCodeNY.put(CLASS_6, "GA12051110277");
		gaCodeNY.put(CLASS_7, "GA12051110278");
		gaCodeNY.put(CLASS_8, "GA12051110279");
		gaCodeNY.put(CLASS_9, "GA12051110280");
		
		gaCodeUSA = new HashMap<String, String>();
		gaCodeUSA.put(CLASS_1, "GA12051110263");
		gaCodeUSA.put(CLASS_2, "GA12051110264");
		gaCodeUSA.put(CLASS_3, "GA12051110265");
		gaCodeUSA.put(CLASS_4, "GA12051110266");
		gaCodeUSA.put(CLASS_5, "GA12051110267");
		gaCodeUSA.put(CLASS_6, "GA12051110268");
		gaCodeUSA.put(CLASS_7, "GA12051110269");
		gaCodeUSA.put(CLASS_8, "GA12051110270");
		gaCodeUSA.put(CLASS_9, "GA12051110271");
	}
	
	/**
	 * @param classType
	 * @return DocketNumber
	 */
	public static String getDocketNumberNY(String classType){
		return docketNumbeNY.get(classType);
	}
	
	/**
	 * @param classType
	 * @return DocketNumber
	 */
	public static String getDocketNumberUSA(String classType){
		return docketNumbeUSA.get(classType);
	}
	
	/**
	 * @param classType
	 * @return GACode
	 */
	public static String getGACodeNY(String classType){
		return gaCodeNY.get(classType);
	}
	
	/**
	 * @param classType
	 * @return GACode
	 */
	public static String getGACodeUSA(String classType){
		return gaCodeUSA.get(classType);
	}
	
}