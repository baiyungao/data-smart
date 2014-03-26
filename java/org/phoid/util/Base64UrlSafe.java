package org.phoid.util;

import java.io.*;

/**
 * This class provides utility methods to Base64 encode/decoded byte arrays
 * such that they will not be clobbered in an URL query string.
 *
 */
public abstract class Base64UrlSafe {

    private static final char char62 = '(' ;
    private static final char char63 = ')' ;
    private static final char charNull = '-' ;
    private static final byte byteNull = 45 ;


    private static final char S_BASE64CHAR[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
        'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
        'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', char62, char63
    };
    //private static final char S_BASE64PAD = byteNull;
    private static final byte S_DECODETABLE[];
    //private static final String tab = "0123456789ABCDEF";

    private static int decode0(char ibuf[], byte obuf[], int wp) {
        int outlen = 3;
        if( ibuf[3] == charNull )
            outlen = 2;
        if( ibuf[2] == charNull )
            outlen = 1;
        int b0 = S_DECODETABLE[ibuf[0]];
        int b1 = S_DECODETABLE[ibuf[1]];
        int b2 = S_DECODETABLE[ibuf[2]];
        int b3 = S_DECODETABLE[ibuf[3]];
        switch( outlen ) {
            case 1: // '\001'
                obuf[wp] = (byte)(b0 << 2 & 0xfc | b1 >> 4 & 3);
                return 1;

            case 2: // '\002'
                obuf[wp++] = (byte)(b0 << 2 & 0xfc | b1 >> 4 & 3);
                obuf[wp] = (byte)(b1 << 4 & 0xf0 | b2 >> 2 & 0xf);
                return 2;

            case 3: // '\003'
                obuf[wp++] = (byte)(b0 << 2 & 0xfc | b1 >> 4 & 3);
                obuf[wp++] = (byte)(b1 << 4 & 0xf0 | b2 >> 2 & 0xf);
                obuf[wp] = (byte)(b2 << 6 & 0xc0 | b3 & 0x3f);
                return 3;
        }
        throw new RuntimeException("Internal Errror");
    }

    /**
     * This methods decodes a Base64 char array.
     *
     * @param data   the data to decode
     * @param off    the element to begin decoding from
     * @param len    how may bytes to decode
     * @return the decoded data
     */
    public static byte[] decode(char data[], int off, int len) {
        char ibuf[] = new char[4];
        int ibufcount = 0;
        byte obuf[] = new byte[(len / 4) * 3 + 3];
        int obufcount = 0;
        for( int i = off; i < off + len; i++ ) {
            char ch = data[i];
            if( ch == charNull || ch < S_DECODETABLE.length && S_DECODETABLE[ch] != 127 ) {
                ibuf[ibufcount++] = ch;
                if( ibufcount == ibuf.length ) {
                    ibufcount = 0;
                    obufcount += decode0(ibuf, obuf, obufcount);
                }
            }
        }

        if( obufcount == obuf.length ) {
            return obuf;
        } else {
            byte ret[] = new byte[obufcount];
            System.arraycopy(obuf, 0, ret, 0, obufcount);
            return ret;
        }
    }

    /**
     * This method decodes a Base64 string
     *
     * @param data   the data to decode
     * @return the decoded data
     */
    public static byte[] decode(String data) {
        char ibuf[] = new char[4];
        int ibufcount = 0;
        byte obuf[] = new byte[(data.length() / 4) * 3 + 3];
        int obufcount = 0;
        for( int i = 0; i < data.length(); i++ ) {
            char ch = data.charAt(i);
            if( ch == charNull || ch < S_DECODETABLE.length && S_DECODETABLE[ch] != 127 ) {
                ibuf[ibufcount++] = ch;
                if( ibufcount == ibuf.length ) {
                    ibufcount = 0;
                    obufcount += decode0(ibuf, obuf, obufcount);
                }
            }
        }

        if( obufcount == obuf.length ) {
            return obuf;
        } else {
            byte ret[] = new byte[obufcount];
            System.arraycopy(obuf, 0, ret, 0, obufcount);
            return ret;
        }
    }

    /**
     * This method decodes a Base64 byte array.
     *
     * @param data   the data to decode
     * @return the decoded data
     */
    public static byte[] decode(byte data[]) {
        return decode(data, 0, data.length);
    }

