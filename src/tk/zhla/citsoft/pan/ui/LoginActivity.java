package tk.zhla.citsoft.pan.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.application.UILApplication;
import tk.zhla.citsoft.pan.net.ExecRunable;
import tk.zhla.citsoft.pan.net.HttpUtils;
import tk.zhla.citsoft.pan.net.NetworkUtils;
import tk.zhla.citsoft.pan.parse.LoginParse;
import tk.zhla.citsoft.pan.parse.entity.LoginEntity;
import tk.zhla.citsoft.pan.share.ShareUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import tk.zhla.citsoft.pan.utils.Logs;
import tk.zhla.citsoft.pan.utils.ToastUtils;
import tk.zhla.citsoft.pan.view.CircularProgressButton;

import com.lockscreen.pattern.OffUnlockPasswordActivity;
import com.lockscreen.pattern.UnlockGesturePasswordActivity;

public class LoginActivity extends Activity {

	private CircularProgressButton btn_login = null;

	private EditText et_pwd;

	private EditText et_name;

	private EditText et_url;

	private TextView tv_forgot;
	private ProgressDialog dialog = null;

	private Button show;
	private boolean switcherState = false;
	private ImageView mgb = null;

	private boolean isLogining = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ShareUtils shareUtils = new ShareUtils(this);
		// if(shareUtils.isAutoLogin()){
		// gotoMain();
		// }
		initViews();
		setListener();
		dialog = new ProgressDialog(LoginActivity.this);
		dialog.setTitle("实验室网盘");
		if (UILApplication.getInstance().getLockPatternUtils()
				.savedPatternExists()) {
			if (!getIntent().getBooleanExtra("unlock", false)) {
				Intent i = new Intent(this, UnlockGesturePasswordActivity.class);
				startActivity(i);
				finish();
			}

		}

	}

	private void setListener() {

		btn_login.setIndeterminateProgressMode(true);
		btn_login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				canLogin = true;
				if (isLogining) {
					return;
				}
				if (TextUtils.isEmpty(et_url.getText().toString())) {
					share(et_url.getId());
					return;
				}
				if (TextUtils.isEmpty(et_name.getText().toString())) {
					share(et_name.getId());
					return;
				}
				if (TextUtils.isEmpty(et_pwd.getText().toString())) {
					share(et_pwd.getId());
					return;
				}

				if (!NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
					ToastUtils.toast(LoginActivity.this, "锟斤拷锟界不锟斤拷锟斤拷");
					return;
				}

				// 网络情况显示

				btn_login.setProgress(50);
				// dialog.show();
				isLogining = true;
				ExecRunable.execRun(new Thread() {
					@Override
					public void run() {
						String url = et_url.getText().toString()
								+ "/app/client.php" + "?action=login"
								+ "&username=" + et_name.getText().toString()
								+ "&password=" + et_pwd.getText().toString()
								+ "";
						Message msg = new Message();
						try {
							String result = HttpUtils.GetStringForHttpGet(null,
									null, url, 1);
							try {
								LoginEntity entity = new LoginParse()
										.parse(result);
								if (entity.isState()) {
									msg.what = 0;
									msg.obj = entity;
								} else {
									msg.what = -1;
									msg.obj = result + "账号或密码错误";
								}

							} catch (Exception e) {
								msg.what = -1;
								msg.obj = "账号或密码错误";
							}

						} catch (Exception e) {
							msg.what = -1;
							msg.obj = "网络错误";
						}
						if (canLogin)
							loginHandler.sendMessage(msg);

					}
				});

			}
		});
		tv_forgot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this, FindPwdActivity.class);
				startActivity(i);
			}
		});
		show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (f) {
					show.setText("隐藏");
					show.setBackgroundResource(R.drawable.qihoo_accounts_btn_show_pressed);
					et_pwd.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
					et_pwd.setSelection(et_pwd.getText().toString().length());
					f = false;
				} else {
					show.setText("显示");
					show.setBackgroundResource(R.drawable.qihoo_accounts_btn_show_normal);
					String s = et_pwd.getText().toString();
					et_pwd.setSelection(s.length());
					et_pwd.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
					et_pwd.setSelection(et_pwd.getText().toString().length());
					et_pwd.postInvalidate();
					f = true;
				}
			}
		});

		mgb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switcherChange();
			}
		});
	}

	public void gotoMain() {
		Intent i = new Intent(this, MainFragmentActivity.class);
		startActivity(i);
		finish();
	}

	private void switcherChange() {
		switcherState = !switcherState;
		if (switcherState) {
			mgb.setImageResource(R.drawable.check_on);
		} else {
			mgb.setImageResource(R.drawable.check_off);
		}
	}

	private Handler loginHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (!canLogin) {
				return;
			}
			isLogining = false;
			switch (msg.what) {

			case 0:// 登陆成功
				Logs.log(msg.obj.toString());
				ShareUtils shareUtils = new ShareUtils(LoginActivity.this);
				shareUtils.setLoginEntity((LoginEntity) (msg.obj));
				shareUtils.setURL(et_url.getText().toString());
				shareUtils.setAutoLogin(switcherState);
				shareUtils.setUsername(et_name.getText().toString());
				shareUtils.setPwd(et_pwd.getText().toString());
				gotoMain();
				break;
			case -1:
				btn_login.setProgress(-1);
				loginHandler.sendEmptyMessageDelayed(2, 2000);
				Logs.log(msg.obj.toString());
				ToastUtils.toast(LoginActivity.this, msg.obj.toString());
				// 失锟斤拷
				break;
			case 2:

				btn_login.setProgress(0);
				break;
			}
			dialog.dismiss();
		};

	};
	private boolean f = true;

	private void initViews() {
		btn_login = (CircularProgressButton) findViewById(R.id.log_btn_log);
		et_name = (EditText) findViewById(R.id.log_name);
		et_pwd = (EditText) findViewById(R.id.log_pwd);
		et_url = (EditText) findViewById(R.id.log_server_path);
		tv_forgot = (TextView) findViewById(R.id.log_tv_forgrt);
		mgb = (ImageView) this.findViewById(R.id.tv_forget_password);
		show = (Button) findViewById(R.id.log_btn_show);
		ShareUtils shareUtils = new ShareUtils(this);
		if (shareUtils.isAutoLogin()) {
			et_name.setText(shareUtils.getUsername());
			et_pwd.setText(shareUtils.getPwd());
			et_url.setText(shareUtils.getURL());
			switcherChange();
		}

	}

	public void share(int _id) {
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		findViewById(_id).startAnimation(shake);
	}

	private boolean canLogin = true;

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		canLogin = false;
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && isLogining) {
			if (canLogin == false) {
			} else {
				isLogining = false;
				canLogin = false;
				btn_login.setProgress(0);
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// 锟斤拷锟斤拷锟劫达拷锟斤拷转
		super.onDestroy();
	}

}
