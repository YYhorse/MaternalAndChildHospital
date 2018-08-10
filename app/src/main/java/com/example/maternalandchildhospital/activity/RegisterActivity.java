package com.example.maternalandchildhospital.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.async.CheckVerifyCodeAsync;
import com.example.maternalandchildhospital.async.RegisterAsync;
import com.example.maternalandchildhospital.async.SendVerifyCodeAsync;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.net.HttpxUtils.HttpxUtils;
import com.example.maternalandchildhospital.net.HttpxUtils.SendCallBack;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Md5Util;
import com.example.maternalandchildhospital.publics.util.PopMessageUtil;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;

import cn.jpush.android.api.JPushInterface;

/**
 * @author hxc
 *         <p>
 *         登录界面
 */
public class RegisterActivity extends Activity implements OnClickListener {

	private static final String CODE_FLAG = "userRegister";

	private TextView tvTitle;

	private Button btnRegister;

	private EditText etPhonenum;
	private ImageView ivDelPhonenum;
	private EditText etPsw;
	private ImageView ivDelPsw;
	private ImageView ivShowPsw;
	private boolean isShow = false;

	private EditText etCode;
	private ImageView ivDelCode;
	private Button btnGetCode;

	private ImageView ivHint;
	private boolean isCheckHint = true;
	private TextView tvHint;
	private TextView tvLogin;

	private String phoneNum = "";
	private String psw = "";
	private String code = "";
	private String tempCode = "";// 临时code 用来保存服务器返回，判断 注册按钮是否显示
	private String pushId = "";

	InputMethodManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		if (!CacheActivityManager.activityList.contains(RegisterActivity.this)) {
			CacheActivityManager.addActivity(RegisterActivity.this);
		}

		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		pushId = JPushInterface.getRegistrationID(getApplicationContext());
		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub

		tvTitle = (TextView) findViewById(R.id.title_tv_name);
		tvTitle.setText("注册");

		btnRegister = (Button) findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(this);

		ivDelPhonenum = (ImageView) findViewById(R.id.iv_del_phonenum_register);
		etPhonenum = (EditText) findViewById(R.id.et_phonenum_register);
		etPhonenum.addTextChangedListener(Utils.getTextWatcher(etPhonenum, ivDelPhonenum));

		etPsw = (EditText) findViewById(R.id.et_psw_register);

		ivDelPsw = (ImageView) findViewById(R.id.iv_del_psw_register);
		etPsw.addTextChangedListener(Utils.getTextWatcher(etPsw, ivDelPsw));
		ivShowPsw = (ImageView) findViewById(R.id.iv_show_psw_register);
		ivShowPsw.setOnClickListener(this);

		etCode = (EditText) findViewById(R.id.et_code_register);
		ivDelCode = (ImageView) findViewById(R.id.iv_del_code_register);
		etCode.addTextChangedListener(Utils.getTextWatcher(etCode, ivDelCode));
		btnGetCode = (Button) findViewById(R.id.btn_get_code_register);
		btnGetCode.setOnClickListener(this);

		ivHint = (ImageView) findViewById(R.id.iv_hint_register);
		ivHint.setOnClickListener(this);
		tvHint = (TextView) findViewById(R.id.tv_hint_register);
		tvHint.setOnClickListener(this);
		tvLogin = (TextView) findViewById(R.id.tv_login_register);
		tvLogin.setOnClickListener(this);

		etPhonenum.addTextChangedListener(watcher);
		etPsw.addTextChangedListener(watcher);
		etCode.addTextChangedListener(watcher);

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
			String code = etCode.getText().toString().trim();