    /**
     * This method decodes a Base64 byte array.
     *
     * @param data   the data to decode
     * @param off    the element to start from
     * @param len    the number of bytes to decode
     * @return the decoded data
     */
    public static byte[] decode(byte data[], int off, int len) {
        char ibuf[] = new char[4];
        int ibufcount = 0;
        byte obuf[] = new byte[(len / 4) * 3 + 3];
        int obufcount = 0;
        for( int i = off; i < off + len; i++ ) {
            char ch = (char)data[i];
            if( ch == charNull || ch < S_DECODETABLE.length && S_DECODETABLE[ch] != 127 ) {
                ibuf[ibufcount++] = ch;
                if( ibufcount == ibuf.length ) {
                    ibufcount = 0;
                    obufcount += decode0(ibuf, obuf, obufcount);
                }
            }
        }

        if( obufcount == obuf.length ) {
            return obuf;
        } else {
            byte ret[] = new byte[obufcount];
            System.arraycopy(obuf, 0, ret, 0, obufcount);
            return ret;
        }
    }

    /**
     * This method decodes a Base64 char array to an OutputStream
     *
     * @param data    the data to decode
     * @param off     which element to start from
     * @param len     the number of elements to decode
     * @param ostream the stream to write the decoded data from
     * @see OutputStream
     */
    public static void decode(char data[], int off, int len, OutputStream ostream)
    throws IOException
    {
        char ibuf[] = new char[4];
        int ibufcount = 0;
        byte obuf[] = new byte[3];
        for( int i = off; i < off + len; i++ ) {
            char ch = data[i];
            if( ch == charNull || ch < S_DECODETABLE.length && S_DECODETABLE[ch] != 127 ) {
                ibuf[ibufcount++] = ch;
                if( ibufcount == ibuf.length ) {
                    ibufcount = 0;
                    int obufcount = decode0(ibuf, obuf, 0);
                    ostream.write(obuf, 0, obufcount);
                }
            }
        }

    }

    /**
     * This method decodes a Base64 String to an OutputStream
     *
     * @param data    the data to decode
     * @param ostream the stream to write the decoded data
     * @see OutputStream
     */
    public static void decode(String data, OutputStream ostream)
    throws IOException
    {
        char ibuf[] = new char[4];
        int ibufcount = 0;
        byte obuf[] = new byte[3];
        for( int i = 0; i < data.length(); i++ ) {
            char ch = data.charAt(i);
            if( ch == charNull || ch < S_DECODETABLE.length && S_DECODETABLE[ch] != 127 ) {
                ibuf[ibufcount++] = ch;
                if( ibufcount == ibuf.length ) {
                    ibufcount = 0;
                    int obufcount = decode0(ibuf, obuf, 0);
                    ostream.write(obuf, 0, obufcount);
                }
            }
        }

    }

    /**
     * This method encodes a byte array into a Base64 encoded string.
     *
     * @param data   the data to encode
     * @return the encoded String
     */
    public static String encode(byte data[]) {
        return encode(data, 0, data.length);
    }

    public static String encode(String inStr) {
        return encode(inStr.getBytes()) ;
    }

    /**
     * This method encodes a byte array into a Base64 encoded string.
     *
     * @param data   the data to encode
     * @param off    which element to start from
     * @param len    the number of bytes to encode
     * @return the encoded String
     */
    public static String encode(byte data[], int off, int len) {
        if( len <= 0 )
            return "";
        char out[] = new char[(len / 3) * 4 + 4];
        int rindex = off;
        int windex = 0;
        int rest;
        for( rest = len; rest >= 3; rest -= 3 ) {
            int i = ((data[rindex] & 0xff) << 16) + ((data[rindex + 1] & 0xff) << 8) + (data[rindex + 2] & 0xff);
            out[windex++] = S_BASE64CHAR[i >> 18];
            out[windex++] = S_BASE64CHAR[i >> 12 & 0x3f];
            out[windex++] = S_BASE64CHAR[i >> 6 & 0x3f];
            out[windex++] = S_BASE64CHAR[i & 0x3f];
            rindex += 3;
        }

        if( rest == 1 ) {
            int i = data[rindex] & 0xff;
            out[windex++] = S_BASE64CHAR[i >> 2];
            out[windex++] = S_BASE64CHAR[i << 4 & 0x3f];
            out[windex++] = charNull;
            out[windex++] = charNull;
        } else
            if( rest == 2 ) {
            int i = ((data[rindex] & 0xff) << 8) + (data[rindex + 1] & 0xff);
            out[windex++] = S_BASE64CHAR[i >> 10];
            out[windex++] = S_BASE64CHAR[i >> 4 & 0x3f];
            out[windex++] = S_BASE64CHAR[i << 2 & 0x3f];
            out[windex++] = charNull;
        }
        return new String(out, 0, windex);
    }

