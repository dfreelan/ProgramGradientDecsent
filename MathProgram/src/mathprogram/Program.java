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
class Program{
    int numLines;
    int registers;
    UnaryOperator[] unaryFunctions;
    BinaryOperator[] binaryFunctions;
    static double alpha=.0001f;
    Line[] lines;
    double dropProbability = .005;
    Random generator = new Random();
    public Program clone(){
        Program newProg = new Program(numLines, registers, unaryFunctions, binaryFunctions);
        for(int i = 0; i<newProg.lines.length; i++){
            newProg.lines[i] = lines[i].clone();
        }
        
        return newProg;
    }
    public Program(int numLines, int registers, UnaryOperator[] unaryFunctions, BinaryOperator[] binaryFunctions) {
      this.numLines = numLines;
      this.registers = registers;
      this.unaryFunctions = unaryFunctions;
      this.binaryFunctions = binaryFunctions;
    
      lines = new Line[numLines];
      for(int i = 0; i<numLines; i++){
          lines[i] = new Line(unaryFunctions, binaryFunctions, registers);
      }
    }
    public void setToDefaultProgram(){
        for(Line line : lines){
            line.setToDefault();
        }
    }
    public void applyBackProp(){

        double max = 0.0f;
        //dropProbability = generator.nextDouble()*generator.nextDouble()*generator.nextDouble()*generator.nextDouble()*generator.nextDouble()*generator.nextDouble();

        for(int i = 0; i<numLines; i++){
            //lines[i].addInComplexity();
            lines[i].applyMask(dropProbability);
            double value = lines[i].getMaxDError();
            if(max<value){
                max = value;
            }
        }
        for(int i = 0; i<numLines; i++){
            //System.err.println("line number " + i);
             lines[i].applyBackprop(max);
         }
    }
    void setupBackPropagate(double[] difference){
        double packedInfo[][] = new double[3][difference.length];
        packedInfo[0][0] = 1.0d;
        
        //double[] deltaArr = new double[difference.length];
        //for(int i = 0; i<deltaArr.length; i++){
         //   deltaArr[0] = 1.0f;
        //    deltaArr[1] = 1.0f;
        //}
        
        for(int i = lines.length-1; i>-1; i--){
            packedInfo = lines[i].backPropSetup(difference.clone(),packedInfo);
        }
    }
    double[] getOut(double[] input) {
        input = input.clone();
        //System.err.println("question:");
        //DoubleMath.printdoubleArr(input);
        //System.err.println("answer:");
        for(int i = 0; i<numLines; i++){
            input = lines[i].getOut(input);
            //DoubleMath.printdoubleArr(input);
        }
        
        return input;
    }
    
}
