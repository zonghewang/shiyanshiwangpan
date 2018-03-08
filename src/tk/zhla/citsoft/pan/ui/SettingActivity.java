package tk.zhla.citsoft.pan.ui;

import java.io.File;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.ui.dialog.DialogPopupFromBottom2;
import tk.zhla.citsoft.pan.ui.dialog.DialogPopupFromBottom2.OnConfirmListener;
import tk.zhla.citsoft.pan.utils.TimeAndSizeUtil;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;






import com.nostra13.universalimageloader.utils.StorageUtils;

public class SettingActivity extends Activity implements OnClickListener {

	private View clear = null;

	private ViewGroup v = null;

	private View about = null;

	private View update = null;

	private View back = null;
	private ProgressDialog pd = null;

	private TextView settingROM;

	public Handler checkUpdateHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case -1:

				break;
			case 0:
				pd.dismiss();
				createUpdateDialog();
				break;
			case 1:

				break;

			default:
				break;
			}

		}

	};

	private File file;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		v = (ViewGroup) View.inflate(this, R.layout.setting_layout, null);
		this.setContentView(v);
		file = StorageUtils.getCacheDirectory(SettingActivity.this);
		initView();

	};

	private void initView() {

		clear = this.findViewById(R.id.settingClearROM);
		clear.setOnClickListener(this);

		about = this.findViewById(R.id.settingAbout);
		about.setOnClickListener(this);

		update = this.findViewById(R.id.settingCheckUpdate);
		update.setOnClickListener(this);

		back = this.findViewById(R.id.settingBack);
		back.setOnClickListener(this);
		settingROM = (TextView) findViewById(R.id.settingROM);
		size = 0;
		getFileSize(file);
		settingROM.setText(TimeAndSizeUtil.getSize(size + ""));
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.settingAbout:
			Intent aboutIntent = new Intent(this, AboutActivity.class);
			startActivity(aboutIntent);
			break;

		case R.id.settingBack:
			finish();
			break;
		case R.id.settingClearROM:
			createDialog();
			break;

		case R.id.settingCheckUpdate:
			pd = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
			pd.setMessage("正在获取最新版本");
			//pd.show();
			Toast.makeText(this, "当前是最新版本", 0).show();
			// 更新检查
			new Thread() {
				public void run() {
					SystemClock.sleep(4000);
				//checkUpdateHandler.sendEmptyMessage(0);
				};

			}.start();
			break;

		default:
			break;
		}

	}


	private void createUpdateDialog() {
		if (SettingActivity.this != null) {
			final Dialog d = new Dialog(this);
			d.setContentView(R.layout.lz_update);
			Button b = (Button) d.findViewById(R.id.settingUpdate0Button);

			Window dialogWindow = d.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			lp.width = LayoutParams.MATCH_PARENT;
			lp.height = LayoutParams.WRAP_CONTENT;
			lp.x = 0;
			dialogWindow
					.setBackgroundDrawableResource(R.drawable.background_dialog);
			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					d.dismiss();
				}
			});
			d.show();
		}

	};

	private void createDialog() {

		DialogPopupFromBottom2 bottom = new DialogPopupFromBottom2(this);
		bottom.setOnConfirmListener(new OnConfirmListener() {

			@Override
			public void confirm() {

				clearCache(file);
				size = 0;
				getFileSize(file);
				settingROM.setText(TimeAndSizeUtil.getSize(size + ""));
				Toast.makeText(SettingActivity.this, "清理完成", 0).show();
			}
		});
		bottom.showMyDialog();

	}

	private long size;

	public void clearCache(File f) {
		if (f.isDirectory()) {
			File[] fs = f.listFiles();
			for (int i = 0; i < fs.length; i++) {
				clearCache(fs[i]);
			}
		} else {
			f.delete();
		}
	}

	public void getFileSize(File f) {
		if (f.isDirectory()) {
			File[] fs = f.listFiles();
			for (int i = 0; i < fs.length; i++) {
				getFileSize(fs[i]);
			}
		} else {
			size += f.length();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}

		return super.onKeyDown(keyCode, event);
	}

}
