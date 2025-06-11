package com.hochschild.speed.back.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.spec.KeySpec;

public class EncryptUtil {
	public static final String DEFAULT_ENCRYPTION_KEY = "e10adc3949ba59abbe56e057f20f883e";
	private static final String STRING_ENCODING = "UTF-8";
	private static final String encryptionScheme = "DES";

	/**
	 * @param encryptionKey
	 * @param mode
	 * @return
	 * @throws Exception
	 */
	public static Cipher getCipher(String encryptionKey, int mode) throws Exception {
		if (encryptionKey == null)
			throw new IllegalArgumentException("encryption key was null");
		if (encryptionKey.trim().length() < 24)
			throw new IllegalArgumentException(
					"encryption key was less than 24 characters");

		byte[] keyAsBytes = encryptionKey.getBytes(STRING_ENCODING);

		KeySpec keySpec = new DESKeySpec(keyAsBytes);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encryptionScheme);
		Cipher cipher = Cipher.getInstance(encryptionScheme);
		
		SecretKey key = keyFactory.generateSecret(keySpec);
		cipher.init(mode, key);
		
		return cipher;
	}

	/* (non-Javadoc)
	 * @see net.modlost.crypt.CryptService#encrypt(java.lang.String, java.lang.String)
	 */
	public static String encrypt(String encryptionKey, String unencryptedString) throws Exception {
		if (unencryptedString == null || unencryptedString.trim().length() == 0)
			return unencryptedString;

		Cipher cipher = getCipher(encryptionKey, Cipher.ENCRYPT_MODE);
		
		byte[] cleartext = unencryptedString.getBytes(STRING_ENCODING);
		byte[] ciphertext = cipher.doFinal(cleartext);
                
		return Base64.encode(ciphertext);
	}

	/* (non-Javadoc)
	 * @see net.modlost.crypt.CryptService#decrypt(java.lang.String, java.lang.String)
	 */
	public static String decrypt(String encryptionKey, String encryptedString) throws Exception {
		if (encryptedString == null || encryptedString.trim().length() <= 0)
			return encryptedString;

		Cipher cipher = getCipher(encryptionKey, Cipher.DECRYPT_MODE);

		byte[] cleartext = Base64.decode(encryptedString);
		
		byte[] ciphertext = cipher.doFinal(cleartext);

		return bytes2String(ciphertext);
	}

	private static String bytes2String(byte[] bytes) {
		StringBuilder stringBuffer = new StringBuilder();
        for (byte aByte : bytes) {
            stringBuffer.append((char) aByte);
        }
		return stringBuffer.toString();
	}
}
