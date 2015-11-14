/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mathprogram;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    static int lines = 15;
    static int registers = 4;

    static double[][] getTrainingData(int number) {
        double[][] data = new double[number][2];
        for (int i = 0; i < data.length; i++) {
            data[i][0] = 1 - (2 * generator.nextDouble());
            double x = data[i][0];
            data[i][1] = (double) x * x * x * x * x + x * x * x * x + x * x * x + x * x + x;
        }
        return data;
    }

    public static void oldMain() {
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
        Program.alpha = .1;
        for (int i = 0; i < 50000; i++) {
            double totalSquareError = 0.0f;
            if (i == 5000) {
                Program.alpha = Program.alpha / 10;
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
            double newErr = getSquareError(a, trainingData, input.clone());
            if (newErr >= totalSquareError || Double.isNaN(newErr)) {

                //Program.alpha = Program.alpha/1.01f;
                if (Program.alpha < .0000000001f) {

                    if (Double.isNaN(newErr)) {
                        a = temp;//revert
                        Program.alpha /= 1.01f;
                    } else {
                        Program.alpha = .0000000001f;
                    }

                } else {
                    // Program.alpha /=1.01f;
                    System.err.println("err would have been" + newErr);
                    a = temp;//revert
                }
            } else {
                // Program.alpha *=1.1;
            }

        }

        for (int i = 0; i < 1000; i++) {
            double x = (((double) i) / 500 * 10);
            input[0] = x;
            output = a.getOut(input.clone());
            System.err.println(x + "," + output[0]);
        }

        input[0] = 25;
        output = a.getOut(input.clone());
        System.err.println(output[0]);
        outToFile("reg,lines err" + registers + "," + lines + " " + getSquareError(a, trainingData, input.clone()));

           // }
        // }
    }

    public static void main(String[] args) {
        for (int theLines = 1; theLines < 120; theLines++) {
            for (int theRegisters = 1; theRegisters < 15; theRegisters++) {
                lines = 7;
                registers = 5;
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
                double prevError = Double.POSITIVE_INFINITY;
                int count = 0;
                for (int i = 0; i < 25000; i++) {
                    
                    Program save = a.clone();
                    doNumericalDescent(a, trainingData, input);
                    double newErr = getSquareError(a, trainingData,input);
                    if(prevError <= newErr || Double.isNaN(newErr)){
                        a = save;
                        count++;
                        //  Program.alpha/=10;
                    }else{
                        count = 0;
                        prevError = newErr;
                    }
                    if(count>10){
                        Program.alpha /= 2;
                        if(Program.alpha == 0.0){
                            break;
                        }
                    }
                    System.err.println(" " + i + " errSmarter " + prevError + " " + Program.alpha);
                }

                for (int i = 0; i < 2500; i++) {
                    double x = (((double) i) / 500 * 10);
                    x-=2;
                    input[0] = x;
                    output = a.getOut(input.clone());
                    System.err.println(x + "," + output[0]);
                }
                outToFile("reg,lines,err " + registers + " " + lines +  " " + prevError);
            }
        }

    }

    //Taken from stack overflow, because i'm lazy
    //http://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java

    static void outToFile(String stuff) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/home/dfreelan/Desktop/numerical.5.txt", true)))) {
            out.println(stuff);

        } catch (IOException e) {
            e.printStackTrace();;
            //exception handling left as an exercise for the reader
        }
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

    public static void doNumericalDescent(Program p, double trainingData[][], double[] input) {
        double epsilon = 0.000001;
        for (Line line : p.lines) {
            double[][][] allWeights = line.getAllWeights();
            double[][][] allDeltaWeights = line.getAllDeltaWeights();
            for (int type = 0; type < allWeights.length; type++) {
                for (int q = 0; q < allWeights[type].length; q++) {
                    for (int w = 0; w < allWeights[type][q].length; w++) {
                        double prevValue = allWeights[type][q][w];

                        allWeights[type][q][w] += epsilon;
                        double errA = getSquareError(p, trainingData, input);
                        allWeights[type][q][w] = prevValue - epsilon;
                        double errB = getSquareError(p, trainingData, input);
                        allWeights[type][q][w] = prevValue;

                        allDeltaWeights[type][q][w] = (errB - errA) / (2 * epsilon);

                    }
                }
            }
        }
        p.applyBackProp();
    }

    public static UnaryOperator[] getUnaryFunctions() {
        UnaryOperator[] arr = new UnaryOperator[5];

        arr[3] = new Cos();
        arr[1] = new Sin();
        arr[4] = new Exp();
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
