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
public class FloatMath {
    public static float[] normalize(float[] arr, float sum){
        float total = 0;
        for(int i = 0; i<arr.length; i++){
            total+=arr[i];
        }
        for(int i = 0; i<arr.length; i++){
            arr[i] /= (total/sum);
        }
        return arr;
    }
    public static float[] normalize(float[] arr){
        return normalize(arr, 1.0f);
    }
    public static void printFloatArr(float[] arr){
        for(int i = 0; i<arr.length-1; i++){
            System.err.print(arr[i] + ",");
        }
        System.err.println(arr[arr.length-1]);
    }
    public static void printFloatArr(float[][] arr){
        for(int i = 0; i<arr.length; i++){
            printFloatArr(arr[i]);
        }
        System.err.println();
    }
}
