package tk.zhla.citsoft.pan.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpUtils {

	/**
	 * Õ¯¬Á∑√Œ 
	 * */
	public static String GetStringForHttpPost(CookieUtil cookieUtil,
			List<NameValuePair> list, String url, int size)
			throws ClientProtocolException, IOException {
		if(url.startsWith("https://")){
			return HttpsUtils.GetStringForHttpsPost(cookieUtil, list, url, size);
		}
		HttpURLConnection conn= null;
		DataOutputStream dos =null;
		try {
			URL url2 = new URL(url);
			conn = (HttpURLConnection) url2.openConnection();
			conn.setReadTimeout(5000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Cookie", "fuid=" + cookieUtil.getId()+ ";token=" + cookieUtil.getToken());
			conn.connect();
		dos = new DataOutputStream(conn.getOutputStream());
			StringBuffer buf = new StringBuffer();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					System.out.println(list.get(i).getValue());
					if(i==list.size()-1){
						buf.append(list.get(i).getName()+"="+URLEncoder.encode(list.get(i).getValue(),"utf-8"));
					}else {
						buf.append(list.get(i).getName()+"="+URLEncoder.encode(list.get(i).getValue(),"utf-8")+"&");
					}
				}
			}
			
			dos.write(buf.toString().getBytes("utf-8"));
			dos.flush();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				sb.append(temp);
			}
			return UTF2GBK.Unicode2GBK(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(dos!=null){
				dos.close();
			}
			if(conn!=null){
				
				conn.disconnect();
			}
			
		}
		Log.i("syso","citno"+url);
		return null;
	}



	/**
	 * Õ¯¬Á∑√Œ 
	 * */
	public static String GetStringForHttpGet(CookieUtil cookieUtil,
			List<NameValuePair> list, String ul, int size)
			throws ClientProtocolException, IOException {
		System.out.println(ul);
		if(ul.startsWith("https://")){
			return HttpsUtils.GetStringForHttpsGet(cookieUtil, list, ul, size);
		}

		if (list != null) {
			ul += "?";
			for (int j = 0; j < list.size(); j++) {
				NameValuePair pair = list.get(j);
				ul = ul + pair.getName() + "=" + pair.getValue();
				if (j < list.size() - 1) {
					ul += "&";
				}
			}

		}
		URL url = new URL(ul);
		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();
		urlConnection.setReadTimeout(5000);
		if(cookieUtil!=null)
		urlConnection.setRequestProperty("Cookie", "fuid=" + cookieUtil.getId()
				+ ";token=" + cookieUtil.getToken());
		try {
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				sb.append(temp);
			}
			Log.i("syso",sb.toString());
			return UTF2GBK.Unicode2GBK(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			urlConnection.disconnect();
		}

		return null;
	}

}
