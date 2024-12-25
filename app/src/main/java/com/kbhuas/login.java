package com.kbhuas;
import android.app.Activity;
import android.widget.EditText;
import android.os.Bundle;
import android.webkit.WebView;
import android.view.View;
import java.util.Map;
import android.content.Intent;
import java.util.HashMap;
import java.io.IOException;

public class login extends Activity { 
	static EditText edit;
	static WebView web;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		try {
			util.exec("sed.sh");
		} catch (IOException e) {}
        setContentView(R.layout.login);
		edit = findViewById(R.id.code);
		web = findViewById(R.id.view);
		web.getSettings().setJavaScriptEnabled(true);
		web.loadUrl("file:///android_asset/getencode.html");
	}	
	
	public static void login(View View){
		Map map = new HashMap<String,String>();
		String s = edit.getText().toString();
		if(!s.contains("あ")){
			util.showDiag("请输入正确的encode！");
			return;
		}
		String b[]= s.split("あ");
		map.put("username",b[0]);
		map.put("encode",b[1]);
		util.saveMap(map);
		Intent a = new Intent(util.ct, kb.class);
		a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);	
		util.ct.startActivity(a);
	}
        
}
