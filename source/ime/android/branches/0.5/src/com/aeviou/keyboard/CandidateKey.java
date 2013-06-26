package com.aeviou.keyboard;

import com.aeviou.pinyin.PinyinContext;
import com.aeviou.Environment;
import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Bitmap;

public class CandidateKey{
	public static final float FONTSIZE=40 ;
	public static final int ERROR_CANDIDATEINDEX=0xFF ;
	public static boolean isEnd ;
	public static int CandidateIndex=0 ;
	public static float FONTSIZE_CANDIDATE=Environment.FONTSIZE_CANDIDATE_TEMP ;
	public String keyValue ;
	public boolean isVisible ;
	public float startPos ;
	public float stopPos ;
	public int index ;
	public boolean isSelected ;

	public CandidateKey(int index){
		this.index=index ;
		this.isSelected=false ;
		this.isVisible=false ;
		this.keyValue="" ;
	}
	public void Refresh(Canvas canvas ,float height,Paint paint,int index,Bitmap bitmap){
		if(this.isVisible){
			if(this.index==index){
				RectF rect=new RectF(this.startPos,height/7,this.stopPos,height*3/4) ;
				canvas.drawBitmap(bitmap,null, rect, paint) ;
				this.isSelected=false ;
			}
			paint.setTextSize(FONTSIZE_CANDIDATE) ;
			canvas.drawText(keyValue, this.startPos, height*3/5, paint) ;
			paint.setTextSize(AeviouKeyboardView.FONTSIZE) ;
		}
	}
	
	public static int formatCandidateKey(ArrayList<CandidateKey> candidateKey,PinyinContext text,int index,int width){
		int temp=0 ;
		float len=0 ;
		int i=0 ;
		String str="" ;
		CandidateKey.isEnd=false ;
		final Paint paint=new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		if(!candidateKey.isEmpty())
			candidateKey.clear() ;
		paint.setTextSize(FONTSIZE_CANDIDATE) ;
		while(len<width){
			CandidateKey keyTemp=new CandidateKey(i) ;
			str=text.getCandidate(i+index) ;
			if(str==null){
				CandidateKey.isEnd=true ;
				break ;
			}
			len=paint.measureText(str) ;
			if(i==0){
				keyTemp.startPos=0 ;
				keyTemp.stopPos=len+5 ;
			}else{
				keyTemp.startPos=candidateKey.get(candidateKey.size()-1).stopPos;
				keyTemp.stopPos=keyTemp.startPos+len+5 ;
				len=keyTemp.stopPos ;
			}
			if(len>width)
				break ;
			keyTemp.keyValue=str ;
			keyTemp.isVisible=true ;
			keyTemp.isSelected=false ;
			candidateKey.add(keyTemp) ;
			i++ ;
			temp=i+index ;
		}
		paint.setTextSize(AeviouKeyboardView.FONTSIZE) ;
		return temp;
	}
	public static int getCandidateIndex(ArrayList<CandidateKey>candidateKey,float X,float Y,float height)
	{
		int temp=ERROR_CANDIDATEINDEX ;
		if(candidateKey.isEmpty())
			return temp ;
		if((Y<height)&&(Y>0)){
			for(int i=0;i<candidateKey.size();i++){
				if(X>candidateKey.get(i).startPos&&(X<candidateKey.get(i).stopPos)){
					temp=i ;
					break ;
				}
			}
		}
		return temp ;
	}
	
	
	public static void nextLineCandidate(ArrayList<Integer> canLineIndex, ArrayList<CandidateKey> candidateKey,PinyinContext mPinyinContext,int width){
		int index;
		int size =canLineIndex.size() ;
		if(canLineIndex.isEmpty())
			index=0;
		else
			index=canLineIndex.get(size-1) ;
		if(CandidateKey.isEnd)
			return ;
		index=CandidateKey.formatCandidateKey(candidateKey, mPinyinContext, index,width) ;
		canLineIndex.add(index) ;				
	}
	public static void returnLineCandidate(ArrayList<Integer> canLineIndex, ArrayList<CandidateKey> candidateKey,PinyinContext mPinyinContext,int width){
		int size=canLineIndex.size() ;
		int index=0 ;
		if(size>2){
			canLineIndex.remove(size-1);
			index=canLineIndex.get(size-3) ;
			CandidateKey.formatCandidateKey(candidateKey, mPinyinContext, index,width) ;	
		}else{
			index=0 ;
			CandidateKey.formatCandidateKey(candidateKey, mPinyinContext, index,width) ;
		}
	}
}
