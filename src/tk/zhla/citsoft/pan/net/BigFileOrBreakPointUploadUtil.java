package tk.zhla.citsoft.pan.net;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import tk.zhla.citsoft.pan.db.DBManager;
import tk.zhla.citsoft.pan.parse.entity.FileDataDBEntity;
import tk.zhla.citsoft.pan.parse.entity.PrepareFileEntity;
import tk.zhla.citsoft.pan.share.ShareUtils;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;


public class BigFileOrBreakPointUploadUtil {
	
	
	private static int sizeK = 1024;
	

	/* 上传文件至Server的方法 */
	public static  PrepareFileEntity uploadBigFileMethod(String fuid, String token,
			String uploadFile, long size, long startPosition, int pid, int aid,int sizeK1,Context context) {
		PrepareFileEntity entity2return = null;
		ShareUtils utils = new ShareUtils(context);
		String actionUrl = utils.getURL()+"/a1/resumeUpload?";
		if(actionUrl.startsWith("https://")){
			return BigFileOrBreakPointUploadUtilWithHttps.uploadBigFileMethod(fuid, token, uploadFile, size, startPosition, pid, aid, sizeK, context);
		}
		System.out.println("uploadBigFileMethod-1");
	/*	//https用
	 * TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {
			}
		} };

		SSLContext context = null;
		try {
			context = SSLContext.getInstance("TLS");
			context.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultHostnameVerifier(new myHostnameVerifier());
			HttpsURLConnection.setDefaultSSLSocketFactory(context
					.getSocketFactory());
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}*/
//		System.setProperty("http.keepAlive", "false");  
		File file = new File(uploadFile);
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		HttpURLConnection con = null;
		long uptime = System.currentTimeMillis();
		try {
			
			URL url = new URL(actionUrl + "sha1="
					+ MakeFileHash.getFileSHA1(uploadFile) + "&size=" + size
					+ "");
			 con = (HttpURLConnection) url.openConnection();
			 
			/* 允许Input、Output，不使用Cache */
			System.out.println("uploadBigFileMethod-3");
			con.setDoInput(true);
			con.setChunkedStreamingMode(0); 
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "keep-live");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			con.setRequestProperty("Cookie", "fuid=" + fuid + "; token="
					+ token + "");
//			con.setRequestProperty("name", file.getName());
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			System.out.println("uploadBigFileMethod-4");
			
		
			
			
		
		
//			StringBuffer b = new StringBuffer();
//			String[] ss = file.getName().split("\\.");
//			for(int i=0;i<ss.length;i++){
//				if(i==ss.length-1){
//					b.append(URLEncoder.encode(ss[i],"utf-8"));
//				}else {
//					b.append(URLEncoder.encode(ss[i],"utf-8")+".");
//				}
//			}
//			if(file.getName().startsWith(".")||file.getName().endsWith(".")){
//				b = file.getName();
//			}
			
//			ds.writeBytes(twoHyphens + boundary + end); 
//			ds.writeBytes("Content-Disposition: form-data; " + "name=\"aid\""
//					+ end + end + URLEncoder.encode(aid+"", "utf-8") + end);
//			ds.writeBytes(twoHyphens + boundary + end);
//			ds.writeBytes("Content-Disposition: form-data; " + "name=\"pid\""
//					+ end + end + URLEncoder.encode(pid+"", "utf-8")  + end);
//			ds.writeBytes(twoHyphens + boundary + end);
//			ds.writeBytes("Content-Disposition: form-data; " + "name=\"name\""
//					+ end + end + URLEncoder.encode(file.getName(), "utf-8")+ end);
//			ds.writeBytes(twoHyphens + boundary + end);
//			ds.writeBytes("Content-Disposition: form-data; "
//					+ "name=\"Filedata\";filename=\"" +file.getName() + "\""
//					+ end);
//			ds.writeBytes(end);
			
			StringBuffer buf = new StringBuffer();
			buf.append(twoHyphens + boundary + end);
			buf.append("Content-Disposition: form-data; " + "name=\"aid\""
					+ end + end + aid + end);
			buf.append(twoHyphens + boundary + end);
			buf.append("Content-Disposition: form-data; " + "name=\"pid\""
					+ end + end + pid + end);
			buf.append(twoHyphens + boundary + end);
			buf.append("Content-Disposition: form-data; " + "name=\"name\""
					+ end + end + file.getName()+ end);
			buf.append(twoHyphens + boundary + end);
			buf.append("Content-Disposition: form-data; "
					+ "name=\"Filedata\";filename=\"" +file.getName() + "\""
					+ end);
			buf.append(end);
			ds.write(buf.toString().getBytes());
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
			//FileInputStream fStream = new FileInputStream(uploadFile);
			RandomAccessFile fStream = new RandomAccessFile(uploadFile, "r");
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			System.out.println("uploadBigFileMethod-5");
			// 前面 startPosition个头字节读取不上传
//			for (int i = 0; i < startPosition / bufferSize; i++) {
//				fStream.read(buffer);
//			}
//			if (startPosition % bufferSize != 0) {
//				byte[] b = new byte[(int) (startPosition % bufferSize)];
//				fStream.read(b);
//			}
			fStream.seek(startPosition);
			Log.e("aa", startPosition+"");
		
			int sum = 0;
			/* 从文件读取数据至缓冲区 */
			while (sum < sizeK && (length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
				sum++;
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			Log.v("wang",UTF2GBK.Unicode2GBK(b.toString()));
			entity2return = PrepareFileUploadUtil.parse(UTF2GBK.Unicode2GBK(b.toString()));
			long usedTime = System.currentTimeMillis()-uptime;
			sizeK = (int) (5000*sizeK/usedTime);
			
			/* 将Response显示于Dialog */

			/* 关闭DataOutputStream */
			 
			ds.close();
			is.close();
			return entity2return;
		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			if(con!=null){
				con.disconnect();
			}
		}
		return entity2return;
	}

	private boolean flag = true;

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	
	public boolean uploadBigFile(int fileDowningId,String file, int pid, int aid, String fuid,
			String token, ProgressBar pb,Context context) {
		
		
		
		
		if(!NetworkUtils.isNetworkAvailable(context)){
			return false;
		}
		File file2 = new File(file);
		if (flag) {
			System.out.println("开始上传喽");
			String sha1 = MakeFileHash.getFileSHA1(file);
			long size = file2.length();
			
			if(pb!=null)
			pb.setMax(100);
			
			long start = getUploadedFileSize(fileDowningId,pid,file2, sha1,context);
			System.out.println(start);
			if (start == -2) {
				// 网络异常
				errorNum = 0;
				return false;
			} else if (start == -1) {
				// 上传已经成功
				if(pb!=null)
				pb.setProgress(100);
				errorNum = 0;
				System.out.println("上传成功");
				
				return true;
			} else if(start==-3){
				if(pb!=null)
					pb.setProgress((int)(start/size*100));
//					
//					PrepareFileEntity entity = BigFileOrBreakPointUploadUtil.uploadBigFileMethod(fuid,
//							token, file, file2.length(),
//							0, pid, 1,1,context);
//					System.out.println("妙传");
//					if(entity!=null){
//					uploadBigFile(fileDowningId,file, pid, aid, fuid,
//							token, pb,context);
//					SQLUtils su = new SQLUtils(context);
//					// 删除upLoading表中记录
//					su.deleteUserUpingFile(fileDowningId);
//					// 添加upLoaded表中记录
//					UserLocalFile localFile = new UserLocalFile();
//					try{
//						localFile.setAid(entity.getData().getAid());
//					}catch (Exception e){
//						
//					}
//					try {
//						localFile.setFid(entity.getData().getFid());
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//					}
//					try {
//						localFile.setPid(pid);
//					} catch (Exception e) {
//					}
//					try {
//						localFile.setM(entity.getData().getM());
//						localFile.setN(file2.getName());
//						localFile.setPc(entity.getData().getPc());
//						localFile.setS(entity.getData().getS());
//						localFile.setT(entity.getData().getT());
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//					}
//					try {
//						localFile.setPath(file);
//					} catch (Exception e) {
//					}
//					su.addUserUpFile(localFile);
//					
//					// 添加数据到local中
////					su.addUserLocalFile(localFile);
				
//					}
//					}else {
//						 uploadBigFile(fileDowningId, file, pid, aid, fuid, token, pb, context);
//					}
					// index+1继续递归
					
			}else {
				
				if(pb!=null)
				pb.setProgress((int)(start/size*100));
				PrepareFileEntity entity =  BigFileOrBreakPointUploadUtil.uploadBigFileMethod(fuid,
						token, file, file2.length(),
						start, pid, 1,1024,context);
				if(entity==null){
					// 删除upLoading表中记录
				//	su.deleteUserUpingFile(fileDowningId);
					errorNum++;
					if(errorNum==3){
						errorNum=0;
						return true;
					}
				}else {
					if(errorNum!=0){
						errorNum=0;
					}
				}
				file2 =null;
				Log.e("kk", "go");
				uploadBigFile(fileDowningId,file, pid, aid, fuid, token, pb,context);
			}
		}
		return true;
	}
	
	private static int errorNum = 0;

	/**
	 * 
	 * @param file2
	 * @param sha1
	 * @return -1 已经上传完成 0 表示没有上传 -2 网络有问题 其他 已经上传的数据大小
	 * id 为文件在downing 中的id
	 */
	private long getUploadedFileSize(int id,int pid,File file2, String sha1,Context context) {
		
		long size = file2.length();
		PrepareFileEntity entity = PrepareFileUploadUtil.getPrepareFileEntity(
				sha1, size, file2.getName(), pid,context);
		if (entity != null) {
			if (entity.getData() == null) {
				return 0;
			}
			String us = entity.getData().getUs();
			String s = entity.getData().getS();
			if (s != null && s.equals(us)) {
				// 说明网上有这个文件
//					
//					return -3; 
//				} else {
					DBManager su = new DBManager(context);
					su.open();
					// 删除upLoading表中记录
					su.deleteUpLoadingFile(entity.getData().getFid());
					// 添加upLoaded表中记录
					FileDataDBEntity localFile = new FileDataDBEntity();
					localFile.setAid(entity.getData().getAid());
					System.out.println(entity.getData().getFid()+"fid-------------------------------------+++++++++++++++++++++++");
					localFile.setFid(entity.getData().getFid());
					localFile.setPid(pid);
				
					localFile.setN(file2.getName());
					localFile.setPc(entity.getData().getPc());
				
					localFile.setT(entity.getData().getT());
					localFile.setPath(file2.getAbsolutePath());
					su.addUpLoadedFile(localFile);
					su.close();
					return -1;
				
				
			}
			return Long.parseLong(us);
		}
		return -2;
	}
}
