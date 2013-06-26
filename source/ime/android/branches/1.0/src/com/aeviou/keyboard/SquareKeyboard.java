package com.aeviou.keyboard;

import com.aeviou.key.AbstractKey;
import com.aeviou.key.SquareKey;
import com.aeviou.utils.AeviouConstants;
import com.aeviou.utils.SoundUtils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;

public class SquareKeyboard extends AbstractKeyboard {
	SquareKey[] keys;
	Paint paint;
	
	boolean isShift;

	public SquareKeyboard(SquareKey[] keys) {
		this.keys = keys;
		paint = new Paint();
		paint.setTextSize(AeviouConstants.NORMAL_LETTER_HEIGHT);
		paint.setColor(Color.WHITE);
		
		isShift=false;
	}

	@Override
	public void draw(Canvas canvas) {
		for (SquareKey key : keys) {
			key.draw(canvas, paint);
		}
	}

	@Override
	public void onTouch(int x, int y) {

	}

	@Override
	public void onMove(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRelease(int x, int y) {
		SquareKey key = getKeyByPoint(x, y);
		if (key == null)
			return;

		int type = key.getType();
		switch (type) {
		case AbstractKey.TYPE_KEY_SHIFT:
			applyShift();
			break;
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
		AeviouConstants.needRedraw = true;
	}

	protected SquareKey getKeyByPoint(int x, int y) {
		for (SquareKey key : keys) {
			if (key.inGeometry(x, y)) {
				return key;
			}
		}
		return null;
	}
	
	protected void applyShift() {
		isShift=!isShift;
		for (SquareKey key : keys) {
			if(key.type==SquareKey.TYPE_KEY_LETTER) {
				if(isShift)
					key.doCaps();
				else
					key.undoCpas();
			}
		}
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
	public boolean occupyBarView() {
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
