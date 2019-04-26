package uex;


import uex.md5.MD5;
import uex.sha1.SHA1;

public class ParameterSigner {
	public static StringBuilder exec(StringBuilder sb) {
		String sign = MD5.md5AndHex(SHA1.getSHA1(sb.toString()));
		sb.append("&sign=").append(sign);
		return sb;
	}
}
