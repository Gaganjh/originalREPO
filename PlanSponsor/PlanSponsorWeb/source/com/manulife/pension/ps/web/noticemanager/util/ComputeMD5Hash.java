package com.manulife.pension.ps.web.noticemanager.util;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ComputeMD5Hash {
	public String createHash(String emailAddress) throws NoSuchAlgorithmException {
		StringBuilder sb = new StringBuilder();
		sb.append(emailAddress);
		sb.append("JhN0m@3rS1tE");
		sb.append(new SimpleDateFormat("MM-dd-yyyy").format(new Date()));
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(sb.toString().getBytes());
		byte[] digestBytes = md.digest();
		// convert the digest bytes to hex
		StringBuffer digest = new StringBuffer(digestBytes.length);
		for (int i = 0; i < digestBytes.length; i++) {
			String hex = Integer.toHexString(digestBytes[i]);
			if (hex.length() == 1) {
				digest.append("0");
			} else if (hex.length() > 2) {
				hex = hex.substring(hex.length() - 2);
			}
			digest.append(hex);
		}
		return digest.toString();
	}
	public static void main(String[] args) throws Exception {
		System.out.println(new ComputeMD5Hash().createHash(args[0]));
	}
}