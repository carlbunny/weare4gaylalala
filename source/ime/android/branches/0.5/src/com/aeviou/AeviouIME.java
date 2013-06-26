package com.aeviou;

import com.aeviou.pinyin.PinyinFile;
import com.aeviou.pinyin.PinyinContext;
import com.aeviou.R;
import com.aeviou.keyboard.AeviouKeyboardView;
import com.aeviou.keyboard.AeviouKeyboard;
import com.aeviou.keyboard.CandidateView;
import com.aeviou.keyboard.KeyCh;


import java.io.InputStream;
import java.io.FileOutputStream; 
import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;

import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.content.res.AssetManager ;

import java.util.List;

public class AeviouIME extends InputMethodService implements
		OnKeyboardActionListener {
	// 自定义InputType类型，不同的版本可能实现不一样
	public static final int TYPE_CLASS_STOCK = 0x0000000a;
	
	public static final byte IME_STATE_CH=0x01 ;
	public static final byte IME_STATE_EN=0x02 ;

	private byte mIMEState ;
	private AeviouKeyboardView mInputView;
	private CandidateView mCandidateView ;
	private PinyinContext mPinyinContext ;
	
	private AeviouKeyboard mSymbolsKeyboard;
	private AeviouKeyboard mSymbolsShiftedKeyboard;
	private AeviouKeyboard mQwertyKeyboard;
	
	private boolean mCapsLock;
	private long mLastShiftTime;
	public int mTempImeOption;
	
    private CompletionInfo[] mCompletions;
    private StringBuilder mComposing = new StringBuilder();
    private boolean mCompletionOn;
    private String mWordSeparators;
	@Override
	public void onCreate() {

		super.onCreate();
	}
	@Override
	public void onInitializeInterface() {
	}
	@Override
	public View onCreateInputView() {
		try {
			this.initCibiao();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		File dir=this.getFilesDir() ;
		String path=dir.getPath()+"/";
		PinyinFile.PINYIN_FILE_DIRECTORY=path ;
		mInputView = (AeviouKeyboardView) getLayoutInflater().inflate(R.layout.input,
				null);
		mInputView.setSevice(this);
		this.mIMEState=IME_STATE_CH ;
		mQwertyKeyboard = new AeviouKeyboard(this, R.xml.qwerty);
		mSymbolsKeyboard = new AeviouKeyboard(this, R.xml.symbols);
		mSymbolsShiftedKeyboard = new AeviouKeyboard(this, R.xml.symbols_shift);
		
		mInputView.setOnKeyboardActionListener(this);
		
		Display display=((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
		Environment.RESOLUTION_WIDTH=display.getWidth();
		Environment.RESOLUTION_HEIGHT=display.getHeight();
		int orientation=this.getResources().getConfiguration().orientation;
		if(orientation==Configuration.ORIENTATION_LANDSCAPE){
			System.out.println("horizontal");
		}else if(orientation==Configuration.ORIENTATION_PORTRAIT){
			System.out.println("vertical");
		}
		
		return mInputView;
	}

    @Override 
    public View onCreateCandidatesView() {
    	this.mCandidateView=new CandidateView(this) ;
    	mCandidateView.setService(this) ;
    	return mCandidateView ;
    }
	@Override
	public void onFinishInput() {
		super.onFinishInput();
		this.clearAll() ;
		if (mInputView != null) {
			mInputView.closing();

			
		}
	}

	public void onFinishInputView(boolean finishingInput){
		super.onFinishCandidatesView(finishingInput) ;
		this.clearAll() ;
	}
	public void onFinishCandidateView(boolean finishingInput){
		super.onFinishCandidatesView(finishingInput) ;
	}
	@Override
	public void onStartInputView(EditorInfo attribute, boolean restarting) {
		super.onStartInputView(attribute, restarting);
		mInputView.startKeyboardView();
		this.mPinyinContext=new PinyinContext() ;
		if(mCandidateView!=null)
			this.setCandidatesViewShown(false) ;
	}
	@Override
	public void onStartCandidatesView(EditorInfo attribute, boolean restarting) {
		super.onStartCandidatesView(attribute, restarting);
		
	}
	@Override
	public void onDisplayCompletions(CompletionInfo[] completions) {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (event.getRepeatCount() == 0 && mInputView != null) {
				if (mInputView.handleBack()) {
					return true;
				}
			}
			break;
		case KeyEvent.KEYCODE_DEL:
			onKey(Keyboard.KEYCODE_DELETE, null);
			break;

		case KeyEvent.KEYCODE_ENTER:
			// Let the underlying text editor always handle these.
			return false;

		default:
			// do nothing
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}

	private void updateShiftKeyState(EditorInfo attr) {
		if (attr != null && mInputView != null
				&& mQwertyKeyboard == mInputView.getKeyboard()) {
			int caps = 0;
			EditorInfo ei = getCurrentInputEditorInfo();
			InputConnection connection = getCurrentInputConnection();
			if (connection != null && ei != null && ei.inputType != EditorInfo.TYPE_NULL) {
				caps = connection.getCursorCapsMode(
						attr.inputType);
			}
			mInputView.setShifted(mCapsLock || caps != 0);
		}
	}

	private void keyDownUp(int keyEventCode) {
		InputConnection connection = getCurrentInputConnection();
		if (connection != null){
			connection.sendKeyEvent(
					new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
			connection.sendKeyEvent(
					new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
		}
	}

	public void onKey(int primaryCode, int[] keyCodes) {
		if(this.mIMEState!=IME_STATE_CH)
			this.mInputView.playSound(AeviouKeyboardView.KEYAUDIO);
		if(this.mIMEState==IME_STATE_CH){
			switch(primaryCode){
			case KeyCh.KEYCODE_EN :
				this.chToQwerty();
				return ;
			case KeyCh.KEYCODE_NUM:
				this.chToSymbols() ;
				return ;
			case KeyCh.KEYCODE_SET:
				this.mInputView.setProperty() ;
				return ;
			case KeyCh.KEYCODE_ENTER:
				if(this.mPinyinContext.getSentence().length()!=0){
					this.commiteSentence() ;
				}
				else{
					InputConnection connection = getCurrentInputConnection();
					if (connection != null){
						connection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER)) ;
					}
				}
				return ;
			default:
				break ;
			}
		}
		if (primaryCode == Keyboard.KEYCODE_DELETE) {
			handleBackspace();
		} else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
			handleShift();
		} else if (primaryCode == Keyboard.KEYCODE_CANCEL) {
			handleClose();
			return;
		} else if (primaryCode == AeviouKeyboardView.KEYCODE_OPTIONS) {
			showOptionsMenu();
		} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE
				&& mInputView != null) {
			Keyboard current = mInputView.getKeyboard();
			if (current == mSymbolsKeyboard
					|| current == mSymbolsShiftedKeyboard) {
				this.chToPinyin() ;
			} else if (current == mQwertyKeyboard) {
				this.chToPinyin() ;
			} else {
				current = mSymbolsKeyboard;
			}
			((AeviouKeyboard) current).setImeOptions(getResources(),
					mTempImeOption);
			mInputView.setKeyboard(current);
			if (current == mSymbolsKeyboard) {
				current.setShifted(false);
			}
		} else {
			handleCharacter(primaryCode, keyCodes);
		}
	}
	public void chToQwerty(){
		this.mIMEState=IME_STATE_EN ;
		this.mInputView.setKeyboard(this.mQwertyKeyboard) ;
		this.setCandidatesViewShown(false) ;
	}
	public void chToSymbols(){
		this.mIMEState=IME_STATE_EN ;
		this.mInputView.setKeyboard(this.mSymbolsKeyboard) ;
		this.setCandidatesViewShown(false) ;

	}
	public void chToPinyin(){
		this.mIMEState=IME_STATE_CH;
		this.mInputView.startKeyboardView() ;

	}
	public void onText(CharSequence text) {

		if(this.mIMEState==IME_STATE_CH){
			if(this.mPinyinContext.getSentence().length()>11){
				this.mInputView.playSound(AeviouKeyboardView.KEYWARNING) ;
				return ;
			}
			this.addPinyin(text.toString()) ;
		}else{
			InputConnection ic = getCurrentInputConnection();
			if (ic == null){
				return;
			}
			ic.beginBatchEdit();
			ic.commitText(text, 0);
			ic.endBatchEdit();
			updateShiftKeyState(getCurrentInputEditorInfo());
		}
	}

	private void handleBackspace() {
		if(this.mIMEState==IME_STATE_CH&&this.mPinyinContext.getSentence().length()!=0){
			if(this.mPinyinContext.getPinyin()==null){
				this.mPinyinContext.clearContext() ;
				keyDownUp(KeyEvent.KEYCODE_DEL);
				updateShiftKeyState(getCurrentInputEditorInfo());
			}else if(this.mPinyinContext.getPinyin().length()==0){
				this.mPinyinContext.clearContext() ;
				updateShiftKeyState(getCurrentInputEditorInfo());
			}else{
				this.removePinyin() ;
			}
			this.updateAll() ;
			return ;
		}else if(this.mIMEState==IME_STATE_CH){
			if(this.mPinyinContext.getPinyin()==null){
				this.mPinyinContext.clearContext() ;
			}else if(this.mPinyinContext.getPinyin().length()==0){
				this.mPinyinContext.clearContext() ;
			}
		}
		keyDownUp(KeyEvent.KEYCODE_DEL);
		updateShiftKeyState(getCurrentInputEditorInfo());
		this.updateAll() ;
	}

	private void handleShift() {
		if (mInputView == null) {
			return;
		}
		Keyboard currentKeyboard = mInputView.getKeyboard();
		if (mQwertyKeyboard == currentKeyboard) {
			checkToggleCapsLock();
			mInputView.setShifted(mCapsLock || !mInputView.isShifted());
		} else if (currentKeyboard == mSymbolsKeyboard) {
			mSymbolsKeyboard.setShifted(true);
			mInputView.setKeyboard(mSymbolsShiftedKeyboard);
			mSymbolsShiftedKeyboard.setShifted(true);
		} else if (currentKeyboard == mSymbolsShiftedKeyboard) {
			mSymbolsShiftedKeyboard.setShifted(false);
			mInputView.setKeyboard(mSymbolsKeyboard);
			mSymbolsKeyboard.setShifted(false);
		}
	}

	private boolean isAlphabet(int code) {
		if (Character.isLetter(code)) {
			return true;
		} else {
			return false;
		}
	}

	private void handleCharacter(int primaryCode, int[] keyCodes) {
		if (isInputViewShown()) {
			if (mInputView.isShifted()) {
				primaryCode = Character.toUpperCase(primaryCode);
			}
		}
		InputConnection connection = getCurrentInputConnection();
		if (connection != null){
			if (isAlphabet(primaryCode)) {
				String temp=String.valueOf((char)primaryCode) ;
				connection.commitText(temp, 0) ;
			} else {
				connection.commitText(String.valueOf((char) primaryCode), 1);
			}
		}
	}

	private void handleClose() {
		requestHideSelf(0);
		mInputView.closing();
	}

	private void checkToggleCapsLock() {
		long now = System.currentTimeMillis();
		if (mLastShiftTime + 800 > now) {
			mCapsLock = !mCapsLock;
			mLastShiftTime = 0;
		} else {
			mLastShiftTime = now;
		}
	}

	public void swipeRight() {
	}

	public void swipeLeft() {
		handleBackspace();
	}

	public void swipeDown() {
		handleClose();
	}

	public void swipeUp() {
	}

	public void onPress(int primaryCode) {
	}

	public void onRelease(int primaryCode) {
	}
	public void setIMEState(byte state){
		this.mIMEState=state ;
	}
	public final byte getIMEState() {
		return this.mIMEState ;
	}
	public final Resources getResource(){
		return this.getResources() ;
	}
	public PinyinContext getPinyinContext(){
		return this.mPinyinContext ;
	}
	private void addPinyin(String pinyin){
		this.mPinyinContext.addPinyin(pinyin) ;

		if(this.mPinyinContext.getSentence().length()!=0)
			this.setCandidatesViewShown(true) ;
		else
			this.setCandidatesViewShown(false) ;
		this.updateAll() ;
	}
	private void removePinyin(){
		if(this.mPinyinContext.getSentence().length()!=0)
			this.setCandidatesViewShown(true) ;
		else
			this.setCandidatesViewShown(false) ;
		this.mPinyinContext.removePinyin() ;
		this.updateAll() ;

	}
	public void correctSentence(int index){
		if(this.mPinyinContext.getSentence()==null)
			this.setCandidatesViewShown(false);
		if(this.mPinyinContext.getSentence().length()!=0)
			this.setCandidatesViewShown(true) ;
		else
			this.setCandidatesViewShown(false) ;

		this.mPinyinContext.correctWord(index) ;
		this.updateAll() ;
	}
	public void commiteSentence(){
		String sentence=this.mPinyinContext.getSentence() ;
		if(sentence.length()==0)
			sentence=this.mInputView.getChoosedCandidate() ;
		InputConnection connection = getCurrentInputConnection();
		if (connection != null){
			connection.commitText(sentence, 0);
		}
		this.mPinyinContext.clearContext() ;
		char lastHanzi ;
		if(sentence==null)
			lastHanzi='的' ;
		else
			lastHanzi=sentence.charAt(sentence.length()-1) ;
		this.mPinyinContext.updateLianXiang(lastHanzi) ;
		this.updateAll() ;
	}
	public void clearSentence(){
		this.mCandidateView.clearSentence() ;
	}
	public void updateAll(){
		if(this.mPinyinContext.getSentence().length()!=0)
			this.setCandidatesViewShown(true) ;
		else
			this.setCandidatesViewShown(false) ;
		this.mInputView.updateCandidateArea() ;
		this.mCandidateView.updateSentence() ;
	}
	
	public void clearAll(){
		if(mInputView!=null){
			mInputView.stopKeyboardView() ;
		}
		if(mCandidateView!=null){
			mCandidateView.clearSentence() ;
		}
	}
	
	private AlertDialog mOptionsDialog;
	private static final int POS_SETTINGS = 0;
	private static final int POS_METHOD = 1;

	private void showOptionsMenu() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setIcon(R.drawable.ic_dialog_keyboard);
		builder.setNegativeButton(android.R.string.cancel, null);
		CharSequence itemSettings = getString(R.string.ime_settings);
		CharSequence itemInputMethod = getString(R.string.inputMethod);
		builder.setItems(new CharSequence[] { itemSettings, itemInputMethod },
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface di, int position) {
						di.dismiss();
						switch (position) {
						case POS_SETTINGS:
//							launchSettings();
							break;
						case POS_METHOD:
							((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
									.showInputMethodPicker();
							break;
						}
					}
				});
		builder.setTitle(getResources().getString(R.string.ime_name));
		mOptionsDialog = builder.create();
		Window window = mOptionsDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.token = mInputView.getWindowToken();
		lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
		window.setAttributes(lp);
		window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		mOptionsDialog.show();
	}
	
	
	public void initCibiao() throws IOException{
		SharedPreferences state=getSharedPreferences("settings", MODE_PRIVATE) ;
		boolean isInit=state.getBoolean("IsInit", false) ;
		if(!isInit){
			this.copyFile(PinyinFile.PINYIN_LIST_FILE, PinyinFile.PINYIN_LIST_FILE);
			this.hebingFile(PinyinFile.PINYIN_HANZI1_FILENAME,PinyinFile.PINYIN_HANZI2_FILENAME,PinyinFile.PINYIN_HANZI_FILENAME) ;
			this.copyFile(PinyinFile.PINYIN_LIANXIANG_FILENAME, PinyinFile.PINYIN_LIANXIANG_FILENAME);
			this.hebingFile(PinyinFile.PINYIN_NODE1_FILENAME, PinyinFile.PINYIN_NODE2_FILENAME, PinyinFile.PINYIN_NODE_FILENAME);
			this.copyFile(PinyinFile.PINYIN_ROOT_FILENAME, PinyinFile.PINYIN_ROOT_FILENAME) ;
			SharedPreferences.Editor editor=state.edit() ;
			editor.putBoolean("IsInit", true) ;
		}
	}
	private void copyFile(String inFileName,String outFileName) throws IOException{
		InputStream in=this.getAssets().open(inFileName,AssetManager.ACCESS_BUFFER	) ;
		File dir=this.getFilesDir() ;
		String path=dir.getPath()+"/";
		File file=new File(path+outFileName) ;
		if(file.exists())
			file.delete()	 ;
		if(!file.createNewFile())
			return ;
		int bytesRead =0 ;
		FileOutputStream out=new FileOutputStream(file);
		byte[] buffer=new byte[8*1024];
		while((bytesRead=in.read(buffer))>0){
			out.write(buffer, 0, bytesRead) ;
		}
		out.flush() ;
		out.close() ;
		in.close() ;
	}
	private void hebingFile(String in1,String in2,String out) throws IOException{
		InputStream in1Stream=this.getAssets().open(in1,AssetManager.ACCESS_BUFFER	) ;
		InputStream in2Stream=this.getAssets().open(in2,AssetManager.ACCESS_BUFFER) ;
		File dir=this.getFilesDir() ;
		String path=dir.getPath()+"/";
		
		File file=new File(path+out);
		if(file.exists())
			file.delete() ;
		
		if(!file.createNewFile())
			return ;
		int count=0 ;
		FileOutputStream outStream=new FileOutputStream(file);
		byte[] buffer=new byte[8*1024];
		while((count=in1Stream.read(buffer	))>0){
			outStream.write(buffer, 0, count) ;
		}
		in1Stream.close() ;
		while((count=in2Stream.read(buffer	))>0){
			outStream.write(buffer, 0, count) ;
		}
		outStream.flush() ;
		outStream.close() ;
		in2Stream.close() ;
	}
	
	

    public void pickSuggestionManually(int index) {
        if (mCompletionOn && mCompletions != null && index >= 0
                && index < mCompletions.length) {
            CompletionInfo ci = mCompletions[index];
            InputConnection connection = this.getCurrentInputConnection();
            if (connection != null){
            	connection.commitCompletion(ci);
            }

            updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (mComposing.length() > 0) {
            // If we were generating candidate suggestions for the current
            // text, we would commit one of them here.  But for this sample,
            // we will just commit the current text.
//            commitTyped(getCurrentInputConnection());
        }
    }
    

    private String getWordSeparators() {
        return mWordSeparators;
    }
    
    public boolean isWordSeparator(int code) {
        String separators = getWordSeparators();
        return separators.contains(String.valueOf((char)code));
    }

    public void pickDefaultCandidate() {
        pickSuggestionManually(0);
    }

    
    public void setSuggestions(List<String> suggestions, boolean completions,
            boolean typedWordValid) {
        if (suggestions != null && suggestions.size() > 0) {
            setCandidatesViewShown(true);
        } else if (isExtractViewShown()) {
            setCandidatesViewShown(true);
        }

    }
}
