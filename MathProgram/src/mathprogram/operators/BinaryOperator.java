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
   public double doOperation(double x, double y);
   public double doDxOperation(double x, double y);
   public double doDyOperation(double x, double y);
}
