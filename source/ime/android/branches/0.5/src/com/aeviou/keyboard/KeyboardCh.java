package com.aeviou.keyboard;

import com.aeviou.pinyin.PinyinTree;
import android.inputmethodservice.Keyboard;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.PointF;

import android.widget.PopupWindow;
import android.util.Log;
import android.view.Gravity;
import android.os.Handler;
import android.os.Message;

public class KeyboardCh {
	public static final int ERROR_KEYSEQ = 0xFF;
	public static final int KEYNUM = 32;
	public static final byte MSG_TIPVIEW = 0x01;

	public static final float FONTSIZE = 25;

	public static final int STROKEWIDTH = 5;
	public static final char[] KEYLAYOUT = { 'q', 'w', 'r', 't', 'y', 'p',
			'\'', 'f', 's', 'd', 'g', 'l', 'h', 'x', 'b', 'z', 'c', 'n', 'm',
			'j', KeyCh.KEYCODE_SET, KeyCh.KEYCODE_NUM, 'k',
			KeyCh.KEYCODE_SPACE, KeyCh.KEYCODE_DEL, KeyCh.KEYCODE_ENTER };
	public KeyCh[] key;
	private KeyCh[] keyTemp;
	private int keyPathSize;

	private AeviouKeyboardView view;
	public boolean isCreated = false;
	private Bitmap mBuffer;
	private Canvas mCanvas;

	String PinyinSeq = null;
	private PopupWindow mTipWindow;

	TipView tipView;
	Handler handler;
	boolean mTipsFlag = true;
	PointF pointTemp;

	public KeyboardCh(AeviouKeyboardView view) {
		this.view = view;
		keyTemp = new KeyCh[7];
		keyPathSize = 0;
		PinyinTree.getInstance();
	}

