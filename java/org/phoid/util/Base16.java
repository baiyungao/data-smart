package org.phoid.util;

public class Base16
{
    private static final char[] S_BASE16CHAR = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'} ;
    private static final String S_DECODETABLE = "0123456789abcdef";

    /**
     * This methods decodes a Base16 string.
     *
     * @param s the string to decode
     * @return the decoded data
     */
    public static byte[] decode(String s)
    {
        byte[] obuf = new byte[s.length()/2] ;
        int j = 0;
        for( int i = 0; i<s.length()-1;i+=2 )
        {
            obuf[j++] = (byte)(((byte)(S_DECODETABLE.indexOf(s.charAt(i))) << 4) | (byte)(S_DECODETABLE.indexOf(s.charAt(i+1))));
        }
        return(obuf) ;
    }

    /**
     * This method encodes a byte array into a Base16 encoded string.
     *
     * @param inbuf the data to encode
     * @return the encoded String
     */
    public static String encode(byte[] inbuf)
    {
        char[] obuf = new char[inbuf.length*2] ;
        int j = 0 ;
        for( int i = 0; i<inbuf.length;i++ )
        {
            obuf[j] = S_BASE16CHAR[(inbuf[i]>>4)&15] ; j++ ;
            obuf[j] = S_BASE16CHAR[inbuf[i]&15] ; j++ ;
        }
        return new String(obuf) ;
    }

    public static String dump(byte[] inbuf)
    {
        char[] obuf = new char[inbuf.length*3] ;
        int j = 0 ;
        for( int i = 0; i<inbuf.length;i++ )
        {
            obuf[j] = S_BASE16CHAR[(inbuf[i]>>4)&15] ; j++ ;
            obuf[j] = S_BASE16CHAR[inbuf[i]&15] ; j++ ;
            obuf[j] = ' ' ; j++ ;
        }
        return new String(obuf) ;
    }

    /**
     * if the input contains spaces, remove them
     * @param inStr
     * @return
     */
    public static String removeBlanks(String inStr) {
        if (inStr.trim().indexOf(' ') < 0)
            return inStr ;
        char[] inCh = inStr.toCharArray() ;
        int j = 0 ;
        for (int i=0; i < inCh.length; i++) {
            if (inCh[i] != ' ')
                inCh[j++] = inCh[i] ;
        }
        return new String(inCh,0,j) ;
    }


}

