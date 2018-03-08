package tk.zhla.citsoft.pan.utils;

import tk.zhla.citsoft.pan.parse.entity.AlterFileEntity;
import tk.zhla.citsoft.pan.parse.entity.AlterFolderEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDataFatherEntity;
import tk.zhla.citsoft.pan.ui.fragment.FileListFragment;
import android.content.Context;
import android.os.Handler;
import android.os.Message;


public class RenameRun implements Runnable {
	private String name;
	private int id;
	private FileListFragment context;
	private int t;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				context.refresh();
				ToastUtils.toast(context.getActivity(), "修改成功");
				break;

			case -1:
				ToastUtils.toast(context.getActivity(), "修改失败");
				break;
			}
		}
	};;
	private FileDataFatherEntity fileLocalFile;

	public RenameRun(int type, final FileListFragment  context, int id, String name,
			Handler handler, FileDataFatherEntity file) {
		this.fileLocalFile = file;
		this.t = type;
		this.context = context;
		this.id = id;
		this.name = name;

	}

	@Override
	public void run() {
		if (t == 1) {
			
			AlterFileEntity file = AlterFile.down(context.getActivity(), id, name, null);
			if(file!=null){
				
				//ToastUtils.toast(context.getActivity(), "修改成功");
				handler.sendEmptyMessage(1);
			}else {
				//ToastUtils.toast(context.getActivity(), "修改失败");
				handler.sendEmptyMessage(-1);
			}
		} else if (t == 2) {
			
			AlterFolderEntity entity = AlterFolder
					.down(context.getActivity(), id, name, null);
				if(entity!=null){
				//	ToastUtils.toast(context.getActivity(), "修改成功");
					handler.sendEmptyMessage(1);
			}else {
				//ToastUtils.toast(context.getActivity(), "修改失败");
				handler.sendEmptyMessage(-1);
			}
		}

	}

}
