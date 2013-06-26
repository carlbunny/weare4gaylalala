package com.aeviou.keyboard;


import java.util.ArrayList;
import java.util.HashMap;

import com.aeviou.keyboard.KeyboardCh;
import com.aeviou.pinyin.PinyinTree;
import com.aeviou.pinyin.PinyinContext; 
import com.aeviou.AeviouIME;
import com.aeviou.R ;
import com.aeviou.Environment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard.Key;
import android.media.AudioManager;
import android.media.SoundPool;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;

import android.util.AttributeSet;
import android.util.DisplayMetrics;

import android.app.Dialog;
public class AeviouKeyboardView extends KeyboardView {
	public static final int KEYCODE_OPTIONS = -100;
	public static final String TAG="KEYBOARDVIEW_TAG";
	
	public static final int RESOLUTION_WIDTH=480 ;
	public static final int RESOLUTION_HEIGHT=854 ;
	public static  float FONTSIZE ;
	public static final int MAXVOLUME=10 ;
	public static final int KEYAUDIO=0x01 ;
	public static final int KEYWARNING =0x02 ;
	
	public static final int BMP_NORMAL=0 ;
	public static final int BMP_PRESSED=1 ;
	public static final int BMP_CANDIDATE1=2 ;
	public static final int BMP_CANDIDATE2=3 ;
	public static final int BMP_DEL=4 ;
	public static final int BMP_NUM=5 ;
	public static final int BMP_SET=6 ;
	public static final int BMP_SPACE=7 ;
	public static final int BMP_ENTER=8 ;
	
	public static final byte KEYBOARD_QWERTY =0x01 ;
	public static final byte KEYBOARD_SYMBOLS=0x02 ;
	public static final byte KEYBOARD_PINYIN=0x03 ;
	public float keyHeight=0 ;
	public float keyWidth=0 ;
	public int viewWidth=0 ;
	public float radius=0 ;
	public Bitmap bmp[] ;
	public Paint paint ;
	
	public KeyboardCh mChKeyboard ;
	private AeviouIME service ;
	AlertDialog setting ;
	private LayoutInflater inflate;
	ScrollView setLayout ;
	public boolean isVertical=false  ;
	
	
	private ArrayList<CandidateKey> candidateKey ;
	private ArrayList<Integer> canLineIndex ;
	
	Dialog mCandidateWindow ;
    private SoundPool soundPool;   
    private HashMap<Integer, Integer> soundPoolMap; 
    private int volumn=5 ;
    private  GestureDetector gesture ;
    private PointF pointTemp ;
	public AeviouKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setBackgroundColor(Color.rgb(39, 39, 39)) ;
		this.loadResource() ;
		this.setDimension() ;
		this.mChKeyboard=new KeyboardCh(this) ;
		this.paint=new Paint() ;
		paint.setTextSize(FONTSIZE) ;
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setStrokeWidth(10) ;
		paint.setColor(Color.WHITE) ;
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		
        inflate =(LayoutInflater) getContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.initSounds() ;
     
		candidateKey=new ArrayList<CandidateKey>() ;
		canLineIndex=new ArrayList<Integer>() ;
		
