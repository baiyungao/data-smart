package org.phoid.util;


import java.security.MessageDigest;

import org.phoid.exception.PhoidException;

public final class SHA1 {
    /**
     * Calc SHA1 digest of the blob
     * @param buf blob
     * @return base64 encoded sha1 digest
     */
    public static final String calcSHA1(byte[] buf) {
        try {
            MessageDigest dig = MessageDigest.getInstance("SHA1");
            return Base64.encode(dig.digest(buf));
        } catch (Exception ex) {
            throw new PhoidException("Cannot calculate SHA1 hash.", ex);
        }
    }

    /**
     * Calc SHA1 digest of the blob
     * @param buf blob
     * @return base64 sha1 digest
     */
    public static final byte[] digest(byte[] buf) {
        try {
            MessageDigest dig = MessageDigest.getInstance("SHA1");
            return dig.digest(buf);
        } catch (Exception ex) {
            throw new PhoidException("Cannot calculate SHA1 hash.", ex);
        }
    }

    /**
     * Verify hash and return true, if hash is valid
     * @param hash base64 encoded hash
     * @param buffer blob
     */
    public static final boolean isHashValid(String hash, byte[] buffer) {
        return calcSHA1(buffer).equalsIgnoreCase(hash);
    }

     public static void main(String[] argv){
        byte[] pw = new byte[100];
        System.out.println("Phrase hasing module");
        try {
            System.out.print("Enter Phrase:");
            int len = System.in.read(pw);
            if (len > 0)   {
                System.out.println("SHA1 Hash b64:" + SHA1.calcSHA1(pw));
                System.out.println("SHA1 Hash Hex:" +  Base64.toHexString(Base64.decode(SHA1.calcSHA1(pw))));
                }
            else
                System.out.println("Unable to read input.. try again..");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
