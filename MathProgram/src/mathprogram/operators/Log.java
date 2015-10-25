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
public class Log implements UnaryOperator {
     @Override
    public float doOperation(float x) {
        return (float)Math.log(x*x)/2;
    }

    @Override
    public float doDxOperation(float x) {
        return 1.0f/x;
    }
}
