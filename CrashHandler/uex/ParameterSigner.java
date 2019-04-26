


public class ParameterSigner {
    public static StringBuilder exec(StringBuilder sb) {
        String sign = MD5.md5AndHex(SHA1.getSHA1(sb.toString()));
        sb.append("&sign=").append(sign);
        return sb;
    }
}
