package tk.zhla.citsoft.pan.ui.fragment.popupwindow;

import java.util.ArrayList;
import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.net.NetworkUtils;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.ui.dialog.PhotoDeleteDialog;
import tk.zhla.citsoft.pan.ui.fragment.PhotoListFragment;

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


public class CameraPopMenuDelete implements OnClickListener {
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
	private PhotoListFragment fragment=null;
	public void check() {
		int j = 0;
		for (int i = 0; i < fragment.getEntity().size(); i++) {
			if (fragment.getEntity().get(i).isChecked) {
				j++;
			}
		}
		if (j == 0) {
			title.setText("ÇëÑ¡ÔñÒªÉ¾³ýµÄÕÕÆ¬");
			bn.setText("É¾³ýÕÕÆ¬");
			bn.setTextColor(Color.rgb(91, 90, 90));
			bn.setEnabled(false);
		} else {
			title.setText("ÒÔÑ¡Ôñ" + j + "Ïî");
			bn.setText("É¾³ýÕÕÆ¬(" + j + ")");
			bn.setTextColor(Color.rgb(91, 90, 90));
			bn.setEnabled(true);
		}
	}
	

	public CameraPopMenuDelete(Context context,PhotoListFragment fragment) {
		this.context = context;
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
		title.setText("ÇëÑ¡ÔñÒªÉ¾³ýÕÕÆ¬");
		textView.setText("ÇëÑ¡ÔñÒªÉ¾³ýµÄÕÕÆ¬");
		bn.setText("É¾³ýÕÕÆ¬");
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
			fragment.refresh();
			dis();
			break;
		case R.id.camera_pop4_bn:
			if (fragment.getEntity().size() > 0) {
				List<FileDataEntity> deletePictures = new ArrayList<FileDataEntity>();
				for (int i = 0; i < fragment.getEntity().size(); i++) {
					if (fragment.getEntity().get(i).isChecked) {
						deletePictures.add(fragment.getEntity().get(i));
					}
				}
				if (!NetworkUtils.isNetworkAvailable(context)) {
					Toast.makeText(context, "ÍøÂç²»¿ÉÓÃ£¬ÇëÉÔºóÖØÊÔ", Toast.LENGTH_SHORT)
							.show();
				}else{ 
//					dis();
					PhotoDeleteDialog deleteialog = new PhotoDeleteDialog(
							context,fragment.getEntity(),fragment);
					
					
				}
			}		

		}
	}
}
