package tk.zhla.citsoft.pan.net;

import tk.zhla.citsoft.pan.parse.entity.DeleteFileEntity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;


public class DeleteRun implements Runnable {
	private Integer fid;
	private Integer cid;
	private Context context;
	private Handler handler;
	private int type;
	private Integer pid;

	public DeleteRun(int type, Integer fid, Integer cid, Context context,
			Handler handler, Integer pid) {
		this.pid = pid;
		this.fid = fid;
		this.cid = cid;
		this.context = context;
		this.handler = handler;
		this.type = type;
	}

	@Override
	public void run() {
		String id = null;
		if (fid == 0) {
			id = String.valueOf(cid);
			type = 2;
		} else {
			id = String.valueOf(fid);
			type = 1;
		}
		DeleteFileEntity entity = DeleteFileUtil.down(context, id);
		if(entity==null){
			Message message = new Message();
			
			message.what = -1;
			handler.sendMessage(message);
		}else {
			System.out.println(entity);
			System.out.println(type + "tttttttt");
			Message message = new Message();
			entity.setFid(fid);
			entity.setCid(cid);
			entity.setType(type);
			entity.setPid(pid);
			entity.setState(true);
			message.what = 2;
			message.obj = entity;
			handler.sendMessage(message);
		}

	}
}
