package com.huas.kb;
import android.app.Activity;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.os.Bundle;
import android.webkit.WebView;
import android.view.View;
import java.util.Map;
import android.content.Intent;


import java.util.HashMap;
import java.io.IOException;
import android.view.View.OnClickListener;

public class login extends Activity {
	private EditText username;
	private EditText password;
	private WebView web;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		try {
			util.exec("sed.sh");
		} catch (IOException e) {}

		ToastUtil.ac = this;

        setContentView(R.layout.login);
		username = findViewById(R.id.username);
		password = findViewById(R.id.password);

		web = new WebView(this);
		web.getSettings().setJavaScriptEnabled(true);
		web.setWebViewClient(new WebViewClient());
		web.setWebChromeClient(new WebChromeClient());
		web.loadUrl("file:///android_asset/getencode.html");

		findViewById(R.id.login).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					final String user = username.getText().toString();
					String pass = password.getText().toString();
					if(user.isEmpty() || pass.isEmpty()) {
						ToastUtil.show("账号或密码不能为空！");
						return;
					}

					String script = "z(\""+user+"\", \""+pass+"\");";

					// 使用 evaluateJavascript 方法执行 JavaScript 并获取返回值
					web.evaluateJavascript(script, new ValueCallback<String>(){

							@Override
							public void onReceiveValue(String value) {
								value = value.replace("\"","");
								Map<String,String> map = new HashMap<>();
								map.put("username",user);
								map.put("encode",value);
								util.saveMap(map);
								Log.d("huas-kb","encode" + value);
								Intent a = new Intent(util.ct,kb.class);
								a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								util.ct.startActivity(a);
								
							}
							
					});
					
				}
				
			
		});


	}	

}
