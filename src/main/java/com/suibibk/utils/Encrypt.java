package com.suibibk.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

/**
 * @author lwh
 *
 */
public class Encrypt {
	public static String encrypt(String msg){
		try {
			String sha2561 = SHA256.encrypt(msg);
			String encoder1 = URLEncoder.encode(sha2561, "UTF-8");
			String md51 = MD5.encrypt(encoder1);
			String encoder2 = URLEncoder.encode(md51, "UTF-8");
			String sha2562 = SHA256.encrypt(encoder2);
			return sha2562;
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) throws Exception {
       
	}
}
