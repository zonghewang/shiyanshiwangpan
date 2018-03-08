package tk.zhla.citsoft.szt.util.fileisexist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import tk.zhla.citsoft.pan.net.HttpUtils;
import tk.zhla.citsoft.pan.share.ShareUtils;

import android.content.Context;



public class FileIisExistUtil {

	public static FileIsExistEntity run(Context context , int pid1, String name1) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		ShareUtils shareUtils = new ShareUtils(context);
		String url =shareUtils.getURL()+"/a1/index?ct=file&ac=check";
		list.add(new BasicNameValuePair("pid", pid1 + ""));
		list.add(new BasicNameValuePair("name", name1));
		FileIsExistEntity entity = new FileIsExistEntity();
		try {
			String result = HttpUtils.GetStringForHttpPost(shareUtils.getCookieUtil(), list, url,
					3);
			if (result == null) {
				return null;
			} else {
System.out.println(result);
				JSONObject jsonObject = new JSONObject(result);
				entity = new FileIsExistEntity();
				entity.setState(jsonObject.getBoolean("state"));
				entity.setError(jsonObject.getString("error"));
				entity.setErrno(jsonObject.getString("errno"));
				if (jsonObject.getBoolean("state")) {
					entity.setType(jsonObject.getString("type"));
				} else {
					entity.setType(null);
				}
				return entity ;

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return entity;
	}

	

}
