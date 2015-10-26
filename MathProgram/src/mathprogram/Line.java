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

    float[][] unaryWeights;
    float[][] binaryWeights;
    float[][] srcWeights;
    float[] outWeights;

    float[][] dUnaryWeights;
    float[][] dBinaryWeights;
    float[][] dSrcWeights;
    float[] dOutWeights;

    float[] lastIn;
    float[] lastOut;
    //float alpha = .0001f;
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
    public float[] copyArray(float[] arr){
        return arr.clone();
    }
    public float[][] copyArray(float arr[][]){
        float[][] newArr = new float[arr.length][];
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
        dUnaryWeights = new float[registers][unaryFunctions.length];
        dBinaryWeights = new float[registers][binaryFunctions.length];
        dSrcWeights = new float[registers][registers];
        dOutWeights = new float[registers];
    }
    public void addInComplexity(){
        applyComplexity(dUnaryWeights, unaryWeights);
        applyComplexity(dBinaryWeights, binaryWeights);
        applyComplexity(dSrcWeights, srcWeights);
        applyComplexity(dOutWeights, outWeights);
        
    }
    public void applyComplexity(float arr[], float arr2[]){
        for(int i = 0; i<arr.length; i++){
            float x = arr2[i];
           
            arr[i] += 10*x*((x-1.0f)*(2*x-1.0f));
                    
        }
    }
    public void applyComplexity(float arr[][], float arr2[][]){
        for(int i = 0; i<arr.length; i++){
            applyComplexity(arr[i], arr2[i]);
        }
    }
    public void applyBackprop(float totalError) {
        //addInComplexity();
        if (totalError < 1.0f) {
           // System.err.println("total error was:" + totalError);
            //totalError = 1.0f;
        }
        /*System.err.println("unary");
        FloatMath.printFloatArr(dUnaryWeights);
         System.err.println("binary");
        FloatMath.printFloatArr(dBinaryWeights);
         System.err.println("out");
        FloatMath.printFloatArr(dOutWeights);
         System.err.println("src");
        FloatMath.printFloatArr(dSrcWeights);*/
        
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

    public void setAllTo(float[] arr, float value) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = value;
        }
    }

    public void setAllTo(float[][] arr, float value) {
        for (int i = 0; i < arr.length; i++) {
            setAllTo(arr[i], value);
        }
    }
    public float getMaxDError() {
        float max = 0.0f;
        
        max = getMaxOf(dOutWeights,max);
        
        max = getMaxOf(dUnaryWeights,max);
        
        max = getMaxOf(dSrcWeights,max);
        
        max = getMaxOf(dBinaryWeights,max);
        
        return max;
    }
    public float getMaxOf(float[] arr, float maxInit){
        float max = maxInit;
        for(int i = 0; i<arr.length; i++){
            if(arr[i]>max){
                max = arr[i];
            }
        }
        return max;
        
    }
    public float getMaxOf(float[][] arr, float maxInit){
        float max = maxInit;
        for(int i = 0; i<arr.length; i++){
            max = getMaxOf(arr[i],max);
        }
        return max;
    }
    public float getTotalDError() {
        float sum = 0.0f;
        sum += getSumOf(dOutWeights);
        sum += getSumOf(dUnaryWeights);
        sum += getSumOf(dSrcWeights);
        sum += getSumOf(dBinaryWeights);
        return sum;
    }

    public float getSumOf(float[] arr) {
        float sum = 0.0f;
        for (float a : arr) {
            sum += Math.abs(a);
        }
        return sum;
    }

    public float getSumOf(float[][] arr) {
        float sum = 0.0f;
        for (float[] a : arr) {
            sum += getSumOf(a);
        }
        return sum;
    }

    public void addTogether(float[] dest, float[] src, float alpha) {
        for (int i = 0; i < dest.length; i++) {
            dest[i] = dest[i] + (alpha * src[i]);
        }
    }

    public void addTogether(float[][] dest, float[][] src, float alpha) {
        for (int i = 0; i < dest.length; i++) {
            addTogether(dest[i], src[i], alpha);
        }
    }

    public float[] getOut(float[] in) {
        this.lastIn = in.clone();

        float[] out = new float[registers];
        for (int output = 0; output < out.length; output++) {
            out[output] = 0;
            float outWeight = outWeights[output];
            float inputK = in[output];
            for (int unary = 0; unary < unaryFunctions.length; unary++) {
                float value = unaryWeights[output][unary] * unaryFunctions[unary].doOperation(outWeight * inputK);

                out[output] += value;
                //System.err.println("output unary" + output + " " + unary + " " + value + " " + unaryFunctions[unary].getClass().getName());
            }

            for (int src = 0; src < binaryWeights.length; src++) {
                float mySrc = in[src];
                for (int binary = 0; binary < binaryFunctions.length; binary++) {
                    float value = binaryWeights[output][binary] * binaryFunctions[binary].doOperation(srcWeights[output][src] * mySrc, outWeight * inputK);

                    out[output] += value;
                    
                    //System.err.println("output binary" + output + " " + binary + " "  + src  + " "+ value);
                }
            }

        }

        lastOut = out.clone();

        return out;
    }

    public float[] backPropSetup(float[] difference, float[] deltaConstant) {
        calculateDOut(difference, deltaConstant);
        calculateDUnary(difference, deltaConstant);
        calculateDBinary(difference, deltaConstant);
        calculateDSrc(difference, deltaConstant);
        return getDeltaConstant();
    }

    public float[] getRandomArr(int length) {
        float arr[] = new float[length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = generator.nextFloat() / 5000;
        }
        return arr;
    }

    public float[][] getRandomArr(int length, int length2) {
        float arr[][] = new float[length][];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = getRandomArr(length2);
        }
        return arr;
    }

    private void calculateDOut(float difference[], float[] deltaConstant) {
        float tempDOut[] = new float[dOutWeights.length];
        for (int out = 0; out < dOutWeights.length; out++) {
            float outWeight = outWeights[out];
            float inputK = lastIn[out];

            for (int unary = 0; unary < unaryWeights[out].length; unary++) {
                tempDOut[out] += unaryWeights[out][unary] * unaryFunctions[unary].doDxOperation(outWeight * inputK) * inputK;
            }

            for (int src = 0; src < binaryWeights.length; src++) {
                float mySrc = lastIn[src];
                for (int binary = 0; binary < binaryFunctions.length; binary++) {
                    tempDOut[out] += binaryWeights[out][binary] * binaryFunctions[binary].doDyOperation(srcWeights[out][src] * mySrc, outWeight * inputK) * inputK;
                    if(tempDOut[out] == 0){
                        System.err.println("hey that's zeroDOut");
                    }
                }
            }

            tempDOut[out] *= difference[out] * deltaConstant[out];
            dOutWeights[out] += tempDOut[out];

        }

    }

    public float[] getDeltaConstant() {
        float tempDeltaConstant[] = new float[dOutWeights.length];
        for (int out = 0; out < dOutWeights.length; out++) {
            float outWeight = outWeights[out];
            float inputK = lastIn[out];

            for (int unary = 0; unary < unaryWeights[out].length; unary++) {
                tempDeltaConstant[out] += outWeight * unaryWeights[out][unary] * unaryFunctions[unary].doDxOperation(outWeight * inputK);
            }

            for (int src = 0; src < binaryWeights.length; src++) {
                float mySrc = lastIn[src];
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

    private void calculateDBinary(float difference[], float[] deltaConstant) {
        float tempDBinaryWeights[][] = new float[dBinaryWeights.length][dBinaryWeights[0].length];
        
        for (int out = 0; out < dOutWeights.length; out++) {
            float outWeight = outWeights[out];
            float inputK = lastIn[out];

            for (int src = 0; src < binaryWeights.length; src++) {
                float mySrc = lastIn[src];
                for (int binary = 0; binary < binaryFunctions.length; binary++) {
                    tempDBinaryWeights[out][binary] += binaryFunctions[binary].doOperation(srcWeights[out][src] * mySrc, outWeight * inputK);
                    if(tempDBinaryWeights[out][binary] == 0){
                        System.err.println("hey that's zero!");
                    }
                }
            }
            for (int a = 0; a < tempDBinaryWeights[out].length; a++) {
                float value = tempDBinaryWeights[out][a] * difference[out] * deltaConstant[out];
                
                dBinaryWeights[out][a] += tempDBinaryWeights[out][a] * difference[out] * deltaConstant[out];
                //System.err.println(value + " im a dik");
            }
        }

    }

    private void calculateDSrc(float difference[], float[] deltaConstant) {
        float[][] tempDSrcWeights = new float[dSrcWeights.length][dSrcWeights[0].length];
        for (int out = 0; out < dOutWeights.length; out++) {
            float outWeight = outWeights[out];
            float inputK = lastIn[out];

            for (int src = 0; src < binaryWeights.length; src++) {
                float mySrc = lastIn[src];
                for (int binary = 0; binary < binaryFunctions.length; binary++) {
                    tempDSrcWeights[out][src] += binaryWeights[out][binary] * binaryFunctions[binary].doDxOperation(srcWeights[out][src] * mySrc, outWeight * inputK) * mySrc;
                }
            }
            for (int a = 0; a < tempDSrcWeights[out].length; a++) {
                dSrcWeights[out][a] += tempDSrcWeights[out][a] * difference[out] * deltaConstant[out];
            }
        }

    }

    private void calculateDUnary(float difference[], float[] deltaConstant) {
        float[][] tempDUnaryWeights = new float[dUnaryWeights.length][dUnaryWeights[0].length];
        for (int out = 0; out < dOutWeights.length; out++) {
            float outWeight = outWeights[out];
            float inputK = lastIn[out];

            for (int unary = 0; unary < unaryWeights[out].length; unary++) {
                tempDUnaryWeights[out][unary] += unaryWeights[out][unary] * unaryFunctions[unary].doOperation(outWeight * inputK);
            }

            for (int a = 0; a < tempDUnaryWeights[out].length; a++) {
                dUnaryWeights[out][a] += tempDUnaryWeights[out][a] * difference[out] * deltaConstant[out];
            }

        }
    }

    

}
