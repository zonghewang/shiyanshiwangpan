package tk.zhla.citsoft.pan.ui.fragment.popupwindow;

import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.net.NetworkUtils;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.ui.dialog.PhotoShareDialog;
import tk.zhla.citsoft.pan.ui.fragment.PhotoListFragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class CameraPopMenuShare implements OnClickListener {
	private PopupWindow menuTop;
	private PopupWindow menuDown;
	private TextView title;
	private TextView textView;
	private TextView cancel;
	private Button bn;
	private boolean flag = false;
	private Handler handler;
	private Message message;
	private Context context;
	private boolean havePop;
	private Activity activity;
	private PhotoListFragment fragment;
	public void check() {
		int j = 0;
		for (int i = 0; i < fragment.getEntity().size(); i++) {
			if (fragment.getEntity().get(i).isChecked) {
				j++;
			}
		}
		if (j == 0) {
			title.setText("��ѡ��Ҫ���͵���Ƭ");
			bn.setText("���͸�����");
			bn.setTextColor(Color.rgb(91, 90, 90));
			bn.setEnabled(false);
		} else {
			title.setText("��ѡ��" + j + "��");
			bn.setText("���͸�����(" + j + ")");
			bn.setTextColor(Color.rgb(91, 90, 90));
			bn.setEnabled(true);
		}
	}

	public CameraPopMenuShare(Context context,Activity activity,PhotoListFragment fragment) {
		this.context = context;
		this.activity=activity;
		this.fragment=fragment;
		View menuViewDown = LayoutInflater.from(context).inflate(
				R.layout.camera_pop4_menu, null);
		menuDown = new PopupWindow(menuViewDown, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		View menuViewTop = LayoutInflater.from(context).inflate(
				R.layout.camera_pop3_menu, null);
		menuTop = new PopupWindow(menuViewTop, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		title = (TextView) menuViewTop.findViewById(R.id.camera_pop3_title);
		textView = (TextView) menuViewDown.findViewById(R.id.camera_pop4_tv);
		bn = (Button) menuViewDown.findViewById(R.id.camera_pop4_bn);
		title.setText("��ѡ��Ҫ���͵���Ƭ");
		textView.setText("��ѡ��Ҫ���͵���Ƭ");
		bn.setText("���͸�����");
		bn.setTextColor(Color.rgb(91, 90, 90));
		cancel = (TextView) menuViewTop.findViewById(R.id.camera_pop3_cancel);
		cancel.setOnClickListener(this);
		bn.setOnClickListener(this);
	}

	public void showPopupWindows(View v) {
		menuTop.showAtLocation(v, Gravity.TOP, 0, 50);
		menuDown.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		havePop=true;
	}
	public boolean havePop() {
		return havePop;
	}

	public void dis() {
		menuTop.dismiss();
		menuDown.dismiss();
		fragment.dis();
		havePop = false;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.camera_pop3_cancel:
			for (int i = 0; i < fragment.getEntity().size(); i++) {
				fragment.getEntity().get(i).isChecked = false;
			}
//			fragment.setEntities(fragment.getEntity());
			dis();
			fragment.refresh();
			break;
		case R.id.camera_pop4_bn:
			if(NetworkUtils.isNetworkAvailable(activity)){
				share();
			}else {
				Toast.makeText(activity, "���粻����",0).show();
			}
			
			
			break;

		}
	}
	
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
//			JDDialogMenuShare dialogMenuShare = new JDDialogMenuShare(getActivity(),
//					fragment.getEntity(), handler);
			PhotoShareDialog dialog=new PhotoShareDialog(activity,fragment.getEntity(),fragment);
			dialog.showDialog();
		}
	
	
		if(j>=2){
			Toast.makeText(activity, "��ѡ��һ����Ƭ����", 0).show();
		}
		
	}
	
}
