package com.huas.kb;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;

import java.io.File;

import android.os.Environment;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class kb extends Activity { 
	static EditText edit;
	static TextView WeekView;
	public static String name;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kb);
		util.ct = this;
		name = getFilesDir().getPath(); //数据私有目录
        if(false){
	    new Thread(){
			public void run() {
				shellutil.execCommand("logcat >" + getFilesDir().getPath() + "/log.log", false);
			}
		}.start();
        }
	
	
		if (util.getMap().isEmpty()) {
			logout(null);
			return;
		}
		util.view = findViewById(R.id.view);
		util.sj = findViewById(R.id.sj);
		edit = findViewById(R.id.date);
		edit.setText(util.getTime());
		WeekView = findViewById(R.id.week);

		updateWeek();
		util.updateSj();

		util.view.getSettings().setDisplayZoomControls(true);
		util.view.getSettings().setSupportZoom(true);
		util.view.getSettings().setUseWideViewPort(true);
		util.view.getSettings().setJavaScriptEnabled(true);
		util.view.setInitialScale(8);

		//String name = util.ct.getFilesDir().getPath() + "/" + edit.getText();
		String name = "file://" + Environment.getExternalStorageDirectory().getPath() + "/kb/";
		util.file = new File(name  + ".html");
		util.file2 = new File(name  + "_no.html");

		if (!util.file.exists() || util.file2.exists()) {
			update(null);
		}
		util.view.loadUrl(util.file.getPath());
	}

	public static void update(View v) {
		if(util.getMap().size()==0) logout(null);
		util.file = new File(name + "/" + edit.getText()  + ".html");
		util.file2 = new File(name + "/" + edit.getText()  + "_no.html");
		util.getKb(edit.getText().toString());
		util.updateSj();
		updateWeek();
	}

	public static void next(View v){
		LocalDate currentDate = LocalDate.parse(edit.getText().toString());
		currentDate = currentDate.plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String r = currentDate.format(formatter);
		edit.setText(r);
		update(null);
   	}
	public static void last(View v){
		LocalDate currentDate = LocalDate.parse(edit.getText().toString());
		currentDate = currentDate.plusDays(-7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String r = currentDate.format(formatter);
		edit.setText(r);
		update(null);
   	}
	public static void logout(View v) {
		shellutil.execCommand("rm -rf " + util.ct.getFilesDir().getPath() + "/*.html ", false);

		util.removeMap();
		Intent a = new Intent(util.ct, login.class);
		a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		util.ct.finish();
		util.ct.startActivity(a);
	}
	
	public static void updateWeek(){
		String s = util.getWeek(edit.getText().toString());
		WeekView.setText("星期"+s);
	}
} 
