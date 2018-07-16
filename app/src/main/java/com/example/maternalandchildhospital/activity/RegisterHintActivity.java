package com.example.maternalandchildhospital.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * @author hxc
 *         <p>
 *         服务协议
 */
public class RegisterHintActivity extends Activity implements OnClickListener {

	private LinearLayout llReturn;
	private TextView tvTitle;
	private ImageView ivBack;
	private WebView mWeb;
	private WebSettings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_hint);

		if (!CacheActivityManager.activityList.contains(RegisterHintActivity.this)) {
			CacheActivityManager.addActivity(RegisterHintActivity.this);
		}
		init();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("RegisterHintActivity");
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("RegisterHintActivity");
		MobclickAgent.onPause(this);
	}

	public void init() {

		llReturn = (LinearLayout) findViewById(R.id.title_ll_back);
		llReturn.setOnClickListener(this);

		ivBack = (ImageView) findViewById(R.id.title_iv_back);
		ivBack.setImageResource(R.drawable.btn_back2);

		tvTitle = (TextView) findViewById(R.id.title_tv_name);
		tvTitle.setText("服务协议");

		mWeb = (WebView) findViewById(R.id.webview_register_hint);
		settings = mWeb.getSettings();
		settings.setJavaScriptEnabled(true);

		mWeb.loadUrl("http://121.42.28.104/fubaoyuan/html5/fby-news/licence.html");

		mWeb.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}

		});
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
				CacheActivityManager.finishSingleActivityByClass(RegisterHintActivity.class);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWeb.canGoBack()) {
				mWeb.goBack();
			} else {
				CacheActivityManager.finishSingleActivityByClass(RegisterHintActivity.class);
			}
			return true;
		}
		return false;
	}
}
