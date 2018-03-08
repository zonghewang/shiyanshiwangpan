package tk.zhla.citsoft.pan.ui.fragment.popupwindow;

import java.util.ArrayList;
import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.net.DownloadRun;
import tk.zhla.citsoft.pan.net.NetworkUtils;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.ui.dialog.PhotoDeleteDialog;
import tk.zhla.citsoft.pan.ui.dialog.PhotoDownloadDialog;
import tk.zhla.citsoft.pan.ui.dialog.PhotoShareDialog;
import tk.zhla.citsoft.pan.ui.fragment.PhotoListFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class CameraPopMenu implements OnClickListener {
	private PopupWindow menuTop;
	private PopupWindow menuDown;
	private TextView cancel;
	private TextView delete;
	private TextView download;
	private TextView share;
	private boolean flag = false;
	private Handler handler ;
	private Message message;
	private Context context;
	private Activity activity;
	private PhotoListFragment fragment;
	private boolean havePop=false;
	public CameraPopMenu(Context context,Activity activity,PhotoListFragment fragment) {
		this.context = context;
		this.fragment=fragment;
		this.activity=activity;
		View menuViewDown = LayoutInflater.from(context).inflate(
				R.layout.camera_pop_menu, null);
		menuDown = new PopupWindow(menuViewDown, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		View menuViewTop = LayoutInflater.from(context).inflate(
				R.layout.camera_pop2_menu, null);
		menuTop = new PopupWindow(menuViewTop, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		cancel = (TextView) menuViewTop.findViewById(R.id.camera_top2_cancel);
		cancel.setOnClickListener(this);
		delete = (TextView) menuViewDown.findViewById(R.id.camera_pop_delete);
		delete.setOnClickListener(this);
		share = (TextView) menuViewDown.findViewById(R.id.camera_pop_share);
		share.setOnClickListener(this);
		download = (TextView) menuViewDown
				.findViewById(R.id.camera_pop_download);
		download.setOnClickListener(this);

	}
	//��ʾPopUpWindows
	public void showPopupWindows(View v) {
		menuTop.showAtLocation(fragment.getView(), Gravity.TOP, 0, 50);
		menuDown.showAtLocation(fragment.getView(), Gravity.BOTTOM, 0, 0);
		havePop=true;
	}
	public boolean havePop() {
		return havePop;
	}
   //ȡ��PopUpWindows
	public void dis() {
		menuTop.dismiss();
		menuDown.dismiss();
		havePop=false;
		fragment.dis();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.camera_top2_cancel:
			for (int i = 0; i < fragment.getEntity().size(); i++) {
				fragment.getEntity().get(i).isChecked = false;
			}
			dis();
			fragment.refresh();
			break;
		case R.id.camera_pop_share:
			if(NetworkUtils.isNetworkAvailable(activity)){
				share();
			}else {
				Toast.makeText(context, "���粻���ã����Ժ�����", Toast.LENGTH_SHORT)
				.show();
			}
			
			break;
		case R.id.camera_pop_delete:
			if (!NetworkUtils.isNetworkAvailable(context)) {
				Toast.makeText(context, "���粻���ã����Ժ�����", Toast.LENGTH_SHORT)
						.show();
			} else {
//				dis();
				PhotoDeleteDialog deldialog = new PhotoDeleteDialog(context,
						fragment.getEntity(),fragment);
			}
			break;
		case R.id.camera_pop_download:
			if (fragment.getEntity().size() > 0) {
				List<FileDataEntity> downPictures = new ArrayList<FileDataEntity>();
				for (int i = 0; i < fragment.getEntity().size(); i++) {
					if (fragment.getEntity().get(i).isChecked) {
						downPictures.add(fragment.getEntity().get(i));
					}
				}
				if (!NetworkUtils.isNetworkAvailable(context)) {
					Toast.makeText(context, "���粻���ã����Ժ�����", Toast.LENGTH_SHORT)
							.show();
				} else if (NetworkUtils.isWifi(context)) {
					
					new Thread(){
						public void run() {
							down();
						};
					}.start();
				} else {
					dis();
					if (downPictures.size() == 0) {
						Toast.makeText(context, "�ļ����������", Toast.LENGTH_SHORT)
								.show();
					}else{
						PhotoDownloadDialog downdialog = new PhotoDownloadDialog(
								context,fragment,fragment.getEntity());
					}
					
				}
			}

			break;
		default:
			break;
		}
	}
	
	private void down(){
		List<FileDataEntity> downPictures = new ArrayList<FileDataEntity>();
		for (int i = 0; i < fragment.getEntity().size(); i++) {
			if (fragment.getEntity().get(i).isChecked) {
				downPictures.add(fragment.getEntity().get(i));
			}
		}
		DownloadRun downloadRun = DownloadRun.getDownLoadRun();
		downloadRun.addToDB(0, 0, context, handler2, downPictures);
		handler2.sendEmptyMessage(1);
	}
	
	
	private Handler handler2  = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==1){
				fragment.refresh();
				fragment.downFile();
				Toast.makeText(context, "��Ƭ���������", Toast.LENGTH_SHORT)
				.show();
				dis();
			}else if(msg.what==300||msg.what==3){
				fragment.refresh();
			}
			
		};
	};
	
	
	
	private void share(){
		int j = 0;
		for (int i = 0; i < fragment.getEntity().size(); i++) {
			if(fragment.getEntity().get(i).isChecked){
				j++;
			}
			
		}
		
//		if(j==0&&a==1){
//			menu_other_share.showContextMenu();
//		}
		
		if(j==1){
			PhotoShareDialog dialog=new PhotoShareDialog(activity,fragment.getEntity(),fragment);
			dialog.showDialog();
		}
	
		if(j>=2){
			Toast.makeText(activity, "��ѡ��һ����Ƭ����", 0).show();
		}
		
	}
	
	
}
