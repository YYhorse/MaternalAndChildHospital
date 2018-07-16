package com.example.maternalandchildhospital.activity;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.async.CheckVerifyCodeAsync;
import com.example.maternalandchildhospital.async.SendVerifyCodeAsync;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * @author hxc
 *         <p>
 *         忘记密码
 */
public class ForgetPswActivity extends Activity implements OnClickListener {

	private static final String CODE_FLAG = "forgetPwd";

	private LinearLayout llReturn;
	private ImageView ivBack;

	private TextView tvTitle;

	private Button btnNext;

	private EditText etPhonenum;
	private ImageView ivDelPhonenum;

	private EditText etCode;
	private ImageView ivDelCode;
	private Button btnGetCode;

	InputMethodManager manager;

	private String phoneNum = "";
	private String code = "";
	private String tempCode = "";// 临时code 用来保存服务器返回，判断 注册按钮是否显示

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_psw);
		if (!CacheActivityManager.activityList.contains(ForgetPswActivity.this)) {
			CacheActivityManager.addActivity(ForgetPswActivity.this);
		}
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub

		llReturn = (LinearLayout) findViewById(R.id.title_ll_back);
		llReturn.setOnClickListener(this);

		ivBack = (ImageView) findViewById(R.id.title_iv_back);
		ivBack.setImageResource(R.drawable.btn_back1);

		tvTitle = (TextView) findViewById(R.id.title_tv_name);
		tvTitle.setText("验证手机");

		btnNext = (Button) findViewById(R.id.btn_next_forget_psw);
		btnNext.setOnClickListener(this);

		ivDelPhonenum = (ImageView) findViewById(R.id.iv_del_phonenum_forget_psw);
		etPhonenum = (EditText) findViewById(R.id.et_phonenum_forget_psw);
		etPhonenum.addTextChangedListener(Utils.getTextWatcher(etPhonenum, ivDelPhonenum));

		etCode = (EditText) findViewById(R.id.et_code_forget_psw);
		ivDelCode = (ImageView) findViewById(R.id.iv_del_code_forget_psw);
		etCode.addTextChangedListener(Utils.getTextWatcher(etCode, ivDelCode));

		btnGetCode = (Button) findViewById(R.id.btn_get_code_forget_psw);
		btnGetCode.setOnClickListener(this);

		etPhonenum.addTextChangedListener(watcher);
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
			String code = etCode.getText().toString().trim();

			if (Utils.showBtnTelJudge(ForgetPswActivity.this, tempPhone) && !Utils.strNullMeans(code) && code.equals(tempCode)) {

				btnNext.setBackgroundColor(getResources().getColor(R.color.color_dark_orange));
				btnNext.setEnabled(true);

			} else {
				btnNext.setBackgroundColor(getResources().getColor(R.color.color_txt_light_grey));
				btnNext.setEnabled(false);
			}

		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (!Utils.isFastDoubleClick()) {
			return;
		}
		if (v == btnNext) {
			phoneNum = etPhonenum.getText().toString().trim();
			code = etCode.getText().toString().trim();

			if (!Utils.telJudge(this, phoneNum)) {
				return;
			}
			if (Utils.strNullMeans(code)) {
				Utils.Toast(this, "请输入验证码");
				return;
			}
			new CheckVerifyCodeAsync(phoneNum, code, CODE_FLAG, this, new UpdateUi() {

				@Override
				public void updateUI(Object ob) {
					String result = (String) ob;
					if (!Utils.strNullMeans(result)) {
						Intent intent = new Intent(ForgetPswActivity.this, ResettingActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("phoneNum", phoneNum);
						intent.putExtras(bundle);
						startActivity(intent);
					}

				}
			}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		} else if (v == btnGetCode) {

			if (GlobalInfo.validataButtonTag) {
				phoneNum = etPhonenum.getText().toString().trim();
				if (!Utils.telJudge(this, phoneNum)) {
					return;
				}
				new SendVerifyCodeAsync(phoneNum, CODE_FLAG, this, new UpdateUi() {

					@Override
					public void updateUI(Object ob) {
						if (ob == null) {
							Utils.Toast(ForgetPswActivity.this, "获取验证码失败");
						} else {
							tempCode = (String) ob;
						}
					}
				}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

				GlobalInfo.validataButtonTag = false;
				btnGetCode.setText("获取验证码（60）");
				Utils.newBtnNum();
				GlobalInfo.vb.setButton(btnGetCode);

			} else {
				Utils.ShowPromptDialog(this, 1, "提示", "请过" + GlobalInfo.remainTime + "秒后再获取短信验证码", "确定");
			}

		} else if (v == llReturn) {

			CacheActivityManager.finishSingleActivity(this);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("ForgetPswActivity");
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("ForgetPswActivity");
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
