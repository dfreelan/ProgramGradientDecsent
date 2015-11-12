/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mathprogram;

/**
 *
 * @author dfreelan
 */
public class DoubleMath {
    public static double[] normalize(double[] arr, double sum){
        double total = 0;
        for(int i = 0; i<arr.length; i++){
            total+=arr[i];
        }
        for(int i = 0; i<arr.length; i++){
            arr[i] /= (total/sum);
        }
        return arr;
    }
    public static double[] normalize(double[] arr){
        return normalize(arr, 1.0f);
    }
    public static void printdoubleArr(double[] arr){
        for(int i = 0; i<arr.length-1; i++){
            System.err.print(arr[i] + ",");
        }
        System.err.println(arr[arr.length-1]);
    }
    public static void printdoubleArr(double[][] arr){
        for(int i = 0; i<arr.length; i++){
            printdoubleArr(arr[i]);
        }
        System.err.println();
    }
    public static void divAllBy(double[][] arr, double value){
        for(int i = 0; i<arr.length; i++){
            divAllBy(arr[i],value);
        }
        
    }
    
    public static void divAllBy(double[] arr, double value){
        for(int i = 0; i<arr.length; i++){
            arr[i] = arr[i]/value;
        }
        
    }
}
