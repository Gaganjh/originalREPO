package com.manulife.pension.ps.web.profiles;
public class ManageUserProfileHelper {

	public static final String MANAGE_EXTERNAL ="SCAR,CAR,PUM";	
	public static final String MANAGE_TPA ="MLUM,SCAR,CAR";
	public static final String MANAGE_INTERNAL ="MLUM";	
	public static final String INTERNAL_USER ="MLUM";
	public static final String USER_TYPE_INTERNAL ="INTERNAL";
	public static final String USER_TYPE_EXTERNAL ="EXTERNAL";	
	public static final String USER_TYPE_TPA ="TPA";		
	
	public static boolean isManageExternal(String roleId){
		return MANAGE_EXTERNAL.indexOf(roleId)>-1;
	}	
	public static boolean isManageTpa(String roleId){
		return MANAGE_TPA.indexOf(roleId)>-1;
	}
	public static boolean isManageInternal(String roleId){
		return MANAGE_INTERNAL.indexOf(roleId)>-1;
	}

}

