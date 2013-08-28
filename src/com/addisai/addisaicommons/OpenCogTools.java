package com.addisai.addisaicommons;




import java.io.BufferedReader;
import java.io.InputStreamReader;


public class OpenCogTools {

    /**
     * path to opencog's build directory
     */
    public static String OPENCOG_HOME = "/usr/local/src/opencog/bin/opencog";
    /**
     * executes moses program assuming moses has been granted execution mode
     */
    public static String MOSES_EXEC = OPENCOG_HOME + "/learning/moses/main";
    /**
     * executes eval-table program assuming eval-table has been granted
     * execution mode
     */
    public static String EVAL_TABALE_EXEC = OPENCOG_HOME + "/comboreduct/main";

    public static void execEvalTable(String logf, String modelf, String testf, String resultf) throws Exception {
        if (modelf == null || testf == null) {
            throw new Exception("training and testing files cannot be null");
        } 
        System.out.println("eval-table " + " -i " +testf  + " -f " +logf + " -C " + modelf + " -o " +resultf);
        
        Process p = Runtime.getRuntime().exec("eval-table " + " -i " +testf  + " -f " +logf + " -C " +modelf + " -o " +resultf);
        p.waitFor();
        BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuffer sb=new StringBuffer();
        String line =reader.readLine();
        sb.append(line);
        while(line!=null){
            line=reader.readLine();
            sb.append(line);
        }
        System.out.println(sb);
    }
}
