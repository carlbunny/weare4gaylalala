package com.aeviou.keyboard;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.aeviou.key.HexKey;
import com.aeviou.key.SettingKeyBoard;

public abstract class AbstractKeyboard {
	public enum Type{
		Aeviou,English,Number,Symbol
	}
	
	public abstract void draw(Canvas canvas);
	public abstract void onTouch(int x, int y);
	public abstract void onMove(int x, int y);
	public abstract void onRelease(int x, int y);
	public abstract void hibernate();
	public abstract void wakeup();
	
	public abstract void reset();
	public abstract void close();
	
	public abstract boolean occupyBarView();
	public void setDrawingCache(Bitmap drawingCache) {}
	
	protected SettingKeyBoard settingkeyBoard;
	
	protected HexKey lastKey=null;
	public int gridX,gridY;
	void setGridResolution(int gridX,int gridY){
		this.gridX=gridX;
		this.gridY=gridY;
	}
}