    /**
     * This method encodes a byte array to an OutputStream.
     *
     * @param data    the date to encode
     * @param off     which element of the array to start from
     * @param len     the number of bytes to encode
     * @param ostream the stream to write the ecoded data to
     * @see OutputStream
     */
    public static void encode(byte data[], int off, int len, OutputStream ostream)
    throws IOException
    {
        if( len <= 0 )
            return;
        byte out[] = new byte[4];
        int rindex = off;
        int rest;
        for( rest = len - off; rest >= 3; rest -= 3 ) {
            int i = ((data[rindex] & 0xff) << 16) + ((data[rindex + 1] & 0xff) << 8) + (data[rindex + 2] & 0xff);
            out[0] = (byte)S_BASE64CHAR[i >> 18];
            out[1] = (byte)S_BASE64CHAR[i >> 12 & 0x3f];
            out[2] = (byte)S_BASE64CHAR[i >> 6 & 0x3f];
            out[3] = (byte)S_BASE64CHAR[i & 0x3f];
            ostream.write(out, 0, 4);
            rindex += 3;
        }

        if( rest == 1 ) {
            int i = data[rindex] & 0xff;
            out[0] = (byte)S_BASE64CHAR[i >> 2];
            out[1] = (byte)S_BASE64CHAR[i << 4 & 0x3f];
            out[2] = byteNull;
            out[3] = byteNull;
            ostream.write(out, 0, 4);
        } else
            if( rest == 2 ) {
            int i = ((data[rindex] & 0xff) << 8) + (data[rindex + 1] & 0xff);
            out[0] = (byte)S_BASE64CHAR[i >> 10];
            out[1] = (byte)S_BASE64CHAR[i >> 4 & 0x3f];
            out[2] = (byte)S_BASE64CHAR[i << 2 & 0x3f];
            out[3] = byteNull;
            ostream.write(out, 0, 4);
        }
    }

    /**
     * The method Base64 encodes the byte array to a Writer
     *
     * @param data   the data to encode
     * @param off    the element of the array to start from
     * @param len    the number of bytes to encode
     * @param writer the Writer to send the encoded data to
     * @exception IOException
     * @see Writer
     */
    public static void encode(byte data[], int off, int len, Writer writer)
    throws IOException
    {
        if( len <= 0 )
            return;
        char out[] = new char[4];
        int rindex = off;
        int rest = len - off;
        int output = 0;
        while( rest >= 3 ) {
            int i = ((data[rindex] & 0xff) << 16) + ((data[rindex + 1] & 0xff) << 8) + (data[rindex + 2] & 0xff);
            out[0] = S_BASE64CHAR[i >> 18];
            out[1] = S_BASE64CHAR[i >> 12 & 0x3f];
            out[2] = S_BASE64CHAR[i >> 6 & 0x3f];
            out[3] = S_BASE64CHAR[i & 0x3f];
            writer.write(out, 0, 4);
            rindex += 3;
            rest -= 3;
            if( (output += 4) % 76 == 0 )
                writer.write("\n");
        }
        if( rest == 1 ) {
            int i = data[rindex] & 0xff;
            out[0] = S_BASE64CHAR[i >> 2];
            out[1] = S_BASE64CHAR[i << 4 & 0x3f];
            out[2] = charNull;
            out[3] = charNull ;
            writer.write(out, 0, 4);
        } else
            if( rest == 2 ) {
            int i = ((data[rindex] & 0xff) << 8) + (data[rindex + 1] & 0xff);
            out[0] = S_BASE64CHAR[i >> 10];
            out[1] = S_BASE64CHAR[i >> 4 & 0x3f];
            out[2] = S_BASE64CHAR[i << 2 & 0x3f];
            out[3] = charNull ;
            writer.write(out, 0, 4);
        }
    }



    /**
     * This method creates a Hex encoded string of the given array
     *
     * @param ab     the data to encode
     * @return the Hex string
     */
    public static String toHexString(byte ab[]) {
        StringBuffer buffer = new StringBuffer(ab.length * 2);
        for( int i = 0; i < ab.length; i++ ) {
            buffer.append("0123456789ABCDEF".charAt(ab[i] >> 4 & 0xf));
            buffer.append("0123456789ABCDEF".charAt(ab[i] & 0xf));
        }

        return new String(buffer);
    }

    static {
        S_DECODETABLE = new byte[128];
        for( int i = 0; i < S_DECODETABLE.length; i++ )
            S_DECODETABLE[i] = 127;

        for( int i = 0; i < S_BASE64CHAR.length; i++ )
            S_DECODETABLE[S_BASE64CHAR[i]] = (byte)i;

    }

    public static final String toUrlSafe(String inStr) {
        String outStr = inStr.replace('/',char62).replace('+',char63).replace('=',charNull) ;
        return outStr ;
    }
    public static final String fromUrlSafe(String inStr) {
        String outStr = inStr.replace(char62, '/').replace(char63, '+').replace(charNull, '=') ;
        return outStr ;
    }
}
