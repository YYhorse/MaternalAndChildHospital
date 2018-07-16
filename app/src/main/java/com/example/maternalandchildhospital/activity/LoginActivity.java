package com.example.maternalandchildhospital.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.async.LoginAsync;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * @author hxc
 *         <p>
 *         登录界面
 */
public class LoginActivity extends Activity implements OnClickListener {

	private TextView tvTitle;

	private Button btnLogin;

	private EditText etPhonenum;
	private ImageView ivDelPhonenum;
	private EditText etPsw;
	private ImageView ivDelPsw;

	private TextView tvForgetPsw;
	private TextView tvRegister;

	private String loginName = "";

	private String password = "";
	private String pushId = "";

	private String oldPhoneNumber = "";
	private String oldPassword = "";
	private String autoLogin = "";

	private boolean otherLogin = false;

	InputMethodManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (!CacheActivityManager.activityList.contains(LoginActivity.this)) {
			CacheActivityManager.addActivity(LoginActivity.this);
		}

		pushId = JPushInterface.getRegistrationID(getApplicationContext());
		GlobalInfo.init();
		try {
			GlobalInfo.VERSION = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String urlType = getString(R.string.publish_environment);
		if (urlType.equals("dev")) {
			GlobalInfo.base_url = GlobalInfo.devUrl;
		} else if (urlType.equals("test")) {
			GlobalInfo.base_url = GlobalInfo.testUrl;
		} else if (urlType.equals("prod")) {
			Utils.showLog = false;
			GlobalInfo.base_url = GlobalInfo.prodUrl;
		}

		initView();
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

	}

	private void initView() {
		// TODO Auto-generated method stub

		tvTitle = (TextView) findViewById(R.id.title_tv_name);
		tvTitle.setText("登录");

		btnLogin = (Button) findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(this);

		ivDelPhonenum = (ImageView) findViewById(R.id.iv_del_phonenum_login);
		etPhonenum = (EditText) findViewById(R.id.et_phonenum_login);
		etPhonenum.addTextChangedListener(Utils.getTextWatcher(etPhonenum, ivDelPhonenum));

		etPsw = (EditText) findViewById(R.id.et_psw_login);

		ivDelPsw = (ImageView) findViewById(R.id.iv_del_psw_login);
		etPsw.addTextChangedListener(Utils.getTextWatcher(etPsw, ivDelPsw));

		tvForgetPsw = (TextView) findViewById(R.id.tv_forget_psw_login);
		tvForgetPsw.setOnClickListener(this);
		tvRegister = (TextView) findViewById(R.id.tv_register_login);
		tvRegister.setOnClickListener(this);

		// 按钮是否变亮
		etPhonenum.addTextChangedListener(watcher);
		etPsw.addTextChangedListener(watcher);

	}

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			String tempPhone = etPhonenum.getText().toString().trim();
			String tempPsw = etPsw.getText().toString().trim();

			if (Utils.showBtnTelJudge(LoginActivity.this, tempPhone) && Utils.showBtnPswJudge(LoginActivity.this, tempPsw)) {

				btnLogin.setBackgroundColor(getResources().getColor(R.color.color_dark_orange));
				btnLogin.setEnabled(true);

			} else {
				btnLogin.setBackgroundColor(getResources().getColor(R.color.color_txt_light_grey));
				btnLogin.setEnabled(false);
			}

		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (!Utils.isFastDoubleClick()) {
			return;
		}
		if (v == btnLogin) {

			// Intent intent = new Intent(this, DueDateActivity.class);
			// startActivity(intent);

			loginName = etPhonenum.getText().toString().trim();
			password = etPsw.getText().toString().trim();

			if (!Utils.telJudge(this, loginName)) {
				return;
			}
			if (!Utils.pswJudge(this, password)) {
				return;
			}

			new LoginAsync(true, loginName, password, pushId, this, 0, new UpdateUi() {

				@Override
				public void updateUI(Object ob) {
					// TODO Auto-generated method stub

					if ("111".equals(ob)) {
						return;
					}
					Utils.saveLocalInfo(LoginActivity.this, GlobalInfo.SETTING, GlobalInfo.LOGINPHONE, loginName);
					Utils.saveLocalInfo(LoginActivity.this, GlobalInfo.SETTING, GlobalInfo.LOGINPSW, password);
					Utils.saveLocalInfo(LoginActivity.this, GlobalInfo.SETTING, GlobalInfo.AUTOLOGIN, "true");

					Utils.Toast(LoginActivity.this, "登录成功");
					// 如果没有输入信息，登录的时候，仍然进入输入信息页
					if (!Utils.strNullMeans((GlobalInfo.userInfo.getYunId()))) {
						Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
						startActivity(intent);
					} else {
						Intent intent = new Intent(LoginActivity.this, InputInfoActivity.class);
						startActivity(intent);
					}
					CacheActivityManager.finishSingleActivity(LoginActivity.this);
					// 极光推送设置别名
					// JPushInterface.setAliasAndTags(getBaseContext(),
					// GlobalInfo.userInfo.getUserId() +
					// GlobalInfo.userInfo.getImVoip(), null);

				}
			}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			//
		} else if (v == tvForgetPsw) {
			Intent intent = new Intent(this, ForgetPswActivity.class);
			startActivity(intent);
		} else if (v == tvRegister) {
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			// Intent intent = new Intent(this, InputInfoActivity.class);
			// startActivity(intent);

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("LoginActivity");
		MobclickAgent.onResume(this);

		oldPhoneNumber = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.LOGINPHONE);
		oldPassword = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.LOGINPSW);
		autoLogin = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.AUTOLOGIN);

		etPhonenum.setText(oldPhoneNumber);
		etPsw.setText(oldPassword);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("LoginActivity");
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			CacheActivityManager.finishSingleActivity(this);
			return false;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}

}
