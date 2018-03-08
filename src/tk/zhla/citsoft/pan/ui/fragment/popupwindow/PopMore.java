package tk.zhla.citsoft.pan.ui.fragment.popupwindow;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.net.NetworkUtils;
import tk.zhla.citsoft.pan.ui.dialog.JDDialogPopupRename;
import tk.zhla.citsoft.pan.ui.fragment.FileListFragment;
import tk.zhla.citsoft.pan.utils.ToastUtils;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;


public class PopMore implements OnClickListener {
	private Activity activity;
	private PopupWindow window;
	private View view;
	private PopupWindow menu;
	private Handler handler;
	
	private FileListFragment fragment;
	
	
	private View v;

	public PopMore(Activity activity, PopupWindow window,FileListFragment fragment, PopupWindow menu,View v) {
		this.activity = activity;
		this.v = v;
		this.fragment = fragment;
		this.menu = menu;
		view = activity.getLayoutInflater().inflate(R.layout.jd_pop_more, null);
		window = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		window.setBackgroundDrawable(new BitmapDrawable());
		window.setOutsideTouchable(true);
		View moveView = view.findViewById(R.id.jd_pop_more_move);
		View renameView = view.findViewById(R.id.jd_pop_more_rename);
		moveView.setOnClickListener(this);
		renameView.setOnClickListener(this);
		this.window = window;
	}
	

	public void setOnDismissListener(OnDismissListener onDismissListener) {
		window.setOnDismissListener(onDismissListener);
	}

	public void show() {
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.showAtLocation(view, Gravity.BOTTOM, 0, menu.getContentView()
				.getHeight());
	}

	public void dismiss() {
		if(window!=null)
		window.dismiss();
		if(move!=null)
		move.dis();
		
	}

	public PopupWindow getWindow() {
		return window;
	}
//	OtherFilePopMenuLoad2 move;
	OtherFilePopMenuLoad2	move;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.jd_pop_more_move:
			if(NetworkUtils.isNetworkAvailable(activity)){
				move = new OtherFilePopMenuLoad2(fragment,v);
				move.showPopupWindows(this.v);
			}else {
				Toast.makeText(activity, "���粻����", 0).show();
			}
			break;
		case R.id.jd_pop_more_rename:
			if(NetworkUtils.isNetworkAvailable(activity)){
				if(fragment.getCheckedFileDataFatherEntities().size()>1){
					ToastUtils.toast(fragment.getActivity(), "�ݲ�֧��һ���޸�2���ļ���");
				}else {
					JDDialogPopupRename rename = new JDDialogPopupRename(fragment);
				}
			}else {
				Toast.makeText(activity, "���粻����", 0).show();
			}
			break;
		}

	}
	
	
}
