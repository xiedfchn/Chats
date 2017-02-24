/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chats;

/**
 *
 * @author anfalaljamea
 *
*/

/*
from :
http://www.thedeveloperspoint.com/using-java-security-libraries-for-implementing-rsa/
*/
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.PrivateKey;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import javax.crypto.Cipher;

public class RSA {
	private KeyPair keyPair;
	public RSA() throws Exception {
		Initialize();
	}

	public void Initialize() throws Exception {
		KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");

		keygen.initialize(512);

		keyPair = keygen.generateKeyPair();
	}

	public byte[] encrypt(byte[] plaintext) throws Exception {
		PublicKey key = keyPair.getPublic();
		System.out.println("Public key  " + key);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);

		byte[] ciphertext = cipher.doFinal(plaintext);

		return ciphertext;
	}
        public byte[] encryptByPrivate(byte[] plaintext) throws Exception {
		PrivateKey key = keyPair.getPrivate();
		System.out.println("Public key  " + key);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);

		byte[] ciphertext = cipher.doFinal(plaintext);

		return ciphertext;
	}
        
	public String decryptByPublic(byte[] ciphertext) throws Exception {
		PublicKey key = keyPair.getPublic();
		System.out.println("private key   " + key);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		cipher.init(Cipher.DECRYPT_MODE, key);

		byte[] plaintext = cipher.doFinal(ciphertext);

		return new String(plaintext, "UTF8");
	}
	public String decrypt(byte[] ciphertext) throws Exception {
		PrivateKey key = keyPair.getPrivate();
		System.out.println("private key   " + key);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		cipher.init(Cipher.DECRYPT_MODE, key);

		byte[] plaintext = cipher.doFinal(ciphertext);

		return new String(plaintext, "UTF8");
	}
        
	private static String encodeBASE64(byte[] bytes) {
		BASE64Encoder b64 = new BASE64Encoder();
		return b64.encode(bytes);
	}
	private static byte[] decodeBASE64(String text) throws Exception {
		BASE64Decoder b64 = new BASE64Decoder();
		return b64.decodeBuffer(text);

	}

//	public static void main(String[] args) throws Exception {
//		RSA app = new RSA();
//		System.out.println("Enter a line: ");
//		java.io.InputStreamReader sreader = new java.io.InputStreamReader(System. in );
//		java.io.BufferedReader breader = new java.io.BufferedReader(sreader);
//		String input = breader.readLine();
//		System.out.println("Plaintext = " + input);
//
//		byte[] ciphertext = app.encrypt(input);
//
//		System.out.println("After Encryption Ciphertext = " + ciphertext);
//
//		System.out.println("After Decryption Plaintext = " + app.decrypt(ciphertext));
//	}   
}