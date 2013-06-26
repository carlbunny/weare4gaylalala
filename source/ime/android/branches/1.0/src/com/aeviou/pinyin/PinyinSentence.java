package com.aeviou.pinyin;

public class PinyinSentence {
	public String sentence;
	int wordWeight;
	int charWeight;
	
	public PinyinSentence(String sentence, int wordWeight, int charWeight){
		this.sentence = sentence;
		this.wordWeight = wordWeight;
		this.charWeight = charWeight;
	}
	
	public boolean isBetterThan(PinyinSentence ps){
		if (this.wordWeight > ps.wordWeight){
			return true;
		}else if (this.wordWeight < ps.wordWeight){
			return false;
		}else{
			if (this.charWeight > ps.charWeight){
				return true;
			}
		}
		
		return false;
	}
}
