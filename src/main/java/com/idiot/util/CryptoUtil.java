package com.idiot.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import jakarta.servlet.http.HttpServlet;

public class CryptoUtil extends HttpServlet {
	
	 public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception { 
		 Cipher cipher = Cipher.getInstance("AES"); 
		 cipher.init(Cipher.DECRYPT_MODE, secretKey); 
		 byte[] decodedBytes = Base64.getDecoder().decode(encryptedText); 
		 byte[] decryptedBytes = cipher.doFinal(decodedBytes);
		 return new String(decryptedBytes);
	 }
	 
	 public static SecretKey getSecretKeyFromEncodedKey(String encodedKey) {
		   byte[] decodedKeyBytes=Base64.getDecoder().decode(encodedKey);
		   return new SecretKeySpec(decodedKeyBytes, 0, decodedKeyBytes.length, "AES");
	 }

}
