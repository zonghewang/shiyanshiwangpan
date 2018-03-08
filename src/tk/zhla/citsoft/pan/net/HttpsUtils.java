package tk.zhla.citsoft.pan.net;

import java.io.IOException;
import java.security.KeyStore;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpsUtils {
	/**
	 * 网络访问
	 * */
	public static String GetStringForHttpsGet(CookieUtil cookieUtil,
			List<NameValuePair> list, String url, int size)
			throws ClientProtocolException, IOException {
		if (url.startsWith("http://")) {
			return HttpUtils.GetStringForHttpGet(cookieUtil, list, url, size);
		}
		HttpGet request;
		HttpClient client;
		HttpResponse response;
		client = getNewHttpClient();
		HttpParams httpParams = client.getParams();
		// 连接 超时时间
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		// Socket 超时时间
		 HttpConnectionParams.setSoTimeout(httpParams, 7000);
		if (list != null) {
			url += "?";
			for (int j = 0; j < list.size(); j++) {
				NameValuePair pair = list.get(j);
				url = url + pair.getName() + "=" + pair.getValue();
				if (j < list.size() - 1) {
					url += "&";
				}
			}

		}
		request = new HttpGet(url);
		if (cookieUtil == null) {
			request.addHeader("Cookie", "fuid=305806304"
					+ ";token=SSHPRfJDKhaUMV8JP1Jlx3zAEgF77G");
		} else {
			request.addHeader("Cookie", "fuid=" + cookieUtil.getId()
					+ ";token=" + cookieUtil.getToken());
		}

		response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity()).trim();

			return UTF2GBK.Unicode2GBK(result);
		}
		if (size > 0) {
			return GetStringForHttpsGet(cookieUtil, list, url, size - 1);
		}
		return null;
	}

	/**
	 * 网络访问
	 * */
	public static String GetStringForHttpsPost(CookieUtil cookieUtil,
			List<NameValuePair> list, String url, int size)
			throws ClientProtocolException, IOException {
		if (url.startsWith("http://")) {
			return HttpUtils.GetStringForHttpPost(cookieUtil, list, url, size);
		}
		HttpPost request;
		HttpClient client;
		HttpResponse response;
		client = getNewHttpClient();
		HttpParams httpParams = client.getParams();
		// 连接 超时时间
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		// Socket 超时时间
		// HttpConnectionParams.setSoTimeout(httpParams, 7000);

		request = new HttpPost(url);
		if (cookieUtil == null) {
			request.addHeader("Cookie", "fuid=305806304"
					+ ";token=SSHPRfJDKhaUMV8JP1Jlx3zAEgF77G");
		} else {
			request.addHeader("Cookie", "fuid=" + cookieUtil.getId()
					+ ";token=" + cookieUtil.getToken());
		}
		if (list != null) {
			request.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
		}

		response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity()).trim();

			return UTF2GBK.Unicode2GBK(result);
		}
		if (size > 0) {
			return GetStringForHttpsPost(cookieUtil, list, url, size - 1);
		}
		return null;
	}

	/**
	 * 获取HttpClient 能够访问HTTPS和HTTP
	 * 
	 * @return
	 */
	private static HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactoryEx sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

}
