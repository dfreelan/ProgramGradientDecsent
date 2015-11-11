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
public class Divide implements BinaryOperator {

    @Override
    public double doOperation(double x, double y) {

       if(x==0) return 1;
        return y/x;

    }

    @Override
    public double doDxOperation(double x, double y) {

       if(x==0.0f) return 0;
        return 1/x;

    }

    @Override
    public double doDyOperation(double x, double y) {

      if(x==0.0f) return 0;
        return -1.0f*(y/(x*x));
    }
    
}
