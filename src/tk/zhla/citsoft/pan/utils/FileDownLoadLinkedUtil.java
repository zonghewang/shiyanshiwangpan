package tk.zhla.citsoft.pan.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import tk.zhla.citsoft.pan.net.HttpUtils;
import tk.zhla.citsoft.pan.parse.entity.FileDownLoadLinkedEnitity;
import tk.zhla.citsoft.pan.share.ShareUtils;

import android.content.Context;



public class FileDownLoadLinkedUtil {

	public static FileDownLoadLinkedEnitity getFileLinked(Context context , int fid) {
		ShareUtils utils = new ShareUtils(context);
		String url = utils.getURL()+"/a1/index?ct=file&ac=url";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("fid",fid+""));
		
		FileDownLoadLinkedEnitity enitity = null;
		try {
			String result = HttpUtils.GetStringForHttpPost(utils.getCookieUtil() , list, url, 3);
			if(result == null){
				return null;
			}else {
				JSONObject jsonObject = new JSONObject(result);
				String u = jsonObject.getString("url");
				boolean state = jsonObject.getBoolean("state");
				String error = jsonObject.getString("error");
				String errno = jsonObject.getString("errno");
				enitity = new FileDownLoadLinkedEnitity();
				enitity.setUrl(u);
				enitity.setErrno(errno);
				enitity.setError(error);
				enitity.setState(state);
				return enitity;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return enitity;
	}

}
