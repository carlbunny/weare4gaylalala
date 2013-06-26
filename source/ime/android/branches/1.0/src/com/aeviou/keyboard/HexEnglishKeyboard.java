package com.aeviou.keyboard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;

import com.aeviou.key.AbstractKey;
import com.aeviou.key.HexKey;
import com.aeviou.key.KeySet;
import com.aeviou.key.SquareKey;
import com.aeviou.key.AbstractKey.KEY_STATUS;
import com.aeviou.utils.AeviouConstants;
import com.aeviou.utils.SoundUtils;

public class HexEnglishKeyboard extends AbstractKeyboard {

	private HexKey[] keys;
	KeySet keySet=null;
	
	Paint paint;
	private KeyBoardTip keyBoardTip;

	private HexKey cacheKey = null;// for search key
	private HexKey startKey = null;// first touched key
	
	boolean isShift;

	public HexEnglishKeyboard(HexKey[] keys) {
		this.keys = keys;
		keySet=new KeySet(keys);
		
		paint = new Paint();
		paint.setTextSize(AeviouConstants.NORMAL_LETTER_HEIGHT);
		paint.setColor(Color.WHITE);
		
		keyBoardTip=new KeyBoardTip(AeviouConstants.keyboardView.getContext());
		
		isShift=false;
	}

	public void draw(Canvas canvas) {
		for (AbstractKey key : keys) {
			key.draw(canvas, paint);
		}
		// draw over the original keyboard
		settingkeyBoard.draw(canvas, paint);
	}

	@Override
	public void onTouch(int x, int y) {
		startKey=null;
		getKeyByPoint(x, y);
		if (startKey != null) {
			lastKey=startKey;
			if (startKey.getType() == AbstractKey.TYPE_KEY_SWITCH) {
				// setting key
				settingkeyBoard.onTouch(x, y);
			} else {
				startKey.status = KEY_STATUS.selected;
				for (HexKey neighborKey : startKey.neighbours) {
					neighborKey.status = KEY_STATUS.next;
				}
				AeviouConstants.needRedraw = true;
				keyBoardTip.setChar(startKey, this,AeviouConstants.Direction.center);
				keyBoardTip.commitUpdate();
			}
		}
		
	}

	@Override
	public void onMove(int x, int y) {
		if (settingkeyBoard.isOpen) {
			settingkeyBoard.onMove(x, y);
		} else {
			getKeyByPoint(x, y);
			if (cacheKey != null) {
				if (cacheKey != startKey) {
					startKey.status = KEY_STATUS.next;
				} else
					startKey.status = KEY_STATUS.selected;
				for (HexKey neighborKey : startKey.neighbours) {
					neighborKey.status = neighborKey == cacheKey ? KEY_STATUS.selected
							: KEY_STATUS.next;
				}
				lastKey=cacheKey;
				
				if(lastKey.name==startKey.leftChar){
					keyBoardTip.setChar(startKey, this,AeviouConstants.Direction.left);
				}else if(lastKey.name==startKey.rightChar){
					keyBoardTip.setChar(startKey, this,AeviouConstants.Direction.right);
				}else{
					keyBoardTip.setChar(startKey, this,AeviouConstants.Direction.center);
				}
				keyBoardTip.commitUpdate();
			}
		}
		AeviouConstants.needRedraw = true;
		
	}

	@Override
	public void onRelease(int x, int y) {
		AeviouConstants.needRedraw = true;

		if (settingkeyBoard.isOpen) {
			settingkeyBoard.onRelease(x, y);
		} else {
			AbstractKey key=lastKey;
			if (key != null){
				
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
				//reset
				if(startKey!=null){
					startKey.status=KEY_STATUS.normal;
					for (HexKey neighborKey : startKey.neighbours) {
						neighborKey.status=KEY_STATUS.normal;
					}
				}
				keyBoardTip.setChar(null, this,AeviouConstants.Direction.center);
				keyBoardTip.commitUpdate();
			}
			startKey=cacheKey=null;
			SoundUtils.playSoundAndVibration();
		}

	}

	protected void applyShift() {
		isShift=!isShift;
		for (HexKey key : keys) {
			if(key.type==SquareKey.TYPE_KEY_LETTER) {
				if(isShift)
					key.doCaps();
				else
					key.undoCpas();
			}
		}
	}

	protected HexKey getKeyByPoint(int x, int y) {
		if (startKey != null) {
			if (cacheKey != null && cacheKey.inGeometry(x, y)) {
				return cacheKey;
			} else {
				for (HexKey key : startKey.neighbours) {
					if (key.inGeometry(x, y)) {
						return cacheKey = key;
					}
				}
				if(startKey.inGeometry(x, y)){
					return cacheKey = startKey;
				}
			}
		}
		return startKey = cacheKey = (HexKey) keySet.getKeyByPoint(x,y);
	}
	
	

	@Override
	public void hibernate() {
		// TODO Auto-generated method stub
		keyBoardTip.closeWindow();
	}

	@Override
	public void wakeup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		keyBoardTip.reset();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		keyBoardTip.closeWindow();
	}

	@Override
	public boolean occupyBarView() {
		// TODO Auto-generated method stub
		return false;
	}

}
