package com.example.maternalandchildhospital.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.activity.HomeActivity;
import com.example.maternalandchildhospital.interfaces.MenuListener;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.example.maternalandchildhospital.publics.view.HTML5WebView;
import com.umeng.analytics.MobclickAgent;

/**
 * @author hxc
 *         <p>
 *         知识
 */
public class FragmentKnowledge extends Fragment implements OnClickListener {

	private View viewMain;

	private LinearLayout llReturn;
	private ImageView ivBack;

	private TextView tvTitle;

	private HTML5WebView mWeb;
	private String url = "http://121.42.28.104/fubaoyuan/html5/fby-news/index.html";

	private MenuListener menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		viewMain = inflater.inflate(R.layout.layout_fragment_knowledge, null);
		menu = (MenuListener) getActivity();
		InitView();
		return viewMain;
	}

	private void InitView() {
		llReturn = (LinearLayout) viewMain.findViewById(R.id.title_ll_back);
		llReturn.setOnClickListener(this);
		ivBack = (ImageView) viewMain.findViewById(R.id.title_iv_back);
		ivBack.setImageResource(R.drawable.btn_menu2);

		tvTitle = (TextView) viewMain.findViewById(R.id.title_tv_name);
		tvTitle.setText("知识");

		mWeb = (HTML5WebView) viewMain.findViewById(R.id.h5_web_frag_knowledge);

		mWeb.loadUrl(url);
		mWeb.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				Utils.Log(url);
				if (mWeb.canGoBack()) {
					ivBack.setImageResource(R.drawable.btn_back2);
				} else {
					ivBack.setImageResource(R.drawable.btn_menu2);
				}
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
			}
		});
		//webview 是否可点击
		mWeb.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (((HomeActivity) getActivity()).isMenuOpen()) {// 侧滑栏打开的时候，禁止主界面点击
					return true;
				} else {
					return false;
				}
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("FragmentKnowledge");

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("FragmentKnowledge");
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
				menu.getMenuListener(0);
			}
		}
	}

	public boolean isGoBack() {
		if (mWeb != null && mWeb.canGoBack()) {
			mWeb.goBack();
			return true;
		}
		return false;
	}

}
