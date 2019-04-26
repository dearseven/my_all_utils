package uex.rsa;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncrypRSA {
	/**
	 * 加密
	 * 
	 * @param publicKey
	 * @param srcBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	protected byte[] encrypt(RSAPublicKey publicKey, byte[] srcBytes)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if (publicKey != null) {
			// Cipher负责完成加密或解密工作，基于RSA
			Cipher cipher = Cipher.getInstance("RSA");
			// 根据公钥，对Cipher对象进行初始化
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] resultBytes = cipher.doFinal(srcBytes);
			return resultBytes;
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param privateKey
	 * @param srcBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	protected byte[] decrypt(RSAPrivateKey privateKey, byte[] srcBytes)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if (privateKey != null) {
			// Cipher负责完成加密或解密工作，基于RSA
			Cipher cipher = Cipher.getInstance("RSA");
			// 根据公钥，对Cipher对象进行初始化
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] resultBytes = cipher.doFinal(srcBytes);
			return resultBytes;
		}
		return null;
	}

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException,
			InvalidKeyException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		EncrypRSA rsa = new EncrypRSA();
		String msg = "wo ji jiushi yonglai baocunmima suo yibu xuyao henchang a ";
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		// 初始化密钥对生成器，密钥大小为1024位
		keyPairGen.initialize(1024);
		// 生成一个密钥对，保存在keyPair中
		KeyPair keyPair = keyPairGen.generateKeyPair();

		// 得到私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// System.out.println(privateKey.getEncoded());

		// 得到公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		// System.out.println(publicKey.getEncoded());

		// 用公钥加密
		byte[] srcBytes = msg.getBytes();
		// 从这里开始是为了测试
		String pubKey = toHexString(publicKey.getEncoded());// 用于保存的公钥字符串
		System.out.println(pubKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(
				toBytes(pubKey));
		try {
			publicKey = (RSAPublicKey) keyFactory.generatePublic(bobPubKeySpec);
		} catch (InvalidKeySpecException e1) {
			e1.printStackTrace();
		}// 其实上面的公钥计算步骤是多余的 我只是为了试着做从数据恢复公钥
		byte[] resultBytes = rsa.encrypt(publicKey, srcBytes);
		String a = toHexString(resultBytes);

		// 用私钥解密
		// 从这里开始是为了测试
		String priKey = (toHexString(privateKey.getEncoded()));// 用于保存的私钥字符串
		System.out.println(priKey);
		System.out.println((priKey));
		priKey = (priKey);
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(toBytes(priKey));
		try {
			privateKey = (RSAPrivateKey) keyFactory.generatePrivate(priPKCS8);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}// 其实上面的私钥计算步骤是多余的 我只是为了试着做从数据恢复私钥
		byte[] decBytes = rsa.decrypt(privateKey, toBytes(a));

		System.out.println("明文是:" + msg);
		System.out.println(a.length() + " 加密后是:" + a);

		System.out.println("解密后是:" + new String(decBytes));
	}

	// -------------下面的方法 toHexString是为了保存钥匙为字符串，toBytes是为了还原钥匙
	private static char[] HEXCHAR = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	// private static char[] HEXCHAR = { 'f', 'e', 'd', 'c', 'b', 'a', '9', '8',
	// '7', '6', '5', '4', '3', '2', '1', '0' };

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEXCHAR[(b[i] & 0xf0) >>> 4]);
			sb.append(HEXCHAR[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static final byte[] toBytes(String s) {
		byte[] bytes;
		bytes = new byte[s.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),
					16);
		}
		return bytes;
	}
}
