package tk.zhla.citsoft.pan.service;

import java.util.ArrayList;
import java.util.List;

import tk.zhla.citsoft.pan.db.DBManager;
import tk.zhla.citsoft.pan.net.BigFileOrBreakPointUploadUtil;
import tk.zhla.citsoft.pan.net.NetworkUtils;
import tk.zhla.citsoft.pan.parse.entity.FileDataDBEntity;
import tk.zhla.citsoft.pan.share.ShareUtils;

import android.content.Context;
import android.util.Log;


public class UpLoadLocalFileBackground implements Runnable {

	List<FileDataDBEntity> files = new ArrayList<FileDataDBEntity>();

	private DBManager su = null;

	private Context context = null;

	/**
	 * 0：立即上传 1：自动备份0
	 * */
	private int upLoadType = -1;

	/**
	 * 0：立即上传 1：自动备份
	 * */
	public UpLoadLocalFileBackground(Context context, int upLoadType) {
		this.context = context;
		this.upLoadType = upLoadType;
		su = new DBManager(context);
		su.open();
	}

	public void run() {

		// if (upLoadType == 1) {
		// if (SharePreUtil.getAutoPhotoBackupThread(context)) {
		// return;
		// }
		// }
		// if (upLoadType == 1) {
		// SharePreUtil.saveAutoPhotoBackupThread(context, true);
		// }

		upLoad();

		// if (upLoadType == 1) {
		// SharePreUtil.saveAutoPhotoBackupThread(context, false);
		// }
		su.close();

	}

	// 找到所有需要上传的列表
	private void findList2UpLoad() {
		files = su.getUpLoadingFiles();

	}

	// 递归上传所有
	private void upLoad() {
		findList2UpLoad();
		if(files.size()==0){
			return ;
		}
		
		if(upLoadFile(files.get(0))){
			
			su.deleteUpLoadingFile(files.get(0).getFid());
			su.addUpLoadedFile(files.get(0));
		}else{
			su.deleteUpLoadingFile(files.get(0).getFid());
		}
			
		
		if(NetworkUtils.isNetworkAvailable(context)){
			upLoad();
		}
		

	}

	private boolean upLoadFile(FileDataDBEntity file) {

		Log.v("wang", file.getPid() + "------------------"+file.getPath());
		boolean b = new BigFileOrBreakPointUploadUtil()
				.uploadBigFile(file.getFid(),file.getPath(), file.getPid(), file.getAid(),
						new ShareUtils(context).getCookieUtil().getId() + "",
						new ShareUtils(context).getCookieUtil().getToken(), null,
						context);
		System.out.println(b);
		
		System.gc();

		return b;
	}

}
