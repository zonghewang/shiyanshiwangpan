package com.lockscreen.pattern;

import tk.zhla.citsoft.pan.R;
import tk.zhla.citsoft.pan.application.UILApplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


public class GuideGesturePasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesturepassword_guide);
		findViewById(R.id.gesturepwd_guide_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UILApplication.getInstance().getLockPatternUtils().clearLock();
				Intent intent = new Intent(GuideGesturePasswordActivity.this,
						CreateGesturePasswordActivity.class);
				// 打开新的Activity
				startActivity(intent);
				finish();
			}
		});
	}

}
