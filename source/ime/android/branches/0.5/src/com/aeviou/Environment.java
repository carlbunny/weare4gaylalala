package com.aeviou;


public class Environment {

	public static int RESOLUTION_WIDTH=480 ;//手机水平分辨率
	public static int RESOLUTION_HEIGHT=854 ;
	
	//手机垂直分辨率
	public static  final float FONTSIZE_KEYBOARD=30 ;//键盘上按键字符的大小
	public static  final float FONTSIZE_CANDIDATE=35 ;//候选字符的大小
	public static  final float FONTSIZE_SENTENCE =35 ;//汉字序列的大小
	public static  final float FONTSIZE_PINYINSEQ=20 ;//拼音序列的大小
	public static final int GAP_CANDIDATEVIEW=10 ;//candidate view 上汉字序列与拼音序列之间的间距
	public static  final int STROKEWIDTH_KEYBOARD=5 ;//键盘线宽
	public static float height_scale=1 ;
	public static float width_scale=1 ;
	public static float FONTSIZE_KEYBOARD_TEMP=0 ;
	public static float FONTSIZE_CANDIDATE_TEMP=0 ;
	public static float FONTSIZE_SENTENCE_TEMP=0 ;
	public static float FONTSIZE_PINYINSEQ_TEMP=0 ;
	public static int GAP_CANDIDATEVIEW_TEMP=0 ;
	public static int STROKEWIDTH_KEYBOARD_TEMP=0 ;
	public static void updateGlobalConstant(float height_scale,float width_scale){
		Environment.height_scale=height_scale ;
		Environment.width_scale=width_scale ;
		FONTSIZE_KEYBOARD_TEMP=FONTSIZE_KEYBOARD*width_scale ;
		FONTSIZE_CANDIDATE_TEMP=FONTSIZE_CANDIDATE*width_scale ;
		FONTSIZE_SENTENCE_TEMP=FONTSIZE_SENTENCE*width_scale ;
		FONTSIZE_PINYINSEQ_TEMP=FONTSIZE_PINYINSEQ*width_scale ;
		GAP_CANDIDATEVIEW_TEMP=(int)(GAP_CANDIDATEVIEW*width_scale) ;
		STROKEWIDTH_KEYBOARD_TEMP=(int)(STROKEWIDTH_KEYBOARD*width_scale) ;
	}
}
