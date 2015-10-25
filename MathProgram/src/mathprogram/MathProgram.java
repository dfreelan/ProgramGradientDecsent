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
        int lines = 10;
        int registers = 5;
        Program a = new Program(lines,registers, getUnaryFunctions(), getBinaryFunctions());
        
        float[] input = new float[registers];
        
        for(int i = 0; i<registers; i++){
            input[i] = 1.0f;
        }
        
        System.err.println("before");
        float[] output =  a.getOut(input.clone());
       
        float[] difference = new float[registers];
        
        difference[0] = output[0] - 2.0f;
        
        a.setupBackPropagate(difference);
        a.applyBackProp();
        
        System.err.println("after");
        
        output = a.getOut(input.clone());
        
        difference[0] = output[0] - 2.0f;
        
        a.setupBackPropagate(difference);
        a.applyBackProp();
        
        System.err.println("after2");
        
        output = a.getOut(input.clone());
        
        for(int i = 0; i<100000; i++){
            difference[0] = 2-output[0];
            difference[1] = 3-output[1];
            difference[2] = 4-output[2];
            difference[3] = 5-output[3];
            difference[4] = 60-output[4];
            a.setupBackPropagate(difference);
            a.applyBackProp();

            System.err.println("after3");

            output = a.getOut(input.clone());
        }
    }
    public static UnaryOperator[] getUnaryFunctions(){
        UnaryOperator[] arr = new UnaryOperator[5];
        
        arr[0] = new Cos();
        arr[1] = new Sin();
        arr[4] = new Exp();
        arr[2] = new Log();
        arr[3] = new NoOp();
        
        return arr;
    }
    
    public static BinaryOperator[] getBinaryFunctions(){
        BinaryOperator[] arr = new BinaryOperator[3];
        
        arr[0] = new Add();
        arr[1] = new Subtract();
        //arr[3] = new Divide();
        arr[2] = new Multiply();
       
        return arr;
    }
    
}
