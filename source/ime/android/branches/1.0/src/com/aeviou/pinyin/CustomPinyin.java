package com.aeviou.pinyin;

import java.util.Collection;

public abstract class CustomPinyin {

	public abstract void clearPinyin();

	public abstract void addPinyin(String pinyin);

	public abstract void removePinyin();

	public abstract Collection<PinyinObject> getPinyin();

	public abstract void debug();
	
	public abstract void onDestory();

	public abstract boolean testCustomExist(String word);

	public abstract void recordPinyin(String word);

	public static CustomPinyin getInstance() {
		return CustomPinyinJSON.getInstance();
	}
	

}
