package chats;

/**
 *
 * @author macbookpro
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {
    /*** 
     * SHA encrypt create 40bits SHA code
     * @param inStr
     * @return 40bits SHA code
     * @throws java.lang.Exception
     */
    public static String shaEncode(String inStr) throws Exception {
        MessageDigest sha;
        sha = null;
        try {
            // Use SHA algorithm
            sha = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.toString());

            return "";
           
        }
        
        // set formate as UTF-8
        byte[] byteArray = inStr.getBytes("UTF-8");
        // create a MD5Byte with a message digest 
        // that is a cryptographic hash function containing a string of digits by a one-way hashing formula
        byte[] md5Bytes = sha.digest(byteArray);
        // 
        StringBuilder hexValue = new StringBuilder();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) { 
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * text main function
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        
         /*
          * all codes below is important for testing,
          * please don not remove;
          */
        // Tell user to type in msg
        System.out.println("Message：");
        // create a new inputstreamreader
        java.io.InputStreamReader sreader = new java.io.InputStreamReader(System.in);
        // create a new buffteredReader object for read data
        java.io.BufferedReader breader = new java.io.BufferedReader(sreader);
        // get a line of data
        String input = breader.readLine();
        // show the result to user
        System.out.println("Plaintext = " + input);
        // call shaEncode function
        System.out.println("after SHA：" + shaEncode(input)==shaEncode(input));
        System.out.println("after SHA：" + shaEncode(input));
        System.out.println("cipher: " + input);
    }
    
}