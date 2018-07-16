package com.example.maternalandchildhospital.activity;

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

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.async.AddSuggestAsync;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.umeng.analytics.MobclickAgent;

public class AdviceActivity extends Activity implements OnClickListener {
	private LinearLayout llReturn;
	private ImageView ivBack;
	private TextView tvTitle;
	
	private EditText etContent;
	private TextView tvSubmit;

	private InputMethodManager manager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advice_layout);
		if (!CacheActivityManager.activityList.contains(AdviceActivity.this)) {
			CacheActivityManager.addActivity(AdviceActivity.this);
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
		tvTitle.setText("建议反馈");
		
		etContent = (EditText) findViewById(R.id.et_content_advice);
		tvSubmit = (TextView) findViewById(R.id.tv_submit);
		tvSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!Utils.isFastDoubleClick()) {
			return;
		}
		if (v == llReturn) {
			CacheActivityManager.finishSingleActivity(this);
		} else if(v==tvSubmit){
			String content = etContent.getText().toString().trim();
			if (Utils.strNullMeans(content)) {
				Utils.Toast(this, "请填写反馈信息内容");
				return;
			}
			new AddSuggestAsync(true,  GlobalInfo.userInfo.getUserId(), GlobalInfo.userInfo.getUserSessionId(), content, this, new UpdateUi() {
				
				@Override
				public void updateUI(Object ob) {
					String result = (String) ob;
					if ("Successed".equals(result)) {
						Utils.Toast(AdviceActivity.this, "反馈信息发送成功");
						CacheActivityManager.finishSingleActivityByClass(AdviceActivity.class);
					} else {
						Utils.Toast(AdviceActivity.this, "反馈信息发送失败");
					}
				}
			}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("AdviceActivity");
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("AdviceActivity");
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
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);

	}

}
