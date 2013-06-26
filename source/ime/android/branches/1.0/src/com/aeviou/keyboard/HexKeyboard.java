package com.aeviou.keyboard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

import com.aeviou.candidate.CandidateBar;
import com.aeviou.key.AbstractKey;
import com.aeviou.key.AbstractKey.KEY_STATUS;
import com.aeviou.key.HexKey;
import com.aeviou.key.KeySet;
import com.aeviou.pinyin.PinyinContext;
import com.aeviou.utils.AeviouConstants;
import com.aeviou.utils.SoundUtils;
import com.aeviou.utils.Vector2F;
public class HexKeyboard extends AbstractKeyboard {
	public SlidePath path; 
	
	private HexKey keys[];
	KeySet keySet=null;
	
	private boolean occupiedViewBar;
	private boolean startAtLetter;
	private TipView tipView;
	private CandidateBar candidateBar;
	
	private Paint paint;

	public Point lastPoint;
	private HexKey lastKey;//for fill the middle point
	private HexKey cacheKey;//for fast find current move key
	private Vector2F sildeVector;
	private boolean capsLock;
	private boolean shiftStatus;
	private boolean upperStatus;
	private boolean hybridMode;


	public HexKeyboard(HexKey keys[],CandidateBar candidateBar) {
		path = new SlidePath();
		occupiedViewBar = false;
		
		this.keys = keys;
		keySet=new KeySet(keys);
		
		lastPoint = new Point();
		sildeVector = new Vector2F();
		paint = new Paint();
		hybridMode = false;
		capsLock = false;
		shiftStatus = false;
		tipView=new TipView(AeviouConstants.keyboardView.getContext(),this);
		this.candidateBar=candidateBar;
	}
	
	public CandidateBar getCandidateBar(){
		return candidateBar;
	}

	private void flipCapsLock(){
		capsLock = !capsLock;
		updateUpperStatus();
	}
	
	private void flipShiftStatus(){
		shiftStatus = !shiftStatus;
		updateUpperStatus();
	}
	
	private void setShiftStatus(boolean value){
		shiftStatus = value;
		updateUpperStatus();
	}
	
	
	private void updateUpperStatus(){
		boolean prevUpperStatus = upperStatus;
		upperStatus = shiftStatus ^ capsLock;
		if (upperStatus != prevUpperStatus){
			for (HexKey key : keys) {
				key.updateUpper(upperStatus);
			}
		}
	}
	
	public boolean getUpperStatus(){
		return upperStatus;
	}
	
	public boolean getShiftStatus(){
		return shiftStatus;
	}
	
	public boolean getCapsLock(){
		return capsLock;
	}
	
	@Override
	public void draw(Canvas canvas) {
		candidateBar.draw(canvas);
		
		paint.setTextSize(AeviouConstants.NORMAL_LETTER_HEIGHT);
		paint.setColor(Color.WHITE);

		path.draw(canvas, paint);
		
		for (HexKey key : keys) {
			key.draw(canvas, paint);
		}
		//draw over the original keyboard
		if(settingkeyBoard!=null)settingkeyBoard.draw(canvas, paint);
		
	}

	@Override
	public void onTouch(int x, int y) {
		if(candidateBar.inGeometry(x, y)){
			candidateBar.isOpen=true;
			candidateBar.onTouch(x, y);
			AeviouConstants.needRedraw = true;
		}else{
			HexKey key = getKeyByPoint(x, y);
			lastPoint.set(x,y);
			if (key != null) {
				if(key.getType()==AbstractKey.TYPE_KEY_SWITCH){
					//setting key
					settingkeyBoard.onTouch(x, y);
				}else{
					if(AeviouConstants.enableTipView){
						tipView.setVisibility(View.VISIBLE);
					}
					switch(key.getType()) {
						case HexKey.TYPE_KEY_LETTER:
							startAtLetter = true;
							break;
						default:
							startAtLetter = false;
					}
				
					path.StartInKey(key, keys, getUpperStatus());
					tipView.updatePostion(lastPoint);
					tipView.commitUpdate();
				}
				AeviouConstants.needRedraw = true;
				lastKey=key;
				
			}
		}
		
		
		SoundUtils.playSoundAndVibration();
	}

	@Override
	public void onMove(int x, int y) {
		AeviouConstants.needRedraw=true;
		lastPoint.set(x,y);
		
		if (candidateBar.isOpen){
			candidateBar.onMove(x, y);
		}else if(settingkeyBoard.isOpen){
			settingkeyBoard.onMove(x,y);
		}else{
			fillMiddlePoint(x, y);
			
			HexKey key = getKeyByPoint(x, y);
			if (key != null){
				if(path.MoveInKey(key, keys)){
					if(lastKey!=key){
						SoundUtils.playSoundAndVibration();
						lastKey=key;
					}
				}
			}
			tipView.updatePostion(lastPoint);
			tipView.commitUpdate();
		}

		
	}
	
