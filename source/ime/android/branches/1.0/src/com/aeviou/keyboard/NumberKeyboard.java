package com.aeviou.keyboard;

import com.aeviou.key.AbstractKey;
import com.aeviou.key.KeySet;
import com.aeviou.utils.AeviouConstants;
import com.aeviou.utils.SoundUtils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;

public class NumberKeyboard extends AbstractKeyboard {
	AbstractKey[] keys;
	KeySet keySet=null;
	
	Paint paint;
	
	public NumberKeyboard(AbstractKey[] keys) {
		this.keys = keys;
		keySet=new KeySet(keys);
		
		paint = new Paint();
		paint.setTextSize(AeviouConstants.NORMAL_LETTER_HEIGHT);
		paint.setColor(Color.WHITE);
	}
	
	public void draw(Canvas canvas) {
		for (AbstractKey key : keys) {
			key.draw(canvas, paint);
		}
		//draw over the original keyboard
		settingkeyBoard.draw(canvas, paint);
		
	}

//	@Override
	public void onTouch(int x, int y) {
		// TODO Auto-generated method stub
		if(settingkeyBoard.inGeometry(x, y))settingkeyBoard.onTouch(x, y);
		AeviouConstants.needRedraw=true;
	}

//	@Override
	public void onMove(int x, int y) {
		// TODO Auto-generated method stub
		if(settingkeyBoard.isOpen)settingkeyBoard.onMove(x, y);
		AeviouConstants.needRedraw=true;
	}

//	@Override
	public void onRelease(int x, int y) {
		// TODO Auto-generated method stub
		if(settingkeyBoard.isOpen){
			settingkeyBoard.onRelease(x, y);
		}else{
			AbstractKey key = getKeyByPoint(x, y);
			if (key == null)
				return;
			int type = key.getType();
			switch (type) {
//			case AbstractKey.TYPE_KEY_SHIFT:
//				applyShift();
//				break;
			case AbstractKey.TYPE_KEY_BACKSPACE:
				AeviouConstants.inputMethodService
					.getCurrentInputConnection()
					.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
				AeviouConstants.inputMethodService
					.getCurrentInputConnection()
					.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
				break;
			case AbstractKey.TYPE_KEY_ENTER:
				AeviouConstants.inputMethodService
					.getCurrentInputConnection()
					.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
				AeviouConstants.inputMethodService
					.getCurrentInputConnection()
					.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
				break;
			case AbstractKey.TYPE_KEY_SPACE:
				AeviouConstants.inputMethodService
					.getCurrentInputConnection()
					.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE));
				AeviouConstants.inputMethodService
					.getCurrentInputConnection()
					.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_SPACE));
				break;
			case AbstractKey.TYPE_KEY_SYMBOL:
			case AbstractKey.TYPE_KEY_LETTER:
				AeviouConstants.inputMethodService.getCurrentInputConnection()
						.commitText(String.valueOf(key.getName()), 1);
				break;
			}
			SoundUtils.playSoundAndVibration();
		}
		AeviouConstants.needRedraw=true;
	}

	protected AbstractKey getKeyByPoint(int x, int y) {
		return keySet.getKeyByPoint(x,y);
	}

	@Override
	public void hibernate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wakeup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean occupyBarView() {
		// TODO Auto-generated method stub
		return false;
	}
}
