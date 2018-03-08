package tk.zhla.citsoft.pan.ui.dialog;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.parse.entity.FileDataEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDataFatherEntity;
import tk.zhla.citsoft.pan.parse.entity.FileDirDataEntity;
import tk.zhla.citsoft.pan.ui.fragment.FileListFragment;
import tk.zhla.citsoft.pan.utils.RenameRun;
import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.text.Selection;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class JDDialogPopupRename {

	private Dialog d = null;

	private View view = null;

	private Button yes = null;

	private Button no = null;

	private EditText text;

	private FileListFragment fragment;


	private Handler handler;

	public JDDialogPopupRename(FileListFragment fragment 
			) {
		this.fragment = fragment;
		FileDataFatherEntity item = fragment.getCheckedFileDataFatherEntities().get(0);

		String n = item.getN();

		d = new Dialog(fragment.getActivity());

		view = View.inflate(fragment.getActivity(), R.layout.jd_pop_rename, null);

		d.setContentView(view);

		text = (EditText) d.findViewById(R.id.jd_rename_name);

		text.setText(n);

		Selection.selectAll(text.getText());

		yes = (Button) d.findViewById(R.id.jd_rename_yes);

		no = (Button) d.findViewById(R.id.jd_rename_no);

		init();
	}

	// private Handler handler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// if (msg.what == 3) {
	// AlterFileEntity entity = (AlterFileEntity) msg.obj;
	// if (entity.isState()) {
	// int fid = entity.getId();
	// for (int i = 0; i < items.size(); i++) {
	// if (items.get(i).getFid() == fid) {
	// sql.getSqlUtils().updateUserLocalFile(items.get(i));
	// }
	// }
	// sql.getAdapter().notifyDataSetChanged();
	// Toast.makeText(context, "操作完成", 0).show();
	// } else {
	// Toast.makeText(context, entity.getError(), 0).show();
	// }
	// } else if (msg.what == 4) {
	// AlterFolderEntity entity = (AlterFolderEntity) msg.obj;
	// if (entity.isState()) {
	// int cid = entity.getId();
	// for (int i = 0; i < items.size(); i++) {
	// if (items.get(i).getCid() == cid) {
	// sql.getSqlUtils().updateUserLocalFile(items.get(i));
	// }
	// }
	// sql.getAdapter().notifyDataSetChanged();
	// Toast.makeText(context, entity.getError(), 0).show();
	// } else {
	// Toast.makeText(context, entity.getError(), 0).show();
	// }
	// }
	// super.handleMessage(msg);
	// }
	// };

	private void init() {

		Window dialogWindow = d.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		lp.width = getWindowHeight() - 100;
		lp.height = LayoutParams.WRAP_CONTENT;
		dialogWindow
				.setBackgroundDrawableResource(R.drawable.background_dialog);

		final int height = lp.height;

		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				String name = text.getText().toString();
				
				if (name.trim().equals("")) {
					Toast.makeText(fragment.getActivity(), "名字不能为空", 0).show();
					return;
				} 
				if(fragment.getCheckedFileDataFatherEntities().get(0) instanceof FileDirDataEntity){
					FileDirDataEntity dataEntity = (FileDirDataEntity) fragment.getCheckedFileDataFatherEntities().get(0);
					RenameRun renameRun = new RenameRun(2, fragment, dataEntity.getCid(), name, handler,
							dataEntity);
					new Thread(renameRun).start();
				}else {
					FileDataEntity dataEntity = (FileDataEntity) fragment.getCheckedFileDataFatherEntities().get(0);
					RenameRun renameRun = new RenameRun(2, fragment, dataEntity.getFid(), name, handler,
							dataEntity);
					new Thread(renameRun).start();
				}
				
					
				
				d.dismiss();
			}
		});
		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				d.dismiss();
			}
		});
		d.show();

	}

	

	private int getWindowHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		fragment.getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	private OnConfirmListener onConfirmListener = null;

	public interface OnConfirmListener {
		public void confirm();
	}

	public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
		this.onConfirmListener = onConfirmListener;
	}

}
