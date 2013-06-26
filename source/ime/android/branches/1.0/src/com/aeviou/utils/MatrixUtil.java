package com.aeviou.utils;

import android.graphics.Matrix;

public class MatrixUtil {
	static float[] b_arr=new float[9];
	static float[] r_arr=new float[9];
	
	public static Matrix mul(float a[],Matrix b){
		
		b.getValues(b_arr);
		for(int i=0; i < 3; i++)
            for(int j=0; j < 3; j++){
            	r_arr[i*3+j]=0;
                for(int c=0; c < 3; c++)
                    r_arr[i*3+j] += a[i*3+c] * b_arr[c*3+j];
            }
		Matrix r=new Matrix();
		r.setValues(r_arr);
		return r;
	}
	
	public static void mul(float a[],Matrix b,Matrix result){
		b.getValues(b_arr);
		for(int i=0; i < 3; i++)
            for(int j=0; j < 3; j++){
            	r_arr[i*3+j]=0;
                for(int c=0; c < 3; c++)
                    r_arr[i*3+j] += a[i*3+c] * b_arr[c*3+j];
            }
		result.setValues(r_arr);
	}
}
