package com.aeviou.utils;

import android.util.Log;

public final class ALog {

	public static int d(String message) {
		return Log.d(AeviouConstants.LogTag, message);
	}

	public static int d(Boolean message) {
		return Log.d(AeviouConstants.LogTag, message + "");
	}

	public static int v(String message) {
		return Log.v(AeviouConstants.LogTag, message);
	}

	public static int e(String message) {
		return Log.e(AeviouConstants.LogTag, message);
	}

	public static int i(String message) {
		return Log.i(AeviouConstants.LogTag, message);
	}

	public static int w(String message) {
		return Log.w(AeviouConstants.LogTag, message);
	}

	public static void v(int size) {
		// TODO Auto-generated method stub
		v(String.valueOf(size));
	}

	public static void v(Object o) {
		// TODO Auto-generated method stub
		v(o.toString());
	}

}
