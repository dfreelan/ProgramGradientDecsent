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
    static int registers = 4;
    static double[][] getTrainingData(int number) {
        double[][] data = new double[number][2];
        for (int i = 0; i < data.length; i++) {
            data[i][0] = 1-(2*generator.nextDouble());
            double x = data[i][0];
            data[i][1] = (double) x*x*x*x*x + x*x*x*x + x*x*x + x*x + x;
        }
        return data;
    }

    public static void main(String[] args) {
        
        Program a = new Program(lines, registers, getUnaryFunctions(), getBinaryFunctions());

        double[] input = new double[registers];

        for (int i = 0; i < registers; i++) {
            input[i] = 1.0f;
        }
        a.setToDefaultProgram();
        System.err.println("before");
        double[] output = a.getOut(input.clone());

        double[] difference = new double[registers];

        double[][] trainingData = getTrainingData(20);
        output = a.getOut(input.clone());
        Program.alpha = 1;
        for (int i = 0; i < 50000; i++) {
            double totalSquareError = 0.0f;
            if(i%5000==4999){
                Program.alpha = Program.alpha/10f;
            }
            for (int k = 0; k < trainingData.length; k++) {
                double actualInput = trainingData[k][0];

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
            double newErr = getSquareError(a,trainingData,input.clone());
            if(newErr>=totalSquareError || Double.isNaN(newErr)){
                
                //Program.alpha = Program.alpha/1.1f;
                if(Program.alpha < .0000000001f){
                    
                    if(Double.isNaN(newErr)){
                        a = temp;//revert
                        Program.alpha /=1.01f;
                    }else{
                        Program.alpha = .0000000001f;
                    }
                         
                }else{
                   // Program.alpha /=1.01f;
                    System.err.println("err would have been"  + newErr);
                    a = temp;//revert
                }
            }else {
              // Program.alpha = .01;
            }
                    
          
        }

        for (int i = 0; i < 1000; i++) {
            double x = (((double) i) / 500 *10);
            input[0] = x;
            output = a.getOut(input.clone());
            System.err.println(x + "," + output[0]);
        }

        input[0] = 25;
        output = a.getOut(input.clone());
        System.err.println(output[0]);
    }

    public static double getSquareError(Program p, double[][] trainingData, double[] input) {
        double[] output;
        double[] difference = new double[registers];
        double totalSquareError = 0.0f;
         for (int k = 0; k < trainingData.length; k++) {
                double actualInput = trainingData[k][0];

                input[0] = actualInput;
                output = p.getOut(input.clone());
                //System.err.println();
                difference[0] = (trainingData[k][1] - output[0]);
                
                totalSquareError += difference[0] * difference[0];
                
                

            }
        
        return totalSquareError;

    }

    public static UnaryOperator[] getUnaryFunctions() {
        UnaryOperator[] arr = new UnaryOperator[4];

        arr[3] = new Cos();
        arr[1] = new Sin();
       // arr[2] = new Exp();
        arr[2] = new Log();
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
