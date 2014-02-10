package com.connexience.server.util;

/**
 * Author: Simon
 * Date: Jun 2, 2009
 */
public class HexUtils
{


  public static String getHexString(byte[] b)
   {
     String result = "";
     for (int i = 0; i < b.length; i++)
     {
       result +=
           Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
     }
     return result;
   }

}
