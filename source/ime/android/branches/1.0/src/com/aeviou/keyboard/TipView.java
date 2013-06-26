package com.aeviou.keyboard;

import com.aeviou.key.HexKey;
import com.aeviou.utils.AeviouConstants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.PopupWindow;

public class TipView extends View {
	public static final int GAP = 10;
	
	private PopupWindow mTipWindow;
	private Handler handler;
	private boolean isShow=false;
	
	private Bitmap bitmap;
	private Canvas mCanvas;
	private Paint paint;
	private RectF rect;
	private Rect cutRect;
	private PaintFlagsDrawFilter drawFilter;

	private final int crossRadius = 10;
	private final int crossWidth = 3;
	
	private int x,y;
	private int width,height;
	
	HexKeyboard hexKeyboard;

	public TipView(Context context,HexKeyboard hexKeyboard) {
		super(context);
		rect = new RectF();
		cutRect=new Rect();
		drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		mCanvas = new Canvas();
		paint = new Paint();
		paint.setTextSize(AeviouConstants.NORMAL_LETTER_HEIGHT);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(crossWidth);
		mTipWindow = null;
		

		this.hexKeyboard=hexKeyboard;
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case AeviouConstants.MSG_UPDATE_TIPVIEW:
					TipView.this.update();
					break;
				}
				super.handleMessage(msg);
			}

		};
		isShow=false;
		
		width=3*AeviouConstants.NORMAL_IME_HEXKEY_WIDTH;
		height=3*AeviouConstants.NORMAL_IME_HEXKEY_HEIGHT;
		
	}
	
	public void onDraw(Canvas canvas) {
		if (bitmap != null) {
			canvas.setDrawFilter(drawFilter);
			canvas.drawBitmap(bitmap, null, cutRect, null);
		}
	}
	
	public void updatePostion(Point point){
		if(point!=null){
			HexKey lastKey = hexKeyboard.path.getLast();
			if (lastKey == null)
				return;
			int left = (int) (lastKey.centerX - 1.5 * AeviouConstants.NORMAL_IME_HEXKEY_WIDTH), top = (int) (lastKey.centerY - 1.5 * AeviouConstants.NORMAL_IME_HEXKEY_HEIGHT), right = (int) (lastKey.centerX + 1.5 * AeviouConstants.NORMAL_IME_HEXKEY_WIDTH), bottom = (int) (lastKey.centerY + 1.5 * AeviouConstants.NORMAL_IME_HEXKEY_HEIGHT);

			if (left < 0) {
				right -= left;
				left = 0;
			} else if (right > AeviouConstants.NORMAL_IME_VIEW_WIDTH) {
				left -= (right - AeviouConstants.NORMAL_IME_VIEW_WIDTH);
				right = AeviouConstants.NORMAL_IME_VIEW_WIDTH;
			}
			if (top < 0) {
				bottom -= top;
				top = 0;
			} else if (bottom > AeviouConstants.NORMAL_IME_VIEW_HEIGHT) {
				top -= (bottom - AeviouConstants.NORMAL_IME_VIEW_HEIGHT);
				bottom = AeviouConstants.NORMAL_IME_VIEW_HEIGHT;
			}
			float[] touchPoint = { hexKeyboard.lastPoint.x - left,
					hexKeyboard.lastPoint.y - top };

			rect.set(left, top, right, bottom);
			AeviouConstants.keyboardView.canvasMatrix.mapRect(rect);
			AeviouConstants.keyboardView.canvasMatrix.mapPoints(touchPoint);

			bitmap = Bitmap.createBitmap(AeviouConstants.keyboardView.mBuffer,
					(int) rect.left, (int) rect.top, (int) rect.width(),
					(int) rect.height());
			mCanvas.setBitmap(bitmap);
			mCanvas.drawLine(touchPoint[0] - crossRadius, touchPoint[1],
					touchPoint[0] + crossRadius, touchPoint[1], paint);
			mCanvas.drawLine(touchPoint[0], touchPoint[1] - crossRadius,
					touchPoint[0], touchPoint[1] + crossRadius, paint);

			x=0;
			y=-AeviouConstants.IME_VIEW_HEIGHT-height;
			width=(int) rect.width();
			height=(int) rect.height();
			
			cutRect.set(GAP, GAP, this.getWidth()- GAP, this.getHeight() - GAP);
			
			isShow=true;
		}else isShow=false;
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
		msg.what = AeviouConstants.MSG_UPDATE_TIPVIEW;
		handler.sendMessage(msg);
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
