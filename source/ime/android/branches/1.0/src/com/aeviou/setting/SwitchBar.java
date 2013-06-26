//package com.aeviou.setting;
//
//import android.content.Intent;
//import android.graphics.Canvas;
//
//import com.aeviou.keyboard.AbstractKeyboard;
//import com.aeviou.keyboard.KeyboardFactory;
//import com.aeviou.utils.AeviouConstants;
//import com.aeviou.utils.BitmapUtils;
//
///**
// * @author Rex
// * 
// * SwithBar is located on the top-most part of the input method interface. It is used for 
// * selecting the characters when sliding, or choosing input mode (ch, eng, num) or toggle
// * on setting view when the user is not writing stuff.
// *
// */
//public class SwitchBar {
//
//	private static final int OFFSET_CH = 0;
//	private static final int OFFSET_EN = 1;
//	private static final int OFFSET_NUM = 2;
//	private static final int OFFSET_SETTING = 3;
//	private static final int OFFSET_HYBRID = 4;
//	private int keyboardMode;
//	private int margin;
//	private boolean inHybridMode;
//	/**
//	 * Background images are different for Chinese input and eng/num input. 
//	 */
//	private int backgroundBitmap;
//	
//	/**
//	 * Load background image mode when initiating this interface
//	 */
//	public SwitchBar(int keyboardMode){
//		backgroundBitmap = AeviouConstants.BMP_SWITCH_BACKGROUND;
//		this.keyboardMode = keyboardMode;
//		if (this.keyboardMode == KeyboardFactory.KEYBOARD_MODE_FULL){
//			margin = AeviouConstants.NORMAL_IME_HEXKEY_WIDTH * 3;
//		}else{
//			margin = AeviouConstants.NORMAL_IME_HEXKEY_WIDTH
//					+ AeviouConstants.NORMAL_IME_HEXKEY_WIDTH / 2;
//		}
//		inHybridMode = false;
//	}
//
//	/**
//	 * @param canvas
//	 * passed in when called to draw the icons for input mode switching 
//	 */
//	public void draw(Canvas canvas) {
//		if (inHybridMode) return;
//		int backgroundCount = this.keyboardMode == KeyboardFactory.KEYBOARD_MODE_FULL ? 5 : 4;
//		for (int i = 0; i < backgroundCount; i++) {
//			BitmapUtils.drawBitmap(canvas, null, backgroundBitmap,
//					AeviouConstants.NORMAL_IME_HEXKEY_WIDTH * i + margin, 25,
//					AeviouConstants.NORMAL_IME_HEXKEY_WIDTH,
//					AeviouConstants.NORMAL_IME_HEXKEY_WIDTH - 9);
//		}
//		BitmapUtils.drawBitmap(canvas, null, AeviouConstants.BMP_SWITCH_CH,
//				AeviouConstants.NORMAL_IME_HEXKEY_WIDTH * 0 + margin, 2,
//				AeviouConstants.NORMAL_IME_HEXKEY_WIDTH,
//				AeviouConstants.NORMAL_IME_HEXKEY_WIDTH);
//		BitmapUtils.drawBitmap(canvas, null, AeviouConstants.BMP_SWITCH_EN,
//				AeviouConstants.NORMAL_IME_HEXKEY_WIDTH * 1 + margin, 2,
//				AeviouConstants.NORMAL_IME_HEXKEY_WIDTH,
//				AeviouConstants.NORMAL_IME_HEXKEY_WIDTH);
//		BitmapUtils.drawBitmap(canvas, null, AeviouConstants.BMP_SWITCH_NUM,
//				AeviouConstants.NORMAL_IME_HEXKEY_WIDTH * 2 + margin, 2,
//				AeviouConstants.NORMAL_IME_HEXKEY_WIDTH,
//				AeviouConstants.NORMAL_IME_HEXKEY_WIDTH);
//		BitmapUtils.drawBitmap(canvas, null, AeviouConstants.BMP_SWITCH_SETTING,
//				AeviouConstants.NORMAL_IME_HEXKEY_WIDTH * 3 + margin, 2,
//				AeviouConstants.NORMAL_IME_HEXKEY_WIDTH,
//				AeviouConstants.NORMAL_IME_HEXKEY_WIDTH);
//		if (this.keyboardMode == KeyboardFactory.KEYBOARD_MODE_FULL){
//			BitmapUtils.drawBitmap(canvas, null, AeviouConstants.BMP_SWITCH_HYBRID,
//					AeviouConstants.NORMAL_IME_HEXKEY_WIDTH * 4 + margin, 2,
//					AeviouConstants.NORMAL_IME_HEXKEY_WIDTH,
//					AeviouConstants.NORMAL_IME_HEXKEY_WIDTH);
//		}
//	}
//
//	/**
//	 * Do NOTHING, handing over to onRelease
//	 * @param x
//	 * @param y
//	 */
//	public void onTouch(int x, int y) {
//
//	}
//
//	/**
//	 * Do NOTHING, handing over to onRelease
//	 * @param x
//	 * @param y
//	 */
//	public void onMove(int x, int y) {
//
//	}
//	
//	public void exitHybirdMode(){
//		inHybridMode = false;
//	}
//
//	/**
//	 * When releasing, the final position of the finger is noted down and acted upon 
//	 * accordingly. Either change the input mode or start up the setting view.
//	 * @param x
//	 * horizontal position
//	 * @param y
//	 * vertical position
//	 */
//	public void onRelease(int x, int y) {
//		if (inHybridMode) return;
//		// x offset starts with the first half of the button size chopped off.
//		int offsetX = (x - margin) / AeviouConstants.NORMAL_IME_HEXKEY_WIDTH;
//		
//		int offsetY = y / AeviouConstants.NORMAL_IME_HEXKEY_HEIGHT;
//
//		// return if y offset indicates a release outside switchbar scope
//		// offsetY should be 0 if y is low enough to fit in the height of one key
//		if (offsetY > 0) {
//			return;
//		}
//
//		switch (offsetX) {
//		case OFFSET_CH:
//			if (AeviouConstants.keyboardView.getCurrentKeyboard() != AbstractKeyboard.KEYBOARD_AEVIOU){
//				AeviouConstants.keyboardView.switchKeyboard(AbstractKeyboard.KEYBOARD_AEVIOU);
//				backgroundBitmap = AeviouConstants.BMP_SWITCH_BACKGROUND;
//				AeviouConstants.needRedraw = true;
//			}
//			break;
//		case OFFSET_EN:
//			if (AeviouConstants.keyboardView.getCurrentKeyboard() != AbstractKeyboard.KEYBOARD_ENGLISH){
//				AeviouConstants.keyboardView.switchKeyboard(AbstractKeyboard.KEYBOARD_ENGLISH);
//				backgroundBitmap = AeviouConstants.BMP_SWITCH_SQUARE_BACKGROUND;
//				AeviouConstants.needRedraw = true;
//			}
//			break;
//		case OFFSET_NUM:
//			if (AeviouConstants.keyboardView.getCurrentKeyboard() != AbstractKeyboard.KEYBOARD_NUMBER){
//				AeviouConstants.keyboardView.switchKeyboard(AbstractKeyboard.KEYBOARD_NUMBER);
//				backgroundBitmap = AeviouConstants.BMP_SWITCH_SQUARE_BACKGROUND;
//				AeviouConstants.needRedraw = true;
//			}
//			break;
//		case OFFSET_SETTING:
//			Intent preferenceIntent = new Intent(
//					AeviouConstants.inputMethodService,
//					com.aeviou.setting.SettingView.class);
//			preferenceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			AeviouConstants.inputMethodService.startActivity(preferenceIntent);
//			break;
//		case OFFSET_HYBRID:
//			if (this.keyboardMode == KeyboardFactory.KEYBOARD_MODE_FULL){
//				AeviouConstants.keyboardView.switchKeyboard(AbstractKeyboard.KEYBOARD_AEVIOU_HYBRID);
//				this.inHybridMode = true;
//				AeviouConstants.needRedraw = true;
//			}
//			break;
//		default:
//			break;
//
//		}
//	}
//}
