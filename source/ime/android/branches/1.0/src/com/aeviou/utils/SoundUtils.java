package com.aeviou.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

import com.aeviou.R;

public class SoundUtils {
	private static SoundPool soundPool;
	private static int soundID;
	private static Vibrator vibrator;
	private static AudioManager mgr;

	public static void initSounds() {
		soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		soundID = soundPool.load(AeviouConstants.inputMethodService,
				R.raw.stardard, 1);
		vibrator = (Vibrator) AeviouConstants.inputMethodService
				.getSystemService(Context.VIBRATOR_SERVICE);
		mgr = (AudioManager) AeviouConstants.inputMethodService
				.getSystemService(Context.AUDIO_SERVICE);
	}

	public static void playSoundAndVibration() {
		//playSound();
		playVibration();
	}

	public static void playSound() {
		if (AeviouConstants.enableSound) {
			float streamVolumeCurrent = mgr
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamVolumeMax = mgr
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = streamVolumeCurrent / streamVolumeMax;
			soundPool.play(soundID, volume, volume, 1, 0, 1.0f);
		}
	}

	public static void playVibration() {
		if (AeviouConstants.enableVibrate) {
			try {
				vibrator.vibrate(AeviouConstants.vibrateDuration);
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
	}
}
