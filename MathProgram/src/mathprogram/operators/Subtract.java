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
public class Subtract implements BinaryOperator {

    @Override
    public float doOperation(float x, float y) {
        return x - y;
    }

    @Override
    public float doDxOperation(float x, float y) {
        return 1.0f;
    }

    @Override
    public float doDyOperation(float x, float y) {
        return -1.0f;
    }
    
}
