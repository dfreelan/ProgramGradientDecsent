/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mathprogram;

import java.util.Random;
import mathprogram.operators.BinaryOperator;
import mathprogram.operators.UnaryOperator;

/**
 *
 * @author dfreelan
 */
public class Line {

    double[][] unaryWeights;
    double[][] binaryWeights;
    double[][] srcWeights;
    double[] outWeights;

    double[][] dUnaryWeights;
    double[][] dBinaryWeights;
    double[][] dSrcWeights;
    double[] dOutWeights;

    double[] lastIn;
    double[] lastOut;

    //double alpha = .0001f;
    static Random generator = new Random();

    UnaryOperator[] unaryFunctions;
    BinaryOperator[] binaryFunctions;
    double prevR[] = new double[1];
    double prevS[];
    double prevSi[];
    int registers;

    public Line clone() {
        Line newLine = new Line(unaryFunctions, binaryFunctions, registers);
        newLine.dBinaryWeights = copyArray(dBinaryWeights);
        newLine.binaryWeights = copyArray(binaryWeights);
        newLine.dUnaryWeights = copyArray(dUnaryWeights);
        newLine.unaryWeights = copyArray(unaryWeights);
        newLine.dOutWeights = copyArray(dOutWeights);
        newLine.outWeights = copyArray(outWeights);
        newLine.dSrcWeights = copyArray(dSrcWeights);
        newLine.srcWeights = copyArray(srcWeights);
        newLine.lastIn = copyArray(lastIn);
        newLine.lastOut = copyArray(lastOut);
        newLine.prevR = copyArray(prevR);
        newLine.prevS = copyArray(prevS);
        newLine.prevSi = copyArray(prevSi);

        return newLine;
    }

    public double[] copyArray(double[] arr) {
        return arr.clone();
    }

