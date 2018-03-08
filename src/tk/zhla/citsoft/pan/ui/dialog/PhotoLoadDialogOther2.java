package tk.zhla.citsoft.pan.ui.dialog;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.net.ExecRunable;
import tk.zhla.citsoft.pan.net.HttpUtils;
import tk.zhla.citsoft.pan.net.RequestParam;
import tk.zhla.citsoft.pan.parse.FileListParse;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDataFatherEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDirDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FilePathEntity;
import tk.zhla.citsoft.pan.parse.entity.FilesListEntity;
import tk.zhla.citsoft.pan.share.ShareUtils;
import tk.zhla.citsoft.pan.ui.adapter.FileListViewAdapter2;
import tk.zhla.citsoft.pan.ui.myviews.FileDirLocationBar;
import tk.zhla.citsoft.pan.ui.myviews.FileDirLocationBar.OnFilePathChange;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class PhotoLoadDialogOther2 {
	
	
	private FilesListEntity entity ;
	
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
	
	

	private Dialog d = null;

	private View view = null;

	private Button confirm = null;

	private View move = null;

	private Button cancel = null;

	private Context context = null;

	private PullToRefreshListView listView ;

	private List<FileDataEntity> list;
	
	private int offset = 0;


	public Context getContext() {
		return context;
	}

	//文件导航
	private FileDirLocationBar bar2 = null;


	
	//最后一个后，是否在加载
		private boolean lastting =  false;
		
		private HorizontalScrollView horizontalScrollView;
		private ViewGroup group;
		//文件导航
		
		
		private FileListViewAdapter2 adapter ;
	


	public PhotoLoadDialogOther2(Context context,FilesListEntity entity) {
		
		this.context = context;
	
		d = new Dialog(context);
		this.entity = entity;
	
		view = View.inflate(context, R.layout.photo_load_dialog_other2, null);

		listView =  (PullToRefreshListView) view.findViewById(R.id.photo_load_dialog_other_lv);
		group = (ViewGroup) view.findViewById(R.id.photo_load_dialog_other_locationID_move);
		 horizontalScrollView = (HorizontalScrollView) view
				.findViewById(R.id.photo_load_dialog_other_move);

		d.setContentView(view);

		confirm = (Button) d.findViewById(R.id.photo_load_dialog_other_btn_move);

		cancel = (Button) d.findViewById(R.id.photo_load_dialog_other_btn_quit);

		move = d.findViewById(R.id.photo_load_dialog_other_newfile);
		

		init();
	}
	
	public void refresh(){
		listView.setState(State.REFRESHING, true);
		lianwang(getFileList(-1, 0, entity.getCid(),FILE_NAME,ASC), new OnLianWangFinishLisenter<FilesListEntity>() {
			public void onError(int errorCode) {
				listView.onRefreshComplete();
			}
			public void onFinish(FilesListEntity t) {
				//初始化
				entity = t;
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
			}
		});
	}
	
	public void setLogic() {
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
				lianwang(getFileList(-1, 0, entity.getCid(),FILE_NAME,ASC), new OnLianWangFinishLisenter<FilesListEntity>() {
					public void onError(int errorCode) {
						listView.onRefreshComplete();
					}
					public void onFinish(FilesListEntity t) {
						//初始化
						entity = t;
						adapter.notifyDataSetChanged();
						//setBar();
						listView.onRefreshComplete();
					}
				});
			}
		});
		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if (lastting) {
					
					return;
				}
				lastting = true;
			
				if(entity!=null){
					offset = entity.getFatherEntities().size();
				}
				lianwang(getFileList(-1, offset, entity.getCid(),FILE_NAME,ASC), new OnLianWangFinishLisenter<FilesListEntity>() {
					public void onError(int errorCode) {
						lastting = false;
					}
					public void onFinish(FilesListEntity t) {
						entity.merge(t);
						adapter.notifyDataSetChanged();
						lastting = false;
						
					}
				});
			}
		});
	}
	

	public void setAdapter() {
		setBar();
		adapter = new FileListViewAdapter2(this);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, false));
	}
	
	public FilesListEntity getFilesListEntity(){
		return entity;
	}
	public void onItemClick(int postion){
		//先看是文件还是文件夹
		FileDataFatherEntity e =  entity.getFatherEntities().get(postion);
		
		if(e instanceof FileDirDataEntity){
			
			addBarItem((FileDirDataEntity) e);
			//文件夹
			lianwang(getFileList(-1, 0, ((FileDirDataEntity) e).getCid(),FILE_NAME,ASC), new OnLianWangFinishLisenter<FilesListEntity>() {
				public void onError(int errorCode) {
				}
				public void onFinish(FilesListEntity t) {
					
					//初始化
					entity = t;
//					setBar();
					adapter.notifyDataSetChanged();
				}
			});
		}
	}
	public void addBarItem(FileDirDataEntity entity){
		FilePathEntity entity2 = new FilePathEntity(entity.getN(), entity.getAid(), entity.getCid(), entity.getPid());
		bar2.addBtn(entity2);
		confirm.setText("选定的文件夹是:"+ entity.getN());
	}
	

	private void init() {

		Window dialogWindow = d.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		// lp.x = 0;
		// lp.y = getWindowHeight();
		dialogWindow
				.setBackgroundDrawableResource(R.drawable.background_dialog);

		final int height = lp.height;

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onConfirmListener != null)
					onConfirmListener.confirm(entity);
				dialogAnimation(d, view, height, getWindowHeight(), true);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogAnimation(d, view, height, getWindowHeight(), true);
			}
		});

		move.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				JDDialogNewFile dialogNewFile = new JDDialogNewFile(activity);
				
			}
		});

		d.show();
		dialogAnimation(d, view, getWindowHeight(), height, false);
		setLogic();
		setAdapter();

	}

	private int getWindowHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	private void dialogAnimation(final Dialog d, View v, int from, int to,
			boolean needDismiss) {

		Animation anim = new TranslateAnimation(0, 0, from, to);
		anim.setFillAfter(true);
		anim.setDuration(600);
		if (needDismiss) {
			anim.setAnimationListener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					d.dismiss();
				}
			});

		}

		v.startAnimation(anim);
	}

	private OnConfirmListener onConfirmListener = null;

	public interface OnConfirmListener {
		public void confirm(FilesListEntity entity);
	}

	public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
		this.onConfirmListener = onConfirmListener;
	}
	
	

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
				lisenter.onFinish(msg.obj);
				break;

			case -1://联网失败
				lisenter.onError(msg.arg1);
				break;
			}
		}
	}
	
	public void setBar(){
		bar2 = new FileDirLocationBar(context,group,horizontalScrollView,entity.getFilePathEntities());
		confirm.setText("选定的文件夹是:"+entity.getFilePathEntities().get(entity.getFilePathEntities().size()-1).getName());
		bar2.setOnFilePathChange(new OnFilePathChange() {
			public void filePathChange(int cid) {
				if(cid==-1){
					return;
				}
				
				if(cid==entity.getCid()){
					
				}else {
					//文件夹
					lianwang(getFileList(-1, 0, cid,FILE_NAME,ASC), new OnLianWangFinishLisenter<FilesListEntity>() {
						public void onError(int errorCode) {
						}
						public void onFinish(FilesListEntity t) {
							//初始化
							entity = t;
							//setBar();
							adapter.notifyDataSetChanged();
						}
					});
				}
				
			}
		});
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
			ShareUtils utils = new ShareUtils(context);
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
		ShareUtils utils = new ShareUtils(context);
		if(type==-1){
			param.url = utils.getURL()+"/a1/index?ct=list&aid=1&cid="+cid+"&o="+order+"&asc="+ascOrdesc+"&offset="+offset+"&limit=100";
		}else {
			param.url = utils.getURL()+"/a1/index?ct=list&aid=1&cid="+cid+"&o="+order+"&asc="+ascOrdesc+"&offset="+offset+"&limit=100&type="+type;
		}
		param.method = RequestParam.GET;
		param.parse = new FileListParse();
		return param;
	}
	

}
