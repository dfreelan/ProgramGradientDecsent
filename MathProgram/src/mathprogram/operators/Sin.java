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
public class Sin implements UnaryOperator {
    @Override
    public double doOperation(double x) {
        
        return (double)Math.sin(x);
    }

    @Override
    public double doDxOperation(double x) {
        return (double)Math.cos(x);
    }
}
