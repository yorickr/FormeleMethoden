package com.imegumii;

/**
 * Created by kenny on 12-6-2017.
 */
public class Util {

    public static String randomString(int lenght)
    {
        String s = "";
        String all = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for(int i = 0; i < lenght; i++)
        {
            int rand = (int)(Math.random() * all.length());
            s += all.substring(rand, rand+1);
        }

        return s;
    }
}
