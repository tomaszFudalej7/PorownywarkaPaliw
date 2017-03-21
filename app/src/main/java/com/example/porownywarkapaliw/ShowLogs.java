package com.example.porownywarkapaliw;

import android.util.Log;

/** @author Konstantyn
 * Created by Администратор on 14.03.2017.
 */

public  class ShowLogs {
    private static final String TAG = "App";

    public static boolean LOG_STATUS= true;

    public static void i(String logMessage) {
        Log.i(TAG," "+logMessage);
    }
}
