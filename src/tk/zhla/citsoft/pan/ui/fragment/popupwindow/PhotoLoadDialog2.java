package tk.zhla.citsoft.pan.ui.fragment.popupwindow;

import java.util.ArrayList;
import java.util.List;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.parse.entity.FilePathEntity;
import tk.zhla.citsoft.pan.parse.entity.FilesListEntity;
import tk.zhla.citsoft.pan.ui.dialog.PhotoLoadDialogOther2;
import tk.zhla.citsoft.pan.ui.dialog.PhotoLoadDialogOther2.OnConfirmListener;
import tk.zhla.citsoft.pan.ui.fragment.FileListFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;


public class PhotoLoadDialog2 extends Dialog implements OnClickListener {

	private Dialog d = null;

	private View view = null;

	private LinearLayout one, two, three, four;

	private Context context;


	private int height = 0;
	private Activity activity;


	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_load_dialog2);
		two = (LinearLayout) findViewById(R.id.photo_load_dialog_two);
		three = (LinearLayout) findViewById(R.id.photo_load_dialog_three);
		four = (LinearLayout) findViewById(R.id.photo_load_dialog_four);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
	};
	
	private FileListFragment fragment;

	public PhotoLoadDialog2(FileListFragment fragment) {
		super(fragment.getActivity());
		this.activity = fragment.getActivity();
		this.fragment = fragment;
		this.context = fragment.getActivity();

		
	}

	

	public void showMyDialog() {

		Window window = getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.mystyle); // ��Ӷ���
		show();
		WindowManager windowManager = activity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (display.getWidth());
		getWindow().setAttributes(params);
		
		window
				.setBackgroundDrawableResource(R.drawable.background_dialog);


	}



	private int getWindowHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	private void dialogAnimation(final Dialog d, View v, int from, int to,
			final boolean needDismiss) {
		if(needDismiss){
			dismiss();
		}else {
			show();
		}
			
	}


	@Override
	public void onClick(View v) {
		dialogAnimation(d, view, height, getWindowHeight(), true);
		switch (v.getId()) {

		case R.id.photo_load_dialog_two:
			if (onConfirmListener!=null) {
				FilesListEntity entity = new FilesListEntity();
				entity.setCid(0);
				List<FilePathEntity> entities = new ArrayList<FilePathEntity>();
				entities.add(new FilePathEntity("�����ļ�", -1, 0, -1));
				entity.setFilePathEntities(entities);
				onConfirmListener.confirm(entity);
			}
			break;
		case R.id.photo_load_dialog_three:
			new PhotoLoadDialogOther2(fragment.getActivity(),fragment.getFilesListEntity()).setOnConfirmListener(new OnConfirmListener() {
				public void confirm(FilesListEntity entity) {
					if (onConfirmListener!=null) {
						onConfirmListener.confirm(entity);
					}
				}
			});;
			
			break;
		case R.id.photo_load_dialog_four:
			break;

		default:
			break;
		}

	}
	
	private OnConfirmListener1 onConfirmListener = null;

	public interface OnConfirmListener1 {
		public void confirm(FilesListEntity entity);
	}

	public void setOnConfirmListener(OnConfirmListener1 onConfirmListener) {
		this.onConfirmListener = onConfirmListener;
	}

}
