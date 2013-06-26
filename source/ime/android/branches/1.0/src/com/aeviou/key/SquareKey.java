package com.aeviou.key;

import com.aeviou.utils.AeviouConstants;
import com.aeviou.utils.BitmapUtils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class SquareKey extends AbstractKey {
	


	@Override
	public boolean inGeometry(int x, int y) {
		int xDistance = (x - this.x);
		int yDistance = (y - this.y);
		return (xDistance < width * widthStep)
				&& (yDistance < height);
	}


	
	@Override
	public void draw(Canvas canvas, Paint paint) {
		// TODO Auto-generated method stub
		float pixelWidthHalf;
		char letter = this.getName();
		AeviouConstants.PIC_ID textureBackgroundIndex = AeviouConstants.PIC_ID.SQUARE_NORAML;

		BitmapUtils.drawBitmap(canvas, paint, textureBackgroundIndex, x, y,
				width, height);
		switch (letter) {
		case AeviouConstants.KEYCODE_SPACE:
			BitmapUtils.drawBitmap(canvas, paint,
					AeviouConstants.PIC_ID.SPACE, centerX-width/2, y, width, height);
			break;
		case AeviouConstants.KEYCODE_BACKSPACE:
			BitmapUtils.drawBitmap(canvas, paint, AeviouConstants.PIC_ID.DEL,
					centerX-width/2, y, width, height);
			break;
		case AeviouConstants.KEYCODE_AOE:
			BitmapUtils.drawBitmap(canvas, paint, AeviouConstants.PIC_ID.AOE,
					centerX-width/2, y, width, height);
			break;
		case AeviouConstants.KEYCODE_ENTER:
			BitmapUtils.drawBitmap(canvas, paint,
					AeviouConstants.PIC_ID.ENTER, centerX-width/2, y, width, height);
			break;
		case AeviouConstants.KEYCODE_SHIFT:
			BitmapUtils.drawBitmap(canvas, paint,
					AeviouConstants.PIC_ID.SHIFT, centerX-width/2, y, width, height);
			break;
		case AeviouConstants.KEYCODE_NULL:
			break;
		default:
			String text = String.valueOf(letter);
			pixelWidthHalf = paint.measureText(text) / 2;
			paint.setColor(Color.BLACK);
			canvas.drawText(text, centerX - pixelWidthHalf + 4,
					centerY + AeviouConstants.NORMAL_LETTER_HEIGHT/4 + 4, paint);
			paint.setColor(Color.WHITE);
			canvas.drawText(text, centerX - pixelWidthHalf,
					centerY + AeviouConstants.NORMAL_LETTER_HEIGHT/4, paint);
			break;
		}
	}
	
	public void calCenter(){
		centerX = (int)(x + width * widthStep / 2);
		centerY = y + height / 2;
	}

}
