package tk.zhla.citsoft.pan.net;

import java.util.ArrayList;
import java.util.List;

import tk.zhla.citsoft.pan.db.DBManager;
import tk.zhla.citsoft.pan.parse.entity.FileDataDBEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;


public class DownloadRun implements Runnable {
	private int id = 0;
	private int t = 0;
	private Context context;
	private Handler handler;
	private Message msg;
	private DownloadRun thread;
	private List<FileDataEntity> file;
	private DBManager utils;

	private Handler threadHandler = null;

	private DownloadRun(int t, int id, Context context, Handler handler,
			List<FileDataEntity> file) {

		this.id = id;
		this.file = file;
		this.t = t;
		this.thread = this;
		this.context = context;
		this.handler = handler;
		
	}

	


	public DownloadRun() {
		super();
		// TODO Auto-generated constructor stub
	}




	public static DownloadRun getDownLoadRun() {
		return new DownloadRun();
	}

	public Handler getThreadHandler() {
		return threadHandler;
	}

	
	public void addToDB(int t, int id, Context context, Handler handler,
			List<FileDataEntity> file) {
		this.id = id;
		this.file = file;
		this.t = t;
		this.thread = this;
		this.context = context;
		this.handler = handler;
		utils = new DBManager(context);
		utils.open();
		new Thread(this).start();
		

	}
	
	

	

	public void add() {
		//обть ап╠М
		String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+Environment.DIRECTORY_DCIM;
		for (int i = 0; i < file.size(); i++) {
			FileDataDBEntity dataDBEntity = file.get(i).getFileDataDBEntity();
			dataDBEntity.setPath(path);
			utils.addDownloadingFile(dataDBEntity);
		}
		msg = new Message();
		msg.what = 1;
		handler.sendMessage(msg);
	}

	@Override
	public void run() {
					add();
	}

}
