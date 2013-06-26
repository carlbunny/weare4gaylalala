package com.aeviou.key;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.aeviou.keyboard.AbstractKeyboard;
import com.aeviou.utils.AeviouConstants;
import com.aeviou.utils.BitmapUtils;

public class SettingKey extends HexKey{
	
	public enum SwitchKeyEnum{  
		  Chinese, English, Number, Setting ,Center, Symbol 
	}
	
	public SwitchKeyEnum switchKey;
	public int xIndex,yIndex;

	public SettingKey(SwitchKeyEnum switchKey){ 
		this.switchKey=switchKey;
		this.type=AbstractKey.TYPE_KEY_SWITCH;
	}
	
	public void setIndex(int x,int y){
		xIndex=x;
		yIndex=y;
	}
	
	public void switchLanguage(){
		switch(switchKey){
		case Chinese:
			if (AeviouConstants.keyboardView.getCurrentKeyboard() != AbstractKeyboard.Type.Aeviou){
				AeviouConstants.keyboardView.switchKeyboard(AbstractKeyboard.Type.Aeviou);
				//backgroundBitmap = AeviouConstants.BMP_SWITCH_BACKGROUND;
				AeviouConstants.needRedraw = true;
			}
			break;
		case English:
			if (AeviouConstants.keyboardView.getCurrentKeyboard() != AbstractKeyboard.Type.English){
				AeviouConstants.keyboardView.switchKeyboard(AbstractKeyboard.Type.English);
				//backgroundBitmap = AeviouConstants.BMP_SWITCH_SQUARE_BACKGROUND;
				AeviouConstants.needRedraw = true;
			}
			break;
		case Number:
			if (AeviouConstants.keyboardView.getCurrentKeyboard() != AbstractKeyboard.Type.Number){
				AeviouConstants.keyboardView.switchKeyboard(AbstractKeyboard.Type.Number);
				//backgroundBitmap = AeviouConstants.BMP_SWITCH_SQUARE_BACKGROUND;
				AeviouConstants.needRedraw = true;
			}
			break;
		case Symbol:
			if (AeviouConstants.keyboardView.getCurrentKeyboard() != AbstractKeyboard.Type.Symbol){
				AeviouConstants.keyboardView.switchKeyboard(AbstractKeyboard.Type.Symbol);
				//backgroundBitmap = AeviouConstants.BMP_SWITCH_SQUARE_BACKGROUND;
				AeviouConstants.needRedraw = true;
			}
			break;
		case Setting:
			Intent preferenceIntent = new Intent(
					AeviouConstants.inputMethodService,
					com.aeviou.setting.SettingView.class);
			preferenceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			AeviouConstants.inputMethodService.startActivity(preferenceIntent);
			break;
		}
	}
	
	public void draw(Canvas canvas, Paint paint) {
//		if (type == TYPE_KEY_HIDE && status == HEXKEY_STATUS_DISABLED) return;
//		if (type == TYPE_KEY_HIDE && status == HEXKEY_STATUS_NORMAL) return;
//		if (AeviouConstants.hideOthers && status == HEXKEY_STATUS_DISABLED) return;

		//float pixelWidthHalf;
		//char letter = this.getName();
		AeviouConstants.PIC_ID textureBackgroundIndex = AeviouConstants.PIC_ID.HEX_NORAML;
		AeviouConstants.PIC_ID textureFrontIndex=AeviouConstants.PIC_ID.HEX_NORAML;

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
		switch (switchKey) {
		case Center:
			break;
		case Chinese:
			textureFrontIndex = AeviouConstants.PIC_ID.SWITCH_CH;
			break; 
		case English:
			textureFrontIndex = AeviouConstants.PIC_ID.SWITCH_EN;
			break;
		case Number:
			textureFrontIndex = AeviouConstants.PIC_ID.SWITCH_NUM;
			break;
		case Setting:
			textureFrontIndex = AeviouConstants.PIC_ID.SWITCH_SETTING;
			break;
		case Symbol:
			textureFrontIndex = AeviouConstants.PIC_ID.SWITCH_SYMBOL;
			break;
		}
		BitmapUtils.drawBitmap(canvas, paint, textureFrontIndex, x, y,
				width, height);
		
	}
	

}