	public void loadKeyboard() {
		pointTemp = new PointF();
		tipView = new TipView(view.getContext());
		key = new KeyCh[KEYNUM];
		for (int i = 0; i < KEYNUM; i++) {
			if (i < 6) {
				key[i] = new KeyCh(KeyCh.KEYCODE_NULL);
				key[i].isVisible = false;
			} else {
				key[i] = new KeyCh(KEYLAYOUT[i - 6]);
				key[i].isVisible = true;
			}
			key[i].setNeibours(i);
			key[i].setRowColumn(i);
			key[i].setPos(view.keyHeight, view.keyWidth);
		}
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_TIPVIEW:
					int size = keyPathSize;
					if (size != 0){
						setTipView(keyTemp[size - 1].pos.x,
								keyTemp[size - 1].pos.y);
					}
					break;
				}
				super.handleMessage(msg);
			}
		};
		this.isCreated = true;
	}

	public void unLoadKeyboard() {
		this.isCreated = false;
		keyPathSize = 0;
		for (int i = 0; i < keyPathSize; i++){
			keyTemp[i] = null;
		}
	}

	public void refresh(Canvas canvas) {
		if (!this.isCreated)
			return;
		if (mBuffer == null) {
			mBuffer = Bitmap.createBitmap(view.getWidth(),
					(int) (view.getHeight()), Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBuffer);
			for (int i = 0; i < KEYNUM; i++) {
				key[i].refresh(mCanvas, view.keyHeight, view.keyWidth,
						view.bmp, view.paint);
			}
		}
		canvas.drawBitmap(mBuffer, 0, 0, null);
		for (int i = 0; i < KEYNUM; i++) {
			key[i].refresh(canvas, view.keyHeight, view.keyWidth, view.bmp,
					view.paint);
		}
		int size = keyPathSize;
		if (size > 1) {
			float oldWidth = view.paint.getStrokeWidth();
			view.paint.setStrokeWidth(5);
			for (int i = 0; i < size - 1; i++) {
				canvas.drawLine(keyTemp[i].pos.x, keyTemp[i].pos.y,
						keyTemp[i + 1].pos.x, keyTemp[i + 1].pos.y,
						view.paint);
			}
			view.paint.setStrokeWidth(oldWidth);
		}

		if (PinyinSeq != null) {
			if (PinyinSeq.length() != 0)
				canvas.drawText(PinyinSeq, 0, view.keyHeight / 4, view.paint);
		}
		if ((handler != null) && mTipsFlag) {
			Message msg = new Message();
			msg.what = MSG_TIPVIEW;
			handler.sendMessage(msg);
		}
	}

	private void setTipView(float X, float Y) {
		Bitmap mTempBuffer = Bitmap.createBitmap(view.getWidth(),
				(int) (view.getHeight()), Bitmap.Config.ARGB_8888);
		mTempBuffer = view.getDrawingCache();
		int size = keyPathSize;
		if (size == 0)
			return;
		Rect rect = TipView.getNeighourRect((int) X, (int) Y, view);
		Bitmap temp = Bitmap.createBitmap(mTempBuffer, rect.left, rect.top,
				rect.right, rect.bottom);
		tipView.setBitmap(temp);
		mTipWindow.setHeight(rect.bottom);
		mTipWindow.setWidth(rect.right);
		this.mTipWindow.setContentView(tipView);
		this.mTipWindow.showAtLocation(view, Gravity.NO_GRAVITY, (int) (150),
				(int) (-250));
	}

	public void handleKeyDown(float x, float y) {
		if (this.mTipsFlag)
			this.mTipWindow = new PopupWindow(view.getContext());
		else {
			if (mTipWindow != null)
				mTipWindow.dismiss();
		}
		if (y > view.keyHeight) {
			keyPathSize = 0;
			int seq = getKeySeq(x, y);
			if (seq == ERROR_KEYSEQ)
				return;
			if (keyPathSize > 6) {
				this.view.playSound(AeviouKeyboardView.KEYWARNING);
				return;
			}

			this.view.clearCandidate();
			view.playSound(AeviouKeyboardView.KEYAUDIO);
			this.ch_KeyLayout(key[seq].code);
			this.ch_KeyState(seq, KeyCh.KEY_STATE_PRESSED);
			keyTemp[keyPathSize] = key[seq];
			keyPathSize++;
			this.setNextKeyState(key[seq]);
			this.view.invalidate();
		}
	}

	public void handleKeyMove(float x, float y) {
		int seqTemp = getKeySeq(x, y);
		if (seqTemp == ERROR_KEYSEQ || keyPathSize == 0)
			return;
		int size = keyPathSize;
		if ((key[seqTemp].state == KeyCh.KEY_STATE_CANDIDATE2)
				|| (key[seqTemp].state == KeyCh.KEY_STATE_NORMAL))
			return;
		if (seqTemp != keyTemp[size - 1].seq) {
			if (size >= 2) {
				if (seqTemp == keyTemp[size - 2].seq) {
					this.setReturnKeyState(keyTemp[size - 1]);
					this.ch_KeyState(keyTemp[size - 1].seq,
							KeyCh.KEY_STATE_CANDIDATE1);
					keyPathSize--;
					this.setNextKeyState(keyTemp[size - 2]);

					this.view.invalidate();
					return;
				}
			}
			if (key[seqTemp].state == KeyCh.KEY_STATE_CANDIDATE1) {
				this.ch_KeyState(seqTemp, KeyCh.KEY_STATE_PRESSED);
				this.setReturnKeyState(keyTemp[size - 1]);
				keyTemp[keyPathSize] = key[seqTemp];
				keyPathSize++;

				this.setNextKeyState(key[seqTemp]);
				this.view.invalidate();
			}
		}

	}

	public void handleKeyUp(float x, float y) {
		String pinyin = "";
		if (mTipWindow != null){
			this.mTipWindow.dismiss();
		}
		if (keyPathSize != 0) {
			if (keyTemp[0].code < 0xFF00) {// pinyin
				pinyin = this.getKeyString();
				if (PinyinTree.getInstance().isCompletedPinyin(pinyin)) {
					this.commitPinyin(pinyin);
				} else {
					view.playSound(AeviouKeyboardView.KEYWARNING);
				}
				keyPathSize = 0;
			} else {// function key
				this.handleFunctionKey();
				keyPathSize = 0;
			}
		}
		this.restorOriginalLayout();
		this.view.invalidate();
	}

	private void handleFunctionKey() {
		int size = keyPathSize;
		if (size == 0)
			return;
		if (size == 1) {
			switch (keyTemp[0].code) {
			case KeyCh.KEYCODE_SPACE:
				this.commitChar(' ');
				break;
			case KeyCh.KEYCODE_DEL:
				this.commitChar(Keyboard.KEYCODE_DELETE);
				break;
			case KeyCh.KEYCODE_ENTER:
				this.commitChar(KeyCh.KEYCODE_ENTER);
				break;
			default:
				this.commitChar(keyTemp[0].code);
			}
		} else if (size == 2) {
			this.commitChar(keyTemp[1].code);
		}
	}

	private int getKeySeq(float x, float y) {
		int temp = ERROR_KEYSEQ;
		float distance = 0, oldDistance = 20000;
		if (view.isVertical) {
			for (int i = 0; i < KEYNUM; i++) {
				if (Math.abs(key[i].pos.x - x) > view.keyWidth / 2)// 80
					continue;
				if (Math.abs(key[i].pos.y - y) > view.keyHeight / 2)
					continue;
				distance = (key[i].pos.x - x) * (key[i].pos.x - x)
						+ (key[i].pos.y - y) * (key[i].pos.y - y);
				if (distance < oldDistance) {
					temp = i;
					oldDistance = distance;
				}
			}
		} else {

			for (int i = 0; i < KEYNUM; i++) {
				if (Math.abs(key[i].pos.y - y) > view.keyWidth / 2)
					continue;
				if (Math.abs(key[i].pos.x - x) > view.keyHeight / 2)// 80
					continue;

				distance = (key[i].pos.x - x) * (key[i].pos.x - x)
						+ (key[i].pos.y - y) * (key[i].pos.y - y);
				if (distance < oldDistance) {
					temp = i;
					oldDistance = distance;
				}
			}
		}
		return temp;
	}

	private void setNextKeyState(KeyCh key1) {
		if (key1.code < 0xFF) {
			String pinyin = getKeyString();
			char[] nextLetters = PinyinTree.getInstance()
					.getNextLetters(pinyin);
			if (key1.code == '\'') {
				nextLetters[0] = 'a';
				nextLetters[1] = 'e';
				nextLetters[2] = 'o';
			}
			for (int i = 0; i < 6; i++) {
				if (key1.neighours[i] == KeyCh.NEIGHBOURS_END)
					break;
				if (key[key1.neighours[i]].state != KeyCh.KEY_STATE_CANDIDATE2)
					continue;
				for (int j = 0; j < 6; j++) {
					if (key[key1.neighours[i]].code == (nextLetters[j])
							&& nextLetters[j] != -1) {
						this.ch_KeyState(key1.neighours[i],
								KeyCh.KEY_STATE_CANDIDATE1);
					}
				}
			}
		} else {
			switch (key1.code) {
			case KeyCh.KEYCODE_SET:
				this.ch_KeyID(19, KeyCh.KEYCODE_SYMBOLSHIFTED);
				this.ch_KeyState(19, KeyCh.KEY_STATE_CANDIDATE1);
				this.ch_KeyID(20, KeyCh.KEYCODE_SYMBOL);
				this.ch_KeyState(20, KeyCh.KEY_STATE_CANDIDATE1);
				this.ch_KeyID(27, KeyCh.KEYCODE_EN);
				this.ch_KeyState(27, KeyCh.KEY_STATE_CANDIDATE1);
				break;
			case KeyCh.KEYCODE_NUM:
				this.ch_KeyID(26, '£¬');
				this.ch_KeyState(26, KeyCh.KEY_STATE_CANDIDATE1);
				this.ch_KeyID(28, '¡£');
				this.ch_KeyState(28, KeyCh.KEY_STATE_CANDIDATE1);
				this.ch_KeyID(20, '£¿');
				this.ch_KeyState(20, KeyCh.KEY_STATE_CANDIDATE1);
				this.ch_KeyID(21, '£¡');
				this.ch_KeyState(21, KeyCh.KEY_STATE_CANDIDATE1);
				break;
			case KeyCh.KEYCODE_SPACE:
				this.ch_KeyID(28, '£¨');
				this.ch_KeyState(28, KeyCh.KEY_STATE_CANDIDATE1);
				this.ch_KeyID(30, '£©');
				this.ch_KeyState(30, KeyCh.KEY_STATE_CANDIDATE1);
				this.ch_KeyID(22, '¡¢');
				this.ch_KeyState(22, KeyCh.KEY_STATE_CANDIDATE1);
				this.ch_KeyID(23, '£¤');
				this.ch_KeyState(23, KeyCh.KEY_STATE_CANDIDATE1);
				break;
			case KeyCh.KEYCODE_DEL:
				break;
			case KeyCh.KEYCODE_ENTER:
				break;
			}
		}
	}

	private void setReturnKeyState(KeyCh key1) {
		if (key1.code < 0xFF00) {
			for (int i = 0; i < 6; i++) {
				if (key1.neighours[i] == KeyCh.NEIGHBOURS_END)
					break;
				if (key[key1.neighours[i]].state == KeyCh.KEY_STATE_CANDIDATE1) {
					this.ch_KeyState(key1.neighours[i],
							KeyCh.KEY_STATE_CANDIDATE2);
				}
			}
		}
	}

	private void restorOriginalLayout() {
		for (int i = 0; i < KEYNUM; i++) {
			if (i < 6) {
				this.ch_KeyID(i, KeyCh.KEYCODE_NULL);
				this.ch_KeyState(i, KeyCh.KEY_STATE_NORMAL);
				key[i].isVisible = false;
			} else {
				this.ch_KeyID(i, KEYLAYOUT[i - 6]);
				this.ch_KeyState(i, KeyCh.KEY_STATE_NORMAL);
			}
		}
	}

	private String getKeyString() {
		StringBuffer temp = new StringBuffer();
		char code;
		if (keyPathSize == 0)
			temp = null;
		else {
			for (int i = 0; i < keyPathSize; i++) {
				code = keyTemp[i].code;
				temp.append(code);
			}
		}
		return temp.toString();
	}

	private void ch_KeyID(int seq, char code) {
		key[seq].code = code;
	}

	private void ch_KeyState(int seq, byte keyState) {
		key[seq].state = keyState;
		if (seq < 6) key[seq].isVisible = true;
		key[seq].isChanged = true;
	}

	private void ch_KeyLayout(char keyID) {
		switch (keyID) {
		case 'b':
			this.ch_KeyID(7, 'n');
			this.ch_KeyState(7, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(8, 'g');
			this.ch_KeyState(8, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(13, 'o');
			this.ch_KeyState(13, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(14, 'a');
			this.ch_KeyState(14, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(21, 'i');
			this.ch_KeyState(21, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(26, 'u');
			this.ch_KeyState(26, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(27, 'e');
			this.ch_KeyState(27, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(28, 'n');
			this.ch_KeyState(28, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(29, 'g');
			this.ch_KeyState(29, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case 'c':
			this.ch_KeyID(7, 'g');
			this.ch_KeyState(7, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(8, 'n');
			this.ch_KeyState(8, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(9, 'i');
			this.ch_KeyState(9, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(10, 'o');
			this.ch_KeyState(10, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(11, 'n');
			this.ch_KeyState(11, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(12, 'g');
			this.ch_KeyState(12, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(14, 'i');
			this.ch_KeyState(14, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(15, 'e');
			this.ch_KeyState(15, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(16, 'h');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(17, 'u');
			this.ch_KeyState(17, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(18, 'i');
			this.ch_KeyState(18, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(20, 'a');
			this.ch_KeyState(20, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(21, 'u');
			this.ch_KeyState(21, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(23, 'a');
			this.ch_KeyState(23, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(24, 'n');
			this.ch_KeyState(24, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(25, 'g');
			this.ch_KeyState(25, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(26, 'g');
			this.ch_KeyState(26, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(27, 'n');
			this.ch_KeyState(27, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(28, 'o');
			this.ch_KeyState(28, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(29, 'i');
			this.ch_KeyState(29, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(30, 'o');
			this.ch_KeyState(30, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case 'd':
			this.ch_KeyID(6, 'g');
			this.ch_KeyState(6, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(7, 'n');
			this.ch_KeyState(7, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(8, 'o');
			this.ch_KeyState(8, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(9, 'a');
			this.ch_KeyState(9, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(10, 'n');
			this.ch_KeyState(10, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(11, 'g');
			this.ch_KeyState(11, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(13, 'a');
			this.ch_KeyState(13, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(14, 'u');
			this.ch_KeyState(14, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(16, 'i');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(17, 'u');
			this.ch_KeyState(17, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(20, 'i');
			this.ch_KeyState(20, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(22, 'e');
			this.ch_KeyState(22, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(23, 'n');
			this.ch_KeyState(23, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(24, 'g');
			this.ch_KeyState(24, KeyCh.KEY_STATE_CANDIDATE2);

			break;
		case 'f':
			this.ch_KeyID(1, 'i');
			this.ch_KeyState(1, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(7, 'e');
			this.ch_KeyState(7, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(8, 'n');
			this.ch_KeyState(8, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(9, 'g');
			this.ch_KeyState(9, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(14, 'a');
			this.ch_KeyState(14, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(19, 'u');
			this.ch_KeyState(19, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(20, 'o');
			this.ch_KeyState(20, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case 'g':
			this.ch_KeyID(2, 'n');
			this.ch_KeyState(2, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(8, 'i');
			this.ch_KeyState(8, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(9, 'u');
			this.ch_KeyState(9, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(10, 'o');
			this.ch_KeyState(10, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(11, 'n');
			this.ch_KeyState(11, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(12, 'g');
			this.ch_KeyState(12, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(13, 'g');
			this.ch_KeyState(13, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(14, 'n');
			this.ch_KeyState(14, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(15, 'a');
			this.ch_KeyState(15, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(17, 'e');
			this.ch_KeyState(17, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(18, 'i');
			this.ch_KeyState(18, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(21, 'o');
			this.ch_KeyState(21, KeyCh.KEY_STATE_CANDIDATE2);
			break;

		case 'h':
			this.ch_KeyID(4, 'g');
			this.ch_KeyState(4, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(5, 'n');
			this.ch_KeyState(5, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(10, 'i');
			this.ch_KeyState(10, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(11, 'u');
			this.ch_KeyState(11, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(12, 'o');
			this.ch_KeyState(12, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(16, 'o');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(17, 'a');
			this.ch_KeyState(17, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(23, 'n');
			this.ch_KeyState(23, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(24, 'e');
			this.ch_KeyState(24, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(25, 'i');
			this.ch_KeyState(25, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(29, 'g');
			this.ch_KeyState(29, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case 'j':
			this.ch_KeyID(11, 'e');
			this.ch_KeyState(11, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(12, 'n');
			this.ch_KeyState(12, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(15, 'g');
			this.ch_KeyState(15, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(16, 'n');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(17, 'a');
			this.ch_KeyState(17, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(18, 'u');
			this.ch_KeyState(18, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(23, 'o');
			this.ch_KeyState(23, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(24, 'i');
			this.ch_KeyState(24, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(29, 'g');
			this.ch_KeyState(29, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(30, 'n');
			this.ch_KeyState(30, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(31, 'e');
			this.ch_KeyState(31, KeyCh.KEY_STATE_CANDIDATE2);
			break;

		case 'k':
			this.ch_KeyID(14, 'o');
			this.ch_KeyState(14, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(15, 'i');
			this.ch_KeyState(15, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(16, 'g');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(19, 'g');
			this.ch_KeyState(19, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(20, 'n');
			this.ch_KeyState(20, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(21, 'a');
			this.ch_KeyState(21, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(22, 'u');
			this.ch_KeyState(22, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(23, 'n');
			this.ch_KeyState(23, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(26, 'i');
			this.ch_KeyState(26, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(27, 'e');
			this.ch_KeyState(27, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(29, 'o');
			this.ch_KeyState(29, KeyCh.KEY_STATE_CANDIDATE2);
			break;

		case 'l':
			this.ch_KeyID(3, 'g');
			this.ch_KeyState(3, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(4, 'n');
			this.ch_KeyState(4, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(9, 'e');
			this.ch_KeyState(9, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(10, 'u');
			this.ch_KeyState(10, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(11, 'o');
			this.ch_KeyState(11, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(15, 'o');
			this.ch_KeyState(15, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(16, 'a');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(18, 'v');
			this.ch_KeyState(18, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(21, 'g');
			this.ch_KeyState(21, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(22, 'n');
			this.ch_KeyState(22, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(23, 'i');
			this.ch_KeyState(23, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(24, 'e');
			this.ch_KeyState(24, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(29, 'u');
			this.ch_KeyState(29, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(30, 'n');
			this.ch_KeyState(30, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(31, 'g');
			this.ch_KeyState(31, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case 'm':
			this.ch_KeyID(10, 'u');
			this.ch_KeyState(10, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(11, 'g');
			this.ch_KeyState(11, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(12, 'n');
			this.ch_KeyState(12, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(15, 'g');
			this.ch_KeyState(15, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(16, 'n');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(17, 'i');
			this.ch_KeyState(17, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(18, 'a');
			this.ch_KeyState(18, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(23, 'e');
			this.ch_KeyState(23, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(25, 'o');
			this.ch_KeyState(25, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(31, 'u');
			this.ch_KeyState(31, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case 'n':
			this.ch_KeyID(10, 'o');
			this.ch_KeyState(10, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(11, 'u');
			this.ch_KeyState(11, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(14, 'g');
			this.ch_KeyState(14, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(15, 'n');
			this.ch_KeyState(15, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(16, 'a');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(17, 'i');
			this.ch_KeyState(17, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(18, 'n');
			this.ch_KeyState(18, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(21, 'e');
			this.ch_KeyState(21, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(22, 'u');
			this.ch_KeyState(22, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(24, 'e');
			this.ch_KeyState(24, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(25, 'g');
			this.ch_KeyState(25, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(27, 'g');
			this.ch_KeyState(27, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(28, 'n');
			this.ch_KeyState(28, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(29, 'o');
			this.ch_KeyState(29, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(30, 'v');
			this.ch_KeyState(30, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case '\'':
			this.ch_KeyID(4, 'u');
			this.ch_KeyState(4, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(5, 'o');
			this.ch_KeyState(5, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(9, 'g');
			this.ch_KeyState(9, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(10, 'n');
			this.ch_KeyState(10, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(11, 'a');
			this.ch_KeyState(11, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(17, 'i');
			this.ch_KeyState(17, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(18, 'e');
			this.ch_KeyState(18, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(24, 'n');
			this.ch_KeyState(24, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(25, 'r');
			this.ch_KeyState(25, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case 'p':
			this.ch_KeyID(2, 'g');
			this.ch_KeyState(2, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(3, 'n');
			this.ch_KeyState(3, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(4, 'e');
			this.ch_KeyState(4, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(5, 'u');
			this.ch_KeyState(5, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(9, 'a');
			this.ch_KeyState(9, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(10, 'i');
			this.ch_KeyState(10, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(15, 'g');
			this.ch_KeyState(15, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(16, 'n');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(17, 'a');
			this.ch_KeyState(17, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(18, 'o');
			this.ch_KeyState(18, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(24, 'u');
			this.ch_KeyState(24, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case 'q':
			this.ch_KeyID(0, 'e');
			this.ch_KeyState(0, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(1, 'n');
			this.ch_KeyState(1, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(2, 'g');
			this.ch_KeyState(2, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(7, 'i');
			this.ch_KeyState(7, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(8, 'o');
			this.ch_KeyState(8, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(13, 'u');
			this.ch_KeyState(13, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(14, 'a');
			this.ch_KeyState(14, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(15, 'n');
			this.ch_KeyState(15, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(16, 'g');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(19, 'n');
			this.ch_KeyState(19, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(20, 'e');
			this.ch_KeyState(20, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case 'r':
			this.ch_KeyID(2, 'i');
			this.ch_KeyState(2, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(7, 'e');
			this.ch_KeyState(7, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(9, 'u');
			this.ch_KeyState(9, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(10, 'a');
			this.ch_KeyState(10, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(13, 'n');
			this.ch_KeyState(13, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(14, 'a');
			this.ch_KeyState(14, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(15, 'o');
			this.ch_KeyState(15, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(16, 'n');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(17, 'g');
			this.ch_KeyState(17, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(19, 'g');
			this.ch_KeyState(19, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case 's':
			this.ch_KeyID(6, 'o');
			this.ch_KeyState(6, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(7, 'i');
			this.ch_KeyState(7, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(8, 'o');
			this.ch_KeyState(8, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(9, 'n');
			this.ch_KeyState(9, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(10, 'g');
			this.ch_KeyState(10, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(13, 'a');
			this.ch_KeyState(13, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(15, 'u');
			this.ch_KeyState(15, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(16, 'i');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(19, 'n');
			this.ch_KeyState(19, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(20, 'e');
			this.ch_KeyState(20, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(21, 'h');
			this.ch_KeyState(21, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(22, 'a');
			this.ch_KeyState(22, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(23, 'n');
			this.ch_KeyState(23, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(24, 'g');
			this.ch_KeyState(24, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(26, 'g');
			this.ch_KeyState(26, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(27, 'i');
			this.ch_KeyState(27, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(28, 'o');
			this.ch_KeyState(28, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(29, 'u');
			this.ch_KeyState(29, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case 't':
			this.ch_KeyID(1, 'i');
			this.ch_KeyState(1, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(2, 'e');
			this.ch_KeyState(2, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(3, 'n');
			this.ch_KeyState(3, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(4, 'g');
			this.ch_KeyState(4, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(7, 'o');
			this.ch_KeyState(7, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(8, 'a');
			this.ch_KeyState(8, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(10, 'o');
			this.ch_KeyState(10, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(13, 'g');
			this.ch_KeyState(13, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(14, 'n');
			this.ch_KeyState(14, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(15, 'i');
			this.ch_KeyState(15, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(16, 'u');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(17, 'a');
			this.ch_KeyState(17, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(21, 'e');
			this.ch_KeyState(21, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(23, 'n');
			this.ch_KeyState(23, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case 'w':
			this.ch_KeyID(6, 'o');
			this.ch_KeyState(6, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(8, 'e');
			this.ch_KeyState(8, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(9, 'i');
			this.ch_KeyState(9, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(13, 'u');
			this.ch_KeyState(13, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(14, 'a');
			this.ch_KeyState(14, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(15, 'n');
			this.ch_KeyState(15, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(16, 'g');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(20, 'i');
			this.ch_KeyState(20, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case 'x':
			this.ch_KeyID(6, 'n');
			this.ch_KeyState(6, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(7, 'a');
			this.ch_KeyState(7, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(13, 'u');
			this.ch_KeyState(13, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(14, 'e');
			this.ch_KeyState(14, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(20, 'i');
			this.ch_KeyState(20, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(21, 'a');
			this.ch_KeyState(21, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(22, 'o');
			this.ch_KeyState(22, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(26, 'o');
			this.ch_KeyState(26, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(27, 'n');
			this.ch_KeyState(27, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(28, 'g');
			this.ch_KeyState(28, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case 'y':
			this.ch_KeyID(4, 'e');
			this.ch_KeyState(4, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(9, 'i');
			this.ch_KeyState(9, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(11, 'u');
			this.ch_KeyState(11, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(12, 'a');
			this.ch_KeyState(12, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(14, 'g');
			this.ch_KeyState(14, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(15, 'n');
			this.ch_KeyState(15, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(16, 'a');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(17, 'o');
			this.ch_KeyState(17, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(18, 'n');
			this.ch_KeyState(18, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(25, 'g');
			this.ch_KeyState(25, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		case 'z':
			this.ch_KeyID(2, 'u');
			this.ch_KeyState(2, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(7, 'g');
			this.ch_KeyState(7, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(8, 'i');
			this.ch_KeyState(8, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(9, 'o');
			this.ch_KeyState(9, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(10, 'n');
			this.ch_KeyState(10, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(11, 'g');
			this.ch_KeyState(11, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(13, 'n');
			this.ch_KeyState(13, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(14, 'e');
			this.ch_KeyState(14, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(15, 'h');
			this.ch_KeyState(15, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(16, 'a');
			this.ch_KeyState(16, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(20, 'a');
			this.ch_KeyState(20, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(22, 'u');
			this.ch_KeyState(22, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(23, 'i');
			this.ch_KeyState(23, KeyCh.KEY_STATE_CANDIDATE2);

			this.ch_KeyID(26, 'o');
			this.ch_KeyState(26, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(27, 'i');
			this.ch_KeyState(27, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(28, 'o');
			this.ch_KeyState(28, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(29, 'n');
			this.ch_KeyState(29, KeyCh.KEY_STATE_CANDIDATE2);
			this.ch_KeyID(30, 'g');
			this.ch_KeyState(30, KeyCh.KEY_STATE_CANDIDATE2);
			break;
		}
	}

	public void openTips() {
		this.mTipsFlag = true;
		view.setDrawingCacheEnabled(true);
	}

	public void closeTips() {
		this.mTipsFlag = false;
		view.setDrawingCacheEnabled(false);
	}

	private void commitPinyin(CharSequence pinyin) {
		view.commitText(pinyin);
	}

	private void commitChar(int code) {
		view.commitChar(code);
	}
}
