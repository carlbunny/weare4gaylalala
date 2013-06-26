package com.aeviou.key;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.aeviou.utils.AeviouConstants;
import com.aeviou.utils.BitmapUtils;

public class HexKey extends AbstractKey {


	public static final int NAME_KEY_AIE = 0;
	public static final int NAME_KEY_PERIOD = 1;
	public static final int NAME_KEY_SPACE = 2;
	public static final int NAME_KEY_BACKSPACE = 3;
	public static final int NAME_KEY_ENTER = 4;
	/*
	 * The keys adjacent to this key. If count < 6, the remains will be set to
	 * null
	 */
	public HexKey[] neighbours;
	/*
	 * The letter when original letter changed. If the letter has not been
	 * changed, It will be set to 0;
	 */
	public char dynamicName;
	public int dynamicType;
	public int[] dynamicKeyIds;
	public char[] dynamicKeyNames;

	private float yScale_inv = 1.0f;
	private float halfWidthSqaure = 0;
	public char leftChar = 0, rightChar = 0;
	
	//for draw symbolcenter key
	public String[] hexText; //three line

	public HexKey() {
		dynamicName = 0;
		dynamicKeyIds = null;
		dynamicKeyNames = null;
		status = KEY_STATUS.normal;
		isCap=false;

		setSize(AeviouConstants.NORMAL_IME_HEXKEY_WIDTH,
				AeviouConstants.NORMAL_IME_HEXKEY_HEIGHT);
	}

	public void setSize(int width, int height) {
		super.setSize(width, height);
		yScale_inv = (width * AeviouConstants.NORMAL_IME_HEXKEY_HEIGHTSCALE)
				/ height;
		halfWidthSqaure = width * width * 0.25f;
	}
	
	public char getName() {
		if(isCap)return super.getName();
		return dynamicName == 0 ? name : dynamicName;
	}
	
	public int  getType() {
		if(isCap)return super.getType();
		return dynamicName == 0 ? type : dynamicType;
	}

	public void setDynamicName(char name) {
		this.status = KEY_STATUS.changed;
		dynamicName = name;
	}

	public void resetName() {
		this.status = KEY_STATUS.normal;
		dynamicName = 0;
	}

