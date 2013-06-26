package com.aeviou.keyboard;

import com.aeviou.R ;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Bitmap; 
import android.view.View;
import android.graphics.Rect;


public class TipView extends View {
	public static final int GAP=10 ;
	private Bitmap bitmap ;
	public TipView(Context context) {
		super(context);
		this.setBackgroundResource(R.drawable.frame) ;
    
	}
	public void setBitmap(Bitmap bitmap){
		this.bitmap=bitmap ;
		this.invalidate() ;
	}
	@Override 
	public void onDraw(Canvas canvas){
		if(bitmap!=null){
			canvas.drawBitmap(bitmap, null,new Rect(GAP,GAP,this.getWidth()-GAP,this.getHeight()-GAP), null) ;
		}
	}
	public static  Rect getNeighourRect(int X ,int Y,AeviouKeyboardView view){
		float keyHeight=view.keyHeight ;
		Rect rect=new Rect() ;
		rect.top=(int )(Y-1.5*keyHeight);
		rect.left=(int)(X-1.5*keyHeight) ;

		if(rect.top<0)
			rect.top=0 ;
		if(rect.left<0)
			rect.left=0 ;
		rect.right=(int)(3*keyHeight)	 ;
		rect.bottom=(int)(3*keyHeight) ;
		if(rect.left+rect.right>view.getWidth())
			rect.left=view.getWidth()-rect.right ;
		if(rect.top+rect.bottom>view.getHeight())
			rect.top=view.getHeight()-rect.bottom ;
		return rect ;
	}
}
