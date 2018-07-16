package com.example.maternalandchildhospital.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.async.CheckCardAsync;
import com.example.maternalandchildhospital.async.GetCommunityListAsync;
import com.example.maternalandchildhospital.async.LoginAsync;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.example.maternalandchildhospital.publics.view.ChooseStrDialog;
import com.example.maternalandchildhospital.publics.view.EnterDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * @author hxc
 *         <p>
 *         填写信息
 */
public class InputInfoActivity extends Activity implements OnClickListener {

	private LinearLayout llReturn;
	private ImageView ivBack;

	private TextView tvTitle;

	private Button btnNext;

	private EditText etName;
	private ImageView ivDelName;

	private EditText etId;
	private ImageView ivDelId;

	private RelativeLayout rlCommunity;
	private TextView tvCommunity;

	private ChooseStrDialog chooseCommunityDialog;

	private String userId = "";
	private String userSessionId = "";

	private List<String> communityList;

	private String name = "";
	private String idCard = "";
	private String community = "";

	InputMethodManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_info);
		if (!CacheActivityManager.activityList.contains(InputInfoActivity.this)) {
			CacheActivityManager.addActivity(InputInfoActivity.this);
		}
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		userId = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.USERID);
		userSessionId = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.USERSESSIONID);

		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub

		llReturn = (LinearLayout) findViewById(R.id.title_ll_back);
		llReturn.setOnClickListener(this);

		ivBack = (ImageView) findViewById(R.id.title_iv_back);
		ivBack.setImageResource(R.drawable.btn_back1);

		tvTitle = (TextView) findViewById(R.id.title_tv_name);
		tvTitle.setText("填写信息");

		btnNext = (Button) findViewById(R.id.btn_confire_input_info);
		btnNext.setOnClickListener(this);

		ivDelName = (ImageView) findViewById(R.id.iv_del_name_input_info);
		etName = (EditText) findViewById(R.id.et_name_input_info);
		etName.addTextChangedListener(Utils.getTextWatcher(etName, ivDelName));

		ivDelId = (ImageView) findViewById(R.id.iv_del_id_input_info);
		etId = (EditText) findViewById(R.id.et_id_input_info);
		etId.addTextChangedListener(Utils.getTextWatcher(etId, ivDelId));

		rlCommunity = (RelativeLayout) findViewById(R.id.rl_community_input_info);
		rlCommunity.setOnClickListener(this);
		tvCommunity = (TextView) findViewById(R.id.tv_community_input_info);

		new GetCommunityListAsync(userId, userSessionId, this, new UpdateUi() {

			@Override
			public void updateUI(Object ob) {
				communityList = (List<String>) ob;
			}
		}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (!Utils.isFastDoubleClick()) {
			return;
		}
		if (v == btnNext) {

			name = etName.getText().toString().trim();
			idCard = etId.getText().toString().trim();
			community = tvCommunity.getText().toString().trim();

			if (Utils.strNullMeans(name)) {
				Utils.Toast(this, "请输入姓名");
				return;
			}
			if (Utils.strNullMeans(idCard)) {
				Utils.Toast(this, "请输入身份证号");
				return;
			}
			if (!Utils.IdentityJudge(idCard)) {
				Utils.Toast(this, "请输入有效的身份证号");
				return;
			}
			if (Utils.strNullMeans(community)) {
				Utils.Toast(this, "请选择社区");
				return;
			}

			new CheckCardAsync(true, userId, userSessionId, idCard, this, new UpdateUi() {

				@Override
				public void updateUI(Object ob) {
					String result = (String) ob;
					if (!Utils.strNullMeans(result)) {
						if ("1".equals(result)) {
							login();
						} else if ("2".equals(result)) {
							Utils.ShowEnterDialog(InputInfoActivity.this, "该身份证已经被他人使用。", "", "确定", "", "提示", EnterDialog.MODE_NOTITLE_SINGLE, null);
						} else if ("0".equals(result)) {

							Intent intent = new Intent(InputInfoActivity.this, DueDateActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("name", name);
							bundle.putString("idCard", idCard);
							bundle.putString("community", community);
							intent.putExtras(bundle);
							startActivity(intent);

						}
					}

				}
			}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		} else if (v == rlCommunity) {

			if (communityList != null && communityList.size() > 0) {

				chooseCommunityDialog = new ChooseStrDialog(this, "请选择社区", communityList, new UpdateUi() {

					@Override
					public void updateUI(Object ob) {
						String flg = (String) ob;
						if (!Utils.strNullMeans(flg)) {
							tvCommunity.setText(flg + "");
						}
						chooseCommunityDialog.dismiss();
					}
				});
				chooseCommunityDialog.show();

			}
		} else if (v == llReturn) {
			goBack();

		}

	}

	private void goBack() {
		Utils.ShowEnterDialog(this, "确定要退出吗？", "", "确定", "取消", "提示", EnterDialog.MODE_DOUBLE_BUTTON, new UpdateUi() {

			@Override
			public void updateUI(Object ob) {
				int temp = (Integer) ob;
				if (temp == 1) {
					CacheActivityManager.finishSingleActivity(InputInfoActivity.this);
					Intent intent = new Intent(InputInfoActivity.this, LoginActivity.class);
					startActivity(intent);
				}

			}

		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("InputInfoActivity");
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("InputInfoActivity");
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			goBack();
			return false;
		}
		return false;
	}

	String pushId = "";
	String phoneNum = "";
	String psw = "";

	private void login() {

		pushId = JPushInterface.getRegistrationID(getApplicationContext());
		phoneNum = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.LOGINPHONE);
		psw = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.LOGINPSW);

		new LoginAsync(false, phoneNum, psw, pushId, InputInfoActivity.this, 0, new UpdateUi() {

			@Override
			public void updateUI(Object ob) {
				// TODO Auto-generated method stub

				if ("111".equals(ob)) {
					return;
				}
				Utils.saveLocalInfo(InputInfoActivity.this, GlobalInfo.SETTING, GlobalInfo.LOGINPHONE, phoneNum);
				Utils.saveLocalInfo(InputInfoActivity.this, GlobalInfo.SETTING, GlobalInfo.LOGINPSW, psw);
				Utils.saveLocalInfo(InputInfoActivity.this, GlobalInfo.SETTING, GlobalInfo.AUTOLOGIN, "true");

				// Utils.Toast(InputInfoActivity.this, "登录成功");
				Intent intent = new Intent(InputInfoActivity.this, HomeActivity.class);
				startActivity(intent);
				CacheActivityManager.finishSingleActivityByClass(RegisterActivity.class);
				CacheActivityManager.finishSingleActivityByClass(InputInfoActivity.class);
				CacheActivityManager.finishSingleActivityByClass(LoginActivity.class);

			}
		}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
