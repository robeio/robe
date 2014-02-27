package io.robe.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(HashingUtils.class);

	public static String hashSHA1(String original) {
		MessageDigest sha1;
		try {
			sha1 = MessageDigest.getInstance("SHA-1");
			byte[] originalBytes = original.getBytes("UTF-8");
			byte[] hashed = sha1.digest(originalBytes);
			StringBuilder sb = new StringBuilder();
			for (byte b : hashed) {
				sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			LOGGER.error(original, e);
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LOGGER.error(original, e);
			return null;
		}
	}

	public static String hashSHA2(String original) {
		MessageDigest sha256;
		try {
			sha256 = MessageDigest.getInstance("SHA-256");
			byte[] originalBytes = original.getBytes("UTF-8");
			byte[] hashed = sha256.digest(originalBytes);
			StringBuilder sb = new StringBuilder();
			for (byte b : hashed) {
				sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			LOGGER.error(original, e);
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LOGGER.error(original, e);
			return null;
		}
	}

	public static String checksumMd5(String original) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			byte[] hashed = md5.digest(original.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (byte b : hashed) {
				sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(original, e);
			return null;
		}
	}
}
