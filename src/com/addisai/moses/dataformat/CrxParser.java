/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.moses.dataformat;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.addisai.addisaicommons.MergeSort;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class CrxParser {

    public static String treamLeadingZero(String a) {
        char[] aarray = a.toCharArray();
        while (aarray[0] == '0' && aarray.length > 1) {
            if (aarray[1] == '.') { //exit if the string represents real number
                break;
            }
            aarray = Arrays.copyOfRange(aarray, 1, aarray.length);

            treamLeadingZero(String.valueOf(aarray));
        }
        return String.valueOf(aarray);
    }

    public static void main(String[] args) {
        try {
            String csvFilename = "crx.data";
            CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
            String[] row = null;
            List<String[]> rowval = new ArrayList<String[]>();
            //there will be 46 columns while encoding the 16 features
            String[] encodedRow = new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"
            };
            while ((row = csvReader.readNext()) != null) {
                if (row.length != 16 || isBadRow(row)) {
                    System.out.println("Bad Row:" + row.length);
                    continue;
                }

                String[] encodedRowClone = encodedRow.clone();
                boolean badRow = false;
                int cursor = 0;
                for (int i = 0; i < row.length; i++) {


                    switch (i) {
                        case 0:
                            //System.out.println(row[i]);
                            if (row[i].equals("a")) {
                                encodedRowClone[i] = "1";
                            }
                            if (row[i].equals("b")) {
                                encodedRowClone[i + 1] = "1";
                            }
                            break;
                        case 1:
                            if (Float.parseFloat(treamLeadingZero(row[i])) > median(i)) {
                                encodedRowClone[i + 1] = "1";
                            }

                            break;
                        case 2:
                            if (Float.parseFloat(treamLeadingZero(row[i])) > median(i)) {
                                encodedRowClone[i + 1] = "1";
                            }
                            break;
                        case 3:
                            if (row[i].equals("u")) {
                                encodedRowClone[i + 1] = "1";
                            }
                            if (row[i].equals("y")) {
                                encodedRowClone[i + 2] = "1";
                            }
                            if (row[i].equals("l")) {
                                encodedRowClone[i + 3] = "1";
                            }
                            if (row[i].equals("t")) {
                                encodedRowClone[i + 4] = "1";
                            }
                            break;
                        case 4:
                            if (row[i].equals("g")) {
                                encodedRowClone[i + 4] = "1";
                            }
                            if (row[i].equals("p")) {
                                encodedRowClone[i + 5] = "1";
                            }
                            if (row[i].equals("gg")) {
                                encodedRowClone[i + 6] = "1";
                            }

                            break;
                        case 5:
                            if (row[i].equals("c")) {
                                encodedRowClone[i + 6] = "1";
                            }
                            if (row[i].equals("d")) {
                                encodedRowClone[i + 7] = "1";
                            }
                            if (row[i].equals("cc")) {
                                encodedRowClone[i + 8] = "1";
                            }
                            if (row[i].equals("i")) {
                                encodedRowClone[i + 9] = "1";
                            }
                            if (row[i].equals("j")) {
                                encodedRowClone[i + 10] = "1";
                            }
                            if (row[i].equals("k")) {
                                encodedRowClone[i + 11] = "1";
                            }
                            if (row[i].equals("m")) {
                                encodedRowClone[i + 12] = "1";
                            }
                            if (row[i].equals("r")) {
                                encodedRowClone[i + 13] = "1";
                            }

                            if (row[i].equals("q")) {
                                encodedRowClone[i + 14] = "1";
                            }
                            if (row[i].equals("w")) {
                                encodedRowClone[i + 15] = "1";
                            }
                            if (row[i].equals("x")) {
                                encodedRowClone[i + 16] = "1";
                            }
                            if (row[i].equals("e")) {
                                encodedRowClone[i + 17] = "1";
                            }
                            if (row[i].equals("aa")) {
                                encodedRowClone[i + 18] = "1";
                            }
                            if (row[i].equals("ff")) {
                                encodedRowClone[i + 19] = "1";
                            }
                            break;
                        case 6:
                            if (row[i].equals("v")) {
                                encodedRowClone[i + 19] = "1";
                            }
                            if (row[i].equals("h")) {
                                encodedRowClone[i + 20] = "1";
                            }
                            if (row[i].equals("bb")) {
                                encodedRowClone[i + 21] = "1";
                            }
                            if (row[i].equals("j")) {
                                encodedRowClone[i + 22] = "1";
                            }
                            if (row[i].equals("n")) {
                                encodedRowClone[i + 23] = "1";
                            }
                            if (row[i].equals("z")) {
                                encodedRowClone[i + 24] = "1";
                            }
                            if (row[i].equals("dd")) {
                                encodedRowClone[i + 25] = "1";
                            }
                            if (row[i].equals("ff")) {
                                encodedRowClone[i + 26] = "1";
                            }
                            if (row[i].equals("o")) {
                                encodedRowClone[i + 27] = "1";
                            }
                            break;
                        case 7:
                            if (Float.parseFloat(treamLeadingZero(row[i])) > median(i)) {
                                encodedRowClone[i + 27] = "1";
                            }
                            break;
                        case 8:
                            if (row[i].equals("t")) {
                                encodedRowClone[i + 27] = "1";
                            }
                            if (row[i].equals("f")) {
                                encodedRowClone[i + 28] = "1";
                            }
                            break;
                        case 9:
                            if (row[i].equals("t")) {
                                encodedRowClone[i + 28] = "1";
                            }
                            if (row[i].equals("f")) {
                                encodedRowClone[i + 29] = "1";
                            }
                            break;
                        case 10:
                            if (Float.parseFloat(treamLeadingZero(row[i])) > median(i)) {
                                encodedRowClone[i + 29] = "1";
                            }
                            break;
                        case 11:
                            if (row[i].equals("t")) {
                                encodedRowClone[i + 29] = "1";
                            }
                            if (row[i].equals("f")) {
                                encodedRowClone[i + 30] = "1";
                            }
                            break;
                        case 12:
                            if (row[i].equals("g")) {
                                encodedRowClone[i + 30] = "1";
                            }
                            if (row[i].equals("p")) {
                                encodedRowClone[i + 31] = "1";
                            }
                            if (row[i].equals("s")) {
                                encodedRowClone[i + 32] = "1";
                            }
                            break;
                        case 13:
                            if (Float.parseFloat(treamLeadingZero(row[i])) > median(i)) {
                                encodedRowClone[i + 32] = "1";
                            }
                            break;
                        case 14:
                            if (Float.parseFloat(treamLeadingZero(row[i])) > median(i)) {
                                encodedRowClone[i + 32] = "1";
                            }
                            break;
                        case 15:
                            if (row[i].equals("+")) {
                                encodedRowClone[i + 32] = "1";
                            }
                            /*if (row[i].equals("-")) {
                             encodedRowClone[i+cursor]="1";
                             }*/
                            break;
                        default:
                            /*if (row[i].equals("?")) {
                             badRow=true;
                             }
                             else{
                             badRow=false;
                             }*/
                            break;
                    }


                }
                // csvReader.close();
                if (!badRow) {
                    rowval.add(encodedRowClone);
                }
                //...
            }
            String preparedCrx = "preparedCrx.moses";
            CSVWriter writer = new CSVWriter(new FileWriter(preparedCrx));
            writer.writeAll(rowval);

            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(CrxParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Finished");
    }

    private static float median(int col) {
        float median = 0;
        int count = 0;
        List<Float> data = new ArrayList<>();
        try {
            String csvFilename = "crx.data";
            CSVReader csvReader = null;
            try {
                csvReader = new CSVReader(new FileReader(csvFilename));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CrxParser.class.getName()).log(Level.SEVERE, null, ex);
            }
            String[] row = null;

            while ((row = csvReader.readNext()) != null) {

                if (row.length != 16 || isBadRow(row)) {
                    continue;
                } else {
                    //count++;
                    //total=total+Float.parseFloat(treamLeadingZero(row[col]));
                    data.add(Float.parseFloat(treamLeadingZero(row[col])));
                }
            }
            data = MergeSort.mergeSort((ArrayList<Float>) data);
            count = data.size();
            if (count <= 1) {
                return count;
            } else if (count % 2 == 0) {
                int index = count / 2 - 1;
                median = (data.get(index) + data.get(index + 1)) / 2;
            } else {
                int index = count / 2 - 1;
                median = (data.get(index + 1)) / 2;
            }
        } catch (IOException ex) {
            Logger.getLogger(CrxParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("MEDIAN DEBUG:col="+col+","+"total="+total+",count="+count+",median="+total/count);
        return median;
    }

    private static boolean isBadRow(String[] row) {
        boolean badrow = false;
        for (String st : row) {
            if (st.contains("?")) {
                badrow = true;
                break;
            }
        }
        return badrow;
    }
}
