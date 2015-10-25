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
       if(y==0) return 1;
        return x/y;
    }

    @Override
    public float doDxOperation(float x, float y) {
       if(y==0) return 1;
        return 1/y;
    }

    @Override
    public float doDyOperation(float x, float y) {
      if(y==0) return 1;
        return -1.0f*(x/(y*y));
    }
    
}
