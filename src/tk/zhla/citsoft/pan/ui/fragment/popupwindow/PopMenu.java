package tk.zhla.citsoft.pan.ui.fragment.popupwindow;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDataFatherEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDirDataEntity;
import tk.zhla.citsoft.pan.ui.MainFragmentActivity;
import tk.zhla.citsoft.pan.ui.dialog.JDDialogMenuShare;
import tk.zhla.citsoft.pan.ui.fragment.FileListFragment;
import tk.zhla.citsoft.pan.utils.ToastUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;


public class PopMenu implements OnClickListener {
	public FileListFragment fragment ;
	private PopupWindow menuTop;
	private PopupWindow menuDown;
	private PopupWindow menuDownOther;
	private PopupWindow menuDownOtherDow;
	private TextView all, title, quit;
	private TextView delete, share, downlond, more_nor, more_dis;
	private View menuViewDown;
	private View menuViewTop;
	private View menuViewDownOther;
	private View menuViewDownOtherDow;
	private PopupWindow window;
	
	private boolean allchecked =false;
	private Button menu_other_share;
	private Button menu_other_download;
	
	private PopMore popmore;
	
	private MainFragmentActivity activity ;
	
	private boolean isShow = false;

	public PopMenu(final FileListFragment fragment,
			MainFragmentActivity activity ,View v) {
		this.activity = activity;
		this.fragment = fragment;
		this.v = v;
		menuViewDown = LayoutInflater.from(activity).inflate(R.layout.pop_menu,
				null);

		menuDown = new PopupWindow(menuViewDown, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		menuViewTop = LayoutInflater.from(activity).inflate(R.layout.pop2_menu,
				null);
		menuTop = new PopupWindow(menuViewTop, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		menuViewDownOther = LayoutInflater.from(activity).inflate(
				R.layout.pop_menu_other, null);
		menuDownOther = new PopupWindow(menuViewDownOther,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		menuViewDownOtherDow = LayoutInflater.from(activity).inflate(
				R.layout.pop_menu_other2, null);
		menuDownOtherDow = new PopupWindow(menuViewDownOtherDow,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		menu_other_download = (Button) menuViewDownOtherDow
				.findViewById(R.id.pop_down_othert_dow);

		menu_other_share = (Button) menuViewDownOther
				.findViewById(R.id.pop_down_othert);
		menu_other_share.setOnClickListener(this);
		menu_other_download.setOnClickListener(this);

		all = (TextView) menuViewTop.findViewById(R.id.pop_top_all);
		title = (TextView) menuViewTop.findViewById(R.id.pop_top_title);
		quit = (TextView) menuViewTop.findViewById(R.id.pop_top_quit);
		all.setOnClickListener(this);
		quit.setOnClickListener(this);

		delete = (TextView) menuViewDown.findViewById(R.id.pop_down_delete);
		share = (TextView) menuViewDown.findViewById(R.id.pop_down_share);
		downlond = (TextView) menuViewDown.findViewById(R.id.pop_down_downlond);
		more_nor = (TextView) menuViewDown.findViewById(R.id.pop_down_more_nor);
		more_dis = (TextView) menuViewDown.findViewById(R.id.pop_down_more_dis);
		delete.setOnClickListener(this);
		share.setOnClickListener(this);
		downlond.setOnClickListener(this);
		more_nor.setOnClickListener(this);
		more_dis.setOnClickListener(this);
		
		popmore = new PopMore(activity, window, fragment, menuDown,v);
		window = popmore.getWindow();
		popmore.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				fragment.isPop = 0;
			}
		});

		

	}
	
	public View v;

	/**
	 * һ��ѡ�к��popmenu
	 */
	public void showPopupWindows() {
		int[] position = new int[2];
		((MainFragmentActivity) activity).getNetDisk().getLocationOnScreen(position);
		menuTop.showAtLocation(v, Gravity.TOP, 0, position[1]);
		menuDown.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		isShow = true;
	}

	private boolean otherShow = false;
	private boolean otherShowDow = false;
	
	

	public boolean isShow() {
		return isShow;
	}

	/**
	 * ��ʾ�����popmenu 
	 */
	public void showSharePopMenu() {
		int[] position = new int[2];
		((MainFragmentActivity) activity).getNetDisk().getLocationOnScreen(position);
		menuTop.showAtLocation(v, Gravity.TOP, 0, position[1]);
		menuDownOther.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		title.setText("��ѡ��Ҫ���͵��ļ�");
		otherShow = true;
		isShow = true;
	}

	/**
	 * ��ʾ���ص� popmenu
	 */
	public void showSharePopMenuDow() {
		int[] position = new int[2];
		((MainFragmentActivity) activity).getNetDisk().getLocationOnScreen(position);
		menuTop.showAtLocation(v, Gravity.TOP, 0, position[1]);
		menuDownOtherDow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		title.setText("��ѡ��Ҫ���ص��ļ�");
		otherShowDow = true;
		isShow = true;
	}
	
	public void setTitle(String msg){
		switch (fragment.isPop) {
		case 1:
			menu_other_download.setText(msg);
			menu_other_download.setEnabled(true);
			break;
		case 2:
			menu_other_share.setText(msg);
			menu_other_share.setEnabled(true);
			break;
		}
	
		
	}

	public boolean getOtherPopMenuShow() {
		return otherShow;
	}

	public boolean getOtherPopMenuDowShow() {
		return otherShowDow;
	}
	public void dis() {
		menuTop.dismiss();
		menuDown.dismiss();
		menuDownOther.dismiss();
		menuDownOtherDow.dismiss();
		popmore.dismiss();
		fragment.isPop = 0;
		menu_other_download.setEnabled(false);
		menu_other_download.setText("��ѡ��Ҫ���ص��ļ�");
		menu_other_share.setEnabled(false);
		menu_other_share.setText("��ѡ��Ҫ������ļ�");
		isShow = false;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_top_all:
			if(!allchecked){
				fragment.checkAll(true);
				allchecked = true;
				all.setText("ȫ��ѡ");
			}else {
				fragment.checkAll(false);
				allchecked = false;
				all.setText("ȫѡ");
			}
			break;
		case R.id.pop_top_quit:
			fragment.dis();
			break;
		case R.id.pop_down_delete:
			new PopDelete(fragment);
			dis();
			break;
		case R.id.pop_down_share:
			if(fragment.getCheckedFileDataFatherEntities().size()>1){
				ToastUtils.toast(fragment.getActivity(), "�ݲ�֧�ֶ���ļ�����");
				return;
			}
			if(fragment.getCheckedFileDataFatherEntities().get(0) instanceof FileDirDataEntity){
				ToastUtils.toast(fragment.getActivity(), "�ݲ�֧���ļ��з���");
				return;
				
			}
			JDDialogMenuShare dialogMenuShare = new JDDialogMenuShare(fragment.getActivity(),(FileDataEntity) fragment.getCheckedFileDataFatherEntities().get(0));
			dialogMenuShare.showDialog();
			break;
		case R.id.pop_down_downlond:
			fragment.down();
			fragment.checkAll(false);
			ToastUtils.toast(fragment.getActivity(), "��ʼ����..");
			dis();
			break;
		case R.id.pop_down_more_nor:
			popmore.show();
			more_nor.setVisibility(View.GONE);
			more_dis.setVisibility(View.VISIBLE);
			break;
		case R.id.pop_down_more_dis:
			popmore.dismiss();
			more_nor.setVisibility(View.VISIBLE);
			more_dis.setVisibility(View.GONE);
			break;

		case R.id.pop_down_othert:
			JDDialogMenuShare dialogMenuShare1 = new JDDialogMenuShare(fragment.getActivity(),(FileDataEntity) fragment.getCheckedFileDataFatherEntities().get(0));
			dialogMenuShare1.showDialog();
			break;
		case R.id.pop_down_othert_dow:
			fragment.down();
			fragment.checkAll(false);
			ToastUtils.toast(fragment.getActivity(), "��ʼ����..");
			dis();
			break;
			
		}
	}
	
	
}
