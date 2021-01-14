package uex.md5;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

public class MD5 {
	public static String md5AndBase64(String str) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			String newstr = new String(Base64.encodeBase64(md5.digest(str
					.getBytes("utf-8"))), "utf-8");
			return newstr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String md5AndHex(String str) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes());
			byte[] digest = md5.digest();
			StringBuffer hexstr = new StringBuffer();
			String shaHex = "";
			for (int i = 0; i < digest.length; i++) {
				shaHex = Integer.toHexString(digest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexstr.append(0);
				}
				hexstr.append(shaHex);
			}
			return hexstr.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
