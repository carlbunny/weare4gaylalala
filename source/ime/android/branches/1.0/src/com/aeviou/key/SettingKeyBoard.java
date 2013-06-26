package com.aeviou.key;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.aeviou.utils.AeviouConstants;
import com.aeviou.utils.Vector2F;

//SettingKeyBoard is a keyunit which can be draw on the origin keyboard
public class SettingKeyBoard extends HexKey{

	public SettingKey keys[];
	private SettingKey cacheKey=null;
	private SettingKey lastKey=null;
	private SettingKey startKey=null;
	
	public boolean isOpen=false;
	private Vector2F sildeVector;
	
	public void calCenter(){
		//load the position account to the aeviou keyboard
		//copy the neighbour position to the setting Key
		super.calCenter();
		
//		int width=(int) (1.5f*AeviouConstants.NORMAL_IME_HEXKEY_WIDTH);
//		int height=(int) (1.5f*AeviouConstants.NORMAL_IME_HEXKEY_HEIGHT);
		for(SettingKey key:keys){

			if(Math.abs(key.yIndex%2)==1){
				key.x = x+(int)(width * ((float)key.xIndex - 0.5f)); 
			}else{
				key.x=x+width * key.xIndex;
			}
			key.y = y+(int)(height * key.yIndex * 0.75);
			key.setSize(width, height);
			key.calCenter();
		}
		sildeVector=new Vector2F();
	}
	
	public void draw(Canvas canvas, Paint paint) {
		if(isOpen==false){
			super.draw(canvas, paint);
		}else{
			paint.setTextSize(AeviouConstants.NORMAL_LETTER_HEIGHT);
			paint.setColor(Color.WHITE);
	
			for (SettingKey key : keys) {
				key.draw(canvas, paint);
			}
		}
	}

	public void onTouch(int x, int y) {
		isOpen=true;
		
		startKey=getKeyByPoint(x, y);
		if(startKey!=null){
			startKey.status=KEY_STATUS.selected;
			for(SettingKey key:keys){
				key.status=KEY_STATUS.next;
			}
			lastKey=startKey;
		}
	}

	public void onMove(int x, int y) {
		if((lastKey=getKeyByPoint(x, y))==null)
			fillMiddlePoint(x,y);

		if(lastKey!=null){
			for(SettingKey key:keys){
				key.status=KEY_STATUS.next;
			}
			lastKey.status=KEY_STATUS.selected;
		}
	}
	


	public void onRelease(int x, int y) {
		if((lastKey=getKeyByPoint(x, y))==null)
			fillMiddlePoint(x,y);
		if(lastKey!=null){
			lastKey.switchLanguage();		
			cacheKey=null;
		}
		for(SettingKey key:keys){
			key.status=KEY_STATUS.normal;
		}
		isOpen=false;
	}

	
	void fillMiddlePoint(int x, int y){
		SettingKey key;
		int middleX, middleY;
		if (startKey == null) return;
		sildeVector.set(x - startKey.centerX, y - startKey.centerY);
		if(sildeVector.isZero() == false){
			sildeVector.normalized();
			key=startKey;
			while(Math.abs(x - key.centerX) > width ||
					Math.abs(y - key.centerY) > height){
				middleX = (int)((key.centerX + sildeVector.x *width));
				middleY = (int)((key.centerY + sildeVector.y *width));
				key = getKeyByPoint(middleX, middleY);
				if(key == null){
					break;
				}else lastKey=key;
			}
		}
	}
	
	protected SettingKey getKeyByPoint(int x, int y) {
		
		if (cacheKey != null){
			if (cacheKey.inGeometry(x, y)){
				return cacheKey;
			}else{
				for (SettingKey key : keys){
					if (key != null){
						if (key.inGeometry(x, y)) {
							return cacheKey = key;
						}
					}
				}
			}
			
		}
		
		for (SettingKey key : keys) {
			if (key.inGeometry(x, y)) {
				return cacheKey = key;
			}
		}
		return cacheKey = null;
	}

	
}
