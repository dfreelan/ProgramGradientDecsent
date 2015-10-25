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
    static Random generator = new Random();
    
    UnaryOperator[] unaryFunctions;
    BinaryOperator[] binaryFunctions;
    
    int registers;
    public Line(UnaryOperator[] unaryFunctions, BinaryOperator[] binaryFunctions, int registers) {
        
        
        this.unaryFunctions = unaryFunctions;
        this.binaryFunctions = binaryFunctions;
        unaryWeights = getRandomArr(registers,unaryFunctions.length);
        binaryWeights = getRandomArr(registers,binaryFunctions.length);
        srcWeights = getRandomArr(registers,registers);
        outWeights = getRandomArr(registers);
        
        this.registers = registers;
        
        initDelta();
    }
    public void initDelta(){
        dUnaryWeights = new float[registers][unaryFunctions.length];
        dBinaryWeights = new float[registers][binaryFunctions.length];
        dSrcWeights = new float[registers][registers];
        dOutWeights = new float[registers];
    }
    
    
    public float[] getOut(float[] in){
        this.lastIn = in.clone();
       
        float[] out = new float[registers];
        for(int output = 0; output<out.length; output++){
            out[output] = 0;
            for(int input = 0;  input <in.length; input++){
                float inputK = in[output];
                float outWeight = outWeights[output];
                for(int unary = 0; unary<unaryFunctions.length; unary++){
                    out[output] += unaryWeights[output][unary] * unaryFunctions[unary].doOperation(outWeight * inputK);
                }
                
                for(int src = 0; src<binaryWeights[output].length; src++){
                    float mySrc = in[src];
                    for(int binary = 0; binary<binaryFunctions.length; binary++){
                        
                        out[output] += binaryWeights[output][binary] * binaryFunctions[binary].doOperation(srcWeights[output][src]*mySrc, outWeight * inputK);
                    }
                }

            }
        }
        
        lastOut = out.clone();
        
        return out;
    }
    public void backPropSetup(float difference, float deltaConstant){
     //   calculateDOut(difference,deltaConstant);
     //   calculateDUnary(difference, deltaConstant);
     //   calculateDBinary(difference,deltaConstant);
     //   calculateDSrc(difference,deltaConstant);
    }
    
    public float[] getRandomArr(int length){
        float arr[] = new float[length];
        for(int i = 0; i<arr.length; i++){
            arr[i] = generator.nextFloat()/5000;
        }
        return arr;
    }
    
    public float[][] getRandomArr(int length, int length2){
        float arr[][] = new float[length][];
        for(int i = 0; i<arr.length; i++){
            arr[i] = getRandomArr(length2);
        }
        return arr;
    }

    private void calculateDOut(float difference, float[] deltaConstant) {
        float tempDOut[] = new float[dOutWeights.length];
        for(int out = 0; out<dOutWeights.length; out++){
            float outWeight = outWeights[out];
            float inputK = lastIn[out];
            
            for(int unary = 0; unary<unaryWeights[out].length; unary++){
                tempDOut[out]+= unaryWeights[out][unary]*unaryFunctions[unary].doDxOperation(outWeight * inputK)*inputK;
            }
            
             for(int src = 0; src<binaryWeights[out].length; src++){
                float mySrc = lastIn[src];
                for(int binary = 0; binary<binaryFunctions.length; binary++){
                    tempDOut[out] += binaryWeights[out][binary] * binaryFunctions[binary].doDyOperation(srcWeights[out][src]*mySrc, outWeight * inputK)*inputK;
                }
            }
           
            tempDOut[out]*=difference*deltaConstant[out];
            dOutWeights[out] += tempDOut[out];
            
        }
        
        
    }
    //public float[] getDeltaConstant(){
        
    //}
    private void calculateDBinary(float difference, float[] deltaConstant) {
        float tempDBinaryWeights[][] = new float[dBinaryWeights.length][dBinaryWeights[0].length];
        for(int out = 0; out<dOutWeights.length; out++){
            float outWeight = outWeights[out];
            float inputK = lastIn[out];
            
             for(int src = 0; src<binaryWeights[out].length; src++){
                float mySrc = lastIn[src];
                for(int binary = 0; binary<binaryFunctions.length; binary++){
                    tempDBinaryWeights[out][binary] +=  binaryFunctions[binary].doOperation(srcWeights[out][src]*mySrc, outWeight * inputK);
                }
            }
            for(int a = 0; a<tempDBinaryWeights[out].length; a++){
                dBinaryWeights[out][a] += tempDBinaryWeights[out][a]*difference*deltaConstant[out];
            }
        }
        
    }

    private void calculateDSrc(float difference, float[] deltaConstant) {
        float[][] tempDSrcWeights = new float[dSrcWeights.length][dSrcWeights[0].length];
        for(int out = 0; out<dOutWeights.length; out++){
            float outWeight = outWeights[out];
            float inputK = lastIn[out];
            
            for(int src = 0; src<binaryWeights[out].length; src++){
                float mySrc = lastIn[src];
                for(int binary = 0; binary<binaryFunctions.length; binary++){
                    tempDSrcWeights[out][src] += binaryWeights[out][binary] * binaryFunctions[binary].doDxOperation(srcWeights[out][src]*mySrc, outWeight * inputK)*mySrc;
                }
            }
            for(int a = 0; a<tempDSrcWeights[out].length; a++){
                dSrcWeights[out][a] += tempDSrcWeights[out][a]*difference*deltaConstant[out];
            }
        }
        
       
    }

    private void calculateDUnary(float difference, float[] deltaConstant) {
        float[][] tempDUnaryWeights = new float[dUnaryWeights.length][dUnaryWeights[0].length];
        for(int out = 0; out<dOutWeights.length; out++){
            float outWeight = outWeights[out];
            float inputK = lastIn[out];
            
            for(int unary = 0; unary<unaryWeights[out].length; unary++){
                tempDUnaryWeights[out][unary]+= unaryWeights[out][unary]*unaryFunctions[unary].doOperation(outWeight * inputK);
            }
            
            for(int a = 0; a<tempDUnaryWeights[out].length; a++){
                dUnaryWeights[out][a] += tempDUnaryWeights[out][a]*difference*deltaConstant[out];
            }
           
        }
    }
}
