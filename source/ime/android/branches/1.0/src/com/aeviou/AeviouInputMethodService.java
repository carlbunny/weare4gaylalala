package com.aeviou;

import java.io.File;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.inputmethodservice.InputMethodService;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.aeviou.keyboard.AeviouKeyboardView;
import com.aeviou.pinyin.CustomPinyin;
import com.aeviou.pinyin.PinyinFile;
import com.aeviou.utils.AeviouConstants;
import com.aeviou.utils.BitmapUtils;
import com.aeviou.utils.FileUtils;
import com.aeviou.utils.SoundUtils;
//import com.aeviou.keyboard.KeyboardSensor;

/**
 * @author dhl
 * 
 */
public class AeviouInputMethodService extends InputMethodService {
	public AeviouKeyboardView inputView;
//	public KeyboardSensor keyboardSensor=null;
	/**
	 * This is the ENTRY of the IME.
	 * The function will initial the environment, and create the input view.
	 * If the IME is first run, word file will be copied into related directory.
	 * The function maybe invoke many times, e.g. when rotate the screen.
	 * 
	 * @return the input view created by inflate input.xml
	 */
	
	
	
	@Override
	public View onCreateInputView() {
		super.onCreateInputView();
		
		/* CAUTION: 
		 *   DO NOT REMOVE THE CODES BELOW, OTHERWISE WILL CAUSE MEMORY LEAK.
		 *   MAYBE IT IS THE BUG OF ANDROID, VERY WERID! 
		 */
		if (inputView != null){
			inputView.closing();
			inputView.close();
			inputView = null;
			AeviouConstants.inputMethodService = null;
			AeviouConstants.keyboardView = null;
			System.gc();
		}
		/* END CAUTION */
		AeviouConstants.inputMethodService = this;

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean isVirgin = preferences.getBoolean("isVirgin", true);
		
		//check dict exist
		File dir = this.getFilesDir();

		File file = new File(dir.getPath() + "/" + "phanzi.jpg");
		if(file.exists()==false){
			try {
				FileUtils.MergeFile("phanzi.jpg", null, "phanzi.jpg");
				FileUtils.MergeFile("pnode.jpg", null, "pnode.jpg");
				FileUtils.MergeFile("proot.jpg", null, "proot.jpg");
				FileUtils.MergeFile("plx.jpg", null, "plx.jpg");
			} catch (Exception ex) {
				/* No enough space */
				Toast.makeText(AeviouConstants.inputMethodService,
						R.string.no_space_dialog_content, Toast.LENGTH_LONG)
						.show(); 
			} 
		}
		
		/* Is the IME first run */
		if (isVirgin) {
			
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("isVirgin", false);
			editor.commit();
		}
		
		SoundUtils.initSounds();

		dir = this.getFilesDir();
		PinyinFile.PINYIN_FILE_DIRECTORY = dir.getPath() + "/";

		BitmapUtils.initBitmapUtils(this.getResources());
		inputView = (AeviouKeyboardView) getLayoutInflater().inflate(
				R.layout.input, null);
		
		//for sensor
//		keyboardSensor=new KeyboardSensor(this);
		return inputView;
	}
 
	/**
	 * This function will be invoke when user active the input fields.
	 */
	@Override
	public void onBindInput() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean lisenceAgreed = preferences.getBoolean("lisenceAgreed", false);
		if (!lisenceAgreed) {
			Intent lisenceIntent = new Intent(this,
					com.aeviou.setting.LicenseView.class);
			lisenceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(lisenceIntent);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("lisenceAgreed", true);
			editor.commit();
		}
		
	}

	/*
	 * Override to reset the state when hide the input view
	 */
	@Override
	public void onFinishInputView(boolean finish) {
		if (AeviouConstants.keyboardView != null) {
				AeviouConstants.keyboardView.reset();
		}
		CustomPinyin.getInstance().onDestory();
	}
	
	/**
	 * Override to prevent enter FULL SCREEN MODE
	 * @return Always return false
	 */
	@Override
	public boolean onEvaluateFullscreenMode(){
		return false; 
	}
	
	public void onDestroy (){
		CustomPinyin.getInstance().onDestory();
	}
}
