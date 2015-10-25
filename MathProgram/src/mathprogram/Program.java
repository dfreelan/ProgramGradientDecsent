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

    float[] getOut(float[] input) {
        FloatMath.printFloatArr(input);
        for(int i = 0; i<numLines; i++){
            input = lines[i].getOut(input);
            FloatMath.printFloatArr(input);
        }
        
        return input;
    }
    
}