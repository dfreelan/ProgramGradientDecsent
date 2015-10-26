/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mathprogram;

import java.util.Random;
import mathprogram.operators.*;
import mathprogram.operators.UnaryOperator;

/**
 *
 * @author dfreelan
 */
public class MathProgram {

    static Random generator = new Random();

    /**
     * @param args the command line arguments
     */
    static int lines = 10;
    static int registers = 7;
    static float[][] getTrainingData(int number) {
        float[][] data = new float[number][2];
        for (int i = 0; i < data.length; i++) {
            data[i][0] = (4 * generator.nextFloat());
            data[i][1] = (float)Math.sqrt(data[i][0]);//(float) (float) Math.sin(Math.sin(data[i][0] * data[i][0]));
        }
        return data;
    }

    public static void main(String[] args) {
        
        Program a = new Program(lines, registers, getUnaryFunctions(), getBinaryFunctions());

        float[] input = new float[registers];

        for (int i = 0; i < registers; i++) {
            input[i] = 1.0f;
        }
        a.setToDefaultProgram();
        System.err.println("before");
        float[] output = a.getOut(input.clone());

        float[] difference = new float[registers];

        float[][] trainingData = getTrainingData(20);
        output = a.getOut(input.clone());

        for (int i = 0; i < 25000; i++) {
            float totalSquareError = 0.0f;
            if(i%2000==1999){
                //Program.alpha = Program.alpha/1.5f;
            }
            for (int k = 0; k < trainingData.length; k++) {
                float actualInput = trainingData[k][0];

                input[0] = actualInput;
                output = a.getOut(input.clone());
                //System.err.println();
                difference[0] = (trainingData[k][1] - output[0]);
                
                totalSquareError += difference[0] * difference[0];
                a.setupBackPropagate(difference);
                

            }
            System.err.println(i + " total square err n: " + totalSquareError);
            Program temp = a.clone();
            a.applyBackProp();
            System.err.println(Program.alpha);
            float newErr = getSquareError(a,trainingData,input.clone());
            if(newErr>totalSquareError || Float.isNaN(newErr)){
                
                Program.alpha = Program.alpha/1.1f;
                if(Program.alpha < .00001f){
                    Program.alpha = .00001f;
                    
                }else if(!Float.isNaN(newErr)){
                    a = temp;
                }
            }else {
               Program.alpha = .001f;
            }
                    
          
        }

        for (int i = 0; i < 1000; i++) {
            float x = (((float) i) / 1000 * 25);
            input[0] = x;
            output = a.getOut(input.clone());
            System.err.println(x + "," + output[0]);
        }

        input[0] = 25;
        output = a.getOut(input.clone());
        System.err.println(output[0]);
    }

    public static float getSquareError(Program p, float[][] trainingData, float[] input) {
        float[] output;
        float[] difference = new float[registers];
        float totalSquareError = 0.0f;
        for (int k = 0; k < trainingData.length; k++) {
                float actualInput = trainingData[k][0];

                input[0] = actualInput;
                output = p.getOut(input.clone());
                //System.err.println();
                difference[0] = (trainingData[k][1] - output[0]);
                totalSquareError += difference[0] * difference[0];
        }
        
        return totalSquareError;

    }

    public static UnaryOperator[] getUnaryFunctions() {
        UnaryOperator[] arr = new UnaryOperator[5];

        arr[3] = new Cos();
        arr[1] = new Sin();
        arr[2] = new Exp();
        arr[4] = new Log();
        arr[0] = new NoOp();

        return arr;
    }

    public static BinaryOperator[] getBinaryFunctions() {
        BinaryOperator[] arr = new BinaryOperator[4];

        arr[0] = new Add();
        arr[1] = new Subtract();
        arr[3] = new Divide();
        arr[2] = new Multiply();

        return arr;
    }

}
