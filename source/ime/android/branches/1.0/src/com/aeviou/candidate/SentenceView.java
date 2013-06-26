package com.aeviou.candidate;

import com.aeviou.utils.AeviouConstants;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.PopupWindow;

public class SentenceView extends View {
	public static final int GAP = 10;
	
	private PopupWindow mSentenceWindow;
	private SentenceView sentenceView;
	private Handler handler;

	private Paint paint;
	private PaintFlagsDrawFilter drawFilter;
	private String sentence;
	private int width, height;

	public SentenceView(Context context) {
		super(context);
		drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(1);
		setBackgroundColor(Color.argb(200, 255, 255, 255));
		mSentenceWindow = null;
		sentenceView = this;
		handler = new Handler(){
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case AeviouConstants.MSG_UPDATE_SENTENCE:
					sentenceView.update();
					break;
				}
				super.handleMessage(msg);
			}
		};
	}
	
	public Handler getHandler(){
		return handler;
	}
	
	public void onDraw(Canvas canvas) {
		if (mSentenceWindow == null) return;
		canvas.setDrawFilter(drawFilter);
		if (sentence != null){
			canvas.drawText(sentence, (int)(20 * AeviouConstants.IME_VIEW_HSCALE), 
							height - (int)(14 * AeviouConstants.IME_VIEW_HSCALE), paint);
		}
		canvas.drawLine(0, 0, width - 1, 0, paint);
		canvas.drawLine(width - 1, 1, width - 1, height, paint);
	}

	public void setSentence(String sentence){
		this.sentence = sentence;
		if (sentence == null || sentence.equals(" ")){
			width = 0;
		}else{
			paint.setTextSize(AeviouConstants.NORMAL_CANDIDATE_SENTENCE * AeviouConstants.IME_VIEW_VSCALE);
			width = (int)paint.measureText(sentence) + (int)(50 * AeviouConstants.IME_VIEW_HSCALE);
		}
	}
	
	public void commitUpdate(){
		Message msg= new Message();
		msg.what = AeviouConstants.MSG_UPDATE_SENTENCE;
		handler.sendMessage(msg);
	}
	
	private void update(){
		if (mSentenceWindow == null){
			createWindow();
		}
		if (width == 0){
			this.setVisibility(INVISIBLE);
		}else{
			this.setVisibility(VISIBLE);
		}
		
		height = (int)(60 * AeviouConstants.IME_VIEW_VSCALE);
		if (AeviouConstants.keyboardView != null) {
			mSentenceWindow.showAsDropDown(AeviouConstants.keyboardView);
			mSentenceWindow.update(0, -height, width, height);
			this.invalidate();
		}
	}
	
	public void closeWindow(){
		if (mSentenceWindow == null) return;
		this.setVisibility(INVISIBLE);
		mSentenceWindow.dismiss();
		mSentenceWindow = null;
	}
	
	private void createWindow(){
		if (mSentenceWindow != null){
			closeWindow();
		}
		mSentenceWindow = new PopupWindow(this);
		mSentenceWindow.setTouchable(false);
		mSentenceWindow.setContentView(this);
	}
	
	public void reset(){
		createWindow();
	}
}


