package com.example.maternalandchildhospital.activity;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.async.ModifiPwdAsync;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.example.maternalandchildhospital.publics.view.EnterDialog;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ModifyPswActivity extends Activity implements OnClickListener {
	private LinearLayout llReturn;
	private ImageView ivBack;

	private TextView tvTitle;

	private EditText etOldPsw;
	private ImageView ivDelOldPsw;

	private EditText etNewPsw;
	private ImageView ivDelNewPsw;

	private EditText etNewPsw2;
	private ImageView ivDelNewPsw2;

	private TextView tvCommit;

	InputMethodManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_psw);
		if (!CacheActivityManager.activityList.contains(ModifyPswActivity.this)) {
			CacheActivityManager.addActivity(ModifyPswActivity.this);
		}
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		findViews();
	}

	private void findViews() {
		llReturn = (LinearLayout) findViewById(R.id.title_ll_back);
		llReturn.setOnClickListener(this);

		ivBack = (ImageView) findViewById(R.id.title_iv_back);
		ivBack.setImageResource(R.drawable.btn_back2);

		tvTitle = (TextView) findViewById(R.id.title_tv_name);
		tvTitle.setText("修改密码");

		ivDelOldPsw = (ImageView) findViewById(R.id.iv_del_old_psw);
		etOldPsw = (EditText) findViewById(R.id.et_old_psw);
		etOldPsw.addTextChangedListener(Utils.getTextWatcher(etOldPsw, ivDelOldPsw));

		ivDelNewPsw = (ImageView) findViewById(R.id.iv_del_new_psw);
		etNewPsw = (EditText) findViewById(R.id.et_new_psw);
		etNewPsw.addTextChangedListener(Utils.getTextWatcher(etNewPsw, ivDelNewPsw));

		ivDelNewPsw2 = (ImageView) findViewById(R.id.iv_del_new_psw_2);
		etNewPsw2 = (EditText) findViewById(R.id.et_new_psw_2);
		etNewPsw2.addTextChangedListener(Utils.getTextWatcher(etNewPsw2, ivDelNewPsw2));

		tvCommit = (TextView) findViewById(R.id.tv_commit);
		tvCommit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!Utils.isFastDoubleClick()) {
			return;
		}
		if (v == llReturn) {
			CacheActivityManager.finishSingleActivity(this);
		} else if (v == tvCommit) {
			String oldPsw = etOldPsw.getText().toString().trim();
			String newPsw = etNewPsw.getText().toString().trim();
			String newPsw2 = etNewPsw2.getText().toString().trim();

			if (Utils.strNullMeans(oldPsw)) {
				Utils.Toast(this, "旧密码不能为空");
				return;
			}
			if (Utils.strNullMeans(newPsw) || Utils.strNullMeans(newPsw2)) {
				Utils.Toast(this, "新密码不能为空");
				return;
			}
			if (oldPsw.equals(newPsw)) {
				Utils.Toast(this, "新密码与旧密码相同，请重新输入");
				return;
			}
			if (!Utils.pswJudge(this, newPsw)) {
				return;
			}
			if (!newPsw.equals(newPsw2)) {
				Utils.Toast(this, "新密码两次不一致，请重新输入");
				return;
			}
			new ModifiPwdAsync(GlobalInfo.userInfo.getUserId(), GlobalInfo.userInfo.getUserSessionId(), "2", newPsw, "", oldPsw, this, new UpdateUi() {

				@Override
				public void updateUI(Object ob) {
					// TODO Auto-generated method stub
					String result = (String) ob;
					if ("Successed".equals(result)) {
						Utils.Toast(ModifyPswActivity.this, "密码修改成功");
						CacheActivityManager.finishSingleActivityByClass(ModifyPswActivity.class);
						Utils.saveLocalInfo(ModifyPswActivity.this, GlobalInfo.SETTING, GlobalInfo.LOGINPSW, "");
					} else {
						if (!Utils.strNullMeans(result)) {
							Utils.ShowEnterDialog(ModifyPswActivity.this, result, "", "确定", "", "提示", EnterDialog.MODE_SINGLE_BUTTON, null);
						}
					}
				}
			}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ModifyPswActivity");
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ModifyPswActivity");
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
