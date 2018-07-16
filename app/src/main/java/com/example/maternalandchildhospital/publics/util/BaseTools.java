package com.example.maternalandchildhospital.publics.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * @author ggl
 * 
 */
public class BaseTools {
	
	/** 获取屏幕的宽度 */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
}
