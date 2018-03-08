package tk.zhla.citsoft.pan.ui.fragment.popupwindow;

import java.util.ArrayList;
import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.myclass.FileWithBoolean;
import tk.zhla.citsoft.pan.myclass.WyfItem;
import tk.zhla.citsoft.pan.ui.UpLoadActivity;

import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;


public class PopMenu2 implements OnClickListener {

	/** �Ϸ��˵� */
	private PopupWindow menuTop;

	/** �·��˵� */
	private PopupWindow menuDown;

	/** ȫѡ or ȫ��ѡ */
	private TextView all = null;

	/** ͷ--����ʾѡ����Ŀ */
	private TextView title = null;

	/** �˳�ѡ��ģʽ ȡ������ѡ�� */
	private TextView quit = null;

	private boolean f = false;

	private boolean havePop = false;

	/** ���� */
	private List<FileWithBoolean> fileList = new ArrayList<FileWithBoolean>();

	private View menuViewDown;
	private View menuViewTop;
	private View v;
	/** �ϴ�Ŀ���ļ��� */
	private TextView up_file;

	/** ��ť---��ʾѡ���ļ���Ŀ */
	private TextView up_account;

	/** ���ݶ�Ӧ��adapter ˢ���� */
	private BaseAdapter adapter;

	// private NetDistProgressBar bar;

	private Fragment fragment = null;

	public PopMenu2(Fragment fragment,
			List<FileWithBoolean> fileList) {
		this.fileList = fileList;
		// this.bar = bar;
		this.fragment = fragment;

		menuViewDown = LayoutInflater.from(fragment.getActivity()).inflate(
				R.layout.yf_pop3_menu, null);

		menuDown = new PopupWindow(menuViewDown, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		menuViewTop = LayoutInflater.from(fragment.getActivity()).inflate(
				R.layout.pop2_menu, null);
		menuTop = new PopupWindow(menuViewTop, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		all = (TextView) menuViewTop.findViewById(R.id.pop_top_all);
		title = (TextView) menuViewTop.findViewById(R.id.pop_top_title);
		quit = (TextView) menuViewTop.findViewById(R.id.pop_top_quit);
		all.setOnClickListener(this);
		quit.setOnClickListener(this);

		v = menuViewDown.findViewById(R.id.first);
		up_file = (TextView) menuViewDown.findViewById(R.id.pop_file);
		up_account = (TextView) menuViewDown.findViewById(R.id.pop_upload);

	}

	// ��ʾ��������menu
	public void showPopupWindows(View v) {
		int[] position = new int[2];
		((UpLoadActivity) fragment.getActivity()).getBack()
				.getLocationOnScreen(position);
		menuTop.showAtLocation(v, Gravity.TOP, 0, position[1]);
		menuDown.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		havePop = true;
	}

	// public void downloadShow() {
	// bar.show();
	// }
	//
	// public void downloadDis() {
	// bar.dis();
	// }

	public void dis() {
		menuTop.dismiss();
		menuDown.dismiss();
		havePop = false;
	}

	public boolean havePop() {
		return havePop;
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
	}
//
//	private boolean m = true;
//	private boolean a = false;

//	public void setA() {
//		a = true;
//	}

//	private WyfDialogDistStop stop;
//	private int i;

//	public void stop(final List<FileWithBoolean> items, final int i) {
//		stop = new WyfDialogDistStop(fragment.getActivity(), items, i);
//		stop.setOnConfirmListener(new OnConfirmListener() {
//
//			@Override
//			public void confirm() {
//				stopDis(items, i);
//			}
//		});
//	}

	public void check() {
		int j = 0;
		for (int i = 0; i < fileList.size(); i++) {
			if (fileList.get(i).isCheck()) {
				j++;
			}
		}
		if (j == 0) {
			title.setText("��ѡ���ļ�");
			up_account.setText("��ʼ�ϴ�");
		} else {
			title.setText("��ѡ" + j + "��");
			up_account.setText("��ʼ�ϴ�(" + j + ")");
		}
		adapter.notifyDataSetChanged();
	}

	public void stopDis(List<WyfItem> items, int i) {
//		stop.dis();
		// int t = 0;
		items.get(i).setDownload(false);
		for (int j = 0; j < items.size(); j++) {
			if (items.get(j).isDownload() && items.get(j).getPyte() == 1) {
				// t++;
			}
		}
		// if (t == 0) {
		// bar.dis();
		// }
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_top_all:
			selsvtAll();
			break;
		case R.id.pop_top_quit:
			quit();
			break;

		}
	}

	private void selsvtAll() {
		if (!f) {
			f = true;
			for (int i = 0; i < fileList.size(); i++) {
				fileList.get(i).setCheck(true);
			}
			all.setText("ȫ��ѡ");
		} else {
			f = false;
			all.setText("ȫѡ");
			for (int i = 0; i < fileList.size(); i++) {
				fileList.get(i).setCheck(false);
			}
		}
		adapter.notifyDataSetChanged();
	}

	private void quit() {
		dis();
		for (int i = 0; i < fileList.size(); i++) {
			fileList.get(i).setCheck(false);
		}
		f = false;
		all.setText("ȫѡ");
		adapter.notifyDataSetChanged();
	}
}
