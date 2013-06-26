package com.aeviou.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.PopupWindow;

import com.aeviou.key.AbstractKey.KEY_STATUS;
import com.aeviou.key.HexKey;
import com.aeviou.utils.AeviouConstants;

public class KeyBoardTip extends View {
	private PopupWindow mTipWindow;
	private Handler handler;
	private boolean isShow=false;

	private Paint paint;
	private PaintFlagsDrawFilter drawFilter;

	private int x,y;
	private int width,height;
	
	private HexKey leftKey=new HexKey(),centerKey=new HexKey(),rightKey=new HexKey();
	

	public KeyBoardTip(Context context) {
		super(context);
		drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(1);
		paint.setTextSize(AeviouConstants.NORMAL_LETTER_HEIGHT);
		setBackgroundColor(Color.GRAY);
		
		mTipWindow = null;
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case AeviouConstants.MSG_UPDATE_KEYBOARDTIP:
					KeyBoardTip.this.update();
					break;
				}
				super.handleMessage(msg);
			}

		};
		isShow=false;
	}

	public void setChar(HexKey key,HexEnglishKeyboard keyboard, AeviouConstants.Direction direction) {
		if(key!=null){
			leftKey.name=key.leftChar;
			centerKey.name=key.name;
			rightKey.name=key.rightChar;
	
			int keyScreenHeight=(int) (key.height*AeviouConstants.IME_VIEW_HSCALE);
			
			width=AeviouConstants.IME_VIEW_WIDTH;
			height=keyScreenHeight;
			
			this.x=0;
			//start(0,0) is the bottom left of the father view
			this.y=-AeviouConstants.IME_VIEW_HEIGHT-this.height;
			
			leftKey.setPos(0, 0);
//			centerKey.setPos((int) (key.x*AeviouConstants.IME_VIEW_VSCALE), 0);
//			rightKey.setPos(centerKey.x+keyScreenWidth,0);
			centerKey.setPos((int) (0.33f*AeviouConstants.IME_VIEW_WIDTH), 0);
			rightKey.setPos((int) (0.66f*AeviouConstants.IME_VIEW_WIDTH), 0);
			
			leftKey.setSize(centerKey.x, height);
			centerKey.setSize(rightKey.x-centerKey.x,height);
			rightKey.setSize(width-rightKey.x, height);
			
			leftKey.calCenter();
			centerKey.calCenter();
			rightKey.calCenter();
			
			leftKey.status=centerKey.status=rightKey.status=KEY_STATUS.next;
			switch(direction){
			case left:
				leftKey.status=KEY_STATUS.selected;
				break;
			case center:
				centerKey.status=KEY_STATUS.selected;
				break;
			case right:
				rightKey.status=KEY_STATUS.selected;
				break;
			}
			isShow=true;
		}else{
			isShow=false;
		}
	}
	
	private void update(){
		if (mTipWindow == null){
			createWindow();
		}
		if (isShow == false){
			this.setVisibility(INVISIBLE);
		}else{
			this.setVisibility(VISIBLE);
		}
		if (AeviouConstants.keyboardView != null) {
			mTipWindow.showAsDropDown(AeviouConstants.keyboardView);
			mTipWindow.update(x, y, width, height);
			this.invalidate();
		}
	}
	
	public void commitUpdate(){
		Message msg= new Message();
		msg.what = AeviouConstants.MSG_UPDATE_KEYBOARDTIP;
		handler.sendMessage(msg);
	}

	public void onDraw(Canvas canvas) {
		if (mTipWindow == null)
			return;
		canvas.setDrawFilter(drawFilter);
		leftKey.draw(canvas, paint);
		centerKey.draw(canvas, paint);
		rightKey.draw(canvas, paint);
	}

	public void closeWindow() {
		if (mTipWindow == null)
			return;
		this.setVisibility(INVISIBLE);
		mTipWindow.dismiss();
		mTipWindow = null;
	}

	private void createWindow() {
		if (mTipWindow != null) {
			closeWindow();
		}
		mTipWindow = new PopupWindow(this);
		mTipWindow.setTouchable(false);
		mTipWindow.setContentView(this);
	}

	public void reset() {
		createWindow();
		isShow=false;
	}
}
