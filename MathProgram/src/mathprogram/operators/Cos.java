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
public class Cos implements UnaryOperator {
     @Override
    public float doOperation(float x) {
        
        return  (float)Math.cos(x);
    }

    @Override
    public float doDxOperation(float x) {
        return -1.0f*(float)Math.sin(x);
    }
}
