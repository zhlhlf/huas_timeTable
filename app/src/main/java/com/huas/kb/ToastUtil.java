package com.huas.kb;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtil {
    public static Activity ac;
    public static Handler mainHandler = new Handler(Looper.getMainLooper()); // 主线程 Handler

    public static void show(final CharSequence msg){
        if(msg != null)
			ac.runOnUiThread(new Runnable(){
					@Override
					public void run() {
						Toast.makeText(ac,msg,Toast.LENGTH_SHORT).show();
					}
				});
    }

 
    public static void show(final Activity ac,final CharSequence msg){
		if(msg != null)
			ac.runOnUiThread(new Runnable(){
					@Override
					public void run() {
						Toast.makeText(ac,msg,Toast.LENGTH_SHORT).show();
					}
				});
    }

    public static void init(Activity ac){
        ToastUtil.ac = ac;
    }

    public static Context getContext() {
        return ToastUtil.ac;
    }
}
