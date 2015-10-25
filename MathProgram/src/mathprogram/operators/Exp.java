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
public class Exp implements UnaryOperator {
    @Override
    public float doOperation(float x) {
        return (float)Math.exp(x);
    }

    @Override
    public float doDxOperation(float x) {
        return (float)Math.exp(x);
    }
    
}
