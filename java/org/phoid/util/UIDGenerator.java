package org.phoid.util;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Random;

public final class UIDGenerator
{
    /** A string representation of the IP */
    private static String     IP_S   = null;
    private static int        _counter;


    // Staticly initialize IP_S to the current IP or a random number
    static
    {
        BigInteger big = null;
        try
        {   // Initialize big with the IP
            big = new BigInteger( InetAddress.getLocalHost().getAddress() );
        }
        catch (Throwable t)
        {   // If we can't use the IP, initialize big with a random number
            big = new BigInteger( 32, new Random () );
        }
        finally
        {
            IP_S =  big.toString(16).toUpperCase() + "-";
        }
    }



    /**
     * @return a unique ID
     */
    public static final String getUID()
    {
        int counter = 0;

        // minimize sync. time
        synchronized (IP_S)
        {
            counter = _counter++;
        }


        return new StringBuffer(30)
            .append( IP_S )
            .append( Long.toHexString( System.currentTimeMillis() ))
            .append( '-' )
            .append( counter )
            .toString();
    }

    public static int getRandom(int precision) {
        int n1 = (int) Math.pow(10, precision);
        int random = (int) (Math.random() * n1);
        return random % n1;
    }

}
