package com.aeviou.utils;

import android.content.res.AssetManager;

import com.aeviou.AeviouInputMethodService;
import com.aeviou.candidate.CandidateBar;
import com.aeviou.keyboard.AeviouKeyboardView;

public class AeviouConstants {
	public static final char KEYCODE_SPACE = 0xFFE1;
	public static final char KEYCODE_BACKSPACE = 0xFFE2;
	public static final char KEYCODE_ENTER = 0xFFE3;
	public static final char KEYCODE_AOE = 0xFFE4;
	public static final char KEYCODE_SHIFT = 0xFFE5;
	public static final char KEYCODE_CAPS = 0xFFE6;
	public static final char KEYCODE_HYBRID = 0xFFE7;
	public static final char KEYCODE_SWITCH = 0xFFE8;
	public static final char KEYCODE_SYMBOL = 0xFFE9;
	public static final char KEYCODE_SYMBOLCENTER=0xFFEA;
	public static final char KEYCODE_NULL = 0xFFFF;

	public enum PIC_ID {
		NULL,HEX_NORAML, HEX_SELECTED, HEX_NEXT, HEX_CHANGED, DEL, NUM, SET, SPACE, ENTER, AOE, CANDIDATE_LEFT_ARROW, CANDIDATE_RIGHT_ARROW, CANDIDATE_LEFT_BG, CANDIDATE_RIGHT_BG, CANDIDATE_SINGLE_BG, CANDIDATE_MID_BG, SWITCH_EN, SWITCH_CH, SWITCH_NUM, SWITCH_SETTING, SWITCH_BACKGROUND, SWITCH_SQUARE_BACKGROUND, SQUARE_NORAML, SHIFT, CLOSE, CAPS, SWITCH_HYBRID, EXIT_HYBRID, SWITCH_SYMBOL, SWITCH_KEY, 
	}

	public static final int NORMAL_IME_VIEW_WIDTH_SIMPLE = 1000;
	public static final int NORMAL_IME_VIEW_WIDTH_FULL = 1575;
	public static int NORMAL_IME_VIEW_WIDTH = 1000;
	public static final int NORMAL_IME_VIEW_HEIGHT = 660;
	public static final int NORMAL_IME_HEXKEY_WIDTH = 143;
	public static final int NORMAL_IME_HEXKEY_HEIGHT = 165;
	public static final float NORMAL_IME_HEXKEY_HEIGHTSCALE = 1.155f;
	public static final int NORMAL_IME_SQUAREKEY_WIDTH = 100;
	public static final int NORMAL_IME_SQUAREKEY_HEIGHT = 137;
	public static final int NORMAL_BAR_HEIGHT = 110;
	public static final int NORMAL_SWITCH_BUTTON_WIDTH = 100;
	public static final int NORMAL_SWITCH_BUTTON_HEIGHT = 100;
	public static final int NORMAL_CANDIDATE_KEY_WIDTH = NORMAL_IME_HEXKEY_WIDTH;

	public static final int NORMAL_LETTER_HEIGHT = NORMAL_IME_HEXKEY_WIDTH / 2;
	public static final int NORMAL_SQUARE_RADIUS_THRESHOLD = 4500;
	public static final int NORMAL_KEYBOARD_TOP_MARGIN = 10;
	public static final int NORMAL_CANDIDATE_WORD = 55;
	public static final int NORMAL_CANDIDATE_SENTENCE = 45;
	public static int NORMAL_CANDIDATE_LEFT_MARGIN = NORMAL_IME_HEXKEY_WIDTH;
	public static final int NORMAL_CANDIDATE_TOP_MARGIN = 25;
	public static final int NORMAL_CANDIDATE_KEY_TOP_MARGIN = 75;
	public static final int NORMAL_CANDIDATE_KEY_LEFT_MARGIN = 25;
	public static final int NORMAL_CANDIDATE_LARROW_LEFT_MARGIN = 16;
	public static final int NORMAL_CANDIDATE_RARROW_LEFT_MARGIN_SIMPLE = 943;
	public static final int NORMAL_CANDIDATE_RARROW_LEFT_MARGIN_FULL = 1495;
	public static int NORMAL_CANDIDATE_RARROW_LEFT_MARGIN;
	public static int NORMAL_CANDIDATE_ARROW_TOP_MARGIN = 60;
	public static final int NORMAL_CANDIDATE_ARROW_WIDTH = 40;
	public static final int NORMAL_CANDIDATE_ARROW_HEIGHT = 47;
	public static final int NORMAL_LINE_WIDTH = 25;
	public static final int NORMAL_SQUAREKEYBOARD_TOP_MARGIN = 25;

	public static final int MSG_UPDATE_TIPVIEW = 0;
	public static final int MSG_UPDATE_SENTENCE = 1;
	public static final int MSG_UPDATE_KEYBOARDTIP = 2;

	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static int IME_VIEW_WIDTH;
	public static int IME_VIEW_HEIGHT;
	public static float IME_VIEW_VSCALE;
	public static float IME_VIEW_HSCALE;
	public static int IME_VIEW_LEFT_MARGIN = 0;

	public static AeviouInputMethodService inputMethodService;
	public static CandidateBar candidateBar;
	public static boolean needRedraw;

	public static AeviouKeyboardView keyboardView;

	public static boolean enableTipView = true;
	public static boolean tipViewLight = true;
	public static boolean enableSound = false;
	public static boolean enableVibrate = true;
	public static boolean verticalFull = false;
	public static boolean horizontalFull = true;
	public static boolean hideOthers = false;
	public static boolean enableZoom = false;
	public static final long vibrateDuration = 20;

	public static final String LogTag = "aeviou";

	public static enum Direction {
		left, center, right;
	};

	public static int clamp(int x, int low, int high) {
		return (x < low) ? low : ((x > high) ? high : x);
	}

	public static void commitText(String text) {
		for (int i = 0; i < text.length(); i++) {
			inputMethodService.getCurrentInputConnection().commitText(
					text.substring(i, i + 1), 1);
		}
	}

}
