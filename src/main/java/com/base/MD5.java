package com.base;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5 {
	
	public String getMD5(String phoneNumber){
		MessageDigest md = null;
		byte[] digest = new byte[0];
		try{
			md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(phoneNumber.getBytes());
			digest = md.digest();			
		}catch(Exception e){
			
		}
		BigInteger bigInt = new BigInteger(1, digest);
		String md5Hex = bigInt.toString(16);
		while(md5Hex.length() < 32){
			md5Hex = "0" + md5Hex;
		}
		return md5Hex;
	}
}
