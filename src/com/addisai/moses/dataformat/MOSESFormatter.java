/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.moses.dataformat;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.addisai.addisaicommons.AddisAiCommons;
import com.addisai.addisaicommons.MergeSort;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class with an objective of manipulating each row values with a possibility
 * of d/t set of binerization method.rather than using one binerization
 * technique for the entire data.
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class MOSESFormatter {

    private static classes group;
    private static binerizationMethod method;
    List<String[]> rawData = new ArrayList<>();
    CSVReader csvReader = null;
    CSVWriter csvWriter = null;
    String[] featureNames = null;
    static HashMap<Integer, Float> max = new HashMap<>();
    static HashMap<Integer, Float> min = new HashMap<>();

    /**
     * returns the entire row values of a feature as a string array
     *
     * @param colIndx
     * @param rawData
     * @return
     */
    private String[] getAllColVal(int colIndx, List<String[]> rawData) {
        String[] allColVal = new String[rawData.size()];
        for (int i = 0; i < rawData.size(); i++) {
            allColVal[i] = rawData.get(i)[colIndx];
        }
        return allColVal;
    }

    public enum binerizationMethod {

        MEDIAN(0), DECILE(1), CUSTOM(2), NOMINAL(3);
        private final int value;

        private binerizationMethod(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    /*
     *Class code :   Class   :                                Number of instances:    alias
     01             Normal				          245                 NORM
     02             Ischemic changes (Coronary Artery Disease)    44                  CAD
     03             Old Anterior Myocardial Infarction            15                  OAMI
     04             Old Inferior Myocardial Infarction            15                  OIMI
     05             Sinus tachycardy			          13                  ST
     06             Sinus bradycardy			          25                  SB 
     07             Ventricular Premature Contraction (PVC)       3                   PVC
     08             Supraventricular Premature Contraction	  2                   SVC
     09             Left bundle branch block 		          9	              LBBB 
     10             Right bundle branch block		          50                  RBBB
     11             1. degree AtrioVentricular block	          0	              DAVB_1
     12             2. degree AV block		                  0                   DAVB_2
     13             3. degree AV block		                  0                   DAVB_3
     14             Left ventricule hypertrophy 	          4                   LVH
     15             Atrial Fibrillation or Flutter	          5                   AFF
     16             Others				          22                  OTHER
     * 
     */

    static enum classes {

        NORM(0), CAD(1), OAMI(2), OIMI(3), ST(4), SB(5), PVC(6), SVC(7), LBBB(8), RBBB(9), DAVB_1(10), DAVB_2(11), DAVB_3(12), LVH(13), AFF(14), OTHER(15);
        private final int value;

        private classes(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public MOSESFormatter(String[] featureNames) {
        this.featureNames = featureNames;
    }

    public void initFormatting() {
        int removed = 0;
        try {
            System.out.println("DEBUG INFO:reading CSV data(arrhytmia.data)");
            rawData = read("arrhythmia.data");
            if (featureNames != null) { //set the names of the features
                rawData.add(0, featureNames);

            } else {
                featureNames = new String[rawData.get(0).length];
                for (int i = 0; i < featureNames.length; i++) {
                    featureNames[i] = "f" + (i + 1);
                }
                rawData.add(0, featureNames);
            }
            HashMap<Integer, String[]> colVal = new HashMap<>();

            rawData = colRemover(13, rawData); //remove col 13 bc of so many unknowns
            for (int i = 0; i < rawData.size(); i++) {
                for (int j = 0; j < rawData.get(i).length; j++) {
                    if (rawData.get(i)[j].equals("?")) {
                        rowRemover(i, rawData); //remove rows containing unkown values

                    }
                }
            }
            System.out.println("DEBUG INFO:collection a features row value to a string array");
            for (int i = 0; i < rawData.get(0).length; i++) {   // collect each rows of a feature value as a string array inorder to apply dt binerization for each fetures
                colVal.put(i, getAllColVal(i, rawData));
            }

            List<List<String[]>> independentBinfeatures = new ArrayList<>(); //a storage of each independent binerized arrays of a feature


            System.out.println("DEBUG INFO:binerizing independently");
            for (int i = 0; i < colVal.size(); i++) { //binerize independently
                String featName = colVal.get(i)[0];
                if (featName.equals("f2") || featName.equals("f22") || featName.equals("f23") || featName.equals("f24") || featName.equals("f25") || featName.equals("f26") || featName.equals("f27") || featName.equals("f280")) {
                    independentBinfeatures.add(Binerize(colVal.get(i), binerizationMethod.NOMINAL));
                } else {
                    //you can binerize column i with a specific technique by putting some conditions here.however,here every continious valued column is being binerized by one technique.
                    independentBinfeatures.add(Binerize(colVal.get(i), method)); //binerize with the choosen method

                }
            }

            List<String[]> mergedBins = new ArrayList<>();
            int binGroups = independentBinfeatures.size();

            System.out.println("DEBUG INFO:merging indpendently binerized features");
            for (int i = 0; i < independentBinfeatures.get(0).size(); i++) { //merge
                List<String[]> sameRows = new ArrayList<>();
                for (int j = 0; j < binGroups; j++) {
                    sameRows.add(independentBinfeatures.get(j).get(i));
                }
                mergedBins.add(merge(sameRows));
            }

            System.out.println("DEBUG INFO:saving");

            save(mergedBins, "arrhythmia_method="+method+"_class=" + group + ".moses");
            System.out.println("DEBUG:Finished");

        } catch (FileNotFoundException ex) {
            System.out.println("EXCEPTION in reading arrhytmia.data file not found");
        } catch (IOException ex) {
            System.out.println("EXCEPTION in reading arrhytmia.data IO error");
        }
    }

    /**
     * Accepts a CSV file to convert it to a two dimensional arrays of String
     * objects in the form of List
     *
     * @param csvDataFile
     * @return a list representation of a two dimensional arrays of string
     * @throws FileNotFoundException
     * @throws IOException
     */
    public List<String[]> read(String csvDataFile) throws FileNotFoundException, IOException {
        String[] row = null;
        List<String[]> rowSt = new ArrayList<>();
        csvReader = new CSVReader(new FileReader(csvDataFile));
        while ((row = csvReader.readNext()) != null) {

            rowSt.add(row); // add each row of feature values            
        }
        return rowSt;
    }

    public List<String[]> colRemover(int colIndex, List<String[]> rawData) {
        List<String[]> cleanData = new ArrayList<>();
        for (String[] st : rawData) {
            String[] clean = new String[st.length - 1];
            int indx = 0;
            for (int i = 0; i < st.length; i++) {
                if (i != colIndex) {
                    clean[indx] = st[i];
                    indx++;
                }
            }
            cleanData.add(clean);
        }
        return cleanData;
    }

    public void rowRemover(int rowIndex, List<String[]> rawData) { //please don't send index 0(i.e header names) its not handled for the time being
        rawData.remove(rowIndex);
    }

    public List<String[]> Binerize(String[] featureValues, binerizationMethod method) {
        List<String[]> binerizedCol = new ArrayList<>();
        String header = featureValues[0];

        if (method == binerizationMethod.DECILE) {
            binerizedCol = new ArrayList<>();
            String[] headers = new String[]{header + "_0", header + "_1", header + "_2", header + "_3", header + "_4", header + "_5", header + "_6", header + "_7", header + "_8"};

            binerizedCol.add(headers);//add the headers

            for (int i = 1; i < featureValues.length; i++) { // i=0 is for header i.e not a number

                String[] rowVal = new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0"};

                rowVal[decilize(featureValues, Float.parseFloat(AddisAiCommons.toRealNumberString(featureValues[i])))] = "1";
                binerizedCol.add(rowVal);
            }

        }
        if (method == binerizationMethod.MEDIAN) {
            List<Float> floatVal = new ArrayList<>();
            String[] headers = new String[]{header};
            binerizedCol.add(headers);

            for (int i = 1; i < featureValues.length; i++) { // i=0 is for header i.e not a number
                floatVal.add(Float.parseFloat(AddisAiCommons.toRealNumberString(featureValues[i])));

            }
            float median = median(floatVal);
            for (int i = 0; i < floatVal.size(); i++) {
                String[] rowVal = new String[]{"0"};
                if (floatVal.get(i) > median) {
                    rowVal[0] = "1";
                }
                binerizedCol.add(rowVal);
            }

        }
        if (method == binerizationMethod.NOMINAL) {
            String[] headers = new String[]{header};
            binerizedCol.add(headers);
            for (int i = 1; i < featureValues.length; i++) {// i=0 is for header i.e not a number
                String[] rowVal = new String[]{"0"};
                if (header.equals("f280")) {
                    if (featureValues[i].equals(String.valueOf(group.getValue()))) { //compare with class of interest
                        rowVal[0] = "1"; //classify normal vs other type
                    } else {
                        rowVal[0] = "0";
                    }
                }
                if (featureValues[i].equals("1")) {
                    rowVal[0] = "1";
                }
                binerizedCol.add(rowVal);
            }
        }
        return binerizedCol;
    }

    public float median(List<Float> floatVal) {
        float median = 0;
        int count = 0;
        floatVal = MergeSort.mergeSort((ArrayList<Float>) floatVal);
        count = floatVal.size();
        if (count <= 1) {
            median = count;
        } else if (count % 2 == 0) {
            int index = count / 2 - 1;
            median = (floatVal.get(index) + floatVal.get(index + 1)) / 2;
        } else {
            int index = count / 2 - 1;
            median = (floatVal.get(index + 1)) / 2;
        }
        return median;
    }

    public void save(List<String[]> formattedData, String fileName) throws IOException {
        csvWriter = new CSVWriter(new FileWriter(fileName));
        csvWriter.writeAll(formattedData);
        csvWriter.close();
    }

    public String[] merge(List<String[]> independentBinary) { //merges all the binerized columns
        int size = 0;
        for (String[] st : independentBinary) {
            size += st.length;
        }
        String[] combined = new String[size];
        int index = 0;
        for (String[] st : independentBinary) {
            for (int i = 0; i < st.length; i++) {
                combined[index] = st[i];
                index++;
            }
        }
        return combined;
    }

    private static int decilize(String[] featValues, float val) {
        float maxVal = 0, minVal = 0;
        for (int i = 1; i < featValues.length; i++) { //i=0 is the feature name
            float comp = Float.parseFloat(AddisAiCommons.toRealNumberString(featValues[i]));
            if (i == 1) {
                maxVal = minVal = comp;
                continue;
            }
            if (maxVal < comp) {
                maxVal = comp;
            }
            if (minVal > comp) {
                minVal = comp;
            }
        }
        float diff = (maxVal - minVal) / 10;
        float nextRange = minVal + diff;

        int indx = 0; //decilization group (size=9)
        for (int i = 0; i < 9; i++) {
            if (val >= minVal && val < nextRange) {
                indx = i;
                break;
            } else {
                minVal += diff;
                nextRange += diff;
            }
        }
        return indx;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("wrong number of parameters \n usage \n  [methods](0|1) [disease_class](0|..|16)");
        } else {
            MOSESFormatter ms = new MOSESFormatter(null);
            ArrhytmiaParser arparser = new ArrhytmiaParser();
            int m_val = Integer.parseInt(args[0]);
            int c_val = Integer.parseInt(args[1]);
            if (m_val == 0) {
                ms.method = binerizationMethod.MEDIAN;
            } else if (m_val == 1) {
                ms.method = binerizationMethod.DECILE;
            } else {
                System.out.println("Wrong value for method type.valid values are 0 for median and 1 for decile");
                System.exit(0);
            }

            if (c_val == 0) {
                ms.group = classes.NORM;
            } else if (c_val == 1) {
                ms.group = classes.CAD;
            } else if (c_val == 2) {
                ms.group = classes.OAMI;
            } else if (c_val == 3) {
                ms.group = classes.OIMI;
            } else if (c_val == 4) {
                ms.group = classes.ST;
            } else if (c_val == 5) {
                ms.group = classes.SB;
            } else if (c_val == 6) {
                ms.group = classes.PVC;
            } else if (c_val == 7) {
                ms.group = classes.SVC;
            } else if (c_val == 8) {
                ms.group = classes.LBBB;
            } else if (c_val == 9) {
                ms.group = classes.RBBB;
            } else if (c_val == 10) {
                ms.group = classes.DAVB_1;
            } else if (c_val == 11) {
                ms.group = classes.DAVB_2;
            } else if (c_val == 12) {
                ms.group = classes.DAVB_3;
            } else if (c_val == 13) {
                ms.group = classes.LVH;
            } else if (c_val == 14) {
                ms.group = classes.AFF;
            } else if (c_val == 15) {
                ms.group = classes.OTHER;
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
            ms.initFormatting();
        }


    }
}
