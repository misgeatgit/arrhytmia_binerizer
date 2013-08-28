/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.addisaicommons;

import static com.addisai.moses.dataformat.ArrhytmiaParser.toRealNumberString;
import java.util.Arrays;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class AddisAiCommons {
    public static String toRealNumberString(String a) {
        char[] aarray = a.toCharArray();
        while (aarray[0] == '0' && aarray.length > 1) {
            if (aarray[1] == '.') { //exit if the string represents real number
                break;
            }
            aarray = Arrays.copyOfRange(aarray, 1, aarray.length);

            toRealNumberString(String.valueOf(aarray));
        }
        return String.valueOf(aarray);
    }
}
