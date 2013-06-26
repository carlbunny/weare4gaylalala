package com.aeviou.candidate;

import com.aeviou.pinyin.PinyinContext;
import com.aeviou.utils.AeviouConstants;
import com.aeviou.utils.BitmapUtils;

import android.graphics.Canvas;
import android.graphics.Paint;

public class CandidateKey {
	public static final int TYPE_SINGLE = 0;
	public static final int TYPE_LEFT = 1;
	public static final int TYPE_RIGHT = 2;
	public static final int TYPE_MID = 3;
	
	public int x, y;
	public int type;
	public int candidateId;
	public int[] leftMargin;
	public String word;
	
	public CandidateKey(){
		type = TYPE_SINGLE;
		leftMargin = new int[PinyinContext.MAX_SETENCE_LENGTH + 1];
		leftMargin[0] = 0;
		leftMargin[1] = 15;
		leftMargin[2] = -10;
		leftMargin[3] = 32;
		leftMargin[4] = 10;
		leftMargin[5] = 50;
		leftMargin[6] = 30;
		leftMargin[7] = -4;
		leftMargin[8] = 47;
		leftMargin[9] = 20;
		leftMargin[10] = -12;
		word = null;
	}
	
	public void drawBackground(Canvas canvas, Paint paint){
		if (candidateId != -1){
			switch (type){
			case TYPE_SINGLE:
				BitmapUtils.drawBitmap(canvas, paint, AeviouConstants.PIC_ID.CANDIDATE_SINGLE_BG, x - 1, y,
					AeviouConstants.NORMAL_IME_HEXKEY_WIDTH + 2, AeviouConstants.NORMAL_IME_HEXKEY_WIDTH - 9);
				break;
			case TYPE_MID:
				BitmapUtils.drawBitmap(canvas, paint, AeviouConstants.PIC_ID.CANDIDATE_MID_BG, x - 1, y,
					AeviouConstants.NORMAL_IME_HEXKEY_WIDTH + 2, AeviouConstants.NORMAL_IME_HEXKEY_WIDTH - 9);
				break;
			case TYPE_LEFT:
				BitmapUtils.drawBitmap(canvas, paint, AeviouConstants.PIC_ID.CANDIDATE_LEFT_BG, x - 1, y,
					AeviouConstants.NORMAL_IME_HEXKEY_WIDTH + 2, AeviouConstants.NORMAL_IME_HEXKEY_WIDTH - 9);
				break;
			case TYPE_RIGHT:
				BitmapUtils.drawBitmap(canvas, paint, AeviouConstants.PIC_ID.CANDIDATE_RIGHT_BG, x - 1, y,
					AeviouConstants.NORMAL_IME_HEXKEY_WIDTH + 2, AeviouConstants.NORMAL_IME_HEXKEY_WIDTH - 9);
				break;
			}
		}
	}
	
	public void drawText(Canvas canvas, Paint paint){
		if (word != null){
			canvas.drawText(word, 
					AeviouConstants.NORMAL_CANDIDATE_KEY_LEFT_MARGIN + x + leftMargin[word.length()], 
					AeviouConstants.NORMAL_CANDIDATE_KEY_TOP_MARGIN + y,
					paint);
		}
	}
	
}
