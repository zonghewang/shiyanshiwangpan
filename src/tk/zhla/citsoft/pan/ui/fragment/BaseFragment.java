package tk.zhla.citsoft.pan.ui.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.db.DBManager;
import tk.zhla.citsoft.pan.net.ExecRunable;
import tk.zhla.citsoft.pan.net.HttpUtils;
import tk.zhla.citsoft.pan.net.RequestParam;
import tk.zhla.citsoft.pan.parse.FileListParse;
import tk.zhla.citsoft.pan.parse.entity.FileDataDBEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDirDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDownLoadLinkedEnitity;
import tk.zhla.citsoft.pan.parse.entity.FilesListEntity;
import tk.zhla.citsoft.pan.share.ShareUtils;
import tk.zhla.citsoft.pan.utils.FileDownLoadLinkedUtil;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lockscreen.pattern.OffUnlockPasswordActivity;

public abstract class BaseFragment extends Fragment {
	DBManager dbManager ;
	/**
	 * 时间排序
	 */
	public static final String USER_PTIME = "user_ptime";
	/**
	 * 大小排序
	 */
	public static final String FILE_SIZE="file_size";
	/**
	 * 文件名
	 */
	public static final String FILE_NAME = "file_name";
	
	/**
	 * 降序
	 */
	public static final int DESC = 1;
	
