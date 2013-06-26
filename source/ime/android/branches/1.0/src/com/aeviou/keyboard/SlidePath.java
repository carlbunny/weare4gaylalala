package com.aeviou.keyboard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.aeviou.key.HexKey;
import com.aeviou.key.AbstractKey.KEY_STATUS;
import com.aeviou.pinyin.PinyinTree;
import com.aeviou.utils.AeviouConstants;

public class SlidePath {
	private HexKey[] path;
	private int length;
	private PinyinTree pinyinTree;

	public SlidePath() {
		path = new HexKey[6];
		length = 0;
		pinyinTree = PinyinTree.getInstance();
	}
	
	public HexKey getLast() {
		return length>0 ? path[length-1] : null;
	}

	public boolean isEmpty() {
		return length==0;
	}
	
	public void draw(Canvas canvas, Paint paint) {
		final int[] colorArr={Color.rgb(255,251,132),Color.rgb(255,173,110)};
		final int[][] offsetArr={{-10,10},{-7,7}};
		
		if (length <= 1)
			return;
		float oldWidth = paint.getStrokeWidth();
		int oldColor=paint.getColor();
		paint.setStrokeWidth(20);
		
		int indexOffset;
		for(int p=0;p<2;p++) {
			paint.setColor(colorArr[p]);
			for (int i = 0; i < length - 1; i++) {
				indexOffset=path[i].centerY==path[i + 1].centerY?0:1;
				canvas.drawLine(path[i].centerX, path[i].centerY+offsetArr[indexOffset][p],
						path[i + 1].centerX, path[i + 1].centerY+offsetArr[indexOffset][p], paint);
			}
		}
		
		paint.setStrokeWidth(oldWidth);
		paint.setColor(oldColor);
	}

	public void StartInKey(HexKey key, HexKey[] keys, boolean upper) {
		if(key.getType()==HexKey.TYPE_KEY_HIDE)return;
		path[length++] = key;
		if (!upper){
			key.updateDynamicKeyboard(keys);
		}
		key.status = KEY_STATUS.selected;
		setNext(path[length - 1]);
	}

	public boolean MoveInKey(HexKey key, HexKey[] keys) {
		boolean canMoveIn=false;
		switch (key.status) {
		case selected:
			if (length == 1)
				break;
			if (path[length - 2] == key) {
				// restore last neighbor
				restoreCurrent(path[length - 1]);
				// delete path key
				length--;
				setNext(path[length - 1]);
				canMoveIn=true;
			}
			break;// not move out the last key
		case next:
			restoreCurrent(path[length - 1]);
			path[length - 1].status=KEY_STATUS.selected;
			// add to path
			path[length++] = key;
			key.status = KEY_STATUS.selected;
			setNext(path[length - 1]);

			canMoveIn=true;
			break;
		}
		return canMoveIn;
	}

	public String EndInKey(HexKey[] keys) {
		if (length == 0)
			return null;
		String pinyin = getPinyin();
		for (HexKey tempKey : keys) {
			tempKey.resetName();
		}
		length = 0;
		return pinyin;
	}

	public String getPinyin() {
		char [] pinyin = new char[length];
		char name;
		int i;
		for (i = 0; i < length; i++) {
			name = path[i].getName();
			if (name == AeviouConstants.KEYCODE_AOE){
				name = 'v';
			}
			pinyin[i] = name;
		}
		if (length > 0){
			if (pinyin[0] == 'a' || pinyin[0] == 'e' || pinyin[0] == 'o'){
				return "v" + new String(pinyin);
			}
		}
		return new String(pinyin);
	}

	private void setNext(HexKey key) {
		HexKey nextKey;
		int type = key.getType();
		if(type == HexKey.TYPE_KEY_LETTER || type== HexKey.TYPE_KEY_HIDE) {
			char[] nextLetters = pinyinTree.getNextLetters(getPinyin());
			
			for (int i = 0; i < 6; i++) {
				nextKey = key.neighbours[i];
				if(nextKey==null)continue;
				for (int j = 0; j < nextLetters.length; j++) {
					if (nextKey.getName() == nextLetters[j]
							&& nextKey.status ==KEY_STATUS.changed) {
						nextKey.status =KEY_STATUS.next;
						break;
					}
				}
			}
		}else {//symbols
			if(length == 1) {
				for (int i = 0; i < 6; i++) {
					nextKey = key.neighbours[i];
					if (nextKey == null) continue;
					if (nextKey.dynamicName == 0) continue;
					nextKey.status = KEY_STATUS.next;
				}
			}
		}
	}

	private void restoreCurrent(HexKey key) {
		HexKey lastNeighbour;
		key.status = KEY_STATUS.changed;
		for (int i = 0; i < 6; i++) {
			lastNeighbour = key.neighbours[i];
			if(lastNeighbour==null)continue;
			if (lastNeighbour.status ==KEY_STATUS.next)
				lastNeighbour.status =KEY_STATUS.changed;
		}
	}
}
