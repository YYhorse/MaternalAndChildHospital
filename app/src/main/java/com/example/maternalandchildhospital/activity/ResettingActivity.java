package com.example.maternalandchildhospital.activity;

import android.app.Activity;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.async.ModifiPwdAsync;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.example.maternalandchildhospital.publics.view.EnterDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * @author hxc
 *         <p>
 *         重置密码
 */
public class ResettingActivity extends Activity implements OnClickListener {

	private LinearLayout llReturn;
	private ImageView ivBack;

	private TextView tvTitle;

	private Button btnConfire;

	private EditText etPsw;
	private ImageView ivDelPsw;
	private ImageView ivShowPsw;
	private boolean isShow = false;

	// 忘记密码 userId userSessionId oldPassword 这三个值为空
	private String userId = "";
	private String userSessionId = "";
	private String transType = "1";
	private String oldPassword = "";
	private String phoneNum = "";
	private String newPassword = "";

	InputMethodManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resetting);
		if (!CacheActivityManager.activityList.contains(ResettingActivity.this)) {
			CacheActivityManager.addActivity(ResettingActivity.this);
		}
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			phoneNum = bundle.getString("phoneNum");
		}

		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub

		llReturn = (LinearLayout) findViewById(R.id.title_ll_back);
		llReturn.setOnClickListener(this);

		ivBack = (ImageView) findViewById(R.id.title_iv_back);
		ivBack.setImageResource(R.drawable.btn_back1);

		tvTitle = (TextView) findViewById(R.id.title_tv_name);
		tvTitle.setText("重置密码");

		btnConfire = (Button) findViewById(R.id.btn_confire_resetting);
		btnConfire.setOnClickListener(this);

		ivDelPsw = (ImageView) findViewById(R.id.iv_del_psw_resetting);
		etPsw = (EditText) findViewById(R.id.et_psw_resetting);
		etPsw.addTextChangedListener(Utils.getTextWatcher(etPsw, ivDelPsw));
		ivShowPsw = (ImageView) findViewById(R.id.iv_show_psw_resetting);
		ivShowPsw.setOnClickListener(this);

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
			String tempPsw = etPsw.getText().toString().trim();

			if (Utils.showBtnPswJudge(ResettingActivity.this, tempPsw)) {

				btnConfire.setBackgroundColor(getResources().getColor(R.color.color_dark_orange));
				btnConfire.setEnabled(true);

			} else {
				btnConfire.setBackgroundColor(getResources().getColor(R.color.color_txt_light_grey));
				btnConfire.setEnabled(false);
			}

		}
	};

	@Override
	public void onClick(View v) {

		if (!Utils.isFastDoubleClick()) {
			return;
		}
		if (v == btnConfire) {

			newPassword = etPsw.getText().toString().trim();

			if (!Utils.pswJudge(this, newPassword)) {
				return;
			}

			new ModifiPwdAsync(userId, userSessionId, transType, newPassword, phoneNum, oldPassword, this, new UpdateUi() {

				@Override
				public void updateUI(Object ob) {
					// TODO Auto-generated method stub
					String result = (String) ob;
					if ("Successed".equals(result)) {
						Utils.Toast(ResettingActivity.this, "密码重置成功");
						CacheActivityManager.finishSingleActivityByClass(ResettingActivity.class);
						CacheActivityManager.finishSingleActivityByClass(ForgetPswActivity.class);
						Utils.saveLocalInfo(ResettingActivity.this, GlobalInfo.SETTING, GlobalInfo.LOGINPSW, "");

					} else {
						if (!Utils.strNullMeans(result)) {
							Utils.ShowEnterDialog(ResettingActivity.this, result, "", "确定", "", "提示", EnterDialog.MODE_SINGLE_BUTTON, null);
						}
					}
				}
			}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		} else if (v == llReturn) {

			CacheActivityManager.finishSingleActivity(this);
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
		MobclickAgent.onPageStart("ResttingActivity");
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("ResttingActivity");
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
