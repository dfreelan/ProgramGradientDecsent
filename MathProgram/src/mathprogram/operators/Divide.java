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
    public float doOperation(float x, float y) {

       if(x==0) return 1;
        return y/x;

    }

    @Override
    public float doDxOperation(float x, float y) {

       if(x==0.0f) return 0;
        return 1/x;

    }

    @Override
    public float doDyOperation(float x, float y) {

      if(x==0.0f) return 0;
        return -1.0f*(y/(x*x));
    }
    
}
