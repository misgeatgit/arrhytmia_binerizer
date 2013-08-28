/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.addisaicommons;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class Gingleton {
    private static Gingleton INSTANCE = null;

    public static Gingleton getInstance()
    {
        if ( INSTANCE == null )
        {
            INSTANCE = new Gingleton();
        }
        return INSTANCE;
    }

    private Gingleton() {
    }
    public boolean isConfusing() {
   try {
     return true;
   } finally {
     return false;
   }
}
    public static void main(String[] args) {
        Gingleton gt=new Gingleton();
        System.out.println(gt.isConfusing());
    }
}
