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
public class Add implements BinaryOperator {

    @Override
    public double doOperation(double x, double y) {
        return y+x;
    }

    @Override
    public double doDxOperation(double x, double y) {
        return 1;
    }

    @Override
    public double doDyOperation(double x, double y) {
        return 1;
    }
    
    
    
}
