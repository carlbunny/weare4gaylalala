package com.aeviou.setting;

import java.util.Map;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.aeviou.R;
import com.aeviou.pinyin.PinyinTree;
import com.aeviou.utils.AeviouConstants;

/**
 * @author Rex
 * 
 *         The SettingView provides interface for the settings and preferences.
 *         It is constructed by reading the XML resources and is easy to modify.
 */
public class SettingView extends PreferenceActivity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 * use addPreferencesFromResource() from XML resource preferences to speed
	 * up coding
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.preferences);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 * 
	 * If the input method somehow is suspended, call that routine to ensure
	 * consistency.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		loadPreferences();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onStop() If the input method
	 * somehow is stopped, still need to call that routine to ensure
	 * consistency.
	 */
	@Override
	protected void onStop() {
		super.onStop();
		loadPreferences();
		Toast.makeText(AeviouConstants.inputMethodService,
				R.string.preference_saved, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Loads preferences into memory and sets all fuzzy pinyin flags
	 * accordingly. Updates global flags to ensure program behavior is
	 * consistent with the user preferences.
	 */
	public static void loadPreferences() {
		PreferenceManager.setDefaultValues(AeviouConstants.inputMethodService,
				R.xml.preferences, false);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(AeviouConstants.inputMethodService);
		int moHuShengFlag = 0;
		int moHuYunFlag = 0;
		if (preferences.getBoolean("z_zh", false)) {
			moHuShengFlag |= PinyinTree.MOHU_SHENG_Z_ZH;
		}
		if (preferences.getBoolean("c_ch", false)) {
			moHuShengFlag |= PinyinTree.MOHU_SHENG_C_CH;
		}
		if (preferences.getBoolean("s_sh", false)) {
			moHuShengFlag |= PinyinTree.MOHU_SHENG_S_SH;
		}
		if (preferences.getBoolean("l_n", false)) {
			moHuShengFlag |= PinyinTree.MOHU_SHENG_L_N;
		}
		if (preferences.getBoolean("r_l", false)) {
			moHuShengFlag |= PinyinTree.MOHU_SHENG_R_L;
		}
		if (preferences.getBoolean("an_ang", false)) {
			moHuYunFlag |= PinyinTree.MOHU_YUN_AN_ANG;
		}
		if (preferences.getBoolean("en_eng", false)) {
			moHuYunFlag |= PinyinTree.MOHU_YUN_EN_ENG;
		}
		if (preferences.getBoolean("in_ing", false)) {
			moHuYunFlag |= PinyinTree.MOHU_YUN_IN_ING;
		}
		if (preferences.getBoolean("uan_uang", false)) {
			moHuYunFlag |= PinyinTree.MOHU_YUN_UAN_UANG;
		}
		if (preferences.getBoolean("ian_iang", false)) {
			moHuYunFlag |= PinyinTree.MOHU_YUN_IAN_IANG;
		}
		PinyinTree.getInstance().setMoHuFlag(moHuShengFlag, moHuYunFlag);

		Map<String, ?> map = preferences.getAll();

		Boolean True = Boolean.valueOf(true);
		AeviouConstants.enableTipView = True.equals((Boolean) map.get("hint"));
		AeviouConstants.tipViewLight = True.equals((Boolean) map
				.get("hint_light"));
		AeviouConstants.enableSound = True.equals((Boolean) map.get("sound"));
		AeviouConstants.enableVibrate = True.equals((Boolean) map
				.get("vibrate"));
		AeviouConstants.verticalFull = True.equals((Boolean) map.get("v_full"));
		AeviouConstants.horizontalFull = True.equals((Boolean) map
				.get("h_full"));
		AeviouConstants.hideOthers = True.equals((Boolean) map
				.get("hide_others"));
		// for 3d keyboard
		// AeviouConstants.enableZoom = preferences.getBoolean("zoom", false);
		// if(AeviouConstants.inputMethodService.keyboardSensor!=null){
		// AeviouConstants.inputMethodService.keyboardSensor.toggleRegister();
		// }
		// AeviouConstants.enableZoom = preferences.getBoolean("zoom", false);
	}

}