	/**
	 * 升序
	 */
	public static final int ASC = 0;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		dbManager= new DBManager(getActivity());
		dbManager.open();
		return createView(inflater);
	}
	/**
	 * 着这个方法中设置布局
	 */
	public abstract View createView(LayoutInflater inflater);
	
	/**
	 * 
	 */
	public abstract void initViews();

	
	/**
	 * 
	 */
	public abstract void setLogic();
	
	public abstract void setAdapter();
	
	// 联网
	public void lianwang(RequestParam qinQiu,OnLianWangFinishLisenter lisenter){
		MyHandler handler = new MyHandler(lisenter);
		//启动线程，执行
		ExecRunable.execRun(new MyThread(qinQiu,handler));
		
		
	}
	
	public class MyHandler extends Handler {
		OnLianWangFinishLisenter lisenter;
		public MyHandler(OnLianWangFinishLisenter lisenter) {
			// TODO Auto-generated constructor stub
			this.lisenter = lisenter;
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0://网络访问成功
				if(msg.obj!=null){
					lisenter.onFinish(msg.obj);
				}else {
					lisenter.onError(1);
				}
				break;

			case -1://联网失败
				lisenter.onError(msg.arg1);
				break;
			}
		}
	}
	
	@Override
	public void onActivityCreated( Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
		initViews();
		setLogic();
		setAdapter();
		
	}
	
	
	public class MyThread extends Thread {
		RequestParam qinQiu ;
		MyHandler handler;
		public MyThread(RequestParam qinQiu,MyHandler handler) {
			// TODO Auto-generated constructor stub
			this.qinQiu = qinQiu;
			this.handler = handler;
		}
		
		@Override
		public void run() {
			String result = null;
			ShareUtils utils = new ShareUtils(getActivity());
			if(qinQiu.method==RequestParam.GET){
				//get
				try {
					result = HttpUtils.GetStringForHttpGet(utils.getCookieUtil(), qinQiu.pairs, qinQiu.url, 1);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				//post
				try {
					result = HttpUtils.GetStringForHttpPost(utils.getCookieUtil(), qinQiu.pairs, qinQiu.url, 1);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//联网结束
			if(result==null){
				Message message = new Message();
				message.what = -1;
				message.arg1 = 404;
				handler.sendMessage(message);
				return;
			}
			//解析
			try{
			Object obj = qinQiu.parse.parse(result);
			Message m1 = new Message();
			m1.what=0;
			m1.obj = obj;
			handler.sendMessage(m1);
			}catch (Exception e) {
				Message m = new Message();
				m.what = -1;
				m.arg1 = -1;
				handler.sendMessage(m);
			}
			
		}
	}
	
	public interface OnLianWangFinishLisenter<T>{
		public void onFinish(T t);
		public void onError(int errorCode);
	}
	public RequestParam getFileList(int type,int offset,int cid,String order,int ascOrdesc){
		RequestParam param = new RequestParam();
		ShareUtils utils = new ShareUtils(getActivity());
		if(type==-1){
			param.url = utils.getURL()+"/a1/index?ct=list&aid=1&cid="+cid+"&o="+order+"&asc="+ascOrdesc+"&offset="+offset+"&limit=100";
		}else {
			param.url = utils.getURL()+"/a1/index?ct=list&aid=1&cid="+cid+"&o="+order+"&asc="+ascOrdesc+"&offset="+offset+"&limit=100&type="+type;
		}
		param.method = RequestParam.GET;
		param.parse = new FileListParse();
		return param;
	}
	
	public abstract boolean onBack();
	
	public void down(FilesListEntity entity,FileDataEntity entity2){
		final FileDataDBEntity entity3  = entity2.getFileDataDBEntity();
		entity3.setPath(getPath(entity));
		dbManager.addDownloadingFile(entity3);
		downFile();
	}
	public String getPath(FilesListEntity entity){
		StringBuffer buf = new StringBuffer();
		buf.append(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+getResources().getString(R.string.app_name)+"/"+new ShareUtils(getActivity()).getLoginEntity().getId()+"/");
		for(int i=0;i<entity.getFilePathEntities().size();i++){
			buf.append(entity.getFilePathEntities().get(i).getName()+"/");
		}
		return buf.toString();
	}
	
	public void down(final FileDirDataEntity entity2,int offset){
		RequestParam param = getFileList(-1, offset, entity2.getCid(), FILE_NAME, ASC);
		lianwang(param, new OnLianWangFinishLisenter<FilesListEntity>() {

			@Override
			public void onFinish(FilesListEntity t) {
				if(t.getOffset()+t.getFatherEntities().size()==t.getCount()){
					
				}else {
					down(entity2, t.getOffset()+t.getFatherEntities().size());
				}
				for (int i = 0; i <t.getFatherEntities().size(); i++) {
					if(t.getFatherEntities().get(i) instanceof FileDataEntity){
						down(t, (FileDataEntity) t.getFatherEntities().get(i));
					}else {
						down((FileDirDataEntity) t.getFatherEntities().get(i), 0);
					}
				}
			}

			@Override
			public void onError(int errorCode) {
				
			}
		});
	}
	
	public void downFile(){
		
		ExecRunable.execDwon(new Runnable() {
			public void run() {
				int fid = 0;
				while(dbManager.getDownloadingFiles().size()>0){
					try{
						System.out.println("开始瞎子啊了");
					List<FileDataDBEntity> entities =dbManager.getDownloadingFiles();
					
					FileDataDBEntity entity3 = entities.get(0);
					fid = entity3.getFid();
					System.out.println(fid);
					FileDownLoadLinkedEnitity linkedEnitity = FileDownLoadLinkedUtil.getFileLinked(getActivity(), entity3.getFid());
					if(linkedEnitity!=null){
						File f = new File(entity3.getPath());
						
						f.mkdirs();
						System.out.println("mkdirs");
						down(new File(f.getAbsolutePath()+"/"+entity3.getN()), linkedEnitity.getUrl(), entity3);
						System.out.println("上传完成");
					}
					}catch (Exception e){
						System.out.println("下载异常");
						dbManager.deleteDownloadingFile(fid);
					}
				}
			}
		});
	}
	
	/**
	 * 在子线程中执行
	 * @param f
	 * @param url
	 * @param entity
	 */
	public void down(File f,String url1,FileDataDBEntity entity){
		try {
	         // 构造URL   
	         URL url = new URL(url1);   
	         // 打开连接   
	         URLConnection con = url.openConnection();
	         //获得文件的长度
	         int contentLength = con.getContentLength();
	         System.out.println("长度 :"+contentLength);
	         // 输入流   
	         InputStream is = con.getInputStream();  
	         // 1K的数据缓冲   
	         byte[] bs = new byte[1024*256];   
	         // 读取到的数据长度   
	         int len;   

	         long sum = 0;
	         
	         // 输出的文件流   
	         OutputStream os = new FileOutputStream(f);   
	         // 开始读取   
	         while ((len = is.read(bs)) != -1) {   
	             os.write(bs, 0, len);   
	             sum+=len;
	             entity.setSpare(sum+"");
	             dbManager.updateDownloadingFile(entity);
	         }  
	         dbManager.deleteDownloadingFile(entity.getFid());
	         dbManager.addDownloadedFile(entity);
	         // 完毕，关闭所有链接   
	         os.close();  
	         is.close();
	            
	} catch (Exception e) {
	        e.printStackTrace();
	}
	}
	
	
	public abstract void pageChange();
}