    public double[][] copyArray(double arr[][]) {
        double[][] newArr = new double[arr.length][];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i].clone();
        }
        return newArr;
    }

    public Line(UnaryOperator[] unaryFunctions, BinaryOperator[] binaryFunctions, int registers) {

        this.unaryFunctions = unaryFunctions;
        this.binaryFunctions = binaryFunctions;
        unaryWeights = getRandomArr(registers, unaryFunctions.length);
        binaryWeights = getRandomArr(registers, binaryFunctions.length);
        srcWeights = getRandomArr(registers, registers);
        outWeights = getRandomArr(registers);

        this.registers = registers;

        initDelta();
    }

    public void initDelta() {
        dUnaryWeights = new double[registers][unaryFunctions.length];
        dBinaryWeights = new double[registers][binaryFunctions.length];
        dSrcWeights = new double[registers][registers];
        dOutWeights = new double[registers];
    }

    public void addInComplexity() {
        applyComplexity(dUnaryWeights, unaryWeights);
        applyComplexity(dBinaryWeights, binaryWeights);
        applyComplexity(dSrcWeights, srcWeights);
        applyComplexity(dOutWeights, outWeights);

    }

    public void applyComplexity(double arr[], double arr2[]) {
        for (int i = 0; i < arr.length; i++) {
            double x = arr2[i];

            arr[i] += 10 * x * ((x - 1.0f) * (2 * x - 1.0f));

        }
    }

    public void applyComplexity(double arr[][], double arr2[][]) {
        for (int i = 0; i < arr.length; i++) {
            applyComplexity(arr[i], arr2[i]);
        }
    }

    public void applyBackprop(double totalError) {
        //addInComplexity();
        if (totalError < 1.0f) {
             System.err.println("total error was:" + totalError);
            totalError = 1.0f;
        }
        /*System.err.println("alpha over err " + Program.alpha/totalError);
        System.err.println("unary");
         DoubleMath.printdoubleArr(dUnaryWeights);
         System.err.println("binary");
         DoubleMath.printdoubleArr(dBinaryWeights);
         System.err.println("out");
         DoubleMath.printdoubleArr(dOutWeights);
         System.err.println("src");
         DoubleMath.printdoubleArr(dSrcWeights);
         System.err.println("total error:"  + totalError);*/
        addTogether(unaryWeights, dUnaryWeights, Program.alpha / totalError);
        addTogether(binaryWeights, dBinaryWeights, Program.alpha / totalError);
        addTogether(srcWeights, dSrcWeights, Program.alpha / totalError);
        addTogether(outWeights, dOutWeights, Program.alpha / totalError);

        initDelta();
    }
    public void applyBackpropMaxOnly(double totalError) {
        //addInComplexity();
        if (totalError < 1.0f) {
            // System.err.println("total error was:" + totalError);
            //totalError = 1.0f;
        }
        /*System.err.println("unary");
         DoubleMath.printdoubleArr(dUnaryWeights);
         System.err.println("binary");
         DoubleMath.printdoubleArr(dBinaryWeights);
         System.err.println("out");
         DoubleMath.printdoubleArr(dOutWeights);
         System.err.println("src");
         DoubleMath.printdoubleArr(dSrcWeights);*/
        
        addTogether(unaryWeights, dUnaryWeights, Program.alpha / totalError, totalError);
        addTogether(binaryWeights, dBinaryWeights, Program.alpha / totalError, totalError);
        addTogether(srcWeights, dSrcWeights, Program.alpha / totalError, totalError);
        addTogether(outWeights, dOutWeights, Program.alpha / totalError,totalError);

        initDelta();
    }
    public void applyBackpropRandomN(double totalError, int n) {
        //addInComplexity();
        if (totalError < 1.0f) {
            // System.err.println("total error was:" + totalError);
            //totalError = 1.0f;
        }
        /*System.err.println("unary");
         DoubleMath.printdoubleArr(dUnaryWeights);
         System.err.println("binary");
         DoubleMath.printdoubleArr(dBinaryWeights);
         System.err.println("out");
         DoubleMath.printdoubleArr(dOutWeights);
         System.err.println("src");
         DoubleMath.printdoubleArr(dSrcWeights);*/
        for(int i = 0; i<n; i++){
            double manipulatedArr[];
            double deltaArr[];
            int index = 0;
            switch(generator.nextInt(4)){
                case 0:
                    index = generator.nextInt(binaryWeights.length);
                    manipulatedArr = binaryWeights[index]; 
                    deltaArr = dBinaryWeights[index];
                    break;
                case 1:
                    index = generator.nextInt(unaryWeights.length);
                    manipulatedArr = unaryWeights[index]; 
                    deltaArr = dUnaryWeights[index];
                    break;
                case 2:
                    index = generator.nextInt(srcWeights.length);
                    manipulatedArr = srcWeights[index]; 
                    deltaArr = dSrcWeights[index];
                    break;
                case 3:
                    manipulatedArr = outWeights; 
                    deltaArr = dOutWeights;
                    break;
                default:
                    manipulatedArr = null;
                    deltaArr = null;

            }

            index = generator.nextInt(manipulatedArr.length);

            manipulatedArr[index] = manipulatedArr[index] + (Program.alpha/totalError*deltaArr[index]);
        }
        initDelta();
    }
    void setToDefault() {
        setAllTo(unaryWeights, 0.0f);
        for (int i = 0; i < unaryWeights.length; i++) {
            unaryWeights[i][0] = 1.0f;
        }
        setAllTo(binaryWeights, 0.0f);
        setAllTo(outWeights, 1.0f);
        setAllTo(srcWeights, 1.0f);
    }

    public void setAllTo(double[] arr, double value) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = value;
        }
    }

    public void setAllTo(double[][] arr, double value) {
        for (int i = 0; i < arr.length; i++) {
            setAllTo(arr[i], value);
        }
    }

    public double getMaxDError() {
        double max = 0.0f;

        max = getMaxOf(dOutWeights, max);

        max = getMaxOf(dUnaryWeights, max);

        max = getMaxOf(dSrcWeights, max);

        max = getMaxOf(dBinaryWeights, max);

        return max;
    }

    public double getMaxOf(double[] arr, double maxInit) {
        double max = maxInit;
        for (int i = 0; i < arr.length; i++) {
            if (Math.abs(arr[i]) > max) {
                max = Math.abs(arr[i]);
            }
        }
        return max;

    }

    public double getMaxOf(double[][] arr, double maxInit) {
        double max = maxInit;
        for (int i = 0; i < arr.length; i++) {
            max = getMaxOf(arr[i], max);
        }
        return max;
    }

    public double getTotalDError() {
        double sum = 0.0f;
        sum += getSumOf(dOutWeights);
        sum += getSumOf(dUnaryWeights);
        sum += getSumOf(dSrcWeights);
        sum += getSumOf(dBinaryWeights);
        return sum;
    }

    public double getSumOf(double[] arr) {
        double sum = 0.0f;
        for (double a : arr) {
            sum += Math.abs(a);
        }
        return sum;
    }

    public double getSumOf(double[][] arr) {
        double sum = 0.0f;
        for (double[] a : arr) {
            sum += getSumOf(a);
        }
        return sum;
    }

    public void addTogether(double[] dest, double[] src, double alpha) {
        for (int i = 0; i < dest.length; i++) {
            
                dest[i] = dest[i] + (alpha * src[i]);
        }
    }

    public void addTogether(double[][] dest, double[][] src, double alpha) {
        for (int i = 0; i < dest.length; i++) {
            addTogether(dest[i], src[i], alpha);
        }
    }
    
    public void addTogether(double[] dest, double[] src, double alpha, double value) {
        for (int i = 0; i < dest.length; i++) {
            if(Math.abs(src[i]) == value){
              System.err.println("this happened");
              dest[i] = dest[i] + (alpha * src[i]);
            }
            //System.err.println("desti" + src[i]);
           //System.err.println("value " + value);
        }
    }

    public void addTogether(double[][] dest, double[][] src, double alpha, double value) {
        
        for (int i = 0; i < dest.length; i++) {
            addTogether(dest[i], src[i], alpha,value);
        }
    }

    public double[] getOut(double[] in) {
        this.lastIn = in.clone();

        double[] out = new double[registers];
        for (int output = 0; output < out.length; output++) {
            out[output] = 0;
            double outWeight = outWeights[output];
            double inputK = in[output];
            for (int unary = 0; unary < unaryFunctions.length; unary++) {
                double value = unaryWeights[output][unary] * unaryFunctions[unary].doOperation(outWeight * inputK);

                out[output] += value;
                //System.err.println("output unary" + output + " " + unary + " " + value + " " + unaryFunctions[unary].getClass().getName());
            }

            for (int src = 0; src < binaryWeights.length; src++) {
                double mySrc = in[src];
                for (int binary = 0; binary < binaryFunctions.length; binary++) {
                    double value = binaryWeights[output][binary] * binaryFunctions[binary].doOperation(srcWeights[output][src] * mySrc, outWeight * inputK);

                    out[output] += value;

                    //System.err.println("output binary" + output + " " + binary + " "  + src  + " "+ value);
                }
            }

        }

        lastOut = out.clone();

        return out;
    }

    public double[][]  backPropSetup(double[] difference, double prevInfo[][]) {
        prevR = prevInfo[0];
        prevS = prevInfo[1];
        prevSi = prevInfo[2];
        calculateDOut(difference);
        calculateDUnary(difference);
        calculateDBinary(difference);
        calculateDSrc(difference);
        
        return calculateDelta();
    }

    public double[] getRandomArr(int length) {
        double arr[] = new double[length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = generator.nextDouble() / 5000;
        }
        return arr;
    }

    public double[][] getRandomArr(int length, int length2) {
        double arr[][] = new double[length][];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = getRandomArr(length2);
        }
        return arr;
    }

    private void calculateDOut(double difference[]) {
        double tempDOut[] = new double[dOutWeights.length];
        for (int out = 0; out < dOutWeights.length; out++) {
            double outWeight = outWeights[out];
            double inputK = lastIn[out];
            double prevR = 0;
            if(out==0){
                prevR = this.prevR[0];
            }
            for (int unary = 0; unary < unaryWeights[out].length; unary++) {
                tempDOut[out] += unaryWeights[out][unary] * unaryFunctions[unary].doDxOperation(outWeight * inputK) * inputK;
            }

            for (int src = 0; src < binaryWeights.length; src++) {
                double mySrc = lastIn[src];
                for (int binary = 0; binary < binaryFunctions.length; binary++) {
                    tempDOut[out] += binaryWeights[out][binary] * binaryFunctions[binary].doDyOperation(srcWeights[out][src] * mySrc, outWeight * inputK) * inputK;
                    if (tempDOut[out] == 0) {
                        //System.err.println("hey that's zeroDOut");
                    }
                }
            }

            tempDOut[out] *= difference[0] * (prevR + prevS[out] + prevSi[out]);
            dOutWeights[out] += tempDOut[out];

        }

    }

    public double[][] calculateDelta() {
        double newR[] = new double[prevR.length];
        double newS[] = new double[prevS.length];
        double newSi[] = new double[prevSi.length];

        newR[0] = prevR[0] * (getDeltaU(0) + getDeltaY(0));
        
        for (int s = 0; s < newS.length; s++) {
            newS[s] = (prevS[s] + prevSi[s]) * (getDeltaU(s) + getDeltaY(s));
        }
        for (int si = 0; si < newSi.length; si++) {
            newSi[si] = prevR[0] * getDeltaX(si, 0);
            for (int s = 0; s < newSi.length; s++) {
                newSi[si] += (prevS[s] + prevSi[s]) * getDeltaX(si, s);
            }
        }

        //prevR = newR;
        //prevS = newS;
        //prevSi = newSi;
        double[][] packedInfo = new double[3][];
        //System.err.println("newSi is");
        //DoubleMath.printdoubleArr(newSi);
        double max = this.getMaxOf(newR, 0);
        max = this.getMaxOf(newS, max);
        max = this.getMaxOf(newSi, max);
        max*=100;
        
        //DoubleMath.divAllBy(newR, max);
        //DoubleMath.divAllBy(newS, max);
        //DoubleMath.divAllBy(newSi, max);
        
        packedInfo[0] = newR;
        packedInfo[1] = newS;
        packedInfo[2] = newSi;
        return packedInfo;
    }
    public void applyMask(double prob){
        zeroWithProb(dOutWeights,prob);
        zeroWithProb(dUnaryWeights,prob);
        zeroWithProb(dSrcWeights,prob);
        zeroWithProb(dBinaryWeights,prob);
    }
    private void zeroWithProb(double[] arr, double prob){
        for(int i = 0; i<arr.length; i++){
            if(generator.nextDouble() > prob){
                arr[i] = 0;
            }
        }
    }
    private void zeroWithProb(double[][] arr, double prob){
        for(int i = 0; i<arr.length; i++){
            zeroWithProb(arr[i],prob);
        }
    }
    private void calculateDBinary(double difference[]) {
        double tempDBinaryWeights[][] = new double[dBinaryWeights.length][dBinaryWeights[0].length];
            
        for (int out = 0; out < dOutWeights.length; out++) {
            double prevR = 0;
            if(out==0){
                prevR = this.prevR[0];
            }
            double outWeight = outWeights[out];
            double inputK = lastIn[out];

            for (int src = 0; src < binaryWeights.length; src++) {
                double mySrc = lastIn[src];
                for (int binary = 0; binary < binaryFunctions.length; binary++) {
                    tempDBinaryWeights[out][binary] += binaryFunctions[binary].doOperation(srcWeights[out][src] * mySrc, outWeight * inputK);
                }
            }
            for (int a = 0; a < tempDBinaryWeights[out].length; a++) {
                double value = tempDBinaryWeights[out][a] * difference[0] *(prevR + prevS[out] + prevSi[out]);

                dBinaryWeights[out][a] += value;
                //System.err.println(value + " im a dik");
            }
        }

    }

    private void calculateDSrc(double difference[]) {
        double[][] tempDSrcWeights = new double[dSrcWeights.length][dSrcWeights[0].length];
        for (int out = 0; out < dOutWeights.length; out++) {
            double prevR = 0;
            if(out==0){
                prevR = this.prevR[0];
            }
            double outWeight = outWeights[out];
            double inputK = lastIn[out];

            for (int src = 0; src < binaryWeights.length; src++) {
                double mySrc = lastIn[src];
                for (int binary = 0; binary < binaryFunctions.length; binary++) {
                    tempDSrcWeights[out][src] += binaryWeights[out][binary] * binaryFunctions[binary].doDxOperation(srcWeights[out][src] * mySrc, outWeight * inputK) * mySrc;
                }
            }
            for (int a = 0; a < tempDSrcWeights[out].length; a++) {
                dSrcWeights[out][a] += tempDSrcWeights[out][a] * difference[0] * (prevR + prevS[out] + prevSi[out]);
            }
        }

    }

    private void calculateDUnary(double difference[]) {
        double[][] tempDUnaryWeights = new double[dUnaryWeights.length][dUnaryWeights[0].length];
        for (int out = 0; out < dOutWeights.length; out++) {
            double prevR = 0;
            if(out==0){
                prevR = this.prevR[0];
            }
            double outWeight = outWeights[out];
            double inputK = lastIn[out];

            for (int unary = 0; unary < unaryWeights[out].length; unary++) {
                tempDUnaryWeights[out][unary] += unaryFunctions[unary].doOperation(outWeight * inputK);
            }

            for (int a = 0; a < tempDUnaryWeights[out].length; a++) {
                dUnaryWeights[out][a] += tempDUnaryWeights[out][a] * difference[0] *(prevR + prevS[out] + prevSi[out]);
            }

        }
    }
    private double getDeltaX(int dest){
        double sum = 0;
        for(int src = 0; src<lastIn.length; src++){
            sum+= getDeltaX(src,dest);
        }
        return sum;
        
    }
     private double getDeltaY(int dest){
        double sum = 0;
        for(int src = 0; src<lastOut.length; src++){
            sum += getDeltaY(src,dest);
        }
        return sum;
        
    }
    private double getDeltaX(int src, int dest) {
        double sum = 0;
        for (int binary = 0; binary < binaryFunctions.length; binary++) {
            sum += binaryWeights[dest][binary] * binaryFunctions[binary].doDxOperation(srcWeights[dest][src] * lastIn[src], outWeights[dest] * lastIn[dest]);
        }
        sum *= lastIn[src];
        return sum;
    }

    private double getDeltaY(int src, int dest) {
        double sum = 0;
        for (int binary = 0; binary < binaryFunctions.length; binary++) {
            sum += binaryWeights[dest][binary] * binaryFunctions[binary].doDyOperation(srcWeights[dest][src] * lastIn[src], outWeights[dest] * lastIn[dest]) * lastIn[dest];
           
        }
        return sum;
    }

    private double getDeltaU(int dest) {
        double sum = 0;
        for (int unary = 0; unary < unaryWeights[dest].length; unary++) {
            sum += unaryFunctions[unary].doDxOperation(outWeights[dest] * lastIn[dest]);
        }
        sum *= outWeights[dest];
        return sum;
    }

}
