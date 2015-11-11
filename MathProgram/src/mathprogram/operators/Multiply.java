/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mathprogram.operators;

/**
 *
 * @author dfreelan
 */
public class Multiply implements BinaryOperator{

    @Override
    public double doOperation(double x, double y) {
        return x*y;
    }

    @Override
    public double doDxOperation(double x, double y) {
        return y;
    }

    @Override
    public double doDyOperation(double x, double y) {
       return x;
    }
    
    
}
