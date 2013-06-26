package com.aeviou.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import com.aeviou.*;
import com.aeviou.utils.AeviouConstants.PIC_ID;

public class BitmapUtils {

	public static Bitmap[] bitmapArr=new Bitmap[PIC_ID.values().length];
	public static Rect rect=new Rect();
	
	public static void initBitmapUtils(Resources res) {
		BitmapDrawable bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.key_normal);
		bitmapArr[PIC_ID.HEX_NORAML.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.key_selected);
		bitmapArr[PIC_ID.HEX_SELECTED.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.key_next);
		bitmapArr[PIC_ID.HEX_NEXT.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.key_changed);
		bitmapArr[PIC_ID.HEX_CHANGED.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.set);
		bitmapArr[PIC_ID.SET.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.space);
		bitmapArr[PIC_ID.SPACE.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.enter);
		bitmapArr[PIC_ID.ENTER.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.num);
		bitmapArr[PIC_ID.NUM.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.del);
		bitmapArr[PIC_ID.DEL.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.aeo);
		bitmapArr[PIC_ID.AOE.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.left_arrow);
		bitmapArr[PIC_ID.CANDIDATE_LEFT_ARROW.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.right_arrow);
		bitmapArr[PIC_ID.CANDIDATE_RIGHT_ARROW.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.candidate_left);
		bitmapArr[PIC_ID.CANDIDATE_LEFT_BG.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.candidate_right);
		bitmapArr[PIC_ID.CANDIDATE_RIGHT_BG.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.candidate_mid);
		bitmapArr[PIC_ID.CANDIDATE_MID_BG.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.candidate_single);
		bitmapArr[PIC_ID.CANDIDATE_SINGLE_BG.ordinal()] = bmpDraw.getBitmap();
		
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.switch_en);
		bitmapArr[PIC_ID.SWITCH_EN.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.switch_ch);
		bitmapArr[PIC_ID.SWITCH_CH.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.switch_num);
		bitmapArr[PIC_ID.SWITCH_NUM.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.switch_setting);
		bitmapArr[PIC_ID.SWITCH_SETTING.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.switch_background);
		bitmapArr[PIC_ID.SWITCH_BACKGROUND.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.switch_square_background);
		bitmapArr[PIC_ID.SWITCH_SQUARE_BACKGROUND.ordinal()] = bmpDraw.getBitmap();
		//square key
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.key_quare_normal);
		bitmapArr[PIC_ID.SQUARE_NORAML.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.shift);
		bitmapArr[PIC_ID.SHIFT.ordinal()] = bmpDraw.getBitmap();
		
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.close_button);
		bitmapArr[PIC_ID.CLOSE.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.caps);
		bitmapArr[PIC_ID.CAPS.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.switch_hybrid);
		bitmapArr[PIC_ID.SWITCH_HYBRID.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.exit_hybrid);
		bitmapArr[PIC_ID.EXIT_HYBRID.ordinal()] = bmpDraw.getBitmap();
		
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.switch_symbol);
		bitmapArr[PIC_ID.SWITCH_SYMBOL.ordinal()] = bmpDraw.getBitmap();
		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.switch_key);
		bitmapArr[PIC_ID.SWITCH_KEY.ordinal()] = bmpDraw.getBitmap();
		
//		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.symbol_1);
//		bitmapArr[PIC_ID.symbol_1.ordinal()] = bmpDraw.getBitmap();
//		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.symbol_2);
//		bitmapArr[PIC_ID.symbol_2.ordinal()] = bmpDraw.getBitmap();
//		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.symbol_5);
//		bitmapArr[PIC_ID.symbol_5.ordinal()] = bmpDraw.getBitmap();
//		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.symbol_6);
//		bitmapArr[PIC_ID.symbol_6.ordinal()] = bmpDraw.getBitmap();
//		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.symbol_7);
//		bitmapArr[PIC_ID.symbol_7.ordinal()] = bmpDraw.getBitmap();
//		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.symbol_11);
//		bitmapArr[PIC_ID.symbol_11.ordinal()] = bmpDraw.getBitmap();
//		bmpDraw = (BitmapDrawable) res.getDrawable(R.drawable.symbol_12);
//		bitmapArr[PIC_ID.symbol_12.ordinal()] = bmpDraw.getBitmap();
	}
	
	public static void drawBitmap(Canvas canvas,Paint paint,PIC_ID bitmap,
			int left,int top,int width,int height){
		rect.set(left, top, left+width, top+height);
		canvas.drawBitmap(bitmapArr[bitmap.ordinal()], null,
				rect, paint);
	}

}
