package com.example.maternalandchildhospital.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.async.CreateCardAsync;
import com.example.maternalandchildhospital.async.LoginAsync;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * @author hxc
 *         <p>
 *         预产期
 */
public class DueDateActivity extends Activity implements OnClickListener {

	private LinearLayout llReturn;
	private ImageView ivBack;

	private TextView tvTitle;

	private Button btnConfire;

	private RelativeLayout rlDate;
	private TextView tvDate;

	private TextView tvKnow;

	private String userId = "";
	private String userSessionId = "";

	private String name = "";
	private String idCard = "";
	private String community = "";
	private String date = "";
	/** 判断是  预产期还是末经期    0 预产期 1末经期推断*/
	int type = 1;

	InputMethodManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_due_date);
		if (!CacheActivityManager.activityList.contains(DueDateActivity.this)) {
			CacheActivityManager.addActivity(DueDateActivity.this);
		}
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		userId = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.USERID);
		userSessionId = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.USERSESSIONID);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			name = bundle.getString("name");
			idCard = bundle.getString("idCard");
			community = bundle.getString("community");
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
		tvTitle.setText("推算预产期");

		btnConfire = (Button) findViewById(R.id.btn_confire_due_date);
		btnConfire.setOnClickListener(this);

		rlDate = (RelativeLayout) findViewById(R.id.rl_due_date);
		rlDate.setOnClickListener(this);
		tvDate = (TextView) findViewById(R.id.tv_date_due_date);

		tvKnow = (TextView) findViewById(R.id.tv_know_due_date);
		tvKnow.setVisibility(View.VISIBLE);
		tvKnow.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (!Utils.isFastDoubleClick()) {
			return;
		}
		if (v == btnConfire) {

			date = tvDate.getText().toString().trim();
			if (Utils.strNullMeans(date)) {
				Utils.Toast(this, "请输入预产期");
				return;
			}
			// 末经期的话，要推送出预产期传给服务器
			if (type == 1) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date data = sdf.parse(date);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(data);
					calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 279);

					date = sdf.format(calendar.getTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			

			new CreateCardAsync(true, userId, userSessionId, name, idCard, date, community, this, new UpdateUi() {

				@Override
				public void updateUI(Object ob) {
					String result = (String) ob;
					if (!Utils.strNullMeans(result)) {
						login();
					}

				}
			}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		} else if (v == llReturn) {

			if ("输入预产期".equals(tvTitle.getText().toString().trim())) {
				changeUI(false);
			} else {
				CacheActivityManager.finishSingleActivity(this);
			}
		} else if (v == rlDate) {
			
			if ("输入预产期".equals(tvTitle.getText().toString().trim())) {
				type = 0;
			} else {
				type = 1;
			}

			Utils.ShowChooseDateDialog(type, this, tvDate.getHint().toString().trim(), "", new UpdateUi() {

				@Override
				public void updateUI(Object ob) {

					String date = (String) ob;
					tvDate.setText(date);

				}
			});

		} else if (v == tvKnow) {
			changeUI(true);

		}
	}

	/**
	 * @param isTrue
	 *            true 输入 false 推算 输入预产期和推算预产期 切换
	 */
	private void changeUI(boolean isTrue) {

		if (isTrue) {
			tvTitle.setText("输入预产期");
			tvDate.setHint("请选择预产期");
			tvDate.setText("");
			btnConfire.setText("完成");
			tvKnow.setVisibility(View.GONE);
		} else {
			tvTitle.setText("推算预产期");
			tvDate.setHint("请选择末次经期开始的日子");
			tvDate.setText("");
			btnConfire.setText("推算");
			tvKnow.setVisibility(View.VISIBLE);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("DueDateActivity");
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("DueDateActivity");
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if ("输入预产期".equals(tvTitle.getText().toString().trim())) {
				changeUI(false);
			} else {
				CacheActivityManager.finishSingleActivity(this);
			}
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

		new LoginAsync(false, phoneNum, psw, pushId, DueDateActivity.this, 0, new UpdateUi() {

			@Override
			public void updateUI(Object ob) {
				// TODO Auto-generated method stub

				if ("111".equals(ob)) {
					return;
				}
				Utils.saveLocalInfo(DueDateActivity.this, GlobalInfo.SETTING, GlobalInfo.LOGINPHONE, phoneNum);
				Utils.saveLocalInfo(DueDateActivity.this, GlobalInfo.SETTING, GlobalInfo.LOGINPSW, psw);
				Utils.saveLocalInfo(DueDateActivity.this, GlobalInfo.SETTING, GlobalInfo.AUTOLOGIN, "true");

				// Utils.Toast(InputInfoActivity.this, "登录成功");
				Intent intent = new Intent(DueDateActivity.this, HomeActivity.class);
				startActivity(intent);
				CacheActivityManager.finishSingleActivityByClass(RegisterActivity.class);
				CacheActivityManager.finishSingleActivityByClass(InputInfoActivity.class);
				CacheActivityManager.finishSingleActivityByClass(LoginActivity.class);
				CacheActivityManager.finishSingleActivityByClass(DueDateActivity.class);

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
