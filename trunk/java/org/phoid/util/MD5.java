package org.phoid.util;


import java.security.MessageDigest;

import org.phoid.exception.PhoidException;

/**
 * 
 * User: Baiyun Gao
 * 
 * */

public final class MD5 {
    /**
     * Calc md5 digest of the blob
     * @param buf blob
     * @return base64 encoded md5 digest
     */
    public static final String calcMD5(byte[] buf) {
        try {
            MessageDigest dig = MessageDigest.getInstance("MD5");
            return Base64.encode(dig.digest(buf));
        } catch (Exception ex) {
            throw new PhoidException("Cannot calculate MD5 hash.", ex);
        }
    }

    /**
     * Calc md5 digest of the blob
     * @param buf blob
     * @return base64 md5 digest
     */
    public static final byte[] digest(byte[] buf) {
        try {
            MessageDigest dig = MessageDigest.getInstance("MD5");
            return dig.digest(buf);
        } catch (Exception ex) {
            throw new PhoidException("Cannot calculate MD5 hash.", ex);
        }
    }

    /**
     * Verify hash and return true, if hash is valid
     * @param hash base64 encoded hash
     * @param buffer blob
     */
    public static final boolean isHashValid(String hash, byte[] buffer) {
        return calcMD5(buffer).equalsIgnoreCase(hash);
    }

       public static void main(String[] argv){
        byte[] pw = new byte[100];
        System.out.println("Phrase hasing module");
        try {
            System.out.print("Enter Phrase:");
            int len = System.in.read(pw);
            if (len > 0)   {
                System.out.println("MD5 Hash b64:" + MD5.calcMD5(pw));
                System.out.println("MD5 Hash Hex:" + Base64.toHexString(Base64.decode(MD5.calcMD5(pw))));
                }
            else
                System.out.println("Unable to read input.. try again..");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}