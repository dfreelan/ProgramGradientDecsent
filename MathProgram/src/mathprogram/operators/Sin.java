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
    public float doOperation(float x) {
        
        return (float)Math.sin(x);
    }

    @Override
    public float doDxOperation(float x) {
        return (float)Math.cos(x);
    }
}
