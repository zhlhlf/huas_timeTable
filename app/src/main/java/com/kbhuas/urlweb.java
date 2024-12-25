package com.kbhuas;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import java.net.URL;
import android.net.Uri;
import android.os.Environment;
import android.content.Context;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.widget.TextView;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import android.webkit.WebView;
import android.webkit.WebSettings;

public class urlweb extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urlweb);
  		util.ct = this;	
		try {		
			util.exec("sed.sh");		
		} catch (Exception e) {}
		final WebView mWebView = findViewById(R.id.view);
		util.urlweb = mWebView;
		String url = "https://xyjw.huas.edu.cn/jsxsd/";
		mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
		mWebView.setWebViewClient(new mywebviewclient(this));
		
	}

}

