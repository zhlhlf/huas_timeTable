package com.kbhuas;
import android.webkit.WebViewClient;
import android.webkit.WebView;
import android.webkit.CookieManager;
import android.util.Log;
import java.util.Map;
import java.util.HashMap;
import android.content.Intent;
import android.app.Activity;
import java.io.File;
import android.widget.Toast;

public class mywebviewclient extends WebViewClient {
	Activity actity;
	String Tourl ="https://xyjw.huas.edu.cn/jsxsd/framework/xsMain.jsp";
	public mywebviewclient(Activity actity) {
		this.actity = actity;
	}

	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		if (url.equals(Tourl)) {
			shellutil.CommandResult result = shellutil.execCommand("rm -rf " + util.ct.getFilesDir().getPath() + "/*.html ", false);
			CookieManager cookieManager = CookieManager.getInstance();
			String CookieStr = cookieManager.getCookie(url);
			Map map1 = new HashMap<String,String>();
			map1.put("cookie", CookieStr);
			util.saveMap(map1);
			actity.finish();
			Intent a = new Intent(actity, kb.class);
			a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			actity.startActivity(a);
		}
	}

}

