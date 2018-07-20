package com.example.maternalandchildhospital.publics.util;

import android.util.Log;
import android.widget.Toast;

import com.example.maternalandchildhospital.ECApplication;

/**
 * Created by yy on 2017/9/25.
 */
public class PopMessageUtil {
    /**********************************************************************************************
     * * 功能说明：Toast
     **********************************************************************************************/
    private static Toast mToast;

    public static void showToastLong(String EnglishMessage) {
        showToast(EnglishMessage, Toast.LENGTH_LONG);
    }

    public static void showToastShort(String EnglishMessage) {
            showToast(EnglishMessage, Toast.LENGTH_SHORT);
    }

    private static void showToast(String text, int time) {
        if (mToast == null)
            mToast = Toast.makeText(ECApplication.getInstance(), text, time);
        else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**********************************************************************************************
     * * 功能说明：Log.e
     **********************************************************************************************/
    public static void Log(String context) {
        Log.e("YY", context);
    }
}
