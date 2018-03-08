package tk.zhla.citsoft.pan.myclass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.db.DBManager;
import tk.zhla.citsoft.pan.net.NetworkUtils;
import tk.zhla.citsoft.pan.parse.entity.FileDataDBEntity;
import tk.zhla.citsoft.pan.parse.entity.FilesListEntity;
import tk.zhla.citsoft.pan.share.ShareUtils;
import tk.zhla.citsoft.pan.ui.UpLoadActivity;
import tk.zhla.citsoft.pan.ui.adapter.PhotoPopMenuLoad;
import tk.zhla.citsoft.pan.ui.dialog.PhotoLoadDialog;
import tk.zhla.citsoft.pan.ui.dialog.PhotoLoadDialog.OnConfirmListener1;
import tk.zhla.citsoft.pan.ui.fragment.UpLoadOtherFileFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class OtherFilePopMenuLoad implements OnClickListener {
	private PopupWindow menuTop;
	private PopupWindow menuDown;
	private TextView all, title, quit, path;
	private boolean flag = false;
	private boolean havePop = false;
	private Message message;
	private View menuViewDown;
	private View menuViewTop;
	private Button onload;
	private LinearLayout layout;
	private List<FileWithBoolean> booleans;
	private ImageView imageView;
	private Context context;
	private Handler handler;
	private int curcid = 0;
	private UpLoadOtherFileFragment fragment;
	private Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {

				imageView.setImageResource(R.drawable.cloud_dir_icon);
				path.setText("�ҵ�����");
				curcid = 0;

				// else{
				// imageView.setImageResource(R.drawable.photo_album_icon);
				// path.setText("�������");
				// curcid=0;
				// }
			} else if (msg.what == -1) {

			} else {
				imageView.setImageResource(R.drawable.cloud_dir_icon);
				curcid = msg.what;
				path.setText((String) msg.obj);
			}
			
		};
		
	};
	private View v = null;

	private Handler handler3 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1024:
				// showDialog();
