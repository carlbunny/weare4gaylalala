package com.aeviou.keyboard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.KeyEvent;

import com.aeviou.key.AbstractKey;
import com.aeviou.key.AbstractKey.KEY_STATUS;
import com.aeviou.key.HexKey;
import com.aeviou.key.KeySet;
import com.aeviou.utils.AeviouConstants;
import com.aeviou.utils.SoundUtils;
import com.aeviou.utils.Vector2F;

public class SymbolKeyboard extends AbstractKeyboard {
	private HexKey keys[];
	KeySet keySet=null;
	
	private Paint paint;

	public Point lastPoint;
	private HexKey lastKey;// for fill the middle point
	private HexKey cacheKey;// for fast find current move key
	private HexKey startKey = null;
	private Vector2F sildeVector;

	public SymbolKeyboard(HexKey keys[]) {
		this.keys = keys;
		keySet=new KeySet(keys);
		
		lastPoint = new Point();
		sildeVector = new Vector2F();
		paint = new Paint();
	}

	public void draw(Canvas canvas) {

		paint.setTextSize(AeviouConstants.NORMAL_LETTER_HEIGHT);
		paint.setColor(Color.WHITE);

		for (HexKey key : keys) {
			key.draw(canvas, paint);
		}
		// draw over the original keyboard
		if (settingkeyBoard != null)
			settingkeyBoard.draw(canvas, paint);

	}

	public void onTouch(int x, int y) {
		startKey = getKeyByPoint(x, y);
		lastPoint.set(x, y);
		if (startKey != null) {
			if (startKey.getType() == AbstractKey.TYPE_KEY_SWITCH) {
				// setting key
				settingkeyBoard.onTouch(x, y);
			} else {
				startKey.updateDynamicKeyboard(keys);
			}
			AeviouConstants.needRedraw = true;
			lastKey = startKey;

		}

		SoundUtils.playSoundAndVibration();
	}

	public void onMove(int x, int y) {
		AeviouConstants.needRedraw = true;
		lastPoint.set(x, y);

		if (settingkeyBoard.isOpen) {
			settingkeyBoard.onMove(x, y);
		} else {
			if(lastKey!=null&&lastKey.status==AbstractKey.KEY_STATUS.selected)
				lastKey.status=AbstractKey.KEY_STATUS.changed;
			if ((lastKey = getKeyByPoint(x, y)) == null)
				fillMiddlePoint(x, y);
			if(lastKey!=null&&lastKey.status!=KEY_STATUS.disable){
				lastKey.status=AbstractKey.KEY_STATUS.selected;
			}
		}
	}

	public void onRelease(int x, int y) {
		AeviouConstants.needRedraw = true;

		if (settingkeyBoard.isOpen) {
			settingkeyBoard.onRelease(x, y);
		} else {
			fillMiddlePoint(x, y);
			if (lastKey!= null&&lastKey.status!=KEY_STATUS.disable){
				int type = lastKey.getType();
				switch (type) {
					case AbstractKey.TYPE_KEY_BACKSPACE:
						AeviouConstants.inputMethodService.getCurrentInputConnection()
								.sendKeyEvent(
										new KeyEvent(KeyEvent.ACTION_DOWN,
												KeyEvent.KEYCODE_DEL));
						AeviouConstants.inputMethodService.getCurrentInputConnection()
								.sendKeyEvent(
										new KeyEvent(KeyEvent.ACTION_UP,
												KeyEvent.KEYCODE_DEL));
						break;
					case AbstractKey.TYPE_KEY_ENTER:
						AeviouConstants.inputMethodService.getCurrentInputConnection()
								.sendKeyEvent(
										new KeyEvent(KeyEvent.ACTION_DOWN,
												KeyEvent.KEYCODE_ENTER));
						AeviouConstants.inputMethodService.getCurrentInputConnection()
								.sendKeyEvent(
										new KeyEvent(KeyEvent.ACTION_UP,
												KeyEvent.KEYCODE_ENTER));
						break;
					case AbstractKey.TYPE_KEY_SPACE:
						AeviouConstants.inputMethodService.getCurrentInputConnection()
								.sendKeyEvent(
										new KeyEvent(KeyEvent.ACTION_DOWN,
												KeyEvent.KEYCODE_SPACE));
						AeviouConstants.inputMethodService.getCurrentInputConnection()
								.sendKeyEvent(
										new KeyEvent(KeyEvent.ACTION_UP,
												KeyEvent.KEYCODE_SPACE));
						break;
					case AbstractKey.TYPE_KEY_SYMBOL:
					case AbstractKey.TYPE_KEY_LETTER:
						AeviouConstants.inputMethodService.getCurrentInputConnection()
								.commitText(String.valueOf(lastKey.getName()), 1);
						break;
				}
			}
			for (HexKey tempKey : keys) {
				tempKey.resetName();
			}
			SoundUtils.playSoundAndVibration();
		}
	}

	void fillMiddlePoint(int x, int y) {
		HexKey key;
		int middleX, middleY;
		if (startKey == null)
			return;
		sildeVector.set(x - startKey.centerX, y - startKey.centerY);
		if (sildeVector.isZero() == false) {
			sildeVector.normalized();
			key = startKey;
			while (Math.abs(x - key.centerX) > key.width
					|| Math.abs(y - key.centerY) > key.height) {
				middleX = (int) ((key.centerX + sildeVector.x * key.width));
				middleY = (int) ((key.centerY + sildeVector.y * key.width));
				key = getKeyByPoint(middleX, middleY);
				if (key == null) {
					break;
				} else
					lastKey = key;
			}
		}
	}

	protected HexKey getKeyByPoint(int x, int y) {
		if (cacheKey != null) {
			if (cacheKey.inGeometry(x, y)) {
				return cacheKey;
			}
		}
		return cacheKey = (HexKey) keySet.getKeyByPoint(x,y);
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
