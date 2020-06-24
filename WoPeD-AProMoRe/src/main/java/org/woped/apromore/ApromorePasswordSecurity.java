package org.woped.apromore;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

/**
 * @author Sascha Schneider, Philip Allgaier
 */

public class ApromorePasswordSecurity {

	// Encoding
	public static String encode(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		String keyStr = "secret";
		byte[] key = (keyStr).getBytes("UTF-8");
		MessageDigest sha = MessageDigest.getInstance("MD5");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		byte[] encrypted = cipher.doFinal(password.getBytes());

		// Convert bytes to Base64 string
		String topSecret = new String(Base64.getEncoder().encode(encrypted));

		return topSecret;
	}

	// Decoding
	public static String decode(String password) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		String keyStr = "secret";
		byte[] key = (keyStr).getBytes("UTF-8");
		MessageDigest sha = MessageDigest.getInstance("MD5");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

		// Convert Base64 string to byte array
		byte[] crypted = Base64.getDecoder().decode(password);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		byte[] decrypted = cipher.doFinal(crypted);
		String plain = new String(decrypted);

		return plain;
	}
}