	@Override
	public void onRelease(int x, int y) {
		occupiedViewBar = false;
		AeviouConstants.needRedraw = true;
		
		if (candidateBar.isOpen){
			candidateBar.onRelease(x, y);
			candidateBar.isOpen=false;
		}else if(settingkeyBoard.isOpen){
			settingkeyBoard.onRelease(x,y);
		}else{
			PinyinContext pinyinContext=candidateBar.pinyinContext;
			if(startAtLetter == false) {
				HexKey key = path.getLast();
				if (key != null) {//deal symbol,enter,backspace
					int type=key.getType();
					if (key.status == KEY_STATUS.selected)
						switch (type) {
						case HexKey.TYPE_KEY_BACKSPACE:
							InputLogic.HexKeyBackspacePressed(pinyinContext);
							path.EndInKey(keys);
							setShiftStatus(false);
							break;
						case HexKey.TYPE_KEY_ENTER:
							InputLogic.HexKeyEnterPressed(pinyinContext);
							path.EndInKey(keys);
							setShiftStatus(false);
							break;
						case HexKey.TYPE_KEY_SPACE:
							InputLogic.HexKeySpacePressed(pinyinContext);
							path.EndInKey(keys);
							setShiftStatus(false);
							break;
						case HexKey.TYPE_KEY_SYMBOL:
							InputLogic.HexKeySymbolPressed(pinyinContext,
										String.valueOf(key.getName()), !hybridMode);
							path.EndInKey(keys);
							setShiftStatus(false);
							break;
						case HexKey.TYPE_KEY_HYBRID:
							AeviouConstants.keyboardView.exitHybridMode();
							path.EndInKey(keys);
							setShiftStatus(false);
							break;
						case HexKey.TYPE_KEY_CAPS:
							flipCapsLock();
							path.EndInKey(keys);
							setShiftStatus(false);
							break;
						case HexKey.TYPE_KEY_SHIFT:
							flipShiftStatus();
							path.EndInKey(keys);
							break;
						default:
							path.EndInKey(keys);
							setShiftStatus(false);
							break;
						}
				}
			}else {
				fillMiddlePoint(x, y);
				String pinyin = path.EndInKey(keys);
				if (upperStatus){
					InputLogic.HexKeySymbolPressed(pinyinContext, pinyin, false);
					setShiftStatus(false);
					
				}else if (hybridMode && pinyin != null && (pinyin.length() == 1
						&& pinyin.charAt(0) != 'm')){
					InputLogic.HexKeyLetterPressed(pinyinContext, pinyin);
					
				}else{
					if (hybridMode){
						if (pinyin != null && ((pinyin.length() >= 2 && pinyin.charAt(0) == 'v')
								|| (pinyin.length() == 1 && pinyin.charAt(0) == 'm'))){
							if (pinyin.charAt(0) == 'v'){
								InputLogic.HexKeyLetterPressed(pinyinContext, String.valueOf(pinyin.charAt(1)));
							}else{
								InputLogic.HexKeyLetterPressed(pinyinContext, String.valueOf(pinyin.charAt(0)));
							}
							InputLogic.HexKeyPinyinPressed(pinyinContext, pinyin);
							AeviouConstants.candidateBar.setDeletePrevious();
						}else{
							InputLogic.HexKeyPinyinPressed(pinyinContext, pinyin);
						}
					}else{
						InputLogic.HexKeyPinyinPressed(pinyinContext, pinyin);
					}
				}
				setShiftStatus(false);
			}
		}
		tipView.updatePostion(null);
		tipView.commitUpdate();
	}
	
	void fillMiddlePoint(int x, int y){
		HexKey key;
		int middleX, middleY;
		if (lastKey == null) return;
		sildeVector.set(x - lastKey.centerX, y - lastKey.centerY);
		if(sildeVector.isZero() == false){
			sildeVector.normalized();
			while(Math.abs(x - lastKey.centerX) > AeviouConstants.NORMAL_IME_HEXKEY_WIDTH ||
					Math.abs(y - lastKey.centerY) > AeviouConstants.NORMAL_IME_HEXKEY_HEIGHT){
				middleX = (int)((lastKey.centerX + sildeVector.x * AeviouConstants.NORMAL_IME_HEXKEY_WIDTH));
				middleY = (int)((lastKey.centerY + sildeVector.y * AeviouConstants.NORMAL_IME_HEXKEY_WIDTH));
				key = getKeyByPoint(middleX, middleY);
				if(key == null){
					break;
				}else if(key == path.getLast()){
					break;
				}
				if(path.MoveInKey(key, keys))
					lastKey = key;
				else
					break;
			}
		}
	}

	protected HexKey getKeyByPoint(int x, int y) {
		if (cacheKey != null){
			if (cacheKey.inGeometry(x, y)){
				return cacheKey;
			}
		}
		return cacheKey = (HexKey) keySet.getKeyByPoint(x,y);
	}
	

	@Override
	public void hibernate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wakeup() {
		
	}

	@Override
	public boolean occupyBarView() {
		return occupiedViewBar;
	}

	@Override
	public void reset() {
		tipView.reset();
		candidateBar.reset();
	}

	@Override
	public void close() {
		tipView.closeWindow();
		candidateBar.close();
	}
	
	public void enterHybridMode(){
		hybridMode = true;
		showFirstRow();
	}
	
	public void exitHybridMode(){
		hideFirstRow();
		hybridMode = false;
	}

	public void showFirstRow() {
		if (!hybridMode) return;
		for (int i = 0; i < 10; i++){
			keys[i].status = KEY_STATUS.normal;
			keys[i].type = HexKey.TYPE_KEY_SYMBOL;
			keys[i].name = (char) (i + '0');
		}
		keys[10].status = KEY_STATUS.normal;
		keys[10].type = HexKey.TYPE_KEY_HYBRID;
		keys[10].name = AeviouConstants.KEYCODE_HYBRID;
	}

	public void hideFirstRow(){
		if (!hybridMode) return;
		for (int i = 0; i < 11; i++){
			keys[i].status = KEY_STATUS.normal;
			keys[i].type = HexKey.TYPE_KEY_HIDE;
			keys[i].name = AeviouConstants.KEYCODE_NULL;
		}
	}
}
