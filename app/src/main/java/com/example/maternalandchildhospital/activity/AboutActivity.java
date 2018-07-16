package com.example.maternalandchildhospital.activity;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutActivity extends Activity implements OnClickListener {

	private LinearLayout llReturn;
	private TextView tvRegisterHint;
	private TextView tvVersionNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		if (!CacheActivityManager.activityList.contains(AboutActivity.this)) {
			CacheActivityManager.addActivity(AboutActivity.this);
		}
		findViews();
		init();
	}

	private void init() {
		try {
			String version = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
			tvVersionNumber.setText("V" + version);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void findViews() {
		llReturn = (LinearLayout) findViewById(R.id.title_ll_back);
		llReturn.setOnClickListener(this);
		tvRegisterHint = (TextView) findViewById(R.id.tv_register_hint);
		tvRegisterHint.setOnClickListener(this);
		tvVersionNumber = (TextView) findViewById(R.id.tv_version_number);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("AboutActivity");
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("AboutActivity");
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		if (!Utils.isFastDoubleClick()) {
			return;
		}
		if (v == llReturn) {
			CacheActivityManager.finishSingleActivity(this);
		} else if (v == tvRegisterHint) {
			Intent intent = new Intent(AboutActivity.this, RegisterHintActivity.class);
			startActivity(intent);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			CacheActivityManager.finishSingleActivity(this);
			return false;
		}
		return false;
	}
}
