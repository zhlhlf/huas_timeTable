package com.huas.kb;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.net.URL;
import java.net.HttpURLConnection;
import org.json.JSONObject;
import org.json.JSONException;

import android.webkit.WebView;
import android.webkit.CookieManager;
import java.net.UnknownHostException;
import android.widget.TextView;
import java.util.List;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import java.text.ParseException;

public class util {
	public static WebView view;
    public static File file;
    public static File file2;
    public static Activity ct = null;

	public static String host = "https://xyjw.huas.edu.cn";
	//public static String host="http://172.26.0.175";

	public static String date;
	public static TextView sj;
	public static String msg;
	public static String s;
	public static void getKb(String date) {
		util.date = date;
		new Thread(){
			public void run() {
				HttpURLConnection ht = null;
				FileOutputStream of = null;

				byte sf[] = new byte[1000];
				try {
					shellutil.CommandResult result = shellutil.execCommand("rm -rf " + util.ct.getFilesDir().getPath() + "/*.html ", false);
					URL url = new URL(host + "/jsxsd/framework/main_index_loadkb.jsp?rq=" + util.date);
					ht = (HttpURLConnection) url.openConnection();
					ht.setRequestMethod("POST");
					ht.setRequestProperty("Cookie", getCookie());
					ht.setInstanceFollowRedirects(true);  
					ht.connect();

					of = new FileOutputStream(file);

					InputStream is = ht.getInputStream();
					int len;
					while ((len = is.read(sf)) != -1) {
						of.write(sf, 0, len);
					}

					of.close();
					ht.disconnect();
					result = shellutil.execCommand("sh " + util.ct.getFilesDir().getPath() + "/sed.sh " + util.date, false);
					//util.showDiag(result.errorMsg);
				} catch (Exception e) {
					showDiag(e.getClass().getName() +"  "+ e.getMessage());
					System.out.printf("getkb error : "+e.getMessage());
				}

				util.ct.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (file.exists()){
								view.loadUrl(util.file.getPath());
							}

							if(file2.exists()) {
								view.loadUrl(util.file2.getPath());
								util.showDiag("账号或密码错误！");
								kb.logout(null);
							}
							
						}
						
					
                });
			}
		}.start();

	}

	public static String getCookie() {
		String cookie = "";
		try {
			Map map = util.getMap();
			URL url = new URL(host + "/jsxsd/xk/LoginToXk");
			Map<String,String> data = new HashMap<>();
			data.put("encoded",(String)map.get("encode"));
			data.put("userAccount","zhlhlf");
			data.put("userPassword","zhlhlf");
			StringBuilder requestDataBuilder = new StringBuilder();
			for (Map.Entry<String, String> entry : data.entrySet()) {
				if (requestDataBuilder.length() != 0) {
					requestDataBuilder.append("&");
				}
				String key = entry.getKey();
				String value = entry.getValue();
				requestDataBuilder.append(key+"="+value);
			}
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// 设置请求方法和其他属性
			connection.setRequestMethod("POST");
			connection.setInstanceFollowRedirects(false);
			connection.setDoOutput(true); // 允许输出流，用于写入请求体
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			// 连接服务器
			connection.connect();
			System.out.println("登陆请求体: "+requestDataBuilder.toString());
			// 写入请求体
			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = requestDataBuilder.toString().getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}
			
			List<String> list = connection.getHeaderFields().get("Set-Cookie");
			if (list == null) throw new Exception("账号或密码错误！");
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).contains("JSE") == true || list.get(i).contains("SER") == true) {
					String hh = list.get(i).split(";")[0];
					hh += "; ";
					cookie += hh;
				}
			}
			connection.disconnect();
		} catch (Exception e) {
			showDiag(e.getClass().getName() + e.getMessage());
			System.out.println("get-cookie："+e.getMessage());
		}
		Log.d("huas-kb","cookie: " + cookie);

		return cookie;
	}
    public static void saveMap(Map<String,String> map) {
        SharedPreferences sp = ct.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor op =sp.edit();
        for (String key : map.keySet())	
			op.putString(key, map.get(key));
        op.commit();
    }
    public static void removeMap() {
        SharedPreferences sp = ct.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor op =sp.edit();
		op.clear();
       	op.commit();
    }

	public static void showDiag(String msg) {
		util.msg = msg;
		util.ct.runOnUiThread(new Runnable(){
				@Override
				public void run() {
					Toast.makeText(util.ct, util.msg, Toast.LENGTH_SHORT).show();
				}							
			});
	}

    public static Map<String,String> getMap() {
        SharedPreferences sp = ct.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Map map = sp.getAll();
		return map;
    }

	public static void exec(String filename) throws IOException {  
		File file = new File(ct.getFilesDir().getPath() + "/" + filename);
		if (file.exists()) return;
		InputStream myInput;  
		OutputStream myOutput = new FileOutputStream(file);  
		myInput = ct.getAssets().open(filename);
		byte[] buffer = new byte[1024];  
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length); 
			length = myInput.read(buffer);	
		}
		myInput.close();  
		myOutput.close();
		file.setExecutable(true);
//		shellutil.execCommand("chmod 777 "+file.getPath()+filename,false);
//		Toast.makeText(ct, file.getPath(), Toast.LENGTH_SHORT).show();
	}

	public static String getTime() {
		LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String r = currentDate.format(formatter);
   		return r;
	}

	public static String MapToJson(Map<String,String> m) {
		JSONObject j = new JSONObject();
		for (String key : m.keySet()) {
			try {
				j.append(key, m.get(key));
			} catch (JSONException e) {
				showDiag("json转换错误");
			}
		}	
		try {
			return j.toString(3);
		} catch (JSONException e) {}
		return null;
	}

	public static void requestPermissions() {
		String a[]={
			"android.permission.INTERNET",
		};
		ct.requestPermissions(a, 1);
	}

	public static void updateSj() {
		new Thread(){
			public void run() {
				try {
					URL u = new URL("https://v2.jinrishici.com/one.json");
					HttpURLConnection ht = (HttpURLConnection) u.openConnection();
					ht.setRequestMethod("GET");
					ht.connect();
					byte b[] = new byte[150];
					ht.getInputStream().read(b);
					s = new String(b);
					s = s.split("content\":\"")[1];
					s = s.split("\",\"popularity")[0];
					System.out.println(s);
					ht.disconnect();
				} catch (UnknownHostException e) {
					showDiag("木有联网啊~");		
				} catch (Exception e) {
					showDiag("更新诗句失败");
					System.out.printf("更新诗句失败: "+e.getMessage());
					//showDiag("更新诗句失败 请告诉zhlhlf修复！");
				}
				util.ct.runOnUiThread(new Runnable(){

						@Override
						public void run() {
							sj.setText(s);
							
							}
						
				} );
			}}.start();
	}
	
	public static String getWeek(String date){
		String week[]={"一","二","三","四","五","六","日",};
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date startDate = dateFormat.parse("2024-09-16 0:00:00");//开始时间 定义为星期一
			Date endDate = dateFormat.parse(date+" 0:00:00");//结束时间
			Long msNum = endDate.getTime()-startDate.getTime();//时间戳相差的毫秒数
			Long dayNum = msNum/(24*60*60*1000); //除以一天的毫秒数，得到相差天数
			int day = dayNum.intValue() % 7;
			if(day <0) day = 7+day;
			return week[day];
		} catch (ParseException e) {
			e.printStackTrace();
			return "error";
		}
	}
}
