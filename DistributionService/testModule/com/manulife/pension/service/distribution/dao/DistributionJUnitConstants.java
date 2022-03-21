package com.manulife.pension.service.distribution.dao;

import java.sql.Timestamp;

public interface DistributionJUnitConstants {

	public static final Integer TEST_SUBMISSION_ID = 111222;
//	public static final Integer TEST_CONTRACT_ID = 111334;
    public static final Integer TEST_CONTRACT_ID = 70300;
	public static final Integer TEST_CONTRACT_ID2 = 26570;
	public static final Integer TEST_USER_PROFILE_ID = 111445;
	public static final Integer TEST_USER_PROFILE_ID_2 = TEST_USER_PROFILE_ID + 1;
	public static final Timestamp TEST_TIMESTAMP_1 = new Timestamp(System.currentTimeMillis());
	public static final Timestamp TEST_TIMESTAMP_2 = new Timestamp(System.currentTimeMillis()+100000);

}
