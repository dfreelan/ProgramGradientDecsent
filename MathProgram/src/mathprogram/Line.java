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

    int registers;
    public Line clone(){
        Line newLine = new Line(unaryFunctions,binaryFunctions,registers);
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
        
        return newLine;
    }
    public double[] copyArray(double[] arr){
        return arr.clone();
    }
    public double[][] copyArray(double arr[][]){
        double[][] newArr = new double[arr.length][];
        for(int i = 0; i<arr.length; i++){
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

    public void addInComplexity(){
        applyComplexity(dUnaryWeights, unaryWeights);
        applyComplexity(dBinaryWeights, binaryWeights);
        applyComplexity(dSrcWeights, srcWeights);
        applyComplexity(dOutWeights, outWeights);
        
    }
    public void applyComplexity(double arr[], double arr2[]){
        for(int i = 0; i<arr.length; i++){
            double x = arr2[i];
           
            arr[i] += 10*x*((x-1.0f)*(2*x-1.0f));
                    
        }
    }
    public void applyComplexity(double arr[][], double arr2[][]){
        for(int i = 0; i<arr.length; i++){
            applyComplexity(arr[i], arr2[i]);
        }
    }
    public void applyBackprop(double totalError) {
        //addInComplexity();
        if (totalError < 1.0f) {
           // System.err.println("total error was:" + totalError);
            //totalError = 1.0f;
        }
        /*System.err.println("unary");
        doubleMath.printdoubleArr(dUnaryWeights);
         System.err.println("binary");
        doubleMath.printdoubleArr(dBinaryWeights);
         System.err.println("out");
        doubleMath.printdoubleArr(dOutWeights);
         System.err.println("src");
        doubleMath.printdoubleArr(dSrcWeights);*/
        
        addTogether(unaryWeights, dUnaryWeights, Program.alpha / totalError);
        addTogether(binaryWeights, dBinaryWeights, Program.alpha / totalError);
        addTogether(srcWeights, dSrcWeights, Program.alpha / totalError);
        addTogether(outWeights, dOutWeights, Program.alpha / totalError);

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
        
        max = getMaxOf(dOutWeights,max);
        
        max = getMaxOf(dUnaryWeights,max);
        
        max = getMaxOf(dSrcWeights,max);
        
        max = getMaxOf(dBinaryWeights,max);
        
        return max;
    }
    public double getMaxOf(double[] arr, double maxInit){
        double max = maxInit;
        for(int i = 0; i<arr.length; i++){
            if(arr[i]>max){
                max = arr[i];
            }
        }
        return max;
        
    }
    public double getMaxOf(double[][] arr, double maxInit){
        double max = maxInit;
        for(int i = 0; i<arr.length; i++){
            max = getMaxOf(arr[i],max);
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

    public double[] backPropSetup(double[] difference, double[] deltaConstant) {
        calculateDOut(difference, deltaConstant);
        calculateDUnary(difference, deltaConstant);
        calculateDBinary(difference, deltaConstant);
        calculateDSrc(difference, deltaConstant);
        return getDeltaConstant();
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

    private void calculateDOut(double difference[], double[] deltaConstant) {
        double tempDOut[] = new double[dOutWeights.length];
        for (int out = 0; out < dOutWeights.length; out++) {
            double outWeight = outWeights[out];
            double inputK = lastIn[out];

            for (int unary = 0; unary < unaryWeights[out].length; unary++) {
                tempDOut[out] += unaryWeights[out][unary] * unaryFunctions[unary].doDxOperation(outWeight * inputK) * inputK;
            }

            for (int src = 0; src < binaryWeights.length; src++) {
                double mySrc = lastIn[src];
                for (int binary = 0; binary < binaryFunctions.length; binary++) {
                    tempDOut[out] += binaryWeights[out][binary] * binaryFunctions[binary].doDyOperation(srcWeights[out][src] * mySrc, outWeight * inputK) * inputK;
                    if(tempDOut[out] == 0){
                        //System.err.println("hey that's zeroDOut");
                    }
                }
            }

            tempDOut[out] *= difference[out] * deltaConstant[out];
            dOutWeights[out] += tempDOut[out];

        }

    }

    public double[] getDeltaConstant() {
        double tempDeltaConstant[] = new double[dOutWeights.length];
        for (int out = 0; out < dOutWeights.length; out++) {
            double outWeight = outWeights[out];
            double inputK = lastIn[out];

            for (int unary = 0; unary < unaryWeights[out].length; unary++) {
                tempDeltaConstant[out] += outWeight * unaryWeights[out][unary] * unaryFunctions[unary].doDxOperation(outWeight * inputK);
            }

            for (int src = 0; src < binaryWeights.length; src++) {
                double mySrc = lastIn[src];
                for (int binary = 0; binary < binaryFunctions.length; binary++) {
                    tempDeltaConstant[out] += outWeight * binaryWeights[out][binary] * binaryFunctions[binary].doDyOperation(srcWeights[out][src] * mySrc, outWeight * inputK);
                    if( tempDeltaConstant[out]==0.0f){
                        //System.err.println( "delta const so far at one " + tempDeltaConstant[out]);
                        //System.exit(0);
                    }
                }
            }

        }
        return tempDeltaConstant;
    }

    private void calculateDBinary(double difference[], double[] deltaConstant) {
        double tempDBinaryWeights[][] = new double[dBinaryWeights.length][dBinaryWeights[0].length];
        
        for (int out = 0; out < dOutWeights.length; out++) {
            double outWeight = outWeights[out];
            double inputK = lastIn[out];

            for (int src = 0; src < binaryWeights.length; src++) {
                double mySrc = lastIn[src];
                for (int binary = 0; binary < binaryFunctions.length; binary++) {
                    tempDBinaryWeights[out][binary] += binaryFunctions[binary].doOperation(srcWeights[out][src] * mySrc, outWeight * inputK);
                    if(tempDBinaryWeights[out][binary] == 0){
                        //System.err.println("hey that's zero!");
                    }
                }
            }
            for (int a = 0; a < tempDBinaryWeights[out].length; a++) {
                double value = tempDBinaryWeights[out][a] * difference[out] * deltaConstant[out];
                
                dBinaryWeights[out][a] += tempDBinaryWeights[out][a] * difference[out] * deltaConstant[out];
                //System.err.println(value + " im a dik");
            }
        }

    }

    private void calculateDSrc(double difference[], double[] deltaConstant) {
        double[][] tempDSrcWeights = new double[dSrcWeights.length][dSrcWeights[0].length];
        for (int out = 0; out < dOutWeights.length; out++) {
            double outWeight = outWeights[out];
            double inputK = lastIn[out];

            for (int src = 0; src < binaryWeights.length; src++) {
                double mySrc = lastIn[src];
                for (int binary = 0; binary < binaryFunctions.length; binary++) {
                    tempDSrcWeights[out][src] += binaryWeights[out][binary] * binaryFunctions[binary].doDxOperation(srcWeights[out][src] * mySrc, outWeight * inputK) * mySrc;
                }
            }
            for (int a = 0; a < tempDSrcWeights[out].length; a++) {
                dSrcWeights[out][a] += tempDSrcWeights[out][a] * difference[out] * deltaConstant[out];
            }
        }

    }

    private void calculateDUnary(double difference[], double[] deltaConstant) {
        double[][] tempDUnaryWeights = new double[dUnaryWeights.length][dUnaryWeights[0].length];
        for (int out = 0; out < dOutWeights.length; out++) {
            double outWeight = outWeights[out];
            double inputK = lastIn[out];

            for (int unary = 0; unary < unaryWeights[out].length; unary++) {
                tempDUnaryWeights[out][unary] += unaryWeights[out][unary] * unaryFunctions[unary].doOperation(outWeight * inputK);
            }

            for (int a = 0; a < tempDUnaryWeights[out].length; a++) {
                dUnaryWeights[out][a] += tempDUnaryWeights[out][a] * difference[out] * deltaConstant[out];
            }

        }
    }

    

}