//				context.stopService(new Intent(context, UpLoadService.class));
//				context.startService(new Intent(context, UpLoadService.class));
				for (int i = 0; i < booleans.size(); i++) {
					booleans.get(i).setCheck(false);
				}
				check(booleans);
				dis();
				disDialog();
				Toast.makeText(context, "�����ӵ��ϴ��б�", Toast.LENGTH_SHORT)
						.show();
				((UpLoadActivity)context).finish();

				break;

			default:
				break;
			}
		};
	};

	public void check(List<FileWithBoolean> booleans) {
		int j = 0;
		for (int i = 0; i < booleans.size(); i++) {

			if (booleans.get(i).isCheck()) {
				j++;
			}
		}

		if (j > 0) {
			title.setText("��ѡ" + j + "��");
			onload.setText("��ʼ�ϴ�(" + j + ")");
			showPopupWindows(v);
		} else if (j == 0) {
			title.setText("��ѡ���ļ�");
			onload.setText("��ʼ�ϴ�");
			dis();
		}
	}

	public OtherFilePopMenuLoad(Context context,
			List<FileWithBoolean> booleans, UpLoadOtherFileFragment fragment,
			View v) {
		entity = (FilesListEntity) fragment.getActivity().getIntent().getSerializableExtra("entity");
		this.booleans = booleans;
		this.v = v;
		this.context = context;
		this.fragment = fragment;
		menuViewDown = LayoutInflater.from(context).inflate(
				R.layout.photo_load_menu_down, null);

		menuDown = new PopupWindow(menuViewDown, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		menuViewTop = LayoutInflater.from(context).inflate(
				R.layout.photo_load_menu_top, null);
		menuTop = new PopupWindow(menuViewTop, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		layout = (LinearLayout) menuViewDown
				.findViewById(R.id.photo_load_menu_lin);
		imageView = (ImageView) menuViewDown
				.findViewById(R.id.photo_load_menu_image);
		path = (TextView) menuViewDown.findViewById(R.id.photo_load_menu_text);
		layout = (LinearLayout) menuViewDown
				.findViewById(R.id.photo_load_menu_lin);
		onload = (Button) menuViewDown.findViewById(R.id.photo_load_menu_bn);
		all = (TextView) menuViewTop.findViewById(R.id.photo_load_menu_top_all);
		title = (TextView) menuViewTop
				.findViewById(R.id.photo_load_menu_top_title);
		quit = (TextView) menuViewTop
				.findViewById(R.id.photo_load_menu_top_quit);
		all.setOnClickListener(this);
		quit.setOnClickListener(this);
		onload.setOnClickListener(this);
		layout.setOnClickListener(this);
		path.setText(entity.getFilePathEntities().get(entity.getFilePathEntities().size()-1).getName());
	}

	public void showPopupWindows(View v) {
		menuTop.showAtLocation(v, Gravity.TOP, 0, 50);
		menuDown.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		fragment.setFlag(true);
		havePop = true;
	}

	public boolean havePop() {
		return havePop;
	}

	Dialog dialog = null;
	private  FilesListEntity entity;

	public void showDialog() {

		dialog = new Dialog(context);
		Window dialogWindow = dialog.getWindow();
		dialogWindow
				.setBackgroundDrawableResource(R.drawable.background_dialog);
		dialog.setContentView(R.layout.photo_delete_dialog);
		TextView textView = (TextView) dialog
				.findViewById(R.id.photo_delete_diaog_textView);
		textView.setText("���ڴ���");
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

	public void dis() {
		menuTop.dismiss();
		menuDown.dismiss();
		fragment.setFlag(false);
		havePop = false;
	}

	public void sethandler(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void onClick(View v) {
		NetworkUtils nUtils = new NetworkUtils();
		switch (v.getId()) {
		case R.id.photo_load_menu_top_quit:
			quit();
			break;
		case R.id.photo_load_menu_top_all:
			selsvtAll();
			break;
		case R.id.photo_load_menu_lin:
			PhotoLoadDialog loadDialog = new PhotoLoadDialog(context,
					fragment.getActivity());
			loadDialog.setOnConfirmListener(new OnConfirmListener1() {
				public void confirm(FilesListEntity entity) {
					OtherFilePopMenuLoad.this.entity = entity;
					
					path.setText(entity.getFilePathEntities().get(entity.getFilePathEntities().size()-1).getName());
				}
			});
			loadDialog.showMyDialog();
			break;
		case R.id.photo_load_menu_bn:
			DBManager dbManager = new DBManager(context);
			dbManager.open();
			ShareUtils utils = new ShareUtils(context);
			dbManager.open();
			if(NetworkUtils.isNetworkAvailable(context)){
				for (int i = 0; i < booleans.size(); i++) {
					if (booleans.get(i).isCheck()) {
						if(booleans.get(i).getFile().isDirectory()){
							utils.addDownPath(booleans.get(i).getFile().getAbsolutePath());
						}else {
							FileDataDBEntity dataDBEntity = new FileDataDBEntity();
							dataDBEntity.setFid((int)System.currentTimeMillis());
							dataDBEntity.setPath(booleans.get(i).getFile().getAbsolutePath());
							dataDBEntity.setPid(entity.getCid());
							dataDBEntity.setAid(1);
							dbManager.addUpLoadingFile(dataDBEntity);
						}
					}
				}
//			new Thread(new AddFileNeedUpLoadRightNow2(loadFiles, curcid,
//					context, handler3)).start();
				dbManager.close();
			}else {
				Toast.makeText(context, "���粻����",0).show();
			}
			// System.out.println("�ϴ��ļ�Cid" + curcid);
			// System.out.println("�ϴ��ļ�" + loadbooleans);
			// �ϴ�������curcid��Ŀ¼cid loadbooleans��Ҫ�ϴ���
			break;
		default:
			break;
		}

	}
	
	
	
	
	

	public void alert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("����!!");
		builder.setMessage("�����������ļ����Ƿ񸲸�?");
		builder.setNegativeButton("ȡ��", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("ȷ��", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(NetworkUtils.isNetworkAvailable(context)){
				List<File> loadFiles = new ArrayList<File>();
				for (int i = 0; i < booleans.size(); i++) {
					if (booleans.get(i).isCheck()) {
						loadFiles.add(booleans.get(i).getFile());
					}
				}
//				new Thread(new AddFileNeedUpLoadRightNow2(loadFiles, curcid,
//						context, handler3)).start();
				dialog.dismiss();
				showDialog();
				}else {
						Toast.makeText(context, "���粻����", 0).show();
				}
			}
		});
		builder.create().show();
	}

	public boolean canLoaded(File localFile) {
		return flag;
		//SQLUtils sqlUtils = new SQLUtils(context);
//		if (sqlUtils.findByNPid(localFile.getName(), curcid) != null) {
//			return false;
//		} else {
//			return true;
//		}
	}


	public void quit() {
		dis();
		for (int i = 0; i < booleans.size(); i++) {
			booleans.get(i).setCheck(false);
		}
		flag = false;
		check(booleans);
		all.setText("ȫѡ");
		message = new Message();
		message.what = 1;
		message.obj = booleans;
		handler.sendMessage(message);

	}

	public void selsvtAll() {
		if (!flag) {
			flag = true;
			for (int i = 0; i < booleans.size(); i++) {
				booleans.get(i).setCheck(true);
			}
			all.setText("ȫ��ѡ");
		} else {
			flag = false;
			all.setText("ȫѡ");
			for (int i = 0; i < booleans.size(); i++) {
				booleans.get(i).setCheck(false);
			}
		}
		check(booleans);
		message = new Message();
		message.what = 2;
		message.obj = booleans;
		handler.sendMessage(message);
	}

}
