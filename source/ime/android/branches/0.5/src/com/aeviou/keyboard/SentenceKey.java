package com.aeviou.keyboard;

import com.aeviou.Environment;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap; 
import java.util.ArrayList;

public class SentenceKey {
	public static final int ERROR_SENTENCEINDEX =0xFF ;
	public static int SentenceIndex=0 ;
	public static final float FONTSIZE_SENTENCE =Environment.FONTSIZE_SENTENCE_TEMP ;
	public static final int GAP=0 ;
	public char keyValue ;
	public int index ;
	public SentenceKey(int index){
		this.index=index ;
	}
	public void refresh(Canvas canvas ,float height,Paint paint,int index,Bitmap bitmap){
		float oldSize = paint.getTextSize();
		paint.setTextSize(SentenceKey.FONTSIZE_SENTENCE);
		canvas.drawText(String.format("%c ", this.keyValue), this.index*FONTSIZE_SENTENCE, height*8/9, paint) ;
		paint.setTextSize(oldSize);
	
	}
	public static void formateSentenceKey(ArrayList<SentenceKey> sentenceKey,String sentence){
		String temp=sentence.trim() ;
		int len=sentence.length() ;
		char sentenceArray[]=new char[len] ;
		temp.getChars(0, len, sentenceArray, 0) ;
		if(!sentenceKey.isEmpty())
			sentenceKey.clear() ;
		for(int i=0;i<len;i++){
			SentenceKey keyTemp=new SentenceKey(i) ;
			keyTemp.index=i ;
			keyTemp.keyValue=sentenceArray[i] ;
			sentenceKey.add(keyTemp) ;
		}
	}
	public static int getSentenceIndex(float X,float Y,String sentence,float height){
		int temp=0 ;
		if((Y>height/3)&&(Y<height*4/5)){
			temp=(int)(X/((FONTSIZE_SENTENCE+GAP))) ;
		}
		Paint paint=new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(SentenceKey.FONTSIZE_SENTENCE);
		if(X>paint.measureText(sentence)){
			return ERROR_SENTENCEINDEX ;
		}
		paint.setTextSize(AeviouKeyboardView.FONTSIZE	);
		return temp ;
	}
}
