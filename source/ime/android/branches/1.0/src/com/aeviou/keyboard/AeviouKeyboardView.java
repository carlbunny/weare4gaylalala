package com.aeviou.keyboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.aeviou.R;
import com.aeviou.setting.SettingView;
import com.aeviou.utils.AeviouConstants;

public class AeviouKeyboardView extends KeyboardView {
	public static final int RECT_SWITCH_BAR = 0;
	public static final int RECT_CANDIDATE_BAR = 1;
	public static final int RECT_KEYBOARD = 2;

	private AbstractKeyboard currentKeyboard;
	private HexKeyboard aeviouKeyboard;
	private HexEnglishKeyboard englishKeyboard;
	private NumberKeyboard numberKeyboard;
	private SymbolKeyboard symbolKeyboard;

//	private SwitchBar switchBar;

	private PaintFlagsDrawFilter drawFilter;
	public Matrix canvasMatrix, canvasMatrixInverse;
//	public Matrix finalMatrix;
	

	public Canvas mCanvas;
	public Bitmap mBuffer;
	public int activitedRect;
	public boolean hybridMode = false;
	private boolean initialized = false;

	

	public AeviouKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);

		AeviouConstants.keyboardView = this;
		this.setDrawingCacheEnabled(true);
		//pinyinContext = new PinyinContext();