	public void updateDynamicKeyboard(HexKey[] keys) {
		if (dynamicKeyIds == null)
			return;
		if (dynamicKeyIds.length == 0)
			return;
		for (int i = 0; i < dynamicKeyIds.length; i++) {
			keys[dynamicKeyIds[i]].dynamicName = dynamicKeyNames[i];
			keys[dynamicKeyIds[i]].status = KEY_STATUS.changed;
			if (keys[dynamicKeyIds[i]].dynamicName >= 'a'
					&& keys[dynamicKeyIds[i]].dynamicName <= 'z') {
				keys[dynamicKeyIds[i]].dynamicType = TYPE_KEY_LETTER;
			} else {
				keys[dynamicKeyIds[i]].dynamicType = TYPE_KEY_SYMBOL;
			}
		}
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].status == KEY_STATUS.normal) {
				keys[i].status = KEY_STATUS.disable;
			}
		}
	}

	@Override
	public boolean inGeometry(int x, int y) {
		float dx = x - centerX, dy = (y - centerY) * yScale_inv;

		float distanceSquare = dx * dx + dy * dy;
		return distanceSquare <= halfWidthSqaure;
	}

	//static final float[][] offset={{-1,-1},{1,-1},{-2,0},{2,0},{-1,1},{1,1}};
	static final float[] offset={-1,0,1};
	@Override
	public void draw(Canvas canvas, Paint paint) {
		if (type == TYPE_KEY_HIDE && status == KEY_STATUS.disable)
			return;
		if (type == TYPE_KEY_HIDE && status == KEY_STATUS.normal)
			return;
		if (AeviouConstants.hideOthers && status == KEY_STATUS.disable)
			return;

		float pixelWidthHalf;
		char letter = this.getName();
		AeviouConstants.PIC_ID textureBackgroundIndex = AeviouConstants.PIC_ID.HEX_NORAML;
		switch (status) {
		case normal:
			break;
		case selected:
			textureBackgroundIndex = AeviouConstants.PIC_ID.HEX_SELECTED;
			break;
		case next:
			textureBackgroundIndex = AeviouConstants.PIC_ID.HEX_NEXT;
			break;
		case changed:
			textureBackgroundIndex = AeviouConstants.PIC_ID.HEX_CHANGED;
			break;
		}
		BitmapUtils.drawBitmap(canvas, paint, textureBackgroundIndex, x, y,
				width, height);

		switch (letter) {
		case AeviouConstants.KEYCODE_SPACE:
			BitmapUtils.drawBitmap(canvas, paint, AeviouConstants.PIC_ID.SPACE, x,
					y, width, height);
			break;
		case AeviouConstants.KEYCODE_HYBRID:
			BitmapUtils.drawBitmap(canvas, paint,
					AeviouConstants.PIC_ID.EXIT_HYBRID, x, y, width, height);
			break;
		case AeviouConstants.KEYCODE_BACKSPACE:
			BitmapUtils.drawBitmap(canvas, paint, AeviouConstants.PIC_ID.DEL, x,
					y, width, height);
			break;
		case AeviouConstants.KEYCODE_AOE:
			BitmapUtils.drawBitmap(canvas, paint, AeviouConstants.PIC_ID.AOE, x,
					y, width, height);
			break;
		case AeviouConstants.KEYCODE_ENTER:
			BitmapUtils.drawBitmap(canvas, paint, AeviouConstants.PIC_ID.ENTER, x,
					y, width, height);
			break;
		case AeviouConstants.KEYCODE_CAPS:
			BitmapUtils.drawBitmap(canvas, paint, AeviouConstants.PIC_ID.CAPS, x,
					y, width, height);
			break;
		case AeviouConstants.KEYCODE_SHIFT:
			BitmapUtils.drawBitmap(canvas, paint, AeviouConstants.PIC_ID.SHIFT, x,
					y, width, height);
			break;
		case AeviouConstants.KEYCODE_SWITCH:
			BitmapUtils.drawBitmap(canvas, paint, AeviouConstants.PIC_ID.SWITCH_KEY, x,
					y, width, height);
			break;
		case AeviouConstants.KEYCODE_NULL:
			break;
		case AeviouConstants.KEYCODE_SYMBOLCENTER:
			
			if (this.status == KEY_STATUS.disable) {
				paint.setColor(Color.GRAY);
			} else {
				paint.setColor(Color.WHITE);
			}
			for(int i=0;i<3;i++){
				pixelWidthHalf = paint.measureText(hexText[i]) / 2;
				canvas.drawText(hexText[i], centerX-pixelWidthHalf,
						 centerY + AeviouConstants.NORMAL_LETTER_HEIGHT*(0.25f+offset[i]), paint);
			}
			
			break;
		default:
			String drawtext=String.valueOf(letter);
			
			pixelWidthHalf = paint.measureText(drawtext) / 2;
			if (this.status == KEY_STATUS.disable) {
				paint.setColor(Color.GRAY);
			} else {
				paint.setColor(Color.WHITE);
			}
			 canvas.drawText(drawtext, centerX - pixelWidthHalf,
			 centerY + AeviouConstants.NORMAL_LETTER_HEIGHT / 4, paint);
			break;
		}
	}

	public void updateUpper(boolean upperStatus) {
		if (upperStatus) {
			if (name >= 'a' && name <= 'z') {
				name += 'A' - 'a';
			}
		} else {
			if (name >= 'A' && name <= 'Z') {
				name -= 'A' - 'a';
			}
		}

	}

	public void calCenter() {
		centerX = x + width / 2;
		centerY = y + height / 2;
	}
}
