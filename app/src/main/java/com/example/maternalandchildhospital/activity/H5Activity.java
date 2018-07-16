package com.example.maternalandchildhospital.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.interfaces.MenuListener;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.example.maternalandchildhospital.publics.view.HTML5WebView;
import com.umeng.analytics.MobclickAgent;

/**
 * @author hxc
 *         <p>
 *         H5通用界面
 */
public class H5Activity extends Activity implements OnClickListener {

	private LinearLayout llReturn;
	private ImageView ivBack;

	private TextView tvTitle;
	private String title = "";

	private HTML5WebView mWeb;
	private String url = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_h5);
		if (!CacheActivityManager.activityList.contains(H5Activity.this)) {
			CacheActivityManager.addActivity(H5Activity.this);
		}
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			title = bundle.getString("title");
			url = bundle.getString("url");
		}

		InitView();
	}

	private void InitView() {
		llReturn = (LinearLayout) findViewById(R.id.title_ll_back);
		llReturn.setOnClickListener(this);
		ivBack = (ImageView) findViewById(R.id.title_iv_back);
		ivBack.setImageResource(R.drawable.btn_back2);

		tvTitle = (TextView) findViewById(R.id.title_tv_name);
		tvTitle.setText(title);

		mWeb = (HTML5WebView) findViewById(R.id.h5_web);

		mWeb.loadUrl(url);
		mWeb.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("H5Activity");

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("H5Activity");
	}

	@Override
	public void onClick(View v) {
		if (!Utils.isFastDoubleClick()) {
			return;
		}
		if (v == llReturn) {
			if (mWeb.canGoBack()) {
				mWeb.goBack();
			} else {
				CacheActivityManager.finishSingleActivity(this);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (mWeb.canGoBack()) {
				mWeb.goBack();
			} else {
				CacheActivityManager.finishSingleActivity(this);
			}
			return false;
		}
		return false;
	}

}
