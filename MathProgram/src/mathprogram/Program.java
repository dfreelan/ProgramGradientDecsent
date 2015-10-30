/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mathprogram;

import mathprogram.operators.BinaryOperator;
import mathprogram.operators.UnaryOperator;

/**
 *
 * @author dfreelan
 */
class Program {
    int numLines;
    int registers;
    UnaryOperator[] unaryFunctions;
    BinaryOperator[] binaryFunctions;
    
    Line[] lines;
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
    public void applyBackProp(){
        
        for(int i = 0; i<numLines; i++){
             lines[i].applyBackprop();
         }
    }
    void setupBackPropagate(float[] difference){
        float[] deltaArr = new float[difference.length];
        for(int i = 0; i<deltaArr.length; i++){
            deltaArr[i] = 1.0f;
        }
        for(int i = lines.length-1; i>-1; i--){
            deltaArr = lines[i].backPropSetup(difference, deltaArr);
        }
    }
    float[] getOut(float[] input) {
        FloatMath.printFloatArr(input);
        for(int i = 0; i<numLines; i++){
            input = lines[i].getOut(input);
            FloatMath.printFloatArr(input);
        }
        
        return input;
    }
    
}
