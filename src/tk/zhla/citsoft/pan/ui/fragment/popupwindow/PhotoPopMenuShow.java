package tk.zhla.citsoft.pan.ui.fragment.popupwindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.net.NetworkUtils;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDataFatherEntity;
import tk.zhla.citsoft.pan.ui.PhotoShowActivity;
import tk.zhla.citsoft.pan.ui.dialog.PhotoDeleteDialog2;
import tk.zhla.citsoft.pan.ui.dialog.PhotoDownloadDialog2;
import tk.zhla.citsoft.pan.ui.dialog.PhotoShareDialog2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class PhotoPopMenuShow implements OnClickListener {
	private PopupWindow menuTop = null;
	private PopupWindow menuDown = null;
	private Handler handler = null;
	private Message message = null;
	private TextView timeView, numView, delete, down, share, artwork;
	private LinearLayout cancelView;
	private boolean flag = false;
	private boolean havePop = false;
	private PhotoShowActivity activity;

	public PhotoPopMenuShow(
			PhotoShowActivity activity) {
		super();
		
		this.activity = activity;
		View menuViewDown = LayoutInflater.from(activity).inflate(
				R.layout.photo_pop2_menu, null);
		menuDown = new PopupWindow(menuViewDown, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		View menuViewTop = LayoutInflater.from(activity).inflate(
				R.layout.photo_pop_menu, null);
		menuTop = new PopupWindow(menuViewTop, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		cancelView = (LinearLayout) menuViewTop
				.findViewById(R.id.photo_show_back);
		timeView = (TextView) menuViewTop.findViewById(R.id.photo_show_time);
		numView = (TextView) menuViewTop.findViewById(R.id.photo_show_num);
		delete = (TextView) menuViewDown.findViewById(R.id.photo_show_delete);
		down = (TextView) menuViewDown.findViewById(R.id.photo_show_download);
		share = (TextView) menuViewDown.findViewById(R.id.photo_show_share);
		artwork = (TextView) menuViewDown.findViewById(R.id.photo_show_artwork);
		cancelView.setOnClickListener(this);
		delete.setOnClickListener(this);
		share.setOnClickListener(this);
		down.setOnClickListener(this);
		artwork.setOnClickListener(this);

	}

	public void setTitle(String time, String num) {
		numView.setText(num);
		timeView.setText(time);
	}

	public void showPopupWindows(View v) {
		menuTop.showAtLocation(v, Gravity.TOP, 0, 50);
		menuDown.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		havePop = true;
	}

	public void dis() {
		menuTop.dismiss();
		menuDown.dismiss();
		havePop = false;
	}

	public boolean havePop() {
		return havePop;
	}

	Dialog dialog = null;

	public void showDialog() {

		dialog = new Dialog(activity);
		Window dialogWindow = dialog.getWindow();
		dialogWindow
				.setBackgroundDrawableResource(R.drawable.background_dialog);
		dialog.setContentView(R.layout.photo_delete_dialog);
		ImageView imageView = (ImageView) dialog
				.findViewById(R.id.photo_delete_diaog_image);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				disDialog();
			}
		});
		dialog.show();
	}

	public void disDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		NetworkUtils nUtils = new NetworkUtils();
		switch (v.getId()) {
		case R.id.photo_show_back:
			dis();
			if(activity.isDelete()){
				activity.setResult(1);
			}
			activity.finish();

			break;
		case R.id.photo_show_share:
			PhotoShareDialog2 dialog = new PhotoShareDialog2(activity,
					activity.getEntity().getFatherEntities().get(activity.getCurrpag()));
			dialog.showDialog();
			break;
		case R.id.photo_show_delete:
//
			if (!NetworkUtils.isNetworkAvailable(activity)) {
				Toast.makeText(activity, "���粻���ã����Ժ�����", Toast.LENGTH_SHORT)
						.show();
			} else {
				PhotoDeleteDialog2 deldialog = new PhotoDeleteDialog2(activity,
						activity.getEntity().getFatherEntities().get(activity.getCurrpag()));
			}
			break;
		case R.id.photo_show_artwork:
			
			//show();
			break;
		case R.id.photo_show_download:
			if (!NetworkUtils.isNetworkAvailable(activity)) {
				Toast.makeText(activity, "���粻���ã����Ժ�����", Toast.LENGTH_SHORT)
						.show();
			} else if (NetworkUtils.isNetworkAvailable(activity)) {
				//dis();
					//	down();
				
			} else {
				//dis();
				PhotoDownloadDialog2 downdialog = new PhotoDownloadDialog2(
						activity, activity.getEntity().getFatherEntities().get(activity.getCurrpag()));
				
			}
			break;
		default:
			break;

		}
	}
	

	
	
//	public void show(){
//		SQLUtils sqlUtils = new SQLUtils(context);
//		UserLocalFile localFile = sqlUtils.findDownedEntitybyFid(Integer.parseInt(entities.get(activity.getCurrpag()).getFid()));
//		if(localFile==null){
//			Toast.makeText(context, "�㻹û�����أ���������", 0).show();
//			return;
//		}
//		if(localFile.getPath()!=null){
//			File file = new File(localFile.getPath());
//			if(file.exists()){
//				Intent intent = FileOpenUtils.openFile(localFile.getPath());
//				try{
//					context.startActivity(intent);
//				}catch (Exception e){
//					Toast.makeText(activity, "�����޷��򿪳���", 0).show();
//				}
//			} else {
//				Toast.makeText(activity, "�ļ��Ѿ���ɾ��������������", 0).show();
//			}
//		}else {
//			Toast.makeText(activity, "·���������������", 0).show();
//		}
//			
//		
//	}
//	
	
//	private void down(){
//		List<UserLocalFile> downPictures = new ArrayList<UserLocalFile>();
//		UserLocalFile localFile = new UserLocalFile();
//		localFile.setFid(Integer.parseInt(entities.get(activity.getCurrpag()).getFid()));
//				downPictures.add(localFile);
//		DownloadRun downloadRun = DownloadRun.getDownLoadRun();
//		downloadRun.addToDB(0, 0, activity, handler2, downPictures);
//		Toast.makeText(activity, "��ʼ��̨����ඣ��Ե�", Toast.LENGTH_SHORT).show();
//	}
//	
	private Handler handler2  = new Handler(){
		public void handleMessage(Message msg) {
			 if(msg.what==300||msg.what==3){
				Toast.makeText(activity, "��Ƭ���������", Toast.LENGTH_SHORT).show();
				
			}
			
		};
	};
}
