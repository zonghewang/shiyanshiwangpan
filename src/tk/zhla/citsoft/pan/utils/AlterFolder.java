package tk.zhla.citsoft.pan.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import tk.zhla.citsoft.pan.parse.entity.AlterFolderEntity;
import tk.zhla.citsoft.pan.share.ShareUtils;

import android.content.Context;



public class AlterFolder {

	public static AlterFolderEntity down(Context context,int fid, String file_name, String file_desc) {
		ShareUtils sp = new ShareUtils(context);
		String url = sp.getURL()+"/a1/index?ct=dir&ac=edit";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		AlterFolderEntity entity=null;
		list.add(new BasicNameValuePair("fid", fid + ""));
		list.add(new BasicNameValuePair("file_name", file_name));
//		list.add(new BasicNameValuePair("file_desc", file_desc));
		String result = null;
		try {
			result = tk.zhla.citsoft.pan.net.HttpUtils.GetStringForHttpPost(sp.getCookieUtil(), list, url, 3);
			System.out.println("222" + result);
			if (result != null) {

				JSONObject jsonObject = new JSONObject(result);
				boolean state = jsonObject.getBoolean("state");
				String error = jsonObject.getString("error");
				String errno = jsonObject.getString("errno");
				String file_name2 = jsonObject.getString("file_name");

				entity = new AlterFolderEntity(state, error, errno, file_name2);
				return entity; 
			} else {
				return null; 
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
