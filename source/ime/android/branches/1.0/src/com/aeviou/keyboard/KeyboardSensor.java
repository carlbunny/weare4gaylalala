//package com.aeviou.keyboard;
//
//
//import android.content.Context;
//import android.graphics.Matrix;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.aeviou.AeviouInputMethodService;
//import com.aeviou.utils.AeviouConstants;
//import com.aeviou.utils.MatrixUtil;
//
//public class KeyboardSensor {
//	AeviouInputMethodService imService;
//
//	// for rotate the keyboard
//	private SensorManager sensorMgr;
//
//	public Matrix[] finalMatrixArr = new Matrix[3];
//	public Matrix[] finalMatrixInvArr = new Matrix[3];
//
//	SensorEventListener lsn;
//	Sensor sensorAccelerometer;
//	Sensor sensorMagnetic;
//	boolean enableTile;
//
//	public KeyboardSensor(AeviouInputMethodService _imService) {
//		imService = _imService;
//		sensorMgr = (SensorManager) imService
//				.getSystemService(Context.SENSOR_SERVICE);
//
//		sensorAccelerometer = sensorMgr
//				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//		sensorMagnetic = sensorMgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//		
//		if(sensorAccelerometer==null||sensorMagnetic==null){
//			
//			if(sensorAccelerometer==null){
//				Toast.makeText(imService.getApplicationContext(),"don`t have sensorAccelerometer",Toast.LENGTH_SHORT).show();
//			}
//			if(sensorMagnetic==null){
//				Toast.makeText(imService.getApplicationContext(),"don`t have sensorAccelerometer",Toast.LENGTH_SHORT).show();
//			}
//			Toast.makeText(imService.getApplicationContext(),"zoom disabled",Toast.LENGTH_SHORT).show();
//			return;
//		}
//
//		// Log.v("aeviou", "List Num:"+sensorList.size());
//
//		for (int i = 0; i < 3; i++) {
//			finalMatrixArr[i] = new Matrix();
//			finalMatrixInvArr[i] = new Matrix();
//		}
//		//Log.v("aeviou","init");
//		//updateMatrix();
//		
//		lsn = new SensorEventListener() {
//			private static final float NS2S = 1.0f / 1000000000.0f;
//			private static final float rollSumThreshold = 20f;
//			private static final float rollSpeedThreshold = 120f;
//			private static final float rollMinThreshold = 5f;
//			
//			private long timestamp = 0;
//			private float rollSum = 0;
//			private int posMatrix = 0;// 0=left,1=center,2=right;
//			private boolean changed = false; // for debounce
//			private float lastV=0; //first order speed calculation
//
//			float[] accelerometerValues = new float[3];
//			float[] magneticFieldValues = new float[3];
//			float[] orientionvalues = new float[3];
//			float[] rotateMatrix = new float[9];
//			float[] preRotateMatrix = {1,0,0,0,1,0,0,0,1};
//			int reverseCount=0;//for count debounce
//			private static final int reverseCountThreshold = 5;
//			
//			public void onSensorChanged(SensorEvent e) {
//				if(enableTile==false)return;
//				if (e.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//					magneticFieldValues = e.values;
//					return;
//				} else if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
//					accelerometerValues = e.values;
//
//				SensorManager.getRotationMatrix(rotateMatrix, null,
//						accelerometerValues, magneticFieldValues);
//				SensorManager.getAngleChange(orientionvalues,rotateMatrix,preRotateMatrix);
//				System.arraycopy(rotateMatrix,0,preRotateMatrix,0,9);
//				
//				if (timestamp != 0) {// check for inital state
//					double dT = (e.timestamp - timestamp) * NS2S;
//					float dRoll = (float) Math.toDegrees(orientionvalues[2]);
//					if (Math.abs(dRoll) > rollMinThreshold) {// Debounce
//						//float RollSpeed = (float) (dRoll / dT);//zero order
//						float RollSpeed = (float) (2*dRoll / dT-lastV);//1st order
//						lastV=RollSpeed;
//						if (RollSpeed >rollSpeedThreshold) {// only one right
//							if(reverseCount>0)reverseCount--;
//							// rotate move
//							rollSum += dRoll;
//////							Log.v("aeviou", "rollSum:" + rollSum + "Speed:"
//////							+ RollSpeed+" "+(e.timestamp - timestamp));
//							if (rollSum >rollSumThreshold) {
//								if(changed == false){//deal 2 recall in one move
//									if (posMatrix == 0)
//										posMatrix = 2;
//									else if (posMatrix == 2)
//										posMatrix = 0;
//									tiltKeyBoard(posMatrix);
//									changed=true;
////									Log.v("aeviou", "Change!!!!!");
//									reverseCount=0;
//								}
//							}
//						} else if (RollSpeed <0){ //for left move
//							if(reverseCount>=reverseCountThreshold) {
//								changed = false;
//								rollSum = 0;
//								lastV=0;
//								reverseCount=0;
//							}else {
//								reverseCount++;
//							}
//						}
//					}else{
//						rollSum=0;
//						lastV=0;
//						changed = false;
//					}
//				}
//				timestamp = e.timestamp;
//			}
//
//			public void onAccuracyChanged(Sensor s, int accuracy) {
//			}
//		};
//
//		toggleRegister();
//		
//	}
//	
//	public void updateMatrix() {
//		// TODO Auto-generated method stub
//		final float valueArr[] = { 0.999f, 1.0f, 0.999f };
//		float scaleArr[] = { 2.0f, 1.0f, 2.0f };
//		final float[] rotatesMatrix = new float[] { 1, 0, 0, 0, 1, 0, 0, 0, 1 };
//		Matrix rotateM=new Matrix();
//		
//		AeviouKeyboardView inputView = imService.inputView;
//		
//		// left
//		float cosTheta = valueArr[0];
//		float sinTheta = (float) Math.sqrt(1 - cosTheta * cosTheta);
//		rotatesMatrix[0] = cosTheta * cosTheta;
//		rotatesMatrix[4] = 1 + 100 * sinTheta * sinTheta;
//		rotatesMatrix[6] = sinTheta * sinTheta;
//		
//	
//		//cal scale
//		float[] point={AeviouConstants.SCREEN_WIDTH,0,0};
//		rotateM.setValues(rotatesMatrix);
//		rotateM.mapPoints(point);
//		float scale=AeviouConstants.SCREEN_WIDTH/point[0];
//		scaleArr[0]=scale;scaleArr[2]=scale;
//		
//		//Log.v("aeviou", "scale:" + scale);
//		
//		MatrixUtil
//				.mul(rotatesMatrix, inputView.canvasMatrix, finalMatrixArr[0]);
//
//		
//		finalMatrixArr[0].postScale(scaleArr[0], 1);
//		finalMatrixArr[0].invert(finalMatrixInvArr[0]);
//		// center
//		finalMatrixArr[1] = inputView.canvasMatrix;
//		finalMatrixArr[1].invert(finalMatrixInvArr[1]);
//		// right
//		Matrix canvasMatrixLeft = new Matrix(inputView.canvasMatrix);
//		canvasMatrixLeft.postTranslate(-AeviouConstants.SCREEN_WIDTH, 0);
//
//		cosTheta = valueArr[2];
//		sinTheta = (float) Math.sqrt(1 - cosTheta * cosTheta);
//		rotatesMatrix[0] = cosTheta * cosTheta;
//		rotatesMatrix[4] = 1 + 100 * sinTheta * sinTheta;
//		rotatesMatrix[6] = -sinTheta * sinTheta;
//		
////		rotatesMatrix[0] = cosTheta;
////		rotatesMatrix[4] = 1 + sinTheta;
////		rotatesMatrix[6] = -sinTheta;
//		
//		MatrixUtil
//				.mul(rotatesMatrix, canvasMatrixLeft, finalMatrixArr[2]);
//		finalMatrixArr[2].postScale(scaleArr[2], 1);
//		finalMatrixArr[2].postTranslate(AeviouConstants.SCREEN_WIDTH, 0);
//		finalMatrixArr[2].invert(finalMatrixInvArr[2]);
//	}
//
//	public void toggleRegister() {
//		enableTile=AeviouConstants.enableZoom;
//		if (AeviouConstants.enableZoom) {
//			// 注册listener，第三个参数是检测的精确度
//
//			sensorMgr.registerListener(lsn, sensorAccelerometer,
//			SensorManager.SENSOR_DELAY_GAME);
//			sensorMgr.registerListener(lsn, sensorMagnetic,
//					SensorManager.SENSOR_DELAY_NORMAL);
//		} else {
//			//seen to be a bug,must unregister the sensor one by one
//			sensorMgr.unregisterListener(lsn,sensorAccelerometer);
//			sensorMgr.unregisterListener(lsn,sensorMagnetic);
//			// reset the finalMatrix
//			Log.v("aeviou", "stop!!!!!");
//			tiltKeyBoard(1);
//		}
//	}
//
//	void tiltKeyBoard(int pos) {
//		AeviouKeyboardView inputView = imService.inputView;
//		if(inputView==null)return;
//		//Log.v("aeviou",finalMatrixArr.toString());
//		inputView.finalMatrix = finalMatrixArr[pos];
//		inputView.canvasMatrixInverse = finalMatrixInvArr[pos];
//		inputView.invalidate();
//	}
//}
