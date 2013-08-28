package com.addisai.moses.evalTable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class ConfusionMatrix {
    
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
    private int confusionMatrix[] = new int[4];// index of  0=>a ,1=>b,2=>c and 3=>d

    public ConfusionMatrix(int[] confusionMatrix) {
        this.confusionMatrix = confusionMatrix;
    }  

    public float getAccuracy() {        
        return ((float)(confusionMatrix[0]+confusionMatrix[3])/(float)(confusionMatrix[0]+confusionMatrix[1]+confusionMatrix[2]+confusionMatrix[3]));
    }

    public float getTruePositiveRate() {
       return ((float)confusionMatrix[3]/(float)(confusionMatrix[2]+confusionMatrix[3]));
    }

    public float getFalsePositiveRate() {
        return ((float)confusionMatrix[1]/(float)(confusionMatrix[0]+confusionMatrix[1]));
    }

    public float getTrueNegativeRate() {
        return ((float)confusionMatrix[0]/(float)(confusionMatrix[0]+confusionMatrix[1]));
    }

    public float getFalseNegativeRate() {
        return ((float)confusionMatrix[2]/(float)(confusionMatrix[2]+confusionMatrix[3]));
    }

    public float getPrecision() {
        return ((float)confusionMatrix[3]/(float)(confusionMatrix[1]+confusionMatrix[3]));
    }
    public static void main(String[] args) { //just for test
        int [] testmatrix=new int[]{17,8,2,0};
        ConfusionMatrix cm=new ConfusionMatrix(testmatrix);
        System.out.println("Acc:"+cm.getAccuracy()+" Prec:"+cm.getPrecision());
    }
}
