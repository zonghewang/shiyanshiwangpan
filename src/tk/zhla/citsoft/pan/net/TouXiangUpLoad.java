package tk.zhla.citsoft.pan.net;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import tk.zhla.citsoft.pan.share.ShareUtils;

import android.content.Context;
import android.util.Log;


public class TouXiangUpLoad {

	/* 上传文件至Server的方法 */
	public static void uploadBigFileMethod(File bitmap, ShareUtils su,
			Context context) {
		String actionUrl =  su.getURL()
				+ "/a1/uhead";
		if(actionUrl.startsWith("https://")){
			 TouXiangUpLoadWithHttps.uploadBigFileMethod(bitmap, su, context);
			 return;
		}
		/*
		 * //https用 TrustManager[] trustAllCerts = new TrustManager[] { new
		 * X509TrustManager() { public java.security.cert.X509Certificate[]
		 * getAcceptedIssuers() { return new
		 * java.security.cert.X509Certificate[] {}; }
		 * 
		 * public void checkClientTrusted( java.security.cert.X509Certificate[]
		 * chain, String authType) throws
		 * java.security.cert.CertificateException { }
		 * 
		 * public void checkServerTrusted( java.security.cert.X509Certificate[]
		 * chain, String authType) throws
		 * java.security.cert.CertificateException { } } };
		 * 
		 * SSLContext context = null; try { context =
		 * SSLContext.getInstance("TLS"); context.init(null, trustAllCerts, new
		 * java.security.SecureRandom()); HttpsURLConnection
		 * .setDefaultHostnameVerifier(new myHostnameVerifier());
		 * HttpsURLConnection.setDefaultSSLSocketFactory(context
		 * .getSocketFactory()); } catch (NoSuchAlgorithmException e1) {
		 * e1.printStackTrace(); } catch (KeyManagementException e) {
		 * e.printStackTrace(); }
		 */
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "WebKitFormBoundaryLL0Bluv332wLOSam";
		HttpURLConnection con = null;
		try {
			// System.setProperty("http.keepAlive", "false");
			URL url = new URL(actionUrl);
			con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			
			con.setDoInput(true);
			// con.setChunkedStreamingMode(1024*1024);
			con.setDoOutput(true);
			// con.setRequestProperty("Connection", "close");
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
				con.setRequestProperty("Connection", "keep-live");
			// con.setFixedLengthStreamingMode(1024*1024*1024);
			// con.setRequestProperty( "Accept-Encoding", "" );
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			con.setRequestProperty("Cookie", "fuid=" + su.getLoginEntity().getId()
					+ "; token=" + su.getLoginEntity().getToken() + "");
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());

			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"Filedata\"; filename=\"1.jgp\"" + end);
			ds.writeBytes("Content-Type: image/jpeg" + end);
			ds.writeBytes(end);

			// String postContent = URLEncoder.encode("aid", "UTF-8")
			// + "=" + URLEncoder.encode(aid+"", "UTF-8") + "&" +
			// URLEncoder.encode("pid", "UTF-8") + "="+
			// URLEncoder.encode(pid+"", "UTF-8")+ "&" +
			// URLEncoder.encode("name", "UTF-8") + "="+
			// URLEncoder.encode(file.getName(), "UTF-8") +"&" +
			// URLEncoder.encode("Filedata", "UTF-8") + "="+
			// URLEncoder.encode(file.getName(), "UTF-8") ;
			// System.out.println(postContent);
			// byte[] bypes = postContent.toString().getBytes();
			// ds.write(bypes);//
			/* 取得文件的FileInputStream */
			// FileInputStream fStream = new FileInputStream(uploadFile);
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			// 前面 startPosition个头字节读取不上传
			// for (int i = 0; i < startPosition / bufferSize; i++) {
			// fStream.read(buffer);
			// }
			// if (startPosition % bufferSize != 0) {
			// byte[] b = new byte[(int) (startPosition % bufferSize)];
			// fStream.read(b);
			// // }
			// File file = StorageUtils.getCacheDirectory(context);
			// File img = new File(file.getAbsolutePath()+"/1.kkk");
			// FileOutputStream stream = new FileOutputStream(img);
			// boolean flag = bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
			// stream);
			// // bitmap.recycle();
			// stream.close();
			// System.out.println(flag);

			FileInputStream input = new FileInputStream(bitmap);

			/* 从文件读取数据至缓冲区 */
			while ((length = input.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}

			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			ds.flush();
			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			// int ch;
			// System.out.println("goood");
			StringBuffer b = new StringBuffer();
			// while ((ch = is.read()) != -1) {
			// b.append((char) ch);
			// }

			byte[] data = new byte[1024];
			byte[] t = new byte[1024];
			int sum = 0;
			int temp;
			while ((temp = bis.read(t)) != -1) {

				System.arraycopy(t, 0, data, sum, temp);
				sum += temp;
			}

			// entity2return =
			// PrepareFileUploadUtil.parse(UTF2GBK.Unicode2GBK(b.toString()));
			/* 将Response显示于Dialog */

			/* 关闭DataOutputStream */

			ds.close();
			is.close();
			input.close();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}

}
