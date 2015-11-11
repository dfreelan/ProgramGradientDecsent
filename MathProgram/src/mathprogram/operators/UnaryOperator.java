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
public interface UnaryOperator {
   public double doOperation(double x);
   public double doDxOperation(double x);
}
