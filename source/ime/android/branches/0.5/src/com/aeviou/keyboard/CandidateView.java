package com.aeviou.keyboard;

import com.aeviou.R ;
import com.aeviou.AeviouIME;
import com.aeviou.pinyin.PinyinContext;
import com.aeviou.Environment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color; 
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View ;
import android.view.WindowManager;
import android.util.DisplayMetrics;

import java.util.ArrayList; 

public class CandidateView extends View {

	private static final float FONTSIZE_PINYINSEQ=Environment.FONTSIZE_PINYINSEQ_TEMP ;
	private static final int GAP=Environment.GAP_CANDIDATEVIEW_TEMP ;
	private String mPinyinSeq ;
	private AeviouIME service ;
	private ArrayList<SentenceKey> mSentenceKey ;
	Bitmap bmp ;
	Paint mPaint ;
	private int desiredHeight ;
	public CandidateView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setBackgroundColor(Color.rgb(40, 40, 40)	) ;
		mSentenceKey=new ArrayList<SentenceKey>() ;
		Resources res=getResources() ;
    	BitmapDrawable bmpDraw=(BitmapDrawable)res.getDrawable(R.drawable.bg_candidate) ;
    	bmp=bmpDraw.getBitmap() ;
    	mPaint=new Paint();
    	mPaint.setColor(Color.WHITE);
    	mPaint.setTextSize(FONTSIZE_PINYINSEQ) ;
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG) ;
	}
	public void setService(AeviouIME service){
		this.service=service ;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		DisplayMetrics dm=new DisplayMetrics() ;
    	WindowManager wm=(WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE) ;
    	wm.getDefaultDisplay().getMetrics(dm) ;	
		desiredHeight = GAP+(int)FONTSIZE_PINYINSEQ+(int)SentenceKey.FONTSIZE_SENTENCE ;
		setMeasuredDimension(dm.widthPixels,desiredHeight);
	}
	public void updateSentence(){
		PinyinContext mPinyinContext=this.service.getPinyinContext() ;
		mPinyinSeq=mPinyinContext.getPinyin() ;
		String sentence=mPinyinContext.getSentence() ;
		SentenceKey.formateSentenceKey(mSentenceKey, sentence) ;
		SentenceKey.SentenceIndex=mPinyinContext.getCorrectPosition() ;
		this.invalidate() ;
	}
	public void clearSentence(){
		if(!mSentenceKey.isEmpty())
			mSentenceKey.clear() ;
		this.mPinyinSeq=""	 ;
	}
    @Override 
    public void onDraw(Canvas canvas)
    {	
    	if(mPinyinSeq != null){
    		canvas.drawText(mPinyinSeq, 0, FONTSIZE_PINYINSEQ + GAP / 4, mPaint) ;
    	}
    	for(int i = 0; i < mSentenceKey.size(); i++){
    		mSentenceKey.get(i).refresh(canvas, desiredHeight, mPaint, SentenceKey.SentenceIndex, bmp) ;
    	}
    }
    
	public boolean onTouchEvent(MotionEvent event) {
		float X=event.getX() ;
		float Y=event.getY() ;
		int eventaction = event.getAction();
		if(eventaction==MotionEvent.ACTION_UP){
			PinyinContext mPinyinContext=this.service.getPinyinContext() ;
			String sentence=mPinyinContext.getSentence() ;
			int index=SentenceKey.getSentenceIndex(X, Y, sentence, desiredHeight) ;
			if(index==SentenceKey.ERROR_SENTENCEINDEX){
				return true;
			}
			service.correctSentence(index) ;
			this.invalidate() ;
		}
		return true ;
	}
}
