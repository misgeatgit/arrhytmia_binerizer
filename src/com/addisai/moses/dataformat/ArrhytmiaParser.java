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
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class ArrhytmiaParser {

    static List<String[]> rowval = new ArrayList<String[]>();
    static HashMap<Integer, Float> medians = new HashMap<>();
    static HashMap<Integer, Float> max = new HashMap<>();
    static HashMap<Integer, Float> min = new HashMap<>();
    static CSVReader csvReader = null;
    static String csvFilename = "arrhythmia.data";

    static enum methods {

        DECILIZE, MEDIAN
    };
    /*
     *Class code :   Class   :                       Number of instances:    alias
     01             Normal				          245        NORM
     02             Ischemic changes (Coronary Artery Disease)   44        CAD
     03             Old Anterior Myocardial Infarction           15        OAMI
     04             Old Inferior Myocardial Infarction           15        OIMI
     05             Sinus tachycardy			           13        ST
     06             Sinus bradycardy			           25        SB 
     07             Ventricular Premature Contraction (PVC)       3        PVC
     08             Supraventricular Premature Contraction	    2        SVC
     09             Left bundle branch block 		            9	     LBBB 
     10             Right bundle branch block		           50        RBBB
     11             1. degree AtrioVentricular block	            0	     DAVB_1
     12             2. degree AV block		            0        DAVB_2
     13             3. degree AV block		            0        DAVB_3
     14             Left ventricule hypertrophy 	            4        LVH
     15             Atrial Fibrillation or Flutter	            5        AFF
     16             Others				           22        other
     * 
     */

    static enum classes {

        NORM, CAD, OAMI, OIMI, ST, SB, PVC, SVC, LBBB, RBBB, DAVB_1, DAVB_2, DAVB_3, LVH, AFF, OTHER
    }
    static methods method = methods.MEDIAN;
    static classes group;

    public static void setMethod(methods method) {
        ArrhytmiaParser.method = method;
    }

    public static void setGroup(classes group) {
        ArrhytmiaParser.group = group;
    }

    public ArrhytmiaParser() {
        try {
            csvReader = new CSVReader(new FileReader(csvFilename));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArrhytmiaParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static float median(int col) {
        float median = 0;
        int count = 0;
        List<Float> data = new ArrayList<>();
        try {
            String csvFilename = "arrhythmia.data";
            CSVReader csvReader = null;
            try {
                csvReader = new CSVReader(new FileReader(csvFilename));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ArrhytmiaParser.class.getName()).log(Level.SEVERE, null, ex);
            }
            String[] row = null;

            while ((row = csvReader.readNext()) != null) {

                if (row.length != 280 || isBadRow(row)) {
                    continue;
                } else {
                    //count++;
                    //total=total+Float.parseFloat(treamLeadingZero(row[col]));
                    try {
                        data.add(Float.parseFloat(toRealNumberString(row[col])));
                    } catch (NumberFormatException nex) {
                        nex.getMessage();
                        continue;
                    }
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
            Logger.getLogger(ArrhytmiaParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("MEDIAN DEBUG:col="+col+","+"total="+total+",count="+count+",median="+total/count);
        return median;
    }

    private static boolean isBadRow(String[] row) {
        boolean badrow = false;
        for (int i = 0; i < row.length; i++) {
            if (i != 13 && row[i].equals("?")) { //check any missing value except for col 13
                System.out.print("col:" + i + ">");
                badrow = true;
                break;
            }
        }
        return badrow;
    }

    private static void setMaxMin() {
        String[] row = null;
        String csvFilename = "arrhythmia.data";
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(csvFilename));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArrhytmiaParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            int totalRowCount = 0;
            while ((row = csvReader.readNext()) != null) {
                if (row.length != 280 || isBadRow(row)) {
                    continue;
                }
                for (int i = 0; i < row.length; i++) {
                    if (i != 1 && i != 13 && i != 21 && i != 22 && i != 23 && i != 24 && i != 25 && i != 26 && i != 27) {
                        float linearVal = Float.parseFloat(toRealNumberString(row[i]));
                        if (totalRowCount == 0) {  //set initial values for max and min lists
                            max.put(i, linearVal);
                            min.put(i, linearVal);
                        } else {  //update max and min list
                            if (max.get(i) < linearVal) {
                                max.put(i, linearVal);
                            }
                            if (min.get(i) > linearVal) {
                                min.put(i, linearVal);
                            }
                        }
                    }
                }
                totalRowCount++;
            }
        } catch (IOException ex) {
            Logger.getLogger(ArrhytmiaParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static int decilize(int colIndex, float val) {
        float maxval = max.get(colIndex);
        float minval = min.get(colIndex);
        float diff = (maxval - minval) / 10;
        float nextRange = minval + diff;

        int indx = 0;
        for (int i = 0; i < 10; i++) {
            if (val >= minval && val < nextRange) {
                indx = i;
                break;
            } else {
                minval += diff;
                nextRange += diff;
            }
        }
        return indx;
    }

    /**
     * Binerizes the csv data according to median binerization of linear valued
     * features method
     *
     * @param
     * @return
     */
    private static void medBinerize() {
        int cleanRowCount = 0;
        int badrowCount = 0;
        int totalRowCount = 0;
        String[] row = null;
        //there will be 46 columns while encoding the 16 features
        String[] encodedDataRow = new String[279];
        System.out.println("encoding row Length:" + encodedDataRow.length);
        setMaxMin();
        try {
            while ((row = csvReader.readNext()) != null) {
                if (row.length != 280 || isBadRow(row)) {
                    System.out.println("Bad Row>indx:" + cleanRowCount);
                    badrowCount++;
                    totalRowCount++;
                    continue;
                }
                String[] encodedDataRowClone = encodedDataRow.clone();
                //  System.out.println("leni" + index + ":" + row.length);
                for (int i = 0; i < row.length; i++) {
                    if (cleanRowCount == 0) {               //calculate all medians for linear attributes
                        medians.put(i, median(i));
                    }
                    if (i == 1) { //already a nominal value
                        encodedDataRowClone[0] = row[1];
                    } else if (i == 13) //leave col 13 because its full of missing values                           
                    {
                        // System.out.println("Col13:"+row[i]);
                    } else if (i == 21) {  //already a nominal value
                        encodedDataRowClone[21] = row[i];
                        //System.out.println("col22:"+row[22]);
                    } else if (i == 22) { //already a nominal value
                        encodedDataRowClone[21] = row[i];
                    } else if (i == 23) { //already a nominal value
                        encodedDataRowClone[22] = row[i];
                    } else if (i == 24) { //already a nominal value
                        encodedDataRowClone[23] = row[i];
                    } else if (i == 25) { //already a nominal value
                        encodedDataRowClone[24] = row[i];
                    } else if (i == 26) { //already a nominal value
                        encodedDataRowClone[25] = row[i];
                    } else if (i == 279) { //class value 
                        if (row[279].equals("1")) {  //classify normal vs other type
                            encodedDataRowClone[278] = "1"; //normal
                        } else {
                            encodedDataRowClone[278] = "0"; //other type
                        }
                    } else { //all linear values are handled hear
                        float linearVal = Float.parseFloat(toRealNumberString(row[i]));
                        if (linearVal > medians.get(i)) {
                            //System.out.println("Converted:" + Float.parseFloat(treamLeadingZero(row[i])));
                            if (i == 0) {
                                encodedDataRowClone[i] = "1";
                            } else {
                                encodedDataRowClone[i - 1] = "1";
                            }
                        } else {
                            if (i == 0) {
                                encodedDataRowClone[i] = "0";
                            } else {
                                encodedDataRowClone[i - 1] = "0";
                            }
                        }
                    }
                }
                rowval.add(encodedDataRowClone);
                totalRowCount++;
                cleanRowCount++;

            }
        } catch (IOException ex) {
            Logger.getLogger(ArrhytmiaParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        String preparedCrx = "calss"+method+"Arrhytmia.moses";
        CSVWriter writer;
        try {
            writer = new CSVWriter(new FileWriter(preparedCrx));
            writer.writeAll(rowval);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(ArrhytmiaParser.class.getName()).log(Level.SEVERE, null, ex);
        }


        System.out.println("Finished\nTotalRow:" + totalRowCount + "\nBadRows:" + badrowCount + "\ncleanRows:" + cleanRowCount);
    }

    /**
     * Binerizes the csv data according to decilization of linear valued
     * features method
     *
     * @param
     * @return
     */
    private static void decBinerize() {
        int cleanRowCount = 0;
        int badrowCount = 0;
        int totalRowCount = 0;
        String[] row = null;
        //there will be 46 columns while encoding the 16 features
        String[] encodedDataRow = new String[279];
    }

    /**
     * A generic method for parsing and converting string of unformatted number
     * to a proper one
     */
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

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("wrong number of parameters \n usage \n  [methods](0|1) [disease_class](0|..|16)");
        } else {
            ArrhytmiaParser arparser = new ArrhytmiaParser();
            int m_val = Integer.parseInt(args[0]);
            int c_val = Integer.parseInt(args[1]);
            if (m_val == 0) {
                method = methods.MEDIAN;
            } else if (m_val == 1) {
                method = methods.DECILIZE;
            } else {
                System.out.println("Wrong value for method type.valid values are 0 for median and 1 for decile");
                System.exit(0);
            }

            if (c_val == 0) {
                group = classes.NORM;
            } else if (c_val == 1) {
                group = classes.CAD;
            } else if (c_val == 2) {
                group = classes.OAMI;
            } else if (c_val == 3) {
                group = classes.OIMI;
            } else if (c_val == 4) {
                group = classes.ST;
            } else if (c_val == 5) {
                group = classes.SB;
            } else if (c_val == 6) {
                group = classes.PVC;
            } else if (c_val == 7) {
                group = classes.SVC;
            } else if (c_val == 8) {
                group = classes.LBBB;
            } else if (c_val == 9) {
                group = classes.RBBB;
            } else if (c_val == 10) {
                group = classes.DAVB_1;
            } else if (c_val == 11) {
                group = classes.DAVB_2;
            } else if (c_val == 12) {
                group = classes.DAVB_3;
            } else if (c_val == 13) {
                group = classes.LVH;
            } else if (c_val == 14) {
                group = classes.AFF;
            } else if (c_val == 15) {
                group = classes.OTHER;
            } else {
                System.out.println("Wrong value for disease classe.valid values are \n"
                        + "0 for Normal\n"
                        + "1   for Ischemic changes (Coronary Artery Disease)n"
                        + "2   for Old Anterior Myocardial Infarction\n"
                        + "3   for Old Inferior Myocardial Infarction\n"
                        + "4   for Sinus tachycardy\n"
                        + "5   for Sinus bradycardy\n"
                        + "6   for Ventricular Premature Contraction (PVC)\n"
                        + "7   for Supraventricular Premature Contraction\n"
                        + "8   for Left bundle branch block\n"
                        + "9   for Right bundle branch block\n"
                        + "10  for 1st degree AtrioVentricular block\n"
                        + "11  for 2nd degree AV block\n"
                        + "12  for 3rd degree AV block\n"
                        + "13  for Left ventricule hypertrophy\n"
                        + "14  for Atrial Fibrillation or Flutter\n"
                        + "15  for Others");
                System.exit(0);
            }
        }

        if (method == methods.MEDIAN) {
            medBinerize();
        }
        if (method == methods.DECILIZE) {
            decBinerize();
        }
    }
}