			if (Utils.showBtnTelJudge(RegisterActivity.this, tempPhone) && Utils.showBtnPswJudge(RegisterActivity.this, tempPsw) && !Utils.strNullMeans(code) && code.equals(tempCode)) {

				btnRegister.setBackgroundColor(getResources().getColor(R.color.color_dark_orange));
				btnRegister.setEnabled(true);

			} else {
				btnRegister.setBackgroundColor(getResources().getColor(R.color.color_txt_light_grey));
				btnRegister.setEnabled(false);
			}

		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (!Utils.isFastDoubleClick()) {
			return;
		}
		if (v == btnRegister) {

			phoneNum = etPhonenum.getText().toString().trim();
			psw = etPsw.getText().toString().trim();
			code = etCode.getText().toString().trim();

			if (!Utils.telJudge(this, phoneNum)) {
				return;
			}
			if (!Utils.pswJudge(this, psw)) {
				return;
			}
			if (Utils.strNullMeans(code)) {
				Utils.Toast(this, "请输入验证码");
				return;
			}

			if (!isCheckHint) {
				Utils.Toast(this, "请同意服务协议");
				return;
			}

			new CheckVerifyCodeAsync(phoneNum, code, CODE_FLAG, this, new UpdateUi() {

				@Override
				public void updateUI(Object ob) {
					String result = (String) ob;
					if (!Utils.strNullMeans(result)) {
						new RegisterAsync(phoneNum, psw, pushId, code, RegisterActivity.this, new UpdateUi() {

							@Override
							public void updateUI(Object ob) {

								Utils.saveLocalInfo(RegisterActivity.this, GlobalInfo.SETTING, GlobalInfo.LOGINPHONE, phoneNum);
								Utils.saveLocalInfo(RegisterActivity.this, GlobalInfo.SETTING, GlobalInfo.LOGINPSW, psw);
								Utils.saveLocalInfo(RegisterActivity.this, GlobalInfo.SETTING, GlobalInfo.AUTOLOGIN, "true");
								Intent intent = new Intent(RegisterActivity.this, InputInfoActivity.class);
								startActivity(intent);
								CacheActivityManager.finishSingleActivity(RegisterActivity.this);

							}
						}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}

				}
			}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		} else if (v == ivHint) {
			isCheckHint = isCheckHint ^ true;
			if (isCheckHint) {
				ivHint.setImageResource(R.drawable.btn_register_checked);
			} else {
				ivHint.setImageResource(R.drawable.btn_register_unchecked);
			}

		} else if (v == tvHint) {// 用户协议

			Intent intent = new Intent(this, RegisterHintActivity.class);
			startActivity(intent);

		} else if (v == tvLogin) {
			CacheActivityManager.finishSingleActivity(this);

		} else if (v == btnGetCode) {

			if (GlobalInfo.validataButtonTag) {
				String phoneNum = etPhonenum.getText().toString().trim();
				if (!Utils.telJudge(this, phoneNum)) {
					return;
				}
				new SendVerifyCodeAsync(phoneNum, CODE_FLAG, this, new UpdateUi() {

					@Override
					public void updateUI(Object ob) {
						if (ob == null) {
							Utils.Toast(RegisterActivity.this, "获取验证码失败");
						} else {
							tempCode = (String) ob;
						}
					}
				}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

				//进行预约网络请求
				String CheckStr = "phoneNumber=" + phoneNum
						+ "&codeType=userRegister";
				String MD5String = Md5Util.generateMD5String(CheckStr);
				String JsonString = "{\"check\":\"" + MD5String + "\",\"json\":{"
						+ "\"phoneNumber\":\"" + phoneNum +"\""+
						",\"codeType\":\"userRegister\""+
						"}}";
				PopMessageUtil.Log(JsonString);

				//网络请求
				HttpxUtils.postHttp(new SendCallBack() {
					@Override
					public void onSuccess(String result) {
						PopMessageUtil.Log("短信接口返回：" + result);
						GlobalInfo.validataButtonTag = false;
						btnGetCode.setText("获取验证码（60）");
						Utils.newBtnNum();
						GlobalInfo.vb.setButton(btnGetCode);
					}

					public void onError(Throwable ex, boolean isOnCallback) {
						PopMessageUtil.Log("短信接口服务器返回：" + ex.getMessage());
						ex.printStackTrace();
					}

					public void onCancelled(Callback.CancelledException cex) {}
					public void onFinished() {}
				}).setUrl("http://fuyouapi.ichees.com/apiNew/public/index.php/api/SendcodeInfo/sendCode")
						.addJsonParameter(JsonString)
						.send();
			} else {
				Utils.ShowPromptDialog(this, 1, "提示", "请过" + GlobalInfo.remainTime + "秒后再获取短信验证码", "确定");
			}

		} else if (v == ivShowPsw) {

			isShow = isShow ^ true;

			if (isShow) {
				etPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				ivShowPsw.setImageResource(R.drawable.btn_display);
			} else {
				etPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
				ivShowPsw.setImageResource(R.drawable.btn_hide);
			}
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("RegisterActivity");
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("RegisterActivity");
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
