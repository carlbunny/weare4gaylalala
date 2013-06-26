package com.aeviou.keyboard;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class KeyCh {

	public static final String TAG = "TAG_KEY_CH";

	public static final byte KEY_STATE_NORMAL = 0x01;
	public static final byte KEY_STATE_PRESSED = 0x02;
	public static final byte KEY_STATE_CANDIDATE1 = 0x03;
	public static final byte KEY_STATE_CANDIDATE2 = 0x04;

	public static final char KEYCODE_SET = 0xFF51;
	public static final char KEYCODE_NUM = 0xFF52;
	public static final char KEYCODE_SPACE = 0xFF53;
	public static final char KEYCODE_DEL = 0xFF54;
	public static final char KEYCODE_ENTER = 0xFF55;
	public static final char KEYCODE_NULL = 0xFFFF;
	public static final char KEYCODE_EN = 0xFF56;
	public static final char KEYCODE_SYMBOL = 0xFF57;
	public static final char KEYCODE_SYMBOLSHIFTED = 0xFF58;

	public static final int NEIGHBOURS_END = 255;

	public char code;
	public byte state;
	public boolean isChanged;
	public Bitmap oldBmp;
	public PointF pos;
	public int row;
	public int column;
	public boolean isVisible;
	public int neighours[];
	public int seq;
	private boolean isInit = false;

	public KeyCh(char code) {
		this.code = code;
		this.state = KEY_STATE_NORMAL;
		this.isChanged = true;
		this.pos = new PointF();
		this.neighours = new int[6];
		this.isInit = false;
		this.isVisible=false ;
	}

	public void setRowColumn(int seq) {
		this.seq = seq;
		if (seq < 6) {
			this.row = 0;
			this.column = seq;
		} else if ((seq >= 6) && (seq < 13)) {
			this.row = 1;
			this.column = seq - 6;
		} else if ((seq >= 13) && (seq < 19)) {
			this.row = 2;
			this.column = seq - 13;
		} else if ((seq >= 19) && (seq < 26)) {
			this.row = 3;
			this.column = seq - 19;
		} else if ((seq >= 26) && (seq < 32)) {
			this.row = 4;
			this.column = seq - 26;
		}
	}

	public void setNeibours(int seq) {
		int index = 0;
		if (seq != 0 && seq != 6 && seq != 13 && seq != 19 && seq != 26) {
			this.neighours[index++] = seq - 1;
		}
		if (seq != 5 && seq != 12 && seq != 18 && seq != 25 && seq != 31) {
			this.neighours[index++] = seq + 1;
		}
		if (seq > 5 && seq != 12 && seq != 25) {
			this.neighours[index++] = seq - 6;
		}
		if (seq > 5 && seq != 6 && seq != 19) {
			this.neighours[index++] = seq - 7;
		}
		if (seq < 26 && seq != 6 && seq != 19) {
			this.neighours[index++] = seq + 6;
		}
		if (seq < 26 && seq != 25 && seq != 12) {
			this.neighours[index++] = seq + 7;
		}
		for (; index < 6;) {
			this.neighours[index++] = NEIGHBOURS_END;
		}
	}

	public void setPos(float height, float width) {

		if (this.row % 2 == 0)
			this.pos.x = this.column * width + width;
		else
			this.pos.x = this.column * width + width / 2;
		this.pos.y = this.row * height * 3 / 4 + height / 2;
	}

	public void refresh(Canvas canvas, float height, float width, Bitmap bmp[],
			Paint paint) {
		if (this.isChanged && this.isVisible) {
			switch (this.state) {
			case KEY_STATE_NORMAL:
				this.refreshKeyNormal(canvas, height, width, bmp, paint);
				break;
			case KEY_STATE_PRESSED:
				this.refreshKeyPressed(canvas, height, width, bmp, paint);
				break;
			case KEY_STATE_CANDIDATE1:
				this.refreshKeyCandidate1(canvas, height, width, bmp[2], paint);
				break;
			case KEY_STATE_CANDIDATE2:
				this.refreshKeyCandidate2(canvas, height, width, bmp[3], paint);
				break;
			}
		}
	}

	private void refreshKeyNormal(Canvas canvas, float height, float width,
			Bitmap[] bmp, Paint paint) {
		if (this.isChanged)
			this.isChanged = false;
		if (this.isInit)
			return;
		float len = paint.measureText(String.valueOf(this.code)) / 2;
		float top, left;
		left = this.pos.x - width / 2;
		top = this.pos.y - height / 2;

		RectF rect = new RectF(left, top, left + width, top + height);
		canvas.drawBitmap(bmp[0], null, rect, paint);

		if (this.code < 0xFF00) {
			canvas.drawText(String.valueOf(code), pos.x - len, pos.y + height
					/ 10, paint);
		} else {
			switch (code) {
			case KEYCODE_SET:
				canvas.drawBitmap(bmp[AeviouKeyboardView.BMP_SET], null, rect,
						paint);
				break;
			case KEYCODE_NUM:
				canvas.drawBitmap(bmp[AeviouKeyboardView.BMP_NUM], null, rect,
						paint);
				break;
			case KEYCODE_SPACE:
				canvas.drawBitmap(bmp[AeviouKeyboardView.BMP_SPACE], null,
						rect, paint);
				break;
			case KEYCODE_DEL:
				canvas.drawBitmap(bmp[AeviouKeyboardView.BMP_DEL], null, rect,
						paint);
				break;
			case KEYCODE_ENTER:
				canvas.drawBitmap(bmp[AeviouKeyboardView.BMP_ENTER], null,
						rect, paint);
				break;
			case KEYCODE_EN:
				canvas.drawText("En", pos.x - len, pos.y + height / 10, paint);
				break;
			case '£¬':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '¡£':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '£¡':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '£¿':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '£¨':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '£©':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '¡¢':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '£¤':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case KEYCODE_NULL:
				break;
			}
		}
		this.isInit = true;
	}

	private void refreshKeyPressed(Canvas canvas, float height, float width,
			Bitmap[] bmp, Paint paint) {
		float len = paint.measureText(String.valueOf(this.code)) / 2;
		float top, left;
		left = this.pos.x - width / 2;
		top = this.pos.y - height / 2;

		RectF rect = new RectF(left, top, left + width, top + height);
		canvas.drawBitmap(bmp[1], null, rect, paint);

		if (this.code < 0xFF00) {
			canvas.drawText(String.valueOf(code), pos.x - len, pos.y + height
					/ 10, paint);
		} else {
			switch (code) {
			case KEYCODE_SET:
				canvas.drawBitmap(bmp[AeviouKeyboardView.BMP_SET], null, rect,
						paint);
				break;
			case KEYCODE_NUM:
				canvas.drawBitmap(bmp[AeviouKeyboardView.BMP_NUM], null, rect,
						paint);
				break;
			case KEYCODE_SPACE:
				canvas.drawBitmap(bmp[AeviouKeyboardView.BMP_SPACE], null,
						rect, paint);
				break;
			case KEYCODE_DEL:
				canvas.drawBitmap(bmp[AeviouKeyboardView.BMP_DEL], null, rect,
						paint);
				break;
			case KEYCODE_ENTER:
				canvas.drawBitmap(bmp[AeviouKeyboardView.BMP_ENTER], null,
						rect, paint);
				break;
			case KEYCODE_EN:
				canvas.drawText("En", pos.x - len, pos.y + height / 10, paint);
				break;
			case '£¬':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '¡£':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '£¡':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '£¿':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '£¨':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '£©':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '¡¢':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '£¤':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case KEYCODE_NULL:
				break;
			}
		}
	}

	private void refreshKeyCandidate1(Canvas canvas, float height, float width,
			Bitmap bmp, Paint paint) {
		float len = paint.measureText(String.valueOf(code)) / 2;
		float top, left;
		left = this.pos.x - width / 2;
		top = this.pos.y - height / 2;

		RectF rect = new RectF(left, top, left + width, top + height);
		canvas.drawBitmap(bmp, null, rect, paint);
		// canvas.drawText(String.valueOf(code), pos.x-len, pos.y+height/10,
		// paint) ;

		if (this.code < 0xFF00) {
			canvas.drawText(String.valueOf(code), pos.x - len, pos.y + height
					/ 10, paint);
		} else {
			switch (code) {
			case KEYCODE_EN:
				canvas.drawText("En", pos.x - len, pos.y + height / 10, paint);
				break;
			case '£¬':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '¡£':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '£¡':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '£¿':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '£¨':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '£©':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '¡¢':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case '£¤':
				canvas.drawText(String.valueOf(code), pos.x - len, pos.y
						+ height / 10, paint);
				break;
			case KEYCODE_NULL:
				break;

			}
		}

	}

	private void refreshKeyCandidate2(Canvas canvas, float height, float width,
			Bitmap bmp, Paint paint) {
		float len = paint.measureText(String.valueOf(code)) / 2;
		float top, left;
		left = this.pos.x - width / 2;
		top = this.pos.y - height / 2;

		RectF rect = new RectF(left, top, left + width, top + height);
		canvas.drawBitmap(bmp, null, rect, paint);
		canvas.drawText(String.valueOf(code), pos.x - len, pos.y + height / 10,
				paint);
	}

}
