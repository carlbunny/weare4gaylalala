package com.aeviou.keyboard;

import android.view.KeyEvent;

import com.aeviou.pinyin.PinyinContext;
import com.aeviou.pinyin.PinyinTree;
import com.aeviou.utils.AeviouConstants;

public class InputLogic {
	public static void HexKeyBackspacePressed(PinyinContext pinyinContext){
		if (pinyinContext.getSize() > 0) {
			if (pinyinContext.getCorrectPosition() > 0){
				pinyinContext.correctWord(pinyinContext.getCorrectPosition() - 1);
			}else{
				pinyinContext.removePinyin();
			}
			AeviouConstants.candidateBar.updatePinyin();
		} else {
			AeviouConstants.inputMethodService
					.getCurrentInputConnection()
					.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
			AeviouConstants.inputMethodService
					.getCurrentInputConnection()
					.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
		}
	}
	
	public static void HexKeyEnterPressed(PinyinContext pinyinContext){
		if (pinyinContext.getSize() > 0) {
			AeviouConstants.inputMethodService
					.getCurrentInputConnection().commitText(
							pinyinContext.getSentence(), 0);
			pinyinContext.clearContext();
			AeviouConstants.candidateBar.updatePinyin();
			
		} else {
			AeviouConstants.inputMethodService
					.getCurrentInputConnection()
					.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
			AeviouConstants.inputMethodService
					.getCurrentInputConnection()
					.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
		}
	}
	
	public static void HexKeySpacePressed(PinyinContext pinyinContext){
		AeviouConstants.inputMethodService
			.getCurrentInputConnection()
			.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE));
		AeviouConstants.inputMethodService
			.getCurrentInputConnection()
			.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_SPACE));
	}
	
	public static void HexKeyPinyinPressed(PinyinContext pinyinContext, String pinyin){
		if (pinyin != null) {
			if (PinyinTree.getInstance().isCompletedPinyin(pinyin)) { 
				pinyinContext.addPinyin(pinyin);
				AeviouConstants.candidateBar.updatePinyin();
			}
		}
	}
	
	public static void HexKeySymbolPressed(PinyinContext pinyinContext, String symbol, boolean commitCandidate){
		if (symbol == null || symbol.equals("")) return;
		if (pinyinContext.getSize() > 0 && commitCandidate) {
			AeviouConstants.inputMethodService
					.getCurrentInputConnection().commitText(
							pinyinContext.getSentence(), 0);
			pinyinContext.clearContext();
			AeviouConstants.candidateBar.updatePinyin();
			
		}
		AeviouConstants.inputMethodService
			.getCurrentInputConnection().commitText(
					symbol, 1);
	}
	
	public static void HexKeyLetterPressed(PinyinContext pinyinContext, String letter){
		String pinyin = pinyinContext.getPinyin();
		if (pinyinContext.getSize() == 0
				|| (pinyin != null && pinyin.length() == 1 && 
					(pinyin.charAt(0) == 'a' || pinyin.charAt(0) == 'e')
					|| pinyin.charAt(0) == 'm' || pinyin.charAt(0) == 'o')) {
			if (pinyinContext.getSize() != 0){
				pinyinContext.clearContext();
				AeviouConstants.candidateBar.updatePinyin();
			}
			AeviouConstants.inputMethodService
				.getCurrentInputConnection().commitText(letter, 1);
		}
	}
}