		this.pointTemp=new PointF() ;
        gesture =new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
    		private static final int SWIPE_MIN_DISTANCE = 80;    
    		private static final int SWIPE_THRESHOLD_VELOCITY = 200; 
    		@Override   
    		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {  
    			if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) { 
    				returnLineCandidate() ;
    			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) { 
    				nextLineCandidate()	 ;
    			}   			
    			return true ;
    		}
        }) ;
	}
    @Override
    protected boolean onLongPress(Key key) {
        if (key.codes[0] == Keyboard.KEYCODE_CANCEL) {
            getOnKeyboardActionListener().onKey(KEYCODE_OPTIONS, null);
            return true;
        } else {
            return super.onLongPress(key);
        }
    }
    public void setSevice(AeviouIME ime){
    	this.service=ime ;
    }
	public void startKeyboardView(){
		byte state =this.service.getIMEState() ;
		switch(state){
		case AeviouIME.IME_STATE_CH:
			   this.initSettingDialog() ;
			this.mChKeyboard.loadKeyboard() ;
			
			break ;
		case AeviouIME.IME_STATE_EN:
			break ;
		}
	}
	public void stopKeyboardView(){
		if(!this.candidateKey.isEmpty())
			this.candidateKey.clear() ;
	}
	

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec) ;
    	byte state=this.service.getIMEState() ;
    	if(state==AeviouIME.IME_STATE_CH){
    		DisplayMetrics dm=new DisplayMetrics() ;
        	WindowManager wm=(WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE) ;
        	wm.getDefaultDisplay().getMetrics(dm) ;	
        	this.setDimension() ;
          	float width_scale=dm.widthPixels/(float)Environment.RESOLUTION_WIDTH ;
        	float height_scale=dm.heightPixels/(float)Environment.RESOLUTION_HEIGHT ;
        	Environment.updateGlobalConstant(height_scale, width_scale) ;
        	if(dm.widthPixels>dm.heightPixels){
            	dm.widthPixels=(int)(Environment.RESOLUTION_HEIGHT*width_scale);
        	}else{
            	dm.widthPixels=(int)(Environment.RESOLUTION_WIDTH*width_scale);
        	}
        	if(dm.heightPixels<dm.widthPixels){
           		this.setMeasuredDimension(dm.widthPixels, dm.widthPixels*1/3+(int)keyHeight/2);
        	}else{
        		this.setMeasuredDimension(dm.widthPixels, dm.widthPixels*3/5);
        	}
    	}
    }
    private void setDimension(){
		DisplayMetrics dm=new DisplayMetrics() ;
    	WindowManager wm=(WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE) ;
    	wm.getDefaultDisplay().getMetrics(dm) ;	
      	float width_scale=dm.widthPixels/(float)Environment.RESOLUTION_WIDTH ;
    	float height_scale=dm.heightPixels/(float)Environment.RESOLUTION_HEIGHT ;

    	Environment.updateGlobalConstant(height_scale, width_scale) ;
    	FONTSIZE=Environment.FONTSIZE_KEYBOARD_TEMP;
    	if(dm.widthPixels>dm.heightPixels){
        	dm.widthPixels=(int)(Environment.RESOLUTION_HEIGHT*width_scale);
    	}else{
        	dm.widthPixels=(int)(Environment.RESOLUTION_WIDTH*width_scale);
    	}
    	this.viewWidth=dm.widthPixels ;
    	this.keyWidth=viewWidth/7 ;
    	if(dm.widthPixels>dm.heightPixels){
    		this.keyHeight=keyWidth*2/3 ;
        	this.isVertical=true ;
    	}else{
        	this.keyHeight=this.keyWidth ;
        	this.isVertical=false ;
    	}
    	this.radius=keyHeight*keyHeight/2 ;
    }
    private void loadResource()
    {
    	Resources res=getResources() ;
    	
    	bmp=new Bitmap[10] ;	
    	BitmapDrawable bmpDraw=(BitmapDrawable)res.getDrawable(R.drawable.keynormal) ;
    	bmp[BMP_NORMAL]=bmpDraw.getBitmap() ;
    	bmpDraw=(BitmapDrawable)res.getDrawable(R.drawable.keypressed) ;
    	bmp[BMP_PRESSED]=bmpDraw.getBitmap() ;
    	bmpDraw=(BitmapDrawable)res.getDrawable(R.drawable.keycandidate1) ;
    	bmp[BMP_CANDIDATE1]=bmpDraw.getBitmap() ;
    	bmpDraw=(BitmapDrawable)res.getDrawable(R.drawable.keycandidate2) ;
    	bmp[BMP_CANDIDATE2]=bmpDraw.getBitmap() ;
    	
    	bmpDraw=(BitmapDrawable)res.getDrawable(R.drawable.set) ;
    	bmp[BMP_SET]=bmpDraw.getBitmap() ;
    	bmpDraw=(BitmapDrawable)res.getDrawable(R.drawable.space) ;
    	bmp[BMP_SPACE]=bmpDraw.getBitmap() ;
    	bmpDraw=(BitmapDrawable)res.getDrawable(R.drawable.enter) ;
    	bmp[BMP_ENTER]=bmpDraw.getBitmap() ;
    	bmpDraw=(BitmapDrawable)res.getDrawable(R.drawable.num) ;
    	bmp[BMP_NUM]=bmpDraw.getBitmap() ; 
       	bmpDraw=(BitmapDrawable)res.getDrawable(R.drawable.del) ;
    	bmp[BMP_DEL]=bmpDraw.getBitmap() ; 
    	
       	bmpDraw=(BitmapDrawable)res.getDrawable(R.drawable.bg_candidate) ;
    	bmp[9]=bmpDraw.getBitmap() ;
    	
    	keyHeight=res.getDimension(R.dimen.keyHeight_H);
    }
    @Override 
    public void onDraw(Canvas canvas)
    {		
    	byte state=this.service.getIMEState() ;
    	switch(state){
    	case AeviouIME.IME_STATE_CH:
    		mChKeyboard.refresh(canvas) ;
    		for(int i=0;i<candidateKey.size();i++){
    			this.candidateKey.get(i).Refresh(canvas, keyHeight, paint, CandidateKey.CandidateIndex,bmp[9]) ;
    		}
    		break ;
    	case AeviouIME.IME_STATE_EN:
    		super.onDraw(canvas) ;
    		break ;
    	}
    }
     
	public boolean onTouchEvent(MotionEvent event) {
		byte state=this.service.getIMEState() ;
		
		if(state!=AeviouIME.IME_STATE_CH)
			super.onTouchEvent( event);
		else{
	        int eventaction = event.getAction();
	        float X=event.getX() ;
	        float Y=event.getY() ;
	        this.gesture.onTouchEvent(event);
	        switch(eventaction){
	        case MotionEvent.ACTION_DOWN:
	        	handleActionDown(X,Y) ;
	        	break ;
	        case MotionEvent.ACTION_MOVE:
	        	handleActionMove(X,Y) ;
	        	break ;
	        case MotionEvent.ACTION_UP:
	        	handleActionUp(X,Y) ;
	        	break ;
	        }
        }
        return true ;
	}
	private void handleActionDown(float X,float Y){
		pointTemp.x=X ;
		pointTemp.y=Y ;
		this.mChKeyboard.handleKeyDown(X, Y) ;
	}
	private void handleActionMove(float X,float Y){
		this.mChKeyboard.handleKeyMove(X, Y) ;
	}
	private void handleActionUp(float X,float Y){
		float distance =(pointTemp.x-X)*(pointTemp.x-X)+(pointTemp.y-Y)*(pointTemp.y-Y) ;
		if(distance<40*40){
			if(Y<keyHeight){
				this.chooseCandidate(X, Y) ;
			}else{
				this.updateCandidateArea() ;
			}
		}
		this.mChKeyboard.handleKeyUp(X, Y) ;
	}
	public void commitText(CharSequence text){
		this.getOnKeyboardActionListener().onText(text) ;
	}
	public void commitChar(int code){
		switch((char)code){
		case '£¨':
			 ;
		case '°£':
			 ;
		case '£ø':
			 ;
		case '£°':
			if(this.service.getPinyinContext().getPinyin()!=null){
				if(this.service.getPinyinContext().getPinyin().length()!=0)
					this.service.commiteSentence() ;
			}
			break ;
		}
		this.getOnKeyboardActionListener().onKey(code, null);
	}
	public void setKeyboard(AeviouKeyboard keyboard) {
		byte state=this.service.getIMEState() ;
		if(state!=AeviouIME.IME_STATE_CH){
			super.setKeyboard(keyboard) ;
		}
		else 
			this.startKeyboardView() ;
	}
	private void nextLineCandidate(){
		PinyinContext mPinyinContext=service.getPinyinContext() ;
		CandidateKey.nextLineCandidate(canLineIndex, candidateKey, mPinyinContext, this.getWidth()) ;
	}
	private void returnLineCandidate(){
		PinyinContext mPinyinContext=service.getPinyinContext() ;
		CandidateKey.returnLineCandidate(canLineIndex, candidateKey, mPinyinContext, this.getWidth());
	}
	public void updateCandidateArea(){
		PinyinContext mPinyinContext=service.getPinyinContext() ;
		int index=CandidateKey.formatCandidateKey(candidateKey, mPinyinContext, 0,this.getWidth()) ;
		if(!canLineIndex.isEmpty())
			canLineIndex.clear() ;
		canLineIndex.add(0) ;
		canLineIndex.add(index) ;
		this.invalidate() ;
	}
	private void chooseCandidate(float X,float Y){
		
		PinyinContext mPinyinContext=service.getPinyinContext() ;
		int index=CandidateKey.getCandidateIndex(candidateKey, X, Y,this.keyHeight) ;
		if(index==CandidateKey.ERROR_CANDIDATEINDEX)
			return ;
		int size=this.canLineIndex.size() ;
		mPinyinContext.chooseCandidate(index+canLineIndex.get(size-2)) ;
		service.updateAll() ;
		String pinyin=mPinyinContext.getPinyin() ;
		if(pinyin==null){
			service.commiteSentence() ;
			service.setCandidatesViewShown(false) ;
			return ;
		}else if(pinyin.length()==0){
			service.commiteSentence() ;
			service.setCandidatesViewShown(false) ;
			return ;
		}
		if(mPinyinContext.getCorrectPosition()>=mPinyinContext.getSentence().length()){
			service.commiteSentence() ;
		}
	}
	public String getChoosedCandidate(){
		String temp=null ;
		if(!candidateKey.isEmpty())
			temp=candidateKey.get(0).keyValue ;
		return temp ;
	}
	public void clearCandidate(){
		this.candidateKey.clear() ;
	}
	private void initSettingDialog(){
		SharedPreferences settings=service.getSharedPreferences("settings", AeviouIME.MODE_PRIVATE) ;
		int mYunMohuFlag=settings.getInt("mYunMoHuFlag", 0) ;
		int mShengMohuFlag=settings.getInt("mShengMoHuFlag", 0) ;
		boolean mTipsFlag=settings.getBoolean("mTipsFlag", false) ;
		volumn=settings.getInt("Volumn", 5) ;
		PinyinTree.getInstance().setMoHuFlag(mShengMohuFlag, mYunMohuFlag);
    	setLayout=(ScrollView)this.inflate.inflate(R.layout.setting, null) ;
    	SeekBar voice=(SeekBar)setLayout.findViewById(R.id.voice) ;
    	voice.setMax(MAXVOLUME) ;
    	voice.setProgress(volumn) ;
    	
    	CheckBox checkBox ;
   
    	if((mShengMohuFlag|PinyinTree.MOHU_SHENG_Z_ZH)==mShengMohuFlag){
        	checkBox=(CheckBox)setLayout.findViewById(R.id.z_zh) ;
        	checkBox.setChecked(true) ;
    	}else{
        	checkBox=(CheckBox)setLayout.findViewById(R.id.z_zh) ;
        	checkBox.setChecked(false) ;
    	}
    	if((mShengMohuFlag|PinyinTree.MOHU_SHENG_C_CH)==mShengMohuFlag){
        	checkBox=(CheckBox)setLayout.findViewById(R.id.c_ch) ;
        	checkBox.setChecked(true) ;
    	}else{
        	checkBox=(CheckBox)setLayout.findViewById(R.id.c_ch) ;
        	checkBox.setChecked(false) ;
    	}
    	if((mShengMohuFlag|PinyinTree.MOHU_SHENG_S_SH)==mShengMohuFlag){
        	checkBox=(CheckBox)setLayout.findViewById(R.id.s_sh) ;
        	checkBox.setChecked(true) ;
    	}else{
        	checkBox=(CheckBox)setLayout.findViewById(R.id.s_sh) ;
        	checkBox.setChecked(false) ;
    	}
    	if((mShengMohuFlag|PinyinTree.MOHU_SHENG_F_H)==mShengMohuFlag){
        	checkBox=(CheckBox)setLayout.findViewById(R.id.f_h) ;
        	checkBox.setChecked(true) ;
    	}else{
        	checkBox=(CheckBox)setLayout.findViewById(R.id.f_h) ;
        	checkBox.setChecked(false) ;
    	}
    	if((mShengMohuFlag|PinyinTree.MOHU_SHENG_L_N)==mShengMohuFlag){
        	checkBox=(CheckBox)setLayout.findViewById(R.id.l_n) ;
        	checkBox.setChecked(true) ;
    	}else{
        	checkBox=(CheckBox)setLayout.findViewById(R.id.l_n) ;
        	checkBox.setChecked(false) ;
    	}
    	if((mShengMohuFlag|PinyinTree.MOHU_SHENG_R_L)==mShengMohuFlag){
        	checkBox=(CheckBox)setLayout.findViewById(R.id.r_l) ;
        	checkBox.setChecked(true) ;
    	}else{
        	checkBox=(CheckBox)setLayout.findViewById(R.id.r_l) ;
        	checkBox.setChecked(false) ;
    	}

    	if((mYunMohuFlag|PinyinTree.MOHU_YUN_AN_ANG)==mYunMohuFlag){
        	checkBox=(CheckBox)setLayout.findViewById(R.id.an_ang) ;
        	checkBox.setChecked(true) ;
    	}else{
        	checkBox=(CheckBox)setLayout.findViewById(R.id.an_ang) ;
        	checkBox.setChecked(false) ;
    	}
    	if((mYunMohuFlag|PinyinTree.MOHU_YUN_EN_ENG)==mYunMohuFlag){
        	checkBox=(CheckBox)setLayout.findViewById(R.id.en_eng) ;
        	checkBox.setChecked(true) ;
    	}else{
        	checkBox=(CheckBox)setLayout.findViewById(R.id.en_eng) ;
        	checkBox.setChecked(false) ;
    	}
    	if((mYunMohuFlag|PinyinTree.MOHU_YUN_IAN_IANG)==mYunMohuFlag){
        	checkBox=(CheckBox)setLayout.findViewById(R.id.ian_iang) ;
        	checkBox.setChecked(true) ;
    	}else{
        	checkBox=(CheckBox)setLayout.findViewById(R.id.ian_iang) ;
        	checkBox.setChecked(false) ;
    	}
    	if((mYunMohuFlag|PinyinTree.MOHU_YUN_IN_ING)==mYunMohuFlag){
        	checkBox=(CheckBox)setLayout.findViewById(R.id.in_ing) ;
        	checkBox.setChecked(true) ;
    	}else{
        	checkBox=(CheckBox)setLayout.findViewById(R.id.in_ing) ;
        	checkBox.setChecked(false) ;
    	}
    	if((mYunMohuFlag|PinyinTree.MOHU_YUN_UAN_UANG)==mYunMohuFlag){
        	checkBox=(CheckBox)setLayout.findViewById(R.id.uan_uang) ;
        	checkBox.setChecked(true) ;
    	}else{
        	checkBox=(CheckBox)setLayout.findViewById(R.id.uan_uang) ;
        	checkBox.setChecked(false) ;
    	}
  
    	RadioGroup radioGroup=(RadioGroup)setLayout.findViewById(R.id.tips_switch	) ;
    	if(mTipsFlag){
    		radioGroup.check(R.id.opentips) ;
    		setDrawingCacheEnabled(true) ;
    		this.mChKeyboard.openTips();
    	}
    	else{
    		radioGroup.check(R.id.closetips);
    		setDrawingCacheEnabled(false) ;
    		this.mChKeyboard.closeTips() ;
    	}
  
	}
	public void setProperty(){
		if(setting!=null){
			if(!setting.isShowing()){
				setting.show() ;
			}
			return ;
		}
	   	AlertDialog.Builder builder = new AlertDialog.Builder(service);
    	builder.setView(setLayout) ;
    	builder.setCancelable(true);
    	builder.setTitle("…Ë÷√") ;
    	setting=builder.create() ;

    	Window window=setting.getWindow() ;
    	WindowManager.LayoutParams lp = window.getAttributes();
		lp.token = getWindowToken();
		lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
		window.setAttributes(lp);
		window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		setting.setButton("»∑∂®", new DialogInterface.OnClickListener(){
			int moHuShengFlag = 0;
			int moHuYunFlag = 0;
			private void updateMohu(){
					moHuShengFlag=0 ;
					moHuYunFlag=0 ;
					CheckBox checkBox1=(CheckBox)setting.findViewById(R.id.z_zh) ;
					if(checkBox1.isChecked()){
						moHuShengFlag |= PinyinTree.MOHU_SHENG_Z_ZH;
					}
					CheckBox checkBox=(CheckBox)setting.findViewById(R.id.c_ch) ;
					if(checkBox.isChecked())
						moHuShengFlag |= PinyinTree.MOHU_SHENG_C_CH;
					checkBox=(CheckBox)setting.findViewById(R.id.s_sh) ;
					if(checkBox.isChecked())
						moHuShengFlag |= PinyinTree.MOHU_SHENG_S_SH;
					checkBox=(CheckBox)setting.findViewById(R.id.l_n) ;
					if(checkBox.isChecked())
						moHuShengFlag |= PinyinTree.MOHU_SHENG_L_N;
					checkBox=(CheckBox)setting.findViewById(R.id.f_h) ;
					if(checkBox.isChecked())
						moHuShengFlag |= PinyinTree.MOHU_SHENG_F_H;
					checkBox=(CheckBox)setting.findViewById(R.id.r_l) ;
					if(checkBox.isChecked())
						moHuShengFlag |= PinyinTree.MOHU_SHENG_R_L;
		
					
					checkBox=(CheckBox)setting.findViewById(R.id.an_ang) ;
					if(checkBox.isChecked())
						moHuYunFlag |= PinyinTree.MOHU_YUN_AN_ANG;
					checkBox=(CheckBox)setting.findViewById(R.id.en_eng) ;
					if(checkBox.isChecked())
						moHuYunFlag |= PinyinTree.MOHU_YUN_EN_ENG;
					checkBox=(CheckBox)setting.findViewById(R.id.in_ing) ;
					if(checkBox.isChecked())
						moHuYunFlag |= PinyinTree.MOHU_YUN_IN_ING ;
					checkBox=(CheckBox)setting.findViewById(R.id.uan_uang) ;
					if(checkBox.isChecked())
						moHuYunFlag |= PinyinTree.MOHU_YUN_UAN_UANG;
					checkBox=(CheckBox)setting.findViewById(R.id.ian_iang) ;
					if(checkBox.isChecked())
						moHuYunFlag |= PinyinTree.MOHU_YUN_IAN_IANG;
					PinyinTree.getInstance().setMoHuFlag(moHuShengFlag, moHuYunFlag);
//				}
			}
			public void onClick(DialogInterface dialog,int whichButton){
				
				boolean mTipsFlag ;
				SeekBar voice=(SeekBar)setting.findViewById(R.id.voice) ;
				volumn=voice.getProgress() ;
				
				this.updateMohu() ;
				RadioGroup radioGroup=(RadioGroup)setting.findViewById(R.id.tips_switch	);
				if(radioGroup.getCheckedRadioButtonId()==R.id.opentips){
					mTipsFlag=true ;
					setDrawingCacheEnabled(true) ;
					mChKeyboard.openTips() ;
				}
				else{
					mTipsFlag=false ;
					mChKeyboard.closeTips() ;
					setDrawingCacheEnabled(false) ;

				}
				
				SharedPreferences state=service.getSharedPreferences("settings", AeviouIME.MODE_PRIVATE) ;
				SharedPreferences.Editor editor=state.edit() ;
				editor.putBoolean("mTipsFlag", mTipsFlag) ;
				editor.putInt("mShengMoHuFlag", moHuShengFlag) ;
				editor.putInt("mYunMoHuFlag", moHuYunFlag) ;
				editor.putInt("Volumn", volumn) ;
				editor.commit() ;
			}
		}) ;
		setting.show() ;
	}
	private void initSounds() {   
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);   
		soundPoolMap = new HashMap<Integer, Integer>();   
		soundPoolMap.put(KEYAUDIO, soundPool.load(getContext(),   
	            R.raw.stardard, KEYAUDIO));   
		soundPoolMap.put(KEYWARNING, soundPool.load(getContext(),   
	            R.raw.warning	, KEYWARNING));   
		}   
	public void playSound(int sound) {   
	    soundPool.play(soundPoolMap.get(sound), (float)volumn/MAXVOLUME, (float)volumn/MAXVOLUME, 1,   
		            1, 1f);
		}   
}
