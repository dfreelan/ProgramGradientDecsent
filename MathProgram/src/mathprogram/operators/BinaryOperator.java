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
public interface BinaryOperator {
   public float doOperation(float x, float y);
   public float doDxOperation(float x, float y);
   public float doDyOperation(float x, float y);
}
