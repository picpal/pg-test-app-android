package com.example.pgdummyapp

import android.util.Log
//import com.example.pgdummyapp.BuildConfig



object DebugLog {
    const val TAG = "AppLog"
//
//    fun logDebugE(msg: String) {
//        if (BuildConfig.DEBUG) Log.e(TAG, getLogMsg(msg))
//    }
//
//    fun logDebugW(msg: String) {
//        if (BuildConfig.DEBUG) Log.w(TAG, getLogMsg(msg))
//    }
//
//    fun logDebugI(msg: String) {
//        if (BuildConfig.DEBUG) Log.i(TAG, getLogMsg(msg))
//    }
//
//    fun logDebugD(msg: String) {
//        if (BuildConfig.DEBUG) Log.d(TAG, getLogMsg(msg))
//    }
//
//    fun logDebugV(msg: String) {
//        if (BuildConfig.DEBUG) Log.v(TAG, getLogMsg(msg))
//    }

    private fun getLogMsg(msg: String): String {
        val ste = Thread.currentThread().stackTrace[4]
        val className = if (ste.fileName.endsWith(".kt"))
            ste.fileName.replace(".kt", "")
        else
            ste.fileName.replace(".java", "")
        return StringBuilder()
            .append("[")
            .append(className)
            .append("::")
            .append(ste.methodName)
            .append("] ")
            .append(msg).toString()
    }
}