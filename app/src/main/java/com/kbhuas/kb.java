package com.kbhuas;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import android.widget.Toast;
import android.content.Intent;
import java.util.Map;
import java.io.File;
import java.io.FileWriter;
import android.widget.EditText;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.util.Log;
import android.widget.TextView;
import java.util.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class kb extends Activity { 
	static EditText edit;
	static TextView WeekView;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kb);
		util.ct = this;
		new Thread(){
			public void run() {
				shellutil.execCommand("logcat >" + getFilesDir().getPath() + "/log.log", false);		
			}
		}.start();
		if (!util.getMap().containsKey("encode")) {
			logout(null);
		}
		util.view = findViewById(R.id.view);
		util.sj = findViewById(R.id.sj);
		edit = findViewById(R.id.date);
		edit.setText(util.getTime());
		WeekView = findViewById(R.id.week);
		updateWeek();
		util.view.getSettings().setDisplayZoomControls(true);
		util.view.getSettings().setSupportZoom(true);
		util.view.getSettings().setUseWideViewPort(true);
		util.view.getSettings().setJavaScriptEnabled(true);
		util.view.setInitialScale(8);
		util.file = new File(util.ct.getFilesDir().getPath() + "/" + edit.getText()  + ".html");
		util.file2 = new File(util.ct.getFilesDir().getPath() + "/" + edit.getText()  + "_no.html");
		if (!util.file.exists() || util.file2.exists()) {
			update(null);
		}
		util.view.loadUrl(util.file.getPath());				
		util.updateSj();
	}

	public static void update(View v) {
		if(util.getMap().size()==0) logout(null);
		util.file = new File(util.ct.getFilesDir().getPath() + "/" + edit.getText()  + ".html");
		util.file2 = new File(util.ct.getFilesDir().getPath() + "/" + edit.getText()  + "_no.html");
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
		shellutil.CommandResult result = shellutil.execCommand("rm -rf " + util.ct.getFilesDir().getPath() + "/*.html ", false);
		if (util.cm != null)
			util.cm.removeAllCookie();
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
