package com.aeviou.key;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class AbstractKey{
	public static final int TYPE_KEY_LETTER = 0;
	public static final int TYPE_KEY_HIDE = 1;
	public static final int TYPE_KEY_SYMBOL = 2;
	public static final int TYPE_KEY_SPACE = 3;
	public static final int TYPE_KEY_ENTER = 4;
	public static final int TYPE_KEY_BACKSPACE = 5;
	public static final int TYPE_KEY_SHIFT = 6;
	public static final int TYPE_KEY_CAPS = 7;
	public static final int TYPE_KEY_HYBRID = 8;
	public static final int TYPE_KEY_SWITCH = 9;
	
	public enum KEY_STATUS{
		normal,selected,next,changed,disable;
	}
	
	public boolean isCap;

	public KEY_STATUS status;
	public int x,y;
	public int width=0,height=0,minWidhHeight=0;;
	public float widthStep=1.0f,heighthStep=1.0f;
	public int row,col;
	
	public char name,capName;

	public int centerX,centerY;
	public int id;

	public int type;





	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		minWidhHeight=Math.min(width, height);
	}
	
	public void setPos(int x,int y){
		this.x=x;
		this.y=y; 
	}

	public abstract boolean inGeometry(int x, int y);
	
	public abstract void draw(Canvas canvas, Paint paint);
	
	public abstract void calCenter();

	public void setRowCol(int row,int col){
		this.row=row;
		this.col=col;
	}
	
	public char getName() {
		return isCap ? capName : name;
	}

	public int getType() {
		return type;
	}
	
	public void doCaps() {
		isCap=!isCap;
	}
	
	public void undoCpas() {
		isCap=!isCap;
	}
}