//		finalMatrix=new Matrix(canvasMatrix);
		canvasMatrix = new Matrix();
		canvasMatrixInverse = new Matrix();
		
		SettingView.loadPreferences();

		activitedRect = RECT_KEYBOARD;
		drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);


		
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) this.getContext().getSystemService(
				Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		widthMeasureSpec = dm.widthPixels;
		heightMeasureSpec = dm.heightPixels;

		AeviouConstants.SCREEN_WIDTH = widthMeasureSpec;
		AeviouConstants.SCREEN_HEIGHT = heightMeasureSpec;
//		if (widthMeasureSpec < heightMeasureSpec) {
			/* Vertical Mode */
			if (!initialized) {
//				if (AeviouConstants.verticalFull) {
//					aeviouKeyboard = (HexKeyboard) KeyboardFactory
//							.getInstance().getKeyboardEntity(
//						 			R.raw.aeviou_qwert,
//									KeyboardFactory.KEYBOARD_MODE_FULL);
////					englishKeyboard = (SquareKeyboard) KeyboardFactory
////							.getInstance().getKeyboardEntity(
////									R.raw.english_qwert,
////									KeyboardFactory.KEYBOARD_MODE_FULL);
//					englishKeyboard = KeyboardFactory.getInstance().createSimpleKeyboard();
//					numberKeyboard = (NumberKeyboard) KeyboardFactory
//							.getInstance().getKeyboardEntity(
//									R.raw.number_9grid,
//									KeyboardFactory.KEYBOARD_MODE_FULL);
//					
////					switchBar = new SwitchBar(
////							KeyboardFactory.KEYBOARD_MODE_FULL);
//					AeviouConstants.NORMAL_IME_VIEW_WIDTH = AeviouConstants.NORMAL_IME_VIEW_WIDTH_FULL;
//				} else {
					aeviouKeyboard = (HexKeyboard) KeyboardFactory
							.getInstance().getKeyboardEntity(
									R.raw.aeviou_simple,
									KeyboardFactory.KEYBOARD_MODE_SIMPLE);
//					englishKeyboard = (SquareKeyboard) KeyboardFactory
//							.getInstance().getKeyboardEntity(
//									R.raw.english_qwert,
//									KeyboardFactory.KEYBOARD_MODE_SIMPLE);
					englishKeyboard=KeyboardFactory.getInstance().createSimpleKeyboard();
					symbolKeyboard=KeyboardFactory.getInstance().createSymbolKeyboard();
					numberKeyboard = (NumberKeyboard) KeyboardFactory
							.getInstance().getKeyboardEntity(
									R.raw.number_9grid,
									KeyboardFactory.KEYBOARD_MODE_SIMPLE);
					
//					switchBar = new SwitchBar(
//							KeyboardFactory.KEYBOARD_MODE_SIMPLE);
					AeviouConstants.NORMAL_IME_VIEW_WIDTH = AeviouConstants.NORMAL_IME_VIEW_WIDTH_SIMPLE;
//				}
				currentKeyboard = aeviouKeyboard;
				currentKeyboard.wakeup();
				AeviouConstants.candidateBar = aeviouKeyboard.getCandidateBar();
			}
			AeviouConstants.IME_VIEW_WIDTH = widthMeasureSpec;
			AeviouConstants.IME_VIEW_HSCALE = (float) AeviouConstants.IME_VIEW_WIDTH
					/ AeviouConstants.NORMAL_IME_VIEW_WIDTH;
			AeviouConstants.IME_VIEW_VSCALE = AeviouConstants.IME_VIEW_HSCALE;
			AeviouConstants.IME_VIEW_HEIGHT = (int) AeviouConstants.IME_VIEW_HSCALE
					* heightMeasureSpec;
			AeviouConstants.IME_VIEW_LEFT_MARGIN = 0;
			this.setMeasuredDimension(
					(int) (AeviouConstants.IME_VIEW_HSCALE * AeviouConstants.NORMAL_IME_VIEW_WIDTH),
					(int) (AeviouConstants.IME_VIEW_VSCALE * AeviouConstants.NORMAL_IME_VIEW_HEIGHT));
//		} else {
//			/* Horizontal Mode */
//			if (!initialized) {
//				if (AeviouConstants.horizontalFull) {
//					aeviouKeyboard = (HexKeyboard) KeyboardFactory
//							.getInstance().getKeyboardEntity(
//									R.raw.aeviou_qwert,
//									KeyboardFactory.KEYBOARD_MODE_FULL);
////					englishKeyboard = (SquareKeyboard) KeyboardFactory
////							.getInstance().getKeyboardEntity(
////									R.raw.english_qwert,
////									KeyboardFactory.KEYBOARD_MODE_FULL);
//					englishKeyboard = KeyboardFactory.getInstance().createSimpleKeyboard();
//					numberKeyboard = (NumberKeyboard) KeyboardFactory
//							.getInstance().getKeyboardEntity(
//									R.raw.number_qwert,
//									KeyboardFactory.KEYBOARD_MODE_FULL);
//					
////					switchBar = new SwitchBar(
////							KeyboardFactory.KEYBOARD_MODE_FULL);
//					AeviouConstants.NORMAL_IME_VIEW_WIDTH = AeviouConstants.NORMAL_IME_VIEW_WIDTH_FULL;
//					AeviouConstants.IME_VIEW_LEFT_MARGIN = 0;
//					AeviouConstants.IME_VIEW_WIDTH = widthMeasureSpec;
//				} else {
//					aeviouKeyboard = (HexKeyboard) KeyboardFactory
//							.getInstance().getKeyboardEntity(
//									R.raw.aeviou_simple,
//									KeyboardFactory.KEYBOARD_MODE_SIMPLE);
////					englishKeyboard = (SquareKeyboard) KeyboardFactory
////							.getInstance().getKeyboardEntity(
////									R.raw.english_qwert,
////									KeyboardFactory.KEYBOARD_MODE_SIMPLE);
//					englishKeyboard = KeyboardFactory.getInstance().createSimpleKeyboard();
//					numberKeyboard = (NumberKeyboard) KeyboardFactory
//							.getInstance().getKeyboardEntity(
//									R.raw.number_qwert,
//									KeyboardFactory.KEYBOARD_MODE_SIMPLE);
//					
////					switchBar = new SwitchBar(
////							KeyboardFactory.KEYBOARD_MODE_SIMPLE);
//					AeviouConstants.NORMAL_IME_VIEW_WIDTH = AeviouConstants.NORMAL_IME_VIEW_WIDTH_SIMPLE;
//					AeviouConstants.IME_VIEW_LEFT_MARGIN = (widthMeasureSpec - heightMeasureSpec) / 2;
//					AeviouConstants.IME_VIEW_WIDTH = heightMeasureSpec;
//				}
//				currentKeyboard = aeviouKeyboard;
//				currentKeyboard.wakeup();
//				AeviouConstants.candidateBar = aeviouKeyboard.getCandidateBar();
//			}

//			AeviouConstants.IME_VIEW_HSCALE = (float) AeviouConstants.IME_VIEW_WIDTH
//					/ AeviouConstants.NORMAL_IME_VIEW_WIDTH;
//			AeviouConstants.IME_VIEW_VSCALE = AeviouConstants.IME_VIEW_HSCALE;
//			AeviouConstants.IME_VIEW_HEIGHT = (int) AeviouConstants.IME_VIEW_HSCALE
//					* heightMeasureSpec;
//			this.setMeasuredDimension(
//					(widthMeasureSpec),
//					(int) (AeviouConstants.IME_VIEW_VSCALE * AeviouConstants.NORMAL_IME_VIEW_HEIGHT));
//		}

		canvasMatrix.reset();
		canvasMatrix.postScale(AeviouConstants.IME_VIEW_HSCALE,
				AeviouConstants.IME_VIEW_VSCALE);
		canvasMatrix.postTranslate(AeviouConstants.IME_VIEW_LEFT_MARGIN, 0);
//		finalMatrix.set(canvasMatrix);
		canvasMatrix.invert(canvasMatrixInverse);
//		AeviouConstants.inputMethodService.keyboardSensor.updateMatrix();
		initialized = true;
		this.reset();

	}

	@Override
	public void onDraw(Canvas canvas) {
		if (!initialized)
			return;
		if (mBuffer == null) {
			mBuffer = Bitmap.createBitmap(
					AeviouConstants.keyboardView.getWidth(),
					AeviouConstants.keyboardView.getWidth(),
					Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBuffer);
		}
		mBuffer.eraseColor(Color.rgb(50, 50, 50));
//		Matrix finalMatrix=MatrixUtil.mul(rotatesMatrix,canvasMatrix);
//		finalMatrix.postScale(rotateScale, 1);
		mCanvas.setMatrix(canvasMatrix);
		mCanvas.setDrawFilter(drawFilter);

		currentKeyboard.draw(mCanvas);

		canvas.drawBitmap(mBuffer, 0, 0, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!initialized)
			return true;

		float[] point = new float[2];
		point[0] = event.getX();
		point[1] = event.getY();
		canvasMatrixInverse.mapPoints(point);

		int x = (int) point[0];
		int y = (int) point[1];

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
//			if (candidateReceive) {
//				candidateBar.onTouch(x, y);
//				activitedRect = RECT_CANDIDATE_BAR;
//			} else if (switchReceive) {
//				switchBar.onTouch(x, y);
//				activitedRect = RECT_SWITCH_BAR;
//			} else {
				currentKeyboard.onTouch(x, y);
				activitedRect = RECT_KEYBOARD;
//			}
			break;
		case MotionEvent.ACTION_MOVE:
//			if (activitedRect == RECT_CANDIDATE_BAR) {
//				candidateBar.onMove(x, y);
//			} else if (activitedRect == RECT_SWITCH_BAR) {
//				switchBar.onMove(x, y);
//			} else {
				currentKeyboard.onMove(x, y);
//			}
			break;
		case MotionEvent.ACTION_UP:
//			if (activitedRect == RECT_CANDIDATE_BAR) {
//				candidateBar.onRelease(x, y);
//			} else if (activitedRect == RECT_SWITCH_BAR) {
//				switchBar.onRelease(x, y);
//			} else {
				currentKeyboard.onRelease(x, y);
//			}
			break;
		}

		if (AeviouConstants.needRedraw == true) {
			this.invalidate();
			AeviouConstants.needRedraw = false;
		}
		return true;
	}

	public void switchKeyboard(AbstractKeyboard.Type keyboard) {
		currentKeyboard.hibernate();
		switch (keyboard) {
		case Aeviou:
			if (currentKeyboard != aeviouKeyboard)
				currentKeyboard = aeviouKeyboard;
			break;
//		case AbstractKeyboard.KEYBOARD_AEVIOU_HYBRID:
//			if (currentKeyboard != aeviouKeyboard)
//				currentKeyboard = aeviouKeyboard;
//			hybridMode = true;
//			aeviouKeyboard.enterHybridMode();
//			break;
		case English:
			if (currentKeyboard != englishKeyboard)
				currentKeyboard = englishKeyboard;
			break;
		case Number:
			if (currentKeyboard != numberKeyboard)
				currentKeyboard = numberKeyboard;
			break;
		case Symbol:
			if (currentKeyboard != symbolKeyboard)
				currentKeyboard = symbolKeyboard;
			break;
		}
		currentKeyboard.wakeup();
	}

	public void updateHybridKeyboard(boolean show) {
		if (!hybridMode)
			return;
		if (show) {
			aeviouKeyboard.showFirstRow();
		} else {
			aeviouKeyboard.hideFirstRow();
		}
	}

	public void exitHybridMode() {
		aeviouKeyboard.exitHybridMode();
//		switchBar.exitHybirdMode();
		hybridMode = false;
		invalidate();
	}

	public AbstractKeyboard.Type getCurrentKeyboard() {
		if (currentKeyboard == aeviouKeyboard)
			return AbstractKeyboard.Type.Aeviou;
		if (currentKeyboard == englishKeyboard)
			return AbstractKeyboard.Type.English;
		if(currentKeyboard==numberKeyboard)
			return AbstractKeyboard.Type.Number;
		return AbstractKeyboard.Type.Symbol;
	}

	public void reset() {
		if (!initialized)
			return;
		aeviouKeyboard.reset();
		invalidate();
	}

	public void close() {
		if (!initialized)
			return;
		aeviouKeyboard.close();
	}

}
