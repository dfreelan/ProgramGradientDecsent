/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mathprogram;

import mathprogram.operators.*;
import mathprogram.operators.UnaryOperator;

/**
 *
 * @author dfreelan
 */
public class MathProgram {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int lines = 200;
        int registers = 100;
        Program a = new Program(lines,registers, getUnaryFunctions(), getBinaryFunctions());
        
        float[] input = new float[registers];
        
        for(int i = 0; i<registers; i++){
            input[i] = 1.0f;
        }
        
        float[] output =  a.getOut(input);
        System.out.println();
        output =  a.getOut(output);
        
        FloatMath.printFloatArr(output);
    }
    public static UnaryOperator[] getUnaryFunctions(){
        UnaryOperator[] arr = new UnaryOperator[4];
        
        arr[0] = new Cos();
        arr[1] = new Sin();
        //arr[2] = new Exp();
        arr[2] = new Log();
        arr[3] = new NoOp();
        
        return arr;
    }
    
    public static BinaryOperator[] getBinaryFunctions(){
        BinaryOperator[] arr = new BinaryOperator[4];
        
        arr[0] = new Add();
        arr[1] = new Subtract();
        arr[2] = new Divide();
        arr[3] = new Multiply();
       
        return arr;
    }
    
}
