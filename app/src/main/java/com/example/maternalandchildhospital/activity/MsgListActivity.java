package com.example.maternalandchildhospital.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.adapter.MsgAdapter;
import com.example.maternalandchildhospital.async.SeachMessageAsync;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.bean.MsgInfo;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.example.maternalandchildhospital.publics.view.AutoListView;
import com.example.maternalandchildhospital.publics.view.AutoListView.OnLoadListener;
import com.example.maternalandchildhospital.publics.view.AutoListView.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

public class MsgListActivity extends Activity implements OnClickListener, OnRefreshListener, OnLoadListener {

	private LinearLayout llReturn;
	private ImageView ivBack;

	private TextView tvTitle;

	private AutoListView lv;
	private List<MsgInfo> list;
	private MsgAdapter adapter;

	// 数据库那边要求 默认传1
	private int pageNum = 1;

	private int pageSize = 10;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<MsgInfo> result = (List<MsgInfo>) msg.obj;

			switch (msg.what) { // 不管有没有下载成功，lv都要复位
			case AutoListView.REFRESH:
				lv.onRefreshComplete();
				break;
			case AutoListView.LOAD:
				lv.onLoadComplete();
				break;
			default:
				break;
			}
			if (result != null && result.size() > 0) {
				switch (msg.what) {
				case AutoListView.REFRESH:
					lv.onRefreshComplete();
					list.clear();
					list.addAll(result);
					break;
				case AutoListView.LOAD:
					lv.onLoadComplete();
					list.addAll(result);
					break;
				}
			} else {
				if (result != null && msg.what == AutoListView.LOAD) {
					lv.onLoadComplete();
				} else {

					lv.setVisibility(View.GONE);
				}
			}
			if (result != null) {
				lv.setResultSize(result.size());
			} else {
				lv.setResultSize(0);
			}
			adapter.setList(list);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg);
		if (!CacheActivityManager.activityList.contains(MsgListActivity.this)) {
			CacheActivityManager.addActivity(MsgListActivity.this);
		}
		findViews();
		addData(AutoListView.REFRESH);
	}

	private void findViews() {
		llReturn = (LinearLayout) findViewById(R.id.title_ll_back);
		llReturn.setOnClickListener(this);
		ivBack = (ImageView) findViewById(R.id.title_iv_back);
		ivBack.setImageResource(R.drawable.btn_back2);

		tvTitle = (TextView) findViewById(R.id.title_tv_name);
		tvTitle.setText("消息");

		list = new ArrayList<MsgInfo>();

		lv = (AutoListView) findViewById(R.id.lv_msg);
		adapter = new MsgAdapter(list, this);
		lv.setAdapter(adapter);
		lv.setOnRefreshListener(this);
		lv.setOnLoadListener(this);
		lv.setPageSize(pageSize);

		// 将未读消息清零
		GlobalInfo.homePageInfo.setMsgCount("0");
	}

	private void addData(final int type) {

		String userId = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.USERID);
		String userSessionId = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.USERSESSIONID);
		new SeachMessageAsync(false, userId, userSessionId, pageNum + "", pageSize + "", this, new UpdateUi() {

			@Override
			public void updateUI(Object ob) {
				if (ob != null) {
					loadData(type, ob);
				}
			}
		}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MsgListActivity");
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MsgListActivity");
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		if (!Utils.isFastDoubleClick()) {
			return;
		}
		if (v == llReturn) {
			CacheActivityManager.finishSingleActivity(this);
		}

	}

	private void loadData(final int what, Object ob) {

		Message msg = handler.obtainMessage();
		msg.what = what;
		msg.obj = ob;
		handler.sendMessage(msg);

	}

	@Override
	public void onRefresh() {
		GlobalInfo.msgDataLoad = true;
		pageNum = 1;
		addData(AutoListView.REFRESH);
	}

	@Override
	public void onLoad() {
		if (GlobalInfo.msgDataLoad) {
			pageNum += 1;
			addData(AutoListView.LOAD);
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
