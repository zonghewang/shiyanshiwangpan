package tk.zhla.citsoft.pan.ui.dialog;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.ui.fragment.FileListFragment;
import android.app.Activity;
import android.app.Dialog;
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


public class DirCreateDialog {

	private Dialog d = null;

	private View view = null;

	private Button yes = null;

	private Button no = null;



	private EditText text;
	private FileListFragment fragment;


	public DirCreateDialog(FileListFragment fragment) {
		
		this.fragment = fragment;
		
		d = new Dialog(fragment.getActivity());

		view = View.inflate(fragment.getActivity(), R.layout.jd_menu_more_new_file, null);

		d.setContentView(view);

		text = (EditText) d.findViewById(R.id.jd_rename_name);

		yes = (Button) d.findViewById(R.id.jd_menu_more_new_file_yes);

		no = (Button) d.findViewById(R.id.jd_menu_more_new_file_quit);

		init();
	}

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
				if (name == null || name.trim().equals("")) {
					Toast.makeText(fragment.getActivity(), "���ֲ���Ϊ��", 0)
							.show();
					return;
				}
				fragment.createDir(name);
				d.dismiss();
				
			}
		});
		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("no");
				d.dismiss();
			}
		});
		d.show();

	}

	private int getWindowHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) fragment.getActivity()).getWindowManager().getDefaultDisplay()
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
