package com.imegumii;

import com.imegumii.ui.Frame;
import com.imegumii.ui.PopupFrame;

import java.util.TreeSet;

public class Main {


    public static void main(String[] args) {
//        Test.test();

//        Hopcroft();
//        TestUitOpdrachtBeschrijving();

        String s = "a*(aa+ |ba*b ) * (abba|baab|bbbb)+";


        String s2 = "a(a+ | b*((a|b)+))";
        String s3 = "(a|b)+a";

        String s4 = "a+((ab)*b|ab|(b)*bb)+(abba|baab)+";
        String s5 = "a(ab)*b(a|b)|ab|(b)*bb";
        String s6 = "(fuck)+";
        String s7 = "(a|b)";

        RegExp reg = new RegExp();

        new Frame();
//        new PopupFrame("Regex", reg.naarRegExp(s6));

    }
}
