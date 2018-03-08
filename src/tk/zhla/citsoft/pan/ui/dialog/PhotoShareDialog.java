package tk.zhla.citsoft.pan.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.net.NetworkUtils;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDownLoadLinkedEnitity;
import tk.zhla.citsoft.pan.ui.MainFragmentActivity;
import tk.zhla.citsoft.pan.ui.fragment.PhotoListFragment;
import tk.zhla.citsoft.pan.utils.FileDownLoadLinkedUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class PhotoShareDialog extends Dialog implements OnClickListener {



	private View back = null;

	private View weixin = null;
	private View friends = null;
	private View sms = null;
	private View email = null;
	private View line = null;
	private View other = null;

	private Button cancel = null;

	private Context context = null;

	private Activity activity;

	private ListView lv;

	private List<FileDataEntity> entities=null;

	private PhotoListFragment fragment = null;

	private List<FileDataEntity> shareEntities;

	private FileDataEntity file ;
	
	public PhotoShareDialog(Activity activity, List<FileDataEntity> entities,
			PhotoListFragment fragment) {
		super(activity);
		this.entities = entities;
		this.activity = activity;
		this.context = activity;
		this.fragment = fragment;
		for (int i = 0; i <entities.size(); i++) {
			if(entities.get(i).isChecked){
				file  = entities.get(i);
			}
		}

		shareEntities = new ArrayList<FileDataEntity>();

		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).isChecked) {
				shareEntities.add(entities.get(i));
			}
		}

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.jd_menu_share);
		setView();
	}

	public void setView() {
		weixin = findViewById(R.id.menu_share_weixin);
		friends = findViewById(R.id.menu_share_friends);
		sms = findViewById(R.id.menu_share_sms);
		email = findViewById(R.id.menu_share_e_mail);
		line = findViewById(R.id.menu_share_line);
		other = findViewById(R.id.menu_share_other);
		back = findViewById(R.id.menu_share_back);
		weixin.setOnClickListener(this);
		friends.setOnClickListener(this);
		sms.setOnClickListener(this);
		email.setOnClickListener(this);
		line.setOnClickListener(this);
		other.setOnClickListener(this);
		back.setOnClickListener(this);
	}


	private int getWindowHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	private void dialogAnimation(final Dialog d, View v, int from, int to,
			boolean needDismiss) {

		if(needDismiss){
			disDialog();
		}else {
			showDialog();
		}
	}


	public void showDialog() {

		Window window = getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.mystyle); // ��Ӷ���
		show();
		WindowManager windowManager = activity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (display.getWidth());
		getWindow().setAttributes(params);
		// lp.x = 0;
		// lp.y = getWindowHeight();
		window
				.setBackgroundDrawableResource(R.drawable.background_dialog);
	}

	public void disDialog() {
		dismiss();
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			share(msg.what, msg.obj.toString());
		};
	};
	
	public void share(int id,String msg){
		switch (id) {
		case 0:
			Toast.makeText(context, "�������", 0).show();
			break;
		case R.id.menu_share_weixin:
			((MainFragmentActivity)activity).send(msg,file.getN());
			break;
		case R.id.menu_share_friends:
			((MainFragmentActivity)activity).sendFriends(msg,file.getN());
			break;
		case R.id.menu_share_sms:
			((MainFragmentActivity)activity).sendSMS(msg);
			break;
		case R.id.menu_share_e_mail:
			((MainFragmentActivity)activity).sendEmail(msg,file.getN());
			break;
		case R.id.menu_share_line:
			((MainFragmentActivity)activity).setClipBoard(msg);
			Toast.makeText(context, "�Ѿ����ƣ���ճ���������ı����͸�����", 0).show();
			break;
		case R.id.menu_share_other:
			((MainFragmentActivity)activity).sendOther(msg);
			break;
			
			
		}
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.menu_share_back:
			disDialog();
			break;
		case R.id.menu_share_weixin:
			
		case R.id.menu_share_friends:
		case R.id.menu_share_sms:
		case R.id.menu_share_e_mail:
		case R.id.menu_share_line:
		case R.id.menu_share_other:
			if(NetworkUtils.isNetworkAvailable(activity)){
			new Thread(){
				public void run() {
					FileDownLoadLinkedEnitity enitity=  FileDownLoadLinkedUtil.getFileLinked(context, file.getFid());
					
					Message message =handler.obtainMessage();

					if(enitity.isState()){
						message.what = v.getId();
						message.obj = enitity.getUrl().toString();
						//share(v.getId(), enitity.getUrl().getPath());
					}else {
						//share(0, enitity.getUrl().getPath());
						message.what = 0;
					}
					message.sendToTarget();
				};
			}.start();
			}else {
				Toast.makeText(activity, "���粻����", 0).show();
			}
			break;
		}
		disDialog();

	}

}
