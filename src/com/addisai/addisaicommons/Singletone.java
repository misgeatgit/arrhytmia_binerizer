/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.addisaicommons;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class Singletone {
    private static Singletone INSTANCE = null;

    public static Singletone getInstance()
    {
        if ( INSTANCE == null )
        {
            INSTANCE = new Singletone();
        }
        return INSTANCE;
    }

    private Singletone() {
    }
    public boolean isConfusing() {
   try {
     return true;
   } finally {
     return false;
   }
}
    public static void main(String[] args) {
        Singletone st=new Singletone();
        System.out.println(st.isConfusing());
    }
}
