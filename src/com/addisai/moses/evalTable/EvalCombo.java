package com.addisai.moses.evalTable;

import com.addisai.addisaicommons.OpenCogTools;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EvalCombo {
    /*
     A cofusion matrix( Kohavi and provost, 1998) a,b,c and d are counts
     _________________________________________________________
     |                          |    Predicted               |
     |                          _________________________
     |                          |  Negative    |   Positive  |
     |_______________________________________________________|
     |   Actual   |   Negative  |    a         |   b         | 
     |____________|_____________|______________|_____________|
     |   Actual   |   Positive  |    c         |   d         | 
     |____________|_____________|______________|_____________|

     */

    private static int confusionMatrix[] = new int[4];// index of  0=>a ,1=>b,2=>c and 4=>d
    private int realOutput[];
    private int appaarentOutput[];
    private static String rootDir;
    private static float maxAccuracy = 0;
    private static float maxPrecision = 0;
    private static String bestmodelFileName;

    public static void main(String[] args) {
        boolean DEBUG =false;
        if (!DEBUG) {
            if (args.length == 0) {
                System.out.println("invalid argument.please rerun with the correct input");
                return;
            } else {

                rootDir = args[0];
                //System.out.println("WORKING DIR:"+rootDir); 
                //return;
            }
        }
        System.out.println(System.getProperty("user.dir"));
        if (DEBUG) {
            rootDir = "./mout/preparedCrx/FOLD2/FOLD2";
        }
        try {

            BufferedReader reader = new BufferedReader(new FileReader(rootDir + ".out"));

            String singleModel;
            int index = 0;
            File f = new File(rootDir + "_EVAL");
            if (!f.mkdir()) {
                throw new Exception("Cant create dir.the folder may already exist.");
            }
            while ((singleModel = reader.readLine()) != null) {
                String fmodel = f.getAbsolutePath() + "/model" + index + ".model";
                write(singleModel, fmodel);
                OpenCogTools.execEvalTable(f.getAbsolutePath() + "/eval.log", f.getAbsolutePath() + "/model" + index + ".model", rootDir + ".test", f.getAbsolutePath() + "/eval" + index + ".result");
                int actuals[] = getOutpts(rootDir + ".test");
                int results[] = getOutpts(rootDir + "_EVAL" + "/eval" + index + ".result" + "0");/*0 is added bc thats how evaltable is saving*/
                confusionMatrix = new int[]{0, 0, 0, 0}; //reset confusion matrix for the new model
                for (int i = 0; i < actuals.length; i++) {
                    if (actuals[i] == 0 && results[i] == 0) { //true negative
                        confusionMatrix[0]++;
                    }
                    if (actuals[i] == 0 && results[i] == 1) { //false positive
                        confusionMatrix[1]++;
                    }
                    if (actuals[i] == 1 && results[i] == 0) { //false negative
                        confusionMatrix[2]++;
                    }
                    if (actuals[i] == 1 && results[i] == 1) { //true positive
                        confusionMatrix[3]++;
                    }
                }
                String confMatrixFile = f.getAbsolutePath() + "/confmat" + index + ".txt";
                String info = "True_negative:" + confusionMatrix[0] + "\nFalse_positive:" + confusionMatrix[1] + "\nFalse_negative:" + confusionMatrix[2] + "\nTrue_positive:" + confusionMatrix[3];
                ConfusionMatrix cm = new ConfusionMatrix(confusionMatrix);
                info = info + "\n\nTrue_nagative rate:" + cm.getTrueNegativeRate() + "\nFalse_positive rate:" + cm.getFalsePositiveRate()
                        + "\nFalse_negative rate" + cm.getFalseNegativeRate() + "\nTrue_positve rate:" + cm.getTruePositiveRate()
                        + "\nAccuracy" + cm.getAccuracy() + "\nPrecision" + cm.getPrecision();
                if (index == 0) {
                    maxAccuracy = cm.getAccuracy();
                    maxPrecision = cm.getPrecision();
                    bestmodelFileName=confMatrixFile;
                } else {
                    if (maxAccuracy < cm.getAccuracy()) {
                        maxAccuracy = cm.getAccuracy();
                        bestmodelFileName=confMatrixFile;
                    }
                    if (maxPrecision < cm.getPrecision()) {
                        maxPrecision = cm.getPrecision();
                    }
                }
                write(bestmodelFileName+","+maxAccuracy+"\n",System.getProperty("user.dir")+"/bestModels.txt");
                write(info, confMatrixFile);
                index++;
            }
            System.out.println("BEST MODEL:"+bestmodelFileName+" with accuracy of"+maxAccuracy);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void write(String line, String filepath) throws IOException {
        //System.out.println("saving to"+filepath);
        File f = new File(filepath);
        if (!f.exists()) {
            f.createNewFile();
        }

        FileWriter fwriter = new FileWriter(f, true);
        BufferedWriter writer = new BufferedWriter(fwriter);
        writer.write(line);
        writer.close();

    }

    public static int[] getOutpts(String file) {
        ArrayList<Integer> output = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            String[] array = line.split(",");
            int outputIndex = array.length - 1;
            while ((line = reader.readLine()) != null) {
                array = line.split(",");
                output.add(Integer.parseInt(array[outputIndex]));
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found:" + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IO exception" + ex.getMessage());
        }
        int[] outs = new int[output.size()];
        for (int i = 0; i < outs.length; i++) {
            outs[i] = output.get(i);
        }
        return outs;
    }
}
